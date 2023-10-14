package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import ar.edu.itba.paw.models.SearchArguments.SearchArgumentsBuilder;

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.FAVORITES;
import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.EDA_ID;
import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class SearchJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private SearchJdbcDao searchDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcFavoriteInsert;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcFavoriteInsert = new SimpleJdbcInsert(ds)
                .withTableName(FAVORITES);
    }

    /* Note tests */
    @Test
    public void testSearchNotesByInstitution() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).category(Category.NOTE.toString());
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(7, notes.size());
    }

    @Test
    public void testSearchNotesByCareer(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_INF).category(Category.NOTE.toString());
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(5, notes.size());
    }

    @Test
    public void testSearchNotesBySubject(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_INF).subjectId(EDA_ID).category(Category.NOTE.toString());
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(2, notes.size());
    }
    @Test
    public void testByCategory(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_INF).subjectId(EDA_ID).category(Category.PRACTICE.toString());
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(1, notes.size());
    }

    @Test
    public void testSearchNotesOrderBy(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().sortBy("name").ascending(true);
        List<Searchable> notes = searchDao.search(sab.build());
        for (int i = 0; i < notes.size() - 2; i++) {
            assertTrue(notes.get(i).getName().toUpperCase().compareTo(notes.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testSearchNotesOrderByScore(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.NOTE.toString()).sortBy("score").ascending(true);
        List<Searchable> notes = searchDao.search(sab.build());
        for (int i = 0; i < notes.size() - 2; i++) {
            assertTrue(notes.get(i).getAvgScore() <= notes.get(i + 1).getAvgScore());
        }
    }

    @Test
    public void testByPage() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().page(1).pageSize(2);
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(2, notes.size());
    }

    @Test
    public void testSearchNotesByWord() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.NOTE.toString()).word("guIA");
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(2, notes.size());
    }

    @Test
    public void testSearchNotesMultipleCareerSubject() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_MEC).subjectId(MATE_ID).category(Category.NOTE.toString());
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(1, notes.size());
        assertEquals(TVM_ID ,notes.get(0).getId());
    }

    @Test
    public void testSearchNotesMultipleCareerSubjectBis() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_MEC).subjectId(MATE_ID).category(Category.NOTE.toString());
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(1, notes.size());
        assertEquals(TVM_ID, notes.get(0).getId());
    }

    /* Directory tests */
//    @Test
//    public void testSearchDirectoriesByInstitution() {
//        SearchArgumentsBuilder sab = new SearchArgumentsBuilder(ITBA_ID, null, null, Category.DIRECTORY.toString(), null, "name", true, 1, 10);
//        List<Searchable> directories = searchDao.search(sab.build());
//        assertEquals(5, directories.size());
//    }
//
//    @Test
//    public void testSearchDirectoriesByCareer(){
//        SearchArgumentsBuilder sab = new SearchArgumentsBuilder(ITBA_ID, ING_INF, null, Category.DIRECTORY.toString(), null, "name", true, 1, 10);
//        List<Searchable> directories = searchDao.search(sab.build());
//        assertEquals(3, directories.size());
//    }
//
//    @Test
//    public void testSearchDirectoriesBySubject(){
//        SearchArgumentsBuilder sab = new SearchArgumentsBuilder(ITBA_ID, ING_INF, EDA_ID, Category.DIRECTORY.toString(), null, "name", true, 1, 10);
//        List<Searchable> directories = searchDao.search(sab.build());
//        assertEquals(1, directories.size());
//    }

    @Test
    public void testSearchDirectoriesOrderBy(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).sortBy("date").ascending(false);
        List<Searchable> directories = searchDao.search(sab.build());
        for (int i = 0; i < directories.size() - 2; i++) {
            assertFalse(directories.get(i).getCreatedAt().isBefore(directories.get(i + 1).getCreatedAt()));
        }
    }

//    @Test
//    public void testSearchDirectoriesByPage() {
//        SearchArgumentsBuilder sab = new SearchArgumentsBuilder(null, null, null, Category.DIRECTORY.toString(), null, "name", true, 1, 2);
//        List<Searchable> directories = searchDao.search(sab.build());
//        assertEquals(2, directories.size());
//    }

    // TODO: Make generic
//    @Test
//    public void testChildren() {
//        List<Directory> directories = directoryDao.getChildren(EDA_DIRECTORY_ID);
//        assertEquals(2, directories.size());
//        assertEquals("Guias", directories.get(0).getName());
//        assertEquals("1eros parciales", directories.get(1).getName());
//    }

//    @Test
//    public void testSearchDirectoriesByWord() {
//        SearchArgumentsBuilder sab = new SearchArgumentsBuilder(null, null, null, Category.DIRECTORY.toString(), "can", "name", true, 1, 10);
//        List<Searchable> directories = searchDao.search(sab.build());
//        assertEquals(3, directories.size());
//        assertEquals("Dinamica de Fluidos", directories.get(0).getName());
//        assertEquals("Matematica I", directories.get(1).getName());
//        assertEquals("Mecanica Gral", directories.get(2).getName());
//    }

    @Test
    public void testEscapePercentage() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).word("%");
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(1, notes.size());
    }

    @Test
    public void testEscapeUnderscore() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).word("_");
        List<Searchable> notes = searchDao.search(sab.build());
        assertEquals(1, notes.size());
    }

    @Test
    public void testNavigation() {
        List<Searchable> notes = searchDao.getNavigationResults(new SearchArgumentsBuilder().build(), EDA_DIRECTORY_ID);
        assertEquals(4, notes.size());
    }

    @Test
    public void testNavigationByWord() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().word("guia");
        List<Searchable> notes = searchDao.getNavigationResults(sab.build(), EDA_DIRECTORY_ID);
        assertEquals(2, notes.size());
    }

    @Test
    public void testSearchListWithFavorites() {
        UUID newDir1 = insertDirectory(namedParameterJdbcTemplate, "temp", PEPE_ID, EDA_DIRECTORY_ID);
        UUID newDir2 = insertDirectory(namedParameterJdbcTemplate, "temp2", PEPE_ID, EDA_DIRECTORY_ID);
        UUID newDir3 = insertDirectory(namedParameterJdbcTemplate, "temp3", PEPE_ID, EDA_DIRECTORY_ID);
        insertFavorite(jdbcFavoriteInsert, newDir1, PEPE_ID);
        insertFavorite(jdbcFavoriteInsert, newDir2, SAIDMAN_ID);
        insertFavorite(jdbcFavoriteInsert, newDir3, PEPE_ID);

        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).currentUserId(PEPE_ID);
        List<Searchable> directories = searchDao.search(sab.build());
        Searchable d1 = directories.stream().filter(d -> d.getId().equals(newDir1)).findAny().orElseThrow(AssertionError::new);
        Searchable d2 = directories.stream().filter(d -> d.getId().equals(newDir2)).findAny().orElseThrow(AssertionError::new);
        Searchable d3 = directories.stream().filter(d -> d.getId().equals(newDir3)).findAny().orElseThrow(AssertionError::new);
        assertEquals(newDir1, d1.getId());
        assertTrue(d1.getFavorite());
        assertTrue(d3.getFavorite());
        assertFalse(d2.getFavorite());
    }


    private static class TestCountChildrenObject {
        private UUID rootDirId;
        private UUID child1Id, child2Id, child3Id, child4Id;
        private UUID grandchild11Id,grandchild12Id;
        private UUID grandchild21Id, grandchild22Id;
        private UUID ggchild111Id;
        private TestCountChildrenObject(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            rootDirId = insertDirectory(namedParameterJdbcTemplate, "root", null, null);
            child1Id = insertDirectory(namedParameterJdbcTemplate, "child1", PEPE_ID, rootDirId);
            child2Id = insertDirectory(namedParameterJdbcTemplate, "child2", PEPE_ID, rootDirId);
            child3Id = insertDirectory(namedParameterJdbcTemplate, "child3", PEPE_ID, rootDirId);
            child4Id = insertNote(namedParameterJdbcTemplate, rootDirId,"child4", EDA_ID,PEPE_ID, true, new byte[]{0}, "other", "pdf");

            grandchild11Id = insertDirectory(namedParameterJdbcTemplate, "grandchild11", PEPE_ID, child1Id);
            grandchild12Id = insertDirectory(namedParameterJdbcTemplate, "grandchild12", PEPE_ID, child1Id);

            grandchild21Id = insertNote(namedParameterJdbcTemplate, child2Id,"grandchild21", EDA_ID,PEPE_ID, true, new byte[]{0}, "other", "pdf");
            grandchild22Id = insertNote(namedParameterJdbcTemplate, child2Id, "grandchild22", EDA_ID,PEPE_ID, true, new byte[]{0}, "other", "pdf");

            ggchild111Id = insertDirectory(namedParameterJdbcTemplate, "ggchild111", PEPE_ID, grandchild11Id);
        }
    }


    @Test
    public void testCountChildrenMixed() {
        TestCountChildrenObject test = new TestCountChildrenObject(namedParameterJdbcTemplate);
        assertEquals(4, searchDao.countChildren(test.rootDirId));
    }

    @Test
    public void testCountChildrenAllDirectories() {
        TestCountChildrenObject test = new TestCountChildrenObject(namedParameterJdbcTemplate);
        assertEquals(2, searchDao.countChildren(test.child1Id));
    }

    @Test
    public void testCountChildrenAllNotes() {
        TestCountChildrenObject test = new TestCountChildrenObject(namedParameterJdbcTemplate);
        assertEquals(2, searchDao.countChildren(test.child2Id));
    }

    @Test
    public void testCountChildrenEmpty() {
        TestCountChildrenObject test = new TestCountChildrenObject(namedParameterJdbcTemplate);
        assertEquals(0, searchDao.countChildren(test.child3Id));
    }

    @Test
    public void testCountChildrenGrandChildren() {
        TestCountChildrenObject test = new TestCountChildrenObject(namedParameterJdbcTemplate);
        assertEquals(1, searchDao.countChildren(test.grandchild11Id));
    }

    @Test
    public void testCountChildrenNotADirectory() {
        TestCountChildrenObject test = new TestCountChildrenObject(namedParameterJdbcTemplate);
        assertEquals(0, searchDao.countChildren(test.child4Id));
    }


}
