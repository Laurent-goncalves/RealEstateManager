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
        } else


        configureAndShowSearchFragment();


        /*if(savedInstanceState!=null){
            showSaveInstanceFragment(savedInstanceState);
        } else
            configureAndShowSearchFragment();*/
    }

    @Override
    public void onBackPressed(){
        if(fragmentDisplayed.equals(EDIT_FRAG))
            configureAndShowDisplayFragment(modeSelected, idProperty);
        else if(fragmentDisplayed.equals(DISPLAY_FRAG) && modeDevice.equals(MODE_PHONE))
            configureAndShowListPropertiesFragment(modeSelected,listProperties);
        else
            returnToSearchCriteria();
    }

    public ScrollView getListFragLayout() {
        return listFragLayout;
    }
}
