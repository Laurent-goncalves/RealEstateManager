package com.openclassrooms.realestatemanager.Models;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE id = :id")
    Cursor getProperty(long id);

    @Query("SELECT * FROM Property")
    Cursor getAllPropertiesWithCursor();

    @Query("SELECT * FROM Property WHERE Property.sold = 0")
    Cursor getPropertiesNotSold();

    @Insert
    long insertProperty(Property property);

    @Update
    int updateProperty(Property property);

    @Query("DELETE FROM Property WHERE id = :id")
    int deletProperty(long id);

    @Query("DELETE FROM Property")
    int deleteAllProperties();

    @Query("SELECT * FROM Property WHERE Property.sold = :sold AND Property.surface <= :surfaceSup AND Property.surface >= :surfaceInf " +
    "AND Property.price>= :priceInf AND Property.price<= :priceSup AND Property.type LIKE :typeProp AND Property.roomNumber <= 99 AND Property.roomNumber >= :nbRoomsMin")
    Cursor getSearchProperties(int sold, Double surfaceInf, Double surfaceSup, Double priceInf, Double priceSup,
                                             int nbRoomsMin, String typeProp);

}


