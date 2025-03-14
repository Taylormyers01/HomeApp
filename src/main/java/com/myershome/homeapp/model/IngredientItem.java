package com.myershome.homeapp.model;

import com.myershome.homeapp.services.Constants;
import org.apache.commons.lang3.math.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myershome.homeapp.services.Constants.Measurements;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "ingredient_item")
public class IngredientItem {

    private static final Logger LOG = LoggerFactory.getLogger(IngredientItem.class);

    @Id
    @Column(name = "ingredient_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    public Double amount;

    @Column(name = "measurement")
    public Measurements measurement;

    @ManyToOne(cascade = CascadeType.MERGE)
    public Ingredient ingredient;

    @Override
    public String toString() {

        return "IngredientItem{" +
                "id=" + id +
                ", amount=" + amountFrac() +
                ", measurement=" + measurement +
                ", ingredient=" + ingredient +
                '}';
    }

    public String getIngName() {
        validateIngredient();
        return this.ingredient.getIngName();
    }

    public void setIngName(String name) {
        validateIngredient();
        this.ingredient.setIngName(name);
    }

    private void validateIngredient() {
        if (this.ingredient == null) {
            this.ingredient = new Ingredient();
            this.ingredient.setIngName("");
        }
    }

    public String cleanString() {
        return String.format("%s %s %s ", amountFrac(), measurement.value, ingredient.getIngName());
    }

    public String amountFrac() {
        List<Constants.FRACTIONS> fracs = Constants.FRACTIONS.fracList;

        if (amount == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        double remiander = amount - amount.intValue();
        if (amount.intValue() > 0) {
            sb.append(amount.intValue()).append(" ");
        }
        if (fracs.stream().anyMatch(n -> n.decimal == remiander)) {
            sb.append(fracs.stream().filter(n -> n.decimal == remiander).findFirst().get().stringValue);
        }else if(remiander > 0 ){
            sb.append(Fraction.getFraction(remiander));
        }
        return sb.toString().strip();
    }
}
