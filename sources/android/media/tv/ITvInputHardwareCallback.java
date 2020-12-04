package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITvInputHardwareCallback extends IInterface {
    void onReleased() throws RemoteException;

    void onStreamConfigChanged(TvStreamConfig[] tvStreamConfigArr) throws RemoteException;

    public static class Default implements ITvInputHardwareCallback {
        public void onReleased() throws RemoteException {
        }

        public void onStreamConfigChanged(TvStreamConfig[] configs) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITvInputHardwareCallback {
        private static final String DESCRIPTOR = "android.media.tv.ITvInputHardwareCallback";
        static final int TRANSACTION_onReleased = 1;
        static final int TRANSACTION_onStreamConfigChanged = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITvInputHardwareCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITvInputHardwareCallback)) {
                return new Proxy(obj);
            }
            return (ITvInputHardwareCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onReleased";
                case 2:
                    return "onStreamConfigChanged";
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
                        onReleased();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        onStreamConfigChanged((TvStreamConfig[]) data.createTypedArray(TvStreamConfig.CREATOR));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements ITvInputHardwareCallback {
            public static ITvInputHardwareCallback sDefaultImpl;
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

            public void onReleased() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onReleased();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStreamConfigChanged(TvStreamConfig[] configs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(configs, 0);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStreamConfigChanged(configs);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITvInputHardwareCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITvInputHardwareCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
