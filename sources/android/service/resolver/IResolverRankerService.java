package android.service.resolver;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.resolver.IResolverRankerResult;
import java.util.List;

public interface IResolverRankerService extends IInterface {
    void predict(List<ResolverTarget> list, IResolverRankerResult iResolverRankerResult) throws RemoteException;

    void train(List<ResolverTarget> list, int i) throws RemoteException;

    public static class Default implements IResolverRankerService {
        public void predict(List<ResolverTarget> list, IResolverRankerResult result) throws RemoteException {
        }

        public void train(List<ResolverTarget> list, int selectedPosition) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IResolverRankerService {
        private static final String DESCRIPTOR = "android.service.resolver.IResolverRankerService";
        static final int TRANSACTION_predict = 1;
        static final int TRANSACTION_train = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IResolverRankerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IResolverRankerService)) {
                return new Proxy(obj);
            }
            return (IResolverRankerService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "predict";
                case 2:
                    return "train";
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
                        predict(data.createTypedArrayList(ResolverTarget.CREATOR), IResolverRankerResult.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        train(data.createTypedArrayList(ResolverTarget.CREATOR), data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IResolverRankerService {
            public static IResolverRankerService sDefaultImpl;
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

            public void predict(List<ResolverTarget> targets, IResolverRankerResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(targets);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().predict(targets, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void train(List<ResolverTarget> targets, int selectedPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(targets);
                    _data.writeInt(selectedPosition);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().train(targets, selectedPosition);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IResolverRankerService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IResolverRankerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
