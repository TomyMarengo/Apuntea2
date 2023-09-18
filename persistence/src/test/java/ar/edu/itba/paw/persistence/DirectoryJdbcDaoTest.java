package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.RootDirectory;
import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import static ar.edu.itba.paw.persistence.JdbcTestConstants.*;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DirectoryJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private DirectoryDao directoryDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testSearchByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(5, directories.size());
    }

    @Test
    public void testSearchByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(3, directories.size());
    }

    @Test
    public void testBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(1, directories.size());
    }

    @Test
    public void testOrderBy(){
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        for (int i = 0; i < directories.size() - 2; i++) {
            assertTrue(directories.get(i).getName().toUpperCase().compareTo(directories.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testByPage() {
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "name", true, 1, 2);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(2, directories.size());
    }

    @Test
    public void testDirectoryById() {
        Directory directory = directoryDao.getDirectoryById(EDA_DIRECTORY_ID);
        assertEquals(EDA_DIRECTORY_ID, directory.getDirectoryId());
        assertEquals("EDA", directory.getName());
        assertNull(directory.getParentId());
    }

    @Test
    public void testChildren() {
        List<Directory> directories = directoryDao.getChildren(EDA_DIRECTORY_ID);
        assertEquals(2, directories.size());
        assertEquals("Guias", directories.get(0).getName());
        assertEquals("1eros parciales", directories.get(1).getName());
    }

    @Test
    public void testByWord() {
        SearchArguments sa = new SearchArguments(null, null, null, null, "can", "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(3, directories.size());
        assertEquals("Dinamica de Fluidos", directories.get(0).getName());
        assertEquals("Matematica I", directories.get(1).getName());
        assertEquals("Mecanica Gral", directories.get(2).getName());
    }

    @Test
    public void testRootAncestor() {
        DirectoryPath path = directoryDao.getDirectoryPath(GUIAS_DIRECTORY_ID);

        RootDirectory rootDirectory = path.getRootDirectory();
        Directory parentDirectory = path.getParentDirectory();

        assertEquals(EDA_DIRECTORY_ID, rootDirectory.getDirectoryId());
        assertEquals("EDA", rootDirectory.getName());
        assertNull(rootDirectory.getParentId());
        assertEquals(EDA_ID, rootDirectory.getSubject().getSubjectId());
        assertEquals("EDA", rootDirectory.getSubject().getName());
        assertEquals(parentDirectory.getDirectoryId(), rootDirectory.getDirectoryId());
    }

    @Test
    public void testRootAncestorMoreLevels() {
        DirectoryPath path = directoryDao.getDirectoryPath(MVC_DIRECTORY_ID);

        RootDirectory rootDirectory = path.getRootDirectory();
        Directory parentDirectory = path.getParentDirectory();
        Directory currentDirectory = path.getCurrentDirectory();

        assertEquals(PAW_DIRECTORY_ID, rootDirectory.getDirectoryId());
        assertEquals("PAW", rootDirectory.getName());
        assertNull(rootDirectory.getParentId());
        assertEquals("PAW", rootDirectory.getSubject().getName());
        assertEquals(THEORY_DIRECTORY_ID, parentDirectory.getDirectoryId());
        assertEquals(MVC_DIRECTORY_ID, currentDirectory.getDirectoryId());
    }

    @Test
    public void testDelete() {
        int qtyBasuraPrev = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_name LIKE " + "'%Basura%'");
        directoryDao.delete(BASURA_ID);
        assertEquals(2, qtyBasuraPrev);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_name LIKE " + "'%Basura%'"));
    }

    @Test
    public void testCreate() {
        Directory directory = directoryDao.create("Nueva basura", EDA_DIRECTORY_ID, PEPE_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + directory.getDirectoryId() + "' AND directory_name = 'Nueva basura' AND parent_id = '" + EDA_DIRECTORY_ID + "' AND user_id = '" + PEPE_ID + "'"));
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "Directories", "user_id = '" + PEPE_ID + "' AND directory_name = 'Nueva basura' AND parent_id = '" + EDA_DIRECTORY_ID + "'");
    }

}
