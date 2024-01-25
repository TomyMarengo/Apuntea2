import { NavLink, useLocation } from 'react-router-dom';
import { useMemo } from 'react';

import { BooksIcon, FilledHeartIcon, MyNotesIcon, OpenBookIcon, ReviewIcon, UserSlashIcon } from '../Utils/Icons';

const Sidebar = () => {
  const options = useMemo(
    () => [
      { icon: <MyNotesIcon className="fill-dark-pri icon-s" key="0" />, link: '/noteboard' },
      { icon: <ReviewIcon className="fill-dark-pri icon-s" key="1" />, link: '/reviews' },
      { icon: <OpenBookIcon className="fill-dark-pri icon-s" key="2" />, link: '/career' },
      { icon: <FilledHeartIcon className="fill-dark-pri icon-s" key="3" />, link: '/favorites' },
      { icon: <UserSlashIcon className="fill-dark-pri icon-s" key="4" />, link: '/manage-users' },
      { icon: <BooksIcon className="fill-dark-pri icon-s" key="5" />, link: '/manage-careers' },
    ],
    []
  );
  const location = useLocation();

  return (
    <aside className="bg-dark-bg min-w-[60px] min-h-[calc(100vh-60px)] flex flex-col items-center gap-3 pt-4 sidebar">
      {options.map(({ icon, link }, i) => (
        <NavLink
          to={link}
          key={i}
          className={`p-3 flex items-center justify-center rounded-full hover:bg-text/10 transition-all duration-200 ${
            location.pathname === link && 'bg-text/10 scale-110'
          }`}
        >
          {icon}
        </NavLink>
      ))}
    </aside>
  );
};

export default Sidebar;
