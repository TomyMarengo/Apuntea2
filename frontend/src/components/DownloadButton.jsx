import { DownloadIcon } from './Icons';

const DownloadButton = ({ fileName, link }) => {
  return (
    <a className="icon-button" href={link} download={fileName}>
      <DownloadIcon className="icon-s" />
    </a>
  );
};
export default DownloadButton;
