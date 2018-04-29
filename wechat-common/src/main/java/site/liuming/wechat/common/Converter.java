package site.liuming.wechat.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换器，转要是一些类型转换
 */
public class Converter {

    public static String convertDateToString(Date date, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        return sdf.format(date);
    }

    public static Date convertStringToDate(String dateString, String datePattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        return sdf.parse(dateString);
    }
}
