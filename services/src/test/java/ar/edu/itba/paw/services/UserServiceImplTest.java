package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exceptions.InvalidFileException;
import ar.edu.itba.paw.models.exceptions.user.InvalidUserException;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.persistence.CareerDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.services.ServiceTestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

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
        Mockito.when(userDao.getStudentsQuantity(Mockito.any(), null)).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), null, Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getStudents(null, "all", PAGE);

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
        Mockito.when(userDao.getStudentsQuantity(Mockito.any(), null)).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), null, Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getStudents(null, "all", PAGE);

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
        Mockito.when(userDao.getStudentsQuantity(Mockito.any(), null)).thenReturn(TOTAL_RESULTS);
        Mockito.when(userDao.getStudents(Mockito.any(), null, Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        Page<User> results = userService.getStudents(null, "all", PAGE);

        assertEquals(TOTAL_RESULTS, results.getTotalResults());
        assertEquals(PAGE_SIZE, results.getPageSize());
        assertEquals(1, results.getCurrentPage());
        assertEquals(5, results.getTotalPages());
    }

    @Test(expected = InvalidFileException.class)
    public void testInvalidFileUpdatingProfile() {
        Mockito.when(securityService.getCurrentUserOrThrow()).thenReturn(mockUser());
        CommonsMultipartFile profilePicture = Mockito.mock(CommonsMultipartFile.class);
        Mockito.when(profilePicture.isEmpty()).thenReturn(false);
        given(profilePicture.getBytes()).willAnswer(invocation -> {throw new IOException();});
        Mockito.when(careerDao.getCareerById(Mockito.any())).thenReturn(Optional.of(mockCareer()));
        userService.updateProfile("firstName", "lastName", "username", profilePicture, UUID.randomUUID());
        fail();
    }

    // TODO: Test user not found
    @Test(expected = InvalidUserException.class)
    public void testBanUserError() {
        Mockito.when(userDao.findById(Mockito.any())).thenReturn(Optional.of(mockUser()));
        Mockito.when(securityService.getAdminOrThrow()).thenReturn(mockAdmin());
        Mockito.when(userDao.banUser(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        userService.banUser(UUID.randomUUID(), "Hm?");
        fail();
    }

    // TODO: Test user not found
    @Test(expected = InvalidUserException.class)
    public void testUnbanUserError() {
        Mockito.when(userDao.findById(Mockito.any())).thenReturn(Optional.of(mockUser()));
        Mockito.when(userDao.unbanUser(Mockito.any())).thenReturn(false);
        userService.unbanUser(UUID.randomUUID());
        fail();
    }

}
