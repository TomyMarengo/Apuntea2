package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.user.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static ar.edu.itba.paw.models.NameConstants.*;

public class TestUtils {
    // Institutions
    static UUID ITBA_ID = UUID.fromString("10000000-0000-0000-0000-000000000000");
    static UUID UTN_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");

    // Careers
    static UUID ING_INF_ID = UUID.fromString("c0000000-0000-0000-0000-000000000000");
    static Career ING_INF = new Career(ING_INF_ID, "");
    static UUID ING_MEC_ID = UUID.fromString("c0000000-0000-0000-0000-000000000001");
    static Career ING_MEC = new Career(ING_MEC_ID, "");

    // Subjects
    static UUID EDA_ID = UUID.fromString("50000000-0000-0000-0000-000000000000");
    static UUID PAW_ID = UUID.fromString("50000000-0000-0000-0000-000000000001");
    static UUID MATE_ID = UUID.fromString("50000000-0000-0000-0000-000000000005");

    // Notes, directories
    static UUID EDA_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000000");
    static UUID PAW_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000001");
    static UUID MATE_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-00000000000b");

    static UUID GUIAS_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000005");
    static UUID THEORY_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000007");
    static UUID MVC_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000008");
    static UUID BASURA_ID = UUID.fromString("d0000000-0000-0000-0000-000000000009");
    static UUID MVC_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000002");
    static UUID GUIA1EDA_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000000");
    static UUID LUCENE_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000001");
    static UUID JAVA_BEANS_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000005");
    static UUID PARCIAL_DINAMICA_FLUIDOS_NOTE_ID = UUID.fromString("a0000000-0000-0000-0000-000000000004");

    // Users
    static UUID PEPE_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static UUID JAIMITO_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    static UUID CARLADMIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    static UUID SAIDMAN_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");

    static String PSVM = "private static void main";

    static UUID TVM_ID = UUID.fromString("a0000000-0000-0000-0000-000000000006");

    static String VERIFICATION_EMAIL = "verification@itba.edu.ar";
    static String ADMIN_EMAIL = "admin@apuntea.com";
    static String DEFAULT_CODE = "123456";
    static String DEFAULT_CODE2 = "123457";

    private TestUtils() {}

    static User insertStudent(EntityManager em, String email, String password, UUID careerId, String locale) {
        return insertUser(em, email, password, careerId, locale, Role.ROLE_STUDENT);
    }

    static User insertAdmin(EntityManager em, String email, String password, UUID careerId, String locale) {
        return insertUser(em, email, password, careerId, locale, Role.ROLE_ADMIN);
    }

    static void banUser(EntityManager em, User user, User admin, String reason, LocalDateTime endDate) {
        Ban ban = new Ban(user, admin, reason, endDate);
        em.persist(ban);
        ban.getUser().setStatus(UserStatus.BANNED);
        em.flush();
    }

    private static User insertUser(EntityManager em, String email, String password, UUID careerId, String locale, Role role) {
        Career career = em.find(Career.class, careerId);
        User user = new User.UserBuilder()
                .email(email)
                .password(password)
                .career(career)
                .locale(locale)
                .status(UserStatus.ACTIVE)
                .roles(Collections.singleton(role))
                .build();
        em.persist(user);
        em.flush();
        return user;
    }

    static VerificationCode insertVerificationCode(EntityManager em, String code, User user, LocalDateTime expirationDate) {
        VerificationCode vc = new VerificationCode(code, user, expirationDate);
        em.persist(vc);
        em.flush();
        return vc;
    }

    static Note insertNote(EntityManager em, Note.NoteBuilder builder) {
        return insertNote(em, builder, new byte[]{1});
    }

    static Note insertNote(EntityManager em, Note.NoteBuilder builder, byte [] file) {
        Note note = builder.build();
        em.persist(note);
        NoteFile nf = new NoteFile(file, note); // Empty file for testing
        nf.setNote(note);
        em.persist(nf);
        em.flush();
        return note;
    }

    static Directory insertDirectory(EntityManager em, Directory.DirectoryBuilder builder) {
        Directory directory = builder.build();
        em.persist(directory);
        em.flush();
        return directory;
    }

    static void insertReview(EntityManager em, Note note, User user, int score, String content) {
        Review review = new Review(note, user, score, content);
        em.persist(review);
        em.flush();
    }

    static void insertFavoriteDirectory(EntityManager em, UUID directoryId, UUID userId) {
        em.find(User.class, userId).getDirectoryFavorites().add(em.find(Directory.class, directoryId));
        em.flush();
    }

    static void insertFavoriteNote(EntityManager em, UUID noteId, UUID userId) {
        em.find(User.class, userId).getNoteFavorites().add(em.find(Note.class, noteId));
        em.flush();
    }

    static void insertFollower(EntityManager em, User follower, User followed) {
        follower.getUsersFollowing().add(followed);
        em.flush();
    }

    static Subject insertSubject(EntityManager em, String name, UUID rootDirectoryId) {
        Subject subject = new Subject(name, em.getReference(Directory.class, rootDirectoryId));
        em.persist(subject);
        em.flush();
        return subject;
    }

    /*----------------------------------------------------------------------------------------------------*/

    static UUID jdbcInsertStudent(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email, String password, UUID careerId, String locale) {
        return jdbcInsertUser(namedParameterJdbcTemplate, email, password, careerId, locale, Role.ROLE_STUDENT);
    }

    static UUID jdbcInsertLegacyUser(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Users (email) VALUES (:email)",
                args, keyHolder, new String[]{USER_ID});
        return (UUID) keyHolder.getKeys().get(USER_ID);
    }

    static UUID jdbcInsertAdmin(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email, String password, UUID careerId, String locale) {
        return jdbcInsertUser(namedParameterJdbcTemplate, email, password, careerId, locale, Role.ROLE_ADMIN);
    }

    private static UUID jdbcInsertUser(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String email, String password, UUID careerId, String locale, Role role) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(EMAIL, email);
        args.addValue(PASSWORD, password);
        args.addValue(CAREER_ID, careerId);
        args.addValue(LOCALE, locale);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Users (email, password, career_id, locale) VALUES (:email, :password, :career_id, :locale)",
                args, keyHolder, new String[]{USER_ID});
        UUID newUserId = (UUID) keyHolder.getKeys().get(USER_ID);

        args = new MapSqlParameterSource().addValue(USER_ID, newUserId).addValue(ROLE_NAME, role.getRole());
        namedParameterJdbcTemplate.update("INSERT INTO User_Roles (user_id, role_name) VALUES (:user_id, :role_name)", args);
        return (UUID) keyHolder.getKeys().get(USER_ID);
    }

    static Image insertImage(EntityManager em, Image image, User user) {
        em.persist(image);
        user.setProfilePicture(image);
        em.flush();
        return image;
    }

    static UUID jdbcInsertImage(NamedParameterJdbcTemplate namedParameterJdbcTemplate, byte[] image, UUID userId) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(IMAGE, image);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Images (image) VALUES (:image)",
                args, keyHolder, new String[]{IMAGE_ID});

        if(userId != null) {
            args = new MapSqlParameterSource().addValue(USER_ID, userId).addValue(IMAGE_ID, keyHolder.getKeys().get(IMAGE_ID));
            namedParameterJdbcTemplate.update("UPDATE Users SET profile_picture_id = :image_id WHERE user_id = :user_id", args);
        }

        return (UUID) keyHolder.getKeys().get(IMAGE_ID);

    }


    static void jdbcBanUser(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UUID userId, UUID adminId, LocalDateTime endDate) {
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

    static UUID jdbcInsertDirectory(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String directoryName, UUID userId, UUID parentId, boolean visible, String iconColor) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(DIRECTORY_NAME, directoryName);
        args.addValue(USER_ID, userId);
        args.addValue(PARENT_ID, parentId);
        args.addValue(VISIBLE, visible);
        args.addValue(ICON_COLOR, iconColor);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("INSERT INTO Directories (directory_name, user_id, parent_id, visible, icon_color) VALUES (:directory_name, :user_id, :parent_id, :visible, :icon_color)",
                args, keyHolder, new String[]{DIRECTORY_ID});
        return (UUID) keyHolder.getKeys().get(DIRECTORY_ID);
    }

    static UUID jdbcInsertDirectory(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String directoryName, UUID userId, UUID parentId, boolean visible) {
        return jdbcInsertDirectory(namedParameterJdbcTemplate, directoryName, userId, parentId, visible, "BBBBBB");
    }

    static UUID jdbcInsertDirectory(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String directoryName, UUID userId, UUID parentId) {
        return jdbcInsertDirectory(namedParameterJdbcTemplate, directoryName, userId, parentId, true);
    }

    static UUID jdbcInsertNote(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UUID parentId, String name, UUID subjectId, UUID userId, boolean visible, byte[] file, String category, String fileType) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_NAME, name);
        args.addValue(SUBJECT_ID, subjectId);
        args.addValue(USER_ID, userId);
        args.addValue(PARENT_ID, parentId);
        args.addValue(VISIBLE, visible);
        args.addValue(CATEGORY, category.toLowerCase());
        args.addValue(FILE_TYPE, fileType);
        KeyHolder holder = new GeneratedKeyHolder();
        // TODO: Fix this, and don't forget to add the file
        namedParameterJdbcTemplate.update("INSERT INTO Notes (note_name, subject_id, user_id, parent_id, visible, category, file_type)  " +
                        "SELECT :note_name, :subject_id, :user_id, d.directory_id, :visible, :category, :file_type FROM Directories d " +
                        "WHERE d.directory_id = :parent_id AND (d.user_id = :user_id OR d.parent_id IS NULL)"
                , args, holder, new String[]{NOTE_ID});
        return (UUID) holder.getKeys().get(NOTE_ID);
    }


    static void jdbcInsertReview(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UUID noteId, UUID userId, int score, String content) {
        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValue(NOTE_ID, noteId);
        args.addValue(USER_ID, userId);
        args.addValue(SCORE, score);
        args.addValue(CONTENT, content);

        namedParameterJdbcTemplate.update("INSERT INTO Reviews (note_id, user_id, score, content) VALUES (:note_id, :user_id, :score, :content)",
                args);
    }

    static void jdbcInsertFavorite(SimpleJdbcInsert jdbcFavoriteInsert, UUID directoryId, UUID userId) {
        jdbcFavoriteInsert.execute(new HashMap<String, Object>(){{
            put(DIRECTORY_ID, directoryId);
            put(USER_ID, userId);
        }});
    }

    static void jdbcInsertVerificationCode(NamedParameterJdbcTemplate namedParameterJdbcTemplate, VerificationCode verificationCode) {
        namedParameterJdbcTemplate.update(
                "INSERT INTO Verification_Codes(user_id, code, expires_at) values(" +
                        "(SELECT user_id FROM Users WHERE email = :email), " +
                        ":code, :expires_at)",
                new MapSqlParameterSource()
                        .addValue("email", verificationCode.getEmail())
                        .addValue("code", verificationCode.getCode())
                        .addValue("expires_at", verificationCode.getExpirationDate()));
    }
    static UUID jdbcInsertSubject(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String subjectName, UUID rootDirectoryId) {
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

    static int countSearchResults(JdbcTemplate jdbcTemplate, String condition) {
        String query = "SELECT COUNT(DISTINCT id) FROM Search WHERE visible = TRUE";
        if (condition != null) {
            query += " AND " + condition;
        }
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    static int countNavigationResults(JdbcTemplate jdbcTemplate, String condition) {
        String query = "SELECT COUNT(DISTINCT id) FROM Navigation WHERE visible = TRUE";
        if (condition != null) {
            query += " AND " + condition;
        }
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

}
