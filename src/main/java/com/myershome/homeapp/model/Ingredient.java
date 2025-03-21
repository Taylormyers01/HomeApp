package com.myershome.homeapp.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ingredient {

    // private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ingredient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingredient_name", nullable = false)
    private String ingName;

    @Column(name = "on_shopping_list", nullable = true)
    private Boolean onShoppingList;

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingName='" + ingName + '\'' +
                ", id=" + id +
                '}';
    }

}
