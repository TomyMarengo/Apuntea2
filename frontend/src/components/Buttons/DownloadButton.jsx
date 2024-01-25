import { Link } from 'react-router-dom';

import { DownloadIcon } from '../Utils/Icons';

const DownloadButton = ({ link }) => {
  return (
    <Link className="icon-button" to={link} download="hola" target="_self">
      <DownloadIcon className="icon-s" />
    </Link>
  );
};
export default DownloadButton;
