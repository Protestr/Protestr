package org.protestr.app.utils;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by someone on 13/02/17.
 */

public class Sha256Utils {

    public static String digest(@NonNull final String data) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return "";
        }
        return String.format("%064x", new java.math.BigInteger(1, md.digest()));
    }

}
