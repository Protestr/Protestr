package es.dmoral.protestr.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Random;

import es.dmoral.protestr.R;

/**
 * Created by grender on 1/07/17.
 */

public class UsernameGenerator {
    public static String generateUsername(@NonNull Context context) {
        final Random randomGenerator = new Random();
        final String[] adjectives = context.getResources().getStringArray(R.array.adjectives);
        final String[] names = context.getResources().getStringArray(R.array.names);

        return adjectives[randomGenerator.nextInt(adjectives.length)] + " " +
                names[randomGenerator.nextInt(names.length)];
    }
}
