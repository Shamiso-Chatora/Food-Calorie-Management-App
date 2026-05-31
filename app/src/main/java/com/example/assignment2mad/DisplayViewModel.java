package com.example.assignment2mad;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
/**
 * DisplayViewModel interacts with the MealRepository
 * to retrieve and manage a list of logged meals. It provides a LiveData object
 * that the UI can observe for updates, allowing for responsive UI changes
 * when the meal data changes.
 */

public class DisplayViewModel extends AndroidViewModel {
    private final MealRepository mealRepository;
    private final LiveData<List<MealEntity>> loggedMeals;

    public DisplayViewModel(Application application) {
        super(application);
        mealRepository = new MealRepository(application);
        loggedMeals = mealRepository.getAllMeals(); // Fetching all meals
    }

    public LiveData<List<MealEntity>> getLoggedMeals() {
        return loggedMeals;
    }
}
