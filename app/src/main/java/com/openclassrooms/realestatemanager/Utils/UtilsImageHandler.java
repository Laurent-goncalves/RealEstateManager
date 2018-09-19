package com.openclassrooms.realestatemanager.Utils;


import com.openclassrooms.realestatemanager.Models.ImageProperty;
import java.util.List;

public class UtilsImageHandler {

    // ------------------------------------------------- Change image path in listImages ---------------------------
    public static void changeImagePathInListImages(String imagePath, List<ImageProperty> listImages, int position){
        listImages.get(position).setImagePath(imagePath);
    }

    // ------------------------------------------------- Change description in listImages --------------------------
    public static void changeDescriptionInListImages(String description, List<ImageProperty> listImages, int position){
        listImages.get(position).setDescription(description);
    }



}
