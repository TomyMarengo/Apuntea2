import { selectCurrentUser } from '../../store/slices/authSlice';
import { useSelector } from 'react-redux';

const RequireAuth = ({ children }) => {
  const user = useSelector(selectCurrentUser);
  if (user) return <>{children}</>;
};

export default RequireAuth;
