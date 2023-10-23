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

    static final String SUBJECTS_CAREERS = "Subjects_Careers";
    static final String DIRECTORIES = "Directories";
    static final String FAVORITES = "Favorites";
    static final String USERS = "Users";
    static final String USER_ROLES = "User_Roles";
    static final String VERIFICATION_CODES = "Verification_Codes";

    static final String SEARCH = "Search";

    static final String NAVIGATION = "Navigation";

    static final String INSTITUTION_DATA = "Institution_Data";

    static final String INSTITUTION_ID = "institution_id";
    static final String ID = "id";
    static final String NAME = "name";
    static final String NOTE_NAME = "note_name";
    static final String DIRECTORY_NAME = "directory_name";
    static final String INSTITUTION_NAME = "institution_name";
    static final String CAREER_NAME = "career_name";
    static final String SUBJECT_NAME = "subject_name";
    static final String ROLE_NAME = "role_name";
    static final String CAREER_ID = "career_id";
    static final String SUBJECT_ID = "subject_id";
    static final String CATEGORY = "category";
    static final String NOTE_ID = "note_id";
    static final String USER_ID = "user_id";
    static final String ADMIN_ID = "admin_id";
    static final String PASSWORD = "password";
    static final String EMAIL = "email";
    static final String USERNAME = "username";
    static final String FIRST_NAME = "first_name";
    static final String LAST_NAME = "last_name";
    static final String CONTENT = "content";
    static final String CREATED_AT = "created_at";
    static final String AVG_SCORE = "avg_score";
    static final String VISIBLE = "visible";
    static final String FILE = "file";
    static final String IMAGE_ID = "image_id";
    static final String IMAGE = "image";
    static final String PARENT_ID = "parent_id";
    static final String DIRECTORY_ID = "directory_id";
    static final String SCORE = "score";
    static final String ROOT_DIRECTORY_ID = "root_directory_id";
    static final EnumMap<SortBy, String> SORTBY = new EnumMap<>(SortBy.class);
    static final String OWNER_ID = "owner_id";
    static final String OWNER_EMAIL = "owner_email";

    static final String OWNER_LOCALE = "owner_locale";
    static final String FAVORITE = "favorite";
    static final String LAST_MODIFIED_AT = "last_modified_at";
    static final String ICON_COLOR = "icon_color";
    static final String LOCALE = "locale";
    static final String FILE_TYPE = "file_type";
    static final String ROLES = "roles";
    static final String STATUS = "status";

    static final String YEAR = "year";
    static final String END_DATE = "end_date";

    // JPA Attributes
    static final String CAREERS_ATTR = "careers";
    static final String SUBJECTS_ATTR = "subjects";

    static final String FETCH_GRAPH =  "javax.persistence.fetchgraph";

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
