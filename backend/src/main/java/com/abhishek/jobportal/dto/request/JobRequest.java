package com.abhishek.jobportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @NotBlank(message = "At least one required skill must be listed")
    private String skillsRequired;

    @PositiveOrZero(message = "Minimum salary must be zero or positive")
    private Double minSalary;

    @PositiveOrZero(message = "Maximum salary must be zero or positive")
    private Double maxSalary;
}
