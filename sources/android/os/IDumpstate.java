package android.os;

import android.os.IDumpstateListener;
import android.os.IDumpstateToken;
import java.io.FileDescriptor;

public interface IDumpstate extends IInterface {
    public static final int BUGREPORT_MODE_DEFAULT = 6;
    public static final int BUGREPORT_MODE_FULL = 0;
    public static final int BUGREPORT_MODE_INTERACTIVE = 1;
    public static final int BUGREPORT_MODE_REMOTE = 2;
    public static final int BUGREPORT_MODE_TELEPHONY = 4;
    public static final int BUGREPORT_MODE_WEAR = 3;
    public static final int BUGREPORT_MODE_WIFI = 5;

    void cancelBugreport() throws RemoteException;

    IDumpstateToken setListener(String str, IDumpstateListener iDumpstateListener, boolean z) throws RemoteException;

    void startBugreport(int i, String str, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, int i2, IDumpstateListener iDumpstateListener) throws RemoteException;

    public static class Default implements IDumpstate {
        public IDumpstateToken setListener(String name, IDumpstateListener listener, boolean getSectionDetails) throws RemoteException {
            return null;
        }

        public void startBugreport(int callingUid, String callingPackage, FileDescriptor bugreportFd, FileDescriptor screenshotFd, int bugreportMode, IDumpstateListener listener) throws RemoteException {
        }

        public void cancelBugreport() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDumpstate {
        private static final String DESCRIPTOR = "android.os.IDumpstate";
        static final int TRANSACTION_cancelBugreport = 3;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_startBugreport = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDumpstate asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDumpstate)) {
                return new Proxy(obj);
            }
            return (IDumpstate) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setListener";
                case 2:
                    return "startBugreport";
                case 3:
                    return "cancelBugreport";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        IDumpstateToken _result = setListener(data.readString(), IDumpstateListener.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0);
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result != null ? _result.asBinder() : null);
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        startBugreport(data.readInt(), data.readString(), data.readRawFileDescriptor(), data.readRawFileDescriptor(), data.readInt(), IDumpstateListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        cancelBugreport();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IDumpstate {
            public static IDumpstate sDefaultImpl;
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

            public IDumpstateToken setListener(String name, IDumpstateListener listener, boolean getSectionDetails) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(getSectionDetails);
                    if (!this.mRemote.transact(1, _data, _reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setListener(name, listener, getSectionDetails);
                    }
                    _reply.readException();
                    IDumpstateToken _result = IDumpstateToken.Stub.asInterface(_reply.readStrongBinder());
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startBugreport(int callingUid, String callingPackage, FileDescriptor bugreportFd, FileDescriptor screenshotFd, int bugreportMode, IDumpstateListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(callingUid);
                        try {
                            _data.writeString(callingPackage);
                        } catch (Throwable th) {
                            th = th;
                            FileDescriptor fileDescriptor = bugreportFd;
                            FileDescriptor fileDescriptor2 = screenshotFd;
                            int i = bugreportMode;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeRawFileDescriptor(bugreportFd);
                            try {
                                _data.writeRawFileDescriptor(screenshotFd);
                            } catch (Throwable th2) {
                                th = th2;
                                int i2 = bugreportMode;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                            try {
                                _data.writeInt(bugreportMode);
                                _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                                if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().startBugreport(callingUid, callingPackage, bugreportFd, screenshotFd, bugreportMode, listener);
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            FileDescriptor fileDescriptor22 = screenshotFd;
                            int i22 = bugreportMode;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        String str = callingPackage;
                        FileDescriptor fileDescriptor3 = bugreportFd;
                        FileDescriptor fileDescriptor222 = screenshotFd;
                        int i222 = bugreportMode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i3 = callingUid;
                    String str2 = callingPackage;
                    FileDescriptor fileDescriptor32 = bugreportFd;
                    FileDescriptor fileDescriptor2222 = screenshotFd;
                    int i2222 = bugreportMode;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelBugreport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelBugreport();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IDumpstate impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDumpstate getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
