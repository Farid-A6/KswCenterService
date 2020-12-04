package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBluetoothGattServerCallback extends IInterface {
    void onCharacteristicReadRequest(String str, int i, int i2, boolean z, int i3) throws RemoteException;

    void onCharacteristicWriteRequest(String str, int i, int i2, int i3, boolean z, boolean z2, int i4, byte[] bArr) throws RemoteException;

    void onConnectionUpdated(String str, int i, int i2, int i3, int i4) throws RemoteException;

    void onDescriptorReadRequest(String str, int i, int i2, boolean z, int i3) throws RemoteException;

    void onDescriptorWriteRequest(String str, int i, int i2, int i3, boolean z, boolean z2, int i4, byte[] bArr) throws RemoteException;

    void onExecuteWrite(String str, int i, boolean z) throws RemoteException;

    void onMtuChanged(String str, int i) throws RemoteException;

    void onNotificationSent(String str, int i) throws RemoteException;

    void onPhyRead(String str, int i, int i2, int i3) throws RemoteException;

    void onPhyUpdate(String str, int i, int i2, int i3) throws RemoteException;

    void onServerConnectionState(int i, int i2, boolean z, String str) throws RemoteException;

    void onServerRegistered(int i, int i2) throws RemoteException;

    void onServiceAdded(int i, BluetoothGattService bluetoothGattService) throws RemoteException;

    public static class Default implements IBluetoothGattServerCallback {
        public void onServerRegistered(int status, int serverIf) throws RemoteException {
        }

        public void onServerConnectionState(int status, int serverIf, boolean connected, String address) throws RemoteException {
        }

        public void onServiceAdded(int status, BluetoothGattService service) throws RemoteException {
        }

        public void onCharacteristicReadRequest(String address, int transId, int offset, boolean isLong, int handle) throws RemoteException {
        }

        public void onDescriptorReadRequest(String address, int transId, int offset, boolean isLong, int handle) throws RemoteException {
        }

        public void onCharacteristicWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) throws RemoteException {
        }

        public void onDescriptorWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) throws RemoteException {
        }

        public void onExecuteWrite(String address, int transId, boolean execWrite) throws RemoteException {
        }

        public void onNotificationSent(String address, int status) throws RemoteException {
        }

        public void onMtuChanged(String address, int mtu) throws RemoteException {
        }

        public void onPhyUpdate(String address, int txPhy, int rxPhy, int status) throws RemoteException {
        }

        public void onPhyRead(String address, int txPhy, int rxPhy, int status) throws RemoteException {
        }

        public void onConnectionUpdated(String address, int interval, int latency, int timeout, int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothGattServerCallback {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGattServerCallback";
        static final int TRANSACTION_onCharacteristicReadRequest = 4;
        static final int TRANSACTION_onCharacteristicWriteRequest = 6;
        static final int TRANSACTION_onConnectionUpdated = 13;
        static final int TRANSACTION_onDescriptorReadRequest = 5;
        static final int TRANSACTION_onDescriptorWriteRequest = 7;
        static final int TRANSACTION_onExecuteWrite = 8;
        static final int TRANSACTION_onMtuChanged = 10;
        static final int TRANSACTION_onNotificationSent = 9;
        static final int TRANSACTION_onPhyRead = 12;
        static final int TRANSACTION_onPhyUpdate = 11;
        static final int TRANSACTION_onServerConnectionState = 2;
        static final int TRANSACTION_onServerRegistered = 1;
        static final int TRANSACTION_onServiceAdded = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothGattServerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothGattServerCallback)) {
                return new Proxy(obj);
            }
            return (IBluetoothGattServerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onServerRegistered";
                case 2:
                    return "onServerConnectionState";
                case 3:
                    return "onServiceAdded";
                case 4:
                    return "onCharacteristicReadRequest";
                case 5:
                    return "onDescriptorReadRequest";
                case 6:
                    return "onCharacteristicWriteRequest";
                case 7:
                    return "onDescriptorWriteRequest";
                case 8:
                    return "onExecuteWrite";
                case 9:
                    return "onNotificationSent";
                case 10:
                    return "onMtuChanged";
                case 11:
                    return "onPhyUpdate";
                case 12:
                    return "onPhyRead";
                case 13:
                    return "onConnectionUpdated";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            BluetoothGattService _arg1;
            int i = code;
            Parcel parcel = data;
            if (i != 1598968902) {
                boolean _arg2 = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        onServerRegistered(data.readInt(), data.readInt());
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _arg0 = data.readInt();
                        int _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onServerConnectionState(_arg0, _arg12, _arg2, data.readString());
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = BluetoothGattService.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        onServiceAdded(_arg02, _arg1);
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        onCharacteristicReadRequest(data.readString(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        onDescriptorReadRequest(data.readString(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        onCharacteristicWriteRequest(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt() != 0, data.readInt(), data.createByteArray());
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        onDescriptorWriteRequest(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt() != 0, data.readInt(), data.createByteArray());
                        return true;
                    case 8:
                        parcel.enforceInterface(DESCRIPTOR);
                        String _arg03 = data.readString();
                        int _arg13 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onExecuteWrite(_arg03, _arg13, _arg2);
                        return true;
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        onNotificationSent(data.readString(), data.readInt());
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        onMtuChanged(data.readString(), data.readInt());
                        return true;
                    case 11:
                        parcel.enforceInterface(DESCRIPTOR);
                        onPhyUpdate(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 12:
                        parcel.enforceInterface(DESCRIPTOR);
                        onPhyRead(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 13:
                        parcel.enforceInterface(DESCRIPTOR);
                        onConnectionUpdated(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IBluetoothGattServerCallback {
            public static IBluetoothGattServerCallback sDefaultImpl;
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

            public void onServerRegistered(int status, int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(serverIf);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onServerRegistered(status, serverIf);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onServerConnectionState(int status, int serverIf, boolean connected, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(serverIf);
                    _data.writeInt(connected);
                    _data.writeString(address);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onServerConnectionState(status, serverIf, connected, address);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onServiceAdded(int status, BluetoothGattService service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onServiceAdded(status, service);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCharacteristicReadRequest(String address, int transId, int offset, boolean isLong, int handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(transId);
                    _data.writeInt(offset);
                    _data.writeInt(isLong);
                    _data.writeInt(handle);
                    if (this.mRemote.transact(4, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCharacteristicReadRequest(address, transId, offset, isLong, handle);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDescriptorReadRequest(String address, int transId, int offset, boolean isLong, int handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(transId);
                    _data.writeInt(offset);
                    _data.writeInt(isLong);
                    _data.writeInt(handle);
                    if (this.mRemote.transact(5, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDescriptorReadRequest(address, transId, offset, isLong, handle);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCharacteristicWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(address);
                    } catch (Throwable th) {
                        th = th;
                        int i = transId;
                        int i2 = offset;
                        int i3 = length;
                        boolean z = isPrep;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(transId);
                        try {
                            _data.writeInt(offset);
                        } catch (Throwable th2) {
                            th = th2;
                            int i32 = length;
                            boolean z2 = isPrep;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(length);
                        } catch (Throwable th3) {
                            th = th3;
                            boolean z22 = isPrep;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        int i22 = offset;
                        int i322 = length;
                        boolean z222 = isPrep;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(isPrep ? 1 : 0);
                        _data.writeInt(needRsp ? 1 : 0);
                        _data.writeInt(handle);
                        _data.writeByteArray(value);
                        if (this.mRemote.transact(6, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().onCharacteristicWriteRequest(address, transId, offset, length, isPrep, needRsp, handle, value);
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = address;
                    int i4 = transId;
                    int i222 = offset;
                    int i3222 = length;
                    boolean z2222 = isPrep;
                    _data.recycle();
                    throw th;
                }
            }

            public void onDescriptorWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(address);
                    } catch (Throwable th) {
                        th = th;
                        int i = transId;
                        int i2 = offset;
                        int i3 = length;
                        boolean z = isPrep;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(transId);
                        try {
                            _data.writeInt(offset);
                        } catch (Throwable th2) {
                            th = th2;
                            int i32 = length;
                            boolean z2 = isPrep;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(length);
                        } catch (Throwable th3) {
                            th = th3;
                            boolean z22 = isPrep;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        int i22 = offset;
                        int i322 = length;
                        boolean z222 = isPrep;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(isPrep ? 1 : 0);
                        _data.writeInt(needRsp ? 1 : 0);
                        _data.writeInt(handle);
                        _data.writeByteArray(value);
                        if (this.mRemote.transact(7, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().onDescriptorWriteRequest(address, transId, offset, length, isPrep, needRsp, handle, value);
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = address;
                    int i4 = transId;
                    int i222 = offset;
                    int i3222 = length;
                    boolean z2222 = isPrep;
                    _data.recycle();
                    throw th;
                }
            }

            public void onExecuteWrite(String address, int transId, boolean execWrite) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(transId);
                    _data.writeInt(execWrite);
                    if (this.mRemote.transact(8, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onExecuteWrite(address, transId, execWrite);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onNotificationSent(String address, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    if (this.mRemote.transact(9, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onNotificationSent(address, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onMtuChanged(String address, int mtu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(mtu);
                    if (this.mRemote.transact(10, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMtuChanged(address, mtu);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPhyUpdate(String address, int txPhy, int rxPhy, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(status);
                    if (this.mRemote.transact(11, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPhyUpdate(address, txPhy, rxPhy, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPhyRead(String address, int txPhy, int rxPhy, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(status);
                    if (this.mRemote.transact(12, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPhyRead(address, txPhy, rxPhy, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onConnectionUpdated(String address, int interval, int latency, int timeout, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(interval);
                    _data.writeInt(latency);
                    _data.writeInt(timeout);
                    _data.writeInt(status);
                    if (this.mRemote.transact(13, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConnectionUpdated(address, interval, latency, timeout, status);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothGattServerCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothGattServerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
