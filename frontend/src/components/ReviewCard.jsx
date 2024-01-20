import { useState, useEffect } from 'react';
import { NavLink } from 'react-router-dom';
import { Box, DeleteButton } from './index';
import { ChevronDownIcon, ChevronUpIcon } from './Icons';

const ReviewCard = ({ user, userLink, score, content }) => {
  const [isTextOverflowing, setIsTextOverflowing] = useState(false);
  const [isContentVisible, setIsContentVisible] = useState(false);

  useEffect(() => {
    const spanElement = document.getElementById(`rcContent-${userLink}`);
    if (spanElement) {
      setIsTextOverflowing(
        spanElement.scrollWidth > spanElement.clientWidth || spanElement.scrollHeight > spanElement.clientHeight
      );
    }
  }, [content, userLink]);

  const toggleContentVisibility = () => {
    setIsContentVisible(!isContentVisible);
  };

  return (
    <Box className="flex flex-col gap-1 my-3">
      <div className="flex items-center justify-between">
        <div className="link text-xl">
          <NavLink to={userLink}>
            Usuario
            {/* {user} */}
          </NavLink>
        </div>
        <DeleteButton />
      </div>
      <div>{score}</div>
      <div id={`rcContent-${userLink}`} className={`${isContentVisible ? '' : 'truncate'}`}>
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
