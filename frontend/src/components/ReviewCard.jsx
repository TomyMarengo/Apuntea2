import { useState, useEffect, useRef } from 'react';
import { NavLink } from 'react-router-dom';
import { Box, DeleteButton } from './index';
import { ChevronDownIcon, ChevronUpIcon } from './Icons';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import Spinner from './Spinner';

const ReviewCard = ({ user, score, content }) => {
  const [isTextOverflowing, setIsTextOverflowing] = useState(false);
  const [isContentVisible, setIsContentVisible] = useState(false);
  const contentRef = useRef(null);

  const { data: owner, isLoading: isLoadingOwner, error: errorOwner } = useGetUserQuery({ url: user });

  console.log('owner', owner);
  useEffect(() => {
    if (contentRef.current) {
      setIsTextOverflowing(
        contentRef.current.scrollWidth > contentRef.current.clientWidth ||
          contentRef.current.scrollHeight > contentRef.current.clientHeight
      );
    }
  }, []);

  const toggleContentVisibility = () => {
    setIsContentVisible(!isContentVisible);
  };

  return (
    <Box className="flex flex-col gap-1 my-3">
      <div className="flex items-center justify-between">
        <div className="link text-xl">
          {isLoadingOwner ? <Spinner /> : <NavLink to={owner.self + '/noteboard'}>{owner.username}</NavLink>}
        </div>
        <DeleteButton />
      </div>
      <div>
        {Array.from({ length: score }, (_, i) => (
          <span key={i}>⭐</span>
        ))}
      </div>
      <div ref={contentRef} className={`${isContentVisible ? 'break-all' : 'truncate'}`}>
        {content}
      </div>
      {isTextOverflowing && (
        <div className="flex justify-center">
          <button className="icon-button" onClick={toggleContentVisibility}>
            {isContentVisible ? <ChevronUpIcon className="icon-s" /> : <ChevronDownIcon className="icon-s" />}
          </button>
        </div>
      )}
    </Box>
  );
};

export default ReviewCard;
