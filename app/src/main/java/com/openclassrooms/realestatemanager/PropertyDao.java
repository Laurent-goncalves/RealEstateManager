package com.openclassrooms.realestatemanager;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE id = :id")
    LiveData<Property> getProperty(int id);

    @Insert
    long insertProperty(Property property);

    @Update
    int updateProperty(Property property);

}
