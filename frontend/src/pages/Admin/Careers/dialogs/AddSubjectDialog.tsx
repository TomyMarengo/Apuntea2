// src/pages/Admin/Careers/dialogs/AddSubjectDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  CircularProgress,
  Box,
  TextField,
  Autocomplete,
} from '@mui/material';
import React, { useEffect, useMemo } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import {
  useGetSubjectsNotInCareerQuery,
  useLinkSubjectCareerMutation,
} from '../../../../store/slices/institutionsApiSlice';
import { Subject } from '../../../../types';

interface AddSubjectDialogProps {
  open: boolean;
  onClose: () => void;
  selectedInstitutionId: string;
  selectedCareerId: string;
  refetchSubjects: () => void;
  refetchSubjectCareers: () => void;
}

const AddSubjectDialog: React.FC<AddSubjectDialogProps> = ({
  open,
  onClose,
  selectedInstitutionId,
  selectedCareerId,
  refetchSubjects,
  refetchSubjectCareers,
}) => {
  const { t } = useTranslation('addSubjectDialog');

  const {
    data: subjectsNotInCareer,
    refetch: refetchNotInCareer,
    isLoading: loadingNotInCareer,
  } = useGetSubjectsNotInCareerQuery(
    {
      careerId: selectedCareerId, // TODO: pass notInCareerUrl
    },
    { skip: !selectedCareerId },
  );

  const [linkSubjectCareer, { isLoading: linkingSubject }] =
    useLinkSubjectCareerMutation();

  const AddSubjectFormSchema = z.object({
    subjectId: z
      .string()
      .uuid(t('validation.subjectInvalid'))
      .nonempty(t('validation.subjectNotEmpty')),
    year: z
      .number({ invalid_type_error: t('validation.yearInvalid') })
      .positive(t('validation.yearPositive'))
      .max(5, t('validation.yearMax')),
  });

  type AddSubjectFormData = z.infer<typeof AddSubjectFormSchema>;

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    reset,
    formState: { errors },
  } = useForm<AddSubjectFormData>({
    resolver: zodResolver(AddSubjectFormSchema),
    defaultValues: {
      subjectId: '',
      year: 1,
    },
  });

  const watchSubjectId = watch('subjectId');
  const watchYear = watch('year');

  useEffect(() => {
    if (open) {
      refetchNotInCareer();
      reset({
        subjectId: '',
        year: 1,
      });
    }
  }, [open, refetchNotInCareer, reset]);

  // Find the currently selected subject object
  const selectedSubjectObject = useMemo(() => {
    return (
      subjectsNotInCareer?.find((sub) => sub.id === watchSubjectId) || null
    );
  }, [subjectsNotInCareer, watchSubjectId]);

  // Handle form submission
  const onSubmit = async (data: AddSubjectFormData) => {
    if (!selectedInstitutionId || !selectedCareerId || !data.subjectId) return;
    try {
      await linkSubjectCareer({
        institutionId: selectedInstitutionId,
        careerId: selectedCareerId,
        subjectId: data.subjectId,
        year: data.year,
      }).unwrap();

      toast.success(t('toast.success'));
      refetchSubjects();
      refetchSubjectCareers();
      onClose();
    } catch (error) {
      console.error('Failed to link subject:', error);
      toast.error(t('toast.error'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle>{t('title')}</DialogTitle>
      <DialogContent>
        {loadingNotInCareer ? (
          <Box sx={{ textAlign: 'center', py: 3 }}>
            <CircularProgress />
          </Box>
        ) : (
          <>
            {/* Subject selection */}
            <Autocomplete
              options={subjectsNotInCareer || []}
              getOptionLabel={(option: Subject) => option.name || ''}
              value={selectedSubjectObject}
              onChange={(_, newValue) => {
                setValue('subjectId', newValue?.id || '');
              }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label={t('selectSubject')}
                  variant="outlined"
                  margin="normal"
                  fullWidth
                  error={!!errors.subjectId}
                  helperText={errors.subjectId?.message}
                />
              )}
              sx={{ mt: 2, width: '100%' }}
            />

            {/* Year field */}
            <TextField
              sx={{ mt: 2 }}
              type="number"
              fullWidth
              label={t('yearLabel')}
              {...register('year', { valueAsNumber: true })}
              value={watchYear}
              onChange={(e) => setValue('year', Number(e.target.value))}
              error={!!errors.year}
              helperText={errors.year?.message}
            />
          </>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('cancel')}</Button>
        <Button
          onClick={handleSubmit(onSubmit)}
          disabled={!watchSubjectId || linkingSubject || loadingNotInCareer}
        >
          {linkingSubject ? <CircularProgress size={20} /> : t('confirm')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AddSubjectDialog;
