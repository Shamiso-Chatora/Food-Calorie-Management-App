package com.example.assignment2mad;

import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
/**
 * APISearchThread is a custom thread that handles API requests to retrieve
 * nutritional information for a specified meal from the CalorieNinja API.
 * It constructs the request URL, manages the HTTP connection, processes
 * the JSON response, and calculates adjusted calorie values based on the
 * user's specified portion size. The resulting nutritional data is
 * posted back to the MealViewModel for updating the UI.
 */

public class APISearchThread extends Thread {
    private String mealName;
    private double calories;
    private double servingSize;
    private double fatTotal;
    private double protein;
    private double carbohydrates;
    private double adjustedCalories;
    private String searchKey;
    private String portionSize;
    private String baseUrl;
    private RemoteUtilities remoteUtilities;
    private MealViewModel viewModel;
    private static final String API_KEY = "bXid+WPiIBbCP2KWLVazcQ==4bgfetqoYr2mH8xj"; // Your API key here


    // Constructor for APISearchThread that initializes the search parameters, base URL, and dependencies for the API request.
    public APISearchThread(String searchKey, String portionSize, Fragment fragment, MealViewModel viewModel) {
        this.searchKey = searchKey;
        this.portionSize = portionSize;
        this.baseUrl = "https://api.calorieninjas.com/v1/nutrition"; // Base URL
        remoteUtilities = RemoteUtilities.getInstance(fragment.requireActivity());
        this.viewModel = viewModel;
    }


    // Executes the API search in a separate thread. It opens a connection, sets the API key, checks the connection status,
   // retrieves the response, and parses it.
    @Override
    public void run() {
        String endpoint = getSearchEndpoint();
        HttpURLConnection connection = remoteUtilities.openConnection(endpoint);
        if (connection != null) {
            connection.setRequestProperty("X-Api-Key", API_KEY);
            if (remoteUtilities.isConnectionOkay(connection)) {
                String response = remoteUtilities.getResponseString(connection);
                connection.disconnect();
                try {
                    parseResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Constructs the API endpoint URL by appending the search query and a default portion size of 100 grams.
    private String getSearchEndpoint() {
        // Build the endpoint with searchKey and a default portion size of 100
        Uri.Builder url = Uri.parse(this.baseUrl).buildUpon();
        url.appendQueryParameter("query", this.searchKey);
        url.appendQueryParameter("portion_size", "100"); // Always use 100g
        Log.d("APISearchThread", "Endpoint URL: " + url.build().toString());
        return url.build().toString();
    }


    // Parses the JSON response from the API to extract nutritional information for the meal.
// It calculates adjusted calories based on the specified portion size and updates the LiveData in the ViewModel.
    private void parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray items = jsonResponse.getJSONArray("items");

        if (items.length() > 0) {
            JSONObject item = items.getJSONObject(0); // Get the first item
            mealName = item.getString("name"); // Save the meal name
            calories = item.getDouble("calories"); // Calories for 100g
            servingSize = item.getDouble("serving_size_g");
            fatTotal = item.getDouble("fat_total_g");
            protein = item.getDouble("protein_g");
            carbohydrates = item.getDouble("carbohydrates_total_g");

            // Convert the user input for portion size to a double
            double portionSizeValue = Double.parseDouble(portionSize);
            adjustedCalories = (calories / servingSize) * portionSizeValue; // Calculate adjusted calories

            // Format the result string with adjusted values
            String result = String.format("Name: %s\nCalories (100g): %.1f\nTotal Fat: %.1f g\nProtein: %.1f g\nCarbohydrates: %.1f g\nAdjusted Calories for %.1f g: %.1f",
                    mealName, calories, fatTotal, protein, carbohydrates, portionSizeValue, adjustedCalories);

            // Use postValue to update LiveData on the main thread
            viewModel.getApiResponse().postValue(result);
        } else {
            viewModel.getApiResponse().postValue("No nutritional information found.");
        }
    }

    // getter methods for the nutritional values
    public String getMealName() {
        return mealName;
    }

    public double getCalories() {
        return calories;
    }

    public double getFatTotal() {
        return fatTotal;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getAdjustedCalories() { // Add getter for adjustedCalories
        return adjustedCalories;
    }
}
