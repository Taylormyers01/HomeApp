package com.myershome.homeapp.controller;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.repository.TaskRepository;
import com.myershome.homeapp.services.Constants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myershome.homeapp.services.Constants.APIResources;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping(APIResources.taskURL)
public class TaskController implements Serializable {

    @Autowired
    TaskRepository taskRepository;

    private static final Logger LOG = LoggerFactory.getLogger(Task.class);
    @GetMapping()
    private ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok()
                .body(taskRepository.findAll());
    }

    @GetMapping("/add")
    private ResponseEntity<Task> addTask(){
        Task task = new Task();
        Set<Constants.Days> days = new HashSet<>();
        days.add(Constants.Days.FRIDAY);
        days.add(Constants.Days.SATURDAY);
        task.setDaysList(days);
        task.setTaskName("Testing");
        return ResponseEntity.ok()
                .body(taskRepository.save(task));
    }

    @PostMapping()
    private ResponseEntity<Task> addTask(@RequestBody @Valid Task task){
        LOG.info(String.valueOf(task));
        if(task.getId()==null){
            return ResponseEntity.ok()
                    .body(taskRepository.save(task));
        }
        return ResponseEntity.badRequest()
                .body(null);
    }
}
