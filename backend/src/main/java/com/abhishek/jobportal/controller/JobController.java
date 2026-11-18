package com.abhishek.jobportal.controller;

import com.abhishek.jobportal.dto.request.JobRequest;
import com.abhishek.jobportal.dto.response.JobResponse;
import com.abhishek.jobportal.dto.response.PageResponse;
import com.abhishek.jobportal.entity.Job;
import com.abhishek.jobportal.security.CustomUserDetails;
import com.abhishek.jobportal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // Public: search/browse active jobs, paginated + filterable
    @GetMapping
    public ResponseEntity<PageResponse<JobResponse>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String employmentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobs = jobService.searchJobs(keyword, location, employmentType, pageable);
        Page<JobResponse> mapped = jobs.map(JobResponse::fromEntity);
        return ResponseEntity.ok(PageResponse.from(mapped));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(@PathVariable Long id) {
        Job job = jobService.getJobOrThrow(id);
        return ResponseEntity.ok(JobResponse.fromEntity(job));
    }

    // Recruiter: create job posting
    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        Job job = jobService.createJob(request, principal.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(JobResponse.fromEntity(job));
    }

    // Recruiter: update own job posting
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id,
                                                  @Valid @RequestBody JobRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        Job job = jobService.updateJob(id, request, principal.getUser());
        return ResponseEntity.ok(JobResponse.fromEntity(job));
    }

    // Recruiter: close/deactivate own job posting
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateJob(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails principal) {
        jobService.deactivateJob(id, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    // Recruiter: view jobs they've posted
    @GetMapping("/mine")
    public ResponseEntity<PageResponse<JobResponse>> myJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobs = jobService.getJobsByRecruiter(principal.getUser(), pageable);
        Page<JobResponse> mapped = jobs.map(JobResponse::fromEntity);
        return ResponseEntity.ok(PageResponse.from(mapped));
    }
}
