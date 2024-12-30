package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.user.InvalidUserFollowException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.CareerDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)

public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private CareerDao careerDao;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetStudentsSuccess() {
        final int PAGE_SIZE = 10;
        final int PAGE = 2;
        final int TOTAL_RESULTS = PAGE_SIZE * 4 + 1;
        Mockito.when(userDao.getUsersQuantity(Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getUsers(Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getUsers(null, "all", null, null, PAGE, PAGE_SIZE);

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
        Mockito.when(userDao.getUsersQuantity(Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getUsers(Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getUsers(null, "all", null, null, PAGE, PAGE_SIZE);

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
        Mockito.when(userDao.getUsersQuantity(Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull())).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getUsers(Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getUsers(null, "all", null, null, PAGE, PAGE_SIZE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(1, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test(expected = InvalidUserFollowException.class)
    public void testFollowUserFollowsHimself() {
        User mockUser = mockUser();
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser);

        userService.follow(mockUser.getUserId());

        fail();
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetProfilePictureUserNotFound() {
        Mockito.when(userDao.findById(Mockito.any())).thenReturn(Optional.empty());

        userService.getProfilePictureByUserId(UUID.randomUUID());

        fail();
    }

}
