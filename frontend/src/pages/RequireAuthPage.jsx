import { useLocation, Navigate, Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../store/slices/authSlice';

const RequireAuthPage = () => {
  const user = useSelector(selectCurrentUser);
  const location = useLocation();

  return user ? <Outlet /> : <Navigate to="/login" state={{ from: location }} replace />;
};
export default RequireAuthPage;
