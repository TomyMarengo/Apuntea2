// src/pages/Notes/NotePage.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import DeleteIcon from '@mui/icons-material/Delete';
import DownloadIcon from '@mui/icons-material/Download';
import EditIcon from '@mui/icons-material/Edit';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import KeyboardDoubleArrowDownIcon from '@mui/icons-material/KeyboardDoubleArrowDown';
import {
  Box,
  Avatar,
  Typography,
  IconButton,
  Tooltip,
  Button,
  Rating,
  TextField,
  CircularProgress,
} from '@mui/material';
import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Helmet } from 'react-helmet-async';
import { useForm } from 'react-hook-form';
import { Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { z } from 'zod';

import DeleteNoteDialog from './dialogs/DeleteNoteDialog';
import EditNoteDialog from './dialogs/EditNoteDialog';
import DirectoryBreadcrumbs from '../../components/DirectoryBreadcrumbs';
import ReviewCard from '../../components/ReviewCard';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetDirectoryQuery } from '../../store/slices/directoriesApiSlice';
import {
  useGetNoteQuery,
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useGetIsFavoriteNoteQuery,
  useGetNoteFileQuery,
  useAddInteractionNoteMutation,
} from '../../store/slices/notesApiSlice';
import {
  useGetReviewsQuery,
  useGetMyReviewQuery,
  useCreateReviewMutation,
  useUpdateReviewMutation,
} from '../../store/slices/reviewsApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { RootState } from '../../store/store';
import { Review } from '../../types';
import { Token } from '../../types';

const reviewSchema = z.object({
  score: z
    .number()
    .int()
    .min(1, { message: 'minScore' })
    .max(5, { message: 'maxScore' }),
  content: z.string().max(255, { message: 'maxContentLength' }),
});

type ReviewFormData = z.infer<typeof reviewSchema>;

const NotePage: React.FC = () => {
  const { t } = useTranslation('notePage');
  const { noteId } = useParams<{ noteId: string }>();
  const user = useSelector(selectCurrentUser);
  const token = useSelector(
    (state: RootState & { auth: { token: Token } }) => state.auth.token,
  );
  const [addInteractionNote] = useAddInteractionNoteMutation();
  const isFirstRender = useRef(true);

  /** 1) Basic note data */
  const {
    data: note,
    isLoading: noteLoading,
    isError: noteError,
  } = useGetNoteQuery({ noteId: noteId || '' }, { skip: !noteId });

  useEffect(() => {
    if (noteId && user && isFirstRender.current) {
      addInteractionNote({
        noteId: noteId,
        userId: user.id,
      });
      isFirstRender.current = false;
    }
  }, [noteId, user, addInteractionNote]);

  /** 1.1) Directory data */
  const { data: directoryData } = useGetDirectoryQuery(
    { url: note?.parentUrl },
    { skip: !note?.parentUrl },
  );

  /** 2) Owner user data (via note?.ownerUrl) */
  const { data: ownerData } = useGetUserQuery(
    note?.ownerUrl ? { url: note.ownerUrl } : {},
    { skip: !note?.ownerUrl },
  );

  /** 2.1) Current user's existing review (if any) */
  const { data: myReviewData, refetch: refetchMyReview } = useGetMyReviewQuery(
    { url: note?.reviewsUrl, noteId: note?.id, userId: user?.id },
    { skip: !note || !user },
  );

  /** 3) Favorite logic */
  const { data: isFavData, refetch: refetchFav } = useGetIsFavoriteNoteQuery(
    user && note ? { noteId: note.id, userId: user.id } : {},
    { skip: !user || !note },
  );
  const [addFavoriteNote] = useAddFavoriteNoteMutation();
  const [removeFavoriteNote] = useRemoveFavoriteNoteMutation();
  const [isFavorite, setIsFavorite] = useState<boolean>(false);

  useEffect(() => {
    if (typeof isFavData === 'boolean') setIsFavorite(isFavData);
  }, [isFavData]);

  const isOwner = user && note && user.id === note.ownerUrl?.split('/').pop();
  const isAdmin = token?.payload?.authorities?.includes('ROLE_ADMIN') ?? false;

  const handleToggleFavorite = async () => {
    if (!user) {
      toast.error(t('mustLoginFavorite'));
      return;
    }
    try {
      if (isFavorite) {
        const result = await removeFavoriteNote({
          noteId,
          userId: user.id,
        }).unwrap();
        if (result) {
          toast.success(t('unfavorited'));
        }
      } else {
        const result = await addFavoriteNote({ noteId }).unwrap();
        if (result) {
          toast.success(t('favorited'));
        }
      }
      refetchFav();
    } catch (error) {
      console.error('Failed to toggle favorite:', error);
      toast.error(t('favoriteError'));
    }
  };

  /** 4) File fetch logic */
  const {
    data: noteFileBlob,
    isLoading: noteFileLoading,
    isError: noteFileError,
  } = useGetNoteFileQuery({ noteId: noteId! }, { skip: !noteId });

  const [fileUrl, setFileUrl] = useState<string>('');
  useEffect(() => {
    if (noteFileBlob) {
      const url = URL.createObjectURL(noteFileBlob);
      setFileUrl(url);
      return () => {
        URL.revokeObjectURL(url);
      };
    }
  }, [noteFileBlob]);

  const handleDownload = () => {
    if (!note || !noteFileBlob) {
      toast.error(t('downloadError'));
      return;
    }
    const ext = note.fileType ? `.${note.fileType}` : '';
    const filename = note.name + ext;
    const blobUrl = URL.createObjectURL(noteFileBlob);
    const a = document.createElement('a');
    a.href = blobUrl;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(blobUrl);
    toast.success(t('downloadSuccess'));
  };

  /** 5) Edit/Delete note with modals */
  const [openEdit, setOpenEdit] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);

  const handleOpenEdit = () => setOpenEdit(true);
  const handleCloseEdit = () => setOpenEdit(false);

  const handleOpenDelete = () => setOpenDelete(true);
  const handleCloseDelete = () => {
    setOpenDelete(false);
  };

  /** 6) Reviews - infinite scroll */
  const [reviewsPage, setReviewsPage] = useState(1);
  const pageSize = 15;

  // Fetch paginated reviews, EXCLUDING the user's review
  const { data: reviewData, isFetching } = useGetReviewsQuery(
    {
      noteId,
      page: reviewsPage,
      pageSize,
    },
    { skip: !noteId },
  );

  const [allReviews, setAllReviews] = useState<Review[]>([]);
  const totalReviewPages = reviewData?.totalPages || 1;

  // To avoid triggering the same page multiple times
  const [lastFetchedPage, setLastFetchedPage] = useState<number>(0);

  // Each time the current page data arrives, merge it with allReviews
  useEffect(() => {
    if (reviewData?.reviews) {
      // Filter out the user's review (if it exists in the list)
      const fetchedReviews = reviewData.reviews.filter(
        (r) => r.userId !== user?.id,
      );

      if (reviewsPage === 1) {
        setAllReviews(fetchedReviews);
      } else {
        setAllReviews((prev) => {
          const combined = [...prev, ...fetchedReviews];
          const uniqueById = Array.from(
            new Map(
              combined.map((item) => [item.noteId + '_' + item.userId, item]),
            ).values(),
          );
          return uniqueById;
        });
      }
    }
  }, [reviewData, reviewsPage, user?.id]);

  const canLoadMore = reviewsPage < totalReviewPages;

  // handleLoadMore -> increments page if not fetching
  const handleLoadMore = useCallback(() => {
    if (!isFetching && canLoadMore && reviewsPage > lastFetchedPage) {
      setLastFetchedPage(reviewsPage);
      setReviewsPage((prev) => prev + 1);
    }
  }, [isFetching, canLoadMore, reviewsPage, lastFetchedPage]);

  // IntersectionObserver -> triggers handleLoadMore
  const sentinelRef = useRef<HTMLDivElement | null>(null);
  useEffect(() => {
    const sentinel = sentinelRef.current;
    if (!sentinel) return;

    const observer = new IntersectionObserver(
      (entries) => {
        const firstEntry = entries[0];
        if (firstEntry.isIntersecting && !isFetching) {
          handleLoadMore();
        }
      },
      {
        root: null,
        rootMargin: '0px 0px 300px 0px',
        threshold: 0,
      },
    );

    observer.observe(sentinel);
    return () => {
      if (sentinel) {
        observer.unobserve(sentinel);
      }
    };
  }, [handleLoadMore, isFetching]);

  /** 7) Create or Update review (if not owner) */
  const [createReviewMutation] = useCreateReviewMutation();
  const [updateReviewMutation] = useUpdateReviewMutation();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<ReviewFormData>({
    resolver: zodResolver(reviewSchema),
    defaultValues: {
      content: myReviewData?.content || '',
      score: myReviewData?.score || 1,
    },
  });

  const handleDeleteSuccess = (review: Review) => {
    setAllReviews((prev) =>
      prev.filter(
        (r) => r.noteId !== review.noteId || r.userId !== review.userId,
      ),
    );
  };
  // Handle form submit
  const handleSaveReview = async (data: ReviewFormData) => {
    if (!user) {
      toast.error(t('mustLoginReview'));
      return;
    }
    if (isOwner) return; // Owners can't review their own note
    try {
      let result;
      if (myReviewData) {
        // Update review
        result = await updateReviewMutation({
          url: myReviewData.selfUrl,
          noteId: note?.id || '',
          userId: user.id,
          score: data.score,
          content: data.content,
        }).unwrap();
      } else {
        // Create new review
        result = await createReviewMutation({
          noteId: note?.id || '',
          userId: user.id,
          score: data.score,
          content: data.content,
        }).unwrap();
      }

      if (result.success) {
        toast.success(myReviewData ? t('reviewUpdated') : t('reviewCreated'));
        refetchMyReview();
      } else {
        toast.error(
          t('reviewError', {
            errorMessage:
              result.messages && result.messages.length > 0
                ? `: ${result.messages[0]}`
                : '',
          }),
        );
      }
    } catch (error) {
      console.error('Failed to save review:', error);
      toast.error(t('reviewError'));
    }
  };

  let pageTitle = t('titlePage', { noteName: note?.name || '' });
  if (noteLoading) {
    pageTitle = t('loading');
  } else if (noteError) {
    pageTitle = t('errorFetching');
  } else if (!note) {
    pageTitle = t('noteNotFound');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>

      {noteLoading ? (
        <Box sx={{ p: 3 }}>
          <CircularProgress />
        </Box>
      ) : noteError || !note ? (
        <Box sx={{ p: 3 }}>
          <Typography color="error">{t('noteNotFound')}</Typography>
        </Box>
      ) : (
        <Box
          sx={{
            display: 'grid',
            gridTemplateColumns: {
              xs: '1fr',
              md: '2fr 1fr',
            },
            height: {
              xs: 'auto',
              md: 'calc(100vh - 64px)',
            },
          }}
        >
          {/* LEFT side */}
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              overflow: 'hidden',
            }}
          >
            <Box sx={{ p: 2, borderBottom: 1, borderColor: 'divider' }}>
              {directoryData && note && (
                <DirectoryBreadcrumbs
                  currentDirectory={directoryData}
                  note={note}
                />
              )}
            </Box>
            {/* Top bar */}
            <Box
              sx={{
                display: 'flex',
                alignItems: 'center',
                p: 2,
                justifyContent: 'space-between',
              }}
            >
              {/* Owner info */}
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Avatar src={ownerData?.profilePictureUrl || ''} />
                <Box>
                  <Typography variant="subtitle1">
                    {ownerData?.username || t('ownerUnknown')}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {t('views', { count: note.interactions || 0 })}
                  </Typography>
                </Box>
              </Box>

              {/* Actions */}
              <Box sx={{ display: 'flex', gap: 1 }}>
                {/* Favorite if logged in */}
                {user && (
                  <Tooltip
                    title={isFavorite ? t('unfavorite')! : t('favorite')!}
                  >
                    <IconButton onClick={handleToggleFavorite}>
                      {isFavorite ? (
                        <FavoriteIcon sx={{ color: 'error.main' }} />
                      ) : (
                        <FavoriteBorderIcon />
                      )}
                    </IconButton>
                  </Tooltip>
                )}

                {/* Edit if owner */}
                {isOwner && (
                  <Tooltip title={t('edit')!}>
                    <IconButton onClick={handleOpenEdit}>
                      <EditIcon />
                    </IconButton>
                  </Tooltip>
                )}

                {/* Delete if owner or admin */}
                {(isOwner || isAdmin) && (
                  <Tooltip title={t('delete')!}>
                    <IconButton onClick={handleOpenDelete}>
                      <DeleteIcon />
                    </IconButton>
                  </Tooltip>
                )}

                {/* Download always */}
                <Tooltip title={t('download')!}>
                  <IconButton onClick={handleDownload}>
                    <DownloadIcon />
                  </IconButton>
                </Tooltip>
              </Box>
            </Box>

            {/* The note file area */}
            <Box
              sx={{
                flex: 1,
                overflow: 'hidden',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                p: 2,
                minHeight: {
                  xs: 600,
                },
              }}
            >
              {fileUrl ? (
                note.fileType?.toLowerCase().includes('pdf') ? (
                  <iframe
                    src={fileUrl}
                    style={{ width: '100%', height: '100%' }}
                    title="Note PDF"
                  />
                ) : note.fileType?.match(/(jpe?g|png|gif)$/i) ? (
                  <img
                    src={fileUrl}
                    alt={note.name}
                    style={{ maxWidth: '100%', maxHeight: '100%' }}
                  />
                ) : note.fileType?.match(/(mp4|webm|ogg)$/i) ? (
                  <video
                    src={fileUrl}
                    controls
                    style={{ maxWidth: '100%', maxHeight: '100%' }}
                  />
                ) : (
                  <Typography>{t('fileNotPreviewable')}</Typography>
                )
              ) : noteFileLoading ? (
                <CircularProgress />
              ) : noteFileError ? (
                <Typography color="error">{t('fileNotFound')}</Typography>
              ) : (
                <Typography>{t('fileNotFound')}</Typography>
              )}
            </Box>
          </Box>

          {/* RIGHT side: reviews */}
          <Box
            sx={{
              borderLeft: {
                xs: 'none',
                md: '1px solid',
              },
              borderLeftColor: {
                xs: 'transparent',
                md: 'divider',
              },
              display: 'flex',
              flexDirection: 'column',
              overflow: 'hidden',
            }}
          >
            <Box sx={{ p: 2, borderBottom: 1, borderColor: 'divider' }}>
              <Typography variant="h6">{t('reviewsTitle')}</Typography>
            </Box>

            <Box
              sx={{
                display: 'flex',
                flexDirection: {
                  xs: 'column-reverse',
                  md: 'column',
                },
                overflow: 'hidden',
              }}
            >
              {/* Reviews scroll container with custom scrollbar */}
              <Box
                sx={{
                  flex: 1,
                  p: 2,
                  overflowY: 'auto',
                  '&::-webkit-scrollbar': {
                    width: '6px',
                  },
                  '&::-webkit-scrollbar-track': {
                    backgroundColor: 'transparent',
                  },
                  '&::-webkit-scrollbar-thumb': {
                    backgroundColor: 'primary.main',
                    borderRadius: '3px',
                  },
                }}
              >
                {allReviews.map((rev: Review) => (
                  <ReviewCard
                    key={`${rev.noteId}_${rev.userId}`}
                    review={rev}
                    noteId={note.id}
                    onDeleteSuccess={handleDeleteSuccess}
                  />
                ))}

                {/* Loading spinner */}
                {isFetching && (
                  <Box sx={{ textAlign: 'center', p: 1 }}>
                    <CircularProgress size={24} />
                  </Box>
                )}

                {/* Sentinel for infinite scroll */}
                {!isFetching && (
                  <Box
                    ref={sentinelRef}
                    sx={{ textAlign: 'center', mt: 2, mb: 2 }}
                  >
                    {canLoadMore ? (
                      <Box
                        sx={{
                          display: 'inline-flex',
                          alignItems: 'center',
                          gap: 1,
                        }}
                      >
                        <KeyboardDoubleArrowDownIcon />
                        <Typography variant="body2">
                          {t('moreReviewsHint')}
                        </Typography>
                      </Box>
                    ) : (
                      <Typography variant="body2" color="text.secondary">
                        {t('noMoreReviews')}
                      </Typography>
                    )}
                  </Box>
                )}
              </Box>

              {/* Create/Update Review if not owner, and user is logged in */}
              {user && !isOwner && (
                <Box
                  sx={{
                    borderTop: 1,
                    borderColor: 'divider',
                    p: 2,
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 1,
                  }}
                >
                  <Controller
                    name="score"
                    control={control}
                    render={({ field }) => (
                      <Rating
                        {...field}
                        onChange={(_, newValue) => field.onChange(newValue)} // Handle score change
                        value={field.value} // Use value from form state
                      />
                    )}
                  />
                  <Controller
                    name="content"
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        multiline
                        rows={2}
                        placeholder={t('reviewPlaceholder')!}
                        error={!!errors.content}
                        helperText={
                          errors.content
                            ? t(errors.content.message as string)
                            : ''
                        }
                      />
                    )}
                  />

                  <Button
                    variant="contained"
                    onClick={handleSubmit(handleSaveReview)}
                  >
                    {myReviewData ? t('updateReview') : t('sendReview')}
                  </Button>
                </Box>
              )}
            </Box>
          </Box>

          {/* EDIT NOTE DIALOG */}
          {isOwner && (
            <EditNoteDialog
              open={openEdit}
              onClose={handleCloseEdit}
              note={note}
            />
          )}

          {/* DELETE NOTE DIALOG */}
          {(isOwner || isAdmin) && (
            <DeleteNoteDialog
              open={openDelete}
              onClose={handleCloseDelete}
              note={note}
              shouldShowReason={isAdmin && !isOwner}
              navigateBack
            />
          )}
        </Box>
      )}
    </>
  );
};

export default NotePage;
