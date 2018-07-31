package com.openclassrooms.realestatemanager;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM ImageProperty WHERE id = :id")
    LiveData<ImageProperty> getImage(int id);

    @Query("SELECT * FROM ImageProperty")
    LiveData<List<ImageProperty>> getAllImages();

    @Insert
    long insertImage(ImageProperty image);

    @Update
    int updateImage(ImageProperty image);

    @Query("DELETE FROM ImageProperty WHERE id = :itemId")
    int deleteImage(long itemId);
}
