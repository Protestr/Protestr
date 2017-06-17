package es.dmoral.protestr.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by grender on 17/06/17.
 */

public class LocationUtils {
    public interface OnAddressDecodedListener {
        void onAddressDecoded(LatLng latLng);
    }

    public static void getLocationFromAddress(@NonNull final Context context, @NonNull final String address,
                                                @NonNull final OnAddressDecodedListener onAddressDecodedListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Geocoder geocoder = new Geocoder(context);
                LatLng latLng;
                try {
                    final List<Address> addressList = geocoder.getFromLocationName(address, 1);
                    latLng = new LatLng(addressList.get(0).getLatitude(),
                            addressList.get(0).getLongitude());
                } catch (Exception e) {
                    latLng = new LatLng(0, 0);
                }
                onAddressDecodedListener.onAddressDecoded(latLng);
            }
        }).start();
    }
}
