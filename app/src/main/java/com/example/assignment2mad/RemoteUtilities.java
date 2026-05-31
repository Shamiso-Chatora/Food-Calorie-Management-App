package com.example.assignment2mad;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * RemoteUtilities provides utility methods for managing network operations,
 * including checking network connectivity, opening HTTP connections,
 * verifying connection success, and reading response data from an endpoint.
 * It follows the Singleton pattern to ensure a single instance is used throughout the application.
 */


public class RemoteUtilities {

    private static RemoteUtilities instance;
    private Activity activity;

    private RemoteUtilities(Activity activity) {
        this.activity = activity;
    }

    public static synchronized RemoteUtilities getInstance(Activity activity) {
        if (instance == null) {
            instance = new RemoteUtilities(activity);
        }
        return instance;
    }

    // Method to check network connectivity
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Method to open a connection
    public HttpURLConnection openConnection(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            return connection;
        } catch (IOException e) {
            Log.e("RemoteUtilities", "Error opening connection: " + e.getMessage());
            return null;
        }
    }

    // Method to check if the connection is successful
    public boolean isConnectionOkay(HttpURLConnection connection) {
        try {
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            Log.e("RemoteUtilities", "Error checking connection: " + e.getMessage());
            return false;
        }
    }

    // Method to get the response string from the connection
    public String getResponseString(HttpURLConnection connection) {
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.e("RemoteUtilities", "Error reading response: " + e.getMessage());
        }
        return response.toString();
    }
}
