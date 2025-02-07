package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.search.SearchArguments;

import static ar.edu.itba.paw.models.search.SortArguments.*;
import static ar.edu.itba.paw.models.NameConstants.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Optional;


public class DaoUtils {
    static final EnumMap<SortBy, String> SORTBY = new EnumMap<>(SortBy.class);
    static{
        SORTBY.put(SortBy.DATE, CREATED_AT);
        SORTBY.put(SortBy.MODIFIED, LAST_MODIFIED_AT);
        SORTBY.put(SortBy.NAME, NAME);
        SORTBY.put(SortBy.SCORE, AVG_SCORE);
    }
    static final EnumMap<SortBy, String> SORTBY_CAMELCASE = new EnumMap<>(SortBy.class);
    static{
        SORTBY_CAMELCASE.put(SortBy.DATE, CREATED_AT_CAMELCASE);
        SORTBY_CAMELCASE.put(SortBy.MODIFIED, LAST_MODIFIED_AT_CAMELCASE);
        SORTBY_CAMELCASE.put(SortBy.NAME, NAME);
        SORTBY_CAMELCASE.put(SortBy.SCORE, AVG_SCORE_CAMELCASE);
    }
    static class QueryCreator {
        private final StringBuilder query;
        private final HashMap<String, Object> params = new HashMap<>();

        QueryCreator(String rawQuery) {
            this.query = new StringBuilder(rawQuery);
        }

        void addConditionIfPresent(final String field, final String compareOp, final String logicOp, final Optional<?> value) {
            addConditionIfPresent(null, field, compareOp, logicOp, value);
        }

        void addConditionIfPresent(final String entityAlias, final String field, final String compareOp, final String logicOp, final Optional<?> value) {
            value.ifPresent(val -> {
                query.append(logicOp).append(" ");
                if (entityAlias != null)
                    query.append(entityAlias).append(".");
                query.append(field).append(" ").append(compareOp).append(" :").append(field).append(" ");
                params.put(field, val);
            });
        }

        String createQuery() {
            return query.toString();
        }

        <T> QueryCreator appendIfPresent(Optional<T> opt, final String suffix) {
            if (opt.isPresent())
                query.append(suffix);
            return this;
        }

        QueryCreator append(final String suffix) {
            query.append(suffix);
            return this;
        }

        Object addParameter(final String field, final Object value) {
            return params.put(field, value);
        }

        HashMap<String, Object> getParams() {
            return params;
        }
    }

    static String escapeLikeString(String str) {
        return "%" + str
                .replace("!", "!!") /* Use ! as escape character */
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![")
                + "%";
    }

    private DaoUtils() {} /* Make class non-instantiable */


    static void applyInstitutionalFilters(final QueryCreator queryCreator, final SearchArguments sa) {
        queryCreator.addConditionIfPresent("i", INSTITUTION_ID, "=", "AND", sa.getInstitutionId());
        queryCreator.addConditionIfPresent("c", CAREER_ID, "=", "AND", sa.getCareerId());
        queryCreator.addConditionIfPresent("s", SUBJECT_ID, "=", "AND", sa.getSubjectId());
    }

    /**
     * Applies the category, user_id and word filters to the queryCreator
     * Warning: This method assumes that the queryCreator already has the "WHERE TRUE" clause, also it assumes that the tuple is named 't'
     * @param queryCreator The queryCreator to which the filters will be applied
     * @param sa The search arguments
     */
    static void applyGeneralFilters(final QueryCreator queryCreator, final SearchArguments sa) {
        queryCreator.addConditionIfPresent(CATEGORY, "=", "AND", sa.getCategory().map(Enum::toString));

        queryCreator.addConditionIfPresent("t", USER_ID, "=", "AND", sa.getUserId());

        sa.getWord().ifPresent(w -> {
                    String searchWord = escapeLikeString(w);
                    queryCreator.append("AND LOWER(t.name) LIKE LOWER(:name) ESCAPE '!' ")
                                .addParameter(NAME, searchWord);
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
