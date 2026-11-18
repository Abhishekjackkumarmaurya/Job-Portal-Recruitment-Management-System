package com.abhishek.jobportal.service;

import com.abhishek.jobportal.dto.request.JobRequest;
import com.abhishek.jobportal.entity.Job;
import com.abhishek.jobportal.entity.Role;
import com.abhishek.jobportal.entity.User;
import com.abhishek.jobportal.exception.ForbiddenActionException;
import com.abhishek.jobportal.exception.ResourceNotFoundException;
import com.abhishek.jobportal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Job createJob(JobRequest request, User recruiter) {
        if (recruiter.getRole() != Role.RECRUITER) {
            throw new ForbiddenActionException("Only recruiters can post jobs");
        }
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .skillsRequired(request.getSkillsRequired())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .recruiter(recruiter)
                .active(true)
                .build();
        return jobRepository.save(job);
    }

    public Job updateJob(Long jobId, JobRequest request, User recruiter) {
        Job job = getJobOrThrow(jobId);
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new ForbiddenActionException("You can only edit jobs you posted");
        }
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        return jobRepository.save(job);
    }

    public void deactivateJob(Long jobId, User recruiter) {
        Job job = getJobOrThrow(jobId);
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new ForbiddenActionException("You can only close jobs you posted");
        }
        job.setActive(false);
        jobRepository.save(job);
    }

    public Job getJobOrThrow(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
    }

    public Page<Job> searchJobs(String keyword, String location, String employmentType, Pageable pageable) {
        return jobRepository.search(
                blankToNull(keyword), blankToNull(location), blankToNull(employmentType), pageable);
    }

    public Page<Job> getJobsByRecruiter(User recruiter, Pageable pageable) {
        return jobRepository.findByRecruiter(recruiter, pageable);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
