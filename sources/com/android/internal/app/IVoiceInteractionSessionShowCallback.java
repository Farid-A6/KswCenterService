package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVoiceInteractionSessionShowCallback extends IInterface {
    void onFailed() throws RemoteException;

    void onShown() throws RemoteException;

    public static class Default implements IVoiceInteractionSessionShowCallback {
        public void onFailed() throws RemoteException {
        }

        public void onShown() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVoiceInteractionSessionShowCallback {
        private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractionSessionShowCallback";
        static final int TRANSACTION_onFailed = 1;
        static final int TRANSACTION_onShown = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVoiceInteractionSessionShowCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVoiceInteractionSessionShowCallback)) {
                return new Proxy(obj);
            }
            return (IVoiceInteractionSessionShowCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onFailed";
                case 2:
                    return "onShown";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        onFailed();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        onShown();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IVoiceInteractionSessionShowCallback {
            public static IVoiceInteractionSessionShowCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onFailed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFailed();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onShown();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IVoiceInteractionSessionShowCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVoiceInteractionSessionShowCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
