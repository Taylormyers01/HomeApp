package com.myershome.homeapp.services;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

public class Constants implements Serializable {
    public enum Days{
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }
    public static class APIResources {
        private final static String baseURL = "/api";
        public final static String ingredientURL = baseURL + "/ingredient";
        public final static String mealURL = baseURL + "/meal";
        public final static String taskURL = baseURL + "/task";
        public final static String userURL = baseURL + "/user";
    }
}
