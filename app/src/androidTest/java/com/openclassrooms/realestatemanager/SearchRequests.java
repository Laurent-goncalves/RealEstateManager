package com.openclassrooms.realestatemanager;

import android.arch.persistence.room.Room;
import android.content.ContentUris;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class SearchRequests {

    @Rule
    public ActivityTestRule<SearchActivity> mSearchActivityTestRule = new ActivityTestRule<>(SearchActivity.class);

    private Property PROPERTY_DEMO = new Property(0, "Apartment", 125000d,30.25d,1,
            "description","address",null,false,"01/06/2018","02/06/2018",48.848819d,2.342349d,"Eric",null,null);

    int idProp;
    PropertyContentProvider propertyContentProvider;
    Uri uriInsert;
    SearchFragment searchFragment;

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

    @After
    public void deleteProperty(){
        // Delete property
        Uri uriDelete = ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, ContentUris.parseId(uriInsert));
        propertyContentProvider.delete(uriDelete,null,null);
    }


    @Test
    public void TestSearch(){

        mSearchActivityTestRule.getActivity().configureAndShowSearchFragment();

        waiting_time(3000);

        searchFragment = mSearchActivityTestRule.getActivity().getSearchFragment();


        try {
            mSearchActivityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // set criteria
                    searchFragment.getPriceInfView().setText("120000");
                    searchFragment.getPriceSupView().setText("130000");
                    searchFragment.getSurfaceInfView().setText("30");
                    searchFragment.getSurfaceSupView().setText("35");

                    // click on search button
                    searchFragment.getButtonSearch().performClick();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        waiting_time(8000);

        // Recover results
        List<Property> listProperties = mSearchActivityTestRule.getActivity().getListProperties();

        // check that 1 property is found
        Assert.assertTrue(listProperties.size() == 1);

        mSearchActivityTestRule.getActivity().returnToSearchCriteria();
        mSearchActivityTestRule.getActivity().setListProperties(null);

        waiting_time(3000);

        searchFragment = mSearchActivityTestRule.getActivity().getSearchFragment();

        try {
            mSearchActivityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // set criteria by changing surface inf
                    searchFragment.getPriceInfView().setText("120000");
                    searchFragment.getPriceSupView().setText("121000");
                    searchFragment.getSurfaceInfView().setText("34");
                    searchFragment.getSurfaceSupView().setText("35");

                    // click on search button
                    searchFragment.getButtonSearch().performClick();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        waiting_time(8000);

        // Recover results
        listProperties = mSearchActivityTestRule.getActivity().getListProperties();

        // check that 0 property is found
        Assert.assertTrue(listProperties==null);
    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
