import { Link } from 'react-router-dom';

const BottomNavbar = ({ title, to }) => {
  return (
    <div className="bottom-navbar">
      <Link to={to} className="bottom-navbar-title">
        {title}
      </Link>
    </div>
  );
};

export default BottomNavbar;
