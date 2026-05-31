package com.example.assignment2mad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;


/**
 * MyViewModel manages the daily goal and total calories using MutableLiveData.
 * It allows other components to observe changes to the daily goal (String) and total calories (Double).
 * Methods are provided to get and set these values, ensuring that any updates are reflected in the UI.
 */

public class MyViewModel extends ViewModel {

    private final MutableLiveData<String> dailyGoal = new MutableLiveData<>();

    private final MutableLiveData<Double> totalCalories = new MutableLiveData<>();


    public LiveData<Double> getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Double value) {
        totalCalories.setValue(value);
    }

    public LiveData<String> getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(String value) {
        dailyGoal.setValue(value);
    }







}
