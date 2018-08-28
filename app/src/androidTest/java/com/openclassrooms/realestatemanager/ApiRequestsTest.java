package com.openclassrooms.realestatemanager;


import android.arch.persistence.room.Room;
import android.content.ContentUris;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.ListPointsInterest;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Utils.Utils;

import junit.framework.Assert;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class ApiRequestsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // FOR DATA
    private PropertyContentProvider propertyContentProvider;
    private int idProp;
    private Uri uriInsert;

    // Property for demo
    private Property PROPERTY_DEMO = new Property(0, "Appartment", 125000d,30.25d,1,
            "description","address",null,false,"01/06/2018","02/06/2018",0d,0d,"Eric",null,null);

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
    public void TEST_getinterestPoints(){

        List<String> listTypes = new ArrayList<>(Arrays.asList("electronics_store", "art_gallery", "shoe_store","parking"));

        List<String> listInterestTemp = new ArrayList<>(Utils.getInterestPoints(listTypes, mActivityTestRule.getActivity().getApplicationContext()));

        List<String> listInterest = new ArrayList<>(Utils.removeDuplicates(listInterestTemp));

        Assert.assertTrue(listInterest.size()==3);
    }

    @Test
    public void TEST_API_interestPoints(){

        ListPointsInterest listPointsInterest = new ListPointsInterest(mActivityTestRule.getActivity().getResources().getString(R.string.google_maps_key2),
                new LatLng(48.857327,2.336151),"500",mActivityTestRule.getActivity().getApplicationContext(),null);

        waiting_time(5000);

        Assert.assertTrue(listPointsInterest.getListPointsInterest().size() > 0);

    }

    @Test
    public void TEST_latLngRequest(){

        mActivityTestRule.getActivity().configureAndShowDisplayFragment(PROPERTY_DEMO);

        waiting_time(1000);

        mActivityTestRule.getActivity().setCurrentPositionDisplayed(-1);

        mActivityTestRule.getActivity().configureAndShowEditFragment(PROPERTY_DEMO);

        EditFragment editFragment = mActivityTestRule.getActivity().getEditFragment();

        waiting_time(2000);

        //SearchView.SearchAutoComplete searchAutoComplete = editFragment.getSearchView().findViewById(android.support.v7.appcompat.R.id.search_src_text);

        mActivityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editFragment.getSearchView().setQuery("rue Saint-Nicolas, Paris, France",true);
                //searchAutoComplete.setText("rue Saint-Nicolas, Paris, France");
            }
        });

        waiting_time(8000);

        Assert.assertTrue(editFragment.getLatLngAddress().latitude!=0d);
        Assert.assertTrue(editFragment.getLatLngAddress().longitude!=0d);
        Assert.assertTrue(editFragment.getStaticMap()!=null);
        Assert.assertTrue(editFragment.getInterestPoints()!=null);


    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
