package com.openclassrooms.realestatemanager;


import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.ListPointsInterest;
import com.openclassrooms.realestatemanager.Utils.Utils;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class PointInterestTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

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

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
