package com.openclassrooms.realestatemanager.Controllers.Activities;


import android.arch.persistence.room.Room;
import android.content.ContentUris;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;



@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // FOR DATA
    private PropertyContentProvider propertyContentProvider;
    private int idProp;
    private Uri uriInsert;

    // Property for demo
    private Property PROPERTY_DEMO = new Property(0, "Apartment", 125000d,30.25d,1,
            "description","address",null,false,"01/06/2018","02/06/2018",48.866298, 2.383746,"Eric",null,null);
    private static final String MODE_DISPLAY = "mode_display";

    @Rule public GrantPermissionRule runtimePermissionRule3 = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE);

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();

        propertyContentProvider = new PropertyContentProvider();
        propertyContentProvider.setUtils(InstrumentationRegistry.getTargetContext(),false);

        // Insert new property in database
        uriInsert = propertyContentProvider.insert(PropertyContentProvider.URI_ITEM, Property.createContentValuesFromPropertyInsert(PROPERTY_DEMO));
        idProp = (int) ContentUris.parseId(uriInsert);
        PROPERTY_DEMO.setId(idProp);
    }

    @Test
    public void mainActivityTest() {

        mActivityTestRule.getActivity().configureAndShowListPropertiesFragment(MODE_DISPLAY);

        waiting_time(3000); // WAITING TIME /////////////////////////////////////////////////

        mActivityTestRule.getActivity().configureAndShowDisplayFragment(MODE_DISPLAY,idProp);

        waiting_time(3000); // WAITING TIME /////////////////////////////////////////////////

        mActivityTestRule.getActivity().configureAndShowEditFragment(MODE_DISPLAY,idProp);

        waiting_time(3000); // WAITING TIME /////////////////////////////////////////////////

        mActivityTestRule.getActivity().configureAndShowListPropertiesFragment(MODE_DISPLAY);

    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void deleteProperty(){
        // Delete property
        Uri uriDelete = ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, ContentUris.parseId(uriInsert));
        propertyContentProvider.delete(uriDelete,null,null);
    }
}