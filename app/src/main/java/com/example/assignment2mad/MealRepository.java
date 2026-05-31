package com.example.assignment2mad;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MealRepository acts as a mediator between the MealDAO and the UI.
 * It provides methods to access meal data from the database in a
 * background thread while exposing the data as LiveData for
 * observation by the UI. The repository supports inserting new
 * meals and retrieving a list of all meals.
 *
 * The ExecutorService is initialized with a fixed thread pool of size 2, meaning that it can
 *  * run up to two tasks concurrently. When the insert(MealEntity meal) method is called,
 *  * it submits a task to the executor, which executes the insertion of the meal into the database
 *  * in a background thread. This approach helps avoid blocking the UI and improves overall app
 *  * performance, particularly when dealing with potentially slow database operations.
 *
 */

public class MealRepository {
    private final MealDAO mealDao;
    private final LiveData<List<MealEntity>> allMeals;
    private final ExecutorService executorService;

    public MealRepository(Application application) {
        MealDatabase db = MealDatabase.getDatabase(application);
        mealDao = db.mealDao();
        allMeals = mealDao.getAllMeals();
        //use of threads and asynchrony
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<MealEntity>> getAllMeals() {
        return allMeals;
    }

    public void insert(MealEntity meal) {
        executorService.execute(() -> mealDao.insert(meal));
    }
}
