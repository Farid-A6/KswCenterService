package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFullBackupRestoreObserver extends IInterface {
    void onBackupPackage(String str) throws RemoteException;

    void onEndBackup() throws RemoteException;

    void onEndRestore() throws RemoteException;

    void onRestorePackage(String str) throws RemoteException;

    void onStartBackup() throws RemoteException;

    void onStartRestore() throws RemoteException;

    void onTimeout() throws RemoteException;

    public static class Default implements IFullBackupRestoreObserver {
        public void onStartBackup() throws RemoteException {
        }

        public void onBackupPackage(String name) throws RemoteException {
        }

        public void onEndBackup() throws RemoteException {
        }

        public void onStartRestore() throws RemoteException {
        }

        public void onRestorePackage(String name) throws RemoteException {
        }

        public void onEndRestore() throws RemoteException {
        }

        public void onTimeout() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IFullBackupRestoreObserver {
        private static final String DESCRIPTOR = "android.app.backup.IFullBackupRestoreObserver";
        static final int TRANSACTION_onBackupPackage = 2;
        static final int TRANSACTION_onEndBackup = 3;
        static final int TRANSACTION_onEndRestore = 6;
        static final int TRANSACTION_onRestorePackage = 5;
        static final int TRANSACTION_onStartBackup = 1;
        static final int TRANSACTION_onStartRestore = 4;
        static final int TRANSACTION_onTimeout = 7;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFullBackupRestoreObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFullBackupRestoreObserver)) {
                return new Proxy(obj);
            }
            return (IFullBackupRestoreObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onStartBackup";
                case 2:
                    return "onBackupPackage";
                case 3:
                    return "onEndBackup";
                case 4:
                    return "onStartRestore";
                case 5:
                    return "onRestorePackage";
                case 6:
                    return "onEndRestore";
                case 7:
                    return "onTimeout";
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
                        onStartBackup();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        onBackupPackage(data.readString());
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        onEndBackup();
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        onStartRestore();
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        onRestorePackage(data.readString());
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        onEndRestore();
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        onTimeout();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IFullBackupRestoreObserver {
            public static IFullBackupRestoreObserver sDefaultImpl;
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

            public void onStartBackup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStartBackup();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBackupPackage(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackupPackage(name);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onEndBackup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onEndBackup();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStartRestore() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStartRestore();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRestorePackage(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (this.mRemote.transact(5, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRestorePackage(name);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onEndRestore() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onEndRestore();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTimeout() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTimeout();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFullBackupRestoreObserver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IFullBackupRestoreObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
