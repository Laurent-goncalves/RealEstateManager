package com.openclassrooms.realestatemanager.Models.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;


public class PropertyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.Models.Provider";
    public static final String TABLE_NAME = Property.class.getSimpleName();
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private Boolean allProperties;
    private Context context;

    @Override
    public boolean onCreate() {
        return true;
    }

    public void setUtils(Context context, Boolean allProperties){
        this.allProperties=allProperties;
        this.context=context;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (context != null){
            if(allProperties)
                return PropertyDatabase.getInstance(context).propertyDao().getPropertiesNotSold();
            else {
                long idProperty = ContentUris.parseId(uri);
                return PropertyDatabase.getInstance(context).propertyDao().getProperty(idProperty);
            }
        }
        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        if (context != null && values !=null){
            final long id = PropertyDatabase.getInstance(context).propertyDao().insertProperty(Property.fromContentValues(values));
            if (id != 0){
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (context!= null){
            long idProperty = ContentUris.parseId(uri);
            return PropertyDatabase.getInstance(context).propertyDao().deletProperty(idProperty);
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        if (context!= null && values!=null){
            return PropertyDatabase.getInstance(context).propertyDao().updateProperty(Property.fromContentValues(values));
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
