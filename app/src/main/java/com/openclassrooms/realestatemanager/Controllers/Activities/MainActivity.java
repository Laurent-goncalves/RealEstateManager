package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.Controllers.Fragments.DisplayFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.Provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        // Configure toolbar

        currentPositionDisplayed=-1;

        if(getIntent()!=null){
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null) {
                if (bundle.getInt(EXTRA_PROPERTY_ID, -1) != -1) {
                    configureAndShowDisplayFragment(bundle.getInt(EXTRA_PROPERTY_ID, -1));
                }
            } else {
                /*Intent intent = new Intent(this,MapsActivity.class);
                startActivity(intent);*/
                configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
            }
        } else {
            /*Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);*/
            configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
        }

        // Show ListPropertiesFragment
        // configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
    }



}
