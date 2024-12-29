// src/components/CreateDirectoryFab.tsx

import React, { useState, useRef } from 'react';
import {
  Box,
  Fab,
  Tooltip,
  Typography,
  TextField,
  Button,
  FormControlLabel,
  Switch,
  Paper,
  ClickAwayListener,
  CircularProgress,
  ToggleButton,
  ToggleButtonGroup,
} from '@mui/material';
import CreateNewFolderIcon from '@mui/icons-material/CreateNewFolder';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useCreateDirectoryMutation } from '../store/slices/directoriesApiSlice';
import { z } from 'zod';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

// Define the color options
const colorOptions = [
  { label: 'Gray', value: '#BBBBBB' },
  { label: 'Green', value: '#16A765' },
  { label: 'Blue', value: '#4986E7' },
  { label: 'Pink', value: '#CD35A6' },
];

// Define the Zod schema for form validation
const directorySchema = z.object({
  name: z
    .string()
    .regex(
      /^(?!([ ,\-_.]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]+$/,
      'createDirectoryFab.invalidName',
    ),
  iconColor: z.enum(['#BBBBBB', '#16A765', '#4986E7', '#CD35A6'], {
    errorMap: () => ({ message: 'createDirectoryFab.invalidColor' }),
  }),
  visible: z.boolean(),
});

type DirectoryFormData = z.infer<typeof directorySchema>;

interface CreateDirectoryFabProps {
  parentId: string;
}

const CreateDirectoryFab: React.FC<CreateDirectoryFabProps> = ({
  parentId,
}) => {
  const { t } = useTranslation();
  const [createDirectory, { isLoading }] = useCreateDirectoryMutation();

  // Tracks if the form is expanded
  const [expanded, setExpanded] = useState(false);

  // Reference to the wrapper for ClickAwayListener
  const containerRef = useRef<HTMLDivElement | null>(null);

  // Initialize react-hook-form with Zod resolver
  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<DirectoryFormData>({
    resolver: zodResolver(directorySchema),
    defaultValues: {
      name: '',
      iconColor: '#BBBBBB',
      visible: true,
    },
  });

  // Toggle the form expansion
  const handleFabClick = () => {
    setExpanded((prev) => !prev);
  };

  // Close the form when clicking outside
  const handleClickAway = (_event: MouseEvent | TouchEvent) => {
    if (expanded) {
      setExpanded(false);
    }
  };

  // Handle form submission
  const onSubmit = async (data: DirectoryFormData) => {
    try {
      const result = await createDirectory({
        name: data.name,
        iconColor: data.iconColor,
        visible: data.visible,
        parentId,
      }).unwrap();

      if (result) {
        toast.success(t('createDirectoryFab.directoryCreated'));
        reset();
        setExpanded(false);
      } else {
        toast.error(t('createDirectoryFab.directoryCreationFailed'));
      }
    } catch (error: any) {
      toast.error(
        error?.data?.[0]?.message ||
          t('createDirectoryFab.directoryCreationFailed'),
      );
      console.error('Failed to create directory:', error);
    }
  };

  return (
    <ClickAwayListener onClickAway={handleClickAway}>
      <Box ref={containerRef}>
        {/* Floating Action Button (always visible) */}
        {!expanded && (
          <Tooltip
            title={t('createDirectoryFab.createNewDirectory')}
            placement="left"
          >
            <Fab
              color="primary"
              onClick={handleFabClick}
              sx={{
                cursor: 'pointer',
              }}
            >
              <CreateNewFolderIcon sx={{ color: 'background.paper' }} />
            </Fab>
          </Tooltip>
        )}

        {/* Expanded Form */}
        {expanded && (
          <Paper
            sx={{
              p: 2,
              width: 300,
              borderRadius: 2,
              boxShadow: 4,
              display: 'flex',
              flexDirection: 'column',
              gap: 2,
            }}
            elevation={8}
          >
            <Typography variant="subtitle1" sx={{ mb: 1 }}>
              {t('createDirectoryFab.createNewDirectory')}
            </Typography>

            {/* Directory Name */}
            <Controller
              name="name"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  size="small"
                  label={t('createDirectoryFab.name')}
                  error={!!errors.name}
                  helperText={
                    errors.name ? t(errors.name.message as string) : ''
                  }
                  fullWidth
                />
              )}
            />

            {/* Icon Color Selection */}
            <Box>
              <Typography variant="subtitle2" sx={{ mb: 1 }}>
                {t('createDirectoryFab.iconColor')}
              </Typography>
              <Controller
                name="iconColor"
                control={control}
                render={({ field }) => (
                  <ToggleButtonGroup
                    {...field}
                    exclusive
                    onChange={(_, value) => {
                      if (value !== null) {
                        field.onChange(value);
                      }
                    }}
                    aria-label="icon color"
                    sx={{ gap: 1 }} // Adds spacing between the buttons
                  >
                    {colorOptions.map((color) => (
                      <ToggleButton
                        key={color.value}
                        value={color.value}
                        aria-label={color.label}
                        sx={{
                          width: 40,
                          height: 40,
                          minWidth: 40, // Ensures consistent sizing
                          border: 'none', // Removes border when unselected
                          borderRadius: '50%', // Makes the button circular
                          backgroundColor: color.value,
                          transition: 'transform 0.2s, border 0.2s', // Smooth transitions
                          '&.Mui-selected, &.Mui-selected:hover, &:hover': {
                            transform: 'scale(1.1)', // Slightly enlarges the selected button
                            border: '2px solid #FFFFFF', // Adds a distinct white border when selected
                            backgroundColor: color.value,
                          },
                        }}
                      />
                    ))}
                  </ToggleButtonGroup>
                )}
              />
              {errors.iconColor && (
                <Typography variant="caption" color="error">
                  {t(errors.iconColor.message as string)}
                </Typography>
              )}
            </Box>

            {/* Visibility Switch */}
            <Controller
              name="visible"
              control={control}
              render={({ field }) => (
                <FormControlLabel
                  control={
                    <Switch
                      {...field}
                      checked={field.value}
                      onChange={(e) => field.onChange(e.target.checked)}
                    />
                  }
                  label={
                    field.value
                      ? t('createDirectoryFab.visible')
                      : t('createDirectoryFab.hidden')
                  }
                />
              )}
            />

            {/* Submit Button */}
            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 1 }}>
              {isLoading ? (
                <CircularProgress size={30} />
              ) : (
                <Button
                  variant="contained"
                  size="small"
                  onClick={handleSubmit(onSubmit)}
                  sx={{ textTransform: 'none' }}
                >
                  {t('createDirectoryFab.submit')}
                </Button>
              )}
            </Box>
          </Paper>
        )}
      </Box>
    </ClickAwayListener>
  );
};

export default CreateDirectoryFab;
