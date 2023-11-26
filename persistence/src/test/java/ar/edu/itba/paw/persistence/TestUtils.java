package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.SubjectCareer;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.note.NoteFile;
import ar.edu.itba.paw.models.note.Review;
import ar.edu.itba.paw.models.note.UserNoteInteraction;
import ar.edu.itba.paw.models.user.*;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

public class TestUtils {
    // Institutions
    static UUID ITBA_ID = UUID.fromString("10000000-0000-0000-0000-000000000000");
    static UUID UTN_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");

    // Careers
    static UUID ING_INF_ID = UUID.fromString("c0000000-0000-0000-0000-000000000000");
    static UUID ING_MEC_ID = UUID.fromString("c0000000-0000-0000-0000-000000000001");

    static UUID ING_SIS_ID = UUID.fromString("c0000000-0000-0000-0000-000000000002");

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
        return insertUser(em, new User.UserBuilder().email(email).password(password).career(em.find(Career.class, careerId)).locale(locale).roles(Collections.singleton(Role.ROLE_STUDENT)));
    }

    static User insertAdmin(EntityManager em, String email, String password, UUID careerId, String locale) {
        return insertUser(em, new User.UserBuilder().email(email).password(password).career(em.find(Career.class, careerId)).locale(locale).roles(Collections.singleton(Role.ROLE_ADMIN)));
    }

    static void banUser(EntityManager em, User user, User admin, String reason, LocalDateTime endDate) {
        Ban ban = new Ban(user, admin, reason, endDate);
        em.persist(ban);
        ban.getUser().setStatus(UserStatus.BANNED);
        em.flush();
    }

    static User insertUser(EntityManager em, User.UserBuilder builder) {
        User user =  builder.status(UserStatus.ACTIVE).build();
        em.persist(user);
        em.flush();
        return user;
    }

    static Image insertImage(EntityManager em, Image image, User user) {
        em.persist(image);
        user.setProfilePicture(image);
        em.flush();
        return image;
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

    static void insertInteraction(EntityManager em, UUID userId, UUID noteId) {
        em.persist(new UserNoteInteraction(em.getReference(User.class, userId), em.getReference(Note.class, noteId)));
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

    static Career insertCareer(EntityManager em, String careerName, UUID institutionId) {
        Career career = new Career(careerName, em.getReference(Institution.class, institutionId));
        em.persist(career);
        em.flush();
        return career;
    }

    static Institution insertInstitution(EntityManager em, String institutionName) {
        Institution institution = new Institution(institutionName);
        em.persist(institution);
        em.flush();
        return institution;
    }

    static void insertSubjectCareer(EntityManager em, UUID subjectId, UUID careerId, int year) {
        SubjectCareer subjectCareer = new SubjectCareer(
                em.getReference(Subject.class, subjectId),
                em.getReference(Career.class, careerId),
                year
        );
        em.persist(subjectCareer);
        em.flush();
    }

    static int countSearchResults(EntityManager em, String condition) {
        return ((BigInteger) em.createNativeQuery("SELECT COUNT(DISTINCT id) FROM Search WHERE (visible = TRUE) " + (condition != null ? "AND " + condition : ""))
                .getSingleResult()).intValue();
    }

    static int countRows(EntityManager em, String table)  {
        return countRows(em, table, null);
    }

    static int countRows(EntityManager em, String table, String condition) {
        return ((BigInteger) em.createNativeQuery("SELECT COUNT(*) FROM " + table + (condition != null ? " WHERE " + condition : ""))
                .getSingleResult()).intValue();
    }
}
