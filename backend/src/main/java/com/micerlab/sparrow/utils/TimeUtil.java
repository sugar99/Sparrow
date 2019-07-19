package com.micerlab.sparrow.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

    /**
     * 获取当前时间，格式 2019-01-02 12:21:32
     * @return time
     */
    public static Timestamp currentTime() {
        Date currentTime = new Date();
        Timestamp timestamp = new Timestamp(currentTime.getTime());
        return timestamp;
    }
    
    public static String currentTimeStr()
    {
//        String timeStr = currentTime().toString();
//        timeStr = timeStr.substring(0, timeStr.lastIndexOf("."));
//        return timeStr;
        return formatTimeStr(currentTime());
    }
    
    public static String formatTimeStr(Timestamp timestamp)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        return dateFormat.format(timestamp);
    }
}
