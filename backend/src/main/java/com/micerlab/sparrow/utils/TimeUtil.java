package com.micerlab.sparrow.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

    /**
     * @return 当前时间戳
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
    
    /**
     * 格式化时间，2019-07-29 13:07:32.000
     * @param timestamp 时间戳
     * @return 格式化的时间字符串
     */
    public static String formatTimeStr(Timestamp timestamp)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(timestamp);
    }
}
