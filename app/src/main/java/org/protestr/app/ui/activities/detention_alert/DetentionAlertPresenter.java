package org.protestr.app.ui.activities.detention_alert;

import android.net.Uri;

/**
 * Created by someone on 5/06/17.
 */

interface DetentionAlertPresenter {
    void requestContactInfo(Uri contactData);

    void onDestroy();
}
