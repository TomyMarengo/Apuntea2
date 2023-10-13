package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)

public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetUsersSuccess() {
        final int PAGE_SIZE = 10;
        final int PAGE = 2;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(userDao.getStudentsQuantity(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>());

        Page<User> results = userService.getStudents(null, PAGE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(PAGE, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetUsersOverPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = 6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(userDao.getStudentsQuantity(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>());

        Page<User> results = userService.getStudents(null, PAGE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(results.getTotalPages(), results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test
    public void testGetUsersUnderPaged() {
        final int PAGE_SIZE = 10;
        final int PAGE = -6;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(userDao.getStudentsQuantity(Mockito.any())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>());

        Page<User> results = userService.getStudents(null, PAGE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(1, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

}
