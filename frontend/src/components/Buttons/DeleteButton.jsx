import { TrashIcon } from '../Utils/Icons';

const DeleteButton = ({ onClick, className }) => {
  return (
    <button className="icon-button" onClick={onClick}>
      <TrashIcon className={className} />
    </button>
  );
};

export default DeleteButton;
