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
}
