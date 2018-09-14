package com.openclassrooms.realestatemanager.Controllers.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.openclassrooms.realestatemanager.Models.ToolbarManager;
import com.openclassrooms.realestatemanager.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.ButterKnife;


public class MainActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        modeSelected = MODE_DISPLAY;

        // Configure toolbar
        setModeDevice();
        toolbarManager = new ToolbarManager(this);
        toolbarManager.configureNavigationDrawer(this);

        idProperty=-1;

        //configureAndShowEditFragment(0);
        configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
    }

    @Override
    public void onBackPressed(){
        if(fragmentDisplayed.equals(EDIT_FRAG))
            configureAndShowDisplayFragment(modeSelected, idProperty);
        else if(modeDevice.equals(MODE_PHONE))
            configureAndShowListPropertiesFragment(MODE_DISPLAY, null);
    }

    public void setMobileDataState2(boolean mobileDataEnabled)
    {
        try{
            ConnectivityManager dataManager;
            dataManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
            dataMtd.invoke(dataManager, mobileDataEnabled);
        }catch(Exception ex){
            System.out.println("eee " + ex.toString());
            //Error Code Write Here
        }
    }

    public void setMobileDataState(boolean mobileDataEnabled)
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception ex)
        {
            //Log.e(TAG, "Error setting mobile data state", ex);
        }
    }


    public void setMobileDataEnabled(boolean enabled) {
        final ConnectivityManager conman =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(
                    iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                    .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}



        /*PropertyDatabase db = PropertyDatabase.getInstance(getApplicationContext());

        db.imageDao().deleteAllImage();
        db.propertyDao().deleteAllProperties();*/