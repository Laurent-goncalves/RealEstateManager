package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Models.BaseActivityListener;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.UtilsBaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;



public class BaseImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_property) ImageView image;
    @BindView(R.id.edit_icon_symbol) ImageButton editIcon;
    @BindView(R.id.delete_icon_symbol) ImageButton deleteIcon;
    @BindView(R.id.icon_add_photo) RelativeLayout addPhotoButton;
    @BindView(R.id.title_image_property_edit) TextView titleImage;
    @BindView(R.id.extra_panel) RelativeLayout extraPanel;
    @BindView(R.id.icon_folder_open) ImageButton selectPhotoIcon;
    @BindView(R.id.image_panel) FrameLayout imagePanel;
    @BindView(R.id.buttonImageCancel) Button buttonCancel;
    @BindView(R.id.buttonImageOK) Button buttonOk;
    @BindView(R.id.description_image_edit_text) EditText descripEdit;
    protected View view;
    protected PropertyDatabase database;
    protected Context context;
    protected ImageProperty imageProperty;
    protected List<ImageProperty> listImages;
    protected ImagesEditAdapter adapter;
    protected BaseActivityListener baseActivityListener;

    public BaseImageViewHolder(View itemView) {
        super(itemView);
        this.view=itemView;
        ButterKnife.bind(this, itemView);
    }

    // ---------------------------------------------------------------------------------------------------
    // ------------------------------------- CONFIGURATION VIEW ------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    public void configureImagesViews(List<ImageProperty> listImages, int position, ImagesEditAdapter adapter, Context context, BaseActivityListener baseActivityListener) {
        // Initialize variables
        this.listImages=listImages;
        this.imageProperty = listImages.get(position);
        this.context=context;
        this.adapter=adapter;
        this.database = PropertyDatabase.getInstance(context);
        this.baseActivityListener = baseActivityListener;

        // if the image has no Edition mode, set it to false
        if(imageProperty.getInEdition()==null){
            imageProperty.setInEdition(false);
        }

        // if the image is in Edition mode, open the extra panel
        if (imageProperty.getInEdition()){
            openExtraPanel();
        } else {
            closeExtraPanel();
        }

        // add icon add a photo if the imagePath is null
        if(imageProperty.getImagePath()==null){
            addPhotoButton.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            addPhotoButton.setVisibility(View.GONE);
        }

        // insert title under the image
        if(imageProperty.getInEdition()){
            if(imageProperty.getDescription()!=null){
                titleImage.setVisibility(View.VISIBLE);
                titleImage.setText(imageProperty.getDescription());
                descripEdit.setText(imageProperty.getDescription());
            } else {
                titleImage.setVisibility(View.GONE);
                descripEdit.setText(null);
            }
        } else {
            if(imageProperty.getDescription()==null)
                titleImage.setVisibility(View.GONE);
            else {
                titleImage.setVisibility(View.VISIBLE);
                titleImage.setText(imageProperty.getDescription());
            }
        }
    }

    public void setExtraImage(String imagePath){
        UtilsBaseActivity.setImage(imagePath, image, baseActivityListener.getBaseActivity());
        image.setVisibility(View.VISIBLE);
        addPhotoButton.setVisibility(View.GONE);
        imageProperty.setImagePath(imagePath);
    }

    // ---------------------------------------------------------------------------------------------------
    // -------------------------------- ON CLICK LISTENER BUTTONS ----------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @OnClick(R.id.edit_icon_symbol)
    public void configureEditButton() {
        openExtraPanel();
        adapter.notifyDataSetChanged();
    }

    @OnTextChanged(value=R.id.description_image_edit_text, callback = AFTER_TEXT_CHANGED)
    public void changeTitleImage(Editable s) {
        if (s.length() == 0){
            titleImage.setVisibility(View.GONE);
            imageProperty.setDescription(null);
        } else {
            titleImage.setVisibility(View.VISIBLE);
            titleImage.setText(s.toString());
            imageProperty.setDescription(s.toString());
        }
    }

    protected void openExtraPanel(){

        imageProperty.setInEdition(true);

        // hide the edit icon
        editIcon.setVisibility(View.INVISIBLE);
        editIcon.setEnabled(false);
        deleteIcon.setVisibility(View.INVISIBLE);
        deleteIcon.setEnabled(false);

        // open the panel
        view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.extra_panel_width_expanded);
        extraPanel.setVisibility(View.VISIBLE);
    }

    protected void closeExtraPanel(){

        imageProperty.setInEdition(false);

        // hide the edit icon
        if(imageProperty.getImagePath()!=null){
            editIcon.setVisibility(View.VISIBLE);
            editIcon.setEnabled(true);
            deleteIcon.setVisibility(View.VISIBLE);
            deleteIcon.setEnabled(true);
        } else {
            editIcon.setVisibility(View.INVISIBLE);
            editIcon.setEnabled(false);
            deleteIcon.setVisibility(View.INVISIBLE);
            deleteIcon.setEnabled(false);
            addPhotoButton.setVisibility(View.VISIBLE);
            addPhotoButton.setEnabled(true);
        }

        // close the extra panel
        view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.extra_panel_width_reduced);
        extraPanel.setVisibility(View.GONE);
    }

    protected Boolean isAnImageInEdition(List<ImageProperty> listImages){

        Boolean answer = false;

        if(listImages!=null){
            if(listImages.size()>0){
                for(ImageProperty img : listImages){
                    if(img.getInEdition()!=null){
                        if(img.getInEdition()) {
                            answer = true;
                            break;
                        }
                    }
                }
            }
        }
        return answer;
    }

    public View getView() {
        return view;
    }
}


/*
        ObjectAnimator animX = ObjectAnimator.ofFloat(extraPanel, View.TRANSLATION_X,
                 extraPanel.getWidth(), 0);

        animX.setDuration(1000);
        animX.start();
*/


        /*ObjectAnimator animX = ObjectAnimator.ofFloat(extraPanel, View.TRANSLATION_X,
                -1*extraPanel.getWidth(), 0);

animX.setDuration(1000);
        animX.start();*/