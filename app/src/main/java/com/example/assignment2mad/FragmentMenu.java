package com.example.assignment2mad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;



/*
 * FragmentMenu displays the main menu options for logging meals,
 * setting daily goals, and viewing meals. .
 */

public class FragmentMenu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        // Initialize buttons
        Button logMealButton = view.findViewById(R.id.logMealButton);
        Button dailyGoalButton = view.findViewById(R.id.dailyGoalButton);
        Button displayMealButton = view.findViewById(R.id.displayMealButton);


        logMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with FragmentLogMeal
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentLogMeal())
                        .addToBackStack(null)
                        .commit();
            }
        });

        dailyGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with FragmentDailyGoal
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentDailyGoal())
                        .addToBackStack(null)
                        .commit();
            }
        });

        displayMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with FragmentDailyGoal
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentDisplayMeals())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
