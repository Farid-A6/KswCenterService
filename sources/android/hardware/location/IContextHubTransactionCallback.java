package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IContextHubTransactionCallback extends IInterface {
    void onQueryResponse(int i, List<NanoAppState> list) throws RemoteException;

    void onTransactionComplete(int i) throws RemoteException;

    public static class Default implements IContextHubTransactionCallback {
        public void onQueryResponse(int result, List<NanoAppState> list) throws RemoteException {
        }

        public void onTransactionComplete(int result) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IContextHubTransactionCallback {
        private static final String DESCRIPTOR = "android.hardware.location.IContextHubTransactionCallback";
        static final int TRANSACTION_onQueryResponse = 1;
        static final int TRANSACTION_onTransactionComplete = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContextHubTransactionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContextHubTransactionCallback)) {
                return new Proxy(obj);
            }
            return (IContextHubTransactionCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onQueryResponse";
                case 2:
                    return "onTransactionComplete";
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
                        onQueryResponse(data.readInt(), data.createTypedArrayList(NanoAppState.CREATOR));
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        onTransactionComplete(data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IContextHubTransactionCallback {
            public static IContextHubTransactionCallback sDefaultImpl;
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

            public void onQueryResponse(int result, List<NanoAppState> nanoappList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeTypedList(nanoappList);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onQueryResponse(result, nanoappList);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTransactionComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTransactionComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IContextHubTransactionCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContextHubTransactionCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
