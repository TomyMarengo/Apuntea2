package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.SearchArguments;

import static ar.edu.itba.paw.models.SearchArguments.*;
import static ar.edu.itba.paw.models.NameConstants.*;
import java.util.EnumMap;
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
    static void addIfPresent(StringBuilder query, List<Object> args, String field, String compareOp, String logicOp, Optional<?> value) {
        value.ifPresent(val -> {
            query.append(logicOp).append(" ").append(field).append(" ").append(compareOp).append(" ? ");
            args.add(val);
        });
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
