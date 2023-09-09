package ar.edu.itba.apuntea.persistence;

import static ar.edu.itba.apuntea.models.SearchArguments.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

public class JdbcDaoUtils {
    static final String INSTITUTION_ID = "institution_id";
    static final String NAME = "name";
    static final String CAREER_ID = "career_id";
    static final String SUBJECT_ID = "subject_id";
    static final String CATEGORY = "category";
    static final String NOTE_ID = "note_id";
    static final String USER_ID = "user_id";
    static final String CREATED_AT = "created_at";
    static final String AVG_SCORE = "avg_score";
    static final String FILE = "file";
    static final String PARENT_ID = "parent_id";
    static final String DIRECTORY_ID = "directory_id";

    static final EnumMap<SortBy, String> SORTBY = new EnumMap<>(SortBy.class);

    static{
        SORTBY.put(SortBy.SCORE, AVG_SCORE);
        SORTBY.put(SortBy.DATE, CREATED_AT);
        SORTBY.put(SortBy.NAME, NAME);
    }

    static void addIfPresent(StringBuilder query, List<Object> args, String field, String cmpOp, Optional<?> value) {
        value.ifPresent(val -> {
            query.append("AND ").append(field).append(" ").append(cmpOp).append(" ? ");
            args.add(val);
        });
    }

}
