package com.myershome.homeapp.services;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myershome.homeapp.model.Ingredient;
import com.myershome.homeapp.model.IngredientItem;
import com.myershome.homeapp.services.Constants.Measurements;

public class RecipeParser {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeParser.class);

    private String webpage;
    private Document doc;
    public List<String> ingredients;
    public List<String> instructions;
    public List<String> failedIngredients = new ArrayList<>();
    private Long id = 1L;
    public List<IngredientItem> ingredientObj = new ArrayList();
    public final Map<Measurements, List<String>> measAbv = Map.ofEntries(
            Map.entry(Measurements.TABLESPOON,
                    List.of("TABLESPOON", "Tbsp", "tbsp", "tablespoon", "T", "Tb", "Tablespoon")),
            Map.entry(Measurements.TEASPOON, List.of("TEASPOON", "tsp", "teaspoon", "t", "tspn", "ts", "Teaspoon")),
            Map.entry(Measurements.CUP, List.of("C", "c", "cup", "Cup")),
            Map.entry(Measurements.OUNCE, List.of("Ounce", "Oz", "OZ", "oz")),
            Map.entry(Measurements.POUND, List.of("Pound", "LB", "lb")));

    public RecipeParser(String webpage) {
        this.webpage = webpage;
    }

    public void generate() throws Exception {
        // System.out.println("Generating recipe from " + webpage);
        try {
            doc = Jsoup.connect(webpage).get();
            ingredients = getListByDiv("ingredient");
            for (String i : ingredients) {
                IngredientItem ingItem = parseIngredient(i);
                if (ingItem != null) {
                    ingredientObj.add(ingItem);
                } else {
                    failedIngredients.add(i);
                }
            }
            instructions = getListByDiv("instruction|direction|step");
            if (!failedIngredients.isEmpty()) {
                instructions.add("\n\nFAILED INGREDIENTS:");
                instructions.addAll(failedIngredients);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private List<String> getListByDiv(String className) throws Exception {

        String query = "div[class~=" + className + "]";

        Elements div = doc.select(query);

        Elements list = div.select("li");

        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.eachText();
    }

    private IngredientItem parseIngredient(String value) {
        Pattern amountPattern = Pattern.compile("^([0-9/\\/\\p{No}\\s]+)");

        String amountString;
        String ingredientString;
        Double amountDouble = null;

        Matcher amount = amountPattern.matcher(value);
        if(amount.find()){
            amountString = amount.group(0).strip();

            ingredientString = value.split(amountString)[1].strip();
            try{
                amountDouble = parseAmount(amountString);
            }catch (Exception e) {
                LOG.error("Error parsing: {} Exception {}", value, e.toString());
                return null;
            }

        }else{
            ingredientString = value;
        }
        Measurements meas = parseMeasurements(ingredientString);

        if (meas != null && meas != Measurements.NONE) {
            ingredientString = ingredientString.split(" ", 2)[1];
        }
        Ingredient ing = new Ingredient();
        ing.setIngName(ingredientString);
        ing.setId(id);
        id++;
        return IngredientItem.builder().ingredient(ing)
                .measurement(meas)
                .amount(amountDouble)
                .build();
    }

    private Measurements parseMeasurements(String input) {
        Measurements m = Measurements.NONE;
        String hold = input.split(" ")[0];
        if (hold.endsWith("s")) {
            hold = hold.substring(0, hold.length() - 1);
        }
        for (Map.Entry<Measurements, List<String>> entry : measAbv.entrySet()) {
            if (entry.getValue().contains(hold)) {
                m = entry.getKey();
            }
        }
        LOG.info("Found measurement: " + m);
        return m;
    }

    private Double parseAmount(String valString) {
        Double amount = 0.0;
        List<Constants.FRACTIONS> fracs = Constants.FRACTIONS.fracList;
        
        for (String s : valString.split(" ")) {
            if (s.contains("/")) {
                String[] amountString = s.split("/");
                Double frac = Double.parseDouble(amountString[0]) / Double.parseDouble(amountString[1]);
                amount += frac;
            } else if (fracs.stream().anyMatch(n -> n.hashCode == s.hashCode())) {
                amount += fracs.stream().filter(n -> n.hashCode == s.hashCode()).findFirst().get().decimal;
            } else {
                amount += Double.parseDouble(s);
            }
        }
        return amount;
    }

    public List<IngredientItem> ingredientItemCondense(List<IngredientItem> ingedients) {
        Map<Long, List<IngredientItem>> ingredientItemMap = ingedients.stream()
                .collect(Collectors.groupingBy(i -> i.ingredient.getId()));

        List<IngredientItem> output = new ArrayList();
        for (Long l : ingredientItemMap.keySet()) {
            List<IngredientItem> ingItemList = ingredientItemMap.get(l);

            Collections.sort(ingItemList, (s1, s2) -> {
                return s1.measurement.compareTo(s2.measurement);
            });

            IngredientItem hold = null;
            for (IngredientItem sorted : ingItemList) {
                if (hold == null) {
                    hold = sorted;
                } else if (hold.measurement != null && hold.measurement == sorted.measurement) {
                    hold.amount += sorted.amount;
                } else {
                    output.add(hold);
                    hold = sorted;
                }
            }
            if (hold != null) {
                output.add(hold);
            }
        }
        output.forEach(i -> System.out.println(i));
        return output;

    }
}
