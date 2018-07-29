package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PropertyDaoTest {

    private PropertyDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    private static int PROPERTY_ID = 7;
    private static Property PROPERTY_DEMO = new Property(PROPERTY_ID, "Appartment", 125000d,30.25,1,
            "description","address","School, Subway",false,"01/06/2018",0d,0d,"Eric");

    @Test
    public void insertAndUpdateProperty() throws InterruptedException {
        // BEFORE : Adding a new property
        this.database.propertyDao().insertProperty(PROPERTY_DEMO);
        // TEST
        Property property = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID));

        assertTrue(property.getType().equals(PROPERTY_DEMO.getType()) && property.getId() == PROPERTY_ID);

        // Change the surface of the property with id = 7
        PROPERTY_DEMO.setSurface(40d);

        // Update the property on SQLite
        this.database.propertyDao().updateProperty(PROPERTY_DEMO);
        // TEST
        Double surfaceToCheck = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID)).getSurface();

        assertTrue(surfaceToCheck == 40d);
    }

    @Test
    public void GetListProperties() throws InterruptedException {

        Property PROPERTY_DEMO1 = new Property(1, "Appartment", 361000d,38.25,2,
                "description","address","Swimming pool, gardens, Theater",false,
                "01/07/2018",0d,0d,"Kevin");
        Property PROPERTY_DEMO2 = new Property(2, "Appartment", 400000d,37.57,1,
                "description","address","College, Bus station",false,
                "02/07/2018",0d,0d,"Gaetan");
        Property PROPERTY_DEMO3 = new Property(3, "Appartment", 545000d,60.62,3,
                "description","address","School, Lake, High school, Theater",false,
                "03/07/2018",0d,0d,"Yannick");
        Property PROPERTY_DEMO4 = new Property(4, "Appartment", 660000d,60.44,3,
                "description","address","School, Subway, College, Swimming pool",false,
                "04/07/2018",0d,0d,"Kevin");
        Property PROPERTY_DEMO5 = new Property(5, "Appartment", 720000d,65d,4,
                "description","address","School, Subway, Library, Bowling",false,
                "05/07/2018",0d,0d,"Michelle");

        this.database.propertyDao().insertProperty(PROPERTY_DEMO1);
        this.database.propertyDao().insertProperty(PROPERTY_DEMO2);
        this.database.propertyDao().insertProperty(PROPERTY_DEMO3);
        this.database.propertyDao().insertProperty(PROPERTY_DEMO4);
        this.database.propertyDao().insertProperty(PROPERTY_DEMO5);
        // TEST
        List<Property> properties = LiveDataTestUtil.getValue(this.database.propertyDao().getListProperties(0,50d,70d,0d,1000000d,3,4,"Appartment"));

        assertTrue(properties.size()==3);
    }
}
