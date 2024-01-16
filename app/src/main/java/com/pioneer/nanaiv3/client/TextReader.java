package com.pioneer.nanaiv3.client;

public class TextReader {

    public Boolean isRSValid(String raw, String leftkey, String rightkey){

        raw = raw.replaceAll(" ", "");
        raw = raw.replaceAll("\n", "");
        raw = raw.toUpperCase();

        if (raw.contains(leftkey) && raw.contains(rightkey)) return true;

        return false;
    }
}
