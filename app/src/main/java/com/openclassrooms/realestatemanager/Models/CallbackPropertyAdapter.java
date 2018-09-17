package com.openclassrooms.realestatemanager.Models;

import com.openclassrooms.realestatemanager.Views.PropertyViewHolder;

public interface CallbackPropertyAdapter {

    void proceedToChangeOfPropertySelection(PropertyViewHolder holder, int position);
}
