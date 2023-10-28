package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static ar.edu.itba.paw.models.NameConstants.*;

@Repository
public class SearchJpaDao implements SearchDao {
    @PersistenceContext
    private EntityManager em;

    private static final String WORD_CONDITION = "AND LOWER(t.name) LIKE LOWER(:name) ESCAPE '!' ";

    @Override
    public List<Pair<UUID, Category>> search(SearchArguments sa) {
        QueryCreator queryCreator = new QueryCreator("SELECT DISTINCT CAST(id as VARCHAR(36)), category ")
                .append(JdbcDaoUtils.SORTBY.getOrDefault(sa.getSortBy(), NAME))
                .append(" FROM Search t WHERE TRUE ");

        applyInstitutionFilters(queryCreator, sa);
        applyGeneralFilters(queryCreator, sa);

        queryCreator.append("ORDER BY category, ").append(JdbcDaoUtils.SORTBY.getOrDefault(sa.getSortBy(), NAME)).append(sa.isAscending() ? "" : " DESC ");

        Query query = em.createNativeQuery(queryCreator.createQuery())
                .setFirstResult(sa.getPageSize() * (sa.getPage() - 1))
                .setMaxResults(sa.getPageSize());

       queryCreator.getParams().forEach(query::setParameter);
       return ((List<Object[]>) query.getResultList())
               .stream()
               .map(o -> new Pair<>(UUID.fromString((String) o[0]), Category.valueOf(((String) o[1]).toUpperCase())))
               .collect(Collectors.toList());
    }

    @Override
    public int countSearchResults(SearchArguments sa) {
        QueryCreator queryCreator = new QueryCreator("SELECT COUNT(DISTINCT t.id) FROM Search t WHERE TRUE ");

        applyInstitutionFilters(queryCreator, sa);
        applyGeneralFilters(queryCreator, sa);

        Query query = em.createNativeQuery(queryCreator.createQuery());
        queryCreator.getParams().forEach(query::setParameter);

        return ((BigInteger)query.getSingleResult()).intValue();
    }

    @Override
    public List<Searchable> getNavigationResults(SearchArguments sa, UUID parentId) {
        List<Object> args = new ArrayList<>();

//        StringBuilder query = new StringBuilder(
//                "SELECT DISTINCT t.id, t.name, t.parent_id, t.category, t.created_at, t.last_modified_at, t.visible, " +
//                        "t.avg_score, t.file_type, " +
//                        "t.icon_color, " +
//                        "t.user_id, t.email, " +
//                        " ( f.directory_id IS NOT NULL ) AS favorite " +
//                        "FROM Navigation t " +
//                        "LEFT JOIN Favorites f ON t.category = 'directory' AND t.id = f.directory_id AND f.user_id = ? " +
//                        "WHERE t.parent_id = ? ");
//
//        args.add(sa.getCurrentUserId().orElse(null));
//        args.add(parentId);
//
//        applyGeneralFilters(query, args, sa, NAVIGATION_WORD_CONDITIONS, NAVIGATION_WORD_ARGS);
//        applyPagination(query, sa);
//        return jdbcTemplate.query(query.toString(), args.toArray(), NAVIGATION_ROW_MAPPER);
        return Collections.emptyList();
    }

    @Override
    public int countNavigationResults(SearchArguments sa, UUID parentId){
//        StringBuilder query = new StringBuilder("SELECT COUNT(DISTINCT t.id) FROM Navigation t WHERE t.parent_id = ? ");
//        List<Object> args = new ArrayList<>();
//        args.add(parentId);
//
//        applyGeneralFilters(query, args, sa, NAVIGATION_WORD_CONDITIONS, NAVIGATION_WORD_ARGS);
//        return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
        return 0;
    }

    @Override
    public Optional<UUID> findByName(UUID parentId, String name, UUID currentUserId) {
//       return jdbcTemplate.query("SELECT id FROM Navigation WHERE name = ? AND parent_id = ? AND user_id = ?",
//                new Object[]{name, parentId, currentUserId}, (rs, rowNum) -> UUID.fromString(rs.getString(ID)))
//                .stream()
//                .findFirst();
        return Optional.empty();
    }

    @Override
    public int countChildren(UUID parentId) {
        //return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Navigation WHERE parent_id = ?", new Object[]{parentId}, Integer.class);
        return 0;
    }

    private void applyInstitutionFilters(QueryCreator queryCreator, SearchArguments sa) {
        queryCreator.addConditionIfPresent(INSTITUTION_ID, "=", "AND", sa.getInstitutionId());
        queryCreator.addConditionIfPresent(CAREER_ID, "=", "AND", sa.getCareerId());
        queryCreator.addConditionIfPresent(SUBJECT_ID, "=", "AND", sa.getSubjectId());
    }

    private void applyGeneralFilters(QueryCreator queryCreator, SearchArguments sa) {
        sa.getCategory().ifPresent(c -> {
            if (c == Category.NOTE) {
                queryCreator.addConditionIfPresent(CATEGORY, "!=", "AND", Optional.of(Category.DIRECTORY.toString().toLowerCase()));
            } else {
                queryCreator.addConditionIfPresent(CATEGORY, "=", "AND", sa.getCategory().map(Enum::toString).map(String::toLowerCase));
            }
        });

        queryCreator.addConditionIfPresent(USER_ID, "=", "AND", sa.getUserId());

        sa.getWord().ifPresent(w -> {
                    String searchWord = escapeLikeString(w);
                    queryCreator.append(WORD_CONDITION);
                    queryCreator.addParameter(NAME, searchWord);
                }
        );

        if (sa.getCurrentUserId().isPresent()) {
            queryCreator.append("AND (t.visible OR t.user_id = :currentUserId) ");
            queryCreator.addParameter(CURRENT_USER_ID, sa.getCurrentUserId().get());
        } else {
            queryCreator.append("AND t.visible ");
        }
    }

}
