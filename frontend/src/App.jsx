import { Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'
import JobList from './pages/JobList'
import JobDetail from './pages/JobDetail'
import Login from './pages/Login'
import Register from './pages/Register'
import PostJob from './pages/PostJob'
import MyJobs from './pages/MyJobs'
import MyApplications from './pages/MyApplications'

export default function App() {
  return (
    <div>
      <Navbar />
      <Routes>
        <Route path="/" element={<JobList />} />
        <Route path="/jobs/:id" element={<JobDetail />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/post-job" element={
          <ProtectedRoute role="RECRUITER"><PostJob /></ProtectedRoute>
        } />
        <Route path="/my-jobs" element={
          <ProtectedRoute role="RECRUITER"><MyJobs /></ProtectedRoute>
        } />
        <Route path="/my-applications" element={
          <ProtectedRoute role="CANDIDATE"><MyApplications /></ProtectedRoute>
        } />
      </Routes>
    </div>
  )
}
