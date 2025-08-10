package com.movies.player.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class ConvertUtil {
    public static String integerMapToString(Map<Integer, ArrayList<Integer>> map) {
        StringBuilder sb = new StringBuilder();
        for (Integer num : map.keySet()) {
            int intValue = num.intValue();
            if (sb.length() > 0) {
                sb.append("&");
            }
            ArrayList<Integer> arrayList = map.get(Integer.valueOf(intValue));
            try {
                sb.append(intValue);
                sb.append("=");
                sb.append(arrayList != null ? URLEncoder.encode(integerArrayToString(arrayList), "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return sb.toString();
    }

    public static String mapToString(Map<Integer, ArrayList<String>> map) {
        StringBuilder sb = new StringBuilder();
        for (Integer num : map.keySet()) {
            int intValue = num.intValue();
            if (sb.length() > 0) {
                sb.append("&");
            }
            ArrayList<String> arrayList = map.get(Integer.valueOf(intValue));
            try {
                sb.append(intValue);
                sb.append("=");
                sb.append(arrayList != null ? URLEncoder.encode(stringArrayToString(arrayList), "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return sb.toString();
    }

    public static Map<Integer, ArrayList<String>> stringToMap(String str) {
        HashMap hashMap = new HashMap();
        for (String str2 : str.split("&")) {
            String[] split = str2.split("=");
            try {
                hashMap.put(Integer.valueOf(split[0]), split.length > 1 ? stringToStringArray(URLDecoder.decode(split[1], "UTF-8")) : null);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return hashMap;
    }

    public static Map<Integer, ArrayList<Integer>> stringToIntegerMap(String str) {
        HashMap hashMap = new HashMap();
        for (String str2 : str.split("&")) {
            String[] split = str2.split("=");
            try {
                hashMap.put(Integer.valueOf(Integer.parseInt(split[0])), split.length > 1 ? stringToIntegerList(URLDecoder.decode(split[1], "UTF-8")) : null);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return hashMap;
    }

    public static String integerArrayToString(ArrayList<Integer> arrayList) {
        return TextUtils.join(",", arrayList);
    }

    public static ArrayList<Integer> stringToIntegerList(String str) {
        String[] split;
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String str2 : str.split(",")) {
            try {
                arrayList.add(Integer.valueOf(Integer.parseInt(str2.trim())));
            } catch (NumberFormatException unused) {
                Log.e("stringToIntegerList", "Cannot convert '" + str2 + "' to Integer");
            }
        }
        return arrayList;
    }

    public static String stringArrayToString(ArrayList<String> arrayList) {
        return TextUtils.join("`", arrayList);
    }

    public static ArrayList<String> stringToStringArray(String str) {
        return new ArrayList<>(Arrays.asList(str.split("`")));
    }

    public static Integer parseInteger(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof String) {
            return Integer.valueOf(Integer.parseInt((String) obj));
        }
        return 0;
    }

    public static String decode(String str) {
        byte[] decode = Base64.decode(str, 8);
        for (int i = 0; i < decode.length; i++) {
            decode[i] = (byte) (decode[i] ^ 17);
        }
        return new String(decode, StandardCharsets.UTF_8);
    }
}
