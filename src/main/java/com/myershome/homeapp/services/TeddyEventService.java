package com.myershome.homeapp.services;

import com.myershome.homeapp.model.TeddyEvent;
import com.myershome.homeapp.repository.TeddyEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TeddyEventService {

    @Autowired
    TeddyEventRepository repository;

    public List<TeddyEvent> findAll(){
        return repository.findAll();
    }

    public TeddyEvent save(TeddyEvent event) {
        return repository.save(event);
    }
    public void delete(TeddyEvent event) {repository.delete(event);}

    public List<TeddyEvent> findAllByEventType(Constants.TeddyEventType type, LocalDate eventDate){
        return repository.findAllByEventTypeAndEventDateAfter(type, eventDate);
    }

    public List<TeddyEvent> findAllByEventDateAfter(LocalDate eventDate){
        return repository.findAllByEventDateAfter(eventDate);
    }
}
