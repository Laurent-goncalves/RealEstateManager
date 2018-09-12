package com.openclassrooms.realestatemanager.Models;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.Controllers.Fragments.EditFragment;
import com.openclassrooms.realestatemanager.Controllers.Fragments.SearchFragment;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarDialog extends DialogFragment {

    @BindView(R.id.calendar_view_dialog) CalendarView calendarView;
    private static final String TYPE_DATE = "type_date";
    private static final String PUBLISH_DATE = "publish_date";
    private static final String PUBLISH_DATE_START = "publish_date_start";
    private static final String PUBLISH_DATE_END = "publish_date_end";

    public CalendarDialog() {
    }

    public static CalendarDialog newInstance(String typeDate) {

        CalendarDialog calendarDialog = new CalendarDialog();

        Bundle args = new Bundle();
        args.putString(TYPE_DATE, typeDate);
        calendarDialog.setArguments(args);

        return calendarDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar_dialog, container, false);
        ButterKnife.bind(this,v);
        configureCalendarView(getArguments().getString(TYPE_DATE));
        return v;
    }

    private void configureCalendarView(String dateType) {

        calendarView.setOnDateChangeListener(((view, year, month, dayOfMonth) -> {
                    String date = Utils.create_string_date(year, month, dayOfMonth);


                    if(dateType.equals(PUBLISH_DATE)) {
                        EditFragment editFragment =(EditFragment) getTargetFragment();
                        editFragment.getDatePublish().setText(date); // change date selected into string
                    } else if(dateType.equals(TYPE_DATE)) {
                        EditFragment editFragment =(EditFragment) getTargetFragment();
                        editFragment.getDateSold().setText(date); // change date selected into string
                    } else if (dateType.equals(PUBLISH_DATE_START)){
                        SearchFragment searchFragment =(SearchFragment) getTargetFragment();
                        searchFragment.getStartPublishView().setText(date); // change date selected into string
                    } else if (dateType.equals(PUBLISH_DATE_END)){
                        SearchFragment searchFragment =(SearchFragment) getTargetFragment();
                        searchFragment.getEndPublishView().setText(date); // change date selected into string
                    }

                    this.dismiss();
                }));
    }

    public CalendarView getCalendarView() {
        return calendarView;
    }
}
