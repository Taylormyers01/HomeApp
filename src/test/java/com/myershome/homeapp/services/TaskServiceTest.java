package com.myershome.homeapp.services;

import com.myershome.homeapp.model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    TaskService taskService = new TaskService();

    @Test
    void filter() {
        List<Task> input = List.of(Task.builder().taskName("C").build(),Task.builder().taskName("B").build(),
                Task.builder().taskName("A").build());
        List<Task> output = taskService.filter(input, "A");
        List<Task> output2 = taskService.filter(input, "b");
        List<Task> output3 = taskService.filter(input, "C ");

        assert output.get(0).getTaskName() == "A";
        assert output2.get(0).getTaskName() == "B";
        assert output3.get(0).getTaskName() == "C";
    }

    @Test
    void taskWeeklyCronJob() {
    }
}