package es.dmoral.protestr.ui;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class UpdatePool {
    private static ArrayList<String> updatePool = new ArrayList<>();

    public static void needsUpdates(@NonNull String className) {
        if (!updatePool.contains(className))
            updatePool.add(className);
    }

    public static boolean doINeedUpdates(@NonNull String className, boolean onlyCheck) {
        final boolean doINeed = updatePool.contains(className);
        if (!onlyCheck)
            updatePool.remove(className);
        return doINeed;
    }

    public static boolean doINeedUpdates(@NonNull String className) {
        return doINeedUpdates(className, false);
    }

    public static void clearUpdatePool() {
        updatePool.clear();
    }
}
