package com.myershome.homeapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "meal")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private String recipeUrl;
}
