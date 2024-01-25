import { PencilIcon } from './Utils/Icons';
import clsx from 'clsx';
import { useTranslation } from 'react-i18next';
import { useState } from 'react';

const EditableImage = ({ profilePictureUrl, onChange, ...props }) => {
  const { t } = useTranslation();
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    setSelectedFile(file);

    // Pass the selected file to the parent component if a callback is provided
    if (onChange) {
      onChange({ target: { name: 'profilePicture', value: file } });
    }
  };

  return (
    <div className={clsx('flex flex-col items-center justify-center', props.className)}>
      <label
        htmlFor="fileInput"
        className="relative w-[200px] h-[200px] rounded-full cursor-pointer transition duration-300 ease-in-out group"
      >
        {selectedFile ? (
          <>
            <img
              src={URL.createObjectURL(selectedFile)}
              alt={t('data.profilePicture')}
              className="w-full h-full rounded-full object-cover transition duration-300 ease-in-out group-hover:opacity-50"
            />
            <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100">
              <PencilIcon className="fill-text w-10 h-10" />
            </div>
          </>
        ) : (
          <>
            <img
              className="w-full h-full rounded-full object-cover transition duration-300 ease-in-out group-hover:opacity-50"
              src={profilePictureUrl || '/profile-picture.jpeg'}
              alt={t('data.profilePicture')}
            />
            <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100">
              <PencilIcon className="fill-text w-10 h-10" />
            </div>
          </>
        )}
      </label>
      <input type="file" id="fileInput" accept="image/*" className="hidden" onChange={handleFileChange} />
    </div>
  );
};

export default EditableImage;
