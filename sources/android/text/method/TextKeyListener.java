package android.text.method;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.QwertyKeyListener;
import android.view.KeyEvent;
import android.view.View;
import java.lang.ref.WeakReference;

public class TextKeyListener extends BaseKeyListener implements SpanWatcher {
    static final Object ACTIVE = new NoCopySpan.Concrete();
    static final int AUTO_CAP = 1;
    static final int AUTO_PERIOD = 4;
    static final int AUTO_TEXT = 2;
    static final Object CAPPED = new NoCopySpan.Concrete();
    static final Object INHIBIT_REPLACEMENT = new NoCopySpan.Concrete();
    static final Object LAST_TYPED = new NoCopySpan.Concrete();
    static final int SHOW_PASSWORD = 8;
    private static TextKeyListener[] sInstance = new TextKeyListener[(Capitalize.values().length * 2)];
    private Capitalize mAutoCap;
    private boolean mAutoText;
    private SettingsObserver mObserver;
    private int mPrefs;
    /* access modifiers changed from: private */
    public boolean mPrefsInited;
    /* access modifiers changed from: private */
    public WeakReference<ContentResolver> mResolver;

    public enum Capitalize {
        NONE,
        SENTENCES,
        WORDS,
        CHARACTERS
    }

    public TextKeyListener(Capitalize cap, boolean autotext) {
        this.mAutoCap = cap;
        this.mAutoText = autotext;
    }

    public static TextKeyListener getInstance(boolean autotext, Capitalize cap) {
        int off = (cap.ordinal() * 2) + (autotext);
        if (sInstance[off] == null) {
            sInstance[off] = new TextKeyListener(cap, autotext);
        }
        return sInstance[off];
    }

    public static TextKeyListener getInstance() {
        return getInstance(false, Capitalize.NONE);
    }

    public static boolean shouldCap(Capitalize cap, CharSequence cs, int off) {
        if (cap == Capitalize.NONE) {
            return false;
        }
        if (cap == Capitalize.CHARACTERS) {
            return true;
        }
        if (TextUtils.getCapsMode(cs, off, cap == Capitalize.WORDS ? 8192 : 16384) != 0) {
            return true;
        }
        return false;
    }

    public int getInputType() {
        return makeTextContentType(this.mAutoCap, this.mAutoText);
    }

    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        return getKeyListener(event).onKeyDown(view, content, keyCode, event);
    }

    public boolean onKeyUp(View view, Editable content, int keyCode, KeyEvent event) {
        return getKeyListener(event).onKeyUp(view, content, keyCode, event);
    }

    public boolean onKeyOther(View view, Editable content, KeyEvent event) {
        return getKeyListener(event).onKeyOther(view, content, event);
    }

    public static void clear(Editable e) {
        e.clear();
        e.removeSpan(ACTIVE);
        e.removeSpan(CAPPED);
        e.removeSpan(INHIBIT_REPLACEMENT);
        e.removeSpan(LAST_TYPED);
        for (QwertyKeyListener.Replaced removeSpan : (QwertyKeyListener.Replaced[]) e.getSpans(0, e.length(), QwertyKeyListener.Replaced.class)) {
            e.removeSpan(removeSpan);
        }
    }

    public void onSpanAdded(Spannable s, Object what, int start, int end) {
    }

    public void onSpanRemoved(Spannable s, Object what, int start, int end) {
    }

    public void onSpanChanged(Spannable s, Object what, int start, int end, int st, int en) {
        if (what == Selection.SELECTION_END) {
            s.removeSpan(ACTIVE);
        }
    }

    private KeyListener getKeyListener(KeyEvent event) {
        int kind = event.getKeyCharacterMap().getKeyboardType();
        if (kind == 3) {
            return QwertyKeyListener.getInstance(this.mAutoText, this.mAutoCap);
        }
        if (kind == 1) {
            return MultiTapKeyListener.getInstance(this.mAutoText, this.mAutoCap);
        }
        if (kind == 4 || kind == 5) {
            return QwertyKeyListener.getInstanceForFullKeyboard();
        }
        return NullKeyListener.getInstance();
    }

    private static class NullKeyListener implements KeyListener {
        private static NullKeyListener sInstance;

        private NullKeyListener() {
        }

        public int getInputType() {
            return 0;
        }

        public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
            return false;
        }

        public boolean onKeyUp(View view, Editable content, int keyCode, KeyEvent event) {
            return false;
        }

        public boolean onKeyOther(View view, Editable content, KeyEvent event) {
            return false;
        }

        public void clearMetaKeyState(View view, Editable content, int states) {
        }

        public static NullKeyListener getInstance() {
            if (sInstance != null) {
                return sInstance;
            }
            sInstance = new NullKeyListener();
            return sInstance;
        }
    }

    public void release() {
        if (this.mResolver != null) {
            ContentResolver contentResolver = (ContentResolver) this.mResolver.get();
            if (contentResolver != null) {
                contentResolver.unregisterContentObserver(this.mObserver);
                this.mResolver.clear();
            }
            this.mObserver = null;
            this.mResolver = null;
            this.mPrefsInited = false;
        }
    }

    private void initPrefs(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        this.mResolver = new WeakReference<>(contentResolver);
        if (this.mObserver == null) {
            this.mObserver = new SettingsObserver();
            contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, this.mObserver);
        }
        updatePrefs(contentResolver);
        this.mPrefsInited = true;
    }

    private class SettingsObserver extends ContentObserver {
        public SettingsObserver() {
            super(new Handler());
        }

        public void onChange(boolean selfChange) {
            if (TextKeyListener.this.mResolver != null) {
                ContentResolver contentResolver = (ContentResolver) TextKeyListener.this.mResolver.get();
                if (contentResolver == null) {
                    boolean unused = TextKeyListener.this.mPrefsInited = false;
                } else {
                    TextKeyListener.this.updatePrefs(contentResolver);
                }
            } else {
                boolean unused2 = TextKeyListener.this.mPrefsInited = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void updatePrefs(ContentResolver resolver) {
        int i = 1;
        int i2 = 0;
        boolean cap = Settings.System.getInt(resolver, Settings.System.TEXT_AUTO_CAPS, 1) > 0;
        boolean text = Settings.System.getInt(resolver, Settings.System.TEXT_AUTO_REPLACE, 1) > 0;
        boolean period = Settings.System.getInt(resolver, Settings.System.TEXT_AUTO_PUNCTUATE, 1) > 0;
        boolean pw = Settings.System.getInt(resolver, Settings.System.TEXT_SHOW_PASSWORD, 1) > 0;
        if (!cap) {
            i = 0;
        }
        int i3 = i | (text ? 2 : 0) | (period ? 4 : 0);
        if (pw) {
            i2 = 8;
        }
        this.mPrefs = i3 | i2;
    }

    /* access modifiers changed from: package-private */
    public int getPrefs(Context context) {
        synchronized (this) {
            if (!this.mPrefsInited || this.mResolver.get() == null) {
                initPrefs(context);
            }
        }
        return this.mPrefs;
    }
}
