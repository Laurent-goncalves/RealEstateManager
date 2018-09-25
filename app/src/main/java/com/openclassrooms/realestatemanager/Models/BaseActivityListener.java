package com.openclassrooms.realestatemanager.Models;

import android.os.Bundle;
import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import java.util.List;


public interface BaseActivityListener {

    void configureAndShowDisplayFragment(String modeSelected, int idProp);

    void setListProperties(List<Property> listProperties);

    void returnToSearchCriteria();

    void configureAndShowListPropertiesFragment(String modeSelected);

    void changeToDisplayMode(int idProp);

    ListPropertiesFragment getListPropertiesFragment();

    String getFragmentDisplayed();

    void setCurrentPositionDisplayed(int idProperty);

    void configureAndShowEditFragment(String modeSelected, int propertyId);

    void setSearchQuery(SearchQuery searchQuery);

    void stopActivity();

    void launchMainActivity();

    void changePropertySelectedInList(int idProperty);

    ToolbarManager getToolbarManager();

    SearchQuery getSearchQuery();

    LatLngBounds getCameraBounds();

    EditFragment getEditFragment();

    BaseActivity getBaseActivity();

    void refreshListPropertiesFragment(Bundle bundle, String modeSelected);
}
