package com.myershome.homeapp.model;

import com.myershome.homeapp.services.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "teddy_event")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeddyEvent {
    @Id
    @Column(name = "teddy_event_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_time")
    private LocalTime timeOfEvent;

    @Column(name = "event_type", nullable = false)
    private Constants.TeddyEventType eventType;

    @Column(name = "amount")
    private String amount;
}
