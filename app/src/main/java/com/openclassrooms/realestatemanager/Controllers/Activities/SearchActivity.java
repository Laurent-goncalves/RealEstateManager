package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.relativelayout_return_search_button) RelativeLayout mRelativeLayout;
    private static final String MODE_SEARCH = "mode_search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setModeDevice();
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        mRelativeLayout.setVisibility(View.GONE);

        if(modeDevice.equals(MODE_TABLET)) { // MODE TABLET
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

    @OnClick(R.id.button_return_search)
    public void buttonReturnClick(){


    }

    public RelativeLayout getRelativeLayout() {
        return mRelativeLayout;
    }
}
