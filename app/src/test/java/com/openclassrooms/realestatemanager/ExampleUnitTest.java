package com.openclassrooms.realestatemanager;

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
        Assert.assertEquals(1, Utils.convertDollarToEuro(1));

        // Test conversion Euro -> Dollar
        Assert.assertEquals(1, Utils.convertEuroToDollar(1));
    }

    @Test
    public void TestFormatDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("fr"));
        Calendar cal = Calendar.getInstance();
        Assert.assertEquals(dateFormat.format(cal.getTime()), Utils.getTodayDate());
    }


}