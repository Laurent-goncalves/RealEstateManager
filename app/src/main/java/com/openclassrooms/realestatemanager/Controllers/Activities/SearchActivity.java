package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import android.widget.ScrollView;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import butterknife.ButterKnife;



public class SearchActivity extends BaseActivity {

    private ScrollView listFragLayout;
    private static final String MODE_SEARCH = "mode_search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        modeSelected = MODE_SEARCH;

        setModeDevice();
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        if(modeDevice.equals(MODE_TABLET)) { // MODE TABLET
            listFragLayout = findViewById(R.id.fragment_list_layout);
            listFragLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGrey));
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
