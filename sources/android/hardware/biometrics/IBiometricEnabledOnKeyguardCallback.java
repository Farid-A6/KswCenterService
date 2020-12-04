package android.hardware.biometrics;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBiometricEnabledOnKeyguardCallback extends IInterface {
    void onChanged(BiometricSourceType biometricSourceType, boolean z) throws RemoteException;

    public static class Default implements IBiometricEnabledOnKeyguardCallback {
        public void onChanged(BiometricSourceType type, boolean enabled) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBiometricEnabledOnKeyguardCallback {
        private static final String DESCRIPTOR = "android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback";
        static final int TRANSACTION_onChanged = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBiometricEnabledOnKeyguardCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBiometricEnabledOnKeyguardCallback)) {
                return new Proxy(obj);
            }
            return (IBiometricEnabledOnKeyguardCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            BiometricSourceType _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = BiometricSourceType.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onChanged(_arg0, data.readInt() != 0);
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IBiometricEnabledOnKeyguardCallback {
            public static IBiometricEnabledOnKeyguardCallback sDefaultImpl;
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

            public void onChanged(BiometricSourceType type, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (type != null) {
                        _data.writeInt(1);
                        type.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onChanged(type, enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBiometricEnabledOnKeyguardCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBiometricEnabledOnKeyguardCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
