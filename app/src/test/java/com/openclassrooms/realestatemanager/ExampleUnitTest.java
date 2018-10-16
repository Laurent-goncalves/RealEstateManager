package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.Utils.Utils;

import junit.framework.Assert;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {

    @Test
    public void TestConversionMoney() {

        // Test conversion Dollar -> Euro
        Assert.assertEquals(86, Utils.convertDollarToEuro(100));

        // Test conversion Euro -> Dollar
        Assert.assertEquals(117, Utils.convertEuroToDollar(100));
    }

    @Test
    public void TestFormatDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("fr"));
        Calendar cal = Calendar.getInstance();
        Assert.assertEquals(dateFormat.format(cal.getTime()), Utils.getTodayDate());
    }

    @Test
    public void TestComparisonDates() {
        Assert.assertTrue(Utils.isDateInsidePeriod("01/02/2018","31/01/2018","02/02/2018"));
        Assert.assertFalse(Utils.isDateInsidePeriod("03/02/2018","31/01/2018","02/02/2018"));
        Assert.assertFalse(Utils.isDateInsidePeriod("30/01/2018","31/01/2018","02/02/2018"));
    }

}