package com.radoslav.playwhenever;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Rado on 10/07/2015.
 */
public class hash {

    private static String hashedValue;

    public hash(String password) throws NoSuchAlgorithmException {
        hashedValue = returnHashed(password);
        System.out.println("hash: " + hashedValue);
    }

    public String getHashedValue(){
        return hashedValue;
    }

    static String returnHashed(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

}
