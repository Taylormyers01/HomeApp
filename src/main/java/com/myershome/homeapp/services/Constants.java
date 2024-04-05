package com.myershome.homeapp.services;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.val;

import java.io.Serializable;

public class Constants implements Serializable {

    public enum Days{
        SUNDAY("sunday"),
        MONDAY("monday"),
        TUESDAY("tuesday"),
        WEDNESDAY("wednesday"),
        THURSDAY("thursday"),
        FRIDAY("friday"),
        SATURDAY("saturday");
        private static final Days[] dayArray = values();
        public final String value;
        private Days(String value){
            this.value = value;
        }

    }
    public static class APIResources {
        private final static String baseURL = "/api";
        public final static String ingredientURL = baseURL + "/ingredient";
        public final static String mealURL = baseURL + "/meal";
        public final static String taskURL = baseURL + "/task";
        public final static String userURL = baseURL + "/user";
    }
}
