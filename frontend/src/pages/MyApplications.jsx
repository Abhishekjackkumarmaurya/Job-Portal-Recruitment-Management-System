import { useEffect, useState } from 'react'
import api from '../api/axios'

const statusColors = {
  APPLIED: 'bg-blue-100 text-blue-700',
  UNDER_REVIEW: 'bg-yellow-100 text-yellow-700',
  SHORTLISTED: 'bg-purple-100 text-purple-700',
  REJECTED: 'bg-red-100 text-red-700',
  HIRED: 'bg-green-100 text-green-700',
}

export default function MyApplications() {
  const [applications, setApplications] = useState([])

  useEffect(() => {
    api.get('/applications/mine', { params: { page: 0, size: 20 } }).then(({ data }) => setApplications(data.content))
  }, [])

  return (
    <div className="max-w-3xl mx-auto mt-8 px-4">
      <h1 className="text-2xl font-bold mb-4">My Applications</h1>
      <div className="space-y-3">
        {applications.length === 0 && <p className="text-gray-500">You haven't applied to any jobs yet.</p>}
        {applications.map((app) => (
          <div key={app.id} className="bg-white p-4 rounded shadow flex justify-between items-center">
            <div>
              <h3 className="font-semibold">{app.jobTitle}</h3>
              <p className="text-sm text-gray-500">Applied on {new Date(app.appliedAt).toLocaleDateString()}</p>
            </div>
            <span className={`text-xs px-2 py-1 rounded ${statusColors[app.status]}`}>{app.status}</span>
          </div>
        ))}
      </div>
    </div>
  )
}
