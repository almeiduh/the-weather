package com.wit.weather;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by aribeiro on 10/9/14.
 */
public class ContactGetter {

    private static final String[] PROJECTION = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};

    public static void contacts(Context context) {

        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                PROJECTION, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int colIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String contactName = cursor.getString(colIdx);

                Log.v("CONTACTS-GETTER", contactName);
            }
        }
    }

}
