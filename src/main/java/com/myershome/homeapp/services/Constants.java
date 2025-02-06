package com.myershome.homeapp.services;

import java.io.Serializable;

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
