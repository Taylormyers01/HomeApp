package com.myershome.homeapp.services;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Constants implements Serializable {

    public enum Days {
        SUNDAY("Sunday"),
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday"),
        SATURDAY("Saturday");

        public static final Days[] dayArray = values();
        public final String value;

        private Days(String value) {
            this.value = value;
        }

    }

    public enum TeddyEventType {

        BOTTLE("Bottle"),
        DIAPER("Diaper"),
        NAP("Nap"),
        BATH("Bath");

        public final String value;
        public static final TeddyEventType[] eventTypes = values();

        private TeddyEventType(String value) {
            this.value = value;
        }
    }

    public enum Measurements {

        TABLESPOON("Tablespoon"),
        TEASPOON("Teaspoon"),
        CUP("Cup"),
        OUNCE("Ounce"),
        POUND("Pound"),
        NONE("");

        public final String value;

        Measurements(String value) {
            this.value = value;
        }

    }

    public enum CARDTYPES{

        ICON("ICON"),
        CONTENT("CONTENT");

        public final String value;
        CARDTYPES(String value){
            this.value = value;
        }
    }

    public enum FRACTIONS {

        ONE_FOURTH("¼",188, .25),
        ONE_HALF("½", 189, .5),
        ONE_THIRD("⅓", 8531, .33),
        THREE_QUARTER("¾",190, .75),
        TWO_THIRDS("⅔", 8532, .66),
        ONE_EIGHT("⅛", 8539, .125),
        THREE_EIGHTS("⅜", 8540, .375),
        FIVE_EIGHTS("⅝", 8541, .625),
        SEVEN_EIGHTS("⅞", 8542, .875);

        public final String stringValue;
        public final int hashCode;
        public final double decimal;
        public static final FRACTIONS[] fractionArray = values();
        public static final List<FRACTIONS> fracList = List.of(values());

        FRACTIONS(String stringValue, int hashCode, double decimal) {
            this.stringValue = stringValue;
            this.hashCode = hashCode;
            this.decimal = decimal;
        }

    }


    public static class APIResources {
        private final static String baseURL = "/api";
        public final static String ingredientURL = baseURL + "/ingredient";
        public final static String mealURL = baseURL + "/meal";
        public final static String taskURL = baseURL + "/task";
        public final static String userURL = baseURL + "/user";
        public final static String ingredientItem = baseURL + "/ingredientItem";
        public final static String teddyEventUrl = baseURL + "/teddy-event";
    }

    public static String[] bottleAmount = { "0.5 oz.", "1 oz.", "1.5 oz.", "2 oz.", "2.5 oz.", "3 oz.", "3.5 oz.",
            "4 oz.", "4.5 oz.", "5 oz.",
            "5.5 oz.", "6 oz.", "6.5 oz.", "7 oz.", "7.5 oz.", "8 oz." };
    public static String[] diaperAmount = { "Pee", "Poop", "Pee & Poop" };

}
