package com.example.assignment2mad;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
/**
 * FragmentDisplayMeals is a Fragment that displays a list of meals in a RecyclerView.
 * It initializes a ViewModel to observe changes in logged meals and updates the
 * RecyclerView accordingly. The fragment also includes a button to navigate back
 * to the menu fragment when clicked.
 */

public class FragmentDisplayMeals extends Fragment {

    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;
    private List<MealEntity> mealList; // Use MealEntity instead of Meal
    private DisplayViewModel displayViewModel; // Declare the DisplayViewModel
    private Button doneButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_meals, container, false);

        // Initialize ViewModel
        displayViewModel = new ViewModelProvider(requireActivity()).get(DisplayViewModel.class);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize meal list
        mealList = new ArrayList<>();
        mealAdapter = new MealAdapter(mealList);
        recyclerView.setAdapter(mealAdapter);

        // Observe logged meals from the ViewModel
        displayViewModel.getLoggedMeals().observe(getViewLifecycleOwner(), meals -> {
            Log.d("FragmentDisplayMeals", "Meals observer triggered");
            Log.d("FragmentDisplayMeals", "Meals updated: " + meals.size());
            mealList.clear();
            mealList.addAll(meals);
            mealAdapter.notifyDataSetChanged();
            Log.d("FragmentDisplayMeals", "Meals updated: " + meals.size());  // Check the size of the meals
        });


        doneButton = view.findViewById(R.id.doneButton);
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
}
