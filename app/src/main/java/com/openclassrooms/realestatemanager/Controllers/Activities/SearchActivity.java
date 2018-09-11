package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    private ScrollView listFragLayout;
    private static final String MODE_SEARCH = "mode_search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        modeSelected = MODE_SEARCH;

        setModeDevice();
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);
        listFragLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGrey));

        if(modeDevice.equals(MODE_TABLET)) { // MODE TABLET
            listFragLayout = findViewById(R.id.fragment_list_layout);
            configureAndShowListPropertiesFragment(MODE_SEARCH,null);
        }

        configureAndShowSearchFragment();


        /*if(savedInstanceState!=null){
            showSaveInstanceFragment(savedInstanceState);
        } else
            configureAndShowSearchFragment();*/
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public ScrollView getListFragLayout() {
        return listFragLayout;
    }
}
