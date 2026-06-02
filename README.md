# Food Calorie Management Application

Android application that enables users to search nutritional information, calculate calories for custom serving sizes, log meals, and store meal images in the cloud.

Built using Java and Android Studio to demonstrate mobile application development, REST API integration, local data persistence, cloud storage, and modern Android architecture patterns.

---

## Technical Highlights

* Integrated the CalorieNinjas REST API to retrieve real-time nutritional information
* Parsed JSON responses and transformed API data for display within the application
* Implemented Room Database (SQLite) for local data persistence
* Built cloud-based image storage using Firebase Storage
* Applied the MVVM architectural pattern to separate presentation, business, and data layers
* Used asynchronous operations for network requests and database interactions to maintain UI responsiveness

---

## Key Features

✅ Search food nutrition data using the CalorieNinjas API

✅ Calculate calories based on custom serving sizes

✅ Log meals with nutritional information

✅ Upload and store meal images using Firebase Storage

✅ Persist meal records locally using Room Database (SQLite)

✅ Display logged meals using RecyclerView

✅ Lifecycle-aware data management using ViewModel and LiveData

---

## Tech Stack

**Languages & Tools**

* Java
* Android Studio
* Git

**Mobile Development**

* Android Fragments
* RecyclerView
* ViewModel
* LiveData

**Data & Storage**

* Room Database (SQLite)
* Firebase Storage

**Integration**

* REST APIs
* JSON Parsing

---

## Engineering Concepts Applied

* Mobile Application Development
* RESTful API Integration
* JSON Data Processing
* Local Database Design and Persistence
* Cloud Storage Integration
* MVVM Architecture
* Repository Design Pattern
* Asynchronous Programming
* Android Lifecycle Management
* RecyclerView-Based Data Presentation

---

## Architecture

```text
Fragment (UI)
      ↓
ViewModel
      ↓
Repository
      ↓
Room Database
```

Meal information is stored locally using Room Database, while meal images are uploaded to Firebase Storage. Firebase download URLs are stored alongside meal records and retrieved when displaying logged meals.

---

## Screenshots

### Main Menu

<img width="982" height="581" alt="image" src="https://github.com/user-attachments/assets/eb8bafc2-48d4-4c1e-a4cc-09ea53b5cff1" />

### Nutrition Search Screen

<img width="1011" height="617" alt="Nutrition Search" src="https://github.com/user-attachments/assets/108f5dd7-c340-4831-85b3-1b13122b860e" />

<img width="1017" height="617" alt="Nutrition Results" src="https://github.com/user-attachments/assets/68d9f23f-2d65-45c6-bb55-4161e6a833db" />

### Gallery

<img width="1062" height="513" alt="Gallery" src="https://github.com/user-attachments/assets/8fca4ce8-039f-45e0-98c3-45f7559c34c6" />

### Logged Meals

<img width="1010" height="565" alt="Display All Logged Meals" src="https://github.com/user-attachments/assets/e58dfd09-ba74-4d40-88da-53a1b9072cab" />

### Daily Calorie Goal

<img width="1027" height="605" alt="Goal Calories" src="https://github.com/user-attachments/assets/069fbe2b-66a4-4891-87dc-c7687e3b13c5" />

### Firebase Cloud Storage

<img width="1716" height="915" alt="Firebase Storage" src="https://github.com/user-attachments/assets/7f3a380d-81be-4c3f-a952-7407567b1dfb" />



