package android.view.contentcapture;

import android.content.ContentCaptureOptions;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IContentCaptureDirectManager extends IInterface {
    void sendEvents(ParceledListSlice parceledListSlice, int i, ContentCaptureOptions contentCaptureOptions) throws RemoteException;

    public static class Default implements IContentCaptureDirectManager {
        public void sendEvents(ParceledListSlice events, int reason, ContentCaptureOptions options) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IContentCaptureDirectManager {
        private static final String DESCRIPTOR = "android.view.contentcapture.IContentCaptureDirectManager";
        static final int TRANSACTION_sendEvents = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentCaptureDirectManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContentCaptureDirectManager)) {
                return new Proxy(obj);
            }
            return (IContentCaptureDirectManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "sendEvents";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ParceledListSlice _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                ContentCaptureOptions _arg2 = null;
                if (data.readInt() != 0) {
                    _arg0 = ParceledListSlice.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                int _arg1 = data.readInt();
                if (data.readInt() != 0) {
                    _arg2 = ContentCaptureOptions.CREATOR.createFromParcel(data);
                }
                sendEvents(_arg0, _arg1, _arg2);
                return true;
            } else if (code != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IContentCaptureDirectManager {
            public static IContentCaptureDirectManager sDefaultImpl;
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

            public void sendEvents(ParceledListSlice events, int reason, ContentCaptureOptions options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (events != null) {
                        _data.writeInt(1);
                        events.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(reason);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendEvents(events, reason, options);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IContentCaptureDirectManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContentCaptureDirectManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
