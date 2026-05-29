package com.example.classbooking.controller;

import com.example.classbooking.dto.BookingRequest;
import com.example.classbooking.entity.Booking;
import com.example.classbooking.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    // Get available offerings
    @GetMapping("/offerings")
    public ResponseEntity<?> getAvailableOfferings(
            @RequestParam String timezone) {
        return ResponseEntity.ok(parentService.getAvailableOfferings(timezone));
    }

    // Book an offering
    @PostMapping("/bookings")
    public ResponseEntity<Booking> bookOffering(
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(parentService.bookOffering(request));
    }

    // Get parent's bookings
    @GetMapping("/bookings/{parentId}")
    public ResponseEntity<List<Booking>> getBookings(
            @PathVariable Long parentId) {
        return ResponseEntity.ok(parentService.getBookings(parentId));
    }
}