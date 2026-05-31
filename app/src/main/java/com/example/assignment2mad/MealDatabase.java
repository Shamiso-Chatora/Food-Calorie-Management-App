package com.example.assignment2mad;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * MealDatabase is a Room Database that manages the MealEntity  *
 * Key Features:
 * - Entities: Defines MealEntity as a table in the database.
 * - Thread : Uses synchronized blocks to create a thread-safe singleton instance.
 */
@Database(entities = {MealEntity.class}, version = 1)
public abstract class MealDatabase extends RoomDatabase {

    private static volatile MealDatabase INSTANCE;

    public abstract MealDAO mealDao();

    public static MealDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MealDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MealDatabase.class, "meal_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
