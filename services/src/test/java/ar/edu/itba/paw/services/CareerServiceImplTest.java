package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.persistence.CareerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.services.ServiceTestUtils.mockCareer;
import static ar.edu.itba.paw.services.ServiceTestUtils.mockUser;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CareerServiceImplTest {
    @Mock
    private SecurityService securityService;

    @Mock
    private CareerDao careerDao;

    @InjectMocks
    private CareerServiceImpl careerService;

    @Test
    public void testGetCareerById() {
        Mockito.when(careerDao.getCareerById(Mockito.any())).thenReturn(Optional.of(mockCareer()));
        Optional<Career> maybeCareer = careerService.getCareerById(Mockito.any());
        assertTrue(maybeCareer.isPresent());
    }

    @Test
    public void testGetCareerByIdNotFound() {
        Mockito.when(careerDao.getCareerById(Mockito.any())).thenReturn(Optional.empty());
        Optional<Career> maybeCareer = careerService.getCareerById(Mockito.any());
        assertFalse(maybeCareer.isPresent());
    }
}
