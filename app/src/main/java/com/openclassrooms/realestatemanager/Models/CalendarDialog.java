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
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarDialog extends DialogFragment {

    @BindView(R.id.calendar_view_dialog) CalendarView calendarView;
    private static final String TYPE_DATE = "type_date";
    private static final String PUBLISH_DATE = "publish_date";

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

                    EditFragment editFragment =(EditFragment) getTargetFragment();

                    if(dateType.equals(PUBLISH_DATE))
                        editFragment.getDatePublish().setText(date); // change date selected into string
                    else
                        editFragment.getDateSold().setText(date); // change date selected into string

                    this.dismiss();
                }));
    }

    public CalendarView getCalendarView() {
        return calendarView;
    }
}