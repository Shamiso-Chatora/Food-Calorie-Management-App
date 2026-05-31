package com.example.assignment2mad;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MealDAO {

    @Insert
    void insert(MealEntity meal);

    @Query("SELECT * FROM meal_table")
    LiveData<List<MealEntity>> getAllMeals();
}
