package com.openclassrooms.realestatemanager;


import android.arch.persistence.room.Room;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.ListPointsInterest;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.Utils.UtilsGoogleMap;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static android.Manifest.permission.CHANGE_NETWORK_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;


@RunWith(AndroidJUnit4.class)
public class ApiRequestsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // FOR DATA
    private PropertyContentProvider propertyContentProvider;
    private Uri uriInsert;

    // Property for demo
    private Property PROPERTY_DEMO = new Property(0, "Apartment", 125000d,30.25d,1,
            "description","address",null,false,"01/06/2018","02/06/2018",0d,0d,"Eric",null,null);

    @Rule public GrantPermissionRule runtimePermissionRule1 = GrantPermissionRule.grant(CHANGE_NETWORK_STATE);
    @Rule public GrantPermissionRule runtimePermissionRule2 = GrantPermissionRule.grant(CHANGE_WIFI_STATE);


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
        int idProp = (int) ContentUris.parseId(uriInsert);
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

        List<String> listInterestTemp = new ArrayList<>(UtilsGoogleMap.getInterestPoints(listTypes, mActivityTestRule.getActivity().getApplicationContext()));

        List<String> listInterest = new ArrayList<>(UtilsGoogleMap.removeDuplicates(listInterestTemp));

        Assert.assertEquals(3, listInterest.size());
    }


    @Test
    public void TEST_API_interestPoints(){

        ListPointsInterest listPointsInterest = new ListPointsInterest(mActivityTestRule.getActivity().getResources().getString(R.string.google_maps_key2),
                new LatLng(48.857327,2.336151),"500",mActivityTestRule.getActivity().getApplicationContext(),null);

        waiting_time(5000);

        Assert.assertTrue(listPointsInterest.getListPointsInterest().size() > 0);

    }

    public void Test_Internet_Connection(){

        Context context = mActivityTestRule.getActivity().getApplicationContext();

        // Disable wifi
        WifiManager wifiManager = (WifiManager) mActivityTestRule.getActivity().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);


        mActivityTestRule.getActivity().runOnUiThread(() -> Toast.makeText(context,"You have 15 seconds to desactivate internet",Toast.LENGTH_LONG).show());

        waiting_time(15000);

        // check that no internet connection are available
        Assert.assertFalse(Utils.isInternetAvailable(mActivityTestRule.getActivity().getApplicationContext()));

        // Re-activate wifi
        wifiManager.setWifiEnabled(true);

        waiting_time(3000);

        // Check that internet is available
        Assert.assertTrue(Utils.isInternetAvailable(mActivityTestRule.getActivity().getApplicationContext()));

        mActivityTestRule.getActivity().runOnUiThread(() -> Toast.makeText(context,"Please restore internet connection",Toast.LENGTH_LONG).show());

    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
