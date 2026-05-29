package com.example.classbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "parent_timezone", nullable = false)
    private String parentTimezone;

    @ManyToOne
    @JoinColumn(name = "offering_id", nullable = false)
    private Offering offering;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt = LocalDateTime.now();
}