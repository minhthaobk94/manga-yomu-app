package com.thaontm.mangayomu.utils;

/**
 * Created by thao on 4/10/2017.
 * Copyright thao 2017.
 */

public class StringUtils {
    public static String joinString(String[] stringArray) {
        String result = "";
        for (int i = 0; i < stringArray.length; i++) {
            if (i == 0) {
                result = stringArray[i];
            } else {
                result = result + ", " + stringArray[i];
            }
        }
        return result;
    }

    /*
    * This function makes the input string shorter than 'length' if its length is longer than 'length'
    * */
    public static String shorten(String s, int length) {
        if (s == null || s.length() < length) return s;
        else return new StringBuilder(s.substring(0, length) + "...").toString();
    }
}
