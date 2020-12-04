package android.provider;

import android.annotation.UnsupportedAppUsage;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.internal.R;
import java.util.List;

public class ContactsInternal {
    private static final int CONTACTS_URI_LOOKUP = 1001;
    private static final int CONTACTS_URI_LOOKUP_ID = 1000;
    private static final UriMatcher sContactsUriMatcher = new UriMatcher(-1);

    private ContactsInternal() {
    }

    static {
        UriMatcher matcher = sContactsUriMatcher;
        matcher.addURI(ContactsContract.AUTHORITY, "contacts/lookup/*", 1001);
        matcher.addURI(ContactsContract.AUTHORITY, "contacts/lookup/*/#", 1000);
    }

    @UnsupportedAppUsage
    public static void startQuickContactWithErrorToast(Context context, Intent intent) {
        switch (sContactsUriMatcher.match(intent.getData())) {
            case 1000:
            case 1001:
                if (maybeStartManagedQuickContact(context, intent)) {
                    return;
                }
                break;
        }
        startQuickContactWithErrorToastForUser(context, intent, context.getUser());
    }

    public static void startQuickContactWithErrorToastForUser(Context context, Intent intent, UserHandle user) {
        try {
            context.startActivityAsUser(intent, user);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, (int) R.string.quick_contacts_not_available, 0).show();
        }
    }

    private static boolean maybeStartManagedQuickContact(Context context, Intent originalIntent) {
        long j;
        Uri uri = originalIntent.getData();
        List<String> pathSegments = uri.getPathSegments();
        boolean isContactIdIgnored = pathSegments.size() < 4;
        if (isContactIdIgnored) {
            j = ContactsContract.Contacts.ENTERPRISE_CONTACT_ID_BASE;
        } else {
            j = ContentUris.parseId(uri);
        }
        long contactId = j;
        String lookupKey = pathSegments.get(2);
        String directoryIdStr = uri.getQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY);
        long directoryId = directoryIdStr == null ? 1000000000 : Long.parseLong(directoryIdStr);
        if (TextUtils.isEmpty(lookupKey)) {
        } else if (!lookupKey.startsWith(ContactsContract.Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX)) {
            long j2 = directoryId;
        } else if (!ContactsContract.Contacts.isEnterpriseContactId(contactId)) {
            throw new IllegalArgumentException("Invalid enterprise contact id: " + contactId);
        } else if (ContactsContract.Directory.isEnterpriseDirectoryId(directoryId)) {
            long j3 = directoryId;
            ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).startManagedQuickContact(lookupKey.substring(ContactsContract.Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX.length()), contactId - ContactsContract.Contacts.ENTERPRISE_CONTACT_ID_BASE, isContactIdIgnored, directoryId - 1000000000, originalIntent);
            return true;
        } else {
            throw new IllegalArgumentException("Invalid enterprise directory id: " + directoryId);
        }
        return false;
    }
}
