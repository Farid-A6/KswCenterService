package android.provider;

import android.Manifest;
import android.annotation.SystemApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.SearchIndexablesContract;
import android.util.Log;

@SystemApi
public abstract class SearchIndexablesProvider extends ContentProvider {
    private static final int MATCH_NON_INDEXABLE_KEYS_CODE = 3;
    private static final int MATCH_RAW_CODE = 2;
    private static final int MATCH_RES_CODE = 1;
    private static final int MATCH_SITE_MAP_PAIRS_CODE = 4;
    private static final int MATCH_SLICE_URI_PAIRS_CODE = 5;
    private static final String TAG = "IndexablesProvider";
    private String mAuthority;
    private UriMatcher mMatcher;

    public abstract Cursor queryNonIndexableKeys(String[] strArr);

    public abstract Cursor queryRawData(String[] strArr);

    public abstract Cursor queryXmlResources(String[] strArr);

    public void attachInfo(Context context, ProviderInfo info) {
        this.mAuthority = info.authority;
        this.mMatcher = new UriMatcher(-1);
        this.mMatcher.addURI(this.mAuthority, SearchIndexablesContract.INDEXABLES_XML_RES_PATH, 1);
        this.mMatcher.addURI(this.mAuthority, SearchIndexablesContract.INDEXABLES_RAW_PATH, 2);
        this.mMatcher.addURI(this.mAuthority, SearchIndexablesContract.NON_INDEXABLES_KEYS_PATH, 3);
        this.mMatcher.addURI(this.mAuthority, SearchIndexablesContract.SITE_MAP_PAIRS_PATH, 4);
        this.mMatcher.addURI(this.mAuthority, SearchIndexablesContract.SLICE_URI_PAIRS_PATH, 5);
        if (!info.exported) {
            throw new SecurityException("Provider must be exported");
        } else if (!info.grantUriPermissions) {
            throw new SecurityException("Provider must grantUriPermissions");
        } else if (Manifest.permission.READ_SEARCH_INDEXABLES.equals(info.readPermission)) {
            super.attachInfo(context, info);
        } else {
            throw new SecurityException("Provider must be protected by READ_SEARCH_INDEXABLES");
        }
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            switch (this.mMatcher.match(uri)) {
                case 1:
                    return queryXmlResources((String[]) null);
                case 2:
                    return queryRawData((String[]) null);
                case 3:
                    return queryNonIndexableKeys((String[]) null);
                case 4:
                    return querySiteMapPairs();
                case 5:
                    return querySliceUriPairs();
                default:
                    throw new UnsupportedOperationException("Unknown Uri " + uri);
            }
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e2) {
            Log.e(TAG, "Provider querying exception:", e2);
            return null;
        }
    }

    public Cursor querySiteMapPairs() {
        return null;
    }

    public Cursor querySliceUriPairs() {
        return null;
    }

    public String getType(Uri uri) {
        switch (this.mMatcher.match(uri)) {
            case 1:
                return SearchIndexablesContract.XmlResource.MIME_TYPE;
            case 2:
                return SearchIndexablesContract.RawData.MIME_TYPE;
            case 3:
                return SearchIndexablesContract.NonIndexableKey.MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public final Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Insert not supported");
    }

    public final int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete not supported");
    }

    public final int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Update not supported");
    }
}
