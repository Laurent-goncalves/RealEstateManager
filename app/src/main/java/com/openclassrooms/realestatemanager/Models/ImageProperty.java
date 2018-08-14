package com.openclassrooms.realestatemanager.Models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;


@Entity(foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "id", childColumns = "idProperty"))
public class ImageProperty {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imageUri;
    private String description;
    private int idProperty;

    public ImageProperty(int id, String imageUri, String description, int idProperty) {
        this.id = id;
        this.imageUri = imageUri;
        this.description = description;
        this.idProperty = idProperty;
    }

    public ImageProperty(String imageUri, String description, int idProperty) {
        this.imageUri = imageUri;
        this.description = description;
        this.idProperty = idProperty;
    }

    public ImageProperty() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty = idProperty;
    }
}
