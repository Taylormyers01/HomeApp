package com.myershome.homeapp.services;


import com.github.javaparser.quality.NotNull;
import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.repository.TaskRepository;
import com.myershome.homeapp.repository.UserRepository;
import com.myershome.homeapp.services.Constants.Days;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService{
    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    public List<Task> findAllTasks(String filterText) {
        if(filterText == null){
            return taskRepository.findAll();
        }
        else{
            return taskRepository.search(filterText);
        }
    }

    public List<Task> filter(List<Task> tasks, String filterText){

        return tasks.stream().filter(task -> task.getTaskName().toLowerCase()
                .contains(filterText.strip().toLowerCase()))
                .collect(Collectors.toList());
    }
    public List<Task> findAll(){
        return taskRepository.findAll();
    }
    public List<Task> findAllCompleted(boolean completed){
        return taskRepository.findAllByCompleted(completed);
    }

    public Task saveTask(Task task){
        LOG.info(task.toString());
        if(task.getUser() != null && task.getUser().getUserId() != null){
            var user = userRepository.findById(task.getUser().getUserId());
            user.ifPresent(task::setUser);
            if(user.isPresent()){
                task.setUser(user.get());
            }else {
                task.setUser(userRepository.save(task.getUser()));
            }
        }
        LOG.info("After finding user {}", task);
        return taskRepository.save(task);
    }

    public List<Task> getAllTaskByDay(@NotNull Days day){
        return this.taskRepository.findAllByDaysListContaining(day);
    }

    public void taskWeeklyCronJob(){
        LOG.info("Starting Task Weekly Cron Job");
        List<Task> currentlyCompleted = this.taskRepository.findAllByCompletedAndReoccuring(true, true);
        LOG.info("{} tasks to be updated restarted", currentlyCompleted.size());
        currentlyCompleted.forEach(t -> t.setCompleted(false));
        List<Task> doneTasks = this.taskRepository.saveAll(currentlyCompleted);
        LOG.info("Task Job completed {}", doneTasks);
    }

    public void taskDailyCronJob(){
        LOG.info("Starting Task Daily Cron Job");
        List<Task> currentlyCompleted = this.taskRepository.findAllByCompletedAndReoccuring(true, true);
        Constants.Days date = Constants.Days.valueOf(LocalDate.now().getDayOfWeek().toString().toUpperCase());
        for(Task t: currentlyCompleted){
            if(t.getDaysList().contains(date)){
                t.setCompleted(false);
            }
        }
        taskRepository.saveAll(currentlyCompleted);
        LOG.info("All tasks for " + date.value + " have been reset");
    }

    public void deleteTask(Task task) {
        task.setUser(null);
        taskRepository.delete(taskRepository.save(task));
    }
}
