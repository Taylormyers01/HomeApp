package com.myershome.homeapp.model;

import com.myershome.homeapp.services.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {

    @Id
    @Column(name = "task_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @Column(name = "completed")
    private Boolean completed;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reoccuring")
    private Boolean reoccuring;

    @ElementCollection(targetClass = Constants.Days.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "days", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "days")
    private Set<Constants.Days> daysList = new HashSet<>();

    @Column(name = "due_date")
    private Date dueDate;
}
