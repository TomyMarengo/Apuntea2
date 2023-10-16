package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;
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

import static ar.edu.itba.paw.persistence.JdbcDaoUtils.*;
import static ar.edu.itba.paw.persistence.JdbcDaoTestUtils.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
public class DirectoryJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private DirectoryJdbcDao directoryDao;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert jdbcFavoriteInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcFavoriteInsert = new SimpleJdbcInsert(ds)
                    .withTableName(FAVORITES);
    }

    @Test
    public void testCreate() {
        String name = "tempRoot";
        UUID parentId = EDA_DIRECTORY_ID;
        UUID userId = PEPE_ID;
        boolean visible = true;
        String color = "BBBBBB";

        UUID directoryId = directoryDao.create(name, parentId, userId, visible, color);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + directoryId + "' AND directory_name = '" + name + "' AND parent_id = '" + parentId + "' AND user_id = '" + userId + "' AND visible = " + visible + " AND icon_color = '" + color + "'"));
    }

    @Test
    public void testCreateRootDirectory() {
        String name = "nuevaMateria";
        UUID directoryId = directoryDao.createRootDirectory("nuevaMateria");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + directoryId + "' AND directory_name = '"+ name +"' AND parent_id IS NULL AND user_id IS NULL"));
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "Directories", "directory_id = '" + directoryId + "'");
    }

    @Test
    public void testGetDirectoryByIdPublic() {
        String name = "public static void main";
        UUID newDirId = insertDirectory(namedParameterJdbcTemplate, name, PEPE_ID, EDA_DIRECTORY_ID, true);
        Directory directory = directoryDao.getDirectoryById(newDirId, SAIDMAN_ID).orElseThrow(AssertionError::new);
        assertEquals(newDirId, directory.getId());
        assertEquals(name, directory.getName());
    }

    @Test
    public void testGetDirectoryByIdNonExistent() {
        Optional<Directory> maybeDirectory = directoryDao.getDirectoryById(UUID.randomUUID(), PEPE_ID);
        assertFalse(maybeDirectory.isPresent());
    }

    @Test
    public void testGetDirectoryByIdPrivate() {
        UUID adminId = insertAdmin(namedParameterJdbcTemplate, "admin@mail", "123456",  ING_INF, "es");
        String name = "private static void main";
        UUID newDirId = insertDirectory(namedParameterJdbcTemplate, name, PEPE_ID, EDA_DIRECTORY_ID, false);
        Optional<Directory> maybeDirectory = directoryDao.getDirectoryById(newDirId, adminId);
        assertFalse(maybeDirectory.isPresent());
    }

    @Test
    public void testGetDirectoryByIdPrivateOwner() {
        String name = "private static void main";
        UUID newDirId = insertDirectory(namedParameterJdbcTemplate, name, PEPE_ID, EDA_DIRECTORY_ID, false);
        Directory directory = directoryDao.getDirectoryById(newDirId, PEPE_ID).orElseThrow(AssertionError::new);
        assertEquals(newDirId, directory.getId());
        assertEquals(name, directory.getName());
    }

    @Test
    public void testDirectoryPathLength1() {
        DirectoryPath path = directoryDao.getDirectoryPath(EDA_DIRECTORY_ID);

        Directory rootDirectory = path.getRootDirectory();
        Directory parentDirectory = path.getParentDirectory();
        Directory currentDirectory = path.getCurrentDirectory();

        assertEquals(EDA_DIRECTORY_ID, rootDirectory.getId());
        assertEquals("EDA", rootDirectory.getName());
        assertNull(rootDirectory.getParentId());
        assertNull(parentDirectory);
        assertEquals(rootDirectory, currentDirectory);
        assertEquals(1, path.getLength());
    }

    @Test
    public void testDirectoryPathLength2() {
        DirectoryPath path = directoryDao.getDirectoryPath(GUIAS_DIRECTORY_ID);

        Directory rootDirectory = path.getRootDirectory();
        Directory parentDirectory = path.getParentDirectory();

        assertEquals(EDA_DIRECTORY_ID, rootDirectory.getId());
        assertEquals("EDA", rootDirectory.getName());
        assertNull(rootDirectory.getParentId());
        assertEquals(EDA_ID, rootDirectory.getSubject().getSubjectId());
        assertEquals("EDA", rootDirectory.getSubject().getName());
        assertEquals(parentDirectory.getId(), rootDirectory.getId());
        assertEquals(2, path.getLength());
    }

    @Test
    public void testDirectoryPathLength3() {
        DirectoryPath path = directoryDao.getDirectoryPath(MVC_DIRECTORY_ID);

        Directory rootDirectory = path.getRootDirectory();
        Directory parentDirectory = path.getParentDirectory();
        Directory currentDirectory = path.getCurrentDirectory();

        assertEquals(PAW_DIRECTORY_ID, rootDirectory.getId());
        assertEquals("PAW", rootDirectory.getName());
        assertNull(rootDirectory.getParentId());
        assertEquals("PAW", rootDirectory.getSubject().getName());
        assertEquals(THEORY_DIRECTORY_ID, parentDirectory.getId());
        assertEquals(MVC_DIRECTORY_ID, currentDirectory.getId());
        assertEquals(3, path.getLength());
    }

    @Test
    public void testDirectoryPathLengthGreaterThan3() {
        UUID dir1Id = insertDirectory(namedParameterJdbcTemplate, "d1", PEPE_ID, EDA_DIRECTORY_ID);
        UUID dir2Id = insertDirectory(namedParameterJdbcTemplate, "d2", PEPE_ID, dir1Id);
        UUID dir3Id = insertDirectory(namedParameterJdbcTemplate, "d3", PEPE_ID, dir2Id);
        UUID dir4Id = insertDirectory(namedParameterJdbcTemplate, "d4", PEPE_ID, dir3Id);
        UUID dir5Id = insertDirectory(namedParameterJdbcTemplate, "d5", PEPE_ID, dir4Id);

        DirectoryPath path = directoryDao.getDirectoryPath(dir5Id);

        assertEquals(EDA_DIRECTORY_ID, path.getRootDirectory().getId());
        assertEquals(EDA_ID, path.getRootDirectory().getSubject().getSubjectId());
        assertEquals(dir4Id, path.getParentDirectory().getId());
        assertEquals(dir5Id, path.getCurrentDirectory().getId());
        assertEquals(6, path.getLength());
    }

    @Test
    public void testUpdate() {
        String oldName = "oldName";
        String newName = "newName";
        String oldColor = "BBBBBB";
        String newColor = "CCCCCC";
        boolean oldVisible = true;
        boolean newVisible = false;
        UUID parentId = EDA_DIRECTORY_ID;
        UUID userId = PEPE_ID;
        UUID directoryId = insertDirectory(namedParameterJdbcTemplate, oldName, userId, parentId, oldVisible, oldColor);

        Directory updatedDirectory = new Directory.DirectoryBuilder()
                .directoryId(directoryId)
                .name(newName)
                .iconColor(newColor)
                .visible(newVisible)
                .build();
        boolean success = directoryDao.update(updatedDirectory, userId);

        assertTrue(success);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DIRECTORIES, "directory_id = '" + directoryId + "' AND user_id = '" + userId + "' AND directory_name = '" + newName + "' AND visible = " + newVisible + " AND icon_color = '" + newColor + "'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DIRECTORIES, "directory_id = '" + directoryId + "' AND user_id = '" + userId + "' AND directory_name = '" + oldName + "' AND visible = " + oldVisible + " AND icon_color = '" + oldColor + "'"));
    }

    @Test
    public void testUpdateNonExistent() {
        String newName = "newName";
        String newColor = "CCCCCC";
        boolean newVisible = false;
        UUID userId = PEPE_ID;

        Directory updatedDirectory = new Directory.DirectoryBuilder()
                .directoryId(PEPE_ID)
                .name(newName)
                .iconColor(newColor)
                .visible(newVisible)
                .build();
        boolean success = directoryDao.update(updatedDirectory, userId);

        assertFalse(success);
    }

    @Test
    public void testDelete() {
        int qtyBasuraPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_name LIKE " + "'%Basura%'");
        directoryDao.delete(new UUID[]{BASURA_ID}, PEPE_ID);
        assertEquals(2, qtyBasuraPrev);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_name LIKE " + "'%Basura%'"));
    }

    @Test
    public void testDeleteMany() {
        List<UUID> directoryIds = new ArrayList<>();
        String[] names = {"tmp1", "tmp2", "tmp3", "tmp4"};
        UUID[] ids = new UUID[names.length];
        UUID parentId = insertDirectory(namedParameterJdbcTemplate, "tmpParent", PEPE_ID, EDA_DIRECTORY_ID);
        for (String name : names) {
            UUID newDirId = insertDirectory(namedParameterJdbcTemplate, name, PEPE_ID, parentId);
            directoryIds.add(newDirId);
        }

        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DIRECTORIES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parentId + "'");

        directoryDao.delete(directoryIds.toArray(ids), PEPE_ID);

        int countPostDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DIRECTORIES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + parentId + "'");

        assertEquals(4, countInserted);
        assertEquals(0, countPostDelete);
    }

    @Test
    public void testUserTriesToDeleteDirectory() {
        UUID newDirId = insertDirectory(namedParameterJdbcTemplate, "temp", PEPE_ID, EDA_DIRECTORY_ID);
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + newDirId + "'");
        boolean success = directoryDao.delete(new UUID[]{newDirId}, SAIDMAN_ID);
        assertFalse(success);
        assertEquals(1, countInserted);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + newDirId + "'"));
    }

    @Test
    public void testAdminDeletesDirectory() {
        UUID newDirId = insertDirectory(namedParameterJdbcTemplate, "temp", PEPE_ID, EDA_DIRECTORY_ID);
        UUID newDirId2 = insertDirectory(namedParameterJdbcTemplate, "temp2", PEPE_ID, EDA_DIRECTORY_ID);
        UUID newDirId3 = insertDirectory(namedParameterJdbcTemplate, "temp3", PEPE_ID, EDA_DIRECTORY_ID);
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + newDirId + "' OR directory_id = '" + newDirId2 + "' OR directory_id = '" + newDirId3 + "'");
        List<Directory> dirs = directoryDao.delete(new UUID[]{newDirId, newDirId2, newDirId3});
        assertEquals(3, dirs.size());
        assertEquals(3, countInserted);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + newDirId + "'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + newDirId2 + "'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + newDirId3 + "'"));
    }

    @Test
    public void testDeleteRootDirectorySuccess() {
        UUID rootDirectoryId = insertDirectory(namedParameterJdbcTemplate, "root", null, null);
        UUID root2DirectoryId = insertDirectory(namedParameterJdbcTemplate, "root2", null, null);
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + rootDirectoryId + "'");
        boolean success = directoryDao.deleteRootDirectory(rootDirectoryId);
        assertTrue(success);
        assertEquals(1, countInserted);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + rootDirectoryId + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + root2DirectoryId + "'"));
    }

    @Test
    public void testDeleteRootDirectoryNonRoot() {
        UUID rootDirectoryId = insertDirectory(namedParameterJdbcTemplate, "root", null, null);
        UUID root2DirectoryId = insertDirectory(namedParameterJdbcTemplate, "root2", null, null);
        UUID ruthId = insertStudent(namedParameterJdbcTemplate, "ruthy@itba.edu.ar", "7777777", ING_INF, "es");
        UUID ruthDirectoryId = insertDirectory(namedParameterJdbcTemplate, "Apuntes ruthy", ruthId, rootDirectoryId);
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + ruthDirectoryId + "'");

        boolean success = directoryDao.deleteRootDirectory(ruthDirectoryId);

        assertFalse(success);
        assertEquals(1, countInserted);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + rootDirectoryId + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + root2DirectoryId + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + ruthDirectoryId + "'"));
    }

    @Test
    public void testDeleteRootDirectoryNonExistent() {
        UUID rootDirectoryId = UUID.randomUUID();
        boolean success = directoryDao.deleteRootDirectory(rootDirectoryId);
        assertFalse(success);
    }

    @Test
    public void testGetFavorites() {
        UUID newDir1 = insertDirectory(namedParameterJdbcTemplate, "temp", PEPE_ID, EDA_DIRECTORY_ID);
        UUID newDir2 = insertDirectory(namedParameterJdbcTemplate, "temp2", SAIDMAN_ID, EDA_DIRECTORY_ID);
        UUID newDir3 = insertDirectory(namedParameterJdbcTemplate, "temp3", SAIDMAN_ID, EDA_DIRECTORY_ID);
        insertFavorite(jdbcFavoriteInsert, newDir1, PEPE_ID);
        insertFavorite(jdbcFavoriteInsert, newDir2, SAIDMAN_ID);
        insertFavorite(jdbcFavoriteInsert, newDir3, PEPE_ID);

        List<Directory> favorites = directoryDao.getFavorites(PEPE_ID);
        assertEquals(2, favorites.size());
        favorites.stream().filter(d -> d.getId().equals(newDir1)).findAny().orElseThrow(AssertionError::new);
        favorites.stream().filter(d -> d.getId().equals(newDir3)).findAny().orElseThrow(AssertionError::new);
    }

    @Test
    public void testGetFavoritePrivate() {
        UUID newDir1 = insertDirectory(namedParameterJdbcTemplate, "temp", PEPE_ID, EDA_DIRECTORY_ID, false);
        UUID newDir2 = insertDirectory(namedParameterJdbcTemplate, "extern1", SAIDMAN_ID, EDA_DIRECTORY_ID, true);
        UUID newDir3 = insertDirectory(namedParameterJdbcTemplate, "extern2", SAIDMAN_ID, EDA_DIRECTORY_ID, true);
        UUID newDir4 = insertDirectory(namedParameterJdbcTemplate, "private", SAIDMAN_ID, EDA_DIRECTORY_ID, false);
        insertFavorite(jdbcFavoriteInsert, newDir1, PEPE_ID);
        insertFavorite(jdbcFavoriteInsert, newDir2, SAIDMAN_ID);
        insertFavorite(jdbcFavoriteInsert, newDir3, PEPE_ID);
        insertFavorite(jdbcFavoriteInsert, newDir4, PEPE_ID);

        List<Directory> favorites = directoryDao.getFavorites(PEPE_ID);
        assertEquals(2, favorites.size());
        favorites.stream().filter(d -> d.getId().equals(newDir1)).findAny().orElseThrow(AssertionError::new);
        favorites.stream().filter(d -> d.getId().equals(newDir2)).findAny().ifPresent(d -> {throw new AssertionError();});
        favorites.stream().filter(d -> d.getId().equals(newDir3)).findAny().orElseThrow(AssertionError::new);
        favorites.stream().filter(d -> d.getId().equals(newDir4)).findAny().ifPresent(d -> {throw new AssertionError();});
    }
    @Test
    public void testAddFavorite() {
        boolean success = directoryDao.addFavorite(PEPE_ID, EDA_DIRECTORY_ID);
        assertTrue(success);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Favorites", "user_id = '" + PEPE_ID + "' AND directory_id = '" + EDA_DIRECTORY_ID + "'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Favorites", "user_id = '" + SAIDMAN_ID + "' AND directory_id = '" + EDA_DIRECTORY_ID + "'"));
    }

    @Test
    public void testRemoveFavorite() {
        UUID newDirId = insertDirectory(namedParameterJdbcTemplate, "temp", PEPE_ID, EDA_DIRECTORY_ID);
        insertFavorite(jdbcFavoriteInsert, newDirId, PEPE_ID);
        insertFavorite(jdbcFavoriteInsert, newDirId, SAIDMAN_ID);
        directoryDao.removeFavorite(PEPE_ID, newDirId);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Favorites", "user_id = '" + PEPE_ID + "' AND directory_id = '" + newDirId + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Favorites", "user_id = '" + SAIDMAN_ID + "' AND directory_id = '" + newDirId + "'"));
    }

}
