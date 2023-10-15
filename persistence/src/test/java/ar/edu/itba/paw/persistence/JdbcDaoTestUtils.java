package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.models.user.VerificationCode;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;

public class JdbcDaoTestUtils {
    static UUID ITBA_ID = UUID.fromString("10000000-0000-0000-0000-000000000000");

    static UUID UTN_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    static UUID ING_INF = UUID.fromString("c0000000-0000-0000-0000-000000000000");
    static UUID ING_MEC = UUID.fromString("c0000000-0000-0000-0000-000000000001");
    static UUID EDA_ID = UUID.fromString("50000000-0000-0000-0000-000000000000");
    static UUID EDA_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000000");
    static UUID PAW_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000001");
    static UUID GUIAS_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000005");
    static UUID THEORY_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000007");
    static UUID MVC_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000008");
    static UUID BASURA_ID = UUID.fromString("d0000000-0000-0000-0000-000000000009");
    static UUID MVC_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000002");
    static UUID GUIA1EDA_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000000");
    static UUID PARCIAL_DINAMICA_FLUIDOS_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000004");
    static UUID PEPE_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static UUID SAIDMAN_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");

    static UUID MATE_ID = UUID.fromString("50000000-0000-0000-0000-000000000005");
    static UUID TVM_ID = UUID.fromString("a0000000-0000-0000-0000-000000000006");


    //inserted in test
    static UUID TMP_PARENT_DIR_ID = UUID.fromString("dF000000-0000-0000-0000-000000000000");
    static UUID TMP_NOTE_ID_1 = UUID.fromString("aF000000-0000-0000-0000-000000000001");
    static UUID TMP_NOTE_ID_2 = UUID.fromString("aF000000-0000-0000-0000-000000000002");
    static UUID TMP_NOTE_ID_3 = UUID.fromString("aF000000-0000-0000-0000-000000000003");
    static UUID TMP_NOTE_ID_4 = UUID.fromString("aF000000-0000-0000-0000-000000000004");
    
    static UUID TMP_DIR_ID_1 = UUID.fromString("dF000000-0000-0000-0000-000000000001");
    static UUID TMP_DIR_ID_2 = UUID.fromString("dF000000-0000-0000-0000-000000000002");
    static UUID TMP_DIR_ID_3 = UUID.fromString("dF000000-0000-0000-0000-000000000003");
    static UUID TMP_DIR_ID_4 = UUID.fromString("dF000000-0000-0000-0000-000000000004");
    private JdbcDaoTestUtils() {}

    static UUID insertStudent(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email, String password, UUID careerId, String locale) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);
        args.addValue(PASSWORD, password);
        args.addValue(CAREER_ID, careerId);
        args.addValue(LOCALE, locale);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Users (email, password, career_id, locale) VALUES (:email, :password, :career_id, :locale)",
                args, keyHolder, new String[]{USER_ID});
        UUID newUserId = (UUID) keyHolder.getKeys().get(USER_ID);

        args = new MapSqlParameterSource().addValue(USER_ID, newUserId).addValue(ROLE_NAME, Role.ROLE_STUDENT.getRole());
        namedParameterJdbcTemplate.update("INSERT INTO User_Roles (user_id, role_name) VALUES (:user_id, :role_name)", args);
        return (UUID) keyHolder.getKeys().get(USER_ID);
    }

    static UUID insertLegacyUser(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Users (email) VALUES (:email)",
                args, keyHolder, new String[]{USER_ID});
        return (UUID) keyHolder.getKeys().get(USER_ID);
    }

    // TODO: Modularize?
    static UUID insertAdmin(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email, String password, UUID careerId, String locale) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);
        args.addValue(PASSWORD, password);
        args.addValue(CAREER_ID, careerId);
        args.addValue(LOCALE, locale);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Users (email, password, career_id, locale) VALUES (:email, :password, :career_id, :locale)",
                args, keyHolder, new String[]{USER_ID});
        UUID newUserId = (UUID) keyHolder.getKeys().get(USER_ID);

        args = new MapSqlParameterSource().addValue(USER_ID, newUserId).addValue(ROLE_NAME, Role.ROLE_ADMIN.getRole());
        namedParameterJdbcTemplate.update("INSERT INTO User_Roles (user_id, role_name) VALUES (:user_id, :role_name)", args);
        return (UUID) keyHolder.getKeys().get(USER_ID);
    }

    static void banUser(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UUID userId, UUID adminId, LocalDateTime endDate) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(USER_ID, userId);
        args.addValue(ADMIN_ID, adminId);
        args.addValue(END_DATE, endDate);
        namedParameterJdbcTemplate.update("INSERT INTO Bans (user_id, admin_id, end_date) VALUES (:user_id, :admin_id, :end_date)", args);

        args = new MapSqlParameterSource();
        args.addValue(USER_ID, userId);
        namedParameterJdbcTemplate.update("UPDATE Users SET status = 'BANNED' WHERE user_id = :user_id", args);
    }


    static UUID insertCompleteStudent(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email, String password, UUID careerId, String locale,
                                      String username, String firstName, String lastName) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);
        args.addValue(PASSWORD, password);
        args.addValue(CAREER_ID, careerId);
        args.addValue(LOCALE, locale);
        args.addValue(USERNAME, username);
        args.addValue(FIRST_NAME, firstName);
        args.addValue(LAST_NAME, lastName);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Users (email, password, career_id, locale, username, first_name, last_name) VALUES (:email, :password, :career_id, :locale, :username, :first_name, :last_name)",
                args, keyHolder, new String[]{USER_ID});
        UUID newUserId = (UUID) keyHolder.getKeys().get(USER_ID);

        args = new MapSqlParameterSource().addValue(USER_ID, newUserId).addValue(ROLE_NAME, Role.ROLE_STUDENT.getRole());
        namedParameterJdbcTemplate.update("INSERT INTO User_Roles (user_id, role_name) VALUES (:user_id, :role_name)", args);
        return (UUID) keyHolder.getKeys().get(USER_ID);
    }
    static UUID insertDirectory(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String directoryName, UUID userId, UUID parentId, boolean visible) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(DIRECTORY_NAME, directoryName);
        args.addValue(USER_ID, userId);
        args.addValue(PARENT_ID, parentId);
        args.addValue(VISIBLE, visible);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Directories (directory_name, user_id, parent_id, visible) VALUES (:directory_name, :user_id, :parent_id, :visible)",
                args, keyHolder, new String[]{DIRECTORY_ID});
        return (UUID) keyHolder.getKeys().get(DIRECTORY_ID);
    }

    static UUID insertDirectory(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String directoryName, UUID userId, UUID parentId) {
        return insertDirectory(namedParameterJdbcTemplate, directoryName, userId, parentId, true);
    }

    static UUID insertNote(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UUID parentId, String name, UUID subjectId, UUID userId, boolean visible, byte[] file, String category, String fileType) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_NAME, name);
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(USER_ID, userId);
        args.addValue(PARENT_ID, parentId);
        args.addValue(VISIBLE, visible);
        args.addValue(FILE, file);
        args.addValue(CATEGORY, category.toLowerCase());
        args.addValue(FILE_TYPE, fileType);
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Notes (note_name, subject_id, user_id, parent_id, visible, file, category, file_type)  " +
                        "SELECT :note_name, :subject_id, :user_id, d.directory_id, :visible, :file, :category, :file_type FROM Directories d " +
                        "WHERE d.directory_id = :parent_id AND (d.user_id = :user_id OR d.parent_id IS NULL)"
                , args, holder, new String[]{NOTE_ID});
        return (UUID) holder.getKeys().get(NOTE_ID);
    }


    static void insertReview(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UUID noteId, UUID userId, int score, String content) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_ID, noteId);
        args.addValue(USER_ID, userId);
        args.addValue(SCORE, score);
        args.addValue(CONTENT, content);

        namedParameterJdbcTemplate.update("INSERT INTO Reviews (note_id, user_id, score, content) VALUES (:note_id, :user_id, :score, :content)",
                args);
    }

    static void insertFavorite(SimpleJdbcInsert jdbcFavoriteInsert, UUID directoryId, UUID userId) {
        jdbcFavoriteInsert.execute(new HashMap<String, Object>(){{
            put(DIRECTORY_ID, directoryId);
            put(USER_ID, userId);
        }});
    }

    static void insertVerificationCode(NamedParameterJdbcTemplate namedParameterJdbcTemplate, VerificationCode verificationCode) {
        namedParameterJdbcTemplate.update(
                "INSERT INTO Verification_Codes(user_id, code, expires_at) values(" +
                        "(SELECT user_id FROM Users WHERE email = :email), " +
                        ":code, :expires_at)",
                new MapSqlParameterSource()
                        .addValue("email", verificationCode.getEmail())
                        .addValue("code", verificationCode.getCode())
                        .addValue("expires_at", verificationCode.getExpirationDate()));
    }
    static UUID insertSubject(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String subjectName, UUID rootDirectoryId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(SUBJECT_NAME, subjectName);
        args.addValue(ROOT_DIRECTORY_ID, rootDirectoryId);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Subjects (subject_name, root_directory_id) VALUES (:subject_name, :root_directory_id)",
                args, keyHolder, new String[]{SUBJECT_ID});
        return (UUID) keyHolder.getKeys().get(SUBJECT_ID);
    }

    static UUID insertCareer(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String careerName, UUID institutionId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(CAREER_NAME, careerName);
        args.addValue(INSTITUTION_ID, institutionId);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Careers (career_name, institution_id) VALUES (:career_name, :institution_id)",
                args, keyHolder, new String[]{CAREER_ID});
        return (UUID) keyHolder.getKeys().get(CAREER_ID);
    }

    static UUID insertInstitution(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String institutionName) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(INSTITUTION_NAME, institutionName);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Institutions (institution_name) VALUES (:institution_name)",
                args, keyHolder, new String[]{INSTITUTION_ID});
        return (UUID) keyHolder.getKeys().get(INSTITUTION_ID);
    }

    static void insertSubjectCareer(SimpleJdbcInsert jdbcSubjectsCareersInsert, UUID subjectId, UUID careerId, int year) {
        jdbcSubjectsCareersInsert.execute(new HashMap<String, Object>(){{
            put(SUBJECT_ID, subjectId);
            put(CAREER_ID, careerId);
            put(YEAR, year);
        }});
    }

}
