package com.abhishek.jobportal.dto.response;

import com.abhishek.jobportal.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private String skillsRequired;
    private Double minSalary;
    private Double maxSalary;
    private Boolean active;
    private Long recruiterId;
    private String recruiterCompany;
    private LocalDateTime createdAt;

    public static JobResponse fromEntity(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .skillsRequired(job.getSkillsRequired())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .active(job.getActive())
                .recruiterId(job.getRecruiter().getId())
                .recruiterCompany(job.getRecruiter().getCompanyName())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
