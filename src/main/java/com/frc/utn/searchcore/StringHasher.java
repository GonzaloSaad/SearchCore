package com.frc.utn.searchcore;

public class StringHasher {
    public static int longHash(String string) {
        string = string.trim();
        long h = 98764321261L;
        int l = string.length();
        char[] chars = string.toCharArray();

        for (int i = 0; i < l; i++) {
            h = 31*h + chars[i];
        }
        return Long.hashCode(h);
    }
}
