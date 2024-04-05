package com.myershome.homeapp.controller;

import com.github.javaparser.quality.NotNull;
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
import org.springframework.web.bind.annotation.*;

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


    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody User user){
        LOG.info("Post request for {}", user);
        return ResponseEntity.accepted()
                .body(userService.save(user));
    }
//    @GetMapping("/{id}/task")
//    public ResponseEntity<User> getUserWithMeal(@RequestParam Integer id){
//        User user = this.userService
//    }
}
