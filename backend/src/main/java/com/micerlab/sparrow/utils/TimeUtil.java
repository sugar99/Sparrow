package com.micerlab.sparrow.utils;

import java.sql.Timestamp;
import java.util.Date;

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

}
