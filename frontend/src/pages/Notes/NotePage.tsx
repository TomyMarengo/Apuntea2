import React, { useState, useEffect, useRef, useCallback } from 'react';
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
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import DownloadIcon from '@mui/icons-material/Download';
import KeyboardDoubleArrowDownIcon from '@mui/icons-material/KeyboardDoubleArrowDown';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import {
  useGetNoteQuery,
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useGetIsFavoriteNoteQuery,
  useGetNoteFileQuery,
} from '../../store/slices/notesApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { Review } from '../../types';
import { toast } from 'react-toastify';
import EditNoteDialog from './dialogs/EditNoteDialog';
import DeleteNoteDialog from './dialogs/DeleteNoteDialog';
import ReviewCard from '../../components/ReviewCard';
import {
  useGetReviewsQuery,
  useGetMyReviewQuery,
  useCreateReviewMutation,
  useUpdateReviewMutation,
} from '../../store/slices/reviewsApiSlice';
import { RootState } from '../../store/store';
import { useNavigate } from 'react-router-dom';

const NotePage: React.FC = () => {
  const { t } = useTranslation();
  const { noteId } = useParams<{ noteId: string }>();
  const user = useSelector(selectCurrentUser);
  const token = useSelector((state: RootState) => state.auth.token);
  const navigate = useNavigate();

  /** 1) Basic note data */
  const {
    data: note,
    isLoading: noteLoading,
    isError: noteError,
  } = useGetNoteQuery({ noteId: noteId || '' }, { skip: !noteId });

  /** 2) Owner user data (via note?.ownerUrl) */
  const { data: ownerData } = useGetUserQuery(
    note?.ownerUrl ? { url: note.ownerUrl } : {},
    { skip: !note?.ownerUrl },
  );

  /** 2.1) Current user's existing review (if any) */
  const { data: myReviewData } = useGetMyReviewQuery(
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
  const isAdmin = token?.payload?.authorities.includes('ROLE_ADMIN') as boolean;

  const handleToggleFavorite = async () => {
    if (!user) {
      toast.error(t('notePage.mustLoginFavorite'));
      return;
    }
    try {
      if (isFavorite) {
        const result = await removeFavoriteNote({
          noteId,
          userId: user.id,
        }).unwrap();
        if (result) {
          toast.success(t('notePage.unfavorited'));
        }
      } else {
        const result = await addFavoriteNote({ noteId }).unwrap();
        if (result) {
          toast.success(t('notePage.favorited'));
        }
      }
      refetchFav();
    } catch (err) {
      toast.error(t('notePage.favoriteError'));
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
      toast.error(t('notePage.downloadError'));
      return;
    }
    const ext = note.fileType ? '.' + note.fileType : '';
    const filename = note.name + ext;
    const blobUrl = URL.createObjectURL(noteFileBlob);
    const a = document.createElement('a');
    a.href = blobUrl;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(blobUrl);
    toast.success(t('notePage.downloadSuccess'));
  };

  /** 5) Edit/Delete note with modals */
  const [openEdit, setOpenEdit] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);

  const handleOpenEdit = () => setOpenEdit(true);
  const handleCloseEdit = () => setOpenEdit(false);

  const handleOpenDelete = () => setOpenDelete(true);
  const handleCloseDelete = () => {
    setOpenDelete(false);
    navigate('/', { replace: true });
  };

  /** 6) Reviews - infinite scroll */
  const [reviewsPage, setReviewsPage] = useState(1);
  const pageSize = 5;

  const {
    data: reviewData,
    isLoading: loadingReviews,
    refetch: refetchReviews,
  } = useGetReviewsQuery(
    {
      noteId,
      page: reviewsPage,
      pageSize,
    },
    { skip: !noteId },
  );

  // Consolidate pages in local state
  const [allReviews, setAllReviews] = useState<Review[]>([]);
  const totalReviewPages = reviewData?.totalPages || 1;

  useEffect(() => {
    if (reviewData?.reviews) {
      if (reviewsPage === 1) {
        // First page -> replace
        setAllReviews(reviewData.reviews);
      } else {
        // Next page -> append
        setAllReviews((prev) => [...prev, ...reviewData.reviews]);
      }
    }
  }, [reviewData, reviewsPage]);

  const canLoadMore = reviewsPage < totalReviewPages;

  const handleLoadMore = useCallback(() => {
    if (!loadingReviews && canLoadMore) {
      setReviewsPage((prev) => prev + 1);
    }
  }, [loadingReviews, canLoadMore]);

  // Intersection Observer sentinel
  const sentinelRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const sentinel = sentinelRef.current;
    if (!sentinel) return;

    const observer = new IntersectionObserver(
      (entries) => {
        const firstEntry = entries[0];
        if (firstEntry.isIntersecting) {
          handleLoadMore();
        }
      },
      {
        root: null,
        rootMargin: '0px',
        threshold: 0.1,
      },
    );

    observer.observe(sentinel);

    return () => {
      if (sentinel) {
        observer.unobserve(sentinel);
      }
    };
  }, [handleLoadMore]);

  /** 7) Create or Update review (if not owner) */
  const [createReviewMutation] = useCreateReviewMutation();
  const [updateReviewMutation] = useUpdateReviewMutation();

  const [score, setScore] = useState<number>(5);
  const [content, setContent] = useState('');

  useEffect(() => {
    if (myReviewData) {
      setScore(myReviewData.score);
      setContent(myReviewData.content);
    }
  }, [myReviewData]);

  const handleSaveReview = async () => {
    if (!user) {
      toast.error(t('notePage.mustLoginReview'));
      return;
    }
    if (isOwner) return; // Owners can't review their own note
    try {
      let result;
      if (myReviewData) {
        // Update existing
        result = await updateReviewMutation({
          url: myReviewData.selfUrl,
          noteId: note?.id || '',
          userId: user.id,
          score,
          content,
        }).unwrap();
      } else {
        // Create new
        result = await createReviewMutation({
          noteId: note?.id || '',
          userId: user.id,
          score,
          content,
        }).unwrap();
      }

      if (result) {
        toast.success(
          myReviewData
            ? t('notePage.reviewUpdated')
            : t('notePage.reviewCreated'),
        );
        setReviewsPage(1);
        refetchReviews();
      }
    } catch (err) {
      toast.error(t('notePage.reviewError'));
    }
  };

  if (noteLoading) {
    return (
      <Box sx={{ p: 3 }}>
        <CircularProgress />
      </Box>
    );
  }
  if (noteError || !note) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography color="error">{t('notePage.noteNotFound')}</Typography>
      </Box>
    );
  }

  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: {
          xs: '1fr',
          md: '2fr 1fr',
        },
        height: 'calc(100vh - 64px)',
      }}
    >
      {/* LEFT side */}
      <Box
        sx={{ display: 'flex', flexDirection: 'column', overflow: 'hidden' }}
      >
        <Box sx={{ p: 2, borderBottom: 1, borderColor: 'divider' }}>
          <Typography variant="h5">{note.name}</Typography>
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
                {ownerData?.username || t('notePage.ownerUnknown')}
              </Typography>
              <Typography variant="caption" color="text.secondary">
                {t('notePage.views', { count: note.interactions || 0 })}
              </Typography>
            </Box>
          </Box>

          {/* Actions */}
          <Box sx={{ display: 'flex', gap: 1 }}>
            {/* Favorite if not owner */}
            {user && (
              <Tooltip
                title={
                  isFavorite
                    ? t('notePage.unfavorite')!
                    : t('notePage.favorite')!
                }
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

            {/* Edit/Delete if owner */}
            {isOwner && (
              <Tooltip title={t('notePage.edit')!}>
                <IconButton onClick={handleOpenEdit}>
                  <EditIcon />
                </IconButton>
              </Tooltip>
            )}
            {(isOwner || isAdmin) && (
              <Tooltip title={t('notePage.delete')!}>
                <IconButton onClick={handleOpenDelete}>
                  <DeleteIcon />
                </IconButton>
              </Tooltip>
            )}

            {/* Download always */}
            <Tooltip title={t('notePage.download')!}>
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
              <Typography>{t('notePage.fileNotPreviewable')}</Typography>
            )
          ) : noteFileLoading ? (
            <CircularProgress />
          ) : noteFileError ? (
            <Typography color="error">{t('notePage.fileNotFound')}</Typography>
          ) : (
            <Typography>{t('notePage.fileNotFound')}</Typography>
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
          <Typography variant="h6">{t('notePage.reviewsTitle')}</Typography>
        </Box>

        {/* Reviews scroll container with custom scrollbar */}
        <Box
          sx={{
            flex: 1,
            p: 2,
            overflowY: 'auto',
            /* Custom minimal scrollbar styling */
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
            <ReviewCard key={`${rev.noteId}_${rev.userId}`} review={rev} />
          ))}

          {/* If still loading, show progress; otherwise show arrow or "no more" */}
          {loadingReviews && (
            <Box sx={{ textAlign: 'center', p: 1 }}>
              <CircularProgress size={24} />
            </Box>
          )}

          {/* If we can load more, show arrow & hint; if not, show "no more" */}
          {!loadingReviews && (
            <Box ref={sentinelRef} sx={{ textAlign: 'center', mt: 2, mb: 2 }}>
              {canLoadMore ? (
                <Box
                  sx={{ display: 'inline-flex', alignItems: 'center', gap: 1 }}
                >
                  <KeyboardDoubleArrowDownIcon />
                  <Typography variant="body2">
                    {t('notePage.moreReviewsHint')}
                  </Typography>
                </Box>
              ) : (
                <Typography variant="body2" color="text.secondary">
                  {t('notePage.noMoreReviews')}
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
            <Typography variant="subtitle2">
              {myReviewData
                ? t('notePage.updateReview')
                : t('notePage.addReview')}
            </Typography>

            <Rating
              name="score"
              value={score}
              onChange={(_, newValue) => {
                if (newValue) setScore(newValue);
              }}
            />
            <TextField
              multiline
              rows={2}
              placeholder={t('notePage.reviewPlaceholder')!}
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />

            <Button variant="contained" onClick={handleSaveReview}>
              {myReviewData
                ? t('notePage.updateReview')
                : t('notePage.sendReview')}
            </Button>
          </Box>
        )}
      </Box>

      {/* EDIT NOTE DIALOG */}
      {isOwner && (
        <EditNoteDialog open={openEdit} onClose={handleCloseEdit} note={note} />
      )}

      {/* DELETE NOTE DIALOG */}
      {(isOwner || isAdmin) && (
        <DeleteNoteDialog
          open={openDelete}
          onClose={handleCloseDelete}
          note={note}
          shouldShowReason={isAdmin && !isOwner}
        />
      )}
    </Box>
  );
};

export default NotePage;
