import { PencilIcon } from '../Utils/Icons';

const EditButton = ({ className, onClick }) => {
  return (
    <button className="icon-button" onClick={onClick}>
      <PencilIcon className={className} />
    </button>
  );
};

export default EditButton;
