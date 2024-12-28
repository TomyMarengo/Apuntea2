// src/routes/AppRouter.tsx

import { Routes, Route } from 'react-router-dom';
import HomePage from '../pages/Home/HomePage';
import LoginPage from '../pages/Login/LoginPage';
import RegisterPage from '../pages/Register/RegisterPage';
import SearchPage from '../pages/Search/SearchPage';
import NotePage from '../pages/Notes/NotePage';
import ParentDirectoryPage from '../pages/Directories/ParentDirectoryPage';
import OwnerNotesPage from '../pages/Users/OwnerNotesPage';
import ProfilePage from '../pages/Profile/ProfilePage';
import AdminUsersPage from '../pages/Admin/AdminUsersPage';
import AdminCareersPage from '../pages/Admin/AdminCareersPage';
import FavoritesPage from '../pages/Favorites/FavoritesPage';

export default function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/search" element={<SearchPage />} />
      <Route path="/notes/:id" element={<NotePage />} />
      <Route path="/users/:ownerId/notes" element={<OwnerNotesPage />} />
      <Route path="/directories/:id" element={<ParentDirectoryPage />} />
      <Route path="/profile" element={<ProfilePage />} />
      <Route path="/favorites" element={<FavoritesPage />} />
      <Route path="/admin/users" element={<AdminUsersPage />} />
      <Route path="/admin/careers" element={<AdminCareersPage />} />
      {/* Add more routes as needed */}
    </Routes>
  );
}
