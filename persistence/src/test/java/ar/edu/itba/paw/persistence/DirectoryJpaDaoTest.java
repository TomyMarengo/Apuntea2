package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.note.Note;
import ar.edu.itba.paw.models.search.SortArguments;
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

import static ar.edu.itba.paw.models.NameConstants.*;
import static ar.edu.itba.paw.persistence.TestUtils.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class DirectoryJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private DirectoryJpaDao directoryDao;

    private User pepeUser;
    private User saidmanUser;
    private Directory privateDir;

    @Before
    public void setUp() {
        pepeUser = em.find(User.class, PEPE_ID);
        saidmanUser = em.find(User.class, SAIDMAN_ID);
        privateDir = insertDirectory(em, new Directory.DirectoryBuilder()
                        .name(PSVM)
                        .parent(em.getReference(Directory.class,EDA_DIRECTORY_ID))
                        .user(pepeUser)
                        .visible(false)
        );
    }

    @Test
    public void testCreate() {
        String name = "tempRoot";
        UUID parentId = EDA_DIRECTORY_ID;
        UUID userId = PEPE_ID;
        boolean visible = true;
        String color = "BBBBBB";

        Directory directory = directoryDao.create(name, parentId, pepeUser, visible, color);
        em.flush();

        assertEquals(1, countRows(em, DIRECTORIES, "directory_id = '" + directory.getId() + "' AND directory_name = '" + name + "' AND parent_id = '" + parentId + "' AND user_id = '" + userId + "' AND visible = " + visible + " AND icon_color = '" + color + "'"));
    }

    @Test
    public void testCreateRootDirectory() {
        String name = "nuevaMateria";
        Directory directory = directoryDao.createRootDirectory("nuevaMateria");
        em.flush();
        assertEquals(1, countRows(em, DIRECTORIES, "directory_id = '" + directory.getId() + "' AND directory_name = '"+ name +"' AND parent_id IS NULL AND user_id IS NULL"));
    }

    @Test
    public void testGetDirectoryByIdPublic() {
        String name = "public static void main";
        Directory newDir = insertDirectory(em,
                new Directory.DirectoryBuilder()
                        .name(name)
                        .parent(em.getReference(Directory.class, EDA_DIRECTORY_ID))
                        .user(pepeUser)
                        .visible(true)
        );
        Directory directory = directoryDao.getDirectoryById(newDir.getId(), SAIDMAN_ID).orElseThrow(AssertionError::new);
        assertEquals(newDir.getId(), directory.getId());
        assertEquals(name, directory.getName());
    }

    @Test
    public void testGetDirectoryByIdNonExistent() {
        Optional<Directory> maybeDirectory = directoryDao.getDirectoryById(UUID.randomUUID(), PEPE_ID);
        assertFalse(maybeDirectory.isPresent());
    }

    @Test
    public void testGetDirectoryByIdPrivate() {
        Optional<Directory> maybeDirectory = directoryDao.getDirectoryById(privateDir.getId(), CARLADMIN_ID);
        assertFalse(maybeDirectory.isPresent());
    }

    @Test
    public void testGetDirectoryByIdPrivateOwner() {
        Directory directory = directoryDao.getDirectoryById(privateDir.getId(), PEPE_ID).orElseThrow(AssertionError::new);
        assertEquals(privateDir.getId(), directory.getId());
        assertEquals(privateDir.getName(), directory.getName());
    }

    @Test
    public void testDelete() {
        int qtyBasuraPrev = countRows(em, DIRECTORIES, "directory_name LIKE " + "'%Basura%'");
        directoryDao.delete(BASURA_ID, PEPE_ID);
        em.flush();
        assertEquals(2, qtyBasuraPrev);
        assertEquals(0, countRows(em, DIRECTORIES, "directory_name LIKE " + "'%Basura%'"));
    }


    @Test
    public void testUserTriesToDeleteDirectory() {
        Directory newDir = insertDirectory(em, new Directory.DirectoryBuilder().name("temp").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, EDA_DIRECTORY_ID)));
        int countInserted = countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "'");
        boolean success = directoryDao.delete(newDir.getId(), SAIDMAN_ID);
        assertFalse(success);
        assertEquals(1, countInserted);
        assertEquals(1, countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "'"));
    }

    @Test
    public void testAdminDeletesDirectory() {
        Directory newDir = insertDirectory(em, new Directory.DirectoryBuilder().name("temp").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, EDA_DIRECTORY_ID)));

        int countInserted = countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "'");

        boolean success = directoryDao.delete(newDir.getId());
        assertTrue(success);
        assertEquals(1, countInserted);
        assertEquals(0, countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "'"));
    }

    @Test
    public void testDeleteRootDirectorySuccess() {

        Directory rootDir = insertDirectory(em, new Directory.DirectoryBuilder().name("root"));
        Directory rootDir2 = insertDirectory(em, new Directory.DirectoryBuilder().name("root2"));
        int countInserted = countRows(em, DIRECTORIES, "directory_id = '" + rootDir.getId() + "'");
        boolean success = directoryDao.delete(rootDir.getId());
        assertTrue(success);
        assertEquals(1, countInserted);
        assertEquals(0, countRows(em, DIRECTORIES, "directory_id = '" + rootDir.getId() + "'"));
        assertEquals(1, countRows(em, DIRECTORIES, "directory_id = '" + rootDir2.getId() + "'"));
    }

    @Test
    public void testDeleteDirectoryNonExistent() {
        UUID id = UUID.randomUUID();
        boolean success = directoryDao.delete(id);
        assertFalse(success);
    }

    @Test
    public void testRemoveFavorite() {
        Directory newDir = insertDirectory(em, new Directory.DirectoryBuilder().name("temp")
                .parent(em.getReference(Directory.class, EDA_DIRECTORY_ID))
                .user(pepeUser)
        );
        insertFavoriteDirectory(em, newDir.getId(), PEPE_ID);
        insertFavoriteDirectory(em, newDir.getId(), SAIDMAN_ID);
        directoryDao.removeFavorite(PEPE_ID, newDir.getId());
        em.flush();
        assertEquals(0, countRows(em, DIRECTORY_FAVORITES, "user_id = '" + PEPE_ID + "' AND directory_id = '" + newDir.getId() + "'"));
        assertEquals(1, countRows(em, DIRECTORY_FAVORITES, "user_id = '" + SAIDMAN_ID + "' AND directory_id = '" + newDir.getId() + "'"));
    }

    @Test
    public void testCountRootDirQuantity() {
        Directory edaDir = em.find(Directory.class, EDA_DIRECTORY_ID);
        Note.NoteBuilder nb = new Note.NoteBuilder().category(Category.EXAM).fileType(".jpg").parentId(EDA_DIRECTORY_ID).subject(em.getReference(Subject.class, EDA_ID)).user(pepeUser).visible(true);
        insertNote(em, nb.name("temp").parentId(EDA_DIRECTORY_ID).user(pepeUser).visible(false));
        insertNote(em, nb.name("temp").parentId(EDA_DIRECTORY_ID).user(saidmanUser).visible(true));
        directoryDao.loadRootDirsFileQuantity(Collections.singletonList(EDA_DIRECTORY_ID), PEPE_ID, PEPE_ID);
        assertEquals(3, edaDir.getQtyFiles()); /* 2 previously loaded notes + newNoteP */
    }

    @Test
    public void testPrivateDoesNotCountInQuantity() {
        Directory edaDir = em.find(Directory.class, EDA_DIRECTORY_ID);
        Note.NoteBuilder nb = new Note.NoteBuilder().category(Category.EXAM).fileType(".jpg").parentId(EDA_DIRECTORY_ID).subject(em.getReference(Subject.class, EDA_ID)).user(pepeUser).visible(true);
        insertNote(em, nb.name("temp").parentId(EDA_DIRECTORY_ID).user(pepeUser).visible(false));
        insertNote(em, nb.name("temp").parentId(EDA_DIRECTORY_ID).user(saidmanUser).visible(true));
        directoryDao.loadRootDirsFileQuantity(Collections.singletonList(EDA_DIRECTORY_ID), PEPE_ID, SAIDMAN_ID);
        assertEquals(2, edaDir.getQtyFiles()); /* only the 2 previously loaded notes */
    }

    @Test
    public void testFindDirectoriesByIds(){
        UUID[] ids = {GUIAS_DIRECTORY_ID, THEORY_DIRECTORY_ID, MVC_DIRECTORY_ID, PEPE_ID};
        List<Directory> directories = directoryDao.findDirectoriesByIds(Arrays.asList(ids), new SortArguments(SortArguments.SortBy.DATE, true));

        assertTrue(directories.stream().anyMatch(directory -> directory.getId().equals(GUIAS_DIRECTORY_ID)));
        assertTrue(directories.stream().anyMatch(directory -> directory.getId().equals(THEORY_DIRECTORY_ID)));
        assertTrue(directories.stream().anyMatch(directory -> directory.getId().equals(MVC_DIRECTORY_ID)));
        assertTrue(directories.stream().noneMatch(directory -> directory.getId().equals(MATE_DIRECTORY_ID)));
        assertTrue(directories.stream().noneMatch(directory -> directory.getId().equals(PEPE_ID)));
    }

    @Test
    public void testFindDirectoriesByIdsOrderDateAsc(){
        UUID[] ids = {GUIAS_DIRECTORY_ID, THEORY_DIRECTORY_ID, MVC_DIRECTORY_ID, MATE_DIRECTORY_ID};
        List<Directory> directories = directoryDao.findDirectoriesByIds(Arrays.asList(ids), new SortArguments(SortArguments.SortBy.DATE, true));

        assertEquals(4, directories.size());
        for (int i=0; i< directories.size()-2 ; i++)
            assertFalse(directories.get(i).getCreatedAt().isAfter(directories.get(i+1).getCreatedAt()));
    }

    @Test
    public void testFindDirectoriesByIdsOrderNameDesc(){
        UUID[] ids = {GUIAS_DIRECTORY_ID, THEORY_DIRECTORY_ID, MVC_DIRECTORY_ID, MATE_DIRECTORY_ID};
        List<Directory> directories = directoryDao.findDirectoriesByIds(Arrays.asList(ids), new SortArguments(SortArguments.SortBy.NAME, false));

        assertEquals(4, directories.size());
        for (int i=0; i< directories.size()-2 ; i++)
            assertTrue(directories.get(i).getName().compareToIgnoreCase(directories.get(i+1).getName()) >= 0);
    }

    @Test
    public void testFindRootDirectoryById() {
        Optional<Directory> maybeRoot = directoryDao.getDirectoryRoot(MVC_DIRECTORY_ID);

        assertTrue(maybeRoot.isPresent());
        assertEquals(PAW_DIRECTORY_ID, maybeRoot.get().getId());
    }

    @Test
    public void testFindRootDirectoryByIdNonExistent() {
        Optional<Directory> maybeRoot = directoryDao.getDirectoryRoot(PEPE_ID);
        assertFalse(maybeRoot.isPresent());
    }

    @Test
    public void testFindRootDirIfRootDir() {
        Optional<Directory> maybeRoot = directoryDao.getDirectoryRoot(PAW_DIRECTORY_ID);

        assertTrue(maybeRoot.isPresent());
        assertEquals(PAW_DIRECTORY_ID, maybeRoot.get().getId());
    }
}
