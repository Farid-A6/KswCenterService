package android.content;

import android.content.IIntentReceiver;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IIntentSender extends IInterface {
    void send(int i, Intent intent, String str, IBinder iBinder, IIntentReceiver iIntentReceiver, String str2, Bundle bundle) throws RemoteException;

    public static class Default implements IIntentSender {
        public void send(int code, Intent intent, String resolvedType, IBinder whitelistToken, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IIntentSender {
        private static final String DESCRIPTOR = "android.content.IIntentSender";
        static final int TRANSACTION_send = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIntentSender asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IIntentSender)) {
                return new Proxy(obj);
            }
            return (IIntentSender) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "send";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg1;
            Bundle _arg6;
            int i = code;
            Parcel parcel = data;
            if (i == 1) {
                Parcel parcel2 = reply;
                parcel.enforceInterface(DESCRIPTOR);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = Intent.CREATOR.createFromParcel(parcel);
                } else {
                    _arg1 = null;
                }
                String _arg2 = data.readString();
                IBinder _arg3 = data.readStrongBinder();
                IIntentReceiver _arg4 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                String _arg5 = data.readString();
                if (data.readInt() != 0) {
                    _arg6 = Bundle.CREATOR.createFromParcel(parcel);
                } else {
                    _arg6 = null;
                }
                send(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IIntentSender {
            public static IIntentSender sDefaultImpl;
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

            public void send(int code, Intent intent, String resolvedType, IBinder whitelistToken, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
                Intent intent2 = intent;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(code);
                        if (intent2 != null) {
                            _data.writeInt(1);
                            intent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(resolvedType);
                        } catch (Throwable th) {
                            th = th;
                            IBinder iBinder = whitelistToken;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        String str = resolvedType;
                        IBinder iBinder2 = whitelistToken;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStrongBinder(whitelistToken);
                        _data.writeStrongBinder(finishedReceiver != null ? finishedReceiver.asBinder() : null);
                        _data.writeString(requiredPermission);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().send(code, intent, resolvedType, whitelistToken, finishedReceiver, requiredPermission, options);
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i = code;
                    String str2 = resolvedType;
                    IBinder iBinder22 = whitelistToken;
                    _data.recycle();
                    throw th;
                }
            }
        }

        public static boolean setDefaultImpl(IIntentSender impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IIntentSender getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
