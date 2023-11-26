package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.search.Searchable;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import ar.edu.itba.paw.models.search.SearchArguments.SearchArgumentsBuilder;

import static ar.edu.itba.paw.persistence.TestUtils.*;
import static ar.edu.itba.paw.persistence.TestUtils.EDA_ID;
import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class SearchJpaDaoTest {
    @PersistenceContext
    private EntityManager em;

    @Mock
    private NoteDao noteDao;

    @Mock
    private DirectoryDao directoryDao;

    @InjectMocks
    @Autowired
    private SearchJpaDao searchDao;

    private int allResultsPageSize;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        allResultsPageSize = countSearchResults(em, null);

        Mockito.when(noteDao.findNotesByIds(Mockito.any(), Mockito.any(), Mockito.any())).thenAnswer(
                invocation -> {
                    List<UUID> ids = invocation.getArgument(0);
                    return ids.stream().map(id -> new Note.NoteBuilder().id(id).build()).collect(Collectors.toList());
                }
        );
        Mockito.when(directoryDao.findDirectoriesByIds(Mockito.any(), Mockito.any())).thenAnswer(
                invocation -> {
                    List<UUID> ids = invocation.getArgument(0);
                    return ids.stream().map(id -> new Directory.DirectoryBuilder().id(id).build()).collect(Collectors.toList());
                }
        );
    }

    @Test
    public void testCountSearchResults() {
        int searchResults = searchDao.countSearchResults(new SearchArgumentsBuilder().build());
        assertEquals(searchResults, allResultsPageSize);
    }

    @Test
    public void testCountSearchResultsWithFilters() {
        int expectedResults = countSearchResults(em, " institution_id = '" + ITBA_ID + "' AND career_id = '" + ING_INF_ID + "' AND subject_id = '" + EDA_ID + "' AND category = '" + Category.PRACTICE.name() + "'");

        int searchResults = searchDao.countSearchResults(new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_INF_ID).subjectId(EDA_ID).category(Category.PRACTICE.name()).build());

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
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_INF_ID).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(9, results.size());
    }

    @Test
    public void testSearchNotesBySubject(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder()
                                            .institutionId(ITBA_ID)
                                            .careerId(ING_INF_ID)
                                            .subjectId(EDA_ID)
                                            .pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(4, results.size());
        //assertTrue(results.stream().allMatch(n -> n.getSubject().getSubjectId().equals(EDA_ID)));
    }

    @Test
    public void testByCategory(){
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.PRACTICE.toString()).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(3, results.size());
//        assertTrue(results.stream().allMatch(n -> n.getValue().equals(Category.PRACTICE)));
    }

    @Test
    public void testSearchNotes() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.NOTE.toString()).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(8, results.size());
        assertTrue(results.stream().allMatch(n -> n instanceof Note));
    }

    @Test
    public void testSearchDirectory() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().category(Category.DIRECTORY.toString()).pageSize(allResultsPageSize);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(6, results.size());
        assertTrue(results.stream().allMatch(d -> d instanceof Directory));
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
//        assertTrue(results.stream().allMatch(n -> n.getName().toUpperCase().contains(word.toUpperCase())));
    }

    @Test
    public void testSearchMultipleCareerSubject() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().institutionId(ITBA_ID).careerId(ING_MEC_ID).subjectId(MATE_ID);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(1, results.size());
        assertEquals(TVM_ID, results.get(0).getId());
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
    public void testPrivacyMine() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().currentUserId(JAIMITO_ID).userId(JAIMITO_ID);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(1, results.size());
    }

    @Test
    public void testPrivacyOthers() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().currentUserId(PEPE_ID).userId(JAIMITO_ID);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(0, results.size());
    }

    @Test
    public void testPrivacyIncognito() {
        SearchArgumentsBuilder sab = new SearchArgumentsBuilder().userId(JAIMITO_ID);
        List<Searchable> results = searchDao.search(sab.build());
        assertEquals(0, results.size());
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
//        assertTrue(results.stream().allMatch(p -> p.getValue().equals(Category.PRACTICE)));
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

    private class TestCountChildrenObject {
        private final UUID rootDirId;
        private final UUID child1Id, child2Id, child3Id, child4Id;
        private final UUID grandchild11Id;

        private TestCountChildrenObject() {
            Directory.DirectoryBuilder dbuilder = new Directory.DirectoryBuilder();

            rootDirId = insertDirectory(em, dbuilder.name("root")).getId();
            dbuilder.user(em.getReference(User.class, PEPE_ID))
                    .parent(em.getReference(Directory.class, rootDirId));
            child1Id = insertDirectory(em, dbuilder.name("child1")).getId();
            child2Id = insertDirectory(em, dbuilder.name("child2")).getId();
            child3Id = insertDirectory(em, dbuilder.name("child3")).getId();
            child4Id = insertNote(em, new Note.NoteBuilder()
                            .parentId(rootDirId)
                            .name("child4")
                            .subject(em.getReference(Subject.class, EDA_ID))
                            .user(em.getReference(User.class, PEPE_ID))
                            .visible(true)
                            .category(Category.OTHER)
                            .fileType("pdf")).getId();

            grandchild11Id = insertDirectory(em, dbuilder.name("grandchild11").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, child1Id))).getId();
            insertDirectory(em, dbuilder.name("grandchild12").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, child1Id)));
            Note.NoteBuilder nb = new Note.NoteBuilder()
                    .parentId(child2Id)
                    .subject(em.getReference(Subject.class, EDA_ID))
                    .user(em.getReference(User.class, PEPE_ID))
                    .visible(true)
                    .category(Category.OTHER)
                    .fileType("pdf");
            insertNote(em, nb.name("grandchild21"));
            insertNote(em, nb.name("grandchild22"));

            insertDirectory(em, dbuilder.name("ggchild111").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, grandchild11Id)));
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
