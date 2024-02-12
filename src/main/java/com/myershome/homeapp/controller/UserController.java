package com.myershome.homeapp.controller;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.model.User;
import com.myershome.homeapp.repository.UserRepository;
import com.myershome.homeapp.services.Constants.APIResources;
import com.myershome.homeapp.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping(APIResources.userURL)
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(Task.class);


    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok()
                .body(userRepository.findAll());
    }

    @GetMapping("/add")
    public ResponseEntity<User> testUser(){
        User user = new User();
        user.setUsername("Taylor");
        Set<Task> taskSet = new HashSet<>();
        taskSet.add(new Task(1L, "Test", null, null, null,null, null));
        taskSet.add(new Task(null, "Test2", null, null, null,null, null));

        user.setTaskset(taskSet);
        return ResponseEntity.ok()
                .body(userService.save(user));
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@Valid User user){
        LOG.info("Post request for {}", user);
        return ResponseEntity.accepted()
                .body(userService.save(user));
    }
}
