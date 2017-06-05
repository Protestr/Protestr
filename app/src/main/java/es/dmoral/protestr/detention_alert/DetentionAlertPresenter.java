package es.dmoral.protestr.detention_alert;

import android.net.Uri;

/**
 * Created by grender on 5/06/17.
 */

interface DetentionAlertPresenter {
    void requestContactInfo(Uri contactData);
    void onDestroy();
}
