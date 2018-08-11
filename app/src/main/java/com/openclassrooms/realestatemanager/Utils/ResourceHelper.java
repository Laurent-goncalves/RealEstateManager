package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import com.openclassrooms.realestatemanager.R;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResourceHelper {

    public static List<TypedArray> getMultiTypedArray(Context context) {
        List<TypedArray> array = new ArrayList<>();

        try {
            Class<R.array> res = R.array.class;
            Field field;
            int counter = 1;

            do {
                field = res.getField("pointinterest_" + counter);
                array.add(context.getResources().obtainTypedArray(field.getInt(null)));
                counter++;
            } while (field != null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return array;
        }
    }
}
