package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGetEuiccInfo1Callback extends IInterface {
    void onComplete(int i, byte[] bArr) throws RemoteException;

    public static class Default implements IGetEuiccInfo1Callback {
        public void onComplete(int resultCode, byte[] info) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGetEuiccInfo1Callback {
        private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IGetEuiccInfo1Callback";
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGetEuiccInfo1Callback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGetEuiccInfo1Callback)) {
                return new Proxy(obj);
            }
            return (IGetEuiccInfo1Callback) iin;
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
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                onComplete(data.readInt(), data.createByteArray());
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IGetEuiccInfo1Callback {
            public static IGetEuiccInfo1Callback sDefaultImpl;
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

            public void onComplete(int resultCode, byte[] info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resultCode);
                    _data.writeByteArray(info);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onComplete(resultCode, info);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IGetEuiccInfo1Callback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGetEuiccInfo1Callback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
