import { LinkIcon } from './Icons';

const CopyButton = ({ link }) => {
  return (
    <button
      className="icon-button"
      onClick={() => {
        navigator.clipboard.writeText(link);
      }}
    >
      <LinkIcon className="icon-s" />
    </button>
  );
};

export default CopyButton;
