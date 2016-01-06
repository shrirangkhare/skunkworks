package com.example.shirang.myapplication;

/**
 * Created by shrirang on 5/1/16.
 */
public class Utils {
    public static String truncate(String in, int num) {
        String upToNCharacters = in.substring(0, Math.min(in.length(), num));
        return upToNCharacters;
    }
}
