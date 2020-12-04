package android.app;

import android.app.IUserSwitchObserver;
import android.os.Bundle;
import android.os.IRemoteCallback;
import android.os.RemoteException;

public class UserSwitchObserver extends IUserSwitchObserver.Stub {
    public void onUserSwitching(int newUserId, IRemoteCallback reply) throws RemoteException {
        if (reply != null) {
            reply.sendResult((Bundle) null);
        }
    }

    public void onUserSwitchComplete(int newUserId) throws RemoteException {
    }

    public void onForegroundProfileSwitch(int newProfileId) throws RemoteException {
    }

    public void onLockedBootComplete(int newUserId) throws RemoteException {
    }
}
