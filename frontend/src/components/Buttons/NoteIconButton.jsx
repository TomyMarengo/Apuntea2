import { NavLink } from 'react-router-dom';
import { FavoriteButton } from '../index';
import { jpg, jpeg, mp3, mp4, pdf, png } from '../../assets/images/index';
const NoteIconButton = ({ name, fileType, noteId, fav }) => {
  const ft =
    fileType === 'jpg'
      ? jpg
      : fileType === 'jpeg'
        ? jpeg
        : fileType === 'mp3'
          ? mp3
          : fileType === 'mp4'
            ? mp4
            : fileType === 'pdf'
              ? pdf
              : png;
  return (
    <div className="relative hover:scale-110 transition-transform">
      {fav && (
        <div className="heart-container">
          <FavoriteButton className="icon-xxs" />
        </div>
      )}
      <NavLink to={'/notes/' + noteId} className="flex flex-col items-center gap-2">
        <img src={ft} alt={fileType} className="icon-lg" />
        <span className="file-name">{name}</span>
      </NavLink>
    </div>
  );
};

export default NoteIconButton;
