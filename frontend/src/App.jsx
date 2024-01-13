import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import { Home, Profile, Login, RequireAuth } from './pages';
import Navbar from './components/Navbar.jsx';
import Sidebar from './components/Sidebar.jsx';
import { useSelector } from 'react-redux';
import { selectCurrentToken } from './store/slices/authSlice';

function App() {
  const token = useSelector(selectCurrentToken);
  return (
    <Router>
      <Navbar />
      {token && <Sidebar />}
      <main>
        <Routes>
          {/* Public Routes */}
          <Route index element={<Home />} />
          <Route path="/login" element={<Login />} />

          {/* Protected Routes */}
          <Route element={<RequireAuth />}>
            <Route path="/profile" element={<Profile />} />
          </Route>
        </Routes>
      </main>
    </Router>
  );
}

export default App;
