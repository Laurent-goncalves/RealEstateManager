package com.openclassrooms.realestatemanager.Models;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;


@Database(entities = {Property.class, ImageProperty.class}, version = 17, exportSchema = false)
public abstract class PropertyDatabase extends RoomDatabase {

    private static volatile PropertyDatabase INSTANCE;
    public abstract PropertyDao propertyDao();
    public abstract ImageDao imageDao();

    // Create a single instance of property database
    public static PropertyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PropertyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PropertyDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Prepopulate the database
    private static RoomDatabase.Callback prepopulateDatabase(){
        return new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                ContentValues contentValues1 = new ContentValues();

                contentValues1.put("id", 1);
                contentValues1.put("type", "Appartement");
                contentValues1.put("price", "361000");
                contentValues1.put("surface", "38.25");
                contentValues1.put("roomNumber", "2");
                contentValues1.put("description", "Nice appartment in the center of Paris");
                contentValues1.put("address", "37 rue des Panoyaux 75020 Paris");
                contentValues1.put("interestPoints", "Swimming pool, gardens, Theater");
                contentValues1.put("sold", false);
                contentValues1.put("dateStart", "01/07/2018");
                contentValues1.put("estateAgent", "Kevin");
                contentValues1.put("lat", "48.85161");
                contentValues1.put("lng", "2.374858");

                ContentValues contentValues2 = new ContentValues();

                contentValues2.put("id", 2);
                contentValues2.put("type", "Appartement");
                contentValues2.put("price", "400000");
                contentValues2.put("surface", "37.57");
                contentValues2.put("roomNumber", "1");
                contentValues2.put("description", "The apartment includes a large living room, a kitchen, a bathroom with toilet, a dressing room");
                contentValues2.put("address", "147 rue Oberkampf Paris 11ème");
                contentValues2.put("interestPoints", "College, Bus station");
                contentValues2.put("sold", false);
                contentValues2.put("dateStart", "02/07/2018");
                contentValues2.put("estateAgent", "Gaetan");
                contentValues2.put("lat", "48.852265");
                contentValues2.put("lng", "2.380437");

                ContentValues contentValues3 = new ContentValues();

                contentValues3.put("id", 3);
                contentValues3.put("type", "Appartement");
                contentValues3.put("price", "545000");
                contentValues3.put("surface", "60.62");
                contentValues3.put("roomNumber", "3");
                contentValues3.put("description", "The apartment includes an entrance, a living room with a fitted kitchen, two bedrooms (one with shower room), a bathroom with toilet, a separate toilet with washbasin and a closet.");
                contentValues3.put("address", "39 boulevard Ornano Paris 18ème");
                contentValues3.put("interestPoints", "Forrest, Lake, High school, Theater");
                contentValues3.put("sold", false);
                contentValues3.put("dateStart", "03/07/2018");
                contentValues3.put("estateAgent", "Yannick");
                contentValues3.put("lat", "48.845013");
                contentValues3.put("lng", "2.355803");

                ContentValues contentValues4 = new ContentValues();

                contentValues4.put("id", 4);
                contentValues4.put("type", "Appartement");
                contentValues4.put("price", "660000");
                contentValues4.put("surface", "60.44");
                contentValues4.put("roomNumber", "3");
                contentValues4.put("description", "Luxury building, very nice apartment (60 m² including 20 m² in private enjoyment inalienable) atypical refitted by interior designer, excellent condition entirely on courtyard (very quiet)");
                contentValues4.put("address", "12 rue Cambronne 75015 PARIS");
                contentValues4.put("interestPoints", "Church, Subway, College, Swimming pool");
                contentValues4.put("sold", false);
                contentValues4.put("dateStart", "04/07/2018");
                contentValues4.put("estateAgent", "Xavier");
                contentValues4.put("lat", "48.849057");
                contentValues4.put("lng", "2.318295");

                ContentValues contentValues5 = new ContentValues();

                contentValues5.put("id", 5);
                contentValues5.put("type", "Appartement");
                contentValues5.put("price", "720000");
                contentValues5.put("surface", "60");
                contentValues5.put("roomNumber", "4");
                contentValues5.put("description", "Beautiful Haussmanian building with common areas in good condition. Located on the 1st floor, beautiful apartment crossing character (parquet floors point of Hungary, moldings, fireplace) in excellent condition");
                contentValues5.put("address", "44 rue Elvis 75015 PARIS");
                contentValues5.put("interestPoints", "School, Subway, Library, Bowling");
                contentValues5.put("sold", true);
                contentValues5.put("dateStart", "05/07/2018");
                contentValues5.put("dateSold", "28/07/2018");
                contentValues5.put("estateAgent", "Michelle");
                contentValues5.put("lat", "48.836912");
                contentValues5.put("lng", "2.290229");

                db.insert("Property", OnConflictStrategy.IGNORE, contentValues1);
                db.insert("Property", OnConflictStrategy.IGNORE, contentValues2);
                db.insert("Property", OnConflictStrategy.IGNORE, contentValues3);
                db.insert("Property", OnConflictStrategy.IGNORE, contentValues4);
                db.insert("Property", OnConflictStrategy.IGNORE, contentValues5);
            }
        };
    }


}
