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
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void testGetSubjectsByCareerId(){
        UUID i1Id = insertInstitution(namedParameterJdbcTemplate, "i1");

        UUID i2Id = insertInstitution(namedParameterJdbcTemplate, "i2");

        UUID career1Id = insertCareer(namedParameterJdbcTemplate, "career1", i1Id);
        UUID career2Id = insertCareer(namedParameterJdbcTemplate, "career2", i1Id);

        UUID career3Id = insertCareer(namedParameterJdbcTemplate, "career3", i2Id);


        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID c1s1Id = insertSubject(namedParameterJdbcTemplate, "c1s1", dirId);
        UUID c1s2Id = insertSubject(namedParameterJdbcTemplate, "c1s2", dirId);
        UUID c1s3Id = insertSubject(namedParameterJdbcTemplate, "c1s3", dirId);

        UUID c2s1Id = insertSubject(namedParameterJdbcTemplate, "c2s1", dirId);
        UUID c2s2Id = insertSubject(namedParameterJdbcTemplate, "c2s2", dirId);

        UUID c12s1Id = insertSubject(namedParameterJdbcTemplate, "c12s1", dirId);
        UUID c12s2Id = insertSubject(namedParameterJdbcTemplate, "c12s1", dirId);

        UUID c3s1Id = insertSubject(namedParameterJdbcTemplate, "c3s1", dirId);
        UUID c3s2Id = insertSubject(namedParameterJdbcTemplate, "c3s2", dirId);

        UUID nos1Id = insertSubject(namedParameterJdbcTemplate, "ns1", dirId);
        UUID nos2Id = insertSubject(namedParameterJdbcTemplate, "ns2", dirId);


        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s1Id, career1Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s2Id, career1Id, 2);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s3Id, career1Id, 3);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c2s1Id, career2Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c2s2Id, career2Id, 2);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s1Id, career1Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s1Id, career2Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s2Id, career1Id, 2);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s2Id, career2Id, 2);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c3s1Id, career3Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c3s2Id, career3Id, 2);

        List<Subject> c1sList = subjectDao.getSubjectsByCareerId(career1Id);
        List<Subject> c2sList = subjectDao.getSubjectsByCareerId(career2Id);
        List<Subject> c3sList = subjectDao.getSubjectsByCareerId(career3Id);

        List<UUID> c1sIds = Stream.of(c1s1Id, c1s2Id, c1s3Id, c12s1Id, c12s2Id).collect(Collectors.toList());
        List<UUID> c2sIds = Stream.of(c2s1Id, c2s2Id, c12s1Id, c12s2Id).collect(Collectors.toList());
        List<UUID> c3sIds = Stream.of(c3s1Id, c3s2Id).collect(Collectors.toList());
        List<UUID> nosIds = Stream.of(nos1Id, nos2Id).collect(Collectors.toList());

        assertEquals(5, c1sList.size());
        assertEquals(4, c2sList.size());
        assertEquals(2, c3sList.size());

        assertTrue(c1sList.stream().allMatch(s -> c1sIds.contains(s.getSubjectId())));
        assertTrue(c2sList.stream().allMatch(s -> c2sIds.contains(s.getSubjectId())));
        assertTrue(c3sList.stream().allMatch(s -> c3sIds.contains(s.getSubjectId())));

        assertTrue(c1sList.stream().noneMatch(s -> nosIds.contains(s.getSubjectId())));
        assertTrue(c2sList.stream().noneMatch(s -> nosIds.contains(s.getSubjectId())));
        assertTrue(c3sList.stream().noneMatch(s -> nosIds.contains(s.getSubjectId())));

        assertTrue(c1sList.stream().noneMatch(s -> c3sIds.contains(s.getSubjectId())));
        assertTrue(c2sList.stream().noneMatch(s -> c3sIds.contains(s.getSubjectId())));

        assertTrue(c3sList.stream().noneMatch(s -> c1sIds.contains(s.getSubjectId())));
        assertTrue(c3sList.stream().noneMatch(s -> c2sIds.contains(s.getSubjectId())));
    }

    @Test
    public void testOrderGetSubjectsByCareerId() {
        List<Subject> subjects = subjectDao.getSubjectsByCareerId(ING_INF);

        for (int i = 0; i < subjects.size() - 2; i++) {
            assertTrue(subjects.get(i).getYear() <= subjects.get(i + 1).getYear());
        }

    }

    @Test
    public void testGetSubjectsByCareerIdComplemented() {
        UUID i1Id = insertInstitution(namedParameterJdbcTemplate, "i1");

        UUID i2Id = insertInstitution(namedParameterJdbcTemplate, "i2");

        UUID career1Id = insertCareer(namedParameterJdbcTemplate, "career1", i1Id);
        UUID career2Id = insertCareer(namedParameterJdbcTemplate, "career2", i1Id);

        UUID career3Id = insertCareer(namedParameterJdbcTemplate, "career3", i2Id);


        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID c1s1Id = insertSubject(namedParameterJdbcTemplate, "c1s1", dirId);
        UUID c1s2Id = insertSubject(namedParameterJdbcTemplate, "c1s2", dirId);
        UUID c1s3Id = insertSubject(namedParameterJdbcTemplate, "c1s3", dirId);

        UUID c2s1Id = insertSubject(namedParameterJdbcTemplate, "c2s1", dirId);
        UUID c2s2Id = insertSubject(namedParameterJdbcTemplate, "c2s2", dirId);

        UUID c12s1Id = insertSubject(namedParameterJdbcTemplate, "c12s1", dirId);
        UUID c12s2Id = insertSubject(namedParameterJdbcTemplate, "c12s2", dirId);

        UUID c3s1Id = insertSubject(namedParameterJdbcTemplate, "c3s1", dirId);
        UUID c3s2Id = insertSubject(namedParameterJdbcTemplate, "c3s2", dirId);

        UUID nos1Id = insertSubject(namedParameterJdbcTemplate, "ns1", dirId);
        UUID nos2Id = insertSubject(namedParameterJdbcTemplate, "ns2", dirId);


        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s1Id, career1Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s2Id, career1Id, 2);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s3Id, career1Id, 3);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c2s1Id, career2Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c2s2Id, career2Id, 2);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s1Id, career1Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s1Id, career2Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s2Id, career1Id, 2);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s2Id, career2Id, 2);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c3s1Id, career3Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c3s2Id, career3Id, 2);

        List<UUID> c1exIds = Stream.of(c1s1Id, c1s2Id, c1s3Id).collect(Collectors.toList());
        List<UUID> c2exIds = Stream.of(c2s1Id, c2s2Id).collect(Collectors.toList());
        List<UUID> c12Ids = Stream.of(c12s1Id, c12s2Id).collect(Collectors.toList());
        List<UUID> c3sIds = Stream.of(c3s1Id, c3s2Id).collect(Collectors.toList());
        List<UUID> nosIds = Stream.of(nos1Id, nos2Id).collect(Collectors.toList());

        List<Subject> c1compList = subjectDao.getSubjectsByCareerIdComplemented(career1Id);
        List<Subject> c2compList = subjectDao.getSubjectsByCareerIdComplemented(career2Id);
        List<Subject> c3compList = subjectDao.getSubjectsByCareerIdComplemented(career3Id);

        assertEquals(2, c1compList.size());
        assertEquals(3, c2compList.size());
        assertEquals(0, c3compList.size()); //career 3 has all subjects from institution2

        assertTrue(c1compList.stream().allMatch(s -> c2exIds.contains(s.getSubjectId())));
        assertTrue(c2compList.stream().allMatch(s -> c1exIds.contains(s.getSubjectId())));

        assertTrue(c1compList.stream().noneMatch(s -> c1exIds.contains(s.getSubjectId()))); // already in career1
        assertTrue(c1compList.stream().noneMatch(s -> c12Ids.contains(s.getSubjectId()))); // already in career1
        assertTrue(c1compList.stream().noneMatch(s -> c3sIds.contains(s.getSubjectId()))); // career3 is from another institution
        assertTrue(c1compList.stream().noneMatch(s -> nosIds.contains(s.getSubjectId()))); // not in any career

        assertTrue(c2compList.stream().noneMatch(s -> c2exIds.contains(s.getSubjectId()))); // already in career2
        assertTrue(c2compList.stream().noneMatch(s -> c12Ids.contains(s.getSubjectId()))); // already in career2
        assertTrue(c2compList.stream().noneMatch(s -> c3sIds.contains(s.getSubjectId()))); // career3 is from another institution
        assertTrue(c2compList.stream().noneMatch(s -> nosIds.contains(s.getSubjectId()))); // not in any career
    }


    @Test
    public void testGetSubjectsByInstitutionId(){
        UUID i1Id = insertInstitution(namedParameterJdbcTemplate, "i1");

        UUID i2Id = insertInstitution(namedParameterJdbcTemplate, "i2");

        UUID career1Id = insertCareer(namedParameterJdbcTemplate, "career1", i1Id);
        UUID career2Id = insertCareer(namedParameterJdbcTemplate, "career2", i1Id);

        UUID career3Id = insertCareer(namedParameterJdbcTemplate, "career3", i2Id);


        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID c1s1Id = insertSubject(namedParameterJdbcTemplate, "c1s1", dirId);
        UUID c1s2Id = insertSubject(namedParameterJdbcTemplate, "c1s2", dirId);
        UUID c1s3Id = insertSubject(namedParameterJdbcTemplate, "c1s3", dirId);

        UUID c2s1Id = insertSubject(namedParameterJdbcTemplate, "c2s1", dirId);
        UUID c2s2Id = insertSubject(namedParameterJdbcTemplate, "c2s2", dirId);

        UUID c12s1Id = insertSubject(namedParameterJdbcTemplate, "c12s1", dirId);
        UUID c12s2Id = insertSubject(namedParameterJdbcTemplate, "c12s1", dirId);

        UUID c3s1Id = insertSubject(namedParameterJdbcTemplate, "c3s1", dirId);
        UUID c3s2Id = insertSubject(namedParameterJdbcTemplate, "c3s2", dirId);

        UUID nos1Id = insertSubject(namedParameterJdbcTemplate, "ns1", dirId);
        UUID nos2Id = insertSubject(namedParameterJdbcTemplate, "ns2", dirId);


        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s1Id, career1Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s2Id, career1Id, 2);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c1s3Id, career1Id, 3);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c2s1Id, career2Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c2s2Id, career2Id, 2);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s1Id, career1Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s1Id, career2Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s2Id, career1Id, 2);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c12s2Id, career2Id, 2);

        insertSubjectCareer(jdbcSubjectsCareersInsert, c3s1Id, career3Id, 1);
        insertSubjectCareer(jdbcSubjectsCareersInsert, c3s2Id, career3Id, 2);

        List<Subject> i1sList = subjectDao.getSubjectsByInstitutionId(i1Id);
        List<Subject> i2sList = subjectDao.getSubjectsByInstitutionId(i2Id);

        List<UUID> i1sIds = Stream.of(c1s1Id, c1s2Id, c1s3Id, c2s1Id, c2s2Id, c12s1Id, c12s2Id).collect(Collectors.toList());
        List<UUID> i2sIds = Stream.of(c3s1Id, c3s2Id).collect(Collectors.toList());
        List<UUID> nosIds = Stream.of(nos1Id, nos2Id).collect(Collectors.toList());

        assertEquals(7, i1sList.size());
        assertEquals(2, i2sList.size());

        assertTrue(i1sList.stream().allMatch(s -> i1sIds.contains(s.getSubjectId())));
        assertTrue(i2sList.stream().allMatch(s -> i2sIds.contains(s.getSubjectId())));

        assertTrue(i1sList.stream().noneMatch(s -> nosIds.contains(s.getSubjectId())));
        assertTrue(i2sList.stream().noneMatch(s -> nosIds.contains(s.getSubjectId())));

        assertTrue(i1sList.stream().noneMatch(s -> i2sIds.contains(s.getSubjectId())));
        assertTrue(i2sList.stream().noneMatch(s -> i1sIds.contains(s.getSubjectId())));
    }

    @Test
    public void testCreateSuccess(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);

        UUID subjectId = subjectDao.create("subject1", dirId);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subjectId + "'"));
    }

    @Test
    public void testLinkSubjectToCareer(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
        UUID careedId = ING_INF;
        boolean result = subjectDao.linkSubjectToCareer(subjectId, careedId, 4);

        assertTrue(result);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careedId + "' AND year = 4"));
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
    public void testUpdateSubjectCareer(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "subject1", dirId);
        UUID careerId = ING_INF;
        insertSubjectCareer(jdbcSubjectsCareersInsert, subjectId, careerId, 3);
        int year3 = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = 3");

        boolean result = subjectDao.updateSubjectCareer(subjectId, careerId, 4);

        assertTrue(result);
        assertEquals(1, year3);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = 4"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = 3"));


    }

    @Test
    public void testUnlinkSubjectFromCareer(){
        UUID dirId = insertDirectory(namedParameterJdbcTemplate, "dir1", null, null);
        UUID subjectId = insertSubject(namedParameterJdbcTemplate, "trash", dirId);
        UUID careerId = ING_INF;
        insertSubjectCareer(jdbcSubjectsCareersInsert, subjectId, careerId, 3);
        boolean inserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "' AND year = 3") == 1;

        boolean result = subjectDao.unlinkSubjectFromCareer(subjectId, careerId);

        assertTrue(result);
        assertTrue(inserted);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS_CAREERS, "subject_id = '" + subjectId + "' AND career_id = '" + careerId + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SUBJECTS, "subject_id = '" + subjectId + "'"));
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

}
