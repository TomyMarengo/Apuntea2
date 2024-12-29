import React, { useState } from 'react';
import {
  Box,
  TextField,
  Button,
  Typography,
  CircularProgress,
  Divider,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import {
  useLazyGetUserByEmailQuery,
  useRequestPasswordChangeMutation,
  useUpdateUserPasswordMutation,
} from '../../store/slices/usersApiSlice';

const ForgotPasswordPage: React.FC = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [code, setCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [userId, setUserId] = useState<string | null>(null);
  const [step, setStep] = useState<'email' | 'code'>('email');

  const [getUserByEmail, { isLoading: isGettingUser }] = useLazyGetUserByEmailQuery();
  const [requestPasswordChange, { isLoading: isRequestingPasswordChange }] = 
    useRequestPasswordChangeMutation();
  const [updateUserPassword, { isLoading: isUpdatingPassword }] =
    useUpdateUserPasswordMutation();

  const handleEmailSubmit = async () => {
    try {
      console.log('email', email);
      const [ user ] = await getUserByEmail(email).unwrap();
      setUserId(user.id);
      await requestPasswordChange({ email }).unwrap();
      setStep('code');
    } catch (error) {
      console.error(error);
    }
  };

  const handleCodeSubmit = async () => {
    if (!userId) return;
    try {
      await updateUserPassword({ userId, password: newPassword, email, code }).unwrap();
      navigate('/login');
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Box sx={{ p: 4, maxWidth: 600, margin: '0 auto' }}>
      <Typography variant="h4" gutterBottom>
        {t('forgotPasswordPage.title')}
      </Typography>

      {step === 'email' && (
        <Box>
          <Typography variant="body1" gutterBottom>
            {t('forgotPasswordPage.enterEmail')}
          </Typography>
          <TextField
            fullWidth
            label={t('forgotPasswordPage.emailLabel')}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            sx={{ mb: 2 }}
          />
          <Button
            variant="contained"
            fullWidth
            onClick={handleEmailSubmit}
            disabled={isGettingUser || isRequestingPasswordChange || !email}
          >
            {isGettingUser || isRequestingPasswordChange ? <CircularProgress size={24} /> : t('forgotPasswordPage.submitEmail')}
          </Button>
        </Box>
      )}

      {step === 'code' && (
        <Box>
          <Typography variant="body1" gutterBottom>
            {t('forgotPasswordPage.enterCode')}
          </Typography>
          <TextField
            fullWidth
            label={t('forgotPasswordPage.codeLabel')}
            value={code}
            onChange={(e) => setCode(e.target.value)}
            sx={{ mb: 2 }}
          />
          <TextField
            fullWidth
            label={t('forgotPasswordPage.newPasswordLabel')}
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            sx={{ mb: 2 }}
          />
          <Button
            variant="contained"
            fullWidth
            onClick={handleCodeSubmit}
            disabled={isUpdatingPassword || !code || !newPassword}
          >
            {isUpdatingPassword ? <CircularProgress size={24} /> : t('forgotPasswordPage.submitCode')}
          </Button>
        </Box>
      )}

      <Divider sx={{ my: 4 }} />
    </Box>
  );
};

export default ForgotPasswordPage;
