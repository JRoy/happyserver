package com.wheezygold.happyserver.util;

public class UtilTime {

    public static String diffString(long start, long end) {
        long diff = end - start;
        String result;
        if (diff >= 86400) {
            result = diff / 86400 + " Days";
        } else if (diff >= 3600) {
            result = diff / 3600 + " Hours";
        } else if (diff >= 60) {
            result = diff / 60 + " Minutes";
        } else {
            result = diff + "Seconds";
        }

        return result;

    }

}