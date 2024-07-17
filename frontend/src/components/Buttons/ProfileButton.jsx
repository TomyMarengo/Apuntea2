import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { logOut, selectCurrentUser } from '../../store/slices/authSlice';
import { UserIcon, ChevronDownIcon } from '../Utils/Icons';
import { Button, Dropdown } from '../index';
import { useTranslation } from 'react-i18next';

const ProfileButton = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);
  const { t } = useTranslation();

  const handleLogOut = () => {
    dispatch(logOut());
    navigate('/');
  };

  return (
    <Dropdown
      className="right"
      open={open}
      trigger={
        <button
          type="button"
          className="nav-profile-button flex justify-between items-center gap-2 py-2 px-4 bg-dark-pri rounded-3xl hover:bg-transparent transition-all duration-300"
        >
          <UserIcon className="icon-xs fill-bg" />
          <ChevronDownIcon className="icon-xs fill-bg" />
        </button>
      }
      menu={[
        <div className="flex flex-col px-4" key="User">
          <span className="text-xs text-dark/10">Se ha iniciado sesión como</span>
          <span className="font-bold">{user.username}</span>
        </div>,
        <hr key="Divider1" className="dropdown-divider" />,
        <Button key="Profile" to="/profile">
          {t('pages.profile.title')}
        </Button>,
        <Button key="Settings" to="/settings">
          {t('pages.settings.title')}
        </Button>,
        <hr key="Divider2" className="dropdown-divider" />,
        <Button key="SignOut" onClick={handleLogOut}>
          {t('actions.logout')}
        </Button>,
      ]}
    />
  );
};

export default ProfileButton;
