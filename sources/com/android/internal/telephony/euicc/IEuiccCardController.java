package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.euicc.IAuthenticateServerCallback;
import com.android.internal.telephony.euicc.ICancelSessionCallback;
import com.android.internal.telephony.euicc.IDeleteProfileCallback;
import com.android.internal.telephony.euicc.IDisableProfileCallback;
import com.android.internal.telephony.euicc.IGetAllProfilesCallback;
import com.android.internal.telephony.euicc.IGetDefaultSmdpAddressCallback;
import com.android.internal.telephony.euicc.IGetEuiccChallengeCallback;
import com.android.internal.telephony.euicc.IGetEuiccInfo1Callback;
import com.android.internal.telephony.euicc.IGetEuiccInfo2Callback;
import com.android.internal.telephony.euicc.IGetProfileCallback;
import com.android.internal.telephony.euicc.IGetRulesAuthTableCallback;
import com.android.internal.telephony.euicc.IGetSmdsAddressCallback;
import com.android.internal.telephony.euicc.IListNotificationsCallback;
import com.android.internal.telephony.euicc.ILoadBoundProfilePackageCallback;
import com.android.internal.telephony.euicc.IPrepareDownloadCallback;
import com.android.internal.telephony.euicc.IRemoveNotificationFromListCallback;
import com.android.internal.telephony.euicc.IResetMemoryCallback;
import com.android.internal.telephony.euicc.IRetrieveNotificationCallback;
import com.android.internal.telephony.euicc.IRetrieveNotificationListCallback;
import com.android.internal.telephony.euicc.ISetDefaultSmdpAddressCallback;
import com.android.internal.telephony.euicc.ISetNicknameCallback;
import com.android.internal.telephony.euicc.ISwitchToProfileCallback;

public interface IEuiccCardController extends IInterface {
    void authenticateServer(String str, String str2, String str3, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, IAuthenticateServerCallback iAuthenticateServerCallback) throws RemoteException;

    void cancelSession(String str, String str2, byte[] bArr, int i, ICancelSessionCallback iCancelSessionCallback) throws RemoteException;

    void deleteProfile(String str, String str2, String str3, IDeleteProfileCallback iDeleteProfileCallback) throws RemoteException;

    void disableProfile(String str, String str2, String str3, boolean z, IDisableProfileCallback iDisableProfileCallback) throws RemoteException;

    void getAllProfiles(String str, String str2, IGetAllProfilesCallback iGetAllProfilesCallback) throws RemoteException;

    void getDefaultSmdpAddress(String str, String str2, IGetDefaultSmdpAddressCallback iGetDefaultSmdpAddressCallback) throws RemoteException;

    void getEuiccChallenge(String str, String str2, IGetEuiccChallengeCallback iGetEuiccChallengeCallback) throws RemoteException;

    void getEuiccInfo1(String str, String str2, IGetEuiccInfo1Callback iGetEuiccInfo1Callback) throws RemoteException;

    void getEuiccInfo2(String str, String str2, IGetEuiccInfo2Callback iGetEuiccInfo2Callback) throws RemoteException;

    void getProfile(String str, String str2, String str3, IGetProfileCallback iGetProfileCallback) throws RemoteException;

    void getRulesAuthTable(String str, String str2, IGetRulesAuthTableCallback iGetRulesAuthTableCallback) throws RemoteException;

    void getSmdsAddress(String str, String str2, IGetSmdsAddressCallback iGetSmdsAddressCallback) throws RemoteException;

    void listNotifications(String str, String str2, int i, IListNotificationsCallback iListNotificationsCallback) throws RemoteException;

    void loadBoundProfilePackage(String str, String str2, byte[] bArr, ILoadBoundProfilePackageCallback iLoadBoundProfilePackageCallback) throws RemoteException;

    void prepareDownload(String str, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, IPrepareDownloadCallback iPrepareDownloadCallback) throws RemoteException;

    void removeNotificationFromList(String str, String str2, int i, IRemoveNotificationFromListCallback iRemoveNotificationFromListCallback) throws RemoteException;

    void resetMemory(String str, String str2, int i, IResetMemoryCallback iResetMemoryCallback) throws RemoteException;

    void retrieveNotification(String str, String str2, int i, IRetrieveNotificationCallback iRetrieveNotificationCallback) throws RemoteException;

    void retrieveNotificationList(String str, String str2, int i, IRetrieveNotificationListCallback iRetrieveNotificationListCallback) throws RemoteException;

    void setDefaultSmdpAddress(String str, String str2, String str3, ISetDefaultSmdpAddressCallback iSetDefaultSmdpAddressCallback) throws RemoteException;

    void setNickname(String str, String str2, String str3, String str4, ISetNicknameCallback iSetNicknameCallback) throws RemoteException;

    void switchToProfile(String str, String str2, String str3, boolean z, ISwitchToProfileCallback iSwitchToProfileCallback) throws RemoteException;

    public static class Default implements IEuiccCardController {
        public void getAllProfiles(String callingPackage, String cardId, IGetAllProfilesCallback callback) throws RemoteException {
        }

        public void getProfile(String callingPackage, String cardId, String iccid, IGetProfileCallback callback) throws RemoteException {
        }

        public void disableProfile(String callingPackage, String cardId, String iccid, boolean refresh, IDisableProfileCallback callback) throws RemoteException {
        }

        public void switchToProfile(String callingPackage, String cardId, String iccid, boolean refresh, ISwitchToProfileCallback callback) throws RemoteException {
        }

        public void setNickname(String callingPackage, String cardId, String iccid, String nickname, ISetNicknameCallback callback) throws RemoteException {
        }

        public void deleteProfile(String callingPackage, String cardId, String iccid, IDeleteProfileCallback callback) throws RemoteException {
        }

        public void resetMemory(String callingPackage, String cardId, int options, IResetMemoryCallback callback) throws RemoteException {
        }

        public void getDefaultSmdpAddress(String callingPackage, String cardId, IGetDefaultSmdpAddressCallback callback) throws RemoteException {
        }

        public void getSmdsAddress(String callingPackage, String cardId, IGetSmdsAddressCallback callback) throws RemoteException {
        }

        public void setDefaultSmdpAddress(String callingPackage, String cardId, String address, ISetDefaultSmdpAddressCallback callback) throws RemoteException {
        }

        public void getRulesAuthTable(String callingPackage, String cardId, IGetRulesAuthTableCallback callback) throws RemoteException {
        }

        public void getEuiccChallenge(String callingPackage, String cardId, IGetEuiccChallengeCallback callback) throws RemoteException {
        }

        public void getEuiccInfo1(String callingPackage, String cardId, IGetEuiccInfo1Callback callback) throws RemoteException {
        }

        public void getEuiccInfo2(String callingPackage, String cardId, IGetEuiccInfo2Callback callback) throws RemoteException {
        }

        public void authenticateServer(String callingPackage, String cardId, String matchingId, byte[] serverSigned1, byte[] serverSignature1, byte[] euiccCiPkIdToBeUsed, byte[] serverCertificatein, IAuthenticateServerCallback callback) throws RemoteException {
        }

        public void prepareDownload(String callingPackage, String cardId, byte[] hashCc, byte[] smdpSigned2, byte[] smdpSignature2, byte[] smdpCertificate, IPrepareDownloadCallback callback) throws RemoteException {
        }

        public void loadBoundProfilePackage(String callingPackage, String cardId, byte[] boundProfilePackage, ILoadBoundProfilePackageCallback callback) throws RemoteException {
        }

        public void cancelSession(String callingPackage, String cardId, byte[] transactionId, int reason, ICancelSessionCallback callback) throws RemoteException {
        }

        public void listNotifications(String callingPackage, String cardId, int events, IListNotificationsCallback callback) throws RemoteException {
        }

        public void retrieveNotificationList(String callingPackage, String cardId, int events, IRetrieveNotificationListCallback callback) throws RemoteException {
        }

        public void retrieveNotification(String callingPackage, String cardId, int seqNumber, IRetrieveNotificationCallback callback) throws RemoteException {
        }

        public void removeNotificationFromList(String callingPackage, String cardId, int seqNumber, IRemoveNotificationFromListCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IEuiccCardController {
        private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccCardController";
        static final int TRANSACTION_authenticateServer = 15;
        static final int TRANSACTION_cancelSession = 18;
        static final int TRANSACTION_deleteProfile = 6;
        static final int TRANSACTION_disableProfile = 3;
        static final int TRANSACTION_getAllProfiles = 1;
        static final int TRANSACTION_getDefaultSmdpAddress = 8;
        static final int TRANSACTION_getEuiccChallenge = 12;
        static final int TRANSACTION_getEuiccInfo1 = 13;
        static final int TRANSACTION_getEuiccInfo2 = 14;
        static final int TRANSACTION_getProfile = 2;
        static final int TRANSACTION_getRulesAuthTable = 11;
        static final int TRANSACTION_getSmdsAddress = 9;
        static final int TRANSACTION_listNotifications = 19;
        static final int TRANSACTION_loadBoundProfilePackage = 17;
        static final int TRANSACTION_prepareDownload = 16;
        static final int TRANSACTION_removeNotificationFromList = 22;
        static final int TRANSACTION_resetMemory = 7;
        static final int TRANSACTION_retrieveNotification = 21;
        static final int TRANSACTION_retrieveNotificationList = 20;
        static final int TRANSACTION_setDefaultSmdpAddress = 10;
        static final int TRANSACTION_setNickname = 5;
        static final int TRANSACTION_switchToProfile = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEuiccCardController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEuiccCardController)) {
                return new Proxy(obj);
            }
            return (IEuiccCardController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAllProfiles";
                case 2:
                    return "getProfile";
                case 3:
                    return "disableProfile";
                case 4:
                    return "switchToProfile";
                case 5:
                    return "setNickname";
                case 6:
                    return "deleteProfile";
                case 7:
                    return "resetMemory";
                case 8:
                    return "getDefaultSmdpAddress";
                case 9:
                    return "getSmdsAddress";
                case 10:
                    return "setDefaultSmdpAddress";
                case 11:
                    return "getRulesAuthTable";
                case 12:
                    return "getEuiccChallenge";
                case 13:
                    return "getEuiccInfo1";
                case 14:
                    return "getEuiccInfo2";
                case 15:
                    return "authenticateServer";
                case 16:
                    return "prepareDownload";
                case 17:
                    return "loadBoundProfilePackage";
                case 18:
                    return "cancelSession";
                case 19:
                    return "listNotifications";
                case 20:
                    return "retrieveNotificationList";
                case 21:
                    return "retrieveNotification";
                case 22:
                    return "removeNotificationFromList";
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
                        getAllProfiles(data.readString(), data.readString(), IGetAllProfilesCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        getProfile(data.readString(), data.readString(), data.readString(), IGetProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        disableProfile(data.readString(), data.readString(), data.readString(), data.readInt() != 0, IDisableProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        switchToProfile(data.readString(), data.readString(), data.readString(), data.readInt() != 0, ISwitchToProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        setNickname(data.readString(), data.readString(), data.readString(), data.readString(), ISetNicknameCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        deleteProfile(data.readString(), data.readString(), data.readString(), IDeleteProfileCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        resetMemory(data.readString(), data.readString(), data.readInt(), IResetMemoryCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 8:
                        parcel.enforceInterface(DESCRIPTOR);
                        getDefaultSmdpAddress(data.readString(), data.readString(), IGetDefaultSmdpAddressCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        getSmdsAddress(data.readString(), data.readString(), IGetSmdsAddressCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        setDefaultSmdpAddress(data.readString(), data.readString(), data.readString(), ISetDefaultSmdpAddressCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 11:
                        parcel.enforceInterface(DESCRIPTOR);
                        getRulesAuthTable(data.readString(), data.readString(), IGetRulesAuthTableCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 12:
                        parcel.enforceInterface(DESCRIPTOR);
                        getEuiccChallenge(data.readString(), data.readString(), IGetEuiccChallengeCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 13:
                        parcel.enforceInterface(DESCRIPTOR);
                        getEuiccInfo1(data.readString(), data.readString(), IGetEuiccInfo1Callback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 14:
                        parcel.enforceInterface(DESCRIPTOR);
                        getEuiccInfo2(data.readString(), data.readString(), IGetEuiccInfo2Callback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 15:
                        parcel.enforceInterface(DESCRIPTOR);
                        authenticateServer(data.readString(), data.readString(), data.readString(), data.createByteArray(), data.createByteArray(), data.createByteArray(), data.createByteArray(), IAuthenticateServerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 16:
                        parcel.enforceInterface(DESCRIPTOR);
                        prepareDownload(data.readString(), data.readString(), data.createByteArray(), data.createByteArray(), data.createByteArray(), data.createByteArray(), IPrepareDownloadCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 17:
                        parcel.enforceInterface(DESCRIPTOR);
                        loadBoundProfilePackage(data.readString(), data.readString(), data.createByteArray(), ILoadBoundProfilePackageCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 18:
                        parcel.enforceInterface(DESCRIPTOR);
                        cancelSession(data.readString(), data.readString(), data.createByteArray(), data.readInt(), ICancelSessionCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 19:
                        parcel.enforceInterface(DESCRIPTOR);
                        listNotifications(data.readString(), data.readString(), data.readInt(), IListNotificationsCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 20:
                        parcel.enforceInterface(DESCRIPTOR);
                        retrieveNotificationList(data.readString(), data.readString(), data.readInt(), IRetrieveNotificationListCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 21:
                        parcel.enforceInterface(DESCRIPTOR);
                        retrieveNotification(data.readString(), data.readString(), data.readInt(), IRetrieveNotificationCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 22:
                        parcel.enforceInterface(DESCRIPTOR);
                        removeNotificationFromList(data.readString(), data.readString(), data.readInt(), IRemoveNotificationFromListCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IEuiccCardController {
            public static IEuiccCardController sDefaultImpl;
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

            public void getAllProfiles(String callingPackage, String cardId, IGetAllProfilesCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getAllProfiles(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getProfile(String callingPackage, String cardId, String iccid, IGetProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getProfile(callingPackage, cardId, iccid, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disableProfile(String callingPackage, String cardId, String iccid, boolean refresh, IDisableProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeInt(refresh);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disableProfile(callingPackage, cardId, iccid, refresh, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void switchToProfile(String callingPackage, String cardId, String iccid, boolean refresh, ISwitchToProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeInt(refresh);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().switchToProfile(callingPackage, cardId, iccid, refresh, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setNickname(String callingPackage, String cardId, String iccid, String nickname, ISetNicknameCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeString(nickname);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setNickname(callingPackage, cardId, iccid, nickname, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deleteProfile(String callingPackage, String cardId, String iccid, IDeleteProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deleteProfile(callingPackage, cardId, iccid, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void resetMemory(String callingPackage, String cardId, int options, IResetMemoryCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(options);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(7, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().resetMemory(callingPackage, cardId, options, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getDefaultSmdpAddress(String callingPackage, String cardId, IGetDefaultSmdpAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(8, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getDefaultSmdpAddress(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getSmdsAddress(String callingPackage, String cardId, IGetSmdsAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(9, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getSmdsAddress(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setDefaultSmdpAddress(String callingPackage, String cardId, String address, ISetDefaultSmdpAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(address);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(10, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setDefaultSmdpAddress(callingPackage, cardId, address, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getRulesAuthTable(String callingPackage, String cardId, IGetRulesAuthTableCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(11, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getRulesAuthTable(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccChallenge(String callingPackage, String cardId, IGetEuiccChallengeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(12, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccChallenge(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccInfo1(String callingPackage, String cardId, IGetEuiccInfo1Callback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(13, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccInfo1(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccInfo2(String callingPackage, String cardId, IGetEuiccInfo2Callback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(14, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccInfo2(callingPackage, cardId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void authenticateServer(String callingPackage, String cardId, String matchingId, byte[] serverSigned1, byte[] serverSignature1, byte[] euiccCiPkIdToBeUsed, byte[] serverCertificatein, IAuthenticateServerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th) {
                        th = th;
                        String str = cardId;
                        String str2 = matchingId;
                        byte[] bArr = serverSigned1;
                        byte[] bArr2 = serverSignature1;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(cardId);
                        try {
                            _data.writeString(matchingId);
                        } catch (Throwable th2) {
                            th = th2;
                            byte[] bArr3 = serverSigned1;
                            byte[] bArr22 = serverSignature1;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeByteArray(serverSigned1);
                        } catch (Throwable th3) {
                            th = th3;
                            byte[] bArr222 = serverSignature1;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        String str22 = matchingId;
                        byte[] bArr32 = serverSigned1;
                        byte[] bArr2222 = serverSignature1;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(serverSignature1);
                        _data.writeByteArray(euiccCiPkIdToBeUsed);
                        _data.writeByteArray(serverCertificatein);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        if (this.mRemote.transact(15, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().authenticateServer(callingPackage, cardId, matchingId, serverSigned1, serverSignature1, euiccCiPkIdToBeUsed, serverCertificatein, callback);
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str3 = callingPackage;
                    String str4 = cardId;
                    String str222 = matchingId;
                    byte[] bArr322 = serverSigned1;
                    byte[] bArr22222 = serverSignature1;
                    _data.recycle();
                    throw th;
                }
            }

            public void prepareDownload(String callingPackage, String cardId, byte[] hashCc, byte[] smdpSigned2, byte[] smdpSignature2, byte[] smdpCertificate, IPrepareDownloadCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th) {
                        th = th;
                        String str = cardId;
                        byte[] bArr = hashCc;
                        byte[] bArr2 = smdpSigned2;
                        byte[] bArr3 = smdpSignature2;
                        byte[] bArr4 = smdpCertificate;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(cardId);
                        try {
                            _data.writeByteArray(hashCc);
                            try {
                                _data.writeByteArray(smdpSigned2);
                            } catch (Throwable th2) {
                                th = th2;
                                byte[] bArr32 = smdpSignature2;
                                byte[] bArr42 = smdpCertificate;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            byte[] bArr22 = smdpSigned2;
                            byte[] bArr322 = smdpSignature2;
                            byte[] bArr422 = smdpCertificate;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        byte[] bArr5 = hashCc;
                        byte[] bArr222 = smdpSigned2;
                        byte[] bArr3222 = smdpSignature2;
                        byte[] bArr4222 = smdpCertificate;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(smdpSignature2);
                        try {
                            _data.writeByteArray(smdpCertificate);
                            _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                            if (this.mRemote.transact(16, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().prepareDownload(callingPackage, cardId, hashCc, smdpSigned2, smdpSignature2, smdpCertificate, callback);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        byte[] bArr42222 = smdpCertificate;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = callingPackage;
                    String str3 = cardId;
                    byte[] bArr52 = hashCc;
                    byte[] bArr2222 = smdpSigned2;
                    byte[] bArr32222 = smdpSignature2;
                    byte[] bArr422222 = smdpCertificate;
                    _data.recycle();
                    throw th;
                }
            }

            public void loadBoundProfilePackage(String callingPackage, String cardId, byte[] boundProfilePackage, ILoadBoundProfilePackageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeByteArray(boundProfilePackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(17, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().loadBoundProfilePackage(callingPackage, cardId, boundProfilePackage, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void cancelSession(String callingPackage, String cardId, byte[] transactionId, int reason, ICancelSessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeByteArray(transactionId);
                    _data.writeInt(reason);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(18, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelSession(callingPackage, cardId, transactionId, reason, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void listNotifications(String callingPackage, String cardId, int events, IListNotificationsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(events);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(19, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().listNotifications(callingPackage, cardId, events, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void retrieveNotificationList(String callingPackage, String cardId, int events, IRetrieveNotificationListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(events);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(20, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().retrieveNotificationList(callingPackage, cardId, events, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void retrieveNotification(String callingPackage, String cardId, int seqNumber, IRetrieveNotificationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(seqNumber);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(21, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().retrieveNotification(callingPackage, cardId, seqNumber, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeNotificationFromList(String callingPackage, String cardId, int seqNumber, IRemoveNotificationFromListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(seqNumber);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(22, _data, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeNotificationFromList(callingPackage, cardId, seqNumber, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IEuiccCardController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IEuiccCardController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
