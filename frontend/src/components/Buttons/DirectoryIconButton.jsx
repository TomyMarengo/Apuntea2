import { DirectoryIcon } from '../Utils/Icons';
import { NavLink } from 'react-router-dom';
import FavoriteButton from './FavoriteButton';

const DirectoryIconButton = ({ name, directoryId, fav }) => {
  return (
    <div className="relative hover:scale-110 transition-transform">
      <div className="heart-container">{fav && <FavoriteButton className="icon-xs" />}</div>
      <NavLink to={'/directories/' + directoryId} className="flex flex-col items-center gap-2">
        <DirectoryIcon className="icon-lg" />
        <span className="file-name">{name}</span>
      </NavLink>
    </div>
  );
};

export default DirectoryIconButton;
