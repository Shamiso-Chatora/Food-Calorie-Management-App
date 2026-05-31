package com.example.assignment2mad;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * MealEntity represents a meal in the database with properties such as
 * name, calories, type, day of the week, and a URL for a photo.
 * It is annotated with @Entity to define it as a Room database entity,
 * and includes methods for accessing and modifying its fields.
 */

@Entity(tableName = "meal_table")
public class MealEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double calories;
    private String type;
    private String day;
    private String photoUrl;  // Field for the photo URL

    public MealEntity(String name, double calories, String type, String day, String photoUrl) {
        this.name = name;
        this.calories = calories;
        this.type = type;
        this.day = day;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return day;
    }


    public String getPhoto() {
        return photoUrl;  // Getter for the photo URL
    }


    public String getPhotoUrl() {
        return photoUrl;  // Getter for the photo URL
    }

    public void setPhoto(String photoUrl) {
        this.photoUrl = photoUrl;  // Setter for the photo URL
    }


    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;  // Setter for the photo URL
    }

    @Override
    public String toString() {
        return String.format("%s - %.1f kcal (%s) [Day: %s]", name, calories, type, day);
    }
}
