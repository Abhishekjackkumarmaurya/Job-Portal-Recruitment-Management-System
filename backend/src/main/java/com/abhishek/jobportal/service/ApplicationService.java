package com.abhishek.jobportal.service;

import com.abhishek.jobportal.dto.request.ApplicationRequest;
import com.abhishek.jobportal.entity.*;
import com.abhishek.jobportal.exception.BadRequestException;
import com.abhishek.jobportal.exception.DuplicateResourceException;
import com.abhishek.jobportal.exception.ForbiddenActionException;
import com.abhishek.jobportal.exception.ResourceNotFoundException;
import com.abhishek.jobportal.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobService jobService;

    public JobApplication apply(Long jobId, ApplicationRequest request, User candidate) {
        if (candidate.getRole() != Role.CANDIDATE) {
            throw new ForbiddenActionException("Only candidates can apply to jobs");
        }
        Job job = jobService.getJobOrThrow(jobId);
        if (!job.getActive()) {
            throw new BadRequestException("This job is no longer accepting applications");
        }
        if (applicationRepository.existsByJobAndCandidate(job, candidate)) {
            throw new DuplicateResourceException("You have already applied to this job");
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .candidate(candidate)
                .coverNote(request.getCoverNote())
                .status(ApplicationStatus.APPLIED)
                .build();

        return applicationRepository.save(application);
    }

    public Page<JobApplication> getMyApplications(User candidate, Pageable pageable) {
        return applicationRepository.findByCandidate(candidate, pageable);
    }

    public Page<JobApplication> getApplicantsForJob(Long jobId, User recruiter, Pageable pageable) {
        Job job = jobService.getJobOrThrow(jobId);
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new ForbiddenActionException("You can only view applicants for jobs you posted");
        }
        return applicationRepository.findByJob(job, pageable);
    }

    public JobApplication updateStatus(Long applicationId, ApplicationStatus status, User recruiter) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new ForbiddenActionException("You can only update applications for your own job postings");
        }

        application.setStatus(status);
        return applicationRepository.save(application);
    }
}
