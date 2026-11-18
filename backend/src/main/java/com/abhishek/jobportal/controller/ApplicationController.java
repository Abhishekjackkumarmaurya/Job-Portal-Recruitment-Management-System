package com.abhishek.jobportal.controller;

import com.abhishek.jobportal.dto.request.ApplicationRequest;
import com.abhishek.jobportal.dto.request.StatusUpdateRequest;
import com.abhishek.jobportal.dto.response.ApplicationResponse;
import com.abhishek.jobportal.dto.response.PageResponse;
import com.abhishek.jobportal.entity.JobApplication;
import com.abhishek.jobportal.security.CustomUserDetails;
import com.abhishek.jobportal.service.ApplicationService;
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
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Candidate: apply to a job
    @PostMapping("/api/jobs/{jobId}/applications")
    public ResponseEntity<ApplicationResponse> apply(@PathVariable Long jobId,
                                                       @Valid @RequestBody ApplicationRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails principal) {
        JobApplication application = applicationService.apply(jobId, request, principal.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApplicationResponse.fromEntity(application));
    }

    // Candidate: view own applications
    @GetMapping("/api/applications/mine")
    public ResponseEntity<PageResponse<ApplicationResponse>> myApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "appliedAt"));
        Page<JobApplication> applications = applicationService.getMyApplications(principal.getUser(), pageable);
        Page<ApplicationResponse> mapped = applications.map(ApplicationResponse::fromEntity);
        return ResponseEntity.ok(PageResponse.from(mapped));
    }

    // Recruiter: view applicants for a specific job they posted
    @GetMapping("/api/jobs/{jobId}/applications")
    public ResponseEntity<PageResponse<ApplicationResponse>> applicantsForJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "appliedAt"));
        Page<JobApplication> applications = applicationService.getApplicantsForJob(jobId, principal.getUser(), pageable);
        Page<ApplicationResponse> mapped = applications.map(ApplicationResponse::fromEntity);
        return ResponseEntity.ok(PageResponse.from(mapped));
    }

    // Recruiter: update an applicant's status (shortlist/reject/hire)
    @PatchMapping("/api/applications/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(@PathVariable Long applicationId,
                                                              @Valid @RequestBody StatusUpdateRequest request,
                                                              @AuthenticationPrincipal CustomUserDetails principal) {
        JobApplication application = applicationService.updateStatus(applicationId, request.getStatus(), principal.getUser());
        return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
    }
}
