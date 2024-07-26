package com.myershome.homeapp.repository;

import com.myershome.homeapp.model.TeddyEvent;
import com.myershome.homeapp.services.Constants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TeddyEventRepository extends JpaRepository<TeddyEvent, Long> {
    List<TeddyEvent> findAllByEventTypeAndEventDateAfter(Constants.TeddyEventType type, LocalDate eventDate);
    List<TeddyEvent> findAllByEventDateAfter(LocalDate eventDate);
}
