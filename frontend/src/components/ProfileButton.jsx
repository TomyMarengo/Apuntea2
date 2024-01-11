import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logOut, selectCurrentUser } from '../store/slices/authSlice';

import { UserIcon, ChevronDown } from './Icons';
import { Button, Dropdown } from '.';

const ProfileButton = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);

  const handleLogOut = () => {
    dispatch(logOut());
    navigate('/');
  };

  return (
    <Dropdown
      className="right"
      open={open}
      trigger={
        <Button className="flex justify-between items-center icon-button">
          <UserIcon className="icon" />
          <ChevronDown className="icon" />
        </Button>
      }
      menu={[
        <div className="flex flex-col px-4" key="User">
          <span className="text-xs text-dark/10">Se ha iniciado sesión como</span>
          <span className="font-bold">{user}</span>
        </div>,
        <hr key="Divider1" className="dropdown-divider" />,
        <Button key="Profile" to="/profile">
          Profile
        </Button>,
        <Button key="Settings" to="/settings">
          Settings
        </Button>,
        <hr key="Divider2" className="dropdown-divider" />,
        <Button key="SignOut" onClick={handleLogOut}>
          Sign out
        </Button>,
      ]}
    />
  );
};

export default ProfileButton;
