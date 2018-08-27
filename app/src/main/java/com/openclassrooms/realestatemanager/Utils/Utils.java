package com.openclassrooms.realestatemanager.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.annotation.StyleableRes;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.openclassrooms.realestatemanager.Controllers.Activities.MainActivity;
import com.openclassrooms.realestatemanager.Models.ImageProperty;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;

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

            if(datetest.compareTo(datesup) <= 0 && datetest.compareTo(dateinf) >= 0)
                answer = true;
            else
                answer = false;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return answer;
    }

    public static Boolean isLocationInsideBounds(LatLng searchLoc, LatLng propertyLoc, Double radius){
        return SphericalUtil.computeDistanceBetween(searchLoc, propertyLoc) <= radius;
    }

    public static List<String> getInterestPoints(List<String> listTypes, Context context) {

        int index;
        @StyleableRes int indexRes = 1;

        List<String> listPointsInterestTemp = new ArrayList<>();

        // Get list of interests points by type from xml file
        List<String> listTypesRes = new ArrayList<>();
        List<String> listInterestRes = new ArrayList<>();

        List<TypedArray> listTypedArray = ResourceHelper.getMultiTypedArray(context);

        for (TypedArray item : listTypedArray) {
            listTypesRes.add(item.getString(indexRes));
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

    public static String removeHooksFromString(String text){

        String newText = null;

        if(text!=null) {
            newText = text.replace("[", "");
            newText = newText.replace("]", "");
        }

        return newText;
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

    public static String getRealPathFromURI(MainActivity mainActivity, Uri uri) {

        String realPath=null;

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mainActivity.getContentResolver().query(uri, projection, null, null, null);

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

    public static List<String> removeDuplicates(List<String> listInterestPts){

        List<String> listInterestPtsFinal = new ArrayList<>();

        // create a new collection and add all datas from listInterestPts inside (collection don't allow duplicates)
        Set<String> hs = new HashSet<>(listInterestPts);
        listInterestPtsFinal.clear();
        listInterestPtsFinal.addAll(hs);

        return listInterestPtsFinal;
    }

    public static Double calculateMonthlyPayment(Double propertyPrice, Double duration, Double interestRate, Double contribution){
        return (propertyPrice-contribution)*(interestRate/1200)/(1 - Math.pow(1+interestRate/1200, -12*duration));
    }

    public static void setImageBitmapInView(String imagePath, ImageView imageView, MainActivity mainActivity){

        try {
            Bitmap bitmap;
            File f= new File(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(mainActivity, galleryPermissions)) {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
                imageView.setImageBitmap(bitmap);

            } else {
                EasyPermissions.requestPermissions(mainActivity, "Access for storage",
                        101, galleryPermissions);
            }

        } catch (Exception e) {
            System.out.println("eee exception = " + e.toString());
        }
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
}
