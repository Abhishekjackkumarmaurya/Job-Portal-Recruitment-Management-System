package com.abhishek.jobportal.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequest {

    @Size(max = 2000, message = "Cover note cannot exceed 2000 characters")
    private String coverNote;
}
