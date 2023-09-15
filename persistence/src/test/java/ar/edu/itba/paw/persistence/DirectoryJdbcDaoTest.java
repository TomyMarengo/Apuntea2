package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
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

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DirectoryJdbcDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private DirectoryDao directoryDao;

    private JdbcTemplate jdbcTemplate;

    // TODO: Group IDs in a single file?
    private static UUID ITBA_ID = UUID.fromString("10000000-0000-0000-0000-000000000000");
    private static UUID ING_INF = UUID.fromString("c0000000-0000-0000-0000-000000000000");
    private static UUID EDA_ID = UUID.fromString("50000000-0000-0000-0000-000000000000");
    private static UUID EDA_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000000");
    private static UUID PAW_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000001");
    private static UUID GUIAS_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000005");
    private static UUID MVC_DIRECTORY_ID = UUID.fromString("d0000000-0000-0000-0000-000000000008");

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testSearchByInstitution() {
        SearchArguments sa = new SearchArguments(ITBA_ID, null, null, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(4, directories.size());
    }

    @Test
    public void testSearchByCareer(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, null, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(2, directories.size());
    }

    @Test
    public void testBySubject(){
        SearchArguments sa = new SearchArguments(ITBA_ID, ING_INF, EDA_ID, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(1, directories.size());
    }

    @Test
    public void testOrderBy(){
        SearchArguments sa = new SearchArguments(null, null, null, null, null, null, "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        for (int i = 0; i < directories.size() - 2; i++) {
            assertTrue(directories.get(i).getName().toUpperCase().compareTo(directories.get(i + 1).getName().toUpperCase()) <= 0);
        }
    }

    @Test
    public void testByPage() {
        SearchArguments sa = new SearchArguments(null, null, null, null, null, null, "name", true, 1, 2);
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
        SearchArguments sa = new SearchArguments(null, null, null, null, null, "can", "name", true, 1, 10);
        List<Directory> directories = directoryDao.search(sa);
        assertEquals(2, directories.size());
        assertEquals("Dinamica de Fluidos", directories.get(0).getName());
        assertEquals("Mecanica Gral", directories.get(1).getName());
    }

    @Test
    public void testRootAncestor() {
        RootDirectory directory = directoryDao.getRootAncestor(GUIAS_DIRECTORY_ID);
        assertEquals(EDA_DIRECTORY_ID, directory.getDirectoryId());
        assertEquals("EDA", directory.getName());
        assertNull(directory.getParentId());
        assertEquals(directory.getSubject().getSubjectId(), EDA_ID);
        assertEquals(directory.getSubject().getName(), "EDA");
    }

    @Test
    public void testRootAncestorMoreLevels() {
        RootDirectory directory = directoryDao.getRootAncestor(MVC_DIRECTORY_ID);
        assertEquals(PAW_DIRECTORY_ID, directory.getDirectoryId());
        assertEquals("PAW", directory.getName());
        assertNull(directory.getParentId());
        assertEquals(directory.getSubject().getName(), "PAW");
    }

}
