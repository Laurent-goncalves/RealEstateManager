package com.openclassrooms.realestatemanager.Utils;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.Models.CalendarDialog;
import com.openclassrooms.realestatemanager.Models.SearchAddress;
import com.openclassrooms.realestatemanager.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConfigureSearchFragment {

    @BindView(R.id.seekbar_radius) SeekBar seekbarRadius;
    @BindView(R.id.radius_value) TextView radiusView;
    @BindView(R.id.start_date_publish_selected_search) TextView startPublishView;
    @BindView(R.id.end_date_publish_selected_search) TextView endPublishView;
    @BindView(R.id.start_icon_expand_search) ImageButton iconExpandStart;
    @BindView(R.id.end_icon_expand_search) ImageButton iconExpandEnd;
    @BindView(R.id.relativelayout_rooms) RelativeLayout layoutRooms;
    private Context context;
    private SearchFragment searchFragment;
    private static final String PUBLISH_DATE_START = "publish_date_start";
    private static final String PUBLISH_DATE_END = "publish_date_end";
    private ImageButton buttonPlus;
    private ImageButton buttonLess;
    private int roomNbMin;
    private TextView nbRoomsView;
    private int radiusVal;


    public ConfigureSearchFragment(View view, Context context, SearchFragment searchFragment) {
        ButterKnife.bind(this, view);
        this.context = context;
        this.searchFragment = searchFragment;
        configureSearchFragment();
    }

    private void configureSearchFragment(){
        roomNbMin = 0;
        LinearLayout buttonSelect = layoutRooms.findViewById(R.id.room_number_selector_search);
        nbRoomsView = buttonSelect.findViewById(R.id.text_selection);
        buttonPlus = buttonSelect.findViewById(R.id.plus_button);
        buttonLess = buttonSelect.findViewById(R.id.less_button);

        buttonLess.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        nbRoomsView.setText(context.getResources().getString(R.string.any));

        configureSeekBarForRadius();
        configureDateSelector();
        new SearchAddress(searchFragment,context);
    }

    private void configureDateSelector(){

        startPublishView.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_START));
        iconExpandStart.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_START));

        endPublishView.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_END));
        iconExpandEnd.setOnClickListener(v -> showCalendarView(PUBLISH_DATE_END));
    }

    private void showCalendarView(String dateType){

        FragmentTransaction ft = searchFragment.getFragmentManager().beginTransaction();
        Fragment prev = searchFragment.getFragmentManager().findFragmentByTag("calendarDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        CalendarDialog calendarDialog = CalendarDialog.newInstance(dateType);
        calendarDialog.setTargetFragment(searchFragment,0);
        calendarDialog.show(ft, "calendarDialog");
    }

    private void configureSeekBarForRadius(){

        // Initialize seekbar (value = 1000m)
        radiusVal = Integer.parseInt(context.getResources().getString(R.string.radius));
        String text = String.valueOf(radiusVal) + " m";
        seekbarRadius.setProgress(radiusVal);
        radiusView.setText(text);

        // set on change listener
        seekbarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = String.valueOf(progress) + " m";
                radiusVal = progress;
                radiusView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @OnClick(R.id.plus_button)
    public void onClickListenerButtonPlus() {
        if(roomNbMin<10) {
            roomNbMin++;
            if(roomNbMin==0)
                nbRoomsView.setText(context.getResources().getString(R.string.any));
            else {
                String text = "+" + String.valueOf(roomNbMin);
                nbRoomsView.setText(text);
            }
        }
        buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonPlus.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    @OnClick(R.id.less_button)
    public void onClickListenerButtonLess() {
        if(roomNbMin>=1) {
            roomNbMin--;
            if(roomNbMin==0)
                nbRoomsView.setText(context.getResources().getString(R.string.any));
            else {
                String text = "+" + String.valueOf(roomNbMin);
                nbRoomsView.setText(text);
            }
        }

        buttonLess.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> buttonLess.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN), 100);
    }

    @OnClick(R.id.buttonSearchCancel)
    public void cancel(){
        searchFragment.getSearchActivity().launchMainActivity();
    }

    @OnClick(R.id.buttonSearch)
    public void search(){
        searchFragment.launchSearchProperties();
    }

    public int getRoomNbMin() {
        return roomNbMin;
    }

    public int getRadiusVal() {
        return radiusVal;
    }
}
