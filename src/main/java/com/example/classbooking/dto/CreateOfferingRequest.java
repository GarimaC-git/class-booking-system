package com.example.classbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOfferingRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotBlank(message = "Timezone is required")
    private String teacherTimezone;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}