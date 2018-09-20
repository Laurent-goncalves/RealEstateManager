package com.openclassrooms.realestatemanager.Models;

import android.widget.ImageView;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Views.PropertyViewHolder;
import java.util.List;


public interface BaseActivityListener {
    void configureAndShowDisplayFragment(String modeSelected, int idProp);

    void setListProperties(List<Property> listProperties);

    void setImage(String imagePath, ImageView mainImage);

    void returnToSearchCriteria();

    void configureAndShowListPropertiesFragment(String modeSelected, List<Property> listProperties);

    void changeToDisplayMode(int idProp);

    void displayAlertDeletion(int viewholderposition);

    void displayError(String errorText);

    ListPropertiesFragment getListPropertiesFragment();

    void showSnackBar(String text);

    void getMainImage();

    void getExtraImage(int viewHolderPosition);

    String getFragmentDisplayed();

    void setCurrentPositionDisplayed(int idProperty);

    void configureAndShowEditFragment(int propertyId);

    void changeOfPropertySelected(PropertyViewHolder holder, int position, CallbackPropertyAdapter callbackPropertyAdapter);

    void setSearchQuery(SearchQuery searchQuery);

    void stopActivity();

    void launchMainActivity();

    void changePropertySelectedInList(int idProperty);

    ToolbarManager getToolbarManager();
}
