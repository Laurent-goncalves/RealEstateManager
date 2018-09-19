package com.openclassrooms.realestatemanager.Utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class RestoreDataMapFragment implements OnMapReadyCallback {

    private LatLng cameraPosition;
    private ConfigureMap configMap;

    public RestoreDataMapFragment(LatLng cameraPosition, ConfigureMap configMap) {
        this.cameraPosition = cameraPosition;
        this.configMap=configMap;
        configMap.getMapFragment().getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        configMap.setMap(googleMap);
        configMap.getPropertiesCloseToTarget(cameraPosition);
    }
}
