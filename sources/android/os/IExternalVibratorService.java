package android.os;

public interface IExternalVibratorService extends IInterface {
    public static final int SCALE_HIGH = 1;
    public static final int SCALE_LOW = -1;
    public static final int SCALE_MUTE = -100;
    public static final int SCALE_NONE = 0;
    public static final int SCALE_VERY_HIGH = 2;
    public static final int SCALE_VERY_LOW = -2;

    int onExternalVibrationStart(ExternalVibration externalVibration) throws RemoteException;

    void onExternalVibrationStop(ExternalVibration externalVibration) throws RemoteException;

    public static class Default implements IExternalVibratorService {
        public int onExternalVibrationStart(ExternalVibration vib) throws RemoteException {
            return 0;
        }

        public void onExternalVibrationStop(ExternalVibration vib) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IExternalVibratorService {
        private static final String DESCRIPTOR = "android.os.IExternalVibratorService";
        static final int TRANSACTION_onExternalVibrationStart = 1;
        static final int TRANSACTION_onExternalVibrationStop = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IExternalVibratorService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IExternalVibratorService)) {
                return new Proxy(obj);
            }
            return (IExternalVibratorService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onExternalVibrationStart";
                case 2:
                    return "onExternalVibrationStop";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                ExternalVibration _arg0 = null;
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = ExternalVibration.CREATOR.createFromParcel(data);
                        }
                        int _result = onExternalVibrationStart(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = ExternalVibration.CREATOR.createFromParcel(data);
                        }
                        onExternalVibrationStop(_arg0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IExternalVibratorService {
            public static IExternalVibratorService sDefaultImpl;
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

            public int onExternalVibrationStart(ExternalVibration vib) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (vib != null) {
                        _data.writeInt(1);
                        vib.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onExternalVibrationStart(vib);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onExternalVibrationStop(ExternalVibration vib) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (vib != null) {
                        _data.writeInt(1);
                        vib.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onExternalVibrationStop(vib);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IExternalVibratorService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IExternalVibratorService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
