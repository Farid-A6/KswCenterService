package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISyncAdapterUnsyncableAccountCallback extends IInterface {
    void onUnsyncableAccountDone(boolean z) throws RemoteException;

    public static class Default implements ISyncAdapterUnsyncableAccountCallback {
        public void onUnsyncableAccountDone(boolean isReady) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISyncAdapterUnsyncableAccountCallback {
        private static final String DESCRIPTOR = "android.content.ISyncAdapterUnsyncableAccountCallback";
        static final int TRANSACTION_onUnsyncableAccountDone = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISyncAdapterUnsyncableAccountCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISyncAdapterUnsyncableAccountCallback)) {
                return new Proxy(obj);
            }
            return (ISyncAdapterUnsyncableAccountCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onUnsyncableAccountDone";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                onUnsyncableAccountDone(data.readInt() != 0);
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements ISyncAdapterUnsyncableAccountCallback {
            public static ISyncAdapterUnsyncableAccountCallback sDefaultImpl;
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

            public void onUnsyncableAccountDone(boolean isReady) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isReady);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onUnsyncableAccountDone(isReady);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISyncAdapterUnsyncableAccountCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISyncAdapterUnsyncableAccountCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
