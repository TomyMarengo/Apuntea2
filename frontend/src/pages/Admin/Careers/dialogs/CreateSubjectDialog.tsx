// src/pages/Admin/Careers/dialogs/CreateSubjectDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  CircularProgress,
  TextField,
} from '@mui/material';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useCreateSubjectMutation } from '../../../../store/slices/institutionsApiSlice';

interface CreateSubjectDialogProps {
  open: boolean;
  onClose: () => void;
  selectedInstitutionId: string;
  selectedCareerId: string;
  refetchSubjects: () => void;
  refetchSubjectCareers: () => void;
}

const CreateSubjectDialog: React.FC<CreateSubjectDialogProps> = ({
  open,
  onClose,
  selectedInstitutionId,
  selectedCareerId,
  refetchSubjects,
  refetchSubjectCareers,
}) => {
  const { t } = useTranslation('createSubjectDialog');
  const [createSubject, { isLoading: creatingSubject }] =
    useCreateSubjectMutation();

  const CreateSubjectFormSchema = z.object({
    name: z
      .string()
      .min(1, { message: t('validation.nameMinLength') })
      .max(50, { message: t('validation.nameMaxLength') })
      .regex(/^(?!([,_.]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\\-_]+$/, {
        message: t('validation.nameInvalid'),
      }),
    year: z
      .number({ invalid_type_error: t('validation.yearInvalid') })
      .positive({ message: t('validation.yearPositive') })
      .max(5, { message: t('validation.yearMax') }),
  });

  type CreateSubjectFormData = z.infer<typeof CreateSubjectFormSchema>;

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<CreateSubjectFormData>({
    resolver: zodResolver(CreateSubjectFormSchema),
    defaultValues: {
      name: '',
      year: 1,
    },
  });

  const watchName = watch('name');
  const watchYear = watch('year');

  useEffect(() => {
    if (open) {
      reset({
        name: '',
        year: 1,
      });
    }
  }, [open, reset]);

  // Handle form submission
  const onSubmit = async (data: CreateSubjectFormData) => {
    if (!selectedInstitutionId || !selectedCareerId || !data.name) return;
    try {
      await createSubject({
        institutionId: selectedInstitutionId,
        careerId: selectedCareerId,
        name: data.name,
        year: data.year,
      }).unwrap();

      toast.success(t('toast.success'));
      refetchSubjects();
      refetchSubjectCareers();
      onClose();
    } catch (error) {
      console.error('Failed to create subject:', error);
      toast.error(t('toast.error'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle>{t('title')}</DialogTitle>
      <DialogContent>
        {/* Name Field */}
        <TextField
          sx={{ mt: 2 }}
          fullWidth
          label={t('nameLabel')}
          {...register('name')}
          value={watchName}
          onChange={(e) => reset({ ...watch(), name: e.target.value })}
          error={!!errors.name}
          helperText={errors.name?.message}
        />
        {/* Year Field */}
        <TextField
          sx={{ mt: 2 }}
          type="number"
          fullWidth
          label={t('yearLabel')}
          {...register('year', { valueAsNumber: true })}
          value={watchYear}
          onChange={(e) => reset({ ...watch(), year: Number(e.target.value) })}
          error={!!errors.year}
          helperText={errors.year?.message}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('cancel')}</Button>
        <Button
          onClick={handleSubmit(onSubmit)}
          disabled={!watchName || creatingSubject}
        >
          {creatingSubject ? <CircularProgress size={20} /> : t('confirm')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CreateSubjectDialog;
