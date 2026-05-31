package com.example.assignment2mad;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {
    private final MealRepository mealRepository;
    private final LiveData<List<MealEntity>> allMeals;

    public DatabaseViewModel(Application application) {
        super(application);
        mealRepository = new MealRepository(application);
        allMeals = mealRepository.getAllMeals();
    }

    public LiveData<List<MealEntity>> getAllMeals() {
        return allMeals;
    }

    public void insert(MealEntity meal) {
        mealRepository.insert(meal);
    }
}
