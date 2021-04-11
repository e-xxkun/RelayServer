package com.xxkun.relayserver.common;

public class Utils {

    public static long stringToLong(String str) {
        if (str.length() < 4) {
            return -1;
        }
        long tmp = 0;
        tmp += str.charAt(0);
        tmp += (long)str.charAt(1) << 16;
        tmp += (long)str.charAt(2) << 32;
        tmp += (long)str.charAt(3) << 48;
        return tmp;
    }

    public static String longToString(long value) {
        char[] charArr = new char[4];
        charArr[0] = (char) (value & 0xFFFF);
        charArr[1] = (char) (value >> 16 & 0xFFFF);
        charArr[2] = (char) (value >> 32 & 0xFFFF);
        charArr[3] = (char) (value >> 48 & 0xFFFF);
        return new String(charArr);
    }
}
