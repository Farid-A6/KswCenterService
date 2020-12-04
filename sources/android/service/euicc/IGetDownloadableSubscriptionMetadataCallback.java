package android.service.euicc;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGetDownloadableSubscriptionMetadataCallback extends IInterface {
    @UnsupportedAppUsage
    void onComplete(GetDownloadableSubscriptionMetadataResult getDownloadableSubscriptionMetadataResult) throws RemoteException;

    public static class Default implements IGetDownloadableSubscriptionMetadataCallback {
        public void onComplete(GetDownloadableSubscriptionMetadataResult result) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGetDownloadableSubscriptionMetadataCallback {
        private static final String DESCRIPTOR = "android.service.euicc.IGetDownloadableSubscriptionMetadataCallback";
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGetDownloadableSubscriptionMetadataCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGetDownloadableSubscriptionMetadataCallback)) {
                return new Proxy(obj);
            }
            return (IGetDownloadableSubscriptionMetadataCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onComplete";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            GetDownloadableSubscriptionMetadataResult _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = GetDownloadableSubscriptionMetadataResult.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onComplete(_arg0);
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IGetDownloadableSubscriptionMetadataCallback {
            public static IGetDownloadableSubscriptionMetadataCallback sDefaultImpl;
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

            public void onComplete(GetDownloadableSubscriptionMetadataResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IGetDownloadableSubscriptionMetadataCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGetDownloadableSubscriptionMetadataCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
