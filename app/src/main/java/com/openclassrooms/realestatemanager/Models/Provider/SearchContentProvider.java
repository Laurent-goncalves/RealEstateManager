package com.openclassrooms.realestatemanager.Models.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.openclassrooms.realestatemanager.Models.PropertyDatabase;

public class SearchContentProvider extends ContentProvider {


    private Context context;
    private int sold;
    private Double surfaceInf;
    private Double surfaceSup;
    private Double priceInf;
    private Double priceSup;
    private int nbRoomsMin;
    private String typeProp;

    public void setParametersQuery(Context context, int sold, Double surfaceInf, Double surfaceSup, Double priceInf, Double priceSup,
                                    int nbRoomsMin, String typeProp){
        this.context=context;
        this.sold=sold;
        this.surfaceInf=surfaceInf;
        this.surfaceSup=surfaceSup;
        this.priceInf=priceInf;
        this.priceSup=priceSup;
        this.nbRoomsMin=nbRoomsMin;
        this.typeProp=typeProp;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (context != null){
            return PropertyDatabase.getInstance(context).propertyDao().getSearchProperties(sold, surfaceInf, surfaceSup, priceInf, priceSup,nbRoomsMin, typeProp);
            //cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Override
    public boolean onCreate() {
        return false;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
