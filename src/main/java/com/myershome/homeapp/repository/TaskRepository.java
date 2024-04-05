package com.myershome.homeapp.repository;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.services.Constants;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t " +
            "where lower(t.taskName) like lower(concat('%', :searchTerm, '%'))")
    List<Task> search(String searchTerm);

    List<Task> findAllByCompletedAndReoccuring(@NotNull Boolean completed, @NotNull Boolean reoccuring);
    List<Task> findAllByCompleted(@NotNull Boolean completed);
    List<Task> findAllByDaysListContaining(Constants.Days day);
}
