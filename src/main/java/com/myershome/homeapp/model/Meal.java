package com.myershome.homeapp.model;


import com.myershome.homeapp.services.Constants;
import jakarta.persistence.*;
import lombok.*;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

@Entity
@Table(name = "meal")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "meal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meal_name", nullable = false)
    private String mealName;

    @Column(name = "meal_day", nullable = true)
    private Constants.Days mealDay;

    @Column(name = "recipe_url")
    private String recipeUrl;

    @Column(name = "picture_url")
    private URL pictureUrl;

    @Lob
    @Column(name = "directions", columnDefinition = "text")
    private String directions;
}
