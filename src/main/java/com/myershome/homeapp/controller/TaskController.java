package com.myershome.homeapp.controller;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.repository.TaskRepository;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myershome.homeapp.services.Constants.APIResources;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController()
@RequestMapping(APIResources.taskURL)
public class TaskController implements Serializable {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskService taskService;

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
                    .body(taskService.saveTask(task));
        }
        return ResponseEntity.badRequest()
                .body(null);
    }

    @GetMapping("/completed")
    private ResponseEntity<List<Task>> findAllCompleted(){
        LOG.info("Finding all completed Tasks");
        List<Task> tasks = taskRepository.findAllByCompleted(true);
        if(!tasks.isEmpty()){
            return ResponseEntity.ok()
                    .body(tasks);
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/taskbyday")
    private ResponseEntity<List<Task>> taskByDay(@RequestBody String day){
        LOG.info(day);
        Constants.Days dayEnum = Constants.Days.valueOf(day);
        LOG.info(dayEnum.value);
        List<Task> tasks = this.taskService.getAllTaskByDay(dayEnum);
       if(tasks != null && !tasks.isEmpty()){
           return ResponseEntity.ok()
                   .body(tasks);
       }
       return ResponseEntity.ofNullable(null);
    }

    @GetMapping("/testing")
    private ResponseEntity<Integer> testing(){
        return ResponseEntity.ok()
                .body(Constants.Days.SUNDAY.ordinal());
    }
}
