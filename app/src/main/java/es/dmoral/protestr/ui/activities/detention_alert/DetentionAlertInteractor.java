package es.dmoral.protestr.ui.activities.detention_alert;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by grender on 5/06/17.
 */

interface DetentionAlertInteractor {
    interface OnContactRetrievedListener {
        void onContactRetrieved(Cursor contactInfoCursor);
        void onContactError();
    }

    void retrieveContact(final OnContactRetrievedListener onContactRetrievedListener, Context context,
                         Uri contactData);
}
