package com.example.assignment2mad;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * FragmentChangeDailyGoal allows the user to input a new daily calorie goal and save it.
 * It interacts with the MyViewModel to update the goal value across the app and provides
 * feedback to the user through Toast messages. The fragment also allows the user to navigate
 * back to the FragmentDailyGoal after making changes.
 */
public class FragmentChangeDailyGoal extends Fragment {

    private MyViewModel viewModel;
    private EditText newGoalInput;
    private Button saveButton;
    private Button doneButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_daily_goal, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        newGoalInput = view.findViewById(R.id.newGoalInput);
        saveButton = view.findViewById(R.id.saveButton);
        doneButton = view.findViewById(R.id.doneButton);

        // Save button to update the goal
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGoal = newGoalInput.getText().toString();
                if (!newGoal.isEmpty()) {

                    Toast.makeText(getActivity(), "Goal updated to: " + newGoal + " kcal", Toast.LENGTH_SHORT).show();
                    newGoalInput.setText("");
                    viewModel.setDailyGoal(newGoal);

                } else {
                    Toast.makeText(getActivity(), "Please enter a valid goal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentDailyGoal())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
