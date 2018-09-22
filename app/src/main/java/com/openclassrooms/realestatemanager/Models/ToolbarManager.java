package com.openclassrooms.realestatemanager.Models;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Controllers.Activities.SearchActivity;
import com.openclassrooms.realestatemanager.R;


public class ToolbarManager implements NavigationView.OnNavigationItemSelectedListener{

    private static final String MODE_DISPLAY = "mode_display";
    private final static String MODE_TABLET = "mode_tablet";
    private final static String MAINACTIVITY = "mainactivity";
    private final static String MAPSACTIVITY = "mapsactivity";
    private final static String SEARCHACTIVITY = "searchactivity";
    private MainActivity mainActivity;
    private MapsActivity mapsActivity;
    private SearchActivity searchActivity;
    private Context context;
    private ImageButton hamburger;
    private ImageButton addButton;
    private ImageButton editButton;
    private Toolbar toolbar;

    public ToolbarManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
        configureToolbar(mainActivity);
    }

    public ToolbarManager(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
        context = mapsActivity.getApplicationContext();
        configureToolbar(mapsActivity);
    }

    public ToolbarManager(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
        context = searchActivity.getApplicationContext();
        configureToolbar(searchActivity);
    }

    // ---------------------------------------------------------------------------------
    // -----------------     CONFIGURATION OF TOOLBAR  ---------------------------------
    // ---------------------------------------------------------------------------------

    public void configureToolbar(MainActivity mainActivity){

        if(mainActivity!=null){
            toolbar = mainActivity.findViewById(R.id.activity_main_toolbar);
            mainActivity.setSupportActionBar(toolbar);

            // configure hamburger menu to open the navigation drawer
            hamburger = toolbar.findViewById(R.id.button_hamburger);
            hamburger.setOnClickListener(v -> mainActivity.getDrawerLayout().openDrawer(Gravity.START));
            mainActivity.getNavigationView().setNavigationItemSelectedListener(this);

            // Assign buttons from toolbar
            addButton = toolbar.findViewById(R.id.add_property_button);
            editButton = toolbar.findViewById(R.id.edit_property_button);

            configureOnClickListener(mainActivity);
        }

        //Assign and edit toolbar title
        TextView title_toolbar = toolbar.findViewById(R.id.title_toolbar);
        title_toolbar.setText(context.getResources().getString(R.string.app_name));
    }

    public void configureToolbar(MapsActivity mapsActivity){

        if (mapsActivity!=null) {

            toolbar = mapsActivity.findViewById(R.id.activity_main_toolbar);
            mapsActivity.setSupportActionBar(toolbar);

            // configure hamburger menu to open the navigation drawer
            hamburger = toolbar.findViewById(R.id.button_hamburger);
            hamburger.setOnClickListener(v -> mapsActivity.getDrawerLayout().openDrawer(Gravity.START));
            mapsActivity.getNavigationView().setNavigationItemSelectedListener(this);

            // Assign buttons from toolbar
            addButton = toolbar.findViewById(R.id.add_property_button);
            editButton = toolbar.findViewById(R.id.edit_property_button);

            configureOnClickListener(mapsActivity);
        }

        //Assign and edit toolbar title
        TextView title_toolbar = toolbar.findViewById(R.id.title_toolbar);
        title_toolbar.setText(context.getResources().getString(R.string.app_name));
    }

    public void configureToolbar(SearchActivity searchActivity){

        if(searchActivity!=null){
            toolbar = searchActivity.findViewById(R.id.activity_main_toolbar);
            searchActivity.setSupportActionBar(toolbar);

            // configure hamburger menu to open the navigation drawer
            hamburger = toolbar.findViewById(R.id.button_hamburger);
            hamburger.setOnClickListener(v -> searchActivity.getDrawerLayout().openDrawer(Gravity.START));
            searchActivity.getNavigationView().setNavigationItemSelectedListener(this);

            // Assign buttons from toolbar
            addButton = toolbar.findViewById(R.id.add_property_button);
            editButton = toolbar.findViewById(R.id.edit_property_button);

            configureOnClickListener(searchActivity);
        }

        //Assign and edit toolbar title
        TextView title_toolbar = toolbar.findViewById(R.id.title_toolbar);
        title_toolbar.setText(context.getResources().getString(R.string.app_name));
    }

    // ---------------------------------------------------------------------------------
    // -----------------     CONFIGURATION OF CLICK LISTENERS --------------------------
    // ---------------------------------------------------------------------------------

    private void configureOnClickListener(MainActivity mainActivity){

        addButton.setOnClickListener(v -> {
                mainActivity.changeToEditMode(-1); // new property to add to BDD
        });

        editButton.setOnClickListener(v -> {
            int propertyId = mainActivity.getCurrentPropertyDisplayed();
            mainActivity.changeToEditMode(propertyId); // edit property currently displayed
        });
    }

    private void configureOnClickListener(MapsActivity mapsActivity){

        editButton.setOnClickListener(v -> {
            int propertyId = mapsActivity.getCurrentPropertyDisplayed();
            mapsActivity.changeToEditMode(propertyId); // edit property currently displayed
        });
    }

    private void configureOnClickListener(SearchActivity searchActivity){

        editButton.setOnClickListener(v -> {
            int propertyId = searchActivity.getCurrentPropertyDisplayed();
            searchActivity.changeToEditMode(propertyId); // edit property currently displayed
        });
    }

    // ---------------------------------------------------------------------------------
    // -----------------     CONFIGURATION OF ICONS ------------------------------------
    // ---------------------------------------------------------------------------------

    public void setIconsToolbarDisplayMode(String modeSelected, String modeDevice){
        if(modeDevice.equals(MODE_TABLET) && modeSelected.equals(MODE_DISPLAY)) {
            addButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        } else {
            addButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
        }
    }

    public void setIconsToolbarEditMode(){
        addButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
    }

    public void setIconsToolbarMapsMode(){
        addButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
    }

    public void setIconsToolbarListPropertiesMode(String modeSelected){

        if(modeSelected.equals(MODE_DISPLAY)){
            addButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
        } else {
            addButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }
    }

    public void setIconsToolbarSearchPropertiesMode(){
        addButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
    }

    // ---------------------------------------------------------------------------------
    // -----------------     CONFIGURATION OF DRAWER  ----------------------------------
    // ---------------------------------------------------------------------------------

    public void configureNavigationDrawer(MainActivity mainActivity){
        Menu navigMenu1 = mainActivity.getNavigationView().getMenu();
        navigMenu1.findItem(R.id.mapsactivity_menu_item).setVisible(true);

        Menu navigMenu2 = mainActivity.getNavigationView().getMenu();
        navigMenu2.findItem(R.id.search_property_menu_item).setVisible(true);

        Menu navigMenu3 = mainActivity.getNavigationView().getMenu();
        navigMenu3.findItem(R.id.mainpage_menu_item).setVisible(false);
    }

    public void configureNavigationDrawer(MapsActivity mapsActivity){
        Menu navigMenu1 = mapsActivity.getNavigationView().getMenu();
        navigMenu1.findItem(R.id.mapsactivity_menu_item).setVisible(false);

        Menu navigMenu2 = mapsActivity.getNavigationView().getMenu();
        navigMenu2.findItem(R.id.search_property_menu_item).setVisible(true);

        Menu navigMenu3 = mapsActivity.getNavigationView().getMenu();
        navigMenu3.findItem(R.id.mainpage_menu_item).setVisible(true);
    }

    public void configureNavigationDrawer(SearchActivity searchActivity){
        Menu navigMenu1 = searchActivity.getNavigationView().getMenu();
        navigMenu1.findItem(R.id.mapsactivity_menu_item).setVisible(true);

        Menu navigMenu2 = searchActivity.getNavigationView().getMenu();
        navigMenu2.findItem(R.id.search_property_menu_item).setVisible(false);

        Menu navigMenu3 = searchActivity.getNavigationView().getMenu();
        navigMenu3.findItem(R.id.mainpage_menu_item).setVisible(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        // ------------------------------ mainActivity ----------------------------------
        if(mainActivity!=null){

            switch (id) {
                case R.id.mapsactivity_menu_item:
                    changeActivity(mainActivity, MAPSACTIVITY);
                    break;
                case R.id.search_property_menu_item:
                    changeActivity(mainActivity, SEARCHACTIVITY);
                    break;
                default:
                    break;
            }

            mainActivity.getDrawerLayout().closeDrawer(GravityCompat.START);

        // ------------------------------ mapsActivity ----------------------------------
        } else if (mapsActivity!=null){

            switch (id) {
                case R.id.search_property_menu_item:
                    changeActivity(mapsActivity, SEARCHACTIVITY);
                    break;
                case R.id.mainpage_menu_item:
                    changeActivity(mapsActivity, MAINACTIVITY);
                    break;
                default:
                    break;
            }

            mapsActivity.getDrawerLayout().closeDrawer(GravityCompat.START);

        // ------------------------------ searchActivity ----------------------------------
        } else if (searchActivity!=null){

            switch (id) {
                case R.id.mapsactivity_menu_item:
                    changeActivity(searchActivity, MAPSACTIVITY);
                    break;
                case R.id.mainpage_menu_item:
                    changeActivity(searchActivity, MAINACTIVITY);
                    break;
                default:
                    break;
            }

            searchActivity.getDrawerLayout().closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void changeActivity(BaseActivity baseActivity, String activityToLaunch){
        final String EDIT_FRAG = "fragment_edit";
        if(baseActivity.getFragmentDisplayed()!=null){
            if(baseActivity.getFragmentDisplayed().equals(EDIT_FRAG)){ // if edit fragment is displayed
                displayAlertEditFragment(baseActivity, activityToLaunch);
            } else
                proceedToChangeOfActivity(baseActivity,activityToLaunch);
        } else
            proceedToChangeOfActivity(baseActivity,activityToLaunch);
    }

    private void displayAlertEditFragment(BaseActivity baseActivity, String activityToLaunch){

        AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
        builder.setCancelable(true);
        builder.setTitle(baseActivity.getApplicationContext().getResources().getString(R.string.warning_title));
        builder.setMessage(baseActivity.getApplicationContext().getResources().getString(R.string.change_activity_confirmation));
        builder.setPositiveButton(baseActivity.getApplicationContext().getResources().getString(R.string.confirm), (dialog, id) -> proceedToChangeOfActivity(baseActivity,activityToLaunch))
                .setNegativeButton(R.string.cancel, (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void proceedToChangeOfActivity(BaseActivity baseActivity, String activityToLaunch){
        switch(activityToLaunch){
            case MAINACTIVITY:
                baseActivity.launchMainActivity();
                break;
            case MAPSACTIVITY:
                baseActivity.launchMapsActivity();
                break;
            case SEARCHACTIVITY:
                baseActivity.launchSearchActivity();
                break;
        }
        baseActivity.stopActivity();
    }
}
