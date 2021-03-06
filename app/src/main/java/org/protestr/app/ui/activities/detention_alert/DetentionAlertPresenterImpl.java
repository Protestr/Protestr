package org.protestr.app.ui.activities.detention_alert;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import org.protestr.app.utils.PreferencesUtils;

import es.dmoral.prefs.Prefs;
import org.protestr.app.utils.PreferencesUtils;

/**
 * Created by someone on 5/06/17.
 */

class DetentionAlertPresenterImpl implements DetentionAlertPresenter,
        DetentionAlertInteractor.OnContactRetrievedListener {

    private DetentionAlertView detentionAlertView;
    private DetentionAlertInteractor detentionAlertInteractor;

    DetentionAlertPresenterImpl(@NonNull DetentionAlertView detentionAlertView) {
        this.detentionAlertView = detentionAlertView;
        detentionAlertInteractor = new DetentionAlertInteractorImpl();
    }

    @Override
    public void requestContactInfo(Uri contactData) {
        detentionAlertInteractor.retrieveContact(this, (DetentionAlertActivity) detentionAlertView,
                contactData);
    }

    @Override
    public void onDestroy() {
        detentionAlertView = null;
    }

    @Override
    public void onContactRetrieved(Cursor contactInfoCursor) {
        contactInfoCursor.moveToFirst();
        try {
            final String displayName = contactInfoCursor.getString(contactInfoCursor.getColumnIndex
                    (ContactsContract.Contacts.DISPLAY_NAME));
            final String phoneNumber = contactInfoCursor.getString(contactInfoCursor.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");

            Prefs.with((DetentionAlertActivity) detentionAlertView).write(
                    PreferencesUtils.PREFERENCES_SELECTED_CONTACT_NAME, displayName
            );
            Prefs.with((DetentionAlertActivity) detentionAlertView).write(
                    PreferencesUtils.PREFERENCES_SELECTED_CONTACT_NUMBER, phoneNumber
            );

            detentionAlertView.contactSelected(displayName);
        } catch (Exception e) {
            e.printStackTrace();
            detentionAlertView.contactError();
        } finally {
            contactInfoCursor.close();
        }
    }

    @Override
    public void onContactError() {
        detentionAlertView.contactError();
    }
}
