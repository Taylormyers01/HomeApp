package com.myershome.homeapp.controller;

import com.myershome.homeapp.model.TeddyEvent;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.TeddyEventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;



@RestController
@RequestMapping(Constants.APIResources.teddyEventUrl)
public class TeddyEventController {

    @Autowired
    TeddyEventService service;
    private static final Logger LOG = LoggerFactory.getLogger(TeddyEvent.class);

    @GetMapping
    public ResponseEntity<List<TeddyEvent>> getAllTeddyEvent(){
        return ResponseEntity
                .ok(service.findAll());
    }

    @GetMapping("/testing")
    public void testing(){
        TeddyEvent event = TeddyEvent.builder()
                .eventType(Constants.TeddyEventType.NAP)
                .eventDate(LocalDate.now())
                .timeOfEvent(LocalTime.now())
                .amount("8 hours")
                .build();
        service.save(event);

    }

    @PostMapping()
    private ResponseEntity<TeddyEvent> addTeddyEvent(@RequestBody @Valid TeddyEvent teddyEvent){
        LOG.info(String.valueOf(teddyEvent));
        if(teddyEvent.getId()==null){
            return ResponseEntity.ok()
                    .body(service.save(teddyEvent));
        }
        return ResponseEntity.badRequest()
                .body(null);
    }
}
