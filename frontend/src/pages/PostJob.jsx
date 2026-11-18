import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'

export default function PostJob() {
  const [form, setForm] = useState({
    title: '', description: '', location: '', employmentType: 'FULL_TIME',
    skillsRequired: '', minSalary: '', maxSalary: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const payload = {
        ...form,
        minSalary: form.minSalary ? Number(form.minSalary) : null,
        maxSalary: form.maxSalary ? Number(form.maxSalary) : null,
      }
      await api.post('/jobs', payload)
      navigate('/my-jobs')
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create job posting')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto mt-8 bg-white p-8 rounded-lg shadow">
      <h1 className="text-2xl font-bold mb-6">Post a Job</h1>
      {error && <div className="bg-red-50 text-red-600 text-sm p-3 rounded mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <input required placeholder="Job title" value={form.title} onChange={update('title')} className="w-full border rounded px-3 py-2" />
        <textarea required rows={5} placeholder="Description" value={form.description} onChange={update('description')} className="w-full border rounded px-3 py-2" />
        <div className="grid grid-cols-2 gap-4">
          <input required placeholder="Location" value={form.location} onChange={update('location')} className="border rounded px-3 py-2" />
          <select value={form.employmentType} onChange={update('employmentType')} className="border rounded px-3 py-2">
            <option value="FULL_TIME">Full-time</option>
            <option value="PART_TIME">Part-time</option>
            <option value="INTERNSHIP">Internship</option>
            <option value="CONTRACT">Contract</option>
          </select>
        </div>
        <input required placeholder="Required skills (comma separated)" value={form.skillsRequired} onChange={update('skillsRequired')} className="w-full border rounded px-3 py-2" />
        <div className="grid grid-cols-2 gap-4">
          <input type="number" placeholder="Min salary" value={form.minSalary} onChange={update('minSalary')} className="border rounded px-3 py-2" />
          <input type="number" placeholder="Max salary" value={form.maxSalary} onChange={update('maxSalary')} className="border rounded px-3 py-2" />
        </div>
        <button disabled={loading} className="w-full bg-indigo-600 text-white py-2 rounded hover:bg-indigo-700 disabled:opacity-50">
          {loading ? 'Posting...' : 'Post Job'}
        </button>
      </form>
    </div>
  )
}
