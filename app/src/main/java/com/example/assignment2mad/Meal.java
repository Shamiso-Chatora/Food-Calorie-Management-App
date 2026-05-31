package com.example.assignment2mad;

public class Meal {
    private String name;
    private double calories;
    private String type;
    private String day; // New attribute for the day

    public Meal(String name, double calories, String type, String day) {
        this.name = name;
        this.calories = calories;
        this.type = type;
        this.day = day; // Initialize the day attribute
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public String getType() {
        return type;
    }

    public String getDay() {
        return day; // Getter for the day
    }

    @Override
    public String toString() {
        return String.format("%s - %.1f kcal (%s) [Day: %s]", name, calories, type, day);
    }
}
