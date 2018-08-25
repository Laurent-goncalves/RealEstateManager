package com.openclassrooms.realestatemanager.Models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE id = :id")
    LiveData<Property> getProperty(int id);

    @Query("SELECT * FROM Property")
    Cursor getAllPropertiesWithCursor();

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllProperties();

    @Insert
    long insertProperty(Property property);

    @Update
    int updateProperty(Property property);

    @Query("SELECT * FROM Property WHERE Property.sold = :sold AND Property.surface <= :surfaceSup AND Property.surface >= :surfaceInf " +
    "AND Property.price>= :priceInf AND Property.price<= :priceSup AND Property.type LIKE :typeProp AND Property.roomNumber <= :nbRoomsSup AND Property.roomNumber >= :nbRoomsInf")
    LiveData<List<Property>> getListProperties(int sold, Double surfaceInf, Double surfaceSup,
                                             Double priceInf, Double priceSup,
                                             int nbRoomsInf, int nbRoomsSup, String typeProp);
                                             //String[] interestPoints, String dateStart);






}


