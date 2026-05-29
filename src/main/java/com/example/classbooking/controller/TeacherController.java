package com.example.classbooking.controller;

import com.example.classbooking.dto.AddSessionRequest;
import com.example.classbooking.dto.CreateOfferingRequest;
import com.example.classbooking.entity.Offering;
import com.example.classbooking.entity.Session;
import com.example.classbooking.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Create an offering
    @PostMapping("/offerings")
    public ResponseEntity<Offering> createOffering(
            @Valid @RequestBody CreateOfferingRequest request) {
        return ResponseEntity.ok(teacherService.createOffering(request));
    }

    // Add session to an offering
    @PostMapping("/sessions")
    public ResponseEntity<Session> addSession(
            @Valid @RequestBody AddSessionRequest request) {
        return ResponseEntity.ok(teacherService.addSession(request));
    }

    // Get all offerings by teacher
    @GetMapping("/offerings/{teacherId}")
    public ResponseEntity<List<Offering>> getOfferings(
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherOfferings(teacherId));
    }
}