package com.example.classbooking.service;

import com.example.classbooking.dto.AddSessionRequest;
import com.example.classbooking.dto.CreateOfferingRequest;
import com.example.classbooking.entity.Offering;
import com.example.classbooking.entity.Session;
import com.example.classbooking.repository.CourseRepository;
import com.example.classbooking.repository.OfferingRepository;
import com.example.classbooking.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final CourseRepository courseRepository;
    private final OfferingRepository offeringRepository;
    private final SessionRepository sessionRepository;

    // Create an offering
    public Offering createOffering(CreateOfferingRequest request) {
        var course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Offering offering = new Offering();
        offering.setName(request.getName());
        offering.setTeacherId(request.getTeacherId());
        offering.setTeacherTimezone(request.getTeacherTimezone());
        offering.setCourse(course);

        return offeringRepository.save(offering);
    }

    // Add a session to an offering
    public Session addSession(AddSessionRequest request) {
        var offering = offeringRepository.findById(request.getOfferingId())
                .orElseThrow(() -> new RuntimeException("Offering not found"));

        // Parse the time the teacher sends in their timezone
        ZoneId teacherZone = ZoneId.of(offering.getTeacherTimezone());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

        // Convert to UTC before saving
        ZonedDateTime startUtc = ZonedDateTime.parse(request.getStartTime(), formatter)
                .withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUtc = ZonedDateTime.parse(request.getEndTime(), formatter)
                .withZoneSameInstant(ZoneId.of("UTC"));

        Session session = new Session();
        session.setOffering(offering);
        session.setStartTime(startUtc);
        session.setEndTime(endUtc);

        return sessionRepository.save(session);
    }

    // Get all offerings by a teacher
    public List<Offering> getTeacherOfferings(Long teacherId) {
        return offeringRepository.findAll()
                .stream()
                .filter(o -> o.getTeacherId().equals(teacherId))
                .toList();
    }
}