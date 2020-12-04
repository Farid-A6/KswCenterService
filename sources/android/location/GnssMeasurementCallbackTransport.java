package android.location;

import android.content.Context;
import android.location.GnssMeasurementsEvent;
import android.location.IGnssMeasurementsListener;
import android.location.LocalListenerHelper;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;

class GnssMeasurementCallbackTransport extends LocalListenerHelper<GnssMeasurementsEvent.Callback> {
    private static final String TAG = "GnssMeasCbTransport";
    private final IGnssMeasurementsListener mListenerTransport = new ListenerTransport();
    private final ILocationManager mLocationManager;

    public GnssMeasurementCallbackTransport(Context context, ILocationManager locationManager) {
        super(context, TAG);
        this.mLocationManager = locationManager;
    }

    /* access modifiers changed from: protected */
    public boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGnssMeasurementsListener(this.mListenerTransport, getContext().getPackageName());
    }

    /* access modifiers changed from: protected */
    public void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGnssMeasurementsListener(this.mListenerTransport);
    }

    /* access modifiers changed from: protected */
    public void injectGnssMeasurementCorrections(GnssMeasurementCorrections measurementCorrections) throws RemoteException {
        Preconditions.checkNotNull(measurementCorrections);
        this.mLocationManager.injectGnssMeasurementCorrections(measurementCorrections, getContext().getPackageName());
    }

    /* access modifiers changed from: protected */
    public long getGnssCapabilities() throws RemoteException {
        return this.mLocationManager.getGnssCapabilities(getContext().getPackageName());
    }

    private class ListenerTransport extends IGnssMeasurementsListener.Stub {
        private ListenerTransport() {
        }

        public void onGnssMeasurementsReceived(final GnssMeasurementsEvent event) {
            GnssMeasurementCallbackTransport.this.foreach(new LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback>() {
                public void execute(GnssMeasurementsEvent.Callback callback) throws RemoteException {
                    callback.onGnssMeasurementsReceived(event);
                }
            });
        }

        public void onStatusChanged(final int status) {
            GnssMeasurementCallbackTransport.this.foreach(new LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback>() {
                public void execute(GnssMeasurementsEvent.Callback callback) throws RemoteException {
                    callback.onStatusChanged(status);
                }
            });
        }
    }
}
