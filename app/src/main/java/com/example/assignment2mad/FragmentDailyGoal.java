package com.example.assignment2mad;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/*
 * FragmentDailyGoal manages the display and comparison of the user's daily calorie goal
 * and the total calories consumed. It observes the ViewModel for changes in the daily
 * goal and total calories, updating the UI accordingly. The fragment also provides
 * buttons for changing the goal and navigating back to the menu.
 */
public class FragmentDailyGoal extends Fragment {

    private MyViewModel viewModel; 
    private TextView currentGoalDisplay;
    private TextView totalCaloriesDisplay;
    private TextView comparisonDisplay;
    private Button changeGoalButton;
    private Button doneButton;

    private static final int DEFAULT_DAILY_GOAL = 2000; // Default daily goal in kcal

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_goal, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        currentGoalDisplay = view.findViewById(R.id.currentGoalDisplay);
        totalCaloriesDisplay = view.findViewById(R.id.totalCaloriesDisplay);
        comparisonDisplay = view.findViewById(R.id.comparisonDisplay);
        changeGoalButton = view.findViewById(R.id.changeGoalButton);
        doneButton = view.findViewById(R.id.doneButton);

        // Observe the daily goal
        viewModel.getDailyGoal().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String dailyGoal) {
                if (dailyGoal == null || dailyGoal.isEmpty()) {
                    dailyGoal = String.valueOf(DEFAULT_DAILY_GOAL); // Default value
                }
                currentGoalDisplay.setText("Current Goal: " + dailyGoal + " kcal");
                updateComparisonDisplay(dailyGoal); // Update the comparison display
            }
        });

        // Observe the total calories
        viewModel.getTotalCalories().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalCalories) {
                totalCaloriesDisplay.setText("Total Calories Eaten: " + totalCalories + " kcal");
                // After updating total calories, get the current daily goal and update the comparison display
                viewModel.getDailyGoal().getValue();
                String dailyGoal = viewModel.getDailyGoal().getValue();
                if (dailyGoal == null || dailyGoal.isEmpty()) {
                    dailyGoal = String.valueOf(DEFAULT_DAILY_GOAL);
                }
                updateComparisonDisplay(dailyGoal);
            }
        });

        // Change goal button listener
        changeGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentChangeDailyGoal())
                        .addToBackStack(null)
                        .commit();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentMenu())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    // Method to update the comparison display
    private void updateComparisonDisplay(String dailyGoalStr) {
        double dailyGoal = DEFAULT_DAILY_GOAL; // Fallback to default value
        try {
            dailyGoal = Double.parseDouble(dailyGoalStr);
        } catch (NumberFormatException e) {

        }

        // Retrieve the total calories and perform the comparison
        Double totalCalories = viewModel.getTotalCalories().getValue();
        if (totalCalories == null) {
            totalCalories = 0.0; // Default to 0 if null
        }

        if (totalCalories < dailyGoal) {
            comparisonDisplay.setText("You're under your goal by " + (dailyGoal - totalCalories) + " kcal.");
        } else if (totalCalories > dailyGoal) {
            comparisonDisplay.setText("You've exceeded your goal by " + (totalCalories - dailyGoal) + " kcal.");
        } else {
            comparisonDisplay.setText("You've met your goal!");
        }
    }
}
