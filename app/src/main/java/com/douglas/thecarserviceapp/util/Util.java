package com.douglas.thecarserviceapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Util {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static java.sql.Date convertDate(String date) throws ParseException {
        java.util.Date parsed = dateFormat.parse(date);
        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
        return sqlDate;
    }

    public static java.sql.Time convertTime(String time) throws ParseException {
        java.util.Date parsed = timeFormat.parse(time);
        java.sql.Time sqlTime = new java.sql.Time(parsed.getTime());
        return sqlTime;
    }
}
