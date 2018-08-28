package com.openclassrooms.realestatemanager;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.Property;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class EditTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /*private static Property PROPERTY_DEMO = new Property(0, "Appartment", 125000d,30.25,1,
            "description","address","School, Subway",false,"01/06/2018",0d,0d,"Eric",null);*/

    @Test
    public void TestAddRemoveImages(){

        //mActivityTestRule.getActivity().configureAndShowEditFragment(PROPERTY_DEMO);

        ViewInteraction button = onView(
                allOf(withId(R.id.icon_add_photo), withText("Add a photo"),
                        childAtPosition(
                                allOf(withId(R.id.image_panel),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        button.perform(click());

        waitingTime(500);

        // WRITE A DESCRIPTION
        ViewInteraction editText = onView(
                allOf(withId(R.id.description_image_edit_text),
                        childAtPosition(
                                allOf(withId(R.id.extra_panel),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        editText.perform(replaceText("Room"), closeSoftKeyboard());

        waitingTime(1000);

        ViewInteraction button2 = onView(
                allOf(withId(R.id.buttonImageOK), withText("OK"),
                        childAtPosition(
                                allOf(withId(R.id.extra_panel),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        button2.perform(click());
    }

    private void waitingTime(long timeWait){
        try {
            Thread.sleep(timeWait);
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
