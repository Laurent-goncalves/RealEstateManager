package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
            "description","address","School, Subway","01/06/2018","Eric");

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


}
