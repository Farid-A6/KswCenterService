package android.telephony.ims;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.aidl.IRcs;

class RcsControllerCall {
    private final Context mContext;

    interface RcsServiceCall<R> {
        R methodOnIRcs(IRcs iRcs, String str) throws RemoteException;
    }

    interface RcsServiceCallWithNoReturn {
        void methodOnIRcs(IRcs iRcs, String str) throws RemoteException;
    }

    RcsControllerCall(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: package-private */
    public <R> R call(RcsServiceCall<R> serviceCall) throws RcsMessageStoreException {
        IRcs iRcs = IRcs.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_RCS_SERVICE));
        if (iRcs != null) {
            try {
                return serviceCall.methodOnIRcs(iRcs, this.mContext.getOpPackageName());
            } catch (RemoteException exception) {
                throw new RcsMessageStoreException(exception.getMessage());
            }
        } else {
            throw new RcsMessageStoreException("Could not connect to RCS storage service");
        }
    }

    /* access modifiers changed from: package-private */
    public void callWithNoReturn(RcsServiceCallWithNoReturn serviceCall) throws RcsMessageStoreException {
        call(new RcsServiceCall() {
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsControllerCall.RcsServiceCallWithNoReturn.this.methodOnIRcs(iRcs, str);
            }
        });
    }
}
