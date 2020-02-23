package com.lavector.collector.crawler.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.selector.PlainText;

import java.text.ParseException;
import java.util.Date;

/**
 * Created on 2018/1/2.
 *
 * @author zeng.zhao
 */
public class TimeUtils {

    public static final String MINUTE = "分";
    public static final String HOUR = "时";
    public static final String DAY = "天";
    public static final String WEEK = "周";
    public static final String MONTH = "月";
    public static final String YEAR = "年";
    public static final String ZUO = "昨";
    public static final String QIAN = "前";


    public static String[] parsePatterns = {"yy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy年MM月dd日", " yyyy年MM月dd日"};

    /**
     *
     * @param 1990-01-04 12:12:11
     * @return Thu Jan 04 12:12:11 CST 1990
     */
    public static Date fromStringToDate(String timeCreatedString) {
        Date timeCreated = null;
        try {
            if (timeCreatedString.matches("\\d+-\\d+")) {
                timeCreatedString = new Date().getYear() - 100 + "-" + timeCreatedString;
            }
            timeCreated = DateUtils.parseDate(timeCreatedString.trim(), parsePatterns);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeCreated;
    }


    /**
     * 4分钟前  3小时前    昨天   前天  一天前  一周前 ...
     * @param stringDate
     * @return
     */
    public static Date timeProcess(String stringDate) {
        if (!RegexUtil.isMatch(".*[\\u4e00-\\u9fa5].*", stringDate)) {
            return fromStringToDate(stringDate);
        }
        Date dateTime = new Date();
        String time = null;
        int number = 0;
        try {
            if (stringDate.contains(MINUTE)) {
                String stringSub = stringSub(stringDate);
                number = Integer.parseInt(stringSub);
                time = MINUTE;
            } else if (stringDate.contains(HOUR)) {
                String stringSub = stringSub(stringDate);
                number = Integer.parseInt(stringSub);
                time = HOUR;
            } else if (stringDate.contains(DAY)) {
                if(stringDate.contains(ZUO)){
                    number = 1;
                }else if(stringDate.contains(QIAN) && stringDate.indexOf(QIAN) < stringDate.indexOf(DAY)){
                    number = 2;
                }else{
                    String stringSub = stringSub(stringDate);
                    number = Integer.parseInt(stringSub);
                }
                time = DAY;
            } else if(stringDate.contains(WEEK)){
                String stringSub = stringSub(stringDate);
                number = Integer.parseInt(stringSub);
                time = WEEK;
            }else if(stringDate.contains(MONTH)){
                String stringSub = stringSub(stringDate);
                number = Integer.parseInt(stringSub);
                time = MONTH;
            }else {
                String stringSub = stringSub(stringDate);
                number = Integer.parseInt(stringSub);
                time = YEAR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (number > 0 && !StringUtils.isEmpty(time)) {
            Date date = new Date();
            switch (time) {
                case MINUTE:
                    dateTime = DateUtils.addMinutes(date, -number);
                    break;
                case HOUR:
                    dateTime = DateUtils.addHours(date, -number);
                    break;
                case DAY:
                    dateTime = DateUtils.addDays(date, -number);
                    break;
                case WEEK:
                    dateTime = DateUtils.addWeeks(date, -number);
                    break;
                case MONTH:
                    dateTime = DateUtils.addMonths(date, -number);
                    break;
                case YEAR:
                    dateTime = DateUtils.addYears(date, -number);
                    break;
            }
        }
        return dateTime;
    }


    private static String stringSub(String model) {
        return new PlainText(model).regex("(\\d+)\\D.*").toString();
    }
}
