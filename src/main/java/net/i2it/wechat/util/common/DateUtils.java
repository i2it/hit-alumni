package net.i2it.wechat.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期对象和字符串相互转换的工具类
 *
 * @author liuming
 * @date 2017/11/16 18:37
 */
public class DateUtils {

    /**
     * 将日期转为指定形式的字符串内容
     *
     * @param dateTime          日期对象
     * @param dateFormatPattern 日期格式
     * @return
     */
    public static String date2Str(Date dateTime, String dateFormatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        return dateFormat.format(dateTime);
    }

    /**
     * 将字符串内容的日期转为日期对象
     *
     * @param dateStr           字符串形式的日期
     * @param dateFormatPattern 字符串形式日期对应的日期格式
     * @return
     */
    public static Date str2Date(String dateStr, String dateFormatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
