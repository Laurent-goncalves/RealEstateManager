package com.openclassrooms.realestatemanager;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;


@Entity(foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "id", childColumns = "idProperty"))
public class ImageProperty {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Bitmap image;
    private String description;
    private int idProperty;

    public ImageProperty(int id, Bitmap image, String description, int idProperty) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.idProperty = idProperty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
