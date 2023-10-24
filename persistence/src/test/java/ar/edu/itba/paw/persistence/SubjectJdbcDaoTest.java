package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import static ar.edu.itba.paw.models.NameConstants.*;
import static ar.edu.itba.paw.persistence.TestUtils.*;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class SubjectJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private SubjectJdbcDao subjectDao;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert jdbcSubjectsCareersInsert;
    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcSubjectsCareersInsert = new SimpleJdbcInsert(ds)
                .withTableName(SUBJECTS_CAREERS)
                .usingColumns(SUBJECT_ID, CAREER_ID, YEAR);
    }

    @Test
    public void testGetSubjectById(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);

        Subject subject = subjectDao.getSubjectById(subjectId).orElse(null);

        assertNotNull(subject);
        assertEquals(subjectId, subject.getSubjectId());
        assertEquals("subject1", subject.getName());
        assertEquals(dirId, subject.getRootDirectoryId());
    }

    @Test
    public void testGetSubjectByIdNonExistent(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);

        Subject subject = subjectDao.getSubjectById(dirId).orElse(null);

        assertNull(subject);
    }

    private class TestSubjectsCareersInserts {
        private final UUID i1Id;
        final UUID career1Id, career2Id, career3Id;

        final UUID subject1Id, subject2Id, subject12Id, subject3Id, floatingSubjectId;

        private TestSubjectsCareersInserts() {
            i1Id = insertInstitution(namedParameterJdbcTemplate, "i1");

            UUID i2Id = insertInstitution(namedParameterJdbcTemplate, "i2");

            career1Id = insertCareer(namedParameterJdbcTemplate, "career1", i1Id);
            career2Id = insertCareer(namedParameterJdbcTemplate, "career2", i1Id);
            career3Id = insertCareer(namedParameterJdbcTemplate, "career3", i2Id);

            UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);

            subject1Id = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
            subject2Id = insertSubject(namedParameterJdbcTemplate, "subject2", dirId);
            subject12Id = insertSubject(namedParameterJdbcTemplate, "subject12", dirId);
            subject3Id = insertSubject(namedParameterJdbcTemplate, "subject3", dirId);
            floatingSubjectId = insertSubject(namedParameterJdbcTemplate, "floating subject", dirId);

            insertSubjectCareer(jdbcSubjectsCareersInsert, subject1Id, career1Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject2Id, career2Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject12Id, career1Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject12Id, career2Id, 1);
            insertSubjectCareer(jdbcSubjectsCareersInsert, subject3Id, career3Id, 1);
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
    public void testCreateSuccess(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);

        UUID subjectId = subjectDao.create("subject1", dirId);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subjectId + "' AND subject_name = 'subject1' AND root_directory_id = '" + dirId + "'"));
    }

    @Test
    public void testDelete() {
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject", dirId);
        UUID subject2Id = insertSubject(namedParameterJdbcTemplate, "subject2", dirId);
        boolean inserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subjectId + "'") == 1;
        boolean inserted2 = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subject2Id + "'") == 1;

        boolean result = subjectDao.delete(subjectId);

        assertTrue(result);
        assertTrue(inserted);
        assertTrue(inserted2);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subjectId + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subject2Id + "'"));
    }

    @Test
    public void testUpdateSubject(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);

        boolean result = subjectDao.updateSubject(subjectId, "subject2");

        assertTrue(result);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subjectId + "' AND subject_name = 'subject2'"));
    }

    @Test
    public void testLinkSubjectToCareer(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
        UUID careerId = ING_INF_ID;
        int year = 4;
        boolean result = subjectDao.linkSubjectToCareer(subjectId, careerId, year);

        assertTrue(result);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = " + year));
    }

    @Test
    public void testLinkSubjectToCareerOtherInstitution() {
        UUID institution1Id = insertInstitution(namedParameterJdbcTemplate, "i1");
        UUID institution2Id = insertInstitution(namedParameterJdbcTemplate, "i2");

        UUID career1Id = insertCareer(namedParameterJdbcTemplate, "career1", institution1Id);
        UUID career2Id = insertCareer(namedParameterJdbcTemplate, "career2", institution2Id);

        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
        insertSubjectCareer(jdbcSubjectsCareersInsert, subjectId, career1Id, 1);

        boolean result = subjectDao.linkSubjectToCareer(subjectId, career2Id, 1);

        assertFalse(result);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + career2Id + "'"));
    }

    @Test
    public void testUpdateSubjectCareer(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
        UUID careerId = ING_INF_ID;
        int oldYear = 3;
        int newYear = 4;
        insertSubjectCareer(jdbcSubjectsCareersInsert, subjectId, careerId, oldYear);
        int oldCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = "+oldYear);

        boolean result = subjectDao.updateSubjectCareer(subjectId, careerId, newYear);

        assertTrue(result);
        assertEquals(1, oldCount);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = "+newYear));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = "+oldYear));


    }

    @Test
    public void testUnlinkSubjectFromCareer(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "trash", dirId);
        UUID careerId = ING_INF_ID;
        int year = 3;
        insertSubjectCareer(jdbcSubjectsCareersInsert, subjectId, careerId, year);
        boolean inserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = " + year) == 1;

        boolean result = subjectDao.unlinkSubjectFromCareer(subjectId, careerId);

        assertTrue(result);
        assertTrue(inserted);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subjectId + "'"));
    }

}
