# Food Calorie Management Application

## Overview

Food Calorie Management Application is an Android application developed using Java and Android Studio. The application allows users to search nutritional information for foods, calculate calories based on custom portion sizes, log meals, upload meal images, and view previously logged meals.

The project demonstrates Android development concepts including REST API integration, local database storage, cloud storage, asynchronous programming, and the MVVM architectural pattern.

---

## Features

* Search nutritional information using the CalorieNinjas API
* Calculate calories for custom portion sizes
* Log meals with nutritional information
* Upload and store meal images using Firebase Storage
* Store meal records locally using Room Database
* Display previously logged meals in a RecyclerView
* Lifecycle-aware data management using ViewModel and LiveData

---

## Technologies Used

* Java
* Android Studio
* Room Database (SQLite)
* Firebase Storage
* REST APIs
* JSON Parsing
* RecyclerView
* ViewModel
* LiveData
* Git

---

## Architecture

The application follows an MVVM-style architecture:

Fragment → ViewModel → Repository → Room Database

Meal information is stored locally using Room Database, while meal images are uploaded to Firebase Storage. The Firebase image URL is stored alongside the meal record and used when displaying logged meals.

---

## API Integration

The application integrates with the CalorieNinjas REST API to retrieve nutritional information for food items.

Users enter a food name and portion size, and the application retrieves nutritional data including:

* Calories
* Protein
* Fat
* Carbohydrates

The application then calculates adjusted calorie values based on the user’s specified portion size.

---

## Cloud Storage

Meal images are uploaded to Firebase Storage.

After a successful upload:

1. Firebase returns a download URL.
2. The URL is stored in the Room database.
3. Logged meals display the associated image using the stored URL.

---

## What I Learned

Through this project, I gained practical experience with:

* Android application development
* REST API integration
* JSON parsing
* Firebase cloud storage
* Room Database and SQLite
* MVVM architecture
* RecyclerView implementation
* Asynchronous programming and background threads
* Debugging cloud service integrations

---

## Screenshots

* Main Menu
  <img width="982" height="581" alt="image" src="https://github.com/user-attachments/assets/eb8bafc2-48d4-4c1e-a4cc-09ea53b5cff1" />

* Nutrition Search Screen
  <img width="1011" height="617" alt="Log Meal" src="https://github.com/user-attachments/assets/108f5dd7-c340-4831-85b3-1b13122b860e" />
  <img width="1017" height="617" alt="Log Meal Snack" src="https://github.com/user-attachments/assets/68d9f23f-2d65-45c6-bb55-4161e6a833db" />

* Gallery
  <img width="1062" height="513" alt="Gallery" src="https://github.com/user-attachments/assets/8fca4ce8-039f-45e0-98c3-45f7559c34c6" />


* Display Meals Screen
  <img width="1010" height="565" alt="Display All Logged Meals" src="https://github.com/user-attachments/assets/e58dfd09-ba74-4d40-88da-53a1b9072cab" />

* Calorie Goal Screen
  <img width="1027" height="605" alt="Goal Calories" src="https://github.com/user-attachments/assets/069fbe2b-66a4-4891-87dc-c7687e3b13c5" />

