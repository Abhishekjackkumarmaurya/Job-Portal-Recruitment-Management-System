package com.abhishek.jobportal.repository;

import com.abhishek.jobportal.entity.Job;
import com.abhishek.jobportal.entity.JobApplication;
import com.abhishek.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByCandidate(User candidate, Pageable pageable);

    Page<JobApplication> findByJob(Job job, Pageable pageable);

    Optional<JobApplication> findByJobAndCandidate(Job job, User candidate);

    boolean existsByJobAndCandidate(Job job, User candidate);
}
