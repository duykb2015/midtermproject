package vn.stu.midtermproject.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    static SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyy hh:mm aa");
    static SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    static SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm aa");

    public static String formatDataTime(Date date) {
        return sdfDateTime.format(date);
    }

    public static String formatDate(Date date) {
        return sdfDate.format(date);
    }

    public static String formatTime(Date date) {
        return sdfTime.format(date);
    }
}
