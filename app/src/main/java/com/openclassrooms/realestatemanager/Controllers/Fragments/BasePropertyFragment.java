package com.openclassrooms.realestatemanager.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;


public class BasePropertyFragment extends Fragment {

    private Property property;

    public BasePropertyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_property, container, false);
    }

}
