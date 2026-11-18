import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-white border-b shadow-sm px-6 py-3 flex items-center justify-between">
      <Link to="/" className="text-xl font-bold text-indigo-600">JobPortal</Link>
      <div className="flex items-center gap-4 text-sm">
        <Link to="/" className="hover:text-indigo-600">Browse Jobs</Link>
        {user?.role === 'RECRUITER' && (
          <>
            <Link to="/post-job" className="hover:text-indigo-600">Post a Job</Link>
            <Link to="/my-jobs" className="hover:text-indigo-600">My Jobs</Link>
          </>
        )}
        {user?.role === 'CANDIDATE' && (
          <Link to="/my-applications" className="hover:text-indigo-600">My Applications</Link>
        )}
        {user ? (
          <>
            <span className="text-gray-500">Hi, {user.fullName}</span>
            <button onClick={handleLogout} className="bg-gray-100 px-3 py-1.5 rounded hover:bg-gray-200">
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="hover:text-indigo-600">Login</Link>
            <Link to="/register" className="bg-indigo-600 text-white px-3 py-1.5 rounded hover:bg-indigo-700">
              Sign Up
            </Link>
          </>
        )}
      </div>
    </nav>
  )
}
