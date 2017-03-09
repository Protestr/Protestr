package es.dmoral.protestr.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by grender on 14/02/17.
 */

public class InternetUtils {

    public static boolean isInternetAvailable(@NonNull Context context) {
        NetworkInfo ni = ((ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return ni != null && ni.isConnected() && ni.isAvailable();
    }
}
