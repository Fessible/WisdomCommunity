package com.example.com.wisdomcommunity.util;

import java.text.SimpleDateFormat;
import java.util.IllegalFormatException;
import java.util.Locale;

/**
 * 时间转换工具
 * Created by rhm on 2018/2/26.
 */

public class DateUtils {
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String formatYMDHMS(long dateStr) {
        return format(YYYY_MM_DD_HH_MM_SS, dateStr);
    }

    public static String format(String pattern, long dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINESE);
        try {
            return simpleDateFormat.format(dateStr);
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        }
        return "";
    }
}
