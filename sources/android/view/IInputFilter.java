package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IInputFilterHost;

public interface IInputFilter extends IInterface {
    void filterInputEvent(InputEvent inputEvent, int i) throws RemoteException;

    void install(IInputFilterHost iInputFilterHost) throws RemoteException;

    void uninstall() throws RemoteException;

    public static class Default implements IInputFilter {
        public void install(IInputFilterHost host) throws RemoteException {
        }

        public void uninstall() throws RemoteException {
        }

        public void filterInputEvent(InputEvent event, int policyFlags) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInputFilter {
        private static final String DESCRIPTOR = "android.view.IInputFilter";
        static final int TRANSACTION_filterInputEvent = 3;
        static final int TRANSACTION_install = 1;
        static final int TRANSACTION_uninstall = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputFilter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInputFilter)) {
                return new Proxy(obj);
            }
            return (IInputFilter) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "install";
                case 2:
                    return "uninstall";
                case 3:
                    return "filterInputEvent";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            InputEvent _arg0;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        install(IInputFilterHost.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        uninstall();
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = InputEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        filterInputEvent(_arg0, data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IInputFilter {
            public static IInputFilter sDefaultImpl;
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

            public void install(IInputFilterHost host) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(host != null ? host.asBinder() : null);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().install(host);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void uninstall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().uninstall();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void filterInputEvent(InputEvent event, int policyFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(policyFlags);
                    if (this.mRemote.transact(3, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().filterInputEvent(event, policyFlags);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInputFilter impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInputFilter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
