package com.myershome.homeapp.services;

import java.io.Serializable;

public class Constants implements Serializable {

    public enum Days{
        SUNDAY("Sunday"),
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday"),
        SATURDAY("Saturday");
        public static final Days[] dayArray = values();
        public final String value;
        private Days(String value){
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
        private TeddyEventType(String value){this.value = value;}
    }
    public static class APIResources {
        private final static String baseURL = "/api";
        public final static String ingredientURL = baseURL + "/ingredient";
        public final static String mealURL = baseURL + "/meal";
        public final static String taskURL = baseURL + "/task";
        public final static String userURL = baseURL + "/user";
        public final static String teddyEventUrl = baseURL + "/teddy-event";
    }
}
