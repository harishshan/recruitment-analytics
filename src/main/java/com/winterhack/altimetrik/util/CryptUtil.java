package com.winterhack.altimetrik.util;

import org.apache.commons.codec.binary.Base64;

public class CryptUtil {
    public String encrypt(String planString) {
        byte[] encryptArray = Base64.encodeBase64(planString.getBytes());
        String encstr = new String(encryptArray);
        return encstr;
    }

    public String decrypt(String encryptString) {
        byte[] dectryptArray = encryptString.getBytes();
        byte[] decarray = Base64.decodeBase64(dectryptArray);
        String decstr = new String(decarray);
        return decstr;
    }
}
