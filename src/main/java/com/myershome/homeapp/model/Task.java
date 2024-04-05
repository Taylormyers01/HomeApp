package com.myershome.homeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myershome.homeapp.services.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.*;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {

    @Id
    @Column(name = "task_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "task_name", nullable = false)
    private String taskName;


    @Column(name = "completed")
    private Boolean completed;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @NotNull
    @Column(name = "reoccuring")
    private Boolean reoccuring;

    @ElementCollection(targetClass = Constants.Days.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "days", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "days")
    private Set<Constants.Days> daysList = new HashSet<>();

    @Column(name = "due_date")
    private Date dueDate;

    public String getUserName() {
        if(user != null){
            return user.getUsername();
        }
        return "None";
    }
}
