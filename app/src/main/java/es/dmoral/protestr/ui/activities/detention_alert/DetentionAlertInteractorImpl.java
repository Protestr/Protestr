package es.dmoral.protestr.ui.activities.detention_alert;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by grender on 5/06/17.
 */

class DetentionAlertInteractorImpl implements DetentionAlertInteractor {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void retrieveContact(OnContactRetrievedListener onContactRetrievedListener, Context context,
                                Uri contactData) {
        Cursor contactCursor = null;
        Cursor phonesCursor = null;
        try {
            contactCursor = context.getContentResolver().query(contactData, null, null, null, null);
            contactCursor.moveToFirst();
            final boolean hasPhone = contactCursor.getString(contactCursor.getColumnIndexOrThrow(
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            )).equals("1");
            if (hasPhone) {
                final String contactId = contactCursor
                        .getString(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                phonesCursor = context.getContentResolver().query
                        (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + " = " + contactId, null, null);
                onContactRetrievedListener.onContactRetrieved(phonesCursor);
            } else {
                onContactRetrievedListener.onContactError();
            }
        } catch (Exception e){
            e.printStackTrace();
            onContactRetrievedListener.onContactError();
        } finally {
            if (contactCursor != null)
                contactCursor.close();
            if (phonesCursor != null)
                phonesCursor.close();
        }
    }
}
