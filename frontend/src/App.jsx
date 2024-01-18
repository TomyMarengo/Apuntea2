import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Toaster } from 'sonner';

import { selectCurrentToken } from './store/slices/authSlice';
import { Home, Profile, Login, Register, RequireAuth, Search, Note } from './pages';
import { Navbar, Sidebar } from './components/index';

function App() {
  const token = useSelector(selectCurrentToken);

  return (
    <Router>
      <Navbar />
      <main className="min-h-[calc(100vh-60px)] flex">
        {token && <Sidebar />}
        <Routes>
          {/* Public Routes */}
          <Route index element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/search" element={<Search />} />
          <Route path="/notes/:noteId" element={<Note />} loader={({ params }) => {  return params; } }/>

          {/* Protected Routes */}
          <Route element={<RequireAuth />}>
            <Route path="/profile" element={<Profile />} />
          </Route>
        </Routes>
      </main>
      {/* https://sonner.emilkowal.ski/ */}
      <Toaster richColors position="bottom-right" closeButton />
    </Router>
  );
}

export default App;
