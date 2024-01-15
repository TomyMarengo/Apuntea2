import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import { Home, Profile, Login, Register, RequireAuth } from './pages';
import Navbar from './components/Navbar.jsx';
import Sidebar from './components/Sidebar.jsx';
import { useSelector } from 'react-redux';
import { selectCurrentToken } from './store/slices/authSlice';
import { Toaster } from 'sonner';

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
