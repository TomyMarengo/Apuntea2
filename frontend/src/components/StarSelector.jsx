// StarSelector.js
import { useState, useEffect } from 'react';
import { StarIcon } from './Icons';

const StarSelector = ({ totalStars = 5, initialRating = 0, onStarClick, name }) => {
  const [rating, setRating] = useState(initialRating);

  useEffect(() => {
    setRating(initialRating);
  }, [initialRating]);

  const handleStarClick = (selectedRating) => {
    setRating(selectedRating);
    if (onStarClick) {
      onStarClick({ target: { name, value: selectedRating, type: 'starselector' } });
    }
  };

  return (
    <div className="mx-2 flex flex-row gap-2 items-cente">
      {[...Array(totalStars)].map((_, index) => (
        <StarIcon
          className={`icon-md cursor-pointer ${index < rating ? 'fill-yellow-400' : 'fill-gray-300'}`}
          key={index}
          onClick={() => handleStarClick(index + 1)}
          onMouseEnter={() => setRating(index + 1)}
          onMouseLeave={() => setRating(initialRating)}
        />
      ))}
    </div>
  );
};

export default StarSelector;
