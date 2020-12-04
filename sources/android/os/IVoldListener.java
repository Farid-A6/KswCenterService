package android.os;

public interface IVoldListener extends IInterface {
    void onDiskCreated(String str, int i) throws RemoteException;

    void onDiskDestroyed(String str) throws RemoteException;

    void onDiskMetadataChanged(String str, long j, String str2, String str3) throws RemoteException;

    void onDiskScanned(String str) throws RemoteException;

    void onVolumeCreated(String str, int i, String str2, String str3) throws RemoteException;

    void onVolumeDestroyed(String str) throws RemoteException;

    void onVolumeInternalPathChanged(String str, String str2) throws RemoteException;

    void onVolumeMetadataChanged(String str, String str2, String str3, String str4) throws RemoteException;

    void onVolumePathChanged(String str, String str2) throws RemoteException;

    void onVolumeStateChanged(String str, int i) throws RemoteException;

    public static class Default implements IVoldListener {
        public void onDiskCreated(String diskId, int flags) throws RemoteException {
        }

        public void onDiskScanned(String diskId) throws RemoteException {
        }

        public void onDiskMetadataChanged(String diskId, long sizeBytes, String label, String sysPath) throws RemoteException {
        }

        public void onDiskDestroyed(String diskId) throws RemoteException {
        }

        public void onVolumeCreated(String volId, int type, String diskId, String partGuid) throws RemoteException {
        }

        public void onVolumeStateChanged(String volId, int state) throws RemoteException {
        }

        public void onVolumeMetadataChanged(String volId, String fsType, String fsUuid, String fsLabel) throws RemoteException {
        }

        public void onVolumePathChanged(String volId, String path) throws RemoteException {
        }

        public void onVolumeInternalPathChanged(String volId, String internalPath) throws RemoteException {
        }

        public void onVolumeDestroyed(String volId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVoldListener {
        private static final String DESCRIPTOR = "android.os.IVoldListener";
        static final int TRANSACTION_onDiskCreated = 1;
        static final int TRANSACTION_onDiskDestroyed = 4;
        static final int TRANSACTION_onDiskMetadataChanged = 3;
        static final int TRANSACTION_onDiskScanned = 2;
        static final int TRANSACTION_onVolumeCreated = 5;
        static final int TRANSACTION_onVolumeDestroyed = 10;
        static final int TRANSACTION_onVolumeInternalPathChanged = 9;
        static final int TRANSACTION_onVolumeMetadataChanged = 7;
        static final int TRANSACTION_onVolumePathChanged = 8;
        static final int TRANSACTION_onVolumeStateChanged = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVoldListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVoldListener)) {
                return new Proxy(obj);
            }
            return (IVoldListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onDiskCreated";
                case 2:
                    return "onDiskScanned";
                case 3:
                    return "onDiskMetadataChanged";
                case 4:
                    return "onDiskDestroyed";
                case 5:
                    return "onVolumeCreated";
                case 6:
                    return "onVolumeStateChanged";
                case 7:
                    return "onVolumeMetadataChanged";
                case 8:
                    return "onVolumePathChanged";
                case 9:
                    return "onVolumeInternalPathChanged";
                case 10:
                    return "onVolumeDestroyed";
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
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        onDiskCreated(data.readString(), data.readInt());
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        onDiskScanned(data.readString());
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        onDiskMetadataChanged(data.readString(), data.readLong(), data.readString(), data.readString());
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        onDiskDestroyed(data.readString());
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        onVolumeCreated(data.readString(), data.readInt(), data.readString(), data.readString());
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        onVolumeStateChanged(data.readString(), data.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        onVolumeMetadataChanged(data.readString(), data.readString(), data.readString(), data.readString());
                        return true;
                    case 8:
                        parcel.enforceInterface(DESCRIPTOR);
                        onVolumePathChanged(data.readString(), data.readString());
                        return true;
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        onVolumeInternalPathChanged(data.readString(), data.readString());
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        onVolumeDestroyed(data.readString());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IVoldListener {
            public static IVoldListener sDefaultImpl;
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

            public void onDiskCreated(String diskId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDiskCreated(diskId, flags);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDiskScanned(String diskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDiskScanned(diskId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDiskMetadataChanged(String diskId, long sizeBytes, String label, String sysPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeLong(sizeBytes);
                    _data.writeString(label);
                    _data.writeString(sysPath);
                    if (this.mRemote.transact(3, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDiskMetadataChanged(diskId, sizeBytes, label, sysPath);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDiskDestroyed(String diskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    if (this.mRemote.transact(4, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDiskDestroyed(diskId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeCreated(String volId, int type, String diskId, String partGuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(type);
                    _data.writeString(diskId);
                    _data.writeString(partGuid);
                    if (this.mRemote.transact(5, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeCreated(volId, type, diskId, partGuid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeStateChanged(String volId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(state);
                    if (this.mRemote.transact(6, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeStateChanged(volId, state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeMetadataChanged(String volId, String fsType, String fsUuid, String fsLabel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(fsType);
                    _data.writeString(fsUuid);
                    _data.writeString(fsLabel);
                    if (this.mRemote.transact(7, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeMetadataChanged(volId, fsType, fsUuid, fsLabel);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumePathChanged(String volId, String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(path);
                    if (this.mRemote.transact(8, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumePathChanged(volId, path);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeInternalPathChanged(String volId, String internalPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(internalPath);
                    if (this.mRemote.transact(9, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeInternalPathChanged(volId, internalPath);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeDestroyed(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    if (this.mRemote.transact(10, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeDestroyed(volId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IVoldListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVoldListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
