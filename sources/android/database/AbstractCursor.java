package android.database;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCursor implements CrossProcessCursor {
    private static final String TAG = "Cursor";
    @Deprecated
    protected boolean mClosed;
    private final ContentObservable mContentObservable = new ContentObservable();
    @Deprecated
    protected ContentResolver mContentResolver;
    protected Long mCurrentRowID;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Bundle mExtras = Bundle.EMPTY;
    @UnsupportedAppUsage
    private Uri mNotifyUri;
    private List<Uri> mNotifyUris;
    @Deprecated
    protected int mPos = -1;
    protected int mRowIdColumnIndex;
    private ContentObserver mSelfObserver;
    private final Object mSelfObserverLock = new Object();
    private boolean mSelfObserverRegistered;
    protected HashMap<Long, Map<String, Object>> mUpdatedRows;

    public abstract String[] getColumnNames();

    public abstract int getCount();

    public abstract double getDouble(int i);

    public abstract float getFloat(int i);

    public abstract int getInt(int i);

    public abstract long getLong(int i);

    public abstract short getShort(int i);

    public abstract String getString(int i);

    public abstract boolean isNull(int i);

    public int getType(int column) {
        return 3;
    }

    public byte[] getBlob(int column) {
        throw new UnsupportedOperationException("getBlob is not supported");
    }

    public CursorWindow getWindow() {
        return null;
    }

    public int getColumnCount() {
        return getColumnNames().length;
    }

    public void deactivate() {
        onDeactivateOrClose();
    }

    /* access modifiers changed from: protected */
    public void onDeactivateOrClose() {
        if (this.mSelfObserver != null) {
            this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
            this.mSelfObserverRegistered = false;
        }
        this.mDataSetObservable.notifyInvalidated();
    }

    public boolean requery() {
        if (this.mSelfObserver != null && !this.mSelfObserverRegistered) {
            int size = this.mNotifyUris.size();
            for (int i = 0; i < size; i++) {
                this.mContentResolver.registerContentObserver(this.mNotifyUris.get(i), true, this.mSelfObserver);
            }
            this.mSelfObserverRegistered = true;
        }
        this.mDataSetObservable.notifyChanged();
        return true;
    }

    public boolean isClosed() {
        return this.mClosed;
    }

    public void close() {
        this.mClosed = true;
        this.mContentObservable.unregisterAll();
        onDeactivateOrClose();
    }

    public boolean onMove(int oldPosition, int newPosition) {
        return true;
    }

    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        String result = getString(columnIndex);
        if (result != null) {
            char[] data = buffer.data;
            if (data == null || data.length < result.length()) {
                buffer.data = result.toCharArray();
            } else {
                result.getChars(0, result.length(), data, 0);
            }
            buffer.sizeCopied = result.length();
            return;
        }
        buffer.sizeCopied = 0;
    }

    public final int getPosition() {
        return this.mPos;
    }

    public final boolean moveToPosition(int position) {
        int count = getCount();
        if (position >= count) {
            this.mPos = count;
            return false;
        } else if (position < 0) {
            this.mPos = -1;
            return false;
        } else if (position == this.mPos) {
            return true;
        } else {
            boolean result = onMove(this.mPos, position);
            if (!result) {
                this.mPos = -1;
            } else {
                this.mPos = position;
            }
            return result;
        }
    }

    public void fillWindow(int position, CursorWindow window) {
        DatabaseUtils.cursorFillWindow(this, position, window);
    }

    public final boolean move(int offset) {
        return moveToPosition(this.mPos + offset);
    }

    public final boolean moveToFirst() {
        return moveToPosition(0);
    }

    public final boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    public final boolean moveToNext() {
        return moveToPosition(this.mPos + 1);
    }

    public final boolean moveToPrevious() {
        return moveToPosition(this.mPos - 1);
    }

    public final boolean isFirst() {
        return this.mPos == 0 && getCount() != 0;
    }

    public final boolean isLast() {
        int cnt = getCount();
        return this.mPos == cnt + -1 && cnt != 0;
    }

    public final boolean isBeforeFirst() {
        if (getCount() == 0 || this.mPos == -1) {
            return true;
        }
        return false;
    }

    public final boolean isAfterLast() {
        if (getCount() == 0 || this.mPos == getCount()) {
            return true;
        }
        return false;
    }

    public int getColumnIndex(String columnName) {
        int periodIndex = columnName.lastIndexOf(46);
        if (periodIndex != -1) {
            Exception e = new Exception();
            Log.e(TAG, "requesting column name with table name -- " + columnName, e);
            columnName = columnName.substring(periodIndex + 1);
        }
        String[] columnNames = getColumnNames();
        int length = columnNames.length;
        for (int i = 0; i < length; i++) {
            if (columnNames[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    public int getColumnIndexOrThrow(String columnName) {
        int index = getColumnIndex(columnName);
        if (index >= 0) {
            return index;
        }
        String availableColumns = "";
        try {
            availableColumns = Arrays.toString(getColumnNames());
        } catch (Exception e) {
            Log.d(TAG, "Cannot collect column names for debug purposes", e);
        }
        throw new IllegalArgumentException("column '" + columnName + "' does not exist. Available columns: " + availableColumns);
    }

    public String getColumnName(int columnIndex) {
        return getColumnNames()[columnIndex];
    }

    public void registerContentObserver(ContentObserver observer) {
        this.mContentObservable.registerObserver(observer);
    }

    public void unregisterContentObserver(ContentObserver observer) {
        if (!this.mClosed) {
            this.mContentObservable.unregisterObserver(observer);
        }
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.unregisterObserver(observer);
    }

    /* access modifiers changed from: protected */
    public void onChange(boolean selfChange) {
        synchronized (this.mSelfObserverLock) {
            this.mContentObservable.dispatchChange(selfChange, (Uri) null);
            if (this.mNotifyUris != null && selfChange) {
                int size = this.mNotifyUris.size();
                for (int i = 0; i < size; i++) {
                    this.mContentResolver.notifyChange(this.mNotifyUris.get(i), this.mSelfObserver);
                }
            }
        }
    }

    public void setNotificationUri(ContentResolver cr, Uri notifyUri) {
        setNotificationUris(cr, Arrays.asList(new Uri[]{notifyUri}));
    }

    public void setNotificationUris(ContentResolver cr, List<Uri> notifyUris) {
        Preconditions.checkNotNull(cr);
        Preconditions.checkNotNull(notifyUris);
        setNotificationUris(cr, notifyUris, cr.getUserId(), true);
    }

    public void setNotificationUris(ContentResolver cr, List<Uri> notifyUris, int userHandle, boolean registerSelfObserver) {
        synchronized (this.mSelfObserverLock) {
            this.mNotifyUris = notifyUris;
            this.mNotifyUri = this.mNotifyUris.get(0);
            this.mContentResolver = cr;
            if (this.mSelfObserver != null) {
                this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
                this.mSelfObserverRegistered = false;
            }
            if (registerSelfObserver) {
                this.mSelfObserver = new SelfContentObserver(this);
                int size = this.mNotifyUris.size();
                for (int i = 0; i < size; i++) {
                    this.mContentResolver.registerContentObserver(this.mNotifyUris.get(i), true, this.mSelfObserver, userHandle);
                }
                this.mSelfObserverRegistered = true;
            }
        }
    }

    public Uri getNotificationUri() {
        Uri uri;
        synchronized (this.mSelfObserverLock) {
            uri = this.mNotifyUri;
        }
        return uri;
    }

    public List<Uri> getNotificationUris() {
        List<Uri> list;
        synchronized (this.mSelfObserverLock) {
            list = this.mNotifyUris;
        }
        return list;
    }

    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    public void setExtras(Bundle extras) {
        this.mExtras = extras == null ? Bundle.EMPTY : extras;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public Bundle respond(Bundle extras) {
        return Bundle.EMPTY;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public boolean isFieldUpdated(int columnIndex) {
        return false;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Object getUpdatedField(int columnIndex) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void checkPosition() {
        if (-1 == this.mPos || getCount() == this.mPos) {
            throw new CursorIndexOutOfBoundsException(this.mPos, getCount());
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (this.mSelfObserver != null && this.mSelfObserverRegistered) {
            this.mContentResolver.unregisterContentObserver(this.mSelfObserver);
        }
        try {
            if (!this.mClosed) {
                close();
            }
        } catch (Exception e) {
        }
    }

    protected static class SelfContentObserver extends ContentObserver {
        WeakReference<AbstractCursor> mCursor;

        public SelfContentObserver(AbstractCursor cursor) {
            super((Handler) null);
            this.mCursor = new WeakReference<>(cursor);
        }

        public boolean deliverSelfNotifications() {
            return false;
        }

        public void onChange(boolean selfChange) {
            AbstractCursor cursor = (AbstractCursor) this.mCursor.get();
            if (cursor != null) {
                cursor.onChange(false);
            }
        }
    }
}
