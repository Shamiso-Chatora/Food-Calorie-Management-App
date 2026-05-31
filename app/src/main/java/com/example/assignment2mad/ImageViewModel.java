package com.example.assignment2mad;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * ImageViewModel holds a mutable LiveData object for managing a selected image
 * (of type Bitmap) within the ViewModel. It provides methods to set and retrieve
 * the selected image, allowing for efficient observation of image changes in the UI.
 */


public class ImageViewModel extends ViewModel {
    private MutableLiveData<Bitmap> selectedImage = new MutableLiveData<>();

    public void setSelectedImage(Bitmap image) {
        selectedImage.setValue(image);
    }

    public LiveData<Bitmap> getSelectedImage() {
        return selectedImage;
    }
}
