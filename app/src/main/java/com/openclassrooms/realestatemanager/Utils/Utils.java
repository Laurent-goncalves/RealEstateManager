package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.widget.ScrollView;

import com.openclassrooms.realestatemanager.Controllers.Activities.BaseActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;
import com.openclassrooms.realestatemanager.Models.Property;
import com.openclassrooms.realestatemanager.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private final static String MODE_TABLET = "mode_tablet";

    // -------------------------------------------------------------------------------------------------
    // --------------------------------------- CURRENCY ------------------------------------------------
    // -------------------------------------------------------------------------------------------------

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.86);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param euros
     * @return
     */
    public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros * 1.17);
    }

    public static Double calculateMonthlyPayment(Double propertyPrice, Double duration, Double interestRate, Double contribution){
        return (propertyPrice-contribution)*(interestRate/1200)/(1 - Math.pow(1+interestRate/1200, -12*duration));
    }

    // -------------------------------------------------------------------------------------------------
    // --------------------------------------- INTERNET ------------------------------------------------
    // -------------------------------------------------------------------------------------------------

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */

    public static Boolean isWifiEnabled(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi != null && wifi.isWifiEnabled();
    }

    public static Boolean isNetworkEnabled(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Boolean isInternetAvailable(Context context){
        return isNetworkEnabled(context) || isWifiEnabled(context);
    }

    // -------------------------------------------------------------------------------------------------
    // --------------------------------------- DATES ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("fr"));
        return dateFormat.format(new Date());
    }

    public static String create_string_date(int year, int month, int dayOfMonth){

        String Day;
        int Month = month + 1;
        String new_month;

        if(dayOfMonth<10)
            Day = "0" + dayOfMonth;
        else
            Day = String.valueOf(dayOfMonth);

        if(Month<10)
            new_month = "0" + Month;
        else
            new_month = String.valueOf(Month);

        return Day + "/" + new_month + "/" + year;
    }

    public static Boolean isDateInsidePeriod(String dateTest, String dateInf, String dateSup){

        Boolean answer = true;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("fr"));

        try {
            Date datetest = dateFormat.parse(dateTest);
            Date dateinf = dateFormat.parse(dateInf);
            Date datesup = dateFormat.parse(dateSup);

            answer = datetest.compareTo(datesup) <= 0 && datetest.compareTo(dateinf) >= 0;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return answer;
    }

    // -------------------------------------------------------------------------------------------------
    // ---------------------------- GET ITEM OR INDEX FROM LIST ----------------------------------------
    // -------------------------------------------------------------------------------------------------

    public static int getIndexFromList(String type, List<String> list){

        int index = 0;

        if(type!=null){
            for(String item : list){
                if(type.equals(item))
                    return index;
                else
                    index++;
            }
        }

        return -1; // no value found
    }

    public static Property getPropertyFromList(int idProp, List<Property> list){

        if(list!=null && idProp!=-1){
            for(Property property : list){
                if(property.getId() == idProp)
                    return property;
            }
        }

        return null; // no value found
    }

    public static int getIndexPropertyFromList(Property property, List<Property> list){

        int index = 0;

        if(list!=null && property!=null){
            for(Property prop : list){
                if(prop.getId() == property.getId())
                    return index;
                else
                    index++;
            }
        }

        return -1; // no value found
    }

    public static int getIndexPropertyFromList(int idProperty, List<Property> list){

        int index = 0;

        if(list!=null && idProperty!=-1){
            for(Property prop : list){
                if(prop.getId() == idProperty)
                    return index;
                else
                    index++;
            }
        }

        return -1; // no value found
    }

    public static Boolean isInTheList(ImageProperty image, List<ImageProperty> list){

        if(image != null){
            for(ImageProperty img : list){
                if(img!=null){
                    if(img.getId()==image.getId()){
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------------
    // -------------------------------------- IMAGES ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------

    public static String getRealPathFromURI(BaseActivity baseActivity, Uri uri) {

        String realPath=null;

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = baseActivity.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if(cursor.moveToFirst()){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                realPath = cursor.getString(column_index);
                cursor.moveToFirst();
            }
            cursor.close();
        }

        return realPath;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    // -------------------------------------------------------------------------------------------------
    // -------------------------------------- OTHERS ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------

    public static String removeHooksFromString(String text){

        String newText = null;

        if(text!=null) {
            newText = text.replace("[", "");
            newText = newText.replace("]", "");
        }

        return newText;
    }

    public static void colorFragmentList(String color, String modeDevice, BaseActivity baseActivity){

        ScrollView viewToColor;

        // Find fragment to color
        if(modeDevice.equals(MODE_TABLET))
            viewToColor = baseActivity.findViewById(R.id.fragment_list_layout);
        else
            viewToColor = baseActivity.findViewById(R.id.fragment_layout);

        // Color the fragment
        if(color.equals("GRAY")){
            viewToColor.setBackgroundColor(baseActivity.getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
        } else {
            viewToColor.setBackgroundColor(Color.WHITE);
        }
    }
}
