// src/routes/AppRouter.tsx

import { Routes, Route } from 'react-router-dom';
import HomePage from '../pages/Home/HomePage';
import LoginPage from '../pages/Login/LoginPage';
import RegisterPage from '../pages/Register/RegisterPage';
import SearchPage from '../pages/Search/SearchPage';
import NotePage from '../pages/Notes/NotePage';
import DirectoryPage from '../pages/Directories/DirectoryPage';
import NotesPage from '../pages/Notes/NotesPage';
import ProfilePage from '../pages/Profile/ProfilePage';
import AdminUsersPage from '../pages/Admin/AdminUsersPage';
import AdminCareersPage from '../pages/Admin/AdminCareersPage';
import FavoritesPage from '../pages/Favorites/FavoritesPage';
import ReviewsPage from '../pages/Reviews/ReviewsPage';
import ForgotPasswordPage from '../pages/ForgotPassword/ForgotPasswordPage';
import UserPage from '../pages/Users/UserPage';

export default function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/forgot-password" element={<ForgotPasswordPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/search" element={<SearchPage />} />
      <Route path="/profile" element={<ProfilePage />} />
      <Route path="/favorites" element={<FavoritesPage />} />
      <Route path="/reviews" element={<ReviewsPage />} />
      <Route path="/notes" element={<NotesPage />} />
      <Route path="/notes/:noteId" element={<NotePage />} />
      <Route path="/directories/:directoryId" element={<DirectoryPage />} />
      <Route path="/admin/users" element={<AdminUsersPage />} />
      <Route path="/admin/careers" element={<AdminCareersPage />} />
      <Route path="/users/:userId" element={<UserPage />} />
      {/* Add more routes as needed */}
    </Routes>
  );
}
