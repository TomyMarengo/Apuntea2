import { useGetNoteQuery, useGetNoteFileQuery } from '../store/slices/noteApiSlice';
import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import FavoriteButton from '../components/FavoriteButton';
import DownloadButton from '../components/DownloadButton';
import { DeleteButton, ReviewCard } from '../components/index';

const Note = () => {
  const { t } = useTranslation();
  const { noteId } = useParams();
  const { data, isLoading, error } = useGetNoteQuery(noteId);
  const { note } = data || {};

  console.log(note);
  return (
    <section className="note-info">
      {/* {isLoading && <p>Loading...</p>}
        {error && <p>{error}</p>} */}
      <div className="note-header flex justify-between items-center">
        <div className="flex flex-row items-center">
          <img className="user-profile-picture" src={note?.user?.profilePicture} alt="user profile" />
          <div className="flex flex-col">
            <h4 className="font-bold text-xl">
              aa
              {/* {note.owner.name} */}
            </h4>
            <span>
              {t('data.views')}: {note.interactions}
            </span>
          </div>
        </div>
        <div className="flex">
          <FavoriteButton />
          <DownloadButton link={`http://localhost:8080/paw-2023b-12/notes/${noteId}/file`} />
          <DeleteButton />
        </div>
      </div>
      <div className="reviews">
        <h2 className="text-3xl text-dark-pri mb-1">{t('data.reviews')}</h2>
        <span>
          {t('data.score')}: {note.avgScore.toFixed(1)} ⭐
        </span>
        <div className="p-2 mt-2 overflow-y-auto">
          {/* {note.reviews.map((review) => (
            <ReviewCard key={review.id} {...review} />
          ))} */}
          <ReviewCard />
        </div>
      </div>
      <div className="note-frame">
        <iframe src={`http://localhost:8080/paw-2023b-12/notes/${noteId}/file`}></iframe>
      </div>
    </section>
  );
};

export default Note;
