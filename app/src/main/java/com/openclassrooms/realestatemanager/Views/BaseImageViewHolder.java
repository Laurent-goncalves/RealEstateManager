package com.openclassrooms.realestatemanager.Views;

import android.content.Context;
import android.net.Uri;
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
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.PropertyDatabase;
import com.openclassrooms.realestatemanager.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;



public class BaseImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_property) ImageView image;
    @BindView(R.id.edit_icon_symbol) ImageButton editIcon;
    @BindView(R.id.delete_icon_symbol) ImageButton deleteIcon;
    @BindView(R.id.icon_add_photo) Button addPhotoButton;
    @BindView(R.id.title_image_property) TextView titleImage;
    @BindView(R.id.extra_panel) RelativeLayout extraPanel;
    @BindView(R.id.icon_folder_open) ImageButton selectPhotoIcon;
    @BindView(R.id.image_panel) FrameLayout imagePanel;
    @BindView(R.id.buttonImageCancel) Button buttonCancel;
    @BindView(R.id.buttonImageOK) Button buttonOk;
    @BindView(R.id.description_image_edit_text) EditText descripEdit;
    protected View view;
    protected PropertyDatabase database;
    protected Context context;
    protected Uri imageUri;
    protected String description;
    protected ImageProperty imageProperty;
    protected ImagesEditAdapter adapter;
    protected Boolean inEdition;
    protected Boolean changesOngoing;

    public BaseImageViewHolder(View itemView) {
        super(itemView);
        this.view=itemView;
        ButterKnife.bind(this, itemView);
    }

    // ---------------------------------------------------------------------------------------------------
    // ------------------------------------- CONFIGURATION VIEW ------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    public void configureImagesViews(ImageProperty imageProperty, ImagesEditAdapter adapter, Boolean inEdition, Boolean changesOngoing, Context context) {
        // Initialize variables
        this.context=context;
        this.adapter=adapter;
        this.inEdition = inEdition;
        this.changesOngoing=changesOngoing;
        this.database = PropertyDatabase.getInstance(context);
    }

    // ---------------------------------------------------------------------------------------------------
    // -------------------------------- ON CLICK LISTENER BUTTONS ----------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @OnClick(R.id.edit_icon_symbol)
    public void configureEditButton() {
        openExtraPanel();
    }

    @OnTextChanged(value=R.id.description_image_edit_text, callback = AFTER_TEXT_CHANGED)
    public void changeTitleImage(Editable s) {
        if (s.length() == 0){
            titleImage.setVisibility(View.GONE);
            description = null;
        } else {
            titleImage.setVisibility(View.VISIBLE);
            titleImage.setText(s.toString());
            description = s.toString();
        }
    }

    public void setImageURI(Uri imageUri){
        image.setImageURI(imageUri);
        this.imageUri=imageUri;
        addPhotoButton.setVisibility(View.GONE);
    }

    protected void openExtraPanel(){

        // hide the edit icon
        editIcon.setVisibility(View.INVISIBLE);
        editIcon.setEnabled(false);
        deleteIcon.setVisibility(View.INVISIBLE);
        deleteIcon.setEnabled(false);

        // open the panel with fields for changing image
        view.getLayoutParams().width = 1035;
        extraPanel.setVisibility(View.VISIBLE);
        adapter.setPositionEdited(getAdapterPosition());
    }

    protected void closeExtraPanel(){

        // hide the edit icon
        editIcon.setVisibility(View.VISIBLE);
        editIcon.setEnabled(true);
        deleteIcon.setVisibility(View.VISIBLE);
        deleteIcon.setEnabled(true);

        // close the extra panel
        view.getLayoutParams().width = 333;
        extraPanel.setVisibility(View.GONE);
        adapter.setPositionEdited(-1);
    }

    public ImageProperty getImageProperty() {
        return imageProperty;
    }

    public View getView() {
        return view;
    }
}
