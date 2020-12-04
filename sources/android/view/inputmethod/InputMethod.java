package android.view.inputmethod;

import android.os.IBinder;
import android.os.ResultReceiver;
import com.android.internal.inputmethod.IInputMethodPrivilegedOperations;

public interface InputMethod {
    public static final String SERVICE_INTERFACE = "android.view.InputMethod";
    public static final String SERVICE_META_DATA = "android.view.im";
    public static final int SHOW_EXPLICIT = 1;
    public static final int SHOW_FORCED = 2;

    public interface SessionCallback {
        void sessionCreated(InputMethodSession inputMethodSession);
    }

    void attachToken(IBinder iBinder);

    void bindInput(InputBinding inputBinding);

    void changeInputMethodSubtype(InputMethodSubtype inputMethodSubtype);

    void createSession(SessionCallback sessionCallback);

    void hideSoftInput(int i, ResultReceiver resultReceiver);

    void restartInput(InputConnection inputConnection, EditorInfo editorInfo);

    void revokeSession(InputMethodSession inputMethodSession);

    void setSessionEnabled(InputMethodSession inputMethodSession, boolean z);

    void showSoftInput(int i, ResultReceiver resultReceiver);

    void startInput(InputConnection inputConnection, EditorInfo editorInfo);

    void unbindInput();

    void initializeInternal(IBinder token, int displayId, IInputMethodPrivilegedOperations privilegedOperations) {
        updateInputMethodDisplay(displayId);
        attachToken(token);
    }

    void updateInputMethodDisplay(int displayId) {
    }

    void dispatchStartInputWithToken(InputConnection inputConnection, EditorInfo editorInfo, boolean restarting, IBinder startInputToken, boolean shouldPreRenderIme) {
        if (restarting) {
            restartInput(inputConnection, editorInfo);
        } else {
            startInput(inputConnection, editorInfo);
        }
    }
}
