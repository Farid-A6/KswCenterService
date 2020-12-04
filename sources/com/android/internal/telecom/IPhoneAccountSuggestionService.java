package com.android.internal.telecom;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telecom.IPhoneAccountSuggestionCallback;

public interface IPhoneAccountSuggestionService extends IInterface {
    void onAccountSuggestionRequest(IPhoneAccountSuggestionCallback iPhoneAccountSuggestionCallback, String str) throws RemoteException;

    public static class Default implements IPhoneAccountSuggestionService {
        public void onAccountSuggestionRequest(IPhoneAccountSuggestionCallback callback, String number) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPhoneAccountSuggestionService {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IPhoneAccountSuggestionService";
        static final int TRANSACTION_onAccountSuggestionRequest = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPhoneAccountSuggestionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPhoneAccountSuggestionService)) {
                return new Proxy(obj);
            }
            return (IPhoneAccountSuggestionService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onAccountSuggestionRequest";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                onAccountSuggestionRequest(IPhoneAccountSuggestionCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IPhoneAccountSuggestionService {
            public static IPhoneAccountSuggestionService sDefaultImpl;
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

            public void onAccountSuggestionRequest(IPhoneAccountSuggestionCallback callback, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(number);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAccountSuggestionRequest(callback, number);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IPhoneAccountSuggestionService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPhoneAccountSuggestionService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
