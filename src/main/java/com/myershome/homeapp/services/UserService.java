package com.myershome.homeapp.services;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.model.User;
import com.myershome.homeapp.repository.TaskRepository;
import com.myershome.homeapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
public class UserService {


    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    private static final Logger LOG = LoggerFactory.getLogger(Task.class);

    public User save(User user){
        LOG.info("Saving " + user);
        if(user.getUserId() != null && userRepository.findById(user.getUserId()).isPresent()){
            User oldUser = userRepository.findById(user.getUserId()).get();
            LOG.info("Updating User {} with {}", oldUser, user);
            for(Task t: user.getTaskset()){
                t.setUser(oldUser);
            }
            oldUser.setUserId(user.getUserId());
            oldUser.setTaskset(user.getTaskset());
            return userRepository.save(oldUser);
        }else{

            Set<Task> taskset = user.getTaskset();
            LOG.info("Taskset {}", taskset);

            user.setTaskset(null);
            LOG.info("Saving User {}", user);
            user = userRepository.save(user);
            for(Task t: taskset){
                t.setUser(user);
            }
            LOG.info("Saving TaskSet {}", taskset);
            taskRepository.saveAll(taskset);
            LOG.info("Saved TaskSet");
            return userRepository.findById(user.getUserId()).orElse(null);

        }
    }
}
