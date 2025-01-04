// src/routes/AppRouter.tsx
import { Routes, Route } from 'react-router-dom';

import AdminRoute from './AdminRoute';
import PrivateRoute from './PrivateRoute';
import AdminCareersPage from '../pages/Admin/Careers/AdminCareersPage';
import AdminUsersPage from '../pages/Admin/Users/AdminUsersPage';
import DirectoryPage from '../pages/Directories/DirectoryPage';
import FavoritesPage from '../pages/Favorites/FavoritesPage';
import ForgotPasswordPage from '../pages/ForgotPassword/ForgotPasswordPage';
import HomePage from '../pages/Home/HomePage';
import LoginPage from '../pages/Login/LoginPage';
import NotePage from '../pages/Notes/NotePage';
import NotesPage from '../pages/Notes/NotesPage';
import ProfilePage from '../pages/Profile/ProfilePage';
import RegisterPage from '../pages/Register/RegisterPage';
import ReviewsPage from '../pages/Reviews/ReviewsPage';
import SearchPage from '../pages/Search/SearchPage';
import UserPage from '../pages/Users/UserPage';

export default function AppRouter() {
  return (
    <Routes>
      {/* Public routes */}
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/forgotpassword" element={<ForgotPasswordPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/search" element={<SearchPage />} />
      <Route path="/notes/:noteId" element={<NotePage />} />
      <Route path="/directories/:directoryId" element={<DirectoryPage />} />
      <Route path="/users/:userId" element={<UserPage />} />

      {/* Protected routes */}
      <Route
        path="/profile"
        element={
          <PrivateRoute>
            <ProfilePage />
          </PrivateRoute>
        }
      />
      <Route
        path="/favorites"
        element={
          <PrivateRoute>
            <FavoritesPage />
          </PrivateRoute>
        }
      />
      <Route
        path="/reviews"
        element={
          <PrivateRoute>
            <ReviewsPage />
          </PrivateRoute>
        }
      />
      <Route
        path="/notes"
        element={
          <PrivateRoute>
            <NotesPage />
          </PrivateRoute>
        }
      />
      {/* Admin routes */}
      <Route
        path="/admin/users"
        element={
          <AdminRoute>
            <AdminUsersPage />
          </AdminRoute>
        }
      />
      <Route
        path="/admin/careers"
        element={
          <AdminRoute>
            <AdminCareersPage />
          </AdminRoute>
        }
      />
    </Routes>
  );
}
