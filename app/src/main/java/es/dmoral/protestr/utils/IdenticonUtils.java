package es.dmoral.protestr.utils;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by grender on 3/07/17.
 */

public class IdenticonUtils {
    private static final String GRAVATAR_URL =
            "https://www.gravatar.com/avatar/%s?s=512&d=identicon";

    @NonNull
    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    @NonNull
    private static String md5Hex(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.toLowerCase().getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String generateGravatarUrl(String email) {
        return String.format(Locale.ENGLISH, GRAVATAR_URL, md5Hex(email));
    }
}
