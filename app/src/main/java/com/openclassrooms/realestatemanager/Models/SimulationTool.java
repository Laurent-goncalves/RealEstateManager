package com.openclassrooms.realestatemanager.Models;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class SimulationTool extends DialogFragment {

    @BindView(R.id.edittext_contribution) EditText contribution;
    @BindView(R.id.edittext_interest_rate) EditText rate;
    @BindView(R.id.edittext_duration) EditText duration;
    @BindView(R.id.button_calculation) TextView result;
    private Property property;
    private static final String PROPERTY_JSON = "property_json";

    public static SimulationTool newInstance(Property property) {

        SimulationTool simulationTool = new SimulationTool();

        Gson gson = new Gson();
        String json = gson.toJson(property);
        Bundle args = new Bundle();
        args.putString(PROPERTY_JSON, json);
        simulationTool.setArguments(args);

        return simulationTool;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.simulation_calculation, container, false);

        ButterKnife.bind(this,v);

        Gson gson = new Gson();
        String json = getArguments().getString(PROPERTY_JSON,null);
        Type propType = new TypeToken<Property>(){}.getType();
        property = gson.fromJson(json,propType);

        changeToDisabled();

        return v;
    }

    @OnTextChanged(R.id.edittext_contribution)
    public void onChangeContrib(){
        resetButtonCalculation();
        checkCalculation();
    }

    @OnTextChanged(R.id.edittext_interest_rate)
    public void onChangeRate(){
        resetButtonCalculation();
        checkCalculation();
    }

    @OnTextChanged(R.id.edittext_duration)
    public void onChangeDuration(){
        resetButtonCalculation();
        checkCalculation();
    }

    private void resetButtonCalculation() {
        result.setText(getActivity().getApplication().getApplicationContext()
                .getResources().getString(R.string.calculation));
    }

    private void checkCalculation() {

        if(contribution.getText()!=null && rate.getText()!=null && duration.getText()!=null) {
            if (!contribution.getText().toString().equals("") && !rate.getText().toString().equals("") && !duration.getText().toString().equals("")) {
                changeToEnabled();
            } else {
                changeToDisabled();
            }
        } else
            changeToDisabled();
    }

    private void changeToDisabled(){

        // change text color to grey
        result.setTextColor(getActivity().getApplication().getApplicationContext()
                .getResources().getColor(R.color.colorPrimary));

        // change background
        result.setBackground(ContextCompat.getDrawable(getActivity().getApplication().getApplicationContext(),
                R.drawable.background_result));
    }

    private void changeToEnabled(){

        // change text color to pink
        result.setTextColor(getActivity().getApplication().getApplicationContext()
                .getResources().getColor(R.color.colorAccent));

        // change background
        result.setBackground(ContextCompat.getDrawable(getActivity().getApplication().getApplicationContext(),
                R.drawable.background_edittext));
    }

    @OnClick(R.id.button_calculation)
    public void calculateMonthlyPayment(){

        if(contribution.getText()!=null && rate.getText()!=null && duration.getText()!=null){
            if(!contribution.getText().toString().equals("") && !rate.getText().toString().equals("") && !duration.getText().toString().equals("")){

                Double montlyRate = Utils.calculateMonthlyPayment(property.getPrice(),
                        Double.parseDouble(duration.getText().toString()),
                        Double.parseDouble(rate.getText().toString()),
                        Double.parseDouble(contribution.getText().toString()));

                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = getActivity().getApplication().getApplicationContext()
                        .getResources().getString(R.string.montly_payment) + "\n" + formatter.format(montlyRate);

                result.setText(moneyString);
            }
        }
    }
}
