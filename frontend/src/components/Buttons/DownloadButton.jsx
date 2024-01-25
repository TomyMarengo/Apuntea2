import { DownloadIcon } from '../Utils/Icons';
import { Link } from 'react-router-dom';
const DownloadButton = ({ link }) => {
  return (
    <Link className="icon-button" to={link} download="hola" target="_self">
      <DownloadIcon className="icon-s" />
    </Link>
  );
};
export default DownloadButton;
