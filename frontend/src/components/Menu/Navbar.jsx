import { NavLink } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { selectCurrentUser } from '../../store/slices/authSlice';
import { Button, DarkMode, ProfileButton, NavSearchButton } from '../index';

const Navbar = () => {
  const user = useSelector(selectCurrentUser);

  return (
    <nav className="flex justify-between bg-dark-bg items-center px-4">
      <NavLink to="/" className="text-dark-pri font-black text-3xl hover:text-pri">
        Apuntea
      </NavLink>
      <div className="flex items-center gap-4">
        <div className="flex gap-2">
          <NavSearchButton />
          <DarkMode />
        </div>
        {user ? (
          <>
            <ProfileButton />
          </>
        ) : (
          <>
            <Button to="/login">Log In</Button>
            <Button to="/register" outlined>
              Sign up
            </Button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
