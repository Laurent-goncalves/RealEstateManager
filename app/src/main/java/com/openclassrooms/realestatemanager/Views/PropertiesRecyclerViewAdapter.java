package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.CallbackListProperties;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;
import java.util.List;
import java.util.Objects;


public class PropertiesRecyclerViewAdapter extends RecyclerView.Adapter<PropertyViewHolder>  {

    private final static String MODE_TABLET = "mode_tablet";
    private static final String MODE_DISPLAY_MAPS = "mode_maps_display";
    private final List<Property> listProperties;
    private Context context;
    private CallbackListProperties callbackListProperties;
    private BaseActivity baseActivity;
    private int propertySelected;
    private String modeSelected;

    public PropertiesRecyclerViewAdapter(List<Property> listProperties, Context context, CallbackListProperties callbackListProperties, BaseActivity baseActivity, String modeSelected) {
        this.listProperties = listProperties;
        this.context=context;
        this.callbackListProperties=callbackListProperties;
        this.baseActivity = baseActivity;
        this.propertySelected=-1;
        this.modeSelected=modeSelected;
    }

    public PropertiesRecyclerViewAdapter(List<Property> listProperties, Context context, int position, CallbackListProperties callbackListProperties, BaseActivity baseActivity, String modeSelected) {
        this.listProperties = listProperties;
        this.context=context;
        this.callbackListProperties=callbackListProperties;
        this.baseActivity = baseActivity;
        this.modeSelected=modeSelected;
        this.propertySelected=position;
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

        if(listProperties!=null && baseActivity!=null)
            holder.configurePropertiesViews(listProperties.get(position), baseActivity);

        holder.propertyLayout.setOnClickListener(v -> {

                if(modeSelected.equals(MODE_DISPLAY_MAPS))
                    callbackListProperties.changeMarkerMap(Objects.requireNonNull(listProperties).get(position).getId());
                else
                    callbackListProperties.showDisplayFragment(position);

                propertySelected = holder.getAdapterPosition();
                if (listProperties != null) {
                    listProperties.get(propertySelected).setSelected(true);
                }

                notifyDataSetChanged();
            }
        );

        setColorItem(holder);
    }

    private void setColorItem(@NonNull PropertyViewHolder holder){

        if (listProperties != null) {
            if (listProperties.size()>0) {
                if (propertySelected!=-1) { // if one item selected in the list
                    if (listProperties.get(holder.getAdapterPosition()) != null) {
                        if (listProperties.get(holder.getAdapterPosition()).getSelected() != null) {
                            if (holder.getAdapterPosition() == propertySelected) {
                                holder.getCostTextView().setTextColor(context.getResources().getColor(R.color.colorWhite));
                                holder.getPropertyLayout().setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            } else {
                                listProperties.get(holder.getAdapterPosition()).setSelected(false);
                                holder.getCostTextView().setTextColor(context.getResources().getColor(R.color.colorAccent));
                                holder.getPropertyLayout().setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                            }
                        } else {
                            listProperties.get(holder.getAdapterPosition()).setSelected(false);
                            holder.getCostTextView().setTextColor(context.getResources().getColor(R.color.colorAccent));
                            holder.getPropertyLayout().setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                        }
                    }
                } else if(holder.getAdapterPosition()==0 && baseActivity.getModeDevice().equals(MODE_TABLET)){ // if no item selected in the list
                    propertySelected = 0;
                    listProperties.get(holder.getAdapterPosition()).setSelected(true);
                    callbackListProperties.showDisplayFragment(holder.getAdapterPosition());
                    holder.getCostTextView().setTextColor(context.getResources().getColor(R.color.colorWhite));
                    holder.getPropertyLayout().setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(listProperties!=null)
            return listProperties.size();
        else
            return 0;
    }
}
