// src/components/Row/RowSubject.tsx

import EditIcon from '@mui/icons-material/Edit';
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import {
  TableRow,
  TableCell,
  IconButton,
  Tooltip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
} from '@mui/material';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';

import {
  useUpdateSubjectMutation,
  useUnlinkSubjectCareerMutation,
} from '../../store/slices/institutionsApiSlice';
import { SubjectWithCareer } from '../../types';

interface RowSubjectProps {
  data: SubjectWithCareer;
  institutionId: string;
  careerId: string;
}

const RowSubject: React.FC<RowSubjectProps> = ({
  data,
  institutionId,
  careerId,
}) => {
  const { t } = useTranslation('rowSubject');

  const [updateSubject] = useUpdateSubjectMutation();
  const [unlinkSubjectCareer] = useUnlinkSubjectCareerMutation();

  // Local states for edit modal
  const [openEdit, setOpenEdit] = useState(false);
  const [newName, setNewName] = useState(data.name);
  const [newYear, setNewYear] = useState(data.year);

  // Local state for unlink confirmation
  const [openUnlink, setOpenUnlink] = useState(false);

  // Edit handlers
  const handleEditClick = () => {
    setNewName(data.name);
    setNewYear(data.year);
    setOpenEdit(true);
  };

  const handleEditSave = async () => {
    try {
      if (!data.subjectId) {
        throw new Error('Missing subject ID');
      }

      // RTK Query: This will do two PUT calls internally:
      // 1) /subjects/:subjectId -> { name: newName }
      // 2) /:inst/careers/:car/subjectcareers/:subjectCareerId -> { year: newYear }
      await updateSubject({
        subjectId: data.subjectId,
        institutionId,
        careerId,
        name: newName,
        year: newYear,
      }).unwrap();

      toast.success(t('modals.edit.success'));
    } catch (error) {
      console.error('Failed to update subject:', error);
      toast.error(t('modals.edit.error'));
    } finally {
      setOpenEdit(false);
    }
  };

  // Unlink handlers
  const handleUnlinkClick = () => {
    setOpenUnlink(true);
  };

  const handleUnlinkConfirm = async () => {
    try {
      if (!data.subjectId) {
        throw new Error('Missing subjectId for unlink');
      }

      // Perform the unlink using subjectCareerId
      await unlinkSubjectCareer({
        institutionId,
        careerId,
        subjectId: data.subjectId,
      }).unwrap();

      toast.success(t('modals.unlink.success'));
    } catch (error) {
      console.error('Failed to unlink subject:', error);
      toast.error(t('modals.unlink.error'));
    } finally {
      setOpenUnlink(false);
    }
  };

  return (
    <>
      <TableRow hover>
        <TableCell>{data.name}</TableCell>
        <TableCell>{data.year}</TableCell>
        <TableCell align="right">
          <Tooltip title={t('actions.edit')}>
            <IconButton color="primary" onClick={handleEditClick} size="small">
              <EditIcon />
            </IconButton>
          </Tooltip>
          <Tooltip title={t('actions.unlink')}>
            <IconButton color="error" onClick={handleUnlinkClick} size="small">
              <RemoveCircleIcon />
            </IconButton>
          </Tooltip>
        </TableCell>
      </TableRow>

      {/* Edit Modal */}
      <Dialog
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        maxWidth="xs"
        fullWidth
      >
        <DialogTitle>{t('modals.edit.title')}</DialogTitle>
        <DialogContent>
          <TextField
            label={t('modals.edit.nameLabel')}
            value={newName}
            onChange={(e) => setNewName(e.target.value)}
            fullWidth
            margin="normal"
          />
          <TextField
            label={t('modals.edit.yearLabel')}
            type="number"
            value={newYear}
            onChange={(e) => setNewYear(Number(e.target.value))}
            fullWidth
            margin="normal"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenEdit(false)}>
            {t('modals.cancel')}
          </Button>
          <Button onClick={handleEditSave}>{t('modals.save')}</Button>
        </DialogActions>
      </Dialog>

      {/* Unlink Confirmation Modal */}
      <Dialog
        open={openUnlink}
        onClose={() => setOpenUnlink(false)}
        maxWidth="xs"
        fullWidth
      >
        <DialogTitle>{t('modals.unlink.title')}</DialogTitle>
        <DialogContent>
          {t('modals.unlink.confirmation', {
            subjectName: data.name,
          })}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenUnlink(false)}>
            {t('modals.cancel')}
          </Button>
          <Button onClick={handleUnlinkConfirm} color="error">
            {t('modals.confirm')}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default RowSubject;
