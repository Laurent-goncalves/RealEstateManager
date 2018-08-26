package com.openclassrooms.realestatemanager.Models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM ImageProperty WHERE idProperty = :userId")
    Cursor getImagesPropertyWithCursor(long userId);

    @Insert
    long insertImage(ImageProperty image);

    @Update
    int updateImage(ImageProperty image);

    @Query("DELETE FROM ImageProperty")
    int deleteAllImage();

    @Query("DELETE FROM ImageProperty WHERE id = :itemId")
    int deleteImage(long itemId);
}
