package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IContextHubClient extends IInterface {
    void close() throws RemoteException;

    int sendMessageToNanoApp(NanoAppMessage nanoAppMessage) throws RemoteException;

    public static class Default implements IContextHubClient {
        public int sendMessageToNanoApp(NanoAppMessage message) throws RemoteException {
            return 0;
        }

        public void close() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IContextHubClient {
        private static final String DESCRIPTOR = "android.hardware.location.IContextHubClient";
        static final int TRANSACTION_close = 2;
        static final int TRANSACTION_sendMessageToNanoApp = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContextHubClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContextHubClient)) {
                return new Proxy(obj);
            }
            return (IContextHubClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "sendMessageToNanoApp";
                case 2:
                    return "close";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            NanoAppMessage _arg0;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = NanoAppMessage.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        int _result = sendMessageToNanoApp(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        close();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IContextHubClient {
            public static IContextHubClient sDefaultImpl;
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

            public int sendMessageToNanoApp(NanoAppMessage message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendMessageToNanoApp(message);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().close();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IContextHubClient impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContextHubClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
