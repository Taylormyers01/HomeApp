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

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
        if(user.getUserId() != null){
            var userOpt = userRepository.getReferenceById(user.getUserId());
            userOpt.setUsername(user.getUsername());
            return userRepository.save(userOpt);
        }
        return userRepository.save(user);
    }

    public User getUserWithTask(Integer id){
        return new User();
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public User findUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }else{
            return null;
        }
    }
}
