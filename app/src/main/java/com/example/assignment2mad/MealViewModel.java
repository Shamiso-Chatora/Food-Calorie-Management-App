package com.example.assignment2mad;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * MealViewModel manages UI-related data in a lifecycle-aware way.
 * It uses MutableLiveData to hold the API response, allowing the UI
 * to observe changes and update automatically when new data is available.
 *
 * The API resopnse is stored here for later use in the FragmentLogMeal that searches for last thread
 */

public class MealViewModel extends ViewModel {

    private MutableLiveData<String> apiResponse;

    public MealViewModel() {
        apiResponse = new MutableLiveData<>();

    }

    public MutableLiveData<String> getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(String response) {
        apiResponse.postValue(response);  // Use postValue to update from background thread
    }


}

