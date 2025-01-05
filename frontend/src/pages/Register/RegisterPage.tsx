// src/pages/Register/RegisterPage.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Visibility,
  VisibilityOff,
  Close as CloseIcon,
} from '@mui/icons-material';
import {
  TextField,
  Button,
  IconButton,
  InputAdornment,
  Box,
  Typography,
  Card,
  CardContent,
  CircularProgress,
  Autocomplete,
} from '@mui/material';
import { useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { toast } from 'react-toastify';
import { z } from 'zod';

import useRegister from '../../hooks/useRegister';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
} from '../../store/slices/institutionsApiSlice';

export default function RegisterPage() {
  const { t } = useTranslation('registerPage');
  const navigate = useNavigate();

  const { registerUser } = useRegister();

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [loading, setLoading] = useState(false);
  const [registerError, setRegisterError] = useState<string | null>(null);

  const registerSchema = z
    .object({
      email: z.string().email({ message: t('validation.invalidEmail') }),
      password: z
        .string()
        .regex(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
          t('validation.passwordInvalid'),
        )
        .min(4, { message: t('validation.passwordMinLength') })
        .max(50, { message: t('validation.passwordMaxLength') }),
      confirmPassword: z
        .string()
        .min(4, { message: t('validation.passwordMinLength') })
        .max(50, { message: t('validation.passwordMaxLength') }),
      institutionId: z
        .string()
        .nonempty({ message: t('validation.youMustSelectInstitution') }),
      careerId: z
        .string()
        .nonempty({ message: t('validation.youMustSelectCareer') }),
    })
    .refine((data) => data.password === data.confirmPassword, {
      path: ['confirmPassword'],
      message: t('validation.passwordsDoNotMatch'),
    });

  type RegisterForm = z.infer<typeof registerSchema>;

  const {
    control,
    register,
    handleSubmit,
    watch,
    reset,
    setValue,
    formState: { errors },
  } = useForm<RegisterForm>({
    resolver: zodResolver(registerSchema),
    mode: 'onChange',
  });

  const selectedInstitution = watch('institutionId');

  const { data: institutions } = useGetInstitutionsQuery(undefined);
  const { data: careers, isFetching: isFetchingCareers } = useGetCareersQuery(
    { institutionId: selectedInstitution },
    { skip: !selectedInstitution },
  );

  const onSubmit = async (data: RegisterForm) => {
    try {
      setRegisterError(null);
      setLoading(true);

      await registerUser({
        email: data.email,
        password: data.password,
        institutionId: data.institutionId,
        careerId: data.careerId,
      });

      toast.success(t('toast.success'));
      navigate('/');
    } catch (err: any) {
      toast.error(t('toast.error'));
      setRegisterError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleTogglePassword = () => {
    setShowPassword((prev) => !prev);
  };

  const handleToggleConfirmPassword = () => {
    setShowConfirmPassword((prev) => !prev);
  };

  const handleClearField = (fieldName: keyof RegisterForm) => {
    reset({ ...watch(), [fieldName]: '' });
  };

  let pageTitle = t('titlePage');
  if (loading) {
    pageTitle = t('loading');
  } else if (registerError) {
    pageTitle = t('errorFetching', { error: String(registerError) });
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '80vh',
        }}
      >
        <Card sx={{ maxWidth: 500, width: '100%', mx: 2 }}>
          <CardContent>
            <Typography variant="h4" gutterBottom align="center">
              {t('signup')}
            </Typography>

            <Box component="form" onSubmit={handleSubmit(onSubmit)}>
              <TextField
                label={t('email')}
                variant="outlined"
                fullWidth
                margin="normal"
                {...register('email')}
                error={!!errors.email}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.email ? t(errors.email.message as string) : ''}
                  </Box>
                }
                InputProps={{
                  endAdornment: watch('email') ? (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => handleClearField('email')}
                        tabIndex={-1}
                      >
                        <CloseIcon />
                      </IconButton>
                    </InputAdornment>
                  ) : null,
                }}
              />
              <TextField
                label={t('password')}
                variant="outlined"
                fullWidth
                margin="normal"
                type={showPassword ? 'text' : 'password'}
                {...register('password')}
                error={!!errors.password}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.password
                      ? t(errors.password.message as string)
                      : ''}
                  </Box>
                }
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton onClick={handleTogglePassword} tabIndex={-1}>
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                      {watch('password') && (
                        <IconButton
                          onClick={() => handleClearField('password')}
                          tabIndex={-1}
                        >
                          <CloseIcon />
                        </IconButton>
                      )}
                    </InputAdornment>
                  ),
                }}
              />
              <TextField
                label={t('confirmPassword')}
                variant="outlined"
                fullWidth
                margin="normal"
                type={showConfirmPassword ? 'text' : 'password'}
                {...register('confirmPassword')}
                error={!!errors.confirmPassword}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.confirmPassword
                      ? t(errors.confirmPassword.message as string)
                      : ''}
                  </Box>
                }
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={handleToggleConfirmPassword}
                        tabIndex={-1}
                      >
                        {showConfirmPassword ? (
                          <VisibilityOff />
                        ) : (
                          <Visibility />
                        )}
                      </IconButton>
                      {watch('confirmPassword') && (
                        <IconButton
                          onClick={() => handleClearField('confirmPassword')}
                          tabIndex={-1}
                        >
                          <CloseIcon />
                        </IconButton>
                      )}
                    </InputAdornment>
                  ),
                }}
              />
              <Controller
                name="institutionId"
                control={control}
                render={({ field }) => (
                  <Autocomplete
                    {...field}
                    options={institutions || []}
                    getOptionLabel={(option: any) => option.name}
                    value={
                      institutions
                        ? institutions.find(
                            (inst: any) => inst.id === field.value,
                          ) || null
                        : null
                    }
                    onChange={(_, newValue) => {
                      field.onChange(newValue ? newValue.id : '');
                      setValue('careerId', '');
                    }}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label={t('institution')}
                        variant="outlined"
                        margin="normal"
                        error={!!errors.institutionId}
                        helperText={
                          errors.institutionId
                            ? t(errors.institutionId.message as string)
                            : ''
                        }
                      />
                    )}
                    isOptionEqualToValue={(option, value) =>
                      option.id === value.id
                    }
                    sx={{ minWidth: 180, width: '100%' }}
                  />
                )}
              />
              <Controller
                name="careerId"
                control={control}
                render={({ field }) => (
                  <Autocomplete
                    {...field}
                    options={careers || []}
                    getOptionLabel={(option: any) => option.name}
                    value={
                      careers
                        ? careers.find((car: any) => car.id === field.value) ||
                          null
                        : null
                    }
                    onChange={(_, newValue) => {
                      field.onChange(newValue ? newValue.id : '');
                    }}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label={t('career')}
                        variant="outlined"
                        margin="normal"
                        error={!!errors.careerId}
                        helperText={
                          errors.careerId
                            ? t(errors.careerId.message as string)
                            : ''
                        }
                        disabled={!watch('institutionId') || isFetchingCareers}
                      />
                    )}
                    isOptionEqualToValue={(option, value) =>
                      option.id === value.id
                    }
                    sx={{ minWidth: 180, width: '100%' }}
                  />
                )}
              />
              {registerError && (
                <Typography variant="body2" color="error" sx={{ mt: 1 }}>
                  {registerError}
                </Typography>
              )}
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ mt: 2 }}
                disabled={loading}
              >
                {loading ? <CircularProgress size={24} /> : t('signup')}
              </Button>
            </Box>
            <Typography variant="body2" align="center" sx={{ mt: 2 }}>
              {t('alreadyHaveAccount')}{' '}
              <RouterLink to="/login" style={{ color: '#1976d2' }}>
                {t('login')}
              </RouterLink>
            </Typography>
          </CardContent>
        </Card>
      </Box>
    </>
  );
}
