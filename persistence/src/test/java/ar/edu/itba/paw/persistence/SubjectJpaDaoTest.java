package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static ar.edu.itba.paw.models.NameConstants.*;
import static ar.edu.itba.paw.persistence.TestUtils.*;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class SubjectJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private SubjectJpaDao subjectDao;

    private User jaimitoUser;
    private User pepeUser;

    @Before
    public void setUp() {
        jaimitoUser = em.find(User.class, JAIMITO_ID);
        pepeUser = em.find(User.class, PEPE_ID);
    }

    @Test
    public void testGetSubjectById(){
        Directory dir1 = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1"));
        Subject insertedSubject = insertSubject(em, "subject1", dir1.getId());

        Subject foundSubject = subjectDao.getSubjectById(insertedSubject.getSubjectId()).orElse(null);

        assertNotNull(foundSubject);
        assertEquals(insertedSubject.getSubjectId(), foundSubject.getSubjectId());
        assertEquals("subject1", foundSubject.getName());
        assertEquals(dir1.getId(), foundSubject.getRootDirectoryId());
    }

    @Test
    public void testGetSubjectByIdNonExistent(){
        Subject subject = subjectDao.getSubjectById(UUID.randomUUID()).orElse(null);

        assertNull(subject);
    }

    private class TestSubjectsCareersInserts {
        private final UUID i1Id;
        final UUID career1Id, career2Id, career3Id;

        final UUID subject1Id, subject2Id, subject12Id, subject3Id, floatingSubjectId;

        private TestSubjectsCareersInserts() {
            i1Id = insertInstitution(em, "i1").getInstitutionId();

            UUID i2Id = insertInstitution(em, "i2").getInstitutionId();

            career1Id = insertCareer(em, "career1", i1Id).getCareerId();
            career2Id = insertCareer(em, "career2", i1Id).getCareerId();
            career3Id = insertCareer(em, "career3", i2Id).getCareerId();

            UUID dirId = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1")).getId();

            subject1Id = insertSubject(em, "subject1", dirId).getSubjectId();
            subject2Id = insertSubject(em, "subject2", dirId).getSubjectId();
            subject12Id = insertSubject(em, "subject12", dirId).getSubjectId();
            subject3Id = insertSubject(em, "subject3", dirId).getSubjectId();
            floatingSubjectId = insertSubject(em, "floating subject", dirId).getSubjectId();

            insertSubjectCareer(em, subject1Id, career1Id, 1);
            insertSubjectCareer(em, subject2Id, career2Id, 1);
            insertSubjectCareer(em, subject12Id, career1Id, 1);
            insertSubjectCareer(em, subject12Id, career2Id, 1);
            insertSubjectCareer(em, subject3Id, career3Id, 1);
        }
    }

    @Test
    public void testGetSubjectsByCareerId(){
        TestSubjectsCareersInserts test = new TestSubjectsCareersInserts();

        List<Subject> career1SubjectList = subjectDao.getSubjectsByCareerId(test.career1Id);

        assertEquals(2, career1SubjectList.size());
        assertTrue(career1SubjectList.stream().anyMatch(s -> s.getSubjectId().equals(test.subject1Id)));
        assertTrue(career1SubjectList.stream().anyMatch(s -> s.getSubjectId().equals(test.subject12Id)));

        assertTrue(career1SubjectList.stream().noneMatch(s -> s.getSubjectId().equals(test.subject2Id)));
        assertTrue(career1SubjectList.stream().noneMatch(s -> s.getSubjectId().equals(test.subject3Id)));
        assertTrue(career1SubjectList.stream().noneMatch(s -> s.getSubjectId().equals(test.floatingSubjectId)));
    }

    @Test
    public void testOrderGetSubjectsByCareerId() {
        List<Subject> subjects = subjectDao.getSubjectsByCareerId(ING_INF_ID);

        for (int i = 0; i < subjects.size() - 2; i++) {
            assertTrue(subjects.get(i).getYear() <= subjects.get(i + 1).getYear());
        }

    }

    @Test
    public void testGetSubjectByRootDirectoryId(){
        UUID dirId = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1")).getId();
        Subject insertedSubject = insertSubject(em, "subject1", dirId);

        UUID foundSubjectId = subjectDao.getSubjectByRootDirectoryId(dirId);

        assertEquals(insertedSubject.getSubjectId(), foundSubjectId);
    }

    @Test
    public void testGetSubjectsByCareerIdComplemented() {
        TestSubjectsCareersInserts test = new TestSubjectsCareersInserts();

        List<Subject> career1ComplementList = subjectDao.getSubjectsByCareerIdComplemented(test.career1Id);

        assertEquals(1, career1ComplementList.size());
        assertTrue(career1ComplementList.stream().anyMatch(s -> s.getSubjectId().equals(test.subject2Id)));

        assertTrue(career1ComplementList.stream().noneMatch(s -> s.getSubjectId().equals(test.subject1Id)));
        assertTrue(career1ComplementList.stream().noneMatch(s -> s.getSubjectId().equals(test.subject12Id)));
        assertTrue(career1ComplementList.stream().noneMatch(s -> s.getSubjectId().equals(test.subject3Id)));
        assertTrue(career1ComplementList.stream().noneMatch(s -> s.getSubjectId().equals(test.floatingSubjectId)));
    }

    @Test
    public void testGetSubjectsByCareerIdComplementedLoneCareer() {
        TestSubjectsCareersInserts test = new TestSubjectsCareersInserts();

        List<Subject> career3ComplementList = subjectDao.getSubjectsByCareerIdComplemented(test.career3Id);

        assertEquals(0, career3ComplementList.size());
    }

    @Test
    public void testGetSubjectByCareerIdComplementedNonExistentCareer() {
        TestSubjectsCareersInserts test = new TestSubjectsCareersInserts();

        List<Subject> career3ComplementList = subjectDao.getSubjectsByCareerIdComplemented(test.i1Id);

        assertEquals(0, career3ComplementList.size());
    }

    @Test
    public void testGetUserSubjects() {
        Note.NoteBuilder nb = new Note.NoteBuilder()
                .visible(true)
                .category(Category.PRACTICE)
                .fileType("jpg");
        insertNote(em, nb.name("n1").subject(em.getReference(Subject.class, EDA_ID)).parentId(EDA_DIRECTORY_ID).user(jaimitoUser));
        insertDirectory(em, new Directory.DirectoryBuilder().name("d1").parent(em.getReference(Directory.class, PAW_DIRECTORY_ID)).user(jaimitoUser));
        insertNote(em, nb.name("n1").subject(em.getReference(Subject.class, MATE_ID)).parentId(MATE_DIRECTORY_ID).user(pepeUser));

        List<Subject> subjects = subjectDao.getSubjectsByUser(jaimitoUser);

        assertEquals(2, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> s.getSubjectId().equals(EDA_ID)));
        assertTrue(subjects.stream().anyMatch(s -> s.getSubjectId().equals(PAW_ID)));
        assertTrue(subjects.stream().noneMatch(s -> s.getSubjectId().equals(MATE_ID)));
    }

    @Test
    public void testCreateSuccess(){
        UUID dirId = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1")).getId();

        Subject newSubject = subjectDao.create("subject1", dirId);
        em.flush();

        assertEquals(1, countRows(em, SUBJECTS, "subject_id = '" + newSubject.getSubjectId() + "' AND subject_name = 'subject1' AND root_directory_id = '" + dirId + "'"));
    }

    @Test
    public void testDelete() {
        UUID dirId = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1")).getId();
        Subject subject = insertSubject(em, "subject", dirId);
        Subject subject2 = insertSubject(em, "subject2", dirId);
        boolean inserted = countRows(em, SUBJECTS, "subject_id = '" + subject.getSubjectId() + "'") == 1;
        boolean inserted2 = countRows(em, SUBJECTS, "subject_id = '" + subject2.getSubjectId() + "'") == 1;

        boolean result = subjectDao.delete(subject.getSubjectId());
        em.flush();

        assertTrue(result);
        assertTrue(inserted);
        assertTrue(inserted2);
        assertEquals(0, countRows(em, SUBJECTS, "subject_id = '" + subject.getSubjectId() + "'"));
        assertEquals(1, countRows(em, SUBJECTS, "subject_id = '" + subject2.getSubjectId() + "'"));
    }

    @Test
    public void testLinkSubjectToCareer(){
        UUID dirId = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1")).getId();
        Subject subject = insertSubject(em, "subject1", dirId);
        UUID careerId = ING_INF_ID;
        int year = 4;
        subjectDao.linkSubjectToCareer(subject, careerId, year);
        em.flush();
        assertEquals(1, countRows(em, SUBJECTS_CAREERS, "subject_id = '" + subject.getSubjectId() + "' AND career_id = '" + careerId + "' AND year = " + year));
    }

    @Test
    public void testLinkSubjectToCareerOtherInstitution() {
        UUID institution1Id = insertInstitution(em, "i1").getInstitutionId();
        UUID institution2Id = insertInstitution(em, "i2").getInstitutionId();

        UUID career1Id = insertCareer(em, "career1", institution1Id).getCareerId();
        UUID career2Id = insertCareer(em, "career2", institution2Id).getCareerId();

        UUID dirId = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1")).getId();
        Subject subject = insertSubject(em, "subject1", dirId);
        insertSubjectCareer(em, subject.getSubjectId(), career1Id, 1);

        boolean success = subjectDao.linkSubjectToCareer(subject, career2Id, 1);

        assertFalse(success);
        assertEquals(0, countRows(em, SUBJECTS_CAREERS, "subject_id = '" + subject.getSubjectId() + "' AND career_id = '" + career2Id + "'"));
    }

    @Test
    public void testUpdateSubjectCareer(){
        UUID dirId = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1")).getId();
        UUID subjectId = insertSubject(em, "subject1", dirId).getSubjectId();
        UUID careerId = ING_INF_ID;
        int oldYear = 3;
        int newYear = 4;
        insertSubjectCareer(em, subjectId, careerId, oldYear);
        int oldCount = countRows(em, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = "+oldYear);

        boolean result = subjectDao.updateSubjectCareer(subjectId, careerId, newYear);
        em.flush();

        assertTrue(result);
        assertEquals(1, oldCount);
        assertEquals(1, countRows(em, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = "+newYear));
        assertEquals(0, countRows(em, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = "+oldYear));
    }

    @Test
    public void testUnlinkSubjectFromCareer(){
        Directory dir = insertDirectory(em, new Directory.DirectoryBuilder().name("dir1").user(null).parent(null));

        UUID subjectId = insertSubject(em, "trash", dir.getId()).getSubjectId();
        UUID careerId = ING_INF_ID;
        int year = 3;
        insertSubjectCareer(em, subjectId, careerId, year);
        boolean inserted = countRows(em, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = " + year) == 1;

        boolean result = subjectDao.unlinkSubjectFromCareer(subjectId, careerId);
        em.flush();

        assertTrue(result);
        assertTrue(inserted);
        assertEquals(0, countRows(em, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "'"));
        assertEquals(1, countRows(em, SUBJECTS, "subject_id = '" + subjectId + "'"));
    }

}
