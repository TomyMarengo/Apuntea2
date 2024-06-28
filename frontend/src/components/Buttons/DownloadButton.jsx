import { Link } from 'react-router-dom';

import { DownloadIcon } from '../Utils/Icons';

const DownloadButton = ({ link, fileName, fileType }) => {
  const downloadFile = (filePath, fileName) => {
    fetch(filePath)
      .then((response) => response.blob())
      .then((blob) => {
        const url = window.URL.createObjectURL(new Blob([blob]));
        const l = document.createElement('a');
        l.href = url;
        l.download = fileName;
        document.body.appendChild(l);
        l.click();
        l.parentNode.removeChild(l);
      });
  };

  return (
    <Link className="icon-button" target="_self" onClick={() => downloadFile(link, fileName + '.' + fileType)}>
      <DownloadIcon className="icon-s" />
    </Link>
  );
};
export default DownloadButton;
