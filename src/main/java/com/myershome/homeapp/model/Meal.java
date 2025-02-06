package com.myershome.homeapp.model;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import com.myershome.homeapp.services.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // fetch = FetchType.EAGER, cascade = CascadeType.REMOVE
    @OneToMany(fetch = FetchType.EAGER)
    private List<IngredientItem> ingredientItems;
}
