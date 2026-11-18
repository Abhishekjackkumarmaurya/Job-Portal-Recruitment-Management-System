import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import api from '../api/axios'
import { useAuth } from '../context/AuthContext'

export default function JobDetail() {
  const { id } = useParams()
  const [job, setJob] = useState(null)
  const [coverNote, setCoverNote] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const { user } = useAuth()

  useEffect(() => {
    api.get(`/jobs/${id}`).then(({ data }) => setJob(data))
  }, [id])

  const handleApply = async (e) => {
    e.preventDefault()
    setError('')
    setMessage('')
    try {
      await api.post(`/jobs/${id}/applications`, { coverNote })
      setMessage('Application submitted successfully!')
      setCoverNote('')
    } catch (err) {
      setError(err.response?.data?.message || 'Could not submit application')
    }
  }

  if (!job) return <p className="text-center mt-10">Loading...</p>

  return (
    <div className="max-w-3xl mx-auto mt-8 px-4">
      <div className="bg-white p-6 rounded-lg shadow">
        <h1 className="text-2xl font-bold">{job.title}</h1>
        <p className="text-gray-500">{job.recruiterCompany} &middot; {job.location} &middot; {job.employmentType}</p>
        {(job.minSalary || job.maxSalary) && (
          <p className="text-sm text-gray-600 mt-1">Salary: {job.minSalary ?? '—'} - {job.maxSalary ?? '—'}</p>
        )}
        <div className="mt-4">
          <h3 className="font-semibold">Description</h3>
          <p className="text-gray-700 whitespace-pre-line">{job.description}</p>
        </div>
        <div className="mt-4">
          <h3 className="font-semibold">Skills required</h3>
          <p className="text-gray-700">{job.skillsRequired}</p>
        </div>
      </div>

      {user?.role === 'CANDIDATE' && (
        <div className="bg-white p-6 rounded-lg shadow mt-6">
          <h3 className="font-semibold mb-3">Apply to this job</h3>
          {message && <div className="bg-green-50 text-green-700 text-sm p-3 rounded mb-3">{message}</div>}
          {error && <div className="bg-red-50 text-red-600 text-sm p-3 rounded mb-3">{error}</div>}
          <form onSubmit={handleApply} className="space-y-3">
            <textarea rows={4} placeholder="Cover note (optional)" value={coverNote}
              onChange={(e) => setCoverNote(e.target.value)} className="w-full border rounded px-3 py-2" />
            <button className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700">Submit Application</button>
          </form>
        </div>
      )}
    </div>
  )
}
