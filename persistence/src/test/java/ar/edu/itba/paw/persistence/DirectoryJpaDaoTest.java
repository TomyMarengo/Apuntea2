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
    public void testDirectoryPathLength1() {
        List<UUID> path = directoryDao.getDirectoryPathIds(EDA_DIRECTORY_ID);

        assertEquals(EDA_DIRECTORY_ID, path.get(0));
        assertEquals(1, path.size());
    }

    @Test
    public void testDirectoryPathLength2() {
        List<UUID> path = directoryDao.getDirectoryPathIds(GUIAS_DIRECTORY_ID);
        assertEquals(2, path.size());
        assertEquals(EDA_DIRECTORY_ID, path.get(0));
        assertEquals(GUIAS_DIRECTORY_ID, path.get(1));
    }

    @Test
    public void testDirectoryPathLength3() {
        List<UUID> path = directoryDao.getDirectoryPathIds(MVC_DIRECTORY_ID);

        assertEquals(3, path.size());
        assertEquals(PAW_DIRECTORY_ID, path.get(0));
        assertEquals(THEORY_DIRECTORY_ID, path.get(1));
        assertEquals(MVC_DIRECTORY_ID, path.get(2));
    }

    @Test
    public void testDirectoryPathLengthGreaterThan3() {
        UUID[] directoryIds = new UUID[5];
        insertDirectoryRec(5, 1, EDA_DIRECTORY_ID, directoryIds);

        List<UUID> path = directoryDao.getDirectoryPathIds(directoryIds[4]);

        assertEquals(6, path.size());
        assertEquals(EDA_DIRECTORY_ID, path.get(0));
        for (int i = 1; i < 6; i++)
            assertEquals(directoryIds[i - 1], path.get(i));
    }

    private void insertDirectoryRec(final int maxLevel, final int currLevel, final UUID parentId, final UUID[] directoryIds)  {
        if (currLevel > maxLevel) return;
        Directory newDirId = insertDirectory(em, new Directory.DirectoryBuilder()
                        .name("d" + currLevel)
                        .parent(em.getReference(Directory.class, parentId))
                        .user(pepeUser));
        em.flush();
        directoryIds[currLevel - 1] = newDirId.getId();
        insertDirectoryRec(maxLevel, currLevel + 1, newDirId.getId(), directoryIds);
    }

    @Test
    public void testDelete() {
        int qtyBasuraPrev = countRows(em, DIRECTORIES, "directory_name LIKE " + "'%Basura%'");
        directoryDao.delete(Collections.singletonList(BASURA_ID), PEPE_ID);
        em.flush();
        assertEquals(2, qtyBasuraPrev);
        assertEquals(0, countRows(em, DIRECTORIES, "directory_name LIKE " + "'%Basura%'"));
    }

    @Test
    public void testDeleteMany() {
        List<UUID> directoryIds = new ArrayList<>();
        String[] names = {"tmp1", "tmp2", "tmp3", "tmp4"};
        Directory parent = insertDirectory(em, new Directory.DirectoryBuilder().name("tmpParent").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, EDA_DIRECTORY_ID)));
        for (String name : names) {
            Directory newDir = insertDirectory(em, new Directory.DirectoryBuilder().name(name).user(em.getReference(User.class, PEPE_ID)).parent(parent));
            directoryIds.add(newDir.getId());
        }

        int countInserted = countRows(em, DIRECTORIES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parent.getId() + "'");

        directoryDao.delete(directoryIds, PEPE_ID);

        int countPostDelete = countRows(em, DIRECTORIES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parent.getId() + "'");

        assertEquals(4, countInserted);
        assertEquals(0, countPostDelete);
    }

    @Test
    public void testUserTriesToDeleteDirectory() {
        Directory newDir = insertDirectory(em, new Directory.DirectoryBuilder().name("temp").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, EDA_DIRECTORY_ID)));
        int countInserted = countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "'");
        boolean success = directoryDao.delete(Collections.singletonList(newDir.getId()), SAIDMAN_ID);
        assertFalse(success);
        assertEquals(1, countInserted);
        assertEquals(1, countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "'"));
    }

    @Test
    public void testAdminDeletesDirectory() {
        Directory newDir = insertDirectory(em, new Directory.DirectoryBuilder().name("temp").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, EDA_DIRECTORY_ID)));
        Directory newDir2 = insertDirectory(em, new Directory.DirectoryBuilder().name("temp2").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, EDA_DIRECTORY_ID)));
        Directory newDir3 = insertDirectory(em, new Directory.DirectoryBuilder().name("temp3").user(em.getReference(User.class, PEPE_ID)).parent(em.getReference(Directory.class, EDA_DIRECTORY_ID)));

        int countInserted = countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "' OR directory_id = '" + newDir2.getId() + "' OR directory_id = '" + newDir3.getId() + "'");
        List<UUID> ids = new ArrayList<>();
        ids.add(newDir.getId());
        ids.add(newDir2.getId());
        ids.add(newDir3.getId());

        boolean success = directoryDao.delete(ids);
        assertTrue(success);
        assertEquals(3, countInserted);
        assertEquals(0, countRows(em, DIRECTORIES, "directory_id = '" + newDir.getId() + "'"));
        assertEquals(0, countRows(em, DIRECTORIES, "directory_id = '" + newDir2.getId() + "'"));
        assertEquals(0, countRows(em, DIRECTORIES, "directory_id = '" + newDir3.getId() + "'"));
    }

    @Test
    public void testDeleteRootDirectorySuccess() {

        Directory rootDir = insertDirectory(em, new Directory.DirectoryBuilder().name("root"));
        Directory rootDir2 = insertDirectory(em, new Directory.DirectoryBuilder().name("root2"));
        int countInserted = countRows(em, DIRECTORIES, "directory_id = '" + rootDir.getId() + "'");
        boolean success = directoryDao.delete(Collections.singletonList(rootDir.getId()));
        assertTrue(success);
        assertEquals(1, countInserted);
        assertEquals(0, countRows(em, DIRECTORIES, "directory_id = '" + rootDir.getId() + "'"));
        assertEquals(1, countRows(em, DIRECTORIES, "directory_id = '" + rootDir2.getId() + "'"));
    }

    @Test
    public void testDeleteDirectoryNonExistent() {
        UUID id = UUID.randomUUID();
        boolean success = directoryDao.delete(Collections.singletonList(id));
        assertFalse(success);
    }

    @Test
    public void testAddFavorite() {
        directoryDao.addFavorite(em.getReference(User.class, PEPE_ID), EDA_DIRECTORY_ID);
        em.flush();
        assertEquals(1, countRows(em, DIRECTORY_FAVORITES, "user_id = '" + PEPE_ID + "' AND directory_id = '" + EDA_DIRECTORY_ID + "'"));
        assertEquals(0, countRows(em, DIRECTORY_FAVORITES, "user_id = '" + SAIDMAN_ID + "' AND directory_id = '" + EDA_DIRECTORY_ID + "'"));
    }

    @Test
    public void testRemoveFavorite() {
        Directory newDir = insertDirectory(em, new Directory.DirectoryBuilder().name("temp")
                .parent(em.getReference(Directory.class, EDA_DIRECTORY_ID))
                .user(pepeUser)
        );
        insertFavoriteDirectory(em, newDir.getId(), PEPE_ID);
        insertFavoriteDirectory(em, newDir.getId(), SAIDMAN_ID);
        directoryDao.removeFavorite(em.getReference(User.class, PEPE_ID), newDir.getId());
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
        directoryDao.setRootDirsFileQuantity(Collections.singletonList(EDA_DIRECTORY_ID), PEPE_ID, PEPE_ID);
        assertEquals(3, edaDir.getQtyFiles()); // 2 previously loaded notes + newNoteP
    }

    @Test
    public void testPrivateDoesNotCountInQuantity() {
        Directory edaDir = em.find(Directory.class, EDA_DIRECTORY_ID);
        Note.NoteBuilder nb = new Note.NoteBuilder().category(Category.EXAM).fileType(".jpg").parentId(EDA_DIRECTORY_ID).subject(em.getReference(Subject.class, EDA_ID)).user(pepeUser).visible(true);
        insertNote(em, nb.name("temp").parentId(EDA_DIRECTORY_ID).user(pepeUser).visible(false));
        insertNote(em, nb.name("temp").parentId(EDA_DIRECTORY_ID).user(saidmanUser).visible(true));
        directoryDao.setRootDirsFileQuantity(Collections.singletonList(EDA_DIRECTORY_ID), PEPE_ID, SAIDMAN_ID);
        assertEquals(2, edaDir.getQtyFiles()); // only the 2 previously loaded notes
    }

    @Test
    public void testLoadDirectoryFavorites() {
        Directory.DirectoryBuilder db = new Directory.DirectoryBuilder()
                .parent(em.getReference(Directory.class, EDA_DIRECTORY_ID))
                .user(pepeUser)
                .visible(true);
        Directory faved1 = insertDirectory(em, db.name("faved1"));
        Directory faved2 = insertDirectory(em, db.name("faved2"));
        Directory nofaved = insertDirectory(em, db.name("nofaved"));
        Directory[] directories = {faved1, faved2};
        insertFavoriteDirectory(em, faved1.getId(), SAIDMAN_ID);
        insertFavoriteDirectory(em, faved2.getId(), SAIDMAN_ID);
        boolean wasFaved1 = faved1.isFavorite();
        boolean wasFaved2 = faved2.isFavorite();

        directoryDao.loadDirectoryFavorites(Arrays.stream(directories).map(Directory::getId).collect(Collectors.toList()), SAIDMAN_ID);

        assertTrue(faved1.isFavorite());
        assertTrue(faved2.isFavorite());
        assertFalse(nofaved.isFavorite());
        assertFalse(wasFaved1);
        assertFalse(wasFaved2);

    }
}
