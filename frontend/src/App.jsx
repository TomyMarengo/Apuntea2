import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import { Home, Profile, Login, RequireAuth } from './pages';
import Navbar from './components/Navbar.jsx';

function App() {
  return (
    <main className="min-h-[100vh]">
      <Router>
        <Navbar />
        <Routes>
          {/* Public Routes */}
          <Route index element={<Home />} />
          <Route path="/login" element={<Login />} />

          {/* Protected Routes */}
          <Route element={<RequireAuth />}>
            <Route path="/profile" element={<Profile />} />
          </Route>
        </Routes>
      </Router>
    </main>
  );
}

export default App;
