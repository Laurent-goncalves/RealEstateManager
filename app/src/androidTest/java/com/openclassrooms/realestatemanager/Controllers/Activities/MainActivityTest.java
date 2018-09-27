package com.openclassrooms.realestatemanager.Controllers.Activities;


import android.arch.persistence.room.Room;
import android.content.ContentUris;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.Manifest.permission.CHANGE_NETWORK_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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



        /*
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list_properties_recycler_view),
                        childAtPosition(
                                withId(R.id.fragment_list_properties),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        waiting_time(5000); // WAITING TIME /////////////////////////////////////////////////

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.edit_property_button), withContentDescription("button edit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar_relativelayout),
                                        2),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        waiting_time(5000); // WAITING TIME /////////////////////////////////////////////////

        pressBack();

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        pressBack();*/