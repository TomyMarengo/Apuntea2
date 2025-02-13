package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationCodeDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class VerificationCodesServiceImplTest {
    @Mock
    private VerificationCodeDao verificationCodesDao;

    @Mock
    private UserDao userDao;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VerificationCodesServiceImpl verificationCodesService;

    @Test
    public void testVerifyForgotPasswordCode() {
        Mockito.when(verificationCodesDao.verifyForgotPasswordCode(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(verificationCodesDao.deleteVerificationCodes(Mockito.any())).thenReturn(true);
        Assert.assertTrue(verificationCodesService.verifyForgotPasswordCode("user@mail.com", "code"));
    }

    @Test
    public void testVerifyForgotPasswordCodeInvalidCode() {
        Mockito.when(verificationCodesDao.verifyForgotPasswordCode(Mockito.any(), Mockito.any())).thenReturn(false);
        Assert.assertFalse(verificationCodesService.verifyForgotPasswordCode("user@mail.com", "code"));
    }

    @Test
    public void testVerifyForgotPasswordCodeInvalidEmail() {
        Mockito.when(verificationCodesDao.verifyForgotPasswordCode(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(verificationCodesDao.deleteVerificationCodes(Mockito.any())).thenReturn(false);
        Assert.assertFalse(verificationCodesService.verifyForgotPasswordCode("user@mail.com", "code"));
    }
}
