package com.douglas.thecarserviceapp.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public static String DateTimeToStringForDatePicker(java.sql.Date date, java.sql.Time time) throws ParseException {
        String dateStr = date.toString();
        String timeStr = time.toString();
        String outputDateStr = "";
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd / MM / yyyy");


        try {
            Date date1 = dateFormat.parse(dateStr);
            outputDateStr = outputFormat.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date time1 = inputTimeFormat.parse(timeStr);
            outputDateStr += " - " + timeFormat.format(time1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }
}
