package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoggingContentInterface implements ContentInterface {
    private final ContentInterface delegate;
    /* access modifiers changed from: private */
    public final String tag;

    public LoggingContentInterface(String tag2, ContentInterface delegate2) {
        this.tag = tag2;
        this.delegate = delegate2;
    }

    private class Logger implements AutoCloseable {
        private final StringBuilder sb = new StringBuilder();

        public Logger(String method, Object... args) {
            for (Bundle bundle : args) {
                if (bundle instanceof Bundle) {
                    bundle.size();
                }
            }
            StringBuilder sb2 = this.sb;
            sb2.append("callingUid=");
            sb2.append(Binder.getCallingUid());
            sb2.append(' ');
            this.sb.append(method);
            StringBuilder sb3 = this.sb;
            sb3.append('(');
            sb3.append(deepToString(args));
            sb3.append(')');
        }

        private String deepToString(Object value) {
            if (value == null || !value.getClass().isArray()) {
                return String.valueOf(value);
            }
            return Arrays.deepToString((Object[]) value);
        }

        public <T> T setResult(T res) {
            if (res instanceof Cursor) {
                this.sb.append(10);
                DatabaseUtils.dumpCursor((Cursor) res, this.sb);
            } else {
                StringBuilder sb2 = this.sb;
                sb2.append(" = ");
                sb2.append(deepToString(res));
            }
            return res;
        }

        public void close() {
            Log.v(LoggingContentInterface.this.tag, this.sb.toString());
        }
    }

    public Cursor query(Uri uri, String[] projection, Bundle queryArgs, CancellationSignal cancellationSignal) throws RemoteException {
        Logger l = new Logger("query", uri, projection, queryArgs, cancellationSignal);
        try {
            Cursor cursor = (Cursor) l.setResult(this.delegate.query(uri, projection, queryArgs, cancellationSignal));
            $closeResource((Throwable) null, l);
            return cursor;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
            } catch (Throwable th) {
                x0.addSuppressed(th);
            }
        } else {
            x1.close();
        }
    }

    public String getType(Uri uri) throws RemoteException {
        Logger l = new Logger("getType", uri);
        try {
            String str = (String) l.setResult(this.delegate.getType(uri));
            $closeResource((Throwable) null, l);
            return str;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public String[] getStreamTypes(Uri uri, String mimeTypeFilter) throws RemoteException {
        Logger l = new Logger("getStreamTypes", uri, mimeTypeFilter);
        try {
            String[] strArr = (String[]) l.setResult(this.delegate.getStreamTypes(uri, mimeTypeFilter));
            $closeResource((Throwable) null, l);
            return strArr;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public Uri canonicalize(Uri uri) throws RemoteException {
        Logger l = new Logger("canonicalize", uri);
        try {
            Uri uri2 = (Uri) l.setResult(this.delegate.canonicalize(uri));
            $closeResource((Throwable) null, l);
            return uri2;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public Uri uncanonicalize(Uri uri) throws RemoteException {
        Logger l = new Logger("uncanonicalize", uri);
        try {
            Uri uri2 = (Uri) l.setResult(this.delegate.uncanonicalize(uri));
            $closeResource((Throwable) null, l);
            return uri2;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public boolean refresh(Uri uri, Bundle args, CancellationSignal cancellationSignal) throws RemoteException {
        Logger l = new Logger("refresh", uri, args, cancellationSignal);
        try {
            boolean booleanValue = ((Boolean) l.setResult(Boolean.valueOf(this.delegate.refresh(uri, args, cancellationSignal)))).booleanValue();
            $closeResource((Throwable) null, l);
            return booleanValue;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public Uri insert(Uri uri, ContentValues initialValues) throws RemoteException {
        Logger l = new Logger("insert", uri, initialValues);
        try {
            Uri uri2 = (Uri) l.setResult(this.delegate.insert(uri, initialValues));
            $closeResource((Throwable) null, l);
            return uri2;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public int bulkInsert(Uri uri, ContentValues[] initialValues) throws RemoteException {
        Logger l = new Logger("bulkInsert", uri, initialValues);
        try {
            int intValue = ((Integer) l.setResult(Integer.valueOf(this.delegate.bulkInsert(uri, initialValues)))).intValue();
            $closeResource((Throwable) null, l);
            return intValue;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) throws RemoteException {
        Logger l = new Logger("delete", uri, selection, selectionArgs);
        try {
            int intValue = ((Integer) l.setResult(Integer.valueOf(this.delegate.delete(uri, selection, selectionArgs)))).intValue();
            $closeResource((Throwable) null, l);
            return intValue;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) throws RemoteException {
        Logger l = new Logger("update", uri, values, selection, selectionArgs);
        try {
            int intValue = ((Integer) l.setResult(Integer.valueOf(this.delegate.update(uri, values, selection, selectionArgs)))).intValue();
            $closeResource((Throwable) null, l);
            return intValue;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Logger l = new Logger("openFile", uri, mode, signal);
        try {
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) l.setResult(this.delegate.openFile(uri, mode, signal));
            $closeResource((Throwable) null, l);
            return parcelFileDescriptor;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Logger l = new Logger("openAssetFile", uri, mode, signal);
        try {
            AssetFileDescriptor assetFileDescriptor = (AssetFileDescriptor) l.setResult(this.delegate.openAssetFile(uri, mode, signal));
            $closeResource((Throwable) null, l);
            return assetFileDescriptor;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Logger l = new Logger("openTypedAssetFile", uri, mimeTypeFilter, opts, signal);
        try {
            AssetFileDescriptor assetFileDescriptor = (AssetFileDescriptor) l.setResult(this.delegate.openTypedAssetFile(uri, mimeTypeFilter, opts, signal));
            $closeResource((Throwable) null, l);
            return assetFileDescriptor;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        Logger l = new Logger("applyBatch", authority, operations);
        try {
            ContentProviderResult[] contentProviderResultArr = (ContentProviderResult[]) l.setResult(this.delegate.applyBatch(authority, operations));
            $closeResource((Throwable) null, l);
            return contentProviderResultArr;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }

    public Bundle call(String authority, String method, String arg, Bundle extras) throws RemoteException {
        Logger l = new Logger("call", authority, method, arg, extras);
        try {
            Bundle bundle = (Bundle) l.setResult(this.delegate.call(authority, method, arg, extras));
            $closeResource((Throwable) null, l);
            return bundle;
        } catch (Exception res) {
            l.setResult(res);
            throw res;
        } catch (Throwable th) {
            $closeResource(r1, l);
            throw th;
        }
    }
}
