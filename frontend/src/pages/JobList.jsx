import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api/axios'

export default function JobList() {
  const [jobs, setJobs] = useState([])
  const [keyword, setKeyword] = useState('')
  const [location, setLocation] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(false)

  const fetchJobs = async (pageNum = 0) => {
    setLoading(true)
    try {
      const { data } = await api.get('/jobs', {
        params: { keyword, location, page: pageNum, size: 6 },
      })
      setJobs(data.content)
      setTotalPages(data.totalPages)
      setPage(data.pageNumber)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchJobs(0) }, [])

  const handleSearch = (e) => {
    e.preventDefault()
    fetchJobs(0)
  }

  return (
    <div className="max-w-4xl mx-auto mt-8 px-4">
      <h1 className="text-2xl font-bold mb-4">Browse Jobs</h1>
      <form onSubmit={handleSearch} className="flex gap-3 mb-6">
        <input placeholder="Keyword or skill" value={keyword} onChange={(e) => setKeyword(e.target.value)}
          className="flex-1 border rounded px-3 py-2" />
        <input placeholder="Location" value={location} onChange={(e) => setLocation(e.target.value)}
          className="flex-1 border rounded px-3 py-2" />
        <button className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700">Search</button>
      </form>

      {loading ? <p>Loading...</p> : (
        <div className="space-y-4">
          {jobs.length === 0 && <p className="text-gray-500">No jobs found.</p>}
          {jobs.map((job) => (
            <Link to={`/jobs/${job.id}`} key={job.id}
              className="block bg-white p-5 rounded-lg shadow hover:shadow-md transition">
              <div className="flex justify-between items-start">
                <div>
                  <h2 className="font-semibold text-lg">{job.title}</h2>
                  <p className="text-sm text-gray-500">{job.recruiterCompany} &middot; {job.location} &middot; {job.employmentType}</p>
                </div>
                {(job.minSalary || job.maxSalary) && (
                  <span className="text-sm text-gray-600">
                    {job.minSalary ?? '—'} - {job.maxSalary ?? '—'}
                  </span>
                )}
              </div>
              <p className="text-sm text-gray-600 mt-2 line-clamp-2">{job.skillsRequired}</p>
            </Link>
          ))}
        </div>
      )}

      {totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-6">
          <button disabled={page === 0} onClick={() => fetchJobs(page - 1)}
            className="px-3 py-1 border rounded disabled:opacity-40">Prev</button>
          <span className="px-3 py-1">Page {page + 1} of {totalPages}</span>
          <button disabled={page + 1 >= totalPages} onClick={() => fetchJobs(page + 1)}
            className="px-3 py-1 border rounded disabled:opacity-40">Next</button>
        </div>
      )}
    </div>
  )
}
