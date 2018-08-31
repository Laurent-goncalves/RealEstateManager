package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.os.Bundle;
import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        configureAndShowSearchFragment();
    }
}
