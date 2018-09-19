package com.openclassrooms.realestatemanager.Models.Provider;

import android.arch.persistence.room.OnConflictStrategy;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;


public class ImageContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.Models.Provider";
    public static final String TABLE_NAME = ImageProperty.class.getSimpleName();
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private Context context;

    @Override
    public boolean onCreate() {
        return true;
    }

    public void setUtils(Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (context != null){
            long imageId = ContentUris.parseId(uri);
            final Cursor cursor = PropertyDatabase.getInstance(context).imageDao().getImagesPropertyWithCursor(imageId);
            //cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
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
            final long id = PropertyDatabase.getInstance(context).imageDao().insertImage(ImageProperty.fromContentValues(values));
            if (id != 0){
                //context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (context!= null){
            long imageId = ContentUris.parseId(uri);
            final int count = PropertyDatabase.getInstance(context).imageDao().deleteImage(imageId);
            //context.getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        if (context!= null && values!=null){
            final int count = PropertyDatabase.getInstance(context).imageDao().updateImage(ImageProperty.fromContentValues(values));
            //context.getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
