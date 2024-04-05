package com.myershome.homeapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Table(name = "ingredient")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ingredient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @Column(name = "on_shopping_list", nullable = false)
    private Boolean onShoppingList;


}
