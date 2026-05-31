package com.example.assignment2mad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


/**
 * MealAdapter is a RecyclerView.Adapter that binds MealEntity data to the RecyclerView for displaying
 * a list of meals. Each meal entry includes the meal name, calorie count, type, day of the week,
 * and an associated image. The adapter utilizes Glide for efficient image loading and caching,
 * displaying a placeholder image while the actual meal image loads and a default image if the load fails.
 *
 * The adapter consists of:
 * - MealViewHolder: Holds views for meal name, calories, type, day, and image.
 * - onCreateViewHolder: Inflates the layout for each meal item.
 * - onBindViewHolder: Binds data from MealEntity to the views.
 * - getItemCount: Returns the total number of meals in the list.
 */

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final List<MealEntity> meals;

    public MealAdapter(List<MealEntity> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealEntity meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName;
        private final TextView mealCalories;
        private final TextView mealType;
        private final TextView mealDay;
        private final ImageView mealImageView; // ImageView for photo

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealNameTextView);
            mealCalories = itemView.findViewById(R.id.caloriesTextView);
            mealType = itemView.findViewById(R.id.mealTypeTextView);
            mealDay = itemView.findViewById(R.id.selectedDayTextView);
            mealImageView = itemView.findViewById(R.id.mealImageView);
        }

        public void bind(MealEntity meal) {
            mealName.setText(meal.getName());
            mealCalories.setText(String.format("%.1f kcal", meal.getCalories()));
            mealType.setText(meal.getType());
            mealDay.setText(meal.getDay());

            // Load image using Glide
            String photoUrl = meal.getPhoto();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(photoUrl)
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image) // Set a default image if loading fails
                        .into(mealImageView);
            } else {
                mealImageView.setImageResource(R.drawable.default_image);
            }
        }
    }
}
