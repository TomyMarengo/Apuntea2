package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.directory.RootDirectory;
import ar.edu.itba.paw.models.exceptions.directory.DirectoryNotFoundException;
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
import java.util.HashMap;
import java.util.List;
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
    private DirectoryDao directoryDao;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert jdbcDirectoryInsert;
    private SimpleJdbcInsert jdbcFavoriteInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcDirectoryInsert = new SimpleJdbcInsert(ds)
                .withTableName(DIRECTORIES)
                .usingColumns(DIRECTORY_ID, DIRECTORY_NAME, USER_ID, PARENT_ID);
        jdbcFavoriteInsert = new SimpleJdbcInsert(ds)
                    .withTableName(FAVORITES);
    }

    @Test
    public void testDirectoryById() {
        Directory directory = directoryDao.getDirectoryById(EDA_DIRECTORY_ID, PEPE_ID).orElseThrow(DirectoryNotFoundException::new);
        assertEquals(EDA_DIRECTORY_ID, directory.getId());
        assertEquals("EDA", directory.getName());
        assertNull(directory.getParentId());
    }

    @Test
    public void testRootAncestor() {
        DirectoryPath path = directoryDao.getDirectoryPath(GUIAS_DIRECTORY_ID);

        RootDirectory rootDirectory = path.getRootDirectory();
        Directory parentDirectory = path.getParentDirectory();

        assertEquals(EDA_DIRECTORY_ID, rootDirectory.getId());
        assertEquals("EDA", rootDirectory.getName());
        assertNull(rootDirectory.getParentId());
        assertEquals(EDA_ID, rootDirectory.getSubject().getSubjectId());
        assertEquals("EDA", rootDirectory.getSubject().getName());
        assertEquals(parentDirectory.getId(), rootDirectory.getId());
    }

    @Test
    public void testRootAncestorMoreLevels() {
        DirectoryPath path = directoryDao.getDirectoryPath(MVC_DIRECTORY_ID);

        RootDirectory rootDirectory = path.getRootDirectory();
        Directory parentDirectory = path.getParentDirectory();
        Directory currentDirectory = path.getCurrentDirectory();

        assertEquals(PAW_DIRECTORY_ID, rootDirectory.getId());
        assertEquals("PAW", rootDirectory.getName());
        assertNull(rootDirectory.getParentId());
        assertEquals("PAW", rootDirectory.getSubject().getName());
        assertEquals(THEORY_DIRECTORY_ID, parentDirectory.getId());
        assertEquals(MVC_DIRECTORY_ID, currentDirectory.getId());
    }

    @Test
    public void testDelete() {
        int qtyBasuraPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_name LIKE " + "'%Basura%'");
        directoryDao.delete(new UUID[]{BASURA_ID}, PEPE_ID);
        assertEquals(2, qtyBasuraPrev);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_name LIKE " + "'%Basura%'"));
    }

    @Test
    public void testCreate() {
        UUID directoryId = directoryDao.create("Nueva basura", EDA_DIRECTORY_ID, PEPE_ID, true, "BBBBBB");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + directoryId + "' AND directory_name = 'Nueva basura' AND parent_id = '" + EDA_DIRECTORY_ID + "' AND user_id = '" + PEPE_ID + "'"));
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "Directories", "user_id = '" + PEPE_ID + "' AND directory_name = 'Nueva basura' AND parent_id = '" + EDA_DIRECTORY_ID + "'");
    }

   // TODO: Change test to use JdbcTestUtils methods
    @Test
    public void testDeleteMany() {
        UUID[] directoryIds = {TMP_DIR_ID_1, TMP_DIR_ID_2, TMP_DIR_ID_3, TMP_DIR_ID_4};
        String[] names = {"tmp1", "tmp2", "tmp3", "tmp4"};
        jdbcDirectoryInsert.execute(new HashMap<String, Object>(){{
            put(DIRECTORY_ID, TMP_PARENT_DIR_ID);
            put(DIRECTORY_NAME, "tmp");
            put(USER_ID, PEPE_ID);
            put(PARENT_ID, EDA_DIRECTORY_ID);
        }});
        HashMap<String, Object> args = new HashMap<String, Object>(){{
            put(USER_ID, PEPE_ID);
            put(PARENT_ID, TMP_PARENT_DIR_ID);
        }};
        for (int i = 0; i < directoryIds.length; i++) {
            args.put(DIRECTORY_NAME, names[i]);
            args.put(DIRECTORY_ID, directoryIds[i]);
            jdbcDirectoryInsert.execute(args);
        }
        int countInserted = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DIRECTORIES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + TMP_PARENT_DIR_ID + "'");
        directoryDao.delete(directoryIds, PEPE_ID);
        int countPostDelete = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DIRECTORIES, "user_id = '" + PEPE_ID + "' AND parent_id = '" + TMP_PARENT_DIR_ID + "'");
        assertEquals(4, countInserted);
        assertEquals(0, countPostDelete);
    }

    @Test
    public void testAddFavorite() {
        directoryDao.addFavorite(PEPE_ID, EDA_DIRECTORY_ID);
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


}
