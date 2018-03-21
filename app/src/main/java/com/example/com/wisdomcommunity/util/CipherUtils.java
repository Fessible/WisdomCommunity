package com.example.com.wisdomcommunity.util;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class CipherUtils {

    public static String md5(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(text.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte resultByte : resultByteArray) {
                String shaHex = Integer.toHexString(resultByte & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    public static String sha1(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(text.getBytes());
            byte resultByteArray[] = messageDigest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte resultByte : resultByteArray) {
                String shaHex = Integer.toHexString(resultByte & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    public static String base64Encode(String text) {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    public static String base64Decode(String text) {
        return Base64.decode(text.getBytes(), Base64.DEFAULT).toString();
    }

    public static String xorEncode(String text, String privateKey) {
        privateKey = privateKey.toLowerCase(Locale.getDefault());
        int[] snNum = new int[text.length()];
        String result = "";
        String temp = "";
        for (int i = 0, j = 0; i < text.length(); i++, j++) {
            if (j == privateKey.length()) {
                j = 0;
            }
            snNum[i] = text.charAt(i) ^ privateKey.charAt(j);
        }
        for (int k = 0; k < text.length(); k++) {
            if (snNum[k] < 10) {
                temp = "00" + snNum[k];
            } else {
                if (snNum[k] < 100) {
                    temp = "0" + snNum[k];
                }
            }
            result += temp;
        }
        return result;
    }

    public static String xorDecode(String text, String privateKey) {
        privateKey = privateKey.toLowerCase(Locale.getDefault());
        char[] snNum = new char[text.length() / 3];
        String result = "";
        for (int i = 0, j = 0; i < text.length() / 3; i++, j++) {
            if (j == privateKey.length()) {
                j = 0;
            }
            int n = Integer.parseInt(text.substring(i * 3, i * 3 + 3));
            snNum[i] = (char) ((char) n ^ privateKey.charAt(j));
        }
        for (int k = 0; k < text.length() / 3; k++) {
            result += snNum[k];
        }
        return result;
    }
}
