package com.example.classbooking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddSessionRequest {

    @NotNull(message = "Offering ID is required")
    private Long offeringId;

    @NotNull(message = "Start time is required")
    private String startTime;

    @NotNull(message = "End time is required")
    private String endTime;
}