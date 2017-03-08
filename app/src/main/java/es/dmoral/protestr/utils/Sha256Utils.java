package es.dmoral.protestr.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by grender on 13/02/17.
 */

public class Sha256Utils {
    public static @Nullable String digest(@NonNull final String data) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
        return String.format("%064x", new java.math.BigInteger(1, md.digest()));
    }

}
