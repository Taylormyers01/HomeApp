package com.myershome.homeapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class ScheduledTasks {

    @Autowired
    TaskService taskService;

    @Scheduled(cron = "0 0 0 * * ?")
    private void resetTaskStatusJob(){
        taskService.taskWeeklyCronJob();
    }

    //runs every night to reset jobs from completed to incomplete
    @Scheduled(cron = "0 0 1 * * ?")
    private void dailyTaskCronJob(){
        taskService.taskDailyCronJob();
    }
}
