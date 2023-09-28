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

    private static final int SEARCH_WORD_ARGS = 4;
    private static final int NAVIGATION_WORD_ARGS = 1;

    private static final String SEARCH_WORD_CONDITIONS = "AND (LOWER(t.name) LIKE LOWER(?) ESCAPE '!' OR LOWER(t.institution_name) LIKE LOWER(?) ESCAPE '!' OR LOWER(t.career_name) LIKE LOWER(?) ESCAPE '!' OR LOWER(t.subject_name) LIKE LOWER(?) ESCAPE '!')  ";
    private static final String NAVIGATION_WORD_CONDITIONS = "AND LOWER(t.name) LIKE LOWER(?) ESCAPE '!' ";

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
                rs.getBoolean(VISIBLE),
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
                rs.getBoolean(VISIBLE),
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
                    rs.getBoolean(VISIBLE),
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
                rs.getBoolean(VISIBLE),
                rs.getString(FILE_TYPE),
                rs.getFloat(AVG_SCORE)
        );
    };

    @Override
    public List<Searchable> search(SearchArguments sa) {
        StringBuilder query = new StringBuilder(
                "SELECT DISTINCT t.id, t.name, t.parent_id, t.category, t.created_at, t.last_modified_at, t.visible," +
                        "t.avg_score, t.file_type, " +
                        "t.icon_color, " +
                        "t.user_id, t.email, " +
                        "t.subject_id, t.subject_name, t.root_directory_id " +
                        "FROM Search t " +
                        "WHERE TRUE "
        );
        List<Object> args = new ArrayList<>();

        applyInstitutionFilters(query, args, sa);

        applyGeneralFilters(query, args, sa, SEARCH_WORD_CONDITIONS, SEARCH_WORD_ARGS);
        applyPagination(query, sa);
        return jdbcTemplate.query(query.toString(), args.toArray(), SEARCH_ROW_MAPPER);
    }

    @Override
    public int countSearchResults(SearchArguments sa) {
        StringBuilder query = new StringBuilder("SELECT COUNT(DISTINCT t.id) FROM Search t WHERE TRUE ");
        List<Object> args = new ArrayList<>();

        applyInstitutionFilters(query, args, sa);
        applyGeneralFilters(query, args, sa, SEARCH_WORD_CONDITIONS, SEARCH_WORD_ARGS);
        return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
    }

    @Override
    public List<Searchable> getNavigationResults(SearchArguments sa, UUID parentId){
        StringBuilder query = new StringBuilder(
                "SELECT DISTINCT t.id, t.name, t.parent_id, t.category, t.created_at, t.last_modified_at, t.visible, " +
                        "t.avg_score, t.file_type, " +
                        "t.icon_color, " +
                        "t.user_id, t.email " +
                        "FROM Navigation t " +
                        "WHERE t.parent_id = ? "
        );
        List<Object> args = new ArrayList<>();
        args.add(parentId);

        applyGeneralFilters(query, args, sa, NAVIGATION_WORD_CONDITIONS, NAVIGATION_WORD_ARGS);
        applyPagination(query, sa);
        return jdbcTemplate.query(query.toString(), args.toArray(), NAVIGATION_ROW_MAPPER);
    }

    @Override
    public int countNavigationResults(SearchArguments sa, UUID parentId){
        StringBuilder query = new StringBuilder("SELECT COUNT(DISTINCT t.id) FROM Navigation t WHERE t.parent_id = ? ");
        List<Object> args = new ArrayList<>();
        args.add(parentId);

        applyGeneralFilters(query, args, sa, NAVIGATION_WORD_CONDITIONS, NAVIGATION_WORD_ARGS);
        return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
    }

    private void applyInstitutionFilters(StringBuilder query, List<Object> args, SearchArguments sa) {
        addIfPresent(query, args,  INSTITUTION_ID, "=", "AND", sa.getInstitutionId());
        addIfPresent(query, args,  CAREER_ID, "=", "AND", sa.getCareerId());
        addIfPresent(query, args,  SUBJECT_ID, "=", "AND", sa.getSubjectId());

    }

    private void applyGeneralFilters(StringBuilder query, List<Object> args, SearchArguments sa, String wordCondition, int conditionCount) {
        sa.getCategory().ifPresent(c -> {
            if (c == Category.NOTE) {
                addIfPresent(query, args, CATEGORY, "!=", "AND", Optional.of(Category.DIRECTORY.toString().toLowerCase()));
            } else {
                addIfPresent(query, args, CATEGORY, "=", "AND", sa.getCategory().map(Enum::toString).map(String::toLowerCase));
            }
        });

        addIfPresent(query, args, USER_ID, "=", "AND", sa.getUserId());

        sa.getWord().ifPresent(w -> {
                    String searchWord = "%" + w
                            .replace("!", "!!") // Use ! as escape character
                            .replace("%", "!%")
                            .replace("_", "!_")
                            .replace("[", "![")
                            + "%";
                    query.append(wordCondition);
                    for (int i = 0; i < conditionCount; i++)
                        args.add(searchWord);
                }
        );

        if (sa.getCurrentUserId().isPresent()) {
            query.append("AND (t.visible OR t.user_id = ?) ");
            args.add(sa.getCurrentUserId().get());
        } else {
            query.append("AND t.visible ");
        }
    }

    private void applyPagination(StringBuilder query, SearchArguments sa) {
        if (sa.getSortBy() != null) {
            query.append(" ORDER BY ").append(JdbcDaoUtils.SORTBY.getOrDefault(sa.getSortBy(), NAME));
            if (!sa.isAscending()) query.append(" DESC");
        }

        query.append(" LIMIT ").append(sa.getPageSize()).append(" OFFSET ").append((sa.getPage() - 1) * sa.getPageSize());
    }


}
