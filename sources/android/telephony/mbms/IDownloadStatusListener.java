package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDownloadStatusListener extends IInterface {
    void onStatusUpdated(DownloadRequest downloadRequest, FileInfo fileInfo, int i) throws RemoteException;

    public static class Default implements IDownloadStatusListener {
        public void onStatusUpdated(DownloadRequest request, FileInfo fileInfo, int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDownloadStatusListener {
        private static final String DESCRIPTOR = "android.telephony.mbms.IDownloadStatusListener";
        static final int TRANSACTION_onStatusUpdated = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDownloadStatusListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDownloadStatusListener)) {
                return new Proxy(obj);
            }
            return (IDownloadStatusListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onStatusUpdated";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DownloadRequest _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                FileInfo _arg1 = null;
                if (data.readInt() != 0) {
                    _arg0 = DownloadRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = FileInfo.CREATOR.createFromParcel(data);
                }
                onStatusUpdated(_arg0, _arg1, data.readInt());
                reply.writeNoException();
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IDownloadStatusListener {
            public static IDownloadStatusListener sDefaultImpl;
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

            public void onStatusUpdated(DownloadRequest request, FileInfo fileInfo, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fileInfo != null) {
                        _data.writeInt(1);
                        fileInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(status);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onStatusUpdated(request, fileInfo, status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IDownloadStatusListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDownloadStatusListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
