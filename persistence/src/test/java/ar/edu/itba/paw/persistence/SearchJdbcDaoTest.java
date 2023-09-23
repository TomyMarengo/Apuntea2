package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static ar.edu.itba.paw.persistence.JdbcTestConstants.*;
import static ar.edu.itba.paw.persistence.JdbcTestConstants.EDA_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class SearchJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private SearchDao searchDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    /* Note tests */
    @Test
    public void testSearchNotesByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, Category.NOTE.toString(), null, "score", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        assertEquals(6, notes.size());
    }

    @Test
    public void testSearchNotesByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, Category.NOTE.toString(), null, "score", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        assertEquals(4, notes.size());
    }

    @Test
    public void testSearchNotesBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, Category.NOTE.toString(), null, "score", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        assertEquals(2, notes.size());
    }
    @Test
    public void testByCategory(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, "practice", null, "score", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        assertEquals(1, notes.size());
    }

    @Test
    public void testSearchNotesOrderBy(){
        SearchArguments sa = new SearchArguments(null, null, null, Category.NOTE.toString(), null, "name", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        for (int i = 0; i < notes.size() - 2; i++) {
            assertTrue(notes.get(i).getName().toUpperCase().compareTo(notes.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testSearchNotesOrderByScore(){
        SearchArguments sa = new SearchArguments(null, null, null, Category.NOTE.toString(), null, "score", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        for (int i = 0; i < notes.size() - 2; i++) {
            assertTrue(notes.get(i).getAvgScore() <= notes.get(i + 1).getAvgScore());
        }
    }

    // TODO: Uncomment when pagination is implemented
//    @Test
//    public void testByPage() {
//        SearchArguments sa = new SearchArguments(null, null, null, null, null, null, true, 1, 2);
//        List<Note> notes = noteDao.search(sa);
//        assertEquals(2, notes.size());
//    }

    @Test
    public void testSearchNotesByWord() {
        SearchArguments sa = new SearchArguments(null, null, null, Category.NOTE.toString(), "guIA", null, true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        assertEquals(2, notes.size());
    }

    @Test
    public void testSearchNotesMultipleCareerSubject() {
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, MATE_ID, Category.NOTE.toString(), null, "score", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        assertEquals(1, notes.size());
        assertEquals(TVM_ID ,notes.get(0).getId());
    }

    @Test
    public void testSearchNotesMultipleCareerSubjectBis() {
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_MEC , MATE_ID, Category.NOTE.toString(), null, "score", true, 1, 10);
        List<Searchable> notes = searchDao.search(sa);
        assertEquals(1, notes.size());
        assertEquals(TVM_ID, notes.get(0).getId());
    }

    /* Directory tests */
    @Test
    public void testSearchDirectoriesByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, Category.DIRECTORY.toString(), null, "name", true, 1, 10);
        List<Searchable> directories = searchDao.search(sa);
        assertEquals(5, directories.size());
    }

    @Test
    public void testSearchDirectoriesByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, Category.DIRECTORY.toString(), null, "name", true, 1, 10);
        List<Searchable> directories = searchDao.search(sa);
        assertEquals(3, directories.size());
    }

    @Test
    public void testSearchDirectoriesBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, Category.DIRECTORY.toString(), null, "name", true, 1, 10);
        List<Searchable> directories = searchDao.search(sa);
        assertEquals(1, directories.size());
    }

    @Test
    public void testSearchDirectoriesOrderBy(){
        SearchArguments sa = new SearchArguments(null, null, null, Category.DIRECTORY.toString(), null, "name", true, 1, 10);
        List<Searchable> directories = searchDao.search(sa);
        for (int i = 0; i < directories.size() - 2; i++) {
            assertTrue(directories.get(i).getName().toUpperCase().compareTo(directories.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testSearchDirectoriesByPage() {
        SearchArguments sa = new SearchArguments(null, null, null, Category.DIRECTORY.toString(), null, "name", true, 1, 2);
        List<Searchable> directories = searchDao.search(sa);
        assertEquals(2, directories.size());
    }

    // TODO: Make generic
//    @Test
//    public void testChildren() {
//        List<Directory> directories = directoryDao.getChildren(EDA_DIRECTORY_ID);
//        assertEquals(2, directories.size());
//        assertEquals("Guias", directories.get(0).getName());
//        assertEquals("1eros parciales", directories.get(1).getName());
//    }

    @Test
    public void testSearchDirectoriesByWord() {
        SearchArguments sa = new SearchArguments(null, null, null, Category.DIRECTORY.toString(), "can", "name", true, 1, 10);
        List<Searchable> directories = searchDao.search(sa);
        assertEquals(3, directories.size());
        assertEquals("Dinamica de Fluidos", directories.get(0).getName());
        assertEquals("Matematica I", directories.get(1).getName());
        assertEquals("Mecanica Gral", directories.get(2).getName());
    }


}
