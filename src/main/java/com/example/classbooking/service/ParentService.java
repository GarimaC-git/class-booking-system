package com.example.classbooking.service;

import com.example.classbooking.dto.BookingRequest;
import com.example.classbooking.entity.Booking;
import com.example.classbooking.entity.Session;
import com.example.classbooking.repository.BookingRepository;
import com.example.classbooking.repository.OfferingRepository;
import com.example.classbooking.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final BookingRepository bookingRepository;
    private final OfferingRepository offeringRepository;
    private final SessionRepository sessionRepository;

    // Get all available offerings
    public List<?> getAvailableOfferings(String parentTimezone) {
        ZoneId parentZone = ZoneId.of(parentTimezone);
        return offeringRepository.findAll()
                .stream()
                .map(offering -> {
                    List<ZonedDateTime[]> sessions = sessionRepository.findAll()
                            .stream()
                            .filter(s -> s.getOffering().getId().equals(offering.getId()))
                            .map(s -> new ZonedDateTime[]{
                                    s.getStartTime().withZoneSameInstant(parentZone),
                                    s.getEndTime().withZoneSameInstant(parentZone)
                            })
                            .toList();
                    return new java.util.HashMap<String, Object>() {{
                        put("offeringId", offering.getId());
                        put("name", offering.getName());
                        put("course", offering.getCourse().getName());
                        put("sessionsInYourTimezone", sessions);
                    }};
                })
                .toList();
    }

    // Book an offering — with conflict detection + concurrency handling
    @Transactional
    public Booking bookOffering(BookingRequest request) {
        var offering = offeringRepository.findById(request.getOfferingId())
                .orElseThrow(() -> new RuntimeException("Offering not found"));

        // Get all sessions of the offering to book
        List<Session> newSessions = sessionRepository.findAll()
                .stream()
                .filter(s -> s.getOffering().getId().equals(request.getOfferingId()))
                .toList();

        if (newSessions.isEmpty()) {
            throw new RuntimeException("This offering has no sessions yet");
        }

        // Check if parent already booked this offering
        boolean alreadyBooked = bookingRepository.findAll()
                .stream()
                .anyMatch(b -> b.getParentId().equals(request.getParentId())
                        && b.getOffering().getId().equals(request.getOfferingId()));

        if (alreadyBooked) {
            throw new RuntimeException("You have already booked this offering");
        }

        // Get all sessions from parent's existing bookings
        List<Session> bookedSessions = bookingRepository.findAll()
                .stream()
                .filter(b -> b.getParentId().equals(request.getParentId()))
                .flatMap(b -> sessionRepository.findAll()
                        .stream()
                        .filter(s -> s.getOffering().getId()
                                .equals(b.getOffering().getId())))
                .toList();

        // Check for time conflicts
        for (Session newSession : newSessions) {
            for (Session bookedSession : bookedSessions) {
                if (hasOverlap(newSession, bookedSession)) {
                    throw new RuntimeException(
                        "Time conflict! Session on " + newSession.getStartTime()
                        + " overlaps with an existing booking"
                    );
                }
            }
        }

        // No conflict — save the booking
        Booking booking = new Booking();
        booking.setParentId(request.getParentId());
        booking.setParentTimezone(request.getParentTimezone());
        booking.setOffering(offering);

        return bookingRepository.save(booking);
    }

    // Get all bookings for a parent
    public List<Booking> getBookings(Long parentId) {
        return bookingRepository.findAll()
                .stream()
                .filter(b -> b.getParentId().equals(parentId))
                .toList();
    }

    // Helper — check if two sessions overlap
    private boolean hasOverlap(Session a, Session b) {
        return a.getStartTime().isBefore(b.getEndTime())
                && b.getStartTime().isBefore(a.getEndTime());
    }
}