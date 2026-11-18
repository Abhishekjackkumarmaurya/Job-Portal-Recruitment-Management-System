import { useEffect, useState } from 'react'
import api from '../api/axios'

export default function MyJobs() {
  const [jobs, setJobs] = useState([])
  const [selectedJobId, setSelectedJobId] = useState(null)
  const [applicants, setApplicants] = useState([])

  useEffect(() => {
    api.get('/jobs/mine', { params: { page: 0, size: 20 } }).then(({ data }) => setJobs(data.content))
  }, [])

  const viewApplicants = async (jobId) => {
    setSelectedJobId(jobId)
    const { data } = await api.get(`/jobs/${jobId}/applications`, { params: { page: 0, size: 20 } })
    setApplicants(data.content)
  }

  const updateStatus = async (applicationId, status) => {
    await api.patch(`/applications/${applicationId}/status`, { status })
    viewApplicants(selectedJobId)
  }

  const closeJob = async (jobId) => {
    await api.delete(`/jobs/${jobId}`)
    setJobs(jobs.map((j) => (j.id === jobId ? { ...j, active: false } : j)))
  }

  return (
    <div className="max-w-5xl mx-auto mt-8 px-4 grid grid-cols-2 gap-6">
      <div>
        <h1 className="text-xl font-bold mb-4">My Job Postings</h1>
        <div className="space-y-3">
          {jobs.map((job) => (
            <div key={job.id} className="bg-white p-4 rounded shadow">
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="font-semibold">{job.title}</h3>
                  <p className="text-sm text-gray-500">{job.location} &middot; {job.employmentType}</p>
                  <span className={`text-xs px-2 py-0.5 rounded ${job.active ? 'bg-green-100 text-green-700' : 'bg-gray-200 text-gray-600'}`}>
                    {job.active ? 'Active' : 'Closed'}
                  </span>
                </div>
                <div className="flex flex-col gap-1">
                  <button onClick={() => viewApplicants(job.id)} className="text-indigo-600 text-sm">View applicants</button>
                  {job.active && <button onClick={() => closeJob(job.id)} className="text-red-500 text-sm">Close</button>}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div>
        <h1 className="text-xl font-bold mb-4">Applicants</h1>
        {!selectedJobId && <p className="text-gray-500">Select a job to view its applicants.</p>}
        <div className="space-y-3">
          {applicants.map((app) => (
            <div key={app.id} className="bg-white p-4 rounded shadow">
              <p className="font-semibold">{app.candidateName}</p>
              <p className="text-sm text-gray-500">{app.candidateEmail}</p>
              {app.coverNote && <p className="text-sm text-gray-700 mt-1">{app.coverNote}</p>}
              <select value={app.status} onChange={(e) => updateStatus(app.id, e.target.value)}
                className="mt-2 border rounded px-2 py-1 text-sm">
                <option value="APPLIED">Applied</option>
                <option value="UNDER_REVIEW">Under Review</option>
                <option value="SHORTLISTED">Shortlisted</option>
                <option value="REJECTED">Rejected</option>
                <option value="HIRED">Hired</option>
              </select>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
