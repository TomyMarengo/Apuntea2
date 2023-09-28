package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.RootDirectory;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static ar.edu.itba.paw.persistence.JdbcTestConstants.*;

import javax.sql.DataSource;
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

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testDirectoryById() {
        Directory directory = directoryDao.getDirectoryById(EDA_DIRECTORY_ID, PEPE_ID);
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
        directoryDao.delete(BASURA_ID, PEPE_ID);
        assertEquals(2, qtyBasuraPrev);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_name LIKE " + "'%Basura%'"));
    }

    @Test
    public void testCreate() {
        UUID directoryId = directoryDao.create("Nueva basura", EDA_DIRECTORY_ID, PEPE_ID, true, "#BBBBBB");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Directories", "directory_id = '" + directoryId + "' AND directory_name = 'Nueva basura' AND parent_id = '" + EDA_DIRECTORY_ID + "' AND user_id = '" + PEPE_ID + "'"));
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "Directories", "user_id = '" + PEPE_ID + "' AND directory_name = 'Nueva basura' AND parent_id = '" + EDA_DIRECTORY_ID + "'");
    }

}
