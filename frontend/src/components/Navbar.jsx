import { NavLink } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentToken} from '../store/slices/authSlice';

import { Button, DarkMode, ProfileButton } from '.';

const Navbar = () => {
  const token = useSelector(selectCurrentToken);

  return (
    <nav className="min-h-[60px] flex justify-between bg-dark-bg items-center px-4">
      <NavLink to="/" className="text-dark-pri font-black text-3xl">
        Apuntea
      </NavLink>
      <div className="flex items-center gap-4">
        <DarkMode />
        {token ? (
          <>
            <ProfileButton/>
          </>
        ) : (
          <>
            <Button to="/login">Log In</Button>
            <Button to="/signup" outlined>
              Sign up
            </Button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
