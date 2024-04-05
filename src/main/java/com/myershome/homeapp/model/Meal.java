package com.myershome.homeapp.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

@Entity
@Table(name = "meal")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "meal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meal_name", nullable = false)
    private String mealName;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "rel_meal_ingredients",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredientList = new HashSet<>();

    @Column(name = "meal_date", nullable = false)
    private Date mealDate;

    @Column(name = "recipe_url")
    private URL recipeUrl;

    @Column(name = "picture_url")
    private URL pictureUrl;

}
