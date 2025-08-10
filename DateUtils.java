package com.movies.player.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class DateUtils {
    public static Date parseDateFromMMddEEEHHmm(String str) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd(EEE) HH:mm", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        try {
            return simpleDateFormat.parse(getYearFromDate(new Date()) + "." + str);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String getTimeDifferenceMessage(Date date) {
        long time = date.getTime() - System.currentTimeMillis();
        if (time <= 0) {
            return "만료시간이 이미 지났습니다.";
        }
        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        if (days >= 4) {
            return null;
        }
        if (days >= 1) {
            return "만료일까지 " + days + "일 남았습니다.";
        }
        return hours >= 1 ? "만료시간까지 " + hours + "시간 남았습니다." : "만료시간까지 1시간 이내입니다.";
    }

    public static Date parseDateFromMMddHHmm(String str) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd HH:mm", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        try {
            return simpleDateFormat.parse(getYearFromDate(new Date()) + "." + str);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date parseDateFromyyyyMMdd(String str) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        try {
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String parseString(Date date) {
        if (date == null) {
            return "null";
        }
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd(E) HH:mm", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    public static String parseStringyyyyMMddHHmmss(Date date) {
        if (date == null) {
            return "null";
        }
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd(E) HH:mm:ss", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    public static String parseStringyyyyMMdd(Date date) {
        if (date == null) {
            return "null";
        }
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    public static String parseStringMMddHHmmss(Date date) {
        if (date == null) {
            return "null";
        }
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd HH:mm:ss", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    public static String parseStringMMddHHmm(Date date) {
        if (date == null) {
            return "null";
        }
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd HH:mm", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    public static String parseStringMMdd(Date date) {
        if (date == null) {
            return "null";
        }
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd", Locale.KOREA);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    public static int getYearFromDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of("Asia/Seoul")).getYear();
    }

    public static int getMonthFromDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of("Asia/Seoul")).getMonthValue();
    }

    public static int getDayOfMonthFromDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of("Asia/Seoul")).getDayOfMonth();
    }

    public static int getHourFromDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of("Asia/Seoul")).getHour();
    }

    public static int getMinuteFromDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of("Asia/Seoul")).getMinute();
    }

    public static int getMinutesOfDay(Date date) {
        if (date == null) {
            return 0;
        }
        return (getHourFromDate(date) * 60) + getMinuteFromDate(date);
    }

    public static int getMinutesOfDay(String str) {
        try {
            return (Integer.parseInt(str.substring(0, 2)) * 60) + Integer.parseInt(str.substring(3, 5));
        } catch (Exception unused) {
            return 0;
        }
    }

    public static int getMinutesDiffOfDay(String str, Date date) {
        return getMinutesOfDay(str) - getMinutesOfDay(date);
    }

    public static long getMinutesDiff(Date date, Date date2) {
        return getSecondsDiff(date, date2) / 60;
    }

    public static long getSecondsDiff(Date date, Date date2) {
        return getMilliSecondsDiff(date, date2) / 1000;
    }

    public static long getMilliSecondsDiff(Date date, Date date2) {
        return (date != null ? date.getTime() : 0L) - (date2 != null ? date2.getTime() : 0L);
    }
}
