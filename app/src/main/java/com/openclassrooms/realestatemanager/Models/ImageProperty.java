package com.openclassrooms.realestatemanager.Models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;


@Entity(foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "id", childColumns = "idProperty"))
public class ImageProperty {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imagePath;
    private String description;
    private int idProperty;

    public ImageProperty(int id, String imagePath, String description, int idProperty) {
        this.id = id;
        this.imagePath = imagePath;
        this.description = description;
        this.idProperty = idProperty;
    }

    @Ignore
    public ImageProperty(String imagePath, String description, int idProperty) {
        this.imagePath = imagePath;
        this.description = description;
        this.idProperty = idProperty;
    }

    @Ignore
    public ImageProperty() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    // --- UTILS ---
    public static ImageProperty fromContentValues(ContentValues values) {

        final ImageProperty imageProperty = new ImageProperty();
        if (values.containsKey("id")) imageProperty.setId(values.getAsInteger("id"));
        if (values.containsKey("imagePath")) imageProperty.setImagePath(values.getAsString("imagePath"));
        if (values.containsKey("description")) imageProperty.setDescription(values.getAsString("description"));
        if (values.containsKey("idProperty")) imageProperty.setIdProperty(values.getAsInteger("idProperty"));

        return imageProperty;
    }

    public static ContentValues createContentValuesFromImagePropertyUpdate(ImageProperty imageProperty) {

        final ContentValues values = new ContentValues();

        values.put("id",imageProperty.getId());
        values.put("imagePath",imageProperty.getImagePath());
        values.put("description",imageProperty.getDescription());
        values.put("idProperty",imageProperty.getIdProperty());

        return values;
    }

    public static ContentValues createContentValuesFromImagePropertyInsert(ImageProperty imageProperty) {

        final ContentValues values = new ContentValues();

        values.put("imagePath",imageProperty.getImagePath());
        values.put("description",imageProperty.getDescription());
        values.put("idProperty",imageProperty.getIdProperty());

        return values;
    }

    public static ImageProperty getImagePropertyFromCursor(Cursor cursor){

        final ImageProperty imageProperty = new ImageProperty();

        if(cursor!=null){
            imageProperty.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            imageProperty.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow("imagePath")));
            imageProperty.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            imageProperty.setIdProperty(cursor.getInt(cursor.getColumnIndexOrThrow("idProperty")));
        }

        return imageProperty;
    }
}
