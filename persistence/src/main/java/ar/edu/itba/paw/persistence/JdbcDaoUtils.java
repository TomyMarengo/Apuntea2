package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.SearchArguments;

import static ar.edu.itba.paw.models.SearchArguments.*;
import static ar.edu.itba.paw.models.NameConstants.*;

import java.security.InvalidParameterException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class JdbcDaoUtils {

    // JPA Attributes
    static final String CAREERS_ATTR = "careers";
    static final String SUBJECTS_ATTR = "subjects";

    static final String FETCH_GRAPH =  "javax.persistence.fetchgraph";

    static final EnumMap<SortBy, String> SORTBY = new EnumMap<>(SearchArguments.SortBy.class);
    static{
        SORTBY.put(SortBy.SCORE, AVG_SCORE);
        SORTBY.put(SortBy.DATE, CREATED_AT);
        SORTBY.put(SortBy.NAME, NAME);
    }

    static class QueryCreator {
        private final StringBuilder query;
        private final HashMap<String, Object> params = new HashMap<>();

        QueryCreator(String rawQuery) {
            this.query = new StringBuilder(rawQuery);
        }

        void addConditionIfPresent(final String field, final String compareOp, final String logicOp, final Optional<?> value) {
            value.ifPresent(val -> {
                query.append(logicOp).append(" ").append(field).append(" ").append(compareOp).append(" :").append(field).append(" ");
                params.put(field, val);
            });
        }

        void addOrderCriteria(final String field, final boolean ascending) {
            query.append(" ORDER BY ").append(field).append(ascending ? " ASC " : " DESC ");
        }

        String createQuery() {
            return query.toString();
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
                .replace("!", "!!") // Use ! as escape character
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![")
                + "%";
    }

    private JdbcDaoUtils() {} // Make class non-instantiable
}
