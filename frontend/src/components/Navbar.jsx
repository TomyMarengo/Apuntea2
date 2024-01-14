import { NavLink } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../store/slices/authSlice';
import { Button, DarkMode, ProfileButton } from '.';

const Navbar = () => {
  const user = useSelector(selectCurrentUser);

  return (
    <nav className="flex justify-between bg-dark-bg items-center px-4">
      <NavLink to="/" className="text-dark-pri font-black text-3xl">
        Apuntea
      </NavLink>
      <div className="flex items-center gap-4">
        <DarkMode />
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
