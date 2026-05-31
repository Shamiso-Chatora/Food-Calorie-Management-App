package com.example.assignment2mad;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.FirebaseApp;



import androidx.activity.result.PickVisualMediaRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * FragmentLogMeal is a Fragment that allows users to log meals by searching for nutritional information
 * via an API, uploading an optional meal image, and saving the meal data to Firestore and Room database.
 * It includes user inputs for meal name, portion size, meal type, and day, along with functionality to
 * pick an image from the gallery. The Fragment observes ViewModel data for nutritional responses and
 * manages UI elements such as progress bars and input fields based on user interactions.
 * Users can either fetch nutritional info from the API or enter it manually before logging the meal.
 */

public class FragmentLogMeal extends Fragment {



    // Initialize Firebase
    FirebaseFirestore firestore;
    ImageView imageView;
    private MealViewModel mealViewModel;
    private Button searchNutritionalInfoButton;
    private Button logMealButton;
    private EditText mealNameInput;
    private EditText portionSizeInput; // Portion size input
    private Spinner mealTypeSpinner;
    private Spinner daySpinner; // Spinner for selecting the day
    private TextView apiResultText;
    private ProgressBar progressBar;
    private DisplayViewModel displayViewModel;
    private DatabaseViewModel databaseViewModel;
    private MyViewModel viewModel;
    private ImageViewModel imageViewModel;
    private EditText caloriesInput;
    private EditText fatsInput;
    private EditText proteinsInput;
    private EditText carbohydratesInput;
    private int found =0;
    private double adjustedCalories;
    private Button doneButton;
    // Declare the loggedMeals list
    private List<Meal> loggedMeals;
    private APISearchThread lastSearchThread;
    private boolean isImageSelected = false;
    private Bitmap selectedImageBitmap;


    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {

        @Override

        public void onActivityResult(Uri uri) {
            if (uri == null) {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                isImageSelected = false;
            } else {
                try {
                    // Convert the URI to a Bitmap
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    imageView.setImageBitmap(selectedImageBitmap);
                    imageView.setVisibility(View.VISIBLE);
                    isImageSelected = true; // Set the flag to true
                    // Set the image in the ViewModel
                    imageViewModel.setSelectedImage(selectedImageBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }


    });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_meal, container, false);


        FirebaseApp.initializeApp(getContext());
        firestore =FirebaseFirestore.getInstance();


        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        displayViewModel = new ViewModelProvider(this).get(DisplayViewModel.class);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        // Initialize loggedMeals list
        loggedMeals = new ArrayList<>();

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        imageView = view.findViewById(R.id.photoImageView);
        MaterialButton pickImage = view.findViewById(R.id.pickImageButton);
        mealNameInput = view.findViewById(R.id.mealNameInput);
        portionSizeInput = view.findViewById(R.id.portionSizeInput); // Initialize portion size input
        mealTypeSpinner = view.findViewById(R.id.mealTypeSpinner);
        apiResultText = view.findViewById(R.id.apiResultText);
        searchNutritionalInfoButton = view.findViewById(R.id.searchNutritionalInfoButton);
        logMealButton = view.findViewById(R.id.logMealButton);
        progressBar = view.findViewById(R.id.progressBarId);
        caloriesInput = view.findViewById(R.id.caloriesInput);
        fatsInput = view.findViewById(R.id.fatsInput);
        proteinsInput = view.findViewById(R.id.proteinsInput);
        carbohydratesInput = view.findViewById(R.id.carbohydratesInput);
        daySpinner = view.findViewById(R.id.daySpinner);

        doneButton = view.findViewById(R.id.doneButton);

        // Set initial visibility
        apiResultText.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        // Populate the Spinners
        ArrayAdapter<CharSequence> mealAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.meal_types, android.R.layout.simple_spinner_item);
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(mealAdapter);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.days_array, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Observe the apiResponse LiveData
        mealViewModel.getApiResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String response) {
                progressBar.setVisibility(View.GONE);
                apiResultText.setVisibility(View.VISIBLE);


                if (response.contains("No nutritional information found.")) {
                    apiResultText.setText("Nutritional information not found. Please enter manually.");
                    Toast.makeText(getActivity(), "Nutritional information not found.", Toast.LENGTH_LONG).show();
                    // Show manual input fields
                    showManualInputFields();
                    found = 0 ;

                } else {
                    //apiResultText.setText("Nutritionalstillhere Information: " + response);
                    apiResultText.setText("Nutritional Information:\n\n" + response);
                    Toast.makeText(getActivity(), "Nutritional Info Retrieved", Toast.LENGTH_LONG).show();
                    found = 1;
                }
            }
        });


        imageViewModel.getSelectedImage().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE); // Hide the ImageView if no image is set
                }
            }
        });


        // Set onClickListener for the button to launch the image picker
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
        // Automatically copy images from drawable to the public gallery on app startup
        insertImageIntoGallery(R.drawable.image1, "image1.png");
        insertImageIntoGallery(R.drawable.image2, "image2.png");
        insertImageIntoGallery(R.drawable.image3, "image3.png");
        insertImageIntoGallery(R.drawable.image4, "image4.png");
        insertImageIntoGallery(R.drawable.image5, "image5.png");
        insertImageIntoGallery(R.drawable.image6, "image6.png");
        insertImageIntoGallery(R.drawable.image7, "image7.png");


        searchNutritionalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mealName = mealNameInput.getText().toString();
                String portionSize = portionSizeInput.getText().toString(); // Get portion size input

                String mealType = mealTypeSpinner.getSelectedItem().toString();
                int selectedMealTypePosition = mealTypeSpinner.getSelectedItemPosition(); // Get the position of the selected meal type

                // expression to check if portion size is a valid number
                String numberPattern = "^[0-9]*\\.?[0-9]+$"; // This pattern allows only positive integers
                boolean isPortionSizeValid = portionSize.matches(numberPattern); // Validate the portion size

                // Check if meal name, portion size, and meal type are provided
                if (!mealName.isEmpty() && isPortionSizeValid && !mealType.equals("Enter meal type")) {
                    progressBar.setVisibility(View.VISIBLE);
                    // Pass the meal name and portion size to the APISearchThread
                    lastSearchThread = new APISearchThread(mealName, portionSize, FragmentLogMeal.this, mealViewModel);
                    lastSearchThread.start();
                } else {
                    StringBuilder message = new StringBuilder("Please enter a meal name , a valid portion size and a meal type. Portion sizes must be rounded to an integer value");
                    if (!isPortionSizeValid) {
                        message.append(" Portion size must be a positive integer.");
                    }
                    if (selectedMealTypePosition < 0) {
                        message.append(" Please select a valid meal type.");
                    }
                    Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        logMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portionSize = portionSizeInput.getText().toString();
                String mealType = mealTypeSpinner.getSelectedItem().toString();
                String selectedDay = daySpinner.getSelectedItem().toString();
              if(found == 1)
              {
                  if (lastSearchThread != null) {
                      String mealName = lastSearchThread.getMealName();
                      if (!mealName.isEmpty() && !portionSize.isEmpty()) {
                          double adjustedCalories = lastSearchThread.getAdjustedCalories();
                          byte[] mealPhoto;

                          // Check if an image is selected
                          if (isImageSelected) {
                              // Convert the selected image to byte array
                              mealPhoto = getImageAsByteArray();
                              if (mealPhoto == null) {
                                  Toast.makeText(getActivity(), "Failed to convert image. Please try again.", Toast.LENGTH_SHORT).show();
                                  return; // Exit early if there's a problem
                              }
                          } else {
                              // Use the default image if no image was selected
                              mealPhoto = getDefaultImageAsByteArray();
                          }

                          // Create MealEntity without photo URL for now (we'll set it after upload)
                          MealEntity mealEntity = new MealEntity(mealName, adjustedCalories, mealType, selectedDay, null);

                          // Show progress bar
                          progressBar.setVisibility(View.VISIBLE);



                          // Upload image to Firebase Storage and insert meal into Room
                          uploadImageToFirestore(mealPhoto, mealEntity); // mealEntity will be updated with the photo URL

                          // Update total calories count in ViewModel
                          double totalCalories = viewModel.getTotalCalories().getValue() != null ? viewModel.getTotalCalories().getValue() : 0;
                          viewModel.setTotalCalories(totalCalories + adjustedCalories);

                          Toast.makeText(getActivity(), "Meal Logged: " + adjustedCalories + " kcal", Toast.LENGTH_SHORT).show();

                          // Clear input and result fields after logging the meal
                          clearFields();
                      } else {
                          Toast.makeText(getActivity(), "Nutritional information not available", Toast.LENGTH_SHORT).show();

                      }
                  } else {
                      Toast.makeText(getActivity(), "Please perform a search before logging a meal", Toast.LENGTH_SHORT).show();
                  }
              }
              else
              {
                  String mealName = mealNameInput.getText().toString();
                  String enteredCalories = caloriesInput.getText().toString();
                  String enteredFats = fatsInput.getText().toString();
                  String enteredProteins = proteinsInput.getText().toString();
                  String enteredCarbohydrates = carbohydratesInput.getText().toString();

                  // Validate that calories, fats, proteins, and carbohydrates are numeric values
                  double newCalories, newFats, newProteins, newCarbohydrates;
                  try {
                      newCalories = Double.parseDouble(enteredCalories);
                      newFats = Double.parseDouble(enteredFats);
                      newProteins = Double.parseDouble(enteredProteins);
                      newCarbohydrates = Double.parseDouble(enteredCarbohydrates);


                      // Check if any values are negative
                      if (newCalories < 0 || newFats < 0 || newProteins < 0 || newCarbohydrates < 0) {
                          Toast.makeText(getActivity(), "Values for calories, fats, proteins, and carbohydrates must be non-negative.", Toast.LENGTH_SHORT).show();
                          return; // Exit if any value is negative
                      }

                  } catch (NumberFormatException e) {
                      Toast.makeText(getActivity(), "Please enter valid numeric values for calories, fats, proteins, and carbohydrates.", Toast.LENGTH_SHORT).show();
                      return; // Exit if any input is invalid
                  }

                  if (!mealName.isEmpty() && !portionSize.isEmpty()) {

                      byte[] mealPhoto;

                      // Check if an image is selected
                      if (isImageSelected) {
                          // Convert the selected image to byte array
                          mealPhoto = getImageAsByteArray();
                          if (mealPhoto == null) {
                              Toast.makeText(getActivity(), "Failed to convert image. Please try again.", Toast.LENGTH_SHORT).show();
                              return; // Exit early if there's a problem
                          }
                      } else {
                          // Use the default image if no image was selected
                          mealPhoto = getDefaultImageAsByteArray();
                      }

                      // Create MealEntity without photo URL for now (we'll set it after upload)
                      MealEntity mealEntity = new MealEntity(mealName, newCalories, mealType, selectedDay, null);

                      // Show progress bar
                      progressBar.setVisibility(View.VISIBLE);


                      // Upload image to Firebase Storage and insert meal into Room
                      uploadImageToFirestore(mealPhoto, mealEntity); // mealEntity will be updated with the photo URL

                      // Update total calories count in ViewModel
                      double totalCalories = viewModel.getTotalCalories().getValue() != null ? viewModel.getTotalCalories().getValue() : 0;
                      viewModel.setTotalCalories(totalCalories + newCalories);

                      Toast.makeText(getActivity(), "Meal Logged: " + enteredCalories + " kcal", Toast.LENGTH_SHORT).show();

                      // Clear input and result fields after logging the meal
                      clearFields();
                      hideInputFields();
                  } else {
                      Toast.makeText(getActivity(), "Nutritional information not available", Toast.LENGTH_SHORT).show();

                  }
              }
            }
        });




        // Done button to navigate back to the menu (assuming you have a menu fragment)
        // Change goal button listener
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
    // Method to clear all input fields and results
    private void clearFields() {
        mealNameInput.setText(""); // Clear meal name input
        portionSizeInput.setText(""); // Clear portion size input
        apiResultText.setText(""); // Clear API result text
        apiResultText.setVisibility(View.GONE); // Hide the API result text
        imageView.setImageBitmap(null); // Clear the image
        imageView.setVisibility(View.GONE); // Hide the ImageView
        isImageSelected = false; // Reset the image selected flag
    }
    private void hideInputFields() {
        caloriesInput.setText(""); // Clear meal name input
        fatsInput.setText(""); // Clear portion size input
        proteinsInput.setText(""); // Clear API result text
        carbohydratesInput.setText("");//
        caloriesInput.setVisibility(View.GONE); // Clear meal name input
        fatsInput.setVisibility(View.GONE); // Clear portion size input
        proteinsInput.setVisibility(View.GONE); // Clear API result text
        carbohydratesInput.setVisibility(View.GONE); // Hide the API result tex

    }


    // Function to convert selected image to byte array
    private byte[] getImageAsByteArray() {
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            Log.e("LogMeal", "Selected image bitmap is null.");
            return null; // Return null if no image is selected
        }
    }

    private void insertImageIntoGallery(int drawableId, String fileName) {
        try {
            // Get input stream from drawable resource
            InputStream inputStream = getResources().openRawResource(drawableId);

            // Create a file in the Pictures directory
            File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(picturesDirectory, fileName);

            // Create the output stream to write the file
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

            // Trigger media scanner to scan the file so it appears in the gallery
            MediaScannerConnection.scanFile(requireContext(), new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), "Image added to gallery: " + path, Toast.LENGTH_SHORT).show();
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to insert image", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImageToFirestore(byte[] mealPhoto, MealEntity mealEntity) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        UploadTask uploadTask = imageRef.putBytes(mealPhoto);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        // Set the photo URL in the MealEntity object
                        mealEntity.setPhoto(downloadUrl.toString());

                        // Insert meal into Room database with the photo URL
                        databaseViewModel.insert(mealEntity);

                        Toast.makeText(getActivity(), "Meal logged successfully to Firebase", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to get image URL", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Image upload failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    // Function to convert default image to byte array
    private byte[] getDefaultImageAsByteArray() {
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image); // Replace with your default image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        defaultBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void showManualInputFields() {
        caloriesInput.setVisibility(View.VISIBLE);
        fatsInput.setVisibility(View.VISIBLE);
        proteinsInput.setVisibility(View.VISIBLE);
        carbohydratesInput.setVisibility(View.VISIBLE);
    }





}

