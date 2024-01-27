import { CrossIcon } from '../Utils/Icons';

const InputSkeleton = () => {
  return (
    <div className="flex flex-col gap-2 w-full">
      <div className="flex w-full relative">
        <label className="relative w-full">
          <input disabled className="skeleton-container" />
          <button tabIndex="-1" className="erasable-button" type="button" disabled>
            <CrossIcon className="w-[10px] h-[10px] fill-pri/50 dark:fill-dark-pri" />
          </button>
        </label>
      </div>
    </div>
  );
};

export default InputSkeleton;
