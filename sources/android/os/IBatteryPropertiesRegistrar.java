package android.os;

public interface IBatteryPropertiesRegistrar extends IInterface {
    int getProperty(int i, BatteryProperty batteryProperty) throws RemoteException;

    void scheduleUpdate() throws RemoteException;

    public static class Default implements IBatteryPropertiesRegistrar {
        public int getProperty(int id, BatteryProperty prop) throws RemoteException {
            return 0;
        }

        public void scheduleUpdate() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBatteryPropertiesRegistrar {
        private static final String DESCRIPTOR = "android.os.IBatteryPropertiesRegistrar";
        static final int TRANSACTION_getProperty = 1;
        static final int TRANSACTION_scheduleUpdate = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBatteryPropertiesRegistrar asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBatteryPropertiesRegistrar)) {
                return new Proxy(obj);
            }
            return (IBatteryPropertiesRegistrar) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getProperty";
                case 2:
                    return "scheduleUpdate";
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
                        int _arg0 = data.readInt();
                        BatteryProperty _arg1 = new BatteryProperty();
                        int _result = getProperty(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        scheduleUpdate();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IBatteryPropertiesRegistrar {
            public static IBatteryPropertiesRegistrar sDefaultImpl;
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

            public int getProperty(int id, BatteryProperty prop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    if (!this.mRemote.transact(1, _data, _reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProperty(id, prop);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        prop.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void scheduleUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleUpdate();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBatteryPropertiesRegistrar impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBatteryPropertiesRegistrar getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
