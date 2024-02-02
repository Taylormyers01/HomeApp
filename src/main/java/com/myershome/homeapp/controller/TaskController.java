package com.myershome.homeapp.controller;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myershome.homeapp.services.Constants.APIResources;
import java.io.Serializable;
import java.util.List;

@RestController()
@RequestMapping(APIResources.taskURL)
public class TaskController implements Serializable {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping()
    private ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok()
                .body(taskRepository.findAll());
    }

    @PostMapping()
    private ResponseEntity<Task> addTask(@RequestBody @Valid Task task){
        if(task.getId()==null){
            return ResponseEntity.ok()
                    .body(taskRepository.save(task));
        }
        return ResponseEntity.badRequest()
                .body(null);
    }
}
