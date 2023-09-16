package ar.edu.itba.paw.persistence;

import static ar.edu.itba.paw.models.SearchArguments.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

public class JdbcDaoUtils {
    static final String NOTES = "Notes";
    static final String REVIEWS = "Reviews";
    static final String INSTITUTIONS = "Institutions";
    static final String CAREERS = "Careers";
    static final String SUBJECTS = "Subjects";
    static final String DIRECTORIES = "Directories";


    static final String INSTITUTION_ID = "institution_id";
//    static final String NAME = "name";
    static final String NOTE_NAME = "note_name";
    static final String DIRECTORY_NAME = "directory_name";
    static final String INSTITUTION_NAME = "institution_name";
    static final String CAREER_NAME = "career_name";
    static final String SUBJECT_NAME = "subject_name";
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

    static final String SCORE = "score";

    static final String ROOT_DIRECTORY_ID = "root_directory_id";
    static final EnumMap<SortBy, String> SORTBY = new EnumMap<>(SortBy.class);

    static{
        SORTBY.put(SortBy.SCORE, AVG_SCORE);
        SORTBY.put(SortBy.DATE, CREATED_AT);
//        SORTBY.put(SortBy.NAME, NAME);
    }

    static void addIfPresent(StringBuilder query, List<Object> args, String field, String compareOp, String logicOp, Optional<?> value) {
        value.ifPresent(val -> {
            query.append(logicOp).append(" ").append(field).append(" ").append(compareOp).append(" ? ");
            args.add(val);
        });
    }

}
