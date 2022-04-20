package com.hair.management.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 */
public class LocalDateTimeUtil {

    public static final String DATE_TIME_FORMATTER="yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMATTER="yyyy-MM-dd";

    public static String getDayStart(String date){
        LocalDate now = LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        return now.atStartOfDay().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER));
    }
    public static String getDayEnd(String date){
        LocalDate now = LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        return now.atTime(23,59,59).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER));
    }
}
