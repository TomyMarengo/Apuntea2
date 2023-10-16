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
import java.util.Optional;
import java.util.UUID;

import ar.edu.itba.paw.models.SearchArguments.SearchArgumentsBuilder;

import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.EDA_ID;
import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
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

    private int allResultsPageSize;
    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcFavoriteInsert = new SimpleJdbcInsert(ds)
                .withTableName(FAVORITES);
        allResultsPageSize = countSearchResults(jdbcTemplate, null);
    }

    @Test
    public void testCountSearchResults() {
        int searchResults = searchDao.countSearchResults(new SearchArgumentsBuilder().build());
        assertEquals(searchResults, allResultsPageSize);
    }

    @Test
    public void testCountSearchResultsWithFilters() {
        int expectedResults = countSearchResults(jdbcTemplate, " institution_id = '" + ITBA_ID + "' AND career_id = '" + ING_INF + "' AND subject_id = '" + EDA_ID + "' AND category = '" + Category.PRACTICE.name() + "'");

        int searchResults = searchDao.countSearchResults(new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_INF).subjectId(EDA_ID).category(Category.PRACTICE.name()).build());

        assertEquals(expectedResults, searchResults);
    }

    @Test
    public void testSearchByInstitution() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(11, results.size());
    }

    @Test
    public void testSearchNotesByCareer(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_INF).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(9, results.size());
    }

    @Test
    public void testSearchNotesBySubject(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder()
                                            .institutionId(ITBA_ID)
                                            .careerId(ING_INF)
                                            .subjectId(EDA_ID)
                                            .pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(4, results.size());
        assertTrue(results.stream().allMatch(n -> n.getSubject().getSubjectId().equals(EDA_ID)));
    }

    @Test
    public void testByCategory(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.PRACTICE.toString()).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(n -> n.getCategory().equals(Category.PRACTICE)));
    }

    @Test
    public void testSearchNotes() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.NOTE.toString()).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(8, results.size());
        assertTrue(results.stream().noneMatch(n -> n.getCategory().equals(Category.DIRECTORY)));
    }

    @Test
    public void testSearchDirectory() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(6, results.size());
        assertTrue(results.stream().allMatch(n -> n.getCategory().equals(Category.DIRECTORY)));
    }

    @Test
    public void testSearchOrderByName(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().sortBy(SearchArguments.SortBy.NAME.name()).ascending(true).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        for (int i = 0; i < results.size() - 2; i++) {
            assertTrue(results.get(i).getName().toUpperCase().compareTo(results.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testSearchOrderByScore(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().sortBy(SearchArguments.SortBy.SCORE.name()).ascending(true).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        for (int i = 0; i < results.size() - 2; i++) {
            assertTrue(results.get(i).getAvgScore() <= results.get(i + 1).getAvgScore());
        }
    }

    @Test
    public void testSearchOrderByDate(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().sortBy(SearchArguments.SortBy.DATE.name()).ascending(false);
        List<Searchable> results = searchDao.search(sab.build());
        for (int i = 0; i < results.size() - 2; i++) {
            assertFalse(results.get(i).getCreatedAt().isBefore(results.get(i + 1).getCreatedAt()));
        }
    }

    @Test
    public void testByPage() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().page(1).pageSize(2);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(2, results.size());
    }

    @Test
    public void testSearchByWord() {
        String word = "guIA";
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().word(word).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(n -> n.getName().toUpperCase().contains(word.toUpperCase())));
    }

    @Test
    public void testSearchMultipleCareerSubject() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_MEC).subjectId(MATE_ID);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(1, results.size());
        assertEquals(TVM_ID ,results.get(0).getId());
    }

    @Test
    public void testEscapePercentage() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).word("%");
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(1, results.size());
    }

    @Test
    public void testEscapeUnderscore() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).word("_");
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(1, results.size());
    }

    @Test
    public void testNavigation() {
        List<Searchable> results = searchDao.getNavigationResults(new SearchArgumentsBuilder().build(), EDA_DIRECTORY_ID);
        assertEquals(4, results.size());
    }

    @Test
    public void testNavigationByCategory() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.PRACTICE.toString());
        List<Searchable> results = searchDao.getNavigationResults(sab.build(), EDA_DIRECTORY_ID);
        assertEquals(1, results.size());
        assertTrue(results.stream().allMatch(n -> n.getCategory().equals(Category.PRACTICE)));
    }
    
    @Test
    public void testNavigationByWord() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().word("guia");
        List<Searchable> results = searchDao.getNavigationResults(sab.build(), EDA_DIRECTORY_ID);
        assertEquals(2, results.size());

    }

    @Test
    public void testCountNavigationResults() {
        int searchResults = searchDao.countNavigationResults(new SearchArgumentsBuilder().build(), EDA_DIRECTORY_ID);
        assertEquals(4, searchResults);
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


    private class TestCountChildrenObject {
        private final UUID rootDirId;
        private final UUID child1Id, child2Id, child3Id, child4Id;
        private final UUID grandchild11Id;

        private TestCountChildrenObject() {
            rootDirId = insertDirectory(namedParameterJdbcTemplate, "root", null, null);
            child1Id = insertDirectory(namedParameterJdbcTemplate, "child1", PEPE_ID, rootDirId);
            child2Id = insertDirectory(namedParameterJdbcTemplate, "child2", PEPE_ID, rootDirId);
            child3Id = insertDirectory(namedParameterJdbcTemplate, "child3", PEPE_ID, rootDirId);
            child4Id = insertNote(namedParameterJdbcTemplate, rootDirId,"child4", EDA_ID,PEPE_ID, true, new byte[]{0}, "other", "pdf");

            grandchild11Id = insertDirectory(namedParameterJdbcTemplate, "grandchild11", PEPE_ID, child1Id);
            insertDirectory(namedParameterJdbcTemplate, "grandchild12", PEPE_ID, child1Id);

            insertNote(namedParameterJdbcTemplate, child2Id, "grandchild21", EDA_ID, PEPE_ID, true, new byte[]{0}, "other", "pdf");
            insertNote(namedParameterJdbcTemplate, child2Id, "grandchild22", EDA_ID, PEPE_ID, true, new byte[]{0}, "other", "pdf");

            insertDirectory(namedParameterJdbcTemplate, "ggchild111", PEPE_ID, grandchild11Id);
        }
    }


    @Test
    public void testCountChildrenMixed() {
        TestCountChildrenObject test = new TestCountChildrenObject();
        int result = searchDao.countChildren(test.rootDirId);
        assertEquals(4, result);
    }

    @Test
    public void testCountChildrenAllDirectories() {
        TestCountChildrenObject test = new TestCountChildrenObject();
        int result = searchDao.countChildren(test.child1Id);
        assertEquals(2, result);
    }

    @Test
    public void testCountChildrenAllNotes() {
        TestCountChildrenObject test = new TestCountChildrenObject();
        int result = searchDao.countChildren(test.child2Id);
        assertEquals(2, result);
    }

    @Test
    public void testCountChildrenEmpty() {
        TestCountChildrenObject test = new TestCountChildrenObject();
        int result = searchDao.countChildren(test.child3Id);
        assertEquals(0, result);
    }

    @Test
    public void testCountChildrenGrandChildren() {
        TestCountChildrenObject test = new TestCountChildrenObject();
        int result = searchDao.countChildren(test.grandchild11Id);
        assertEquals(1, result);
    }

    @Test
    public void testCountChildrenNotADirectory() {
        TestCountChildrenObject test = new TestCountChildrenObject();
        int result = searchDao.countChildren(test.child4Id);
        assertEquals(0, result);
    }

    @Test
    public void testFindByName() {
        String name = "Guias";
        Optional<UUID> resultId = searchDao.findByName(EDA_DIRECTORY_ID, name, PEPE_ID);

        assertTrue(resultId.isPresent());
        assertEquals(GUIAS_DIRECTORY_ID, resultId.get());
    }

    @Test
    public void testFindByNameDifferentParent() {
        String name = "Guias";
        Optional<UUID> resultId = searchDao.findByName(PAW_DIRECTORY_ID, name, PEPE_ID);

        assertFalse(resultId.isPresent());
    }

    @Test
    public void testFindByNameDifferentUser() {
        String name = "Guias";
        Optional<UUID> resultId = searchDao.findByName(EDA_DIRECTORY_ID, name, SAIDMAN_ID);

        assertFalse(resultId.isPresent());
    }
}
