package com.openclassrooms.realestatemanager.Models;

import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.Controllers.Activities.MapsActivity;
import com.openclassrooms.realestatemanager.Utils.ConfigureMap;

public interface MapsActivityListener {

    void setConfigureMap(ConfigureMap configureMap);

    void displayMap();

    LatLngBounds getCameraBounds();

    MapsActivity getMapsActivity();

    ConfigureMap getConfigureMap();
}
