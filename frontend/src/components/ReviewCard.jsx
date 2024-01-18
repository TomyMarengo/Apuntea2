import { NavLink } from 'react-router-dom';
import { Box, DeleteButton } from './index';
const ReviewCard = ({ user, userLink, score, content }) => {
  return (
    <Box className="flex flex-col gap-1">
      <div className="flex items-center justify-between">
        <div className="link text-xl">
          <NavLink to={userLink}>
            {/* {user} */}
            Usuario
          </NavLink>
        </div>
        <DeleteButton />
      </div>
      <div>score</div>
      <div>content</div>
      {/* {score.map((_, index) => (
          <span key={index}>⭐</span>
        ))} */}
      {/* {content} */}
    </Box>
  );
};

export default ReviewCard;
