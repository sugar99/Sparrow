package com.micerlab.sparrow.utils;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class TimeUtilTest
{
    
    @Test
    public void testFormatTimeStr()
    {
        String pattern1 = "yyyy-MM-dd HH:mm:ss.SSS";
        SimpleDateFormat format1 = new SimpleDateFormat(pattern1);
        
        String pattern2 = "yyyy-MM-dd hh:mm:ss.SSS";
        SimpleDateFormat format2 = new SimpleDateFormat(pattern2);
    
        Timestamp timestamp1 = new Timestamp((new Date()).getTime());
        Calendar time = new GregorianCalendar(2019, 7, 29,13,01, 20);
        Timestamp timestamp2 = new Timestamp(
                time.getTimeInMillis());
        
        System.out.println(pattern1 + " : " + format1.format(timestamp1));
        System.out.println(pattern2 + " : " + format2.format(timestamp1));
        
        System.out.println(pattern1 + " : " + format1.format(timestamp2));
        System.out.println(pattern2 + " : " + format2.format(timestamp2));
    }
}