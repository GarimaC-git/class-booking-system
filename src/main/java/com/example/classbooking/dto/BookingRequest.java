package com.example.classbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "Parent ID is required")
    private Long parentId;

    @NotBlank(message = "Parent timezone is required")
    private String parentTimezone;

    @NotNull(message = "Offering ID is required")
    private Long offeringId;
}