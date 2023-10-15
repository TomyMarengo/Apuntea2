package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.persistence.VerificationCodeDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VerificationCodesServiceImplTest {
    @Mock
    private VerificationCodeDao verificationCodesDao;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VerificationCodesServiceImpl verificationCodesService;

    @Test(expected = UserNotFoundException.class)
    public void testVerifyInvalidCode() {
        Mockito.doThrow(new UserNotFoundException()).when(verificationCodesDao).saveVerificationCode(Mockito.any());
        verificationCodesService.sendForgotPasswordCode("email");
        Assert.fail();
    }


    @Test
    public void testVerifyForgotPasswordCode() {
        Mockito.when(verificationCodesDao.verifyForgotPasswordCode(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(verificationCodesDao.deleteVerificationCodes(Mockito.any())).thenReturn(true);
        Assert.assertTrue(verificationCodesService.verifyForgotPasswordCode("email", "code"));
    }

    @Test
    public void testVerifyForgotPasswordCodeInvalidCode() {
        Mockito.when(verificationCodesDao.verifyForgotPasswordCode(Mockito.any(), Mockito.any())).thenReturn(false);
        Assert.assertFalse(verificationCodesService.verifyForgotPasswordCode("email", "code"));
    }

    @Test
    public void testVerifyForgotPasswordCodeInvalidEmail() {
        Mockito.when(verificationCodesDao.verifyForgotPasswordCode(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(verificationCodesDao.deleteVerificationCodes(Mockito.any())).thenReturn(false);
        Assert.assertFalse(verificationCodesService.verifyForgotPasswordCode("email", "code"));
    }
}
