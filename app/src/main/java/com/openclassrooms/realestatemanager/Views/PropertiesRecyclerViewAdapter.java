package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.realestatemanager.Controllers.Fragments.ListPropertiesFragment;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Models.CallbackPropertyAdapter;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;

import java.util.List;
import java.util.Objects;


public class PropertiesRecyclerViewAdapter extends RecyclerView.Adapter<PropertyViewHolder> implements CallbackPropertyAdapter {

    private final static String MODE_TABLET = "mode_tablet";
    private final static String MODE_DISPLAY_MAPS = "mode_maps_display";
    private final static String MAPS_FRAG = "fragment_maps";
    private final static String EDIT_FRAG = "fragment_edit";
    private CallbackListProperties callbackListProperties;
    private CallbackPropertyAdapter callbackPropertyAdapter;
    private BaseActivityListener baseActivityListener;
    private ListPropertiesFragment listPropertiesFragment;
    private List<Property> listProperties;
    private Context context;
    private int propertySelected;
    private String modeSelected;
    private String modeDevice;


    public PropertiesRecyclerViewAdapter(ListPropertiesFragment listPropertiesFragment, List<Property> listProperties, Context context, int position, CallbackListProperties callbackListProperties, BaseActivityListener baseActivityListener, String modeSelected, String modeDevice) {
        this.listProperties = listProperties;
        this.context=context;
        this.callbackListProperties=callbackListProperties;
        this.baseActivityListener = baseActivityListener;
        this.modeSelected=modeSelected;
        this.propertySelected=position;
        this.modeDevice=modeDevice;
        this.listPropertiesFragment=listPropertiesFragment;
        callbackPropertyAdapter=this;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.property_item, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {

        if(listProperties!=null && baseActivityListener!=null)
            holder.configurePropertiesViews(listProperties.get(position), context, modeDevice, baseActivityListener);

        holder.propertyLayout.setOnClickListener(v -> {

            // in case of tablet mode and the property selected
                if(modeDevice.equals(MODE_TABLET) && baseActivityListener.getFragmentDisplayed().equals(EDIT_FRAG) && holder.getAdapterPosition()!= propertySelected && propertySelected!=-1 && listProperties!=null){
                    UtilsBaseActivity.askToChangeOfPropertyToSelect(baseActivityListener.getBaseActivity(),holder, holder.getAdapterPosition(), callbackPropertyAdapter);
                } else {
                    proceedToChangeOfPropertySelection(holder,holder.getAdapterPosition());
                }
            });

        setColorItem(holder);
    }

    public void proceedToChangeOfPropertySelection(PropertyViewHolder holder, int position){

        // if maps mode, change marker. If not, show displayFragment
        if(modeSelected.equals(MODE_DISPLAY_MAPS) && callbackListProperties.getFragmentDisplayed().equals(MAPS_FRAG)) {
            callbackListProperties.changeMarkerMap(Objects.requireNonNull(listProperties).get(position).getId());
        } else {
            callbackListProperties.showDisplayFragment(position);
        }

        // change selected position
        propertySelected = position;
        listPropertiesFragment.changeSelectedItemInList(listProperties.get(position).getId(),baseActivityListener.getFragmentDisplayed());
    }

    private void setColorItem(@NonNull PropertyViewHolder holder){

        if (listProperties != null) {
            if (listProperties.size()>0) {
                if (propertySelected!=-1) { // if one item selected in the list
                    if (listProperties.get(holder.getAdapterPosition()) != null) {
                        if (listProperties.get(holder.getAdapterPosition()).getSelected() != null) {
                            if (holder.getAdapterPosition() == propertySelected) {
                                setAsSelected(holder,true);
                            } else {
                                setAsSelected(holder,false);
                            }
                        } else {
                            setAsSelected(holder,false);
                        }
                    }
                } else if(holder.getAdapterPosition()==0 && modeDevice.equals(MODE_TABLET) && !baseActivityListener.getFragmentDisplayed().equals(EDIT_FRAG) && !baseActivityListener.getFragmentDisplayed().equals(MAPS_FRAG)){ // if no item selected in the list
                    propertySelected = 0;
                    setAsSelected(holder,true);
                } else {
                    setAsSelected(holder,false);
                }
            }
        }
    }

    private void setAsSelected(@NonNull PropertyViewHolder holder, Boolean isSelected){

        if(isSelected){
            listProperties.get(holder.getAdapterPosition()).setSelected(true);
            holder.getCostTextView().setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.getPropertyLayout().setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            listProperties.get(holder.getAdapterPosition()).setSelected(false);
            holder.getCostTextView().setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.getPropertyLayout().setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public int getItemCount() {
        if(listProperties!=null)
            return listProperties.size();
        else
            return 0;
    }

    // ----------------------------------------------- GETTER and SETTER ------------------------------------

    public void setPropertySelected(int propertySelected) {
        this.propertySelected = propertySelected;
    }

    public void setListProperties(List<Property> listProperties) {
        this.listProperties = listProperties;
    }
}