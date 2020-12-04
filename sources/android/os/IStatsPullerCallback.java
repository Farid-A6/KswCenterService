package android.os;

public interface IStatsPullerCallback extends IInterface {
    StatsLogEventWrapper[] pullData(int i, long j, long j2) throws RemoteException;

    public static class Default implements IStatsPullerCallback {
        public StatsLogEventWrapper[] pullData(int atomTag, long elapsedNanos, long wallClocknanos) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStatsPullerCallback {
        private static final String DESCRIPTOR = "android.os.IStatsPullerCallback";
        static final int TRANSACTION_pullData = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatsPullerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStatsPullerCallback)) {
                return new Proxy(obj);
            }
            return (IStatsPullerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "pullData";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = reply;
            if (i == 1) {
                data.enforceInterface(DESCRIPTOR);
                StatsLogEventWrapper[] _result = pullData(data.readInt(), data.readLong(), data.readLong());
                reply.writeNoException();
                parcel.writeTypedArray(_result, 1);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IStatsPullerCallback {
            public static IStatsPullerCallback sDefaultImpl;
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

            public StatsLogEventWrapper[] pullData(int atomTag, long elapsedNanos, long wallClocknanos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(atomTag);
                    _data.writeLong(elapsedNanos);
                    _data.writeLong(wallClocknanos);
                    if (!this.mRemote.transact(1, _data, _reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().pullData(atomTag, elapsedNanos, wallClocknanos);
                    }
                    _reply.readException();
                    StatsLogEventWrapper[] _result = (StatsLogEventWrapper[]) _reply.createTypedArray(StatsLogEventWrapper.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IStatsPullerCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStatsPullerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
