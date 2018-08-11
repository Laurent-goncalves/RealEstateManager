package com.openclassrooms.realestatemanager.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
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

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("fr"));
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public String create_string_date(int year, int month, int dayOfMonth){

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

    @SuppressLint("ResourceType")
    public static List<String> getInterestPoints(List<String> listTypes, Context context) {

        int index;
        List<String> listPointsInterestTemp = new ArrayList<>();

        // Get list of interests points by type from xml file
        List<String> listTypesRes = new ArrayList<>();
        List<String> listInterestRes = new ArrayList<>();

        List<TypedArray> listTypedArray = ResourceHelper.getMultiTypedArray(context);

        for (TypedArray item : listTypedArray) {
            listTypesRes.add(item.getString(1));
            listInterestRes.add(item.getString(0));
        }

        // For each types, get the point of interest (based on string array in the xml typesPointsInterest)
        for (String type : listTypes) {
            index = getIndexFromList(type, listTypesRes);
            if (index != -1) {
                listPointsInterestTemp.add(listInterestRes.get(index));
            }
        }

        return listPointsInterestTemp;
    }

    private static int getIndexFromList(String type, List<String> list){

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

    public static List<String> removeDuplicates(List<String> listInterestPts){

        List<String> listInterestPtsFinal = new ArrayList<>();

        // create a new collection and add all datas from listInterestPts inside (collection don't allow duplicates)
        Set<String> hs = new HashSet<>(listInterestPts);
        listInterestPtsFinal.clear();
        listInterestPtsFinal.addAll(hs);

        return listInterestPtsFinal;
    }
}
