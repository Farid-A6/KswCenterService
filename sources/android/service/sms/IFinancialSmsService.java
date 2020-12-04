package android.service.sms;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;

public interface IFinancialSmsService extends IInterface {
    void getSmsMessages(RemoteCallback remoteCallback, Bundle bundle) throws RemoteException;

    public static class Default implements IFinancialSmsService {
        public void getSmsMessages(RemoteCallback callback, Bundle params) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IFinancialSmsService {
        private static final String DESCRIPTOR = "android.service.sms.IFinancialSmsService";
        static final int TRANSACTION_getSmsMessages = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFinancialSmsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFinancialSmsService)) {
                return new Proxy(obj);
            }
            return (IFinancialSmsService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "getSmsMessages";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            RemoteCallback _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                Bundle _arg1 = null;
                if (data.readInt() != 0) {
                    _arg0 = RemoteCallback.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = Bundle.CREATOR.createFromParcel(data);
                }
                getSmsMessages(_arg0, _arg1);
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IFinancialSmsService {
            public static IFinancialSmsService sDefaultImpl;
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

            public void getSmsMessages(RemoteCallback callback, Bundle params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getSmsMessages(callback, params);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFinancialSmsService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IFinancialSmsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
