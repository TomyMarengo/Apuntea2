package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

@Repository
public class SearchJdbcDao implements SearchDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public SearchJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    private final static RowMapper<Searchable> SEARCH_ROW_MAPPER = (rs, rowNum) -> {
        Category c = Category.valueOf(rs.getString(CATEGORY).toUpperCase());
        if (c.equals(Category.DIRECTORY)) {
            return new Directory(
                UUID.fromString(rs.getString(ID)),
                rs.getString(NAME),
                new User(
                        UUID.fromString(rs.getString(USER_ID)),
                        rs.getString(EMAIL)
                ),
                UUID.fromString(rs.getString(PARENT_ID)),
                new Subject(
                        UUID.fromString(rs.getString(SUBJECT_ID)),
                        rs.getString(SUBJECT_NAME),
                        UUID.fromString(rs.getString(ROOT_DIRECTORY_ID))
                ),
                rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                rs.getTimestamp(LAST_MODIFIED_AT).toLocalDateTime(),
                rs.getString(ICON_COLOR)
            );

        }
        return new Note(
                UUID.fromString(rs.getString(ID)),
                rs.getString(NAME),
                new User(
                        UUID.fromString(rs.getString(USER_ID)),
                        rs.getString(EMAIL)
                ),
                UUID.fromString(rs.getString(PARENT_ID)),
                new Subject(
                        UUID.fromString(rs.getString(SUBJECT_ID)),
                        rs.getString(SUBJECT_NAME),
                        UUID.fromString(rs.getString(ROOT_DIRECTORY_ID))
                ),
                Category.valueOf(rs.getString(CATEGORY).toUpperCase()),
                rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                rs.getTimestamp(LAST_MODIFIED_AT).toLocalDateTime(),
                rs.getString(FILE_TYPE),
                rs.getFloat(AVG_SCORE)
            );
    };

    private final static RowMapper<Searchable> NAVIGATION_ROW_MAPPER = (rs, rowNum) -> {
        Category c = Category.valueOf(rs.getString(CATEGORY).toUpperCase());
        if (c.equals(Category.DIRECTORY)) {
            return new Directory(
                    UUID.fromString(rs.getString(ID)),
                    rs.getString(NAME),
                    new User(
                            UUID.fromString(rs.getString(USER_ID)),
                            rs.getString(EMAIL)
                    ),
                    UUID.fromString(rs.getString(PARENT_ID)),
                    rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                    rs.getTimestamp(LAST_MODIFIED_AT).toLocalDateTime(),
                    rs.getString(ICON_COLOR)
            );

        }
        return new Note(
                UUID.fromString(rs.getString(ID)),
                rs.getString(NAME),
                new User(
                        UUID.fromString(rs.getString(USER_ID)),
                        rs.getString(EMAIL)
                ),
                UUID.fromString(rs.getString(PARENT_ID)),
                Category.valueOf(rs.getString(CATEGORY).toUpperCase()),
                rs.getTimestamp(CREATED_AT).toLocalDateTime(),
                rs.getTimestamp(LAST_MODIFIED_AT).toLocalDateTime(),
                rs.getString(FILE_TYPE),
                rs.getFloat(AVG_SCORE)
        );
    };

    @Override
    public List<Searchable> search(SearchArguments sa) {
        StringBuilder query = new StringBuilder(
                "SELECT DISTINCT t.id, t.name, t.parent_id, t.category, t.created_at, t.last_modified_at, " +
                        "t.avg_score, t.file_type, " +
                        "t.icon_color, " +
                        "u.user_id, u.email, " +
                        "s.subject_id, s.subject_name, s.root_directory_id " +
                        "FROM Search t " +
                        "INNER JOIN Subjects s ON t.subject_id = s.subject_id " +
                        "INNER JOIN Subjects_Careers sc ON s.subject_id = sc.subject_id " +
                        "INNER JOIN Careers c ON sc.career_id = c.career_id " +
                        "INNER JOIN Institutions i ON c.institution_id = i.institution_id " +
                        "INNER JOIN Users u ON t.user_id = u.user_id " +
                        "WHERE true "
        );
        List<Object> args = new ArrayList<>();

        JdbcDaoUtils.addIfPresent(query, args, "i."  + INSTITUTION_ID, "=", "AND", sa.getInstitutionId());
        addIfPresent(query, args, "c." + CAREER_ID, "=", "AND", sa.getCareerId());
        addIfPresent(query, args, "s." + SUBJECT_ID, "=", "AND", sa.getSubjectId());

        applyFiltersAndPagination(query, args, sa, "AND (LOWER(t.name) LIKE LOWER(?) ESCAPE '!' OR LOWER(i.institution_name) LIKE LOWER(?) ESCAPE '!' OR LOWER(c.career_name) LIKE LOWER(?) ESCAPE '!' OR LOWER(s.subject_name) LIKE LOWER(?) ESCAPE '!')  ");
        return jdbcTemplate.query(query.toString(), args.toArray(), SEARCH_ROW_MAPPER);
    }

    @Override
    public List<Searchable> getNavigationResults(SearchArguments sa) {
        StringBuilder query = new StringBuilder(
                "SELECT DISTINCT t.id, t.name, t.parent_id, t.category, t.created_at, t.last_modified_at, " +
                        "t.avg_score, t.file_type, " +
                        "t.icon_color, " +
                        "u.user_id, u.email " +
                        "FROM Navigation t  " +
                        "INNER JOIN Users u ON t.user_id = u.user_id " +
                        "WHERE t.parent_id = ? "
        );
        List<Object> args = new ArrayList<>();
        args.add(sa.getParentId());

        applyFiltersAndPagination(query, args, sa, "AND (LOWER(t.name) LIKE LOWER(?) ESCAPE '!'");
        return jdbcTemplate.query(query.toString(), args.toArray(), NAVIGATION_ROW_MAPPER);
    }

    private void applyFiltersAndPagination(StringBuilder query, List<Object> args, SearchArguments sa, String wordCondition) {
        // TODO: Remove optional for category
        sa.getCategory().ifPresent(c -> {
            if (c == Category.NOTE) {
                addIfPresent(query, args, CATEGORY, "!=", "AND", Optional.of(Category.DIRECTORY.toString().toLowerCase()));
            } else {
                addIfPresent(query, args, CATEGORY, "=", "AND", sa.getCategory().map(Enum::toString).map(String::toLowerCase));
            }
        });

        sa.getWord().ifPresent(w -> {
                    String searchWord = "%" + w
                            .replace("!", "!!") // Use ! as escape character
                            .replace("%", "!%")
                            .replace("_", "!_")
                            .replace("[", "![")
                            + "%";
                    query.append(wordCondition);
                    for (int i = 0; i < 4; i++)
                        args.add(searchWord);
                }
        );

        if (sa.getSortBy() != null) {
            query.append(" ORDER BY ").append(JdbcDaoUtils.SORTBY.getOrDefault(sa.getSortBy(), NAME));
            if (!sa.isAscending()) query.append(" DESC");
        }

        query.append(" LIMIT ").append(sa.getPageSize()).append(" OFFSET ").append((sa.getPage() - 1) * sa.getPageSize());
    }


}
