package com.android.server.wifi.nano;

import android.app.admin.DevicePolicyManager;
import android.util.DisplayMetrics;
import com.android.framework.protobuf.nano.CodedInputByteBufferNano;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.android.framework.protobuf.nano.InternalNano;
import com.android.framework.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.framework.protobuf.nano.WireFormatNano;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.telephony.RILConstants;
import java.io.IOException;

public interface WifiMetricsProto {

    public static final class WifiLog extends MessageNano {
        public static final int FAILURE_WIFI_DISABLED = 4;
        public static final int SCAN_FAILURE_INTERRUPTED = 2;
        public static final int SCAN_FAILURE_INVALID_CONFIGURATION = 3;
        public static final int SCAN_SUCCESS = 1;
        public static final int SCAN_UNKNOWN = 0;
        public static final int WIFI_ASSOCIATED = 3;
        public static final int WIFI_DISABLED = 1;
        public static final int WIFI_DISCONNECTED = 2;
        public static final int WIFI_UNKNOWN = 0;
        private static volatile WifiLog[] _emptyArray;
        public AlertReasonCount[] alertReasonCount;
        public NumConnectableNetworksBucket[] availableOpenBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableOpenOrSavedBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableOpenOrSavedSsidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableOpenSsidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedPasspointProviderBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedPasspointProviderProfilesInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedSsidsInScanHistogram;
        public WifiSystemStateEntry[] backgroundScanRequestState;
        public ScanReturnEntry[] backgroundScanReturnEntries;
        public ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationActionCount;
        public ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationCount;
        public ConnectionEvent[] connectionEvent;
        public ExperimentValues experimentValues;
        public int fullBandAllSingleScanListenerResults;
        public String hardwareRevision;
        public PasspointProfileTypeCount[] installedPasspointProfileTypeForR1;
        public PasspointProfileTypeCount[] installedPasspointProfileTypeForR2;
        public boolean isLocationEnabled;
        public boolean isMacRandomizationOn;
        public boolean isScanningAlwaysEnabled;
        public boolean isWifiNetworksAvailableNotificationOn;
        public LinkProbeStats linkProbeStats;
        public LinkSpeedCount[] linkSpeedCounts;
        public DeviceMobilityStatePnoScanStats[] mobilityStatePnoStatsList;
        public NetworkSelectionExperimentDecisions[] networkSelectionExperimentDecisionsList;
        public int numAddOrUpdateNetworkCalls;
        public int numBackgroundScans;
        public int numClientInterfaceDown;
        public int numConnectivityOneshotScans;
        public int numConnectivityWatchdogBackgroundBad;
        public int numConnectivityWatchdogBackgroundGood;
        public int numConnectivityWatchdogPnoBad;
        public int numConnectivityWatchdogPnoGood;
        public int numEmptyScanResults;
        public int numEnableNetworkCalls;
        public int numEnhancedOpenNetworkScanResults;
        public int numEnhancedOpenNetworks;
        public int numExternalAppOneshotScanRequests;
        public int numExternalBackgroundAppOneshotScanRequestsThrottled;
        public int numExternalForegroundAppOneshotScanRequestsThrottled;
        public int numHalCrashes;
        public int numHiddenNetworkScanResults;
        public int numHiddenNetworks;
        public int numHostapdCrashes;
        public int numHotspot2R1NetworkScanResults;
        public int numHotspot2R2NetworkScanResults;
        public int numLastResortWatchdogAvailableNetworksTotal;
        public int numLastResortWatchdogBadAssociationNetworksTotal;
        public int numLastResortWatchdogBadAuthenticationNetworksTotal;
        public int numLastResortWatchdogBadDhcpNetworksTotal;
        public int numLastResortWatchdogBadOtherNetworksTotal;
        public int numLastResortWatchdogSuccesses;
        public int numLastResortWatchdogTriggers;
        public int numLastResortWatchdogTriggersWithBadAssociation;
        public int numLastResortWatchdogTriggersWithBadAuthentication;
        public int numLastResortWatchdogTriggersWithBadDhcp;
        public int numLastResortWatchdogTriggersWithBadOther;
        public int numLegacyEnterpriseNetworkScanResults;
        public int numLegacyEnterpriseNetworks;
        public int numLegacyPersonalNetworkScanResults;
        public int numLegacyPersonalNetworks;
        public int numNetworksAddedByApps;
        public int numNetworksAddedByUser;
        public int numNonEmptyScanResults;
        public int numOneshotHasDfsChannelScans;
        public int numOneshotScans;
        public int numOpenNetworkConnectMessageFailedToSend;
        public int numOpenNetworkRecommendationUpdates;
        public int numOpenNetworkScanResults;
        public int numOpenNetworks;
        public int numPasspointNetworks;
        public int numPasspointProviderInstallSuccess;
        public int numPasspointProviderInstallation;
        public int numPasspointProviderUninstallSuccess;
        public int numPasspointProviderUninstallation;
        public int numPasspointProviders;
        public int numPasspointProvidersSuccessfullyConnected;
        public int numRadioModeChangeToDbs;
        public int numRadioModeChangeToMcc;
        public int numRadioModeChangeToSbs;
        public int numRadioModeChangeToScc;
        public int numSarSensorRegistrationFailures;
        public int numSavedNetworks;
        public int numSavedNetworksWithMacRandomization;
        public int numScans;
        public int numSetupClientInterfaceFailureDueToHal;
        public int numSetupClientInterfaceFailureDueToSupplicant;
        public int numSetupClientInterfaceFailureDueToWificond;
        public int numSetupSoftApInterfaceFailureDueToHal;
        public int numSetupSoftApInterfaceFailureDueToHostapd;
        public int numSetupSoftApInterfaceFailureDueToWificond;
        public int numSoftApInterfaceDown;
        public int numSoftApUserBandPreferenceUnsatisfied;
        public int numSupplicantCrashes;
        public int numTotalScanResults;
        public int numWifiToggledViaAirplane;
        public int numWifiToggledViaSettings;
        public int numWificondCrashes;
        public int numWpa3EnterpriseNetworkScanResults;
        public int numWpa3EnterpriseNetworks;
        public int numWpa3PersonalNetworkScanResults;
        public int numWpa3PersonalNetworks;
        public NumConnectableNetworksBucket[] observed80211McSupportingApsInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR1ApsInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR1ApsPerEssInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR1EssInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR2ApsInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR2ApsPerEssInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR2EssInScanHistogram;
        public int openNetworkRecommenderBlacklistSize;
        public int partialAllSingleScanListenerResults;
        public PasspointProvisionStats passpointProvisionStats;
        public PnoScanMetrics pnoScanMetrics;
        public int recordDurationSec;
        public RssiPollCount[] rssiPollDeltaCount;
        public RssiPollCount[] rssiPollRssiCount;
        public ScanReturnEntry[] scanReturnEntries;
        public String scoreExperimentId;
        public SoftApConnectedClientsEvent[] softApConnectedClientsEventsLocalOnly;
        public SoftApConnectedClientsEvent[] softApConnectedClientsEventsTethered;
        public SoftApDurationBucket[] softApDuration;
        public SoftApReturnCodeCount[] softApReturnCode;
        public StaEvent[] staEventList;
        public NumConnectableNetworksBucket[] totalBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] totalSsidsInScanHistogram;
        public long watchdogTotalConnectionFailureCountAfterTrigger;
        public long watchdogTriggerToConnectionSuccessDurationMs;
        public WifiAwareLog wifiAwareLog;
        public WifiConfigStoreIO wifiConfigStoreIo;
        public WifiDppLog wifiDppLog;
        public WifiIsUnusableEvent[] wifiIsUnusableEventList;
        public WifiLinkLayerUsageStats wifiLinkLayerUsageStats;
        public WifiLockStats wifiLockStats;
        public WifiNetworkRequestApiLog wifiNetworkRequestApiLog;
        public WifiNetworkSuggestionApiLog wifiNetworkSuggestionApiLog;
        public WifiP2pStats wifiP2PStats;
        public WifiPowerStats wifiPowerStats;
        public WifiRadioUsage wifiRadioUsage;
        public WifiRttLog wifiRttLog;
        public WifiScoreCount[] wifiScoreCount;
        public WifiSystemStateEntry[] wifiSystemStateEntries;
        public WifiToggleStats wifiToggleStats;
        public WifiUsabilityScoreCount[] wifiUsabilityScoreCount;
        public WifiUsabilityStats[] wifiUsabilityStatsList;
        public WifiWakeStats wifiWakeStats;
        public WpsMetrics wpsMetrics;

        public static final class ScanReturnEntry extends MessageNano {
            private static volatile ScanReturnEntry[] _emptyArray;
            public int scanResultsCount;
            public int scanReturnCode;

            public static ScanReturnEntry[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ScanReturnEntry[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ScanReturnEntry() {
                clear();
            }

            public ScanReturnEntry clear() {
                this.scanReturnCode = 0;
                this.scanResultsCount = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.scanReturnCode != 0) {
                    output.writeInt32(1, this.scanReturnCode);
                }
                if (this.scanResultsCount != 0) {
                    output.writeInt32(2, this.scanResultsCount);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.scanReturnCode != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.scanReturnCode);
                }
                if (this.scanResultsCount != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.scanResultsCount);
                }
                return size;
            }

            public ScanReturnEntry mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    this.scanReturnCode = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.scanResultsCount = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static ScanReturnEntry parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ScanReturnEntry) MessageNano.mergeFrom(new ScanReturnEntry(), data);
            }

            public static ScanReturnEntry parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ScanReturnEntry().mergeFrom(input);
            }
        }

        public static final class WifiSystemStateEntry extends MessageNano {
            private static volatile WifiSystemStateEntry[] _emptyArray;
            public boolean isScreenOn;
            public int wifiState;
            public int wifiStateCount;

            public static WifiSystemStateEntry[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new WifiSystemStateEntry[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public WifiSystemStateEntry() {
                clear();
            }

            public WifiSystemStateEntry clear() {
                this.wifiState = 0;
                this.wifiStateCount = 0;
                this.isScreenOn = false;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.wifiState != 0) {
                    output.writeInt32(1, this.wifiState);
                }
                if (this.wifiStateCount != 0) {
                    output.writeInt32(2, this.wifiStateCount);
                }
                if (this.isScreenOn) {
                    output.writeBool(3, this.isScreenOn);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.wifiState != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.wifiState);
                }
                if (this.wifiStateCount != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, this.wifiStateCount);
                }
                if (this.isScreenOn) {
                    return size + CodedOutputByteBufferNano.computeBoolSize(3, this.isScreenOn);
                }
                return size;
            }

            public WifiSystemStateEntry mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                    this.wifiState = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.wifiStateCount = input.readInt32();
                        } else if (tag == 24) {
                            this.isScreenOn = input.readBool();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static WifiSystemStateEntry parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (WifiSystemStateEntry) MessageNano.mergeFrom(new WifiSystemStateEntry(), data);
            }

            public static WifiSystemStateEntry parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new WifiSystemStateEntry().mergeFrom(input);
            }
        }

        public static WifiLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiLog() {
            clear();
        }

        public WifiLog clear() {
            this.connectionEvent = ConnectionEvent.emptyArray();
            this.numSavedNetworks = 0;
            this.numOpenNetworks = 0;
            this.numLegacyPersonalNetworks = 0;
            this.numLegacyEnterpriseNetworks = 0;
            this.isLocationEnabled = false;
            this.isScanningAlwaysEnabled = false;
            this.numWifiToggledViaSettings = 0;
            this.numWifiToggledViaAirplane = 0;
            this.numNetworksAddedByUser = 0;
            this.numNetworksAddedByApps = 0;
            this.numEmptyScanResults = 0;
            this.numNonEmptyScanResults = 0;
            this.numOneshotScans = 0;
            this.numBackgroundScans = 0;
            this.scanReturnEntries = ScanReturnEntry.emptyArray();
            this.wifiSystemStateEntries = WifiSystemStateEntry.emptyArray();
            this.backgroundScanReturnEntries = ScanReturnEntry.emptyArray();
            this.backgroundScanRequestState = WifiSystemStateEntry.emptyArray();
            this.numLastResortWatchdogTriggers = 0;
            this.numLastResortWatchdogBadAssociationNetworksTotal = 0;
            this.numLastResortWatchdogBadAuthenticationNetworksTotal = 0;
            this.numLastResortWatchdogBadDhcpNetworksTotal = 0;
            this.numLastResortWatchdogBadOtherNetworksTotal = 0;
            this.numLastResortWatchdogAvailableNetworksTotal = 0;
            this.numLastResortWatchdogTriggersWithBadAssociation = 0;
            this.numLastResortWatchdogTriggersWithBadAuthentication = 0;
            this.numLastResortWatchdogTriggersWithBadDhcp = 0;
            this.numLastResortWatchdogTriggersWithBadOther = 0;
            this.numConnectivityWatchdogPnoGood = 0;
            this.numConnectivityWatchdogPnoBad = 0;
            this.numConnectivityWatchdogBackgroundGood = 0;
            this.numConnectivityWatchdogBackgroundBad = 0;
            this.recordDurationSec = 0;
            this.rssiPollRssiCount = RssiPollCount.emptyArray();
            this.numLastResortWatchdogSuccesses = 0;
            this.numHiddenNetworks = 0;
            this.numPasspointNetworks = 0;
            this.numTotalScanResults = 0;
            this.numOpenNetworkScanResults = 0;
            this.numLegacyPersonalNetworkScanResults = 0;
            this.numLegacyEnterpriseNetworkScanResults = 0;
            this.numHiddenNetworkScanResults = 0;
            this.numHotspot2R1NetworkScanResults = 0;
            this.numHotspot2R2NetworkScanResults = 0;
            this.numScans = 0;
            this.alertReasonCount = AlertReasonCount.emptyArray();
            this.wifiScoreCount = WifiScoreCount.emptyArray();
            this.softApDuration = SoftApDurationBucket.emptyArray();
            this.softApReturnCode = SoftApReturnCodeCount.emptyArray();
            this.rssiPollDeltaCount = RssiPollCount.emptyArray();
            this.staEventList = StaEvent.emptyArray();
            this.numHalCrashes = 0;
            this.numWificondCrashes = 0;
            this.numSetupClientInterfaceFailureDueToHal = 0;
            this.numSetupClientInterfaceFailureDueToWificond = 0;
            this.wifiAwareLog = null;
            this.numPasspointProviders = 0;
            this.numPasspointProviderInstallation = 0;
            this.numPasspointProviderInstallSuccess = 0;
            this.numPasspointProviderUninstallation = 0;
            this.numPasspointProviderUninstallSuccess = 0;
            this.numPasspointProvidersSuccessfullyConnected = 0;
            this.totalSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.totalBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenOrSavedSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenOrSavedBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedPasspointProviderProfilesInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedPasspointProviderBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.fullBandAllSingleScanListenerResults = 0;
            this.partialAllSingleScanListenerResults = 0;
            this.pnoScanMetrics = null;
            this.connectToNetworkNotificationCount = ConnectToNetworkNotificationAndActionCount.emptyArray();
            this.connectToNetworkNotificationActionCount = ConnectToNetworkNotificationAndActionCount.emptyArray();
            this.openNetworkRecommenderBlacklistSize = 0;
            this.isWifiNetworksAvailableNotificationOn = false;
            this.numOpenNetworkRecommendationUpdates = 0;
            this.numOpenNetworkConnectMessageFailedToSend = 0;
            this.observedHotspotR1ApsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR2ApsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR1EssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR2EssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR1ApsPerEssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR2ApsPerEssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.softApConnectedClientsEventsTethered = SoftApConnectedClientsEvent.emptyArray();
            this.softApConnectedClientsEventsLocalOnly = SoftApConnectedClientsEvent.emptyArray();
            this.wpsMetrics = null;
            this.wifiPowerStats = null;
            this.numConnectivityOneshotScans = 0;
            this.wifiWakeStats = null;
            this.observed80211McSupportingApsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.numSupplicantCrashes = 0;
            this.numHostapdCrashes = 0;
            this.numSetupClientInterfaceFailureDueToSupplicant = 0;
            this.numSetupSoftApInterfaceFailureDueToHal = 0;
            this.numSetupSoftApInterfaceFailureDueToWificond = 0;
            this.numSetupSoftApInterfaceFailureDueToHostapd = 0;
            this.numClientInterfaceDown = 0;
            this.numSoftApInterfaceDown = 0;
            this.numExternalAppOneshotScanRequests = 0;
            this.numExternalForegroundAppOneshotScanRequestsThrottled = 0;
            this.numExternalBackgroundAppOneshotScanRequestsThrottled = 0;
            this.watchdogTriggerToConnectionSuccessDurationMs = -1;
            this.watchdogTotalConnectionFailureCountAfterTrigger = 0;
            this.numOneshotHasDfsChannelScans = 0;
            this.wifiRttLog = null;
            this.isMacRandomizationOn = false;
            this.numRadioModeChangeToMcc = 0;
            this.numRadioModeChangeToScc = 0;
            this.numRadioModeChangeToSbs = 0;
            this.numRadioModeChangeToDbs = 0;
            this.numSoftApUserBandPreferenceUnsatisfied = 0;
            this.scoreExperimentId = "";
            this.wifiRadioUsage = null;
            this.experimentValues = null;
            this.wifiIsUnusableEventList = WifiIsUnusableEvent.emptyArray();
            this.linkSpeedCounts = LinkSpeedCount.emptyArray();
            this.numSarSensorRegistrationFailures = 0;
            this.hardwareRevision = "";
            this.wifiLinkLayerUsageStats = null;
            this.wifiUsabilityStatsList = WifiUsabilityStats.emptyArray();
            this.wifiUsabilityScoreCount = WifiUsabilityScoreCount.emptyArray();
            this.mobilityStatePnoStatsList = DeviceMobilityStatePnoScanStats.emptyArray();
            this.wifiP2PStats = null;
            this.wifiDppLog = null;
            this.numEnhancedOpenNetworks = 0;
            this.numWpa3PersonalNetworks = 0;
            this.numWpa3EnterpriseNetworks = 0;
            this.numEnhancedOpenNetworkScanResults = 0;
            this.numWpa3PersonalNetworkScanResults = 0;
            this.numWpa3EnterpriseNetworkScanResults = 0;
            this.wifiConfigStoreIo = null;
            this.numSavedNetworksWithMacRandomization = 0;
            this.linkProbeStats = null;
            this.networkSelectionExperimentDecisionsList = NetworkSelectionExperimentDecisions.emptyArray();
            this.wifiNetworkRequestApiLog = null;
            this.wifiNetworkSuggestionApiLog = null;
            this.wifiLockStats = null;
            this.wifiToggleStats = null;
            this.numAddOrUpdateNetworkCalls = 0;
            this.numEnableNetworkCalls = 0;
            this.passpointProvisionStats = null;
            this.installedPasspointProfileTypeForR1 = PasspointProfileTypeCount.emptyArray();
            this.installedPasspointProfileTypeForR2 = PasspointProfileTypeCount.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = 0;
            if (this.connectionEvent != null && this.connectionEvent.length > 0) {
                for (ConnectionEvent element : this.connectionEvent) {
                    if (element != null) {
                        output.writeMessage(1, element);
                    }
                }
            }
            if (this.numSavedNetworks != 0) {
                output.writeInt32(2, this.numSavedNetworks);
            }
            if (this.numOpenNetworks != 0) {
                output.writeInt32(3, this.numOpenNetworks);
            }
            if (this.numLegacyPersonalNetworks != 0) {
                output.writeInt32(4, this.numLegacyPersonalNetworks);
            }
            if (this.numLegacyEnterpriseNetworks != 0) {
                output.writeInt32(5, this.numLegacyEnterpriseNetworks);
            }
            if (this.isLocationEnabled) {
                output.writeBool(6, this.isLocationEnabled);
            }
            if (this.isScanningAlwaysEnabled) {
                output.writeBool(7, this.isScanningAlwaysEnabled);
            }
            if (this.numWifiToggledViaSettings != 0) {
                output.writeInt32(8, this.numWifiToggledViaSettings);
            }
            if (this.numWifiToggledViaAirplane != 0) {
                output.writeInt32(9, this.numWifiToggledViaAirplane);
            }
            if (this.numNetworksAddedByUser != 0) {
                output.writeInt32(10, this.numNetworksAddedByUser);
            }
            if (this.numNetworksAddedByApps != 0) {
                output.writeInt32(11, this.numNetworksAddedByApps);
            }
            if (this.numEmptyScanResults != 0) {
                output.writeInt32(12, this.numEmptyScanResults);
            }
            if (this.numNonEmptyScanResults != 0) {
                output.writeInt32(13, this.numNonEmptyScanResults);
            }
            if (this.numOneshotScans != 0) {
                output.writeInt32(14, this.numOneshotScans);
            }
            if (this.numBackgroundScans != 0) {
                output.writeInt32(15, this.numBackgroundScans);
            }
            if (this.scanReturnEntries != null && this.scanReturnEntries.length > 0) {
                for (ScanReturnEntry element2 : this.scanReturnEntries) {
                    if (element2 != null) {
                        output.writeMessage(16, element2);
                    }
                }
            }
            if (this.wifiSystemStateEntries != null && this.wifiSystemStateEntries.length > 0) {
                for (WifiSystemStateEntry element3 : this.wifiSystemStateEntries) {
                    if (element3 != null) {
                        output.writeMessage(17, element3);
                    }
                }
            }
            if (this.backgroundScanReturnEntries != null && this.backgroundScanReturnEntries.length > 0) {
                for (ScanReturnEntry element4 : this.backgroundScanReturnEntries) {
                    if (element4 != null) {
                        output.writeMessage(18, element4);
                    }
                }
            }
            if (this.backgroundScanRequestState != null && this.backgroundScanRequestState.length > 0) {
                for (WifiSystemStateEntry element5 : this.backgroundScanRequestState) {
                    if (element5 != null) {
                        output.writeMessage(19, element5);
                    }
                }
            }
            if (this.numLastResortWatchdogTriggers != 0) {
                output.writeInt32(20, this.numLastResortWatchdogTriggers);
            }
            if (this.numLastResortWatchdogBadAssociationNetworksTotal != 0) {
                output.writeInt32(21, this.numLastResortWatchdogBadAssociationNetworksTotal);
            }
            if (this.numLastResortWatchdogBadAuthenticationNetworksTotal != 0) {
                output.writeInt32(22, this.numLastResortWatchdogBadAuthenticationNetworksTotal);
            }
            if (this.numLastResortWatchdogBadDhcpNetworksTotal != 0) {
                output.writeInt32(23, this.numLastResortWatchdogBadDhcpNetworksTotal);
            }
            if (this.numLastResortWatchdogBadOtherNetworksTotal != 0) {
                output.writeInt32(24, this.numLastResortWatchdogBadOtherNetworksTotal);
            }
            if (this.numLastResortWatchdogAvailableNetworksTotal != 0) {
                output.writeInt32(25, this.numLastResortWatchdogAvailableNetworksTotal);
            }
            if (this.numLastResortWatchdogTriggersWithBadAssociation != 0) {
                output.writeInt32(26, this.numLastResortWatchdogTriggersWithBadAssociation);
            }
            if (this.numLastResortWatchdogTriggersWithBadAuthentication != 0) {
                output.writeInt32(27, this.numLastResortWatchdogTriggersWithBadAuthentication);
            }
            if (this.numLastResortWatchdogTriggersWithBadDhcp != 0) {
                output.writeInt32(28, this.numLastResortWatchdogTriggersWithBadDhcp);
            }
            if (this.numLastResortWatchdogTriggersWithBadOther != 0) {
                output.writeInt32(29, this.numLastResortWatchdogTriggersWithBadOther);
            }
            if (this.numConnectivityWatchdogPnoGood != 0) {
                output.writeInt32(30, this.numConnectivityWatchdogPnoGood);
            }
            if (this.numConnectivityWatchdogPnoBad != 0) {
                output.writeInt32(31, this.numConnectivityWatchdogPnoBad);
            }
            if (this.numConnectivityWatchdogBackgroundGood != 0) {
                output.writeInt32(32, this.numConnectivityWatchdogBackgroundGood);
            }
            if (this.numConnectivityWatchdogBackgroundBad != 0) {
                output.writeInt32(33, this.numConnectivityWatchdogBackgroundBad);
            }
            if (this.recordDurationSec != 0) {
                output.writeInt32(34, this.recordDurationSec);
            }
            if (this.rssiPollRssiCount != null && this.rssiPollRssiCount.length > 0) {
                for (RssiPollCount element6 : this.rssiPollRssiCount) {
                    if (element6 != null) {
                        output.writeMessage(35, element6);
                    }
                }
            }
            if (this.numLastResortWatchdogSuccesses != 0) {
                output.writeInt32(36, this.numLastResortWatchdogSuccesses);
            }
            if (this.numHiddenNetworks != 0) {
                output.writeInt32(37, this.numHiddenNetworks);
            }
            if (this.numPasspointNetworks != 0) {
                output.writeInt32(38, this.numPasspointNetworks);
            }
            if (this.numTotalScanResults != 0) {
                output.writeInt32(39, this.numTotalScanResults);
            }
            if (this.numOpenNetworkScanResults != 0) {
                output.writeInt32(40, this.numOpenNetworkScanResults);
            }
            if (this.numLegacyPersonalNetworkScanResults != 0) {
                output.writeInt32(41, this.numLegacyPersonalNetworkScanResults);
            }
            if (this.numLegacyEnterpriseNetworkScanResults != 0) {
                output.writeInt32(42, this.numLegacyEnterpriseNetworkScanResults);
            }
            if (this.numHiddenNetworkScanResults != 0) {
                output.writeInt32(43, this.numHiddenNetworkScanResults);
            }
            if (this.numHotspot2R1NetworkScanResults != 0) {
                output.writeInt32(44, this.numHotspot2R1NetworkScanResults);
            }
            if (this.numHotspot2R2NetworkScanResults != 0) {
                output.writeInt32(45, this.numHotspot2R2NetworkScanResults);
            }
            if (this.numScans != 0) {
                output.writeInt32(46, this.numScans);
            }
            if (this.alertReasonCount != null && this.alertReasonCount.length > 0) {
                for (AlertReasonCount element7 : this.alertReasonCount) {
                    if (element7 != null) {
                        output.writeMessage(47, element7);
                    }
                }
            }
            if (this.wifiScoreCount != null && this.wifiScoreCount.length > 0) {
                for (WifiScoreCount element8 : this.wifiScoreCount) {
                    if (element8 != null) {
                        output.writeMessage(48, element8);
                    }
                }
            }
            if (this.softApDuration != null && this.softApDuration.length > 0) {
                for (SoftApDurationBucket element9 : this.softApDuration) {
                    if (element9 != null) {
                        output.writeMessage(49, element9);
                    }
                }
            }
            if (this.softApReturnCode != null && this.softApReturnCode.length > 0) {
                for (SoftApReturnCodeCount element10 : this.softApReturnCode) {
                    if (element10 != null) {
                        output.writeMessage(50, element10);
                    }
                }
            }
            if (this.rssiPollDeltaCount != null && this.rssiPollDeltaCount.length > 0) {
                for (RssiPollCount element11 : this.rssiPollDeltaCount) {
                    if (element11 != null) {
                        output.writeMessage(51, element11);
                    }
                }
            }
            if (this.staEventList != null && this.staEventList.length > 0) {
                for (StaEvent element12 : this.staEventList) {
                    if (element12 != null) {
                        output.writeMessage(52, element12);
                    }
                }
            }
            if (this.numHalCrashes != 0) {
                output.writeInt32(53, this.numHalCrashes);
            }
            if (this.numWificondCrashes != 0) {
                output.writeInt32(54, this.numWificondCrashes);
            }
            if (this.numSetupClientInterfaceFailureDueToHal != 0) {
                output.writeInt32(55, this.numSetupClientInterfaceFailureDueToHal);
            }
            if (this.numSetupClientInterfaceFailureDueToWificond != 0) {
                output.writeInt32(56, this.numSetupClientInterfaceFailureDueToWificond);
            }
            if (this.wifiAwareLog != null) {
                output.writeMessage(57, this.wifiAwareLog);
            }
            if (this.numPasspointProviders != 0) {
                output.writeInt32(58, this.numPasspointProviders);
            }
            if (this.numPasspointProviderInstallation != 0) {
                output.writeInt32(59, this.numPasspointProviderInstallation);
            }
            if (this.numPasspointProviderInstallSuccess != 0) {
                output.writeInt32(60, this.numPasspointProviderInstallSuccess);
            }
            if (this.numPasspointProviderUninstallation != 0) {
                output.writeInt32(61, this.numPasspointProviderUninstallation);
            }
            if (this.numPasspointProviderUninstallSuccess != 0) {
                output.writeInt32(62, this.numPasspointProviderUninstallSuccess);
            }
            if (this.numPasspointProvidersSuccessfullyConnected != 0) {
                output.writeInt32(63, this.numPasspointProvidersSuccessfullyConnected);
            }
            if (this.totalSsidsInScanHistogram != null && this.totalSsidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element13 : this.totalSsidsInScanHistogram) {
                    if (element13 != null) {
                        output.writeMessage(64, element13);
                    }
                }
            }
            if (this.totalBssidsInScanHistogram != null && this.totalBssidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element14 : this.totalBssidsInScanHistogram) {
                    if (element14 != null) {
                        output.writeMessage(65, element14);
                    }
                }
            }
            if (this.availableOpenSsidsInScanHistogram != null && this.availableOpenSsidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element15 : this.availableOpenSsidsInScanHistogram) {
                    if (element15 != null) {
                        output.writeMessage(66, element15);
                    }
                }
            }
            if (this.availableOpenBssidsInScanHistogram != null && this.availableOpenBssidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element16 : this.availableOpenBssidsInScanHistogram) {
                    if (element16 != null) {
                        output.writeMessage(67, element16);
                    }
                }
            }
            if (this.availableSavedSsidsInScanHistogram != null && this.availableSavedSsidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element17 : this.availableSavedSsidsInScanHistogram) {
                    if (element17 != null) {
                        output.writeMessage(68, element17);
                    }
                }
            }
            if (this.availableSavedBssidsInScanHistogram != null && this.availableSavedBssidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element18 : this.availableSavedBssidsInScanHistogram) {
                    if (element18 != null) {
                        output.writeMessage(69, element18);
                    }
                }
            }
            if (this.availableOpenOrSavedSsidsInScanHistogram != null && this.availableOpenOrSavedSsidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element19 : this.availableOpenOrSavedSsidsInScanHistogram) {
                    if (element19 != null) {
                        output.writeMessage(70, element19);
                    }
                }
            }
            if (this.availableOpenOrSavedBssidsInScanHistogram != null && this.availableOpenOrSavedBssidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element20 : this.availableOpenOrSavedBssidsInScanHistogram) {
                    if (element20 != null) {
                        output.writeMessage(71, element20);
                    }
                }
            }
            if (this.availableSavedPasspointProviderProfilesInScanHistogram != null && this.availableSavedPasspointProviderProfilesInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element21 : this.availableSavedPasspointProviderProfilesInScanHistogram) {
                    if (element21 != null) {
                        output.writeMessage(72, element21);
                    }
                }
            }
            if (this.availableSavedPasspointProviderBssidsInScanHistogram != null && this.availableSavedPasspointProviderBssidsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element22 : this.availableSavedPasspointProviderBssidsInScanHistogram) {
                    if (element22 != null) {
                        output.writeMessage(73, element22);
                    }
                }
            }
            if (this.fullBandAllSingleScanListenerResults != 0) {
                output.writeInt32(74, this.fullBandAllSingleScanListenerResults);
            }
            if (this.partialAllSingleScanListenerResults != 0) {
                output.writeInt32(75, this.partialAllSingleScanListenerResults);
            }
            if (this.pnoScanMetrics != null) {
                output.writeMessage(76, this.pnoScanMetrics);
            }
            if (this.connectToNetworkNotificationCount != null && this.connectToNetworkNotificationCount.length > 0) {
                for (ConnectToNetworkNotificationAndActionCount element23 : this.connectToNetworkNotificationCount) {
                    if (element23 != null) {
                        output.writeMessage(77, element23);
                    }
                }
            }
            if (this.connectToNetworkNotificationActionCount != null && this.connectToNetworkNotificationActionCount.length > 0) {
                for (ConnectToNetworkNotificationAndActionCount element24 : this.connectToNetworkNotificationActionCount) {
                    if (element24 != null) {
                        output.writeMessage(78, element24);
                    }
                }
            }
            if (this.openNetworkRecommenderBlacklistSize != 0) {
                output.writeInt32(79, this.openNetworkRecommenderBlacklistSize);
            }
            if (this.isWifiNetworksAvailableNotificationOn) {
                output.writeBool(80, this.isWifiNetworksAvailableNotificationOn);
            }
            if (this.numOpenNetworkRecommendationUpdates != 0) {
                output.writeInt32(81, this.numOpenNetworkRecommendationUpdates);
            }
            if (this.numOpenNetworkConnectMessageFailedToSend != 0) {
                output.writeInt32(82, this.numOpenNetworkConnectMessageFailedToSend);
            }
            if (this.observedHotspotR1ApsInScanHistogram != null && this.observedHotspotR1ApsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element25 : this.observedHotspotR1ApsInScanHistogram) {
                    if (element25 != null) {
                        output.writeMessage(83, element25);
                    }
                }
            }
            if (this.observedHotspotR2ApsInScanHistogram != null && this.observedHotspotR2ApsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element26 : this.observedHotspotR2ApsInScanHistogram) {
                    if (element26 != null) {
                        output.writeMessage(84, element26);
                    }
                }
            }
            if (this.observedHotspotR1EssInScanHistogram != null && this.observedHotspotR1EssInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element27 : this.observedHotspotR1EssInScanHistogram) {
                    if (element27 != null) {
                        output.writeMessage(85, element27);
                    }
                }
            }
            if (this.observedHotspotR2EssInScanHistogram != null && this.observedHotspotR2EssInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element28 : this.observedHotspotR2EssInScanHistogram) {
                    if (element28 != null) {
                        output.writeMessage(86, element28);
                    }
                }
            }
            if (this.observedHotspotR1ApsPerEssInScanHistogram != null && this.observedHotspotR1ApsPerEssInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element29 : this.observedHotspotR1ApsPerEssInScanHistogram) {
                    if (element29 != null) {
                        output.writeMessage(87, element29);
                    }
                }
            }
            if (this.observedHotspotR2ApsPerEssInScanHistogram != null && this.observedHotspotR2ApsPerEssInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element30 : this.observedHotspotR2ApsPerEssInScanHistogram) {
                    if (element30 != null) {
                        output.writeMessage(88, element30);
                    }
                }
            }
            if (this.softApConnectedClientsEventsTethered != null && this.softApConnectedClientsEventsTethered.length > 0) {
                for (SoftApConnectedClientsEvent element31 : this.softApConnectedClientsEventsTethered) {
                    if (element31 != null) {
                        output.writeMessage(89, element31);
                    }
                }
            }
            if (this.softApConnectedClientsEventsLocalOnly != null && this.softApConnectedClientsEventsLocalOnly.length > 0) {
                for (SoftApConnectedClientsEvent element32 : this.softApConnectedClientsEventsLocalOnly) {
                    if (element32 != null) {
                        output.writeMessage(90, element32);
                    }
                }
            }
            if (this.wpsMetrics != null) {
                output.writeMessage(91, this.wpsMetrics);
            }
            if (this.wifiPowerStats != null) {
                output.writeMessage(92, this.wifiPowerStats);
            }
            if (this.numConnectivityOneshotScans != 0) {
                output.writeInt32(93, this.numConnectivityOneshotScans);
            }
            if (this.wifiWakeStats != null) {
                output.writeMessage(94, this.wifiWakeStats);
            }
            if (this.observed80211McSupportingApsInScanHistogram != null && this.observed80211McSupportingApsInScanHistogram.length > 0) {
                for (NumConnectableNetworksBucket element33 : this.observed80211McSupportingApsInScanHistogram) {
                    if (element33 != null) {
                        output.writeMessage(95, element33);
                    }
                }
            }
            if (this.numSupplicantCrashes != 0) {
                output.writeInt32(96, this.numSupplicantCrashes);
            }
            if (this.numHostapdCrashes != 0) {
                output.writeInt32(97, this.numHostapdCrashes);
            }
            if (this.numSetupClientInterfaceFailureDueToSupplicant != 0) {
                output.writeInt32(98, this.numSetupClientInterfaceFailureDueToSupplicant);
            }
            if (this.numSetupSoftApInterfaceFailureDueToHal != 0) {
                output.writeInt32(99, this.numSetupSoftApInterfaceFailureDueToHal);
            }
            if (this.numSetupSoftApInterfaceFailureDueToWificond != 0) {
                output.writeInt32(100, this.numSetupSoftApInterfaceFailureDueToWificond);
            }
            if (this.numSetupSoftApInterfaceFailureDueToHostapd != 0) {
                output.writeInt32(101, this.numSetupSoftApInterfaceFailureDueToHostapd);
            }
            if (this.numClientInterfaceDown != 0) {
                output.writeInt32(102, this.numClientInterfaceDown);
            }
            if (this.numSoftApInterfaceDown != 0) {
                output.writeInt32(103, this.numSoftApInterfaceDown);
            }
            if (this.numExternalAppOneshotScanRequests != 0) {
                output.writeInt32(104, this.numExternalAppOneshotScanRequests);
            }
            if (this.numExternalForegroundAppOneshotScanRequestsThrottled != 0) {
                output.writeInt32(105, this.numExternalForegroundAppOneshotScanRequestsThrottled);
            }
            if (this.numExternalBackgroundAppOneshotScanRequestsThrottled != 0) {
                output.writeInt32(106, this.numExternalBackgroundAppOneshotScanRequestsThrottled);
            }
            if (this.watchdogTriggerToConnectionSuccessDurationMs != -1) {
                output.writeInt64(107, this.watchdogTriggerToConnectionSuccessDurationMs);
            }
            if (this.watchdogTotalConnectionFailureCountAfterTrigger != 0) {
                output.writeInt64(108, this.watchdogTotalConnectionFailureCountAfterTrigger);
            }
            if (this.numOneshotHasDfsChannelScans != 0) {
                output.writeInt32(109, this.numOneshotHasDfsChannelScans);
            }
            if (this.wifiRttLog != null) {
                output.writeMessage(110, this.wifiRttLog);
            }
            if (this.isMacRandomizationOn) {
                output.writeBool(111, this.isMacRandomizationOn);
            }
            if (this.numRadioModeChangeToMcc != 0) {
                output.writeInt32(112, this.numRadioModeChangeToMcc);
            }
            if (this.numRadioModeChangeToScc != 0) {
                output.writeInt32(113, this.numRadioModeChangeToScc);
            }
            if (this.numRadioModeChangeToSbs != 0) {
                output.writeInt32(114, this.numRadioModeChangeToSbs);
            }
            if (this.numRadioModeChangeToDbs != 0) {
                output.writeInt32(115, this.numRadioModeChangeToDbs);
            }
            if (this.numSoftApUserBandPreferenceUnsatisfied != 0) {
                output.writeInt32(116, this.numSoftApUserBandPreferenceUnsatisfied);
            }
            if (!this.scoreExperimentId.equals("")) {
                output.writeString(117, this.scoreExperimentId);
            }
            if (this.wifiRadioUsage != null) {
                output.writeMessage(118, this.wifiRadioUsage);
            }
            if (this.experimentValues != null) {
                output.writeMessage(119, this.experimentValues);
            }
            if (this.wifiIsUnusableEventList != null && this.wifiIsUnusableEventList.length > 0) {
                for (WifiIsUnusableEvent element34 : this.wifiIsUnusableEventList) {
                    if (element34 != null) {
                        output.writeMessage(120, element34);
                    }
                }
            }
            if (this.linkSpeedCounts != null && this.linkSpeedCounts.length > 0) {
                for (LinkSpeedCount element35 : this.linkSpeedCounts) {
                    if (element35 != null) {
                        output.writeMessage(121, element35);
                    }
                }
            }
            if (this.numSarSensorRegistrationFailures != 0) {
                output.writeInt32(122, this.numSarSensorRegistrationFailures);
            }
            if (this.installedPasspointProfileTypeForR1 != null && this.installedPasspointProfileTypeForR1.length > 0) {
                for (PasspointProfileTypeCount element36 : this.installedPasspointProfileTypeForR1) {
                    if (element36 != null) {
                        output.writeMessage(123, element36);
                    }
                }
            }
            if (!this.hardwareRevision.equals("")) {
                output.writeString(124, this.hardwareRevision);
            }
            if (this.wifiLinkLayerUsageStats != null) {
                output.writeMessage(125, this.wifiLinkLayerUsageStats);
            }
            if (this.wifiUsabilityStatsList != null && this.wifiUsabilityStatsList.length > 0) {
                for (WifiUsabilityStats element37 : this.wifiUsabilityStatsList) {
                    if (element37 != null) {
                        output.writeMessage(126, element37);
                    }
                }
            }
            if (this.wifiUsabilityScoreCount != null && this.wifiUsabilityScoreCount.length > 0) {
                for (WifiUsabilityScoreCount element38 : this.wifiUsabilityScoreCount) {
                    if (element38 != null) {
                        output.writeMessage(127, element38);
                    }
                }
            }
            if (this.mobilityStatePnoStatsList != null && this.mobilityStatePnoStatsList.length > 0) {
                for (DeviceMobilityStatePnoScanStats element39 : this.mobilityStatePnoStatsList) {
                    if (element39 != null) {
                        output.writeMessage(128, element39);
                    }
                }
            }
            if (this.wifiP2PStats != null) {
                output.writeMessage(129, this.wifiP2PStats);
            }
            if (this.wifiDppLog != null) {
                output.writeMessage(130, this.wifiDppLog);
            }
            if (this.numEnhancedOpenNetworks != 0) {
                output.writeInt32(131, this.numEnhancedOpenNetworks);
            }
            if (this.numWpa3PersonalNetworks != 0) {
                output.writeInt32(132, this.numWpa3PersonalNetworks);
            }
            if (this.numWpa3EnterpriseNetworks != 0) {
                output.writeInt32(133, this.numWpa3EnterpriseNetworks);
            }
            if (this.numEnhancedOpenNetworkScanResults != 0) {
                output.writeInt32(134, this.numEnhancedOpenNetworkScanResults);
            }
            if (this.numWpa3PersonalNetworkScanResults != 0) {
                output.writeInt32(135, this.numWpa3PersonalNetworkScanResults);
            }
            if (this.numWpa3EnterpriseNetworkScanResults != 0) {
                output.writeInt32(136, this.numWpa3EnterpriseNetworkScanResults);
            }
            if (this.wifiConfigStoreIo != null) {
                output.writeMessage(137, this.wifiConfigStoreIo);
            }
            if (this.numSavedNetworksWithMacRandomization != 0) {
                output.writeInt32(138, this.numSavedNetworksWithMacRandomization);
            }
            if (this.linkProbeStats != null) {
                output.writeMessage(139, this.linkProbeStats);
            }
            if (this.networkSelectionExperimentDecisionsList != null && this.networkSelectionExperimentDecisionsList.length > 0) {
                for (NetworkSelectionExperimentDecisions element40 : this.networkSelectionExperimentDecisionsList) {
                    if (element40 != null) {
                        output.writeMessage(140, element40);
                    }
                }
            }
            if (this.wifiNetworkRequestApiLog != null) {
                output.writeMessage(141, this.wifiNetworkRequestApiLog);
            }
            if (this.wifiNetworkSuggestionApiLog != null) {
                output.writeMessage(142, this.wifiNetworkSuggestionApiLog);
            }
            if (this.wifiLockStats != null) {
                output.writeMessage(143, this.wifiLockStats);
            }
            if (this.wifiToggleStats != null) {
                output.writeMessage(144, this.wifiToggleStats);
            }
            if (this.numAddOrUpdateNetworkCalls != 0) {
                output.writeInt32(145, this.numAddOrUpdateNetworkCalls);
            }
            if (this.numEnableNetworkCalls != 0) {
                output.writeInt32(146, this.numEnableNetworkCalls);
            }
            if (this.passpointProvisionStats != null) {
                output.writeMessage(147, this.passpointProvisionStats);
            }
            if (this.installedPasspointProfileTypeForR2 != null && this.installedPasspointProfileTypeForR2.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.installedPasspointProfileTypeForR2.length) {
                        break;
                    }
                    PasspointProfileTypeCount element41 = this.installedPasspointProfileTypeForR2[i2];
                    if (element41 != null) {
                        output.writeMessage(148, element41);
                    }
                    i = i2 + 1;
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int i = super.computeSerializedSize();
            int i2 = 0;
            if (this.connectionEvent != null && this.connectionEvent.length > 0) {
                int size = i;
                for (ConnectionEvent element : this.connectionEvent) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(1, element);
                    }
                }
                i = size;
            }
            if (this.numSavedNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(2, this.numSavedNetworks);
            }
            if (this.numOpenNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(3, this.numOpenNetworks);
            }
            if (this.numLegacyPersonalNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(4, this.numLegacyPersonalNetworks);
            }
            if (this.numLegacyEnterpriseNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(5, this.numLegacyEnterpriseNetworks);
            }
            if (this.isLocationEnabled) {
                i += CodedOutputByteBufferNano.computeBoolSize(6, this.isLocationEnabled);
            }
            if (this.isScanningAlwaysEnabled) {
                i += CodedOutputByteBufferNano.computeBoolSize(7, this.isScanningAlwaysEnabled);
            }
            if (this.numWifiToggledViaSettings != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(8, this.numWifiToggledViaSettings);
            }
            if (this.numWifiToggledViaAirplane != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(9, this.numWifiToggledViaAirplane);
            }
            if (this.numNetworksAddedByUser != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(10, this.numNetworksAddedByUser);
            }
            if (this.numNetworksAddedByApps != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(11, this.numNetworksAddedByApps);
            }
            if (this.numEmptyScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(12, this.numEmptyScanResults);
            }
            if (this.numNonEmptyScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(13, this.numNonEmptyScanResults);
            }
            if (this.numOneshotScans != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(14, this.numOneshotScans);
            }
            if (this.numBackgroundScans != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(15, this.numBackgroundScans);
            }
            if (this.scanReturnEntries != null && this.scanReturnEntries.length > 0) {
                int size2 = i;
                for (ScanReturnEntry element2 : this.scanReturnEntries) {
                    if (element2 != null) {
                        size2 += CodedOutputByteBufferNano.computeMessageSize(16, element2);
                    }
                }
                i = size2;
            }
            if (this.wifiSystemStateEntries != null && this.wifiSystemStateEntries.length > 0) {
                int size3 = i;
                for (WifiSystemStateEntry element3 : this.wifiSystemStateEntries) {
                    if (element3 != null) {
                        size3 += CodedOutputByteBufferNano.computeMessageSize(17, element3);
                    }
                }
                i = size3;
            }
            if (this.backgroundScanReturnEntries != null && this.backgroundScanReturnEntries.length > 0) {
                int size4 = i;
                for (ScanReturnEntry element4 : this.backgroundScanReturnEntries) {
                    if (element4 != null) {
                        size4 += CodedOutputByteBufferNano.computeMessageSize(18, element4);
                    }
                }
                i = size4;
            }
            if (this.backgroundScanRequestState != null && this.backgroundScanRequestState.length > 0) {
                int size5 = i;
                for (WifiSystemStateEntry element5 : this.backgroundScanRequestState) {
                    if (element5 != null) {
                        size5 += CodedOutputByteBufferNano.computeMessageSize(19, element5);
                    }
                }
                i = size5;
            }
            if (this.numLastResortWatchdogTriggers != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(20, this.numLastResortWatchdogTriggers);
            }
            if (this.numLastResortWatchdogBadAssociationNetworksTotal != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(21, this.numLastResortWatchdogBadAssociationNetworksTotal);
            }
            if (this.numLastResortWatchdogBadAuthenticationNetworksTotal != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(22, this.numLastResortWatchdogBadAuthenticationNetworksTotal);
            }
            if (this.numLastResortWatchdogBadDhcpNetworksTotal != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(23, this.numLastResortWatchdogBadDhcpNetworksTotal);
            }
            if (this.numLastResortWatchdogBadOtherNetworksTotal != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(24, this.numLastResortWatchdogBadOtherNetworksTotal);
            }
            if (this.numLastResortWatchdogAvailableNetworksTotal != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(25, this.numLastResortWatchdogAvailableNetworksTotal);
            }
            if (this.numLastResortWatchdogTriggersWithBadAssociation != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(26, this.numLastResortWatchdogTriggersWithBadAssociation);
            }
            if (this.numLastResortWatchdogTriggersWithBadAuthentication != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(27, this.numLastResortWatchdogTriggersWithBadAuthentication);
            }
            if (this.numLastResortWatchdogTriggersWithBadDhcp != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(28, this.numLastResortWatchdogTriggersWithBadDhcp);
            }
            if (this.numLastResortWatchdogTriggersWithBadOther != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(29, this.numLastResortWatchdogTriggersWithBadOther);
            }
            if (this.numConnectivityWatchdogPnoGood != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(30, this.numConnectivityWatchdogPnoGood);
            }
            if (this.numConnectivityWatchdogPnoBad != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(31, this.numConnectivityWatchdogPnoBad);
            }
            if (this.numConnectivityWatchdogBackgroundGood != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(32, this.numConnectivityWatchdogBackgroundGood);
            }
            if (this.numConnectivityWatchdogBackgroundBad != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(33, this.numConnectivityWatchdogBackgroundBad);
            }
            if (this.recordDurationSec != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(34, this.recordDurationSec);
            }
            if (this.rssiPollRssiCount != null && this.rssiPollRssiCount.length > 0) {
                int size6 = i;
                for (RssiPollCount element6 : this.rssiPollRssiCount) {
                    if (element6 != null) {
                        size6 += CodedOutputByteBufferNano.computeMessageSize(35, element6);
                    }
                }
                i = size6;
            }
            if (this.numLastResortWatchdogSuccesses != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(36, this.numLastResortWatchdogSuccesses);
            }
            if (this.numHiddenNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(37, this.numHiddenNetworks);
            }
            if (this.numPasspointNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(38, this.numPasspointNetworks);
            }
            if (this.numTotalScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(39, this.numTotalScanResults);
            }
            if (this.numOpenNetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(40, this.numOpenNetworkScanResults);
            }
            if (this.numLegacyPersonalNetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(41, this.numLegacyPersonalNetworkScanResults);
            }
            if (this.numLegacyEnterpriseNetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(42, this.numLegacyEnterpriseNetworkScanResults);
            }
            if (this.numHiddenNetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(43, this.numHiddenNetworkScanResults);
            }
            if (this.numHotspot2R1NetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(44, this.numHotspot2R1NetworkScanResults);
            }
            if (this.numHotspot2R2NetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(45, this.numHotspot2R2NetworkScanResults);
            }
            if (this.numScans != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(46, this.numScans);
            }
            if (this.alertReasonCount != null && this.alertReasonCount.length > 0) {
                int size7 = i;
                for (AlertReasonCount element7 : this.alertReasonCount) {
                    if (element7 != null) {
                        size7 += CodedOutputByteBufferNano.computeMessageSize(47, element7);
                    }
                }
                i = size7;
            }
            if (this.wifiScoreCount != null && this.wifiScoreCount.length > 0) {
                int size8 = i;
                for (WifiScoreCount element8 : this.wifiScoreCount) {
                    if (element8 != null) {
                        size8 += CodedOutputByteBufferNano.computeMessageSize(48, element8);
                    }
                }
                i = size8;
            }
            if (this.softApDuration != null && this.softApDuration.length > 0) {
                int size9 = i;
                for (SoftApDurationBucket element9 : this.softApDuration) {
                    if (element9 != null) {
                        size9 += CodedOutputByteBufferNano.computeMessageSize(49, element9);
                    }
                }
                i = size9;
            }
            if (this.softApReturnCode != null && this.softApReturnCode.length > 0) {
                int size10 = i;
                for (SoftApReturnCodeCount element10 : this.softApReturnCode) {
                    if (element10 != null) {
                        size10 += CodedOutputByteBufferNano.computeMessageSize(50, element10);
                    }
                }
                i = size10;
            }
            if (this.rssiPollDeltaCount != null && this.rssiPollDeltaCount.length > 0) {
                int size11 = i;
                for (RssiPollCount element11 : this.rssiPollDeltaCount) {
                    if (element11 != null) {
                        size11 += CodedOutputByteBufferNano.computeMessageSize(51, element11);
                    }
                }
                i = size11;
            }
            if (this.staEventList != null && this.staEventList.length > 0) {
                int size12 = i;
                for (StaEvent element12 : this.staEventList) {
                    if (element12 != null) {
                        size12 += CodedOutputByteBufferNano.computeMessageSize(52, element12);
                    }
                }
                i = size12;
            }
            if (this.numHalCrashes != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(53, this.numHalCrashes);
            }
            if (this.numWificondCrashes != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(54, this.numWificondCrashes);
            }
            if (this.numSetupClientInterfaceFailureDueToHal != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(55, this.numSetupClientInterfaceFailureDueToHal);
            }
            if (this.numSetupClientInterfaceFailureDueToWificond != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(56, this.numSetupClientInterfaceFailureDueToWificond);
            }
            if (this.wifiAwareLog != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(57, this.wifiAwareLog);
            }
            if (this.numPasspointProviders != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(58, this.numPasspointProviders);
            }
            if (this.numPasspointProviderInstallation != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(59, this.numPasspointProviderInstallation);
            }
            if (this.numPasspointProviderInstallSuccess != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(60, this.numPasspointProviderInstallSuccess);
            }
            if (this.numPasspointProviderUninstallation != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(61, this.numPasspointProviderUninstallation);
            }
            if (this.numPasspointProviderUninstallSuccess != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(62, this.numPasspointProviderUninstallSuccess);
            }
            if (this.numPasspointProvidersSuccessfullyConnected != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(63, this.numPasspointProvidersSuccessfullyConnected);
            }
            if (this.totalSsidsInScanHistogram != null && this.totalSsidsInScanHistogram.length > 0) {
                int size13 = i;
                for (NumConnectableNetworksBucket element13 : this.totalSsidsInScanHistogram) {
                    if (element13 != null) {
                        size13 += CodedOutputByteBufferNano.computeMessageSize(64, element13);
                    }
                }
                i = size13;
            }
            if (this.totalBssidsInScanHistogram != null && this.totalBssidsInScanHistogram.length > 0) {
                int size14 = i;
                for (NumConnectableNetworksBucket element14 : this.totalBssidsInScanHistogram) {
                    if (element14 != null) {
                        size14 += CodedOutputByteBufferNano.computeMessageSize(65, element14);
                    }
                }
                i = size14;
            }
            if (this.availableOpenSsidsInScanHistogram != null && this.availableOpenSsidsInScanHistogram.length > 0) {
                int size15 = i;
                for (NumConnectableNetworksBucket element15 : this.availableOpenSsidsInScanHistogram) {
                    if (element15 != null) {
                        size15 += CodedOutputByteBufferNano.computeMessageSize(66, element15);
                    }
                }
                i = size15;
            }
            if (this.availableOpenBssidsInScanHistogram != null && this.availableOpenBssidsInScanHistogram.length > 0) {
                int size16 = i;
                for (NumConnectableNetworksBucket element16 : this.availableOpenBssidsInScanHistogram) {
                    if (element16 != null) {
                        size16 += CodedOutputByteBufferNano.computeMessageSize(67, element16);
                    }
                }
                i = size16;
            }
            if (this.availableSavedSsidsInScanHistogram != null && this.availableSavedSsidsInScanHistogram.length > 0) {
                int size17 = i;
                for (NumConnectableNetworksBucket element17 : this.availableSavedSsidsInScanHistogram) {
                    if (element17 != null) {
                        size17 += CodedOutputByteBufferNano.computeMessageSize(68, element17);
                    }
                }
                i = size17;
            }
            if (this.availableSavedBssidsInScanHistogram != null && this.availableSavedBssidsInScanHistogram.length > 0) {
                int size18 = i;
                for (NumConnectableNetworksBucket element18 : this.availableSavedBssidsInScanHistogram) {
                    if (element18 != null) {
                        size18 += CodedOutputByteBufferNano.computeMessageSize(69, element18);
                    }
                }
                i = size18;
            }
            if (this.availableOpenOrSavedSsidsInScanHistogram != null && this.availableOpenOrSavedSsidsInScanHistogram.length > 0) {
                int size19 = i;
                for (NumConnectableNetworksBucket element19 : this.availableOpenOrSavedSsidsInScanHistogram) {
                    if (element19 != null) {
                        size19 += CodedOutputByteBufferNano.computeMessageSize(70, element19);
                    }
                }
                i = size19;
            }
            if (this.availableOpenOrSavedBssidsInScanHistogram != null && this.availableOpenOrSavedBssidsInScanHistogram.length > 0) {
                int size20 = i;
                for (NumConnectableNetworksBucket element20 : this.availableOpenOrSavedBssidsInScanHistogram) {
                    if (element20 != null) {
                        size20 += CodedOutputByteBufferNano.computeMessageSize(71, element20);
                    }
                }
                i = size20;
            }
            if (this.availableSavedPasspointProviderProfilesInScanHistogram != null && this.availableSavedPasspointProviderProfilesInScanHistogram.length > 0) {
                int size21 = i;
                for (NumConnectableNetworksBucket element21 : this.availableSavedPasspointProviderProfilesInScanHistogram) {
                    if (element21 != null) {
                        size21 += CodedOutputByteBufferNano.computeMessageSize(72, element21);
                    }
                }
                i = size21;
            }
            if (this.availableSavedPasspointProviderBssidsInScanHistogram != null && this.availableSavedPasspointProviderBssidsInScanHistogram.length > 0) {
                int size22 = i;
                for (NumConnectableNetworksBucket element22 : this.availableSavedPasspointProviderBssidsInScanHistogram) {
                    if (element22 != null) {
                        size22 += CodedOutputByteBufferNano.computeMessageSize(73, element22);
                    }
                }
                i = size22;
            }
            if (this.fullBandAllSingleScanListenerResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(74, this.fullBandAllSingleScanListenerResults);
            }
            if (this.partialAllSingleScanListenerResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(75, this.partialAllSingleScanListenerResults);
            }
            if (this.pnoScanMetrics != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(76, this.pnoScanMetrics);
            }
            if (this.connectToNetworkNotificationCount != null && this.connectToNetworkNotificationCount.length > 0) {
                int size23 = i;
                for (ConnectToNetworkNotificationAndActionCount element23 : this.connectToNetworkNotificationCount) {
                    if (element23 != null) {
                        size23 += CodedOutputByteBufferNano.computeMessageSize(77, element23);
                    }
                }
                i = size23;
            }
            if (this.connectToNetworkNotificationActionCount != null && this.connectToNetworkNotificationActionCount.length > 0) {
                int size24 = i;
                for (ConnectToNetworkNotificationAndActionCount element24 : this.connectToNetworkNotificationActionCount) {
                    if (element24 != null) {
                        size24 += CodedOutputByteBufferNano.computeMessageSize(78, element24);
                    }
                }
                i = size24;
            }
            if (this.openNetworkRecommenderBlacklistSize != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(79, this.openNetworkRecommenderBlacklistSize);
            }
            if (this.isWifiNetworksAvailableNotificationOn) {
                i += CodedOutputByteBufferNano.computeBoolSize(80, this.isWifiNetworksAvailableNotificationOn);
            }
            if (this.numOpenNetworkRecommendationUpdates != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(81, this.numOpenNetworkRecommendationUpdates);
            }
            if (this.numOpenNetworkConnectMessageFailedToSend != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(82, this.numOpenNetworkConnectMessageFailedToSend);
            }
            if (this.observedHotspotR1ApsInScanHistogram != null && this.observedHotspotR1ApsInScanHistogram.length > 0) {
                int size25 = i;
                for (NumConnectableNetworksBucket element25 : this.observedHotspotR1ApsInScanHistogram) {
                    if (element25 != null) {
                        size25 += CodedOutputByteBufferNano.computeMessageSize(83, element25);
                    }
                }
                i = size25;
            }
            if (this.observedHotspotR2ApsInScanHistogram != null && this.observedHotspotR2ApsInScanHistogram.length > 0) {
                int size26 = i;
                for (NumConnectableNetworksBucket element26 : this.observedHotspotR2ApsInScanHistogram) {
                    if (element26 != null) {
                        size26 += CodedOutputByteBufferNano.computeMessageSize(84, element26);
                    }
                }
                i = size26;
            }
            if (this.observedHotspotR1EssInScanHistogram != null && this.observedHotspotR1EssInScanHistogram.length > 0) {
                int size27 = i;
                for (NumConnectableNetworksBucket element27 : this.observedHotspotR1EssInScanHistogram) {
                    if (element27 != null) {
                        size27 += CodedOutputByteBufferNano.computeMessageSize(85, element27);
                    }
                }
                i = size27;
            }
            if (this.observedHotspotR2EssInScanHistogram != null && this.observedHotspotR2EssInScanHistogram.length > 0) {
                int size28 = i;
                for (NumConnectableNetworksBucket element28 : this.observedHotspotR2EssInScanHistogram) {
                    if (element28 != null) {
                        size28 += CodedOutputByteBufferNano.computeMessageSize(86, element28);
                    }
                }
                i = size28;
            }
            if (this.observedHotspotR1ApsPerEssInScanHistogram != null && this.observedHotspotR1ApsPerEssInScanHistogram.length > 0) {
                int size29 = i;
                for (NumConnectableNetworksBucket element29 : this.observedHotspotR1ApsPerEssInScanHistogram) {
                    if (element29 != null) {
                        size29 += CodedOutputByteBufferNano.computeMessageSize(87, element29);
                    }
                }
                i = size29;
            }
            if (this.observedHotspotR2ApsPerEssInScanHistogram != null && this.observedHotspotR2ApsPerEssInScanHistogram.length > 0) {
                int size30 = i;
                for (NumConnectableNetworksBucket element30 : this.observedHotspotR2ApsPerEssInScanHistogram) {
                    if (element30 != null) {
                        size30 += CodedOutputByteBufferNano.computeMessageSize(88, element30);
                    }
                }
                i = size30;
            }
            if (this.softApConnectedClientsEventsTethered != null && this.softApConnectedClientsEventsTethered.length > 0) {
                int size31 = i;
                for (SoftApConnectedClientsEvent element31 : this.softApConnectedClientsEventsTethered) {
                    if (element31 != null) {
                        size31 += CodedOutputByteBufferNano.computeMessageSize(89, element31);
                    }
                }
                i = size31;
            }
            if (this.softApConnectedClientsEventsLocalOnly != null && this.softApConnectedClientsEventsLocalOnly.length > 0) {
                int size32 = i;
                for (SoftApConnectedClientsEvent element32 : this.softApConnectedClientsEventsLocalOnly) {
                    if (element32 != null) {
                        size32 += CodedOutputByteBufferNano.computeMessageSize(90, element32);
                    }
                }
                i = size32;
            }
            if (this.wpsMetrics != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(91, this.wpsMetrics);
            }
            if (this.wifiPowerStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(92, this.wifiPowerStats);
            }
            if (this.numConnectivityOneshotScans != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(93, this.numConnectivityOneshotScans);
            }
            if (this.wifiWakeStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(94, this.wifiWakeStats);
            }
            if (this.observed80211McSupportingApsInScanHistogram != null && this.observed80211McSupportingApsInScanHistogram.length > 0) {
                int size33 = i;
                for (NumConnectableNetworksBucket element33 : this.observed80211McSupportingApsInScanHistogram) {
                    if (element33 != null) {
                        size33 += CodedOutputByteBufferNano.computeMessageSize(95, element33);
                    }
                }
                i = size33;
            }
            if (this.numSupplicantCrashes != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(96, this.numSupplicantCrashes);
            }
            if (this.numHostapdCrashes != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(97, this.numHostapdCrashes);
            }
            if (this.numSetupClientInterfaceFailureDueToSupplicant != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(98, this.numSetupClientInterfaceFailureDueToSupplicant);
            }
            if (this.numSetupSoftApInterfaceFailureDueToHal != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(99, this.numSetupSoftApInterfaceFailureDueToHal);
            }
            if (this.numSetupSoftApInterfaceFailureDueToWificond != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(100, this.numSetupSoftApInterfaceFailureDueToWificond);
            }
            if (this.numSetupSoftApInterfaceFailureDueToHostapd != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(101, this.numSetupSoftApInterfaceFailureDueToHostapd);
            }
            if (this.numClientInterfaceDown != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(102, this.numClientInterfaceDown);
            }
            if (this.numSoftApInterfaceDown != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(103, this.numSoftApInterfaceDown);
            }
            if (this.numExternalAppOneshotScanRequests != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(104, this.numExternalAppOneshotScanRequests);
            }
            if (this.numExternalForegroundAppOneshotScanRequestsThrottled != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(105, this.numExternalForegroundAppOneshotScanRequestsThrottled);
            }
            if (this.numExternalBackgroundAppOneshotScanRequestsThrottled != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(106, this.numExternalBackgroundAppOneshotScanRequestsThrottled);
            }
            if (this.watchdogTriggerToConnectionSuccessDurationMs != -1) {
                i += CodedOutputByteBufferNano.computeInt64Size(107, this.watchdogTriggerToConnectionSuccessDurationMs);
            }
            if (this.watchdogTotalConnectionFailureCountAfterTrigger != 0) {
                i += CodedOutputByteBufferNano.computeInt64Size(108, this.watchdogTotalConnectionFailureCountAfterTrigger);
            }
            if (this.numOneshotHasDfsChannelScans != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(109, this.numOneshotHasDfsChannelScans);
            }
            if (this.wifiRttLog != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(110, this.wifiRttLog);
            }
            if (this.isMacRandomizationOn) {
                i += CodedOutputByteBufferNano.computeBoolSize(111, this.isMacRandomizationOn);
            }
            if (this.numRadioModeChangeToMcc != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(112, this.numRadioModeChangeToMcc);
            }
            if (this.numRadioModeChangeToScc != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(113, this.numRadioModeChangeToScc);
            }
            if (this.numRadioModeChangeToSbs != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(114, this.numRadioModeChangeToSbs);
            }
            if (this.numRadioModeChangeToDbs != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(115, this.numRadioModeChangeToDbs);
            }
            if (this.numSoftApUserBandPreferenceUnsatisfied != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(116, this.numSoftApUserBandPreferenceUnsatisfied);
            }
            if (!this.scoreExperimentId.equals("")) {
                i += CodedOutputByteBufferNano.computeStringSize(117, this.scoreExperimentId);
            }
            if (this.wifiRadioUsage != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(118, this.wifiRadioUsage);
            }
            if (this.experimentValues != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(119, this.experimentValues);
            }
            if (this.wifiIsUnusableEventList != null && this.wifiIsUnusableEventList.length > 0) {
                int size34 = i;
                for (WifiIsUnusableEvent element34 : this.wifiIsUnusableEventList) {
                    if (element34 != null) {
                        size34 += CodedOutputByteBufferNano.computeMessageSize(120, element34);
                    }
                }
                i = size34;
            }
            if (this.linkSpeedCounts != null && this.linkSpeedCounts.length > 0) {
                int size35 = i;
                for (LinkSpeedCount element35 : this.linkSpeedCounts) {
                    if (element35 != null) {
                        size35 += CodedOutputByteBufferNano.computeMessageSize(121, element35);
                    }
                }
                i = size35;
            }
            if (this.numSarSensorRegistrationFailures != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(122, this.numSarSensorRegistrationFailures);
            }
            if (this.installedPasspointProfileTypeForR1 != null && this.installedPasspointProfileTypeForR1.length > 0) {
                int size36 = i;
                for (PasspointProfileTypeCount element36 : this.installedPasspointProfileTypeForR1) {
                    if (element36 != null) {
                        size36 += CodedOutputByteBufferNano.computeMessageSize(123, element36);
                    }
                }
                i = size36;
            }
            if (!this.hardwareRevision.equals("")) {
                i += CodedOutputByteBufferNano.computeStringSize(124, this.hardwareRevision);
            }
            if (this.wifiLinkLayerUsageStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(125, this.wifiLinkLayerUsageStats);
            }
            if (this.wifiUsabilityStatsList != null && this.wifiUsabilityStatsList.length > 0) {
                int size37 = i;
                for (WifiUsabilityStats element37 : this.wifiUsabilityStatsList) {
                    if (element37 != null) {
                        size37 += CodedOutputByteBufferNano.computeMessageSize(126, element37);
                    }
                }
                i = size37;
            }
            if (this.wifiUsabilityScoreCount != null && this.wifiUsabilityScoreCount.length > 0) {
                int size38 = i;
                for (WifiUsabilityScoreCount element38 : this.wifiUsabilityScoreCount) {
                    if (element38 != null) {
                        size38 += CodedOutputByteBufferNano.computeMessageSize(127, element38);
                    }
                }
                i = size38;
            }
            if (this.mobilityStatePnoStatsList != null && this.mobilityStatePnoStatsList.length > 0) {
                int size39 = i;
                for (DeviceMobilityStatePnoScanStats element39 : this.mobilityStatePnoStatsList) {
                    if (element39 != null) {
                        size39 += CodedOutputByteBufferNano.computeMessageSize(128, element39);
                    }
                }
                i = size39;
            }
            if (this.wifiP2PStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(129, this.wifiP2PStats);
            }
            if (this.wifiDppLog != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(130, this.wifiDppLog);
            }
            if (this.numEnhancedOpenNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(131, this.numEnhancedOpenNetworks);
            }
            if (this.numWpa3PersonalNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(132, this.numWpa3PersonalNetworks);
            }
            if (this.numWpa3EnterpriseNetworks != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(133, this.numWpa3EnterpriseNetworks);
            }
            if (this.numEnhancedOpenNetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(134, this.numEnhancedOpenNetworkScanResults);
            }
            if (this.numWpa3PersonalNetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(135, this.numWpa3PersonalNetworkScanResults);
            }
            if (this.numWpa3EnterpriseNetworkScanResults != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(136, this.numWpa3EnterpriseNetworkScanResults);
            }
            if (this.wifiConfigStoreIo != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(137, this.wifiConfigStoreIo);
            }
            if (this.numSavedNetworksWithMacRandomization != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(138, this.numSavedNetworksWithMacRandomization);
            }
            if (this.linkProbeStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(139, this.linkProbeStats);
            }
            if (this.networkSelectionExperimentDecisionsList != null && this.networkSelectionExperimentDecisionsList.length > 0) {
                int size40 = i;
                for (NetworkSelectionExperimentDecisions element40 : this.networkSelectionExperimentDecisionsList) {
                    if (element40 != null) {
                        size40 += CodedOutputByteBufferNano.computeMessageSize(140, element40);
                    }
                }
                i = size40;
            }
            if (this.wifiNetworkRequestApiLog != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(141, this.wifiNetworkRequestApiLog);
            }
            if (this.wifiNetworkSuggestionApiLog != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(142, this.wifiNetworkSuggestionApiLog);
            }
            if (this.wifiLockStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(143, this.wifiLockStats);
            }
            if (this.wifiToggleStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(144, this.wifiToggleStats);
            }
            if (this.numAddOrUpdateNetworkCalls != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(145, this.numAddOrUpdateNetworkCalls);
            }
            if (this.numEnableNetworkCalls != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(146, this.numEnableNetworkCalls);
            }
            if (this.passpointProvisionStats != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(147, this.passpointProvisionStats);
            }
            if (this.installedPasspointProfileTypeForR2 != null && this.installedPasspointProfileTypeForR2.length > 0) {
                while (true) {
                    int i3 = i2;
                    if (i3 >= this.installedPasspointProfileTypeForR2.length) {
                        break;
                    }
                    PasspointProfileTypeCount element41 = this.installedPasspointProfileTypeForR2[i3];
                    if (element41 != null) {
                        i += CodedOutputByteBufferNano.computeMessageSize(148, element41);
                    }
                    i2 = i3 + 1;
                }
            }
            return i;
        }

        public WifiLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                        int i = this.connectionEvent == null ? 0 : this.connectionEvent.length;
                        ConnectionEvent[] newArray = new ConnectionEvent[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.connectionEvent, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new ConnectionEvent();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new ConnectionEvent();
                        input.readMessage(newArray[i]);
                        this.connectionEvent = newArray;
                        break;
                    case 16:
                        this.numSavedNetworks = input.readInt32();
                        break;
                    case 24:
                        this.numOpenNetworks = input.readInt32();
                        break;
                    case 32:
                        this.numLegacyPersonalNetworks = input.readInt32();
                        break;
                    case 40:
                        this.numLegacyEnterpriseNetworks = input.readInt32();
                        break;
                    case 48:
                        this.isLocationEnabled = input.readBool();
                        break;
                    case 56:
                        this.isScanningAlwaysEnabled = input.readBool();
                        break;
                    case 64:
                        this.numWifiToggledViaSettings = input.readInt32();
                        break;
                    case 72:
                        this.numWifiToggledViaAirplane = input.readInt32();
                        break;
                    case 80:
                        this.numNetworksAddedByUser = input.readInt32();
                        break;
                    case 88:
                        this.numNetworksAddedByApps = input.readInt32();
                        break;
                    case 96:
                        this.numEmptyScanResults = input.readInt32();
                        break;
                    case 104:
                        this.numNonEmptyScanResults = input.readInt32();
                        break;
                    case 112:
                        this.numOneshotScans = input.readInt32();
                        break;
                    case 120:
                        this.numBackgroundScans = input.readInt32();
                        break;
                    case 130:
                        int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 130);
                        int i2 = this.scanReturnEntries == null ? 0 : this.scanReturnEntries.length;
                        ScanReturnEntry[] newArray2 = new ScanReturnEntry[(i2 + arrayLength2)];
                        if (i2 != 0) {
                            System.arraycopy(this.scanReturnEntries, 0, newArray2, 0, i2);
                        }
                        while (i2 < newArray2.length - 1) {
                            newArray2[i2] = new ScanReturnEntry();
                            input.readMessage(newArray2[i2]);
                            input.readTag();
                            i2++;
                        }
                        newArray2[i2] = new ScanReturnEntry();
                        input.readMessage(newArray2[i2]);
                        this.scanReturnEntries = newArray2;
                        break;
                    case 138:
                        int arrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 138);
                        int i3 = this.wifiSystemStateEntries == null ? 0 : this.wifiSystemStateEntries.length;
                        WifiSystemStateEntry[] newArray3 = new WifiSystemStateEntry[(i3 + arrayLength3)];
                        if (i3 != 0) {
                            System.arraycopy(this.wifiSystemStateEntries, 0, newArray3, 0, i3);
                        }
                        while (i3 < newArray3.length - 1) {
                            newArray3[i3] = new WifiSystemStateEntry();
                            input.readMessage(newArray3[i3]);
                            input.readTag();
                            i3++;
                        }
                        newArray3[i3] = new WifiSystemStateEntry();
                        input.readMessage(newArray3[i3]);
                        this.wifiSystemStateEntries = newArray3;
                        break;
                    case 146:
                        int arrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 146);
                        int i4 = this.backgroundScanReturnEntries == null ? 0 : this.backgroundScanReturnEntries.length;
                        ScanReturnEntry[] newArray4 = new ScanReturnEntry[(i4 + arrayLength4)];
                        if (i4 != 0) {
                            System.arraycopy(this.backgroundScanReturnEntries, 0, newArray4, 0, i4);
                        }
                        while (i4 < newArray4.length - 1) {
                            newArray4[i4] = new ScanReturnEntry();
                            input.readMessage(newArray4[i4]);
                            input.readTag();
                            i4++;
                        }
                        newArray4[i4] = new ScanReturnEntry();
                        input.readMessage(newArray4[i4]);
                        this.backgroundScanReturnEntries = newArray4;
                        break;
                    case 154:
                        int arrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(input, 154);
                        int i5 = this.backgroundScanRequestState == null ? 0 : this.backgroundScanRequestState.length;
                        WifiSystemStateEntry[] newArray5 = new WifiSystemStateEntry[(i5 + arrayLength5)];
                        if (i5 != 0) {
                            System.arraycopy(this.backgroundScanRequestState, 0, newArray5, 0, i5);
                        }
                        while (i5 < newArray5.length - 1) {
                            newArray5[i5] = new WifiSystemStateEntry();
                            input.readMessage(newArray5[i5]);
                            input.readTag();
                            i5++;
                        }
                        newArray5[i5] = new WifiSystemStateEntry();
                        input.readMessage(newArray5[i5]);
                        this.backgroundScanRequestState = newArray5;
                        break;
                    case 160:
                        this.numLastResortWatchdogTriggers = input.readInt32();
                        break;
                    case 168:
                        this.numLastResortWatchdogBadAssociationNetworksTotal = input.readInt32();
                        break;
                    case 176:
                        this.numLastResortWatchdogBadAuthenticationNetworksTotal = input.readInt32();
                        break;
                    case 184:
                        this.numLastResortWatchdogBadDhcpNetworksTotal = input.readInt32();
                        break;
                    case 192:
                        this.numLastResortWatchdogBadOtherNetworksTotal = input.readInt32();
                        break;
                    case 200:
                        this.numLastResortWatchdogAvailableNetworksTotal = input.readInt32();
                        break;
                    case 208:
                        this.numLastResortWatchdogTriggersWithBadAssociation = input.readInt32();
                        break;
                    case 216:
                        this.numLastResortWatchdogTriggersWithBadAuthentication = input.readInt32();
                        break;
                    case 224:
                        this.numLastResortWatchdogTriggersWithBadDhcp = input.readInt32();
                        break;
                    case 232:
                        this.numLastResortWatchdogTriggersWithBadOther = input.readInt32();
                        break;
                    case 240:
                        this.numConnectivityWatchdogPnoGood = input.readInt32();
                        break;
                    case 248:
                        this.numConnectivityWatchdogPnoBad = input.readInt32();
                        break;
                    case 256:
                        this.numConnectivityWatchdogBackgroundGood = input.readInt32();
                        break;
                    case 264:
                        this.numConnectivityWatchdogBackgroundBad = input.readInt32();
                        break;
                    case 272:
                        this.recordDurationSec = input.readInt32();
                        break;
                    case 282:
                        int arrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(input, 282);
                        int i6 = this.rssiPollRssiCount == null ? 0 : this.rssiPollRssiCount.length;
                        RssiPollCount[] newArray6 = new RssiPollCount[(i6 + arrayLength6)];
                        if (i6 != 0) {
                            System.arraycopy(this.rssiPollRssiCount, 0, newArray6, 0, i6);
                        }
                        while (i6 < newArray6.length - 1) {
                            newArray6[i6] = new RssiPollCount();
                            input.readMessage(newArray6[i6]);
                            input.readTag();
                            i6++;
                        }
                        newArray6[i6] = new RssiPollCount();
                        input.readMessage(newArray6[i6]);
                        this.rssiPollRssiCount = newArray6;
                        break;
                    case 288:
                        this.numLastResortWatchdogSuccesses = input.readInt32();
                        break;
                    case 296:
                        this.numHiddenNetworks = input.readInt32();
                        break;
                    case 304:
                        this.numPasspointNetworks = input.readInt32();
                        break;
                    case 312:
                        this.numTotalScanResults = input.readInt32();
                        break;
                    case 320:
                        this.numOpenNetworkScanResults = input.readInt32();
                        break;
                    case 328:
                        this.numLegacyPersonalNetworkScanResults = input.readInt32();
                        break;
                    case 336:
                        this.numLegacyEnterpriseNetworkScanResults = input.readInt32();
                        break;
                    case 344:
                        this.numHiddenNetworkScanResults = input.readInt32();
                        break;
                    case 352:
                        this.numHotspot2R1NetworkScanResults = input.readInt32();
                        break;
                    case 360:
                        this.numHotspot2R2NetworkScanResults = input.readInt32();
                        break;
                    case 368:
                        this.numScans = input.readInt32();
                        break;
                    case 378:
                        int arrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(input, 378);
                        int i7 = this.alertReasonCount == null ? 0 : this.alertReasonCount.length;
                        AlertReasonCount[] newArray7 = new AlertReasonCount[(i7 + arrayLength7)];
                        if (i7 != 0) {
                            System.arraycopy(this.alertReasonCount, 0, newArray7, 0, i7);
                        }
                        while (i7 < newArray7.length - 1) {
                            newArray7[i7] = new AlertReasonCount();
                            input.readMessage(newArray7[i7]);
                            input.readTag();
                            i7++;
                        }
                        newArray7[i7] = new AlertReasonCount();
                        input.readMessage(newArray7[i7]);
                        this.alertReasonCount = newArray7;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_SETTINGS_SUGGESTION:
                        int arrayLength8 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_SETTINGS_SUGGESTION);
                        int i8 = this.wifiScoreCount == null ? 0 : this.wifiScoreCount.length;
                        WifiScoreCount[] newArray8 = new WifiScoreCount[(i8 + arrayLength8)];
                        if (i8 != 0) {
                            System.arraycopy(this.wifiScoreCount, 0, newArray8, 0, i8);
                        }
                        while (i8 < newArray8.length - 1) {
                            newArray8[i8] = new WifiScoreCount();
                            input.readMessage(newArray8[i8]);
                            input.readTag();
                            i8++;
                        }
                        newArray8[i8] = new WifiScoreCount();
                        input.readMessage(newArray8[i8]);
                        this.wifiScoreCount = newArray8;
                        break;
                    case 394:
                        int arrayLength9 = WireFormatNano.getRepeatedFieldArrayLength(input, 394);
                        int i9 = this.softApDuration == null ? 0 : this.softApDuration.length;
                        SoftApDurationBucket[] newArray9 = new SoftApDurationBucket[(i9 + arrayLength9)];
                        if (i9 != 0) {
                            System.arraycopy(this.softApDuration, 0, newArray9, 0, i9);
                        }
                        while (i9 < newArray9.length - 1) {
                            newArray9[i9] = new SoftApDurationBucket();
                            input.readMessage(newArray9[i9]);
                            input.readTag();
                            i9++;
                        }
                        newArray9[i9] = new SoftApDurationBucket();
                        input.readMessage(newArray9[i9]);
                        this.softApDuration = newArray9;
                        break;
                    case 402:
                        int arrayLength10 = WireFormatNano.getRepeatedFieldArrayLength(input, 402);
                        int i10 = this.softApReturnCode == null ? 0 : this.softApReturnCode.length;
                        SoftApReturnCodeCount[] newArray10 = new SoftApReturnCodeCount[(i10 + arrayLength10)];
                        if (i10 != 0) {
                            System.arraycopy(this.softApReturnCode, 0, newArray10, 0, i10);
                        }
                        while (i10 < newArray10.length - 1) {
                            newArray10[i10] = new SoftApReturnCodeCount();
                            input.readMessage(newArray10[i10]);
                            input.readTag();
                            i10++;
                        }
                        newArray10[i10] = new SoftApReturnCodeCount();
                        input.readMessage(newArray10[i10]);
                        this.softApReturnCode = newArray10;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_NOTIFICATION_GROUP_GESTURE_EXPANDER:
                        int arrayLength11 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_NOTIFICATION_GROUP_GESTURE_EXPANDER);
                        int i11 = this.rssiPollDeltaCount == null ? 0 : this.rssiPollDeltaCount.length;
                        RssiPollCount[] newArray11 = new RssiPollCount[(i11 + arrayLength11)];
                        if (i11 != 0) {
                            System.arraycopy(this.rssiPollDeltaCount, 0, newArray11, 0, i11);
                        }
                        while (i11 < newArray11.length - 1) {
                            newArray11[i11] = new RssiPollCount();
                            input.readMessage(newArray11[i11]);
                            input.readTag();
                            i11++;
                        }
                        newArray11[i11] = new RssiPollCount();
                        input.readMessage(newArray11[i11]);
                        this.rssiPollDeltaCount = newArray11;
                        break;
                    case 418:
                        int arrayLength12 = WireFormatNano.getRepeatedFieldArrayLength(input, 418);
                        int i12 = this.staEventList == null ? 0 : this.staEventList.length;
                        StaEvent[] newArray12 = new StaEvent[(i12 + arrayLength12)];
                        if (i12 != 0) {
                            System.arraycopy(this.staEventList, 0, newArray12, 0, i12);
                        }
                        while (i12 < newArray12.length - 1) {
                            newArray12[i12] = new StaEvent();
                            input.readMessage(newArray12[i12]);
                            input.readTag();
                            i12++;
                        }
                        newArray12[i12] = new StaEvent();
                        input.readMessage(newArray12[i12]);
                        this.staEventList = newArray12;
                        break;
                    case 424:
                        this.numHalCrashes = input.readInt32();
                        break;
                    case DevicePolicyManager.PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER:
                        this.numWificondCrashes = input.readInt32();
                        break;
                    case DisplayMetrics.DENSITY_440:
                        this.numSetupClientInterfaceFailureDueToHal = input.readInt32();
                        break;
                    case 448:
                        this.numSetupClientInterfaceFailureDueToWificond = input.readInt32();
                        break;
                    case 458:
                        if (this.wifiAwareLog == null) {
                            this.wifiAwareLog = new WifiAwareLog();
                        }
                        input.readMessage(this.wifiAwareLog);
                        break;
                    case MetricsProto.MetricsEvent.ACTION_DELETION_APPS_COLLAPSED:
                        this.numPasspointProviders = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_DELETION_HELPER_DOWNLOADS_DELETION_FAIL:
                        this.numPasspointProviderInstallation = input.readInt32();
                        break;
                    case 480:
                        this.numPasspointProviderInstallSuccess = input.readInt32();
                        break;
                    case 488:
                        this.numPasspointProviderUninstallation = input.readInt32();
                        break;
                    case 496:
                        this.numPasspointProviderUninstallSuccess = input.readInt32();
                        break;
                    case 504:
                        this.numPasspointProvidersSuccessfullyConnected = input.readInt32();
                        break;
                    case 514:
                        int arrayLength13 = WireFormatNano.getRepeatedFieldArrayLength(input, 514);
                        int i13 = this.totalSsidsInScanHistogram == null ? 0 : this.totalSsidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray13 = new NumConnectableNetworksBucket[(i13 + arrayLength13)];
                        if (i13 != 0) {
                            System.arraycopy(this.totalSsidsInScanHistogram, 0, newArray13, 0, i13);
                        }
                        while (i13 < newArray13.length - 1) {
                            newArray13[i13] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray13[i13]);
                            input.readTag();
                            i13++;
                        }
                        newArray13[i13] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray13[i13]);
                        this.totalSsidsInScanHistogram = newArray13;
                        break;
                    case 522:
                        int arrayLength14 = WireFormatNano.getRepeatedFieldArrayLength(input, 522);
                        int i14 = this.totalBssidsInScanHistogram == null ? 0 : this.totalBssidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray14 = new NumConnectableNetworksBucket[(i14 + arrayLength14)];
                        if (i14 != 0) {
                            System.arraycopy(this.totalBssidsInScanHistogram, 0, newArray14, 0, i14);
                        }
                        while (i14 < newArray14.length - 1) {
                            newArray14[i14] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray14[i14]);
                            input.readTag();
                            i14++;
                        }
                        newArray14[i14] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray14[i14]);
                        this.totalBssidsInScanHistogram = newArray14;
                        break;
                    case 530:
                        int arrayLength15 = WireFormatNano.getRepeatedFieldArrayLength(input, 530);
                        int i15 = this.availableOpenSsidsInScanHistogram == null ? 0 : this.availableOpenSsidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray15 = new NumConnectableNetworksBucket[(i15 + arrayLength15)];
                        if (i15 != 0) {
                            System.arraycopy(this.availableOpenSsidsInScanHistogram, 0, newArray15, 0, i15);
                        }
                        while (i15 < newArray15.length - 1) {
                            newArray15[i15] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray15[i15]);
                            input.readTag();
                            i15++;
                        }
                        newArray15[i15] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray15[i15]);
                        this.availableOpenSsidsInScanHistogram = newArray15;
                        break;
                    case 538:
                        int arrayLength16 = WireFormatNano.getRepeatedFieldArrayLength(input, 538);
                        int i16 = this.availableOpenBssidsInScanHistogram == null ? 0 : this.availableOpenBssidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray16 = new NumConnectableNetworksBucket[(i16 + arrayLength16)];
                        if (i16 != 0) {
                            System.arraycopy(this.availableOpenBssidsInScanHistogram, 0, newArray16, 0, i16);
                        }
                        while (i16 < newArray16.length - 1) {
                            newArray16[i16] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray16[i16]);
                            input.readTag();
                            i16++;
                        }
                        newArray16[i16] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray16[i16]);
                        this.availableOpenBssidsInScanHistogram = newArray16;
                        break;
                    case 546:
                        int arrayLength17 = WireFormatNano.getRepeatedFieldArrayLength(input, 546);
                        int i17 = this.availableSavedSsidsInScanHistogram == null ? 0 : this.availableSavedSsidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray17 = new NumConnectableNetworksBucket[(i17 + arrayLength17)];
                        if (i17 != 0) {
                            System.arraycopy(this.availableSavedSsidsInScanHistogram, 0, newArray17, 0, i17);
                        }
                        while (i17 < newArray17.length - 1) {
                            newArray17[i17] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray17[i17]);
                            input.readTag();
                            i17++;
                        }
                        newArray17[i17] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray17[i17]);
                        this.availableSavedSsidsInScanHistogram = newArray17;
                        break;
                    case 554:
                        int arrayLength18 = WireFormatNano.getRepeatedFieldArrayLength(input, 554);
                        int i18 = this.availableSavedBssidsInScanHistogram == null ? 0 : this.availableSavedBssidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray18 = new NumConnectableNetworksBucket[(i18 + arrayLength18)];
                        if (i18 != 0) {
                            System.arraycopy(this.availableSavedBssidsInScanHistogram, 0, newArray18, 0, i18);
                        }
                        while (i18 < newArray18.length - 1) {
                            newArray18[i18] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray18[i18]);
                            input.readTag();
                            i18++;
                        }
                        newArray18[i18] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray18[i18]);
                        this.availableSavedBssidsInScanHistogram = newArray18;
                        break;
                    case 562:
                        int arrayLength19 = WireFormatNano.getRepeatedFieldArrayLength(input, 562);
                        int i19 = this.availableOpenOrSavedSsidsInScanHistogram == null ? 0 : this.availableOpenOrSavedSsidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray19 = new NumConnectableNetworksBucket[(i19 + arrayLength19)];
                        if (i19 != 0) {
                            System.arraycopy(this.availableOpenOrSavedSsidsInScanHistogram, 0, newArray19, 0, i19);
                        }
                        while (i19 < newArray19.length - 1) {
                            newArray19[i19] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray19[i19]);
                            input.readTag();
                            i19++;
                        }
                        newArray19[i19] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray19[i19]);
                        this.availableOpenOrSavedSsidsInScanHistogram = newArray19;
                        break;
                    case 570:
                        int arrayLength20 = WireFormatNano.getRepeatedFieldArrayLength(input, 570);
                        int i20 = this.availableOpenOrSavedBssidsInScanHistogram == null ? 0 : this.availableOpenOrSavedBssidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray20 = new NumConnectableNetworksBucket[(i20 + arrayLength20)];
                        if (i20 != 0) {
                            System.arraycopy(this.availableOpenOrSavedBssidsInScanHistogram, 0, newArray20, 0, i20);
                        }
                        while (i20 < newArray20.length - 1) {
                            newArray20[i20] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray20[i20]);
                            input.readTag();
                            i20++;
                        }
                        newArray20[i20] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray20[i20]);
                        this.availableOpenOrSavedBssidsInScanHistogram = newArray20;
                        break;
                    case 578:
                        int arrayLength21 = WireFormatNano.getRepeatedFieldArrayLength(input, 578);
                        int i21 = this.availableSavedPasspointProviderProfilesInScanHistogram == null ? 0 : this.availableSavedPasspointProviderProfilesInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray21 = new NumConnectableNetworksBucket[(i21 + arrayLength21)];
                        if (i21 != 0) {
                            System.arraycopy(this.availableSavedPasspointProviderProfilesInScanHistogram, 0, newArray21, 0, i21);
                        }
                        while (i21 < newArray21.length - 1) {
                            newArray21[i21] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray21[i21]);
                            input.readTag();
                            i21++;
                        }
                        newArray21[i21] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray21[i21]);
                        this.availableSavedPasspointProviderProfilesInScanHistogram = newArray21;
                        break;
                    case 586:
                        int arrayLength22 = WireFormatNano.getRepeatedFieldArrayLength(input, 586);
                        int i22 = this.availableSavedPasspointProviderBssidsInScanHistogram == null ? 0 : this.availableSavedPasspointProviderBssidsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray22 = new NumConnectableNetworksBucket[(i22 + arrayLength22)];
                        if (i22 != 0) {
                            System.arraycopy(this.availableSavedPasspointProviderBssidsInScanHistogram, 0, newArray22, 0, i22);
                        }
                        while (i22 < newArray22.length - 1) {
                            newArray22[i22] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray22[i22]);
                            input.readTag();
                            i22++;
                        }
                        newArray22[i22] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray22[i22]);
                        this.availableSavedPasspointProviderBssidsInScanHistogram = newArray22;
                        break;
                    case 592:
                        this.fullBandAllSingleScanListenerResults = input.readInt32();
                        break;
                    case 600:
                        this.partialAllSingleScanListenerResults = input.readInt32();
                        break;
                    case 610:
                        if (this.pnoScanMetrics == null) {
                            this.pnoScanMetrics = new PnoScanMetrics();
                        }
                        input.readMessage(this.pnoScanMetrics);
                        break;
                    case MetricsProto.MetricsEvent.PROVISIONING_ENTRY_POINT_TRUSTED_SOURCE:
                        int arrayLength23 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.PROVISIONING_ENTRY_POINT_TRUSTED_SOURCE);
                        int i23 = this.connectToNetworkNotificationCount == null ? 0 : this.connectToNetworkNotificationCount.length;
                        ConnectToNetworkNotificationAndActionCount[] newArray23 = new ConnectToNetworkNotificationAndActionCount[(i23 + arrayLength23)];
                        if (i23 != 0) {
                            System.arraycopy(this.connectToNetworkNotificationCount, 0, newArray23, 0, i23);
                        }
                        while (i23 < newArray23.length - 1) {
                            newArray23[i23] = new ConnectToNetworkNotificationAndActionCount();
                            input.readMessage(newArray23[i23]);
                            input.readTag();
                            i23++;
                        }
                        newArray23[i23] = new ConnectToNetworkNotificationAndActionCount();
                        input.readMessage(newArray23[i23]);
                        this.connectToNetworkNotificationCount = newArray23;
                        break;
                    case MetricsProto.MetricsEvent.PROVISIONING_COPY_ACCOUNT_STATUS:
                        int arrayLength24 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.PROVISIONING_COPY_ACCOUNT_STATUS);
                        int i24 = this.connectToNetworkNotificationActionCount == null ? 0 : this.connectToNetworkNotificationActionCount.length;
                        ConnectToNetworkNotificationAndActionCount[] newArray24 = new ConnectToNetworkNotificationAndActionCount[(i24 + arrayLength24)];
                        if (i24 != 0) {
                            System.arraycopy(this.connectToNetworkNotificationActionCount, 0, newArray24, 0, i24);
                        }
                        while (i24 < newArray24.length - 1) {
                            newArray24[i24] = new ConnectToNetworkNotificationAndActionCount();
                            input.readMessage(newArray24[i24]);
                            input.readTag();
                            i24++;
                        }
                        newArray24[i24] = new ConnectToNetworkNotificationAndActionCount();
                        input.readMessage(newArray24[i24]);
                        this.connectToNetworkNotificationActionCount = newArray24;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_DENIED_UNKNOWN:
                        this.openNetworkRecommenderBlacklistSize = input.readInt32();
                        break;
                    case 640:
                        this.isWifiNetworksAvailableNotificationOn = input.readBool();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_DENIED_READ_CONTACTS:
                        this.numOpenNetworkRecommendationUpdates = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_DENIED_GET_ACCOUNTS:
                        this.numOpenNetworkConnectMessageFailedToSend = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_RECORD_AUDIO:
                        int arrayLength25 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_RECORD_AUDIO);
                        int i25 = this.observedHotspotR1ApsInScanHistogram == null ? 0 : this.observedHotspotR1ApsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray25 = new NumConnectableNetworksBucket[(i25 + arrayLength25)];
                        if (i25 != 0) {
                            System.arraycopy(this.observedHotspotR1ApsInScanHistogram, 0, newArray25, 0, i25);
                        }
                        while (i25 < newArray25.length - 1) {
                            newArray25[i25] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray25[i25]);
                            input.readTag();
                            i25++;
                        }
                        newArray25[i25] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray25[i25]);
                        this.observedHotspotR1ApsInScanHistogram = newArray25;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_CALL_PHONE:
                        int arrayLength26 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_CALL_PHONE);
                        int i26 = this.observedHotspotR2ApsInScanHistogram == null ? 0 : this.observedHotspotR2ApsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray26 = new NumConnectableNetworksBucket[(i26 + arrayLength26)];
                        if (i26 != 0) {
                            System.arraycopy(this.observedHotspotR2ApsInScanHistogram, 0, newArray26, 0, i26);
                        }
                        while (i26 < newArray26.length - 1) {
                            newArray26[i26] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray26[i26]);
                            input.readTag();
                            i26++;
                        }
                        newArray26[i26] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray26[i26]);
                        this.observedHotspotR2ApsInScanHistogram = newArray26;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_WRITE_CALL_LOG:
                        int arrayLength27 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_WRITE_CALL_LOG);
                        int i27 = this.observedHotspotR1EssInScanHistogram == null ? 0 : this.observedHotspotR1EssInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray27 = new NumConnectableNetworksBucket[(i27 + arrayLength27)];
                        if (i27 != 0) {
                            System.arraycopy(this.observedHotspotR1EssInScanHistogram, 0, newArray27, 0, i27);
                        }
                        while (i27 < newArray27.length - 1) {
                            newArray27[i27] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray27[i27]);
                            input.readTag();
                            i27++;
                        }
                        newArray27[i27] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray27[i27]);
                        this.observedHotspotR1EssInScanHistogram = newArray27;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_USE_SIP:
                        int arrayLength28 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_USE_SIP);
                        int i28 = this.observedHotspotR2EssInScanHistogram == null ? 0 : this.observedHotspotR2EssInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray28 = new NumConnectableNetworksBucket[(i28 + arrayLength28)];
                        if (i28 != 0) {
                            System.arraycopy(this.observedHotspotR2EssInScanHistogram, 0, newArray28, 0, i28);
                        }
                        while (i28 < newArray28.length - 1) {
                            newArray28[i28] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray28[i28]);
                            input.readTag();
                            i28++;
                        }
                        newArray28[i28] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray28[i28]);
                        this.observedHotspotR2EssInScanHistogram = newArray28;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_READ_CELL_BROADCASTS:
                        int arrayLength29 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_READ_CELL_BROADCASTS);
                        int i29 = this.observedHotspotR1ApsPerEssInScanHistogram == null ? 0 : this.observedHotspotR1ApsPerEssInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray29 = new NumConnectableNetworksBucket[(i29 + arrayLength29)];
                        if (i29 != 0) {
                            System.arraycopy(this.observedHotspotR1ApsPerEssInScanHistogram, 0, newArray29, 0, i29);
                        }
                        while (i29 < newArray29.length - 1) {
                            newArray29[i29] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray29[i29]);
                            input.readTag();
                            i29++;
                        }
                        newArray29[i29] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray29[i29]);
                        this.observedHotspotR1ApsPerEssInScanHistogram = newArray29;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_SEND_SMS:
                        int arrayLength30 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_SEND_SMS);
                        int i30 = this.observedHotspotR2ApsPerEssInScanHistogram == null ? 0 : this.observedHotspotR2ApsPerEssInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray30 = new NumConnectableNetworksBucket[(i30 + arrayLength30)];
                        if (i30 != 0) {
                            System.arraycopy(this.observedHotspotR2ApsPerEssInScanHistogram, 0, newArray30, 0, i30);
                        }
                        while (i30 < newArray30.length - 1) {
                            newArray30[i30] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray30[i30]);
                            input.readTag();
                            i30++;
                        }
                        newArray30[i30] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray30[i30]);
                        this.observedHotspotR2ApsPerEssInScanHistogram = newArray30;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_READ_SMS:
                        int arrayLength31 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_READ_SMS);
                        int i31 = this.softApConnectedClientsEventsTethered == null ? 0 : this.softApConnectedClientsEventsTethered.length;
                        SoftApConnectedClientsEvent[] newArray31 = new SoftApConnectedClientsEvent[(i31 + arrayLength31)];
                        if (i31 != 0) {
                            System.arraycopy(this.softApConnectedClientsEventsTethered, 0, newArray31, 0, i31);
                        }
                        while (i31 < newArray31.length - 1) {
                            newArray31[i31] = new SoftApConnectedClientsEvent();
                            input.readMessage(newArray31[i31]);
                            input.readTag();
                            i31++;
                        }
                        newArray31[i31] = new SoftApConnectedClientsEvent();
                        input.readMessage(newArray31[i31]);
                        this.softApConnectedClientsEventsTethered = newArray31;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_RECEIVE_MMS:
                        int arrayLength32 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_RECEIVE_MMS);
                        int i32 = this.softApConnectedClientsEventsLocalOnly == null ? 0 : this.softApConnectedClientsEventsLocalOnly.length;
                        SoftApConnectedClientsEvent[] newArray32 = new SoftApConnectedClientsEvent[(i32 + arrayLength32)];
                        if (i32 != 0) {
                            System.arraycopy(this.softApConnectedClientsEventsLocalOnly, 0, newArray32, 0, i32);
                        }
                        while (i32 < newArray32.length - 1) {
                            newArray32[i32] = new SoftApConnectedClientsEvent();
                            input.readMessage(newArray32[i32]);
                            input.readTag();
                            i32++;
                        }
                        newArray32[i32] = new SoftApConnectedClientsEvent();
                        input.readMessage(newArray32[i32]);
                        this.softApConnectedClientsEventsLocalOnly = newArray32;
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                        if (this.wpsMetrics == null) {
                            this.wpsMetrics = new WpsMetrics();
                        }
                        input.readMessage(this.wpsMetrics);
                        break;
                    case MetricsProto.MetricsEvent.ACTION_PERMISSION_DENIED_READ_PHONE_NUMBERS:
                        if (this.wifiPowerStats == null) {
                            this.wifiPowerStats = new WifiPowerStats();
                        }
                        input.readMessage(this.wifiPowerStats);
                        break;
                    case 744:
                        this.numConnectivityOneshotScans = input.readInt32();
                        break;
                    case 754:
                        if (this.wifiWakeStats == null) {
                            this.wifiWakeStats = new WifiWakeStats();
                        }
                        input.readMessage(this.wifiWakeStats);
                        break;
                    case MetricsProto.MetricsEvent.ACTION_LEAVE_SEARCH_RESULT_WITHOUT_QUERY:
                        int arrayLength33 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.ACTION_LEAVE_SEARCH_RESULT_WITHOUT_QUERY);
                        int i33 = this.observed80211McSupportingApsInScanHistogram == null ? 0 : this.observed80211McSupportingApsInScanHistogram.length;
                        NumConnectableNetworksBucket[] newArray33 = new NumConnectableNetworksBucket[(i33 + arrayLength33)];
                        if (i33 != 0) {
                            System.arraycopy(this.observed80211McSupportingApsInScanHistogram, 0, newArray33, 0, i33);
                        }
                        while (i33 < newArray33.length - 1) {
                            newArray33[i33] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray33[i33]);
                            input.readTag();
                            i33++;
                        }
                        newArray33[i33] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray33[i33]);
                        this.observed80211McSupportingApsInScanHistogram = newArray33;
                        break;
                    case 768:
                        this.numSupplicantCrashes = input.readInt32();
                        break;
                    case 776:
                        this.numHostapdCrashes = input.readInt32();
                        break;
                    case 784:
                        this.numSetupClientInterfaceFailureDueToSupplicant = input.readInt32();
                        break;
                    case 792:
                        this.numSetupSoftApInterfaceFailureDueToHal = input.readInt32();
                        break;
                    case 800:
                        this.numSetupSoftApInterfaceFailureDueToWificond = input.readInt32();
                        break;
                    case 808:
                        this.numSetupSoftApInterfaceFailureDueToHostapd = input.readInt32();
                        break;
                    case 816:
                        this.numClientInterfaceDown = input.readInt32();
                        break;
                    case 824:
                        this.numSoftApInterfaceDown = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.NOTIFICATION_SNOOZED_CRITERIA:
                        this.numExternalAppOneshotScanRequests = input.readInt32();
                        break;
                    case 840:
                        this.numExternalForegroundAppOneshotScanRequestsThrottled = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.FIELD_SETTINGS_BUILD_NUMBER_DEVELOPER_MODE_ENABLED:
                        this.numExternalBackgroundAppOneshotScanRequestsThrottled = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_NOTIFICATION_CHANNEL:
                        this.watchdogTriggerToConnectionSuccessDurationMs = input.readInt64();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_GET_CONTACT:
                        this.watchdogTotalConnectionFailureCountAfterTrigger = input.readInt64();
                        break;
                    case 872:
                        this.numOneshotHasDfsChannelScans = input.readInt32();
                        break;
                    case 882:
                        if (this.wifiRttLog == null) {
                            this.wifiRttLog = new WifiRttLog();
                        }
                        input.readMessage(this.wifiRttLog);
                        break;
                    case MetricsProto.MetricsEvent.ACTION_APPOP_GRANT_SYSTEM_ALERT_WINDOW:
                        this.isMacRandomizationOn = input.readBool();
                        break;
                    case 896:
                        this.numRadioModeChangeToMcc = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.APP_TRANSITION_CALLING_PACKAGE_NAME:
                        this.numRadioModeChangeToScc = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.AUTOFILL_AUTHENTICATED:
                        this.numRadioModeChangeToSbs = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.METRICS_CHECKPOINT:
                        this.numRadioModeChangeToDbs = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.FIELD_QS_VALUE:
                        this.numSoftApUserBandPreferenceUnsatisfied = input.readInt32();
                        break;
                    case 938:
                        this.scoreExperimentId = input.readString();
                        break;
                    case MetricsProto.MetricsEvent.FIELD_NOTIFICATION_GROUP_ID:
                        if (this.wifiRadioUsage == null) {
                            this.wifiRadioUsage = new WifiRadioUsage();
                        }
                        input.readMessage(this.wifiRadioUsage);
                        break;
                    case 954:
                        if (this.experimentValues == null) {
                            this.experimentValues = new ExperimentValues();
                        }
                        input.readMessage(this.experimentValues);
                        break;
                    case 962:
                        int arrayLength34 = WireFormatNano.getRepeatedFieldArrayLength(input, 962);
                        int i34 = this.wifiIsUnusableEventList == null ? 0 : this.wifiIsUnusableEventList.length;
                        WifiIsUnusableEvent[] newArray34 = new WifiIsUnusableEvent[(i34 + arrayLength34)];
                        if (i34 != 0) {
                            System.arraycopy(this.wifiIsUnusableEventList, 0, newArray34, 0, i34);
                        }
                        while (i34 < newArray34.length - 1) {
                            newArray34[i34] = new WifiIsUnusableEvent();
                            input.readMessage(newArray34[i34]);
                            input.readTag();
                            i34++;
                        }
                        newArray34[i34] = new WifiIsUnusableEvent();
                        input.readMessage(newArray34[i34]);
                        this.wifiIsUnusableEventList = newArray34;
                        break;
                    case 970:
                        int arrayLength35 = WireFormatNano.getRepeatedFieldArrayLength(input, 970);
                        int i35 = this.linkSpeedCounts == null ? 0 : this.linkSpeedCounts.length;
                        LinkSpeedCount[] newArray35 = new LinkSpeedCount[(i35 + arrayLength35)];
                        if (i35 != 0) {
                            System.arraycopy(this.linkSpeedCounts, 0, newArray35, 0, i35);
                        }
                        while (i35 < newArray35.length - 1) {
                            newArray35[i35] = new LinkSpeedCount();
                            input.readMessage(newArray35[i35]);
                            input.readTag();
                            i35++;
                        }
                        newArray35[i35] = new LinkSpeedCount();
                        input.readMessage(newArray35[i35]);
                        this.linkSpeedCounts = newArray35;
                        break;
                    case 976:
                        this.numSarSensorRegistrationFailures = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.SETTINGS_GESTURE_CAMERA_LIFT_TRIGGER:
                        int arrayLength36 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.SETTINGS_GESTURE_CAMERA_LIFT_TRIGGER);
                        int i36 = this.installedPasspointProfileTypeForR1 == null ? 0 : this.installedPasspointProfileTypeForR1.length;
                        PasspointProfileTypeCount[] newArray36 = new PasspointProfileTypeCount[(i36 + arrayLength36)];
                        if (i36 != 0) {
                            System.arraycopy(this.installedPasspointProfileTypeForR1, 0, newArray36, 0, i36);
                        }
                        while (i36 < newArray36.length - 1) {
                            newArray36[i36] = new PasspointProfileTypeCount();
                            input.readMessage(newArray36[i36]);
                            input.readTag();
                            i36++;
                        }
                        newArray36[i36] = new PasspointProfileTypeCount();
                        input.readMessage(newArray36[i36]);
                        this.installedPasspointProfileTypeForR1 = newArray36;
                        break;
                    case MetricsProto.MetricsEvent.FIELD_SETTINGS_PREFERENCE_CHANGE_LONG_VALUE:
                        this.hardwareRevision = input.readString();
                        break;
                    case 1002:
                        if (this.wifiLinkLayerUsageStats == null) {
                            this.wifiLinkLayerUsageStats = new WifiLinkLayerUsageStats();
                        }
                        input.readMessage(this.wifiLinkLayerUsageStats);
                        break;
                    case 1010:
                        int arrayLength37 = WireFormatNano.getRepeatedFieldArrayLength(input, 1010);
                        int i37 = this.wifiUsabilityStatsList == null ? 0 : this.wifiUsabilityStatsList.length;
                        WifiUsabilityStats[] newArray37 = new WifiUsabilityStats[(i37 + arrayLength37)];
                        if (i37 != 0) {
                            System.arraycopy(this.wifiUsabilityStatsList, 0, newArray37, 0, i37);
                        }
                        while (i37 < newArray37.length - 1) {
                            newArray37[i37] = new WifiUsabilityStats();
                            input.readMessage(newArray37[i37]);
                            input.readTag();
                            i37++;
                        }
                        newArray37[i37] = new WifiUsabilityStats();
                        input.readMessage(newArray37[i37]);
                        this.wifiUsabilityStatsList = newArray37;
                        break;
                    case 1018:
                        int arrayLength38 = WireFormatNano.getRepeatedFieldArrayLength(input, 1018);
                        int i38 = this.wifiUsabilityScoreCount == null ? 0 : this.wifiUsabilityScoreCount.length;
                        WifiUsabilityScoreCount[] newArray38 = new WifiUsabilityScoreCount[(i38 + arrayLength38)];
                        if (i38 != 0) {
                            System.arraycopy(this.wifiUsabilityScoreCount, 0, newArray38, 0, i38);
                        }
                        while (i38 < newArray38.length - 1) {
                            newArray38[i38] = new WifiUsabilityScoreCount();
                            input.readMessage(newArray38[i38]);
                            input.readTag();
                            i38++;
                        }
                        newArray38[i38] = new WifiUsabilityScoreCount();
                        input.readMessage(newArray38[i38]);
                        this.wifiUsabilityScoreCount = newArray38;
                        break;
                    case 1026:
                        int arrayLength39 = WireFormatNano.getRepeatedFieldArrayLength(input, 1026);
                        int i39 = this.mobilityStatePnoStatsList == null ? 0 : this.mobilityStatePnoStatsList.length;
                        DeviceMobilityStatePnoScanStats[] newArray39 = new DeviceMobilityStatePnoScanStats[(i39 + arrayLength39)];
                        if (i39 != 0) {
                            System.arraycopy(this.mobilityStatePnoStatsList, 0, newArray39, 0, i39);
                        }
                        while (i39 < newArray39.length - 1) {
                            newArray39[i39] = new DeviceMobilityStatePnoScanStats();
                            input.readMessage(newArray39[i39]);
                            input.readTag();
                            i39++;
                        }
                        newArray39[i39] = new DeviceMobilityStatePnoScanStats();
                        input.readMessage(newArray39[i39]);
                        this.mobilityStatePnoStatsList = newArray39;
                        break;
                    case 1034:
                        if (this.wifiP2PStats == null) {
                            this.wifiP2PStats = new WifiP2pStats();
                        }
                        input.readMessage(this.wifiP2PStats);
                        break;
                    case RILConstants.RIL_UNSOL_RADIO_CAPABILITY:
                        if (this.wifiDppLog == null) {
                            this.wifiDppLog = new WifiDppLog();
                        }
                        input.readMessage(this.wifiDppLog);
                        break;
                    case 1048:
                        this.numEnhancedOpenNetworks = input.readInt32();
                        break;
                    case 1056:
                        this.numWpa3PersonalNetworks = input.readInt32();
                        break;
                    case 1064:
                        this.numWpa3EnterpriseNetworks = input.readInt32();
                        break;
                    case 1072:
                        this.numEnhancedOpenNetworkScanResults = input.readInt32();
                        break;
                    case 1080:
                        this.numWpa3PersonalNetworkScanResults = input.readInt32();
                        break;
                    case 1088:
                        this.numWpa3EnterpriseNetworkScanResults = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_BOOT:
                        if (this.wifiConfigStoreIo == null) {
                            this.wifiConfigStoreIo = new WifiConfigStoreIO();
                        }
                        input.readMessage(this.wifiConfigStoreIo);
                        break;
                    case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_RESET:
                        this.numSavedNetworksWithMacRandomization = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_DRAG:
                        if (this.linkProbeStats == null) {
                            this.linkProbeStats = new LinkProbeStats();
                        }
                        input.readMessage(this.linkProbeStats);
                        break;
                    case MetricsProto.MetricsEvent.FIELD_SELECTION_RANGE:
                        int arrayLength40 = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsProto.MetricsEvent.FIELD_SELECTION_RANGE);
                        int i40 = this.networkSelectionExperimentDecisionsList == null ? 0 : this.networkSelectionExperimentDecisionsList.length;
                        NetworkSelectionExperimentDecisions[] newArray40 = new NetworkSelectionExperimentDecisions[(i40 + arrayLength40)];
                        if (i40 != 0) {
                            System.arraycopy(this.networkSelectionExperimentDecisionsList, 0, newArray40, 0, i40);
                        }
                        while (i40 < newArray40.length - 1) {
                            newArray40[i40] = new NetworkSelectionExperimentDecisions();
                            input.readMessage(newArray40[i40]);
                            input.readTag();
                            i40++;
                        }
                        newArray40[i40] = new NetworkSelectionExperimentDecisions();
                        input.readMessage(newArray40[i40]);
                        this.networkSelectionExperimentDecisionsList = newArray40;
                        break;
                    case MetricsProto.MetricsEvent.FIELD_AUTOFILL_SAVE_TYPE:
                        if (this.wifiNetworkRequestApiLog == null) {
                            this.wifiNetworkRequestApiLog = new WifiNetworkRequestApiLog();
                        }
                        input.readMessage(this.wifiNetworkRequestApiLog);
                        break;
                    case MetricsProto.MetricsEvent.NOTIFICATION_SELECT_SNOOZE:
                        if (this.wifiNetworkSuggestionApiLog == null) {
                            this.wifiNetworkSuggestionApiLog = new WifiNetworkSuggestionApiLog();
                        }
                        input.readMessage(this.wifiNetworkSuggestionApiLog);
                        break;
                    case 1146:
                        if (this.wifiLockStats == null) {
                            this.wifiLockStats = new WifiLockStats();
                        }
                        input.readMessage(this.wifiLockStats);
                        break;
                    case 1154:
                        if (this.wifiToggleStats == null) {
                            this.wifiToggleStats = new WifiToggleStats();
                        }
                        input.readMessage(this.wifiToggleStats);
                        break;
                    case 1160:
                        this.numAddOrUpdateNetworkCalls = input.readInt32();
                        break;
                    case 1168:
                        this.numEnableNetworkCalls = input.readInt32();
                        break;
                    case 1178:
                        if (this.passpointProvisionStats == null) {
                            this.passpointProvisionStats = new PasspointProvisionStats();
                        }
                        input.readMessage(this.passpointProvisionStats);
                        break;
                    case 1186:
                        int arrayLength41 = WireFormatNano.getRepeatedFieldArrayLength(input, 1186);
                        int i41 = this.installedPasspointProfileTypeForR2 == null ? 0 : this.installedPasspointProfileTypeForR2.length;
                        PasspointProfileTypeCount[] newArray41 = new PasspointProfileTypeCount[(i41 + arrayLength41)];
                        if (i41 != 0) {
                            System.arraycopy(this.installedPasspointProfileTypeForR2, 0, newArray41, 0, i41);
                        }
                        while (i41 < newArray41.length - 1) {
                            newArray41[i41] = new PasspointProfileTypeCount();
                            input.readMessage(newArray41[i41]);
                            input.readTag();
                            i41++;
                        }
                        newArray41[i41] = new PasspointProfileTypeCount();
                        input.readMessage(newArray41[i41]);
                        this.installedPasspointProfileTypeForR2 = newArray41;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static WifiLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiLog) MessageNano.mergeFrom(new WifiLog(), data);
        }

        public static WifiLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiLog().mergeFrom(input);
        }
    }

    public static final class RouterFingerPrint extends MessageNano {
        public static final int AUTH_ENTERPRISE = 3;
        public static final int AUTH_OPEN = 1;
        public static final int AUTH_PERSONAL = 2;
        public static final int AUTH_UNKNOWN = 0;
        public static final int ROAM_TYPE_DBDC = 3;
        public static final int ROAM_TYPE_ENTERPRISE = 2;
        public static final int ROAM_TYPE_NONE = 1;
        public static final int ROAM_TYPE_UNKNOWN = 0;
        public static final int ROUTER_TECH_A = 1;
        public static final int ROUTER_TECH_AC = 5;
        public static final int ROUTER_TECH_B = 2;
        public static final int ROUTER_TECH_G = 3;
        public static final int ROUTER_TECH_N = 4;
        public static final int ROUTER_TECH_OTHER = 6;
        public static final int ROUTER_TECH_UNKNOWN = 0;
        private static volatile RouterFingerPrint[] _emptyArray;
        public int authentication;
        public int channelInfo;
        public int dtim;
        public boolean hidden;
        public boolean passpoint;
        public int roamType;
        public int routerTechnology;
        public boolean supportsIpv6;

        public static RouterFingerPrint[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new RouterFingerPrint[0];
                    }
                }
            }
            return _emptyArray;
        }

        public RouterFingerPrint() {
            clear();
        }

        public RouterFingerPrint clear() {
            this.roamType = 0;
            this.channelInfo = 0;
            this.dtim = 0;
            this.authentication = 0;
            this.hidden = false;
            this.routerTechnology = 0;
            this.supportsIpv6 = false;
            this.passpoint = false;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.roamType != 0) {
                output.writeInt32(1, this.roamType);
            }
            if (this.channelInfo != 0) {
                output.writeInt32(2, this.channelInfo);
            }
            if (this.dtim != 0) {
                output.writeInt32(3, this.dtim);
            }
            if (this.authentication != 0) {
                output.writeInt32(4, this.authentication);
            }
            if (this.hidden) {
                output.writeBool(5, this.hidden);
            }
            if (this.routerTechnology != 0) {
                output.writeInt32(6, this.routerTechnology);
            }
            if (this.supportsIpv6) {
                output.writeBool(7, this.supportsIpv6);
            }
            if (this.passpoint) {
                output.writeBool(8, this.passpoint);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.roamType != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.roamType);
            }
            if (this.channelInfo != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.channelInfo);
            }
            if (this.dtim != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.dtim);
            }
            if (this.authentication != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.authentication);
            }
            if (this.hidden) {
                size += CodedOutputByteBufferNano.computeBoolSize(5, this.hidden);
            }
            if (this.routerTechnology != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, this.routerTechnology);
            }
            if (this.supportsIpv6) {
                size += CodedOutputByteBufferNano.computeBoolSize(7, this.supportsIpv6);
            }
            if (this.passpoint) {
                return size + CodedOutputByteBufferNano.computeBoolSize(8, this.passpoint);
            }
            return size;
        }

        public RouterFingerPrint mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                this.roamType = value;
                                break;
                        }
                    } else if (tag == 16) {
                        this.channelInfo = input.readInt32();
                    } else if (tag != 24) {
                        if (tag == 32) {
                            int value2 = input.readInt32();
                            switch (value2) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                    this.authentication = value2;
                                    break;
                            }
                        } else if (tag != 40) {
                            if (tag == 48) {
                                int value3 = input.readInt32();
                                switch (value3) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                        this.routerTechnology = value3;
                                        break;
                                }
                            } else if (tag == 56) {
                                this.supportsIpv6 = input.readBool();
                            } else if (tag == 64) {
                                this.passpoint = input.readBool();
                            } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                                return this;
                            }
                        } else {
                            this.hidden = input.readBool();
                        }
                    } else {
                        this.dtim = input.readInt32();
                    }
                } else {
                    return this;
                }
            }
        }

        public static RouterFingerPrint parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (RouterFingerPrint) MessageNano.mergeFrom(new RouterFingerPrint(), data);
        }

        public static RouterFingerPrint parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new RouterFingerPrint().mergeFrom(input);
        }
    }

    public static final class ConnectionEvent extends MessageNano {
        public static final int AUTH_FAILURE_EAP_FAILURE = 4;
        public static final int AUTH_FAILURE_NONE = 1;
        public static final int AUTH_FAILURE_TIMEOUT = 2;
        public static final int AUTH_FAILURE_WRONG_PSWD = 3;
        public static final int FAILURE_REASON_UNKNOWN = 0;
        public static final int HLF_DHCP = 2;
        public static final int HLF_NONE = 1;
        public static final int HLF_NO_INTERNET = 3;
        public static final int HLF_UNKNOWN = 0;
        public static final int HLF_UNWANTED = 4;
        public static final int NOMINATOR_CARRIER = 5;
        public static final int NOMINATOR_EXTERNAL_SCORED = 6;
        public static final int NOMINATOR_MANUAL = 1;
        public static final int NOMINATOR_OPEN_NETWORK_AVAILABLE = 9;
        public static final int NOMINATOR_PASSPOINT = 4;
        public static final int NOMINATOR_SAVED = 2;
        public static final int NOMINATOR_SAVED_USER_CONNECT_CHOICE = 8;
        public static final int NOMINATOR_SPECIFIER = 7;
        public static final int NOMINATOR_SUGGESTION = 3;
        public static final int NOMINATOR_UNKNOWN = 0;
        public static final int ROAM_DBDC = 2;
        public static final int ROAM_ENTERPRISE = 3;
        public static final int ROAM_NONE = 1;
        public static final int ROAM_UNKNOWN = 0;
        public static final int ROAM_UNRELATED = 5;
        public static final int ROAM_USER_SELECTED = 4;
        private static volatile ConnectionEvent[] _emptyArray;
        public boolean automaticBugReportTaken;
        public int connectionNominator;
        public int connectionResult;
        public int connectivityLevelFailureCode;
        public int durationTakenToConnectMillis;
        public int level2FailureCode;
        public int level2FailureReason;
        public int networkSelectorExperimentId;
        public int roamType;
        public RouterFingerPrint routerFingerprint;
        public int signalStrength;
        public long startTimeMillis;
        public boolean useRandomizedMac;

        public static ConnectionEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ConnectionEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ConnectionEvent() {
            clear();
        }

        public ConnectionEvent clear() {
            this.startTimeMillis = 0;
            this.durationTakenToConnectMillis = 0;
            this.routerFingerprint = null;
            this.signalStrength = 0;
            this.roamType = 0;
            this.connectionResult = 0;
            this.level2FailureCode = 0;
            this.connectivityLevelFailureCode = 0;
            this.automaticBugReportTaken = false;
            this.useRandomizedMac = false;
            this.connectionNominator = 0;
            this.networkSelectorExperimentId = 0;
            this.level2FailureReason = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.startTimeMillis != 0) {
                output.writeInt64(1, this.startTimeMillis);
            }
            if (this.durationTakenToConnectMillis != 0) {
                output.writeInt32(2, this.durationTakenToConnectMillis);
            }
            if (this.routerFingerprint != null) {
                output.writeMessage(3, this.routerFingerprint);
            }
            if (this.signalStrength != 0) {
                output.writeInt32(4, this.signalStrength);
            }
            if (this.roamType != 0) {
                output.writeInt32(5, this.roamType);
            }
            if (this.connectionResult != 0) {
                output.writeInt32(6, this.connectionResult);
            }
            if (this.level2FailureCode != 0) {
                output.writeInt32(7, this.level2FailureCode);
            }
            if (this.connectivityLevelFailureCode != 0) {
                output.writeInt32(8, this.connectivityLevelFailureCode);
            }
            if (this.automaticBugReportTaken) {
                output.writeBool(9, this.automaticBugReportTaken);
            }
            if (this.useRandomizedMac) {
                output.writeBool(10, this.useRandomizedMac);
            }
            if (this.connectionNominator != 0) {
                output.writeInt32(11, this.connectionNominator);
            }
            if (this.networkSelectorExperimentId != 0) {
                output.writeInt32(12, this.networkSelectorExperimentId);
            }
            if (this.level2FailureReason != 0) {
                output.writeInt32(13, this.level2FailureReason);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.startTimeMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, this.startTimeMillis);
            }
            if (this.durationTakenToConnectMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.durationTakenToConnectMillis);
            }
            if (this.routerFingerprint != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(3, this.routerFingerprint);
            }
            if (this.signalStrength != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.signalStrength);
            }
            if (this.roamType != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, this.roamType);
            }
            if (this.connectionResult != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, this.connectionResult);
            }
            if (this.level2FailureCode != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, this.level2FailureCode);
            }
            if (this.connectivityLevelFailureCode != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, this.connectivityLevelFailureCode);
            }
            if (this.automaticBugReportTaken) {
                size += CodedOutputByteBufferNano.computeBoolSize(9, this.automaticBugReportTaken);
            }
            if (this.useRandomizedMac) {
                size += CodedOutputByteBufferNano.computeBoolSize(10, this.useRandomizedMac);
            }
            if (this.connectionNominator != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, this.connectionNominator);
            }
            if (this.networkSelectorExperimentId != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(12, this.networkSelectorExperimentId);
            }
            if (this.level2FailureReason != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(13, this.level2FailureReason);
            }
            return size;
        }

        public ConnectionEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.startTimeMillis = input.readInt64();
                        break;
                    case 16:
                        this.durationTakenToConnectMillis = input.readInt32();
                        break;
                    case 26:
                        if (this.routerFingerprint == null) {
                            this.routerFingerprint = new RouterFingerPrint();
                        }
                        input.readMessage(this.routerFingerprint);
                        break;
                    case 32:
                        this.signalStrength = input.readInt32();
                        break;
                    case 40:
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                this.roamType = value;
                                break;
                        }
                    case 48:
                        this.connectionResult = input.readInt32();
                        break;
                    case 56:
                        this.level2FailureCode = input.readInt32();
                        break;
                    case 64:
                        int value2 = input.readInt32();
                        switch (value2) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                this.connectivityLevelFailureCode = value2;
                                break;
                        }
                    case 72:
                        this.automaticBugReportTaken = input.readBool();
                        break;
                    case 80:
                        this.useRandomizedMac = input.readBool();
                        break;
                    case 88:
                        int value3 = input.readInt32();
                        switch (value3) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                                this.connectionNominator = value3;
                                break;
                        }
                    case 96:
                        this.networkSelectorExperimentId = input.readInt32();
                        break;
                    case 104:
                        int value4 = input.readInt32();
                        switch (value4) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                this.level2FailureReason = value4;
                                break;
                        }
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static ConnectionEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ConnectionEvent) MessageNano.mergeFrom(new ConnectionEvent(), data);
        }

        public static ConnectionEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ConnectionEvent().mergeFrom(input);
        }
    }

    public static final class RssiPollCount extends MessageNano {
        private static volatile RssiPollCount[] _emptyArray;
        public int count;
        public int frequency;
        public int rssi;

        public static RssiPollCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new RssiPollCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public RssiPollCount() {
            clear();
        }

        public RssiPollCount clear() {
            this.rssi = 0;
            this.count = 0;
            this.frequency = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.rssi != 0) {
                output.writeInt32(1, this.rssi);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            if (this.frequency != 0) {
                output.writeInt32(3, this.frequency);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.rssi != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.rssi);
            }
            if (this.count != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            if (this.frequency != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, this.frequency);
            }
            return size;
        }

        public RssiPollCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.rssi = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (tag == 24) {
                    this.frequency = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static RssiPollCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (RssiPollCount) MessageNano.mergeFrom(new RssiPollCount(), data);
        }

        public static RssiPollCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new RssiPollCount().mergeFrom(input);
        }
    }

    public static final class AlertReasonCount extends MessageNano {
        private static volatile AlertReasonCount[] _emptyArray;
        public int count;
        public int reason;

        public static AlertReasonCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new AlertReasonCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public AlertReasonCount() {
            clear();
        }

        public AlertReasonCount clear() {
            this.reason = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.reason != 0) {
                output.writeInt32(1, this.reason);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.reason != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.reason);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            return size;
        }

        public AlertReasonCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.reason = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static AlertReasonCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (AlertReasonCount) MessageNano.mergeFrom(new AlertReasonCount(), data);
        }

        public static AlertReasonCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new AlertReasonCount().mergeFrom(input);
        }
    }

    public static final class WifiScoreCount extends MessageNano {
        private static volatile WifiScoreCount[] _emptyArray;
        public int count;
        public int score;

        public static WifiScoreCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiScoreCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiScoreCount() {
            clear();
        }

        public WifiScoreCount clear() {
            this.score = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.score != 0) {
                output.writeInt32(1, this.score);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.score != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.score);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            return size;
        }

        public WifiScoreCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.score = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiScoreCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiScoreCount) MessageNano.mergeFrom(new WifiScoreCount(), data);
        }

        public static WifiScoreCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiScoreCount().mergeFrom(input);
        }
    }

    public static final class WifiUsabilityScoreCount extends MessageNano {
        private static volatile WifiUsabilityScoreCount[] _emptyArray;
        public int count;
        public int score;

        public static WifiUsabilityScoreCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiUsabilityScoreCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiUsabilityScoreCount() {
            clear();
        }

        public WifiUsabilityScoreCount clear() {
            this.score = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.score != 0) {
                output.writeInt32(1, this.score);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.score != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.score);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            return size;
        }

        public WifiUsabilityScoreCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.score = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiUsabilityScoreCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiUsabilityScoreCount) MessageNano.mergeFrom(new WifiUsabilityScoreCount(), data);
        }

        public static WifiUsabilityScoreCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiUsabilityScoreCount().mergeFrom(input);
        }
    }

    public static final class LinkSpeedCount extends MessageNano {
        private static volatile LinkSpeedCount[] _emptyArray;
        public int count;
        public int linkSpeedMbps;
        public int rssiSumDbm;
        public long rssiSumOfSquaresDbmSq;

        public static LinkSpeedCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new LinkSpeedCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public LinkSpeedCount() {
            clear();
        }

        public LinkSpeedCount clear() {
            this.linkSpeedMbps = 0;
            this.count = 0;
            this.rssiSumDbm = 0;
            this.rssiSumOfSquaresDbmSq = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.linkSpeedMbps != 0) {
                output.writeInt32(1, this.linkSpeedMbps);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            if (this.rssiSumDbm != 0) {
                output.writeInt32(3, this.rssiSumDbm);
            }
            if (this.rssiSumOfSquaresDbmSq != 0) {
                output.writeInt64(4, this.rssiSumOfSquaresDbmSq);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.linkSpeedMbps != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.linkSpeedMbps);
            }
            if (this.count != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            if (this.rssiSumDbm != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.rssiSumDbm);
            }
            if (this.rssiSumOfSquaresDbmSq != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(4, this.rssiSumOfSquaresDbmSq);
            }
            return size;
        }

        public LinkSpeedCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.linkSpeedMbps = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (tag == 24) {
                    this.rssiSumDbm = input.readInt32();
                } else if (tag == 32) {
                    this.rssiSumOfSquaresDbmSq = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static LinkSpeedCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (LinkSpeedCount) MessageNano.mergeFrom(new LinkSpeedCount(), data);
        }

        public static LinkSpeedCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new LinkSpeedCount().mergeFrom(input);
        }
    }

    public static final class SoftApDurationBucket extends MessageNano {
        private static volatile SoftApDurationBucket[] _emptyArray;
        public int bucketSizeSec;
        public int count;
        public int durationSec;

        public static SoftApDurationBucket[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new SoftApDurationBucket[0];
                    }
                }
            }
            return _emptyArray;
        }

        public SoftApDurationBucket() {
            clear();
        }

        public SoftApDurationBucket clear() {
            this.durationSec = 0;
            this.bucketSizeSec = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.durationSec != 0) {
                output.writeInt32(1, this.durationSec);
            }
            if (this.bucketSizeSec != 0) {
                output.writeInt32(2, this.bucketSizeSec);
            }
            if (this.count != 0) {
                output.writeInt32(3, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.durationSec != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.durationSec);
            }
            if (this.bucketSizeSec != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.bucketSizeSec);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, this.count);
            }
            return size;
        }

        public SoftApDurationBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.durationSec = input.readInt32();
                } else if (tag == 16) {
                    this.bucketSizeSec = input.readInt32();
                } else if (tag == 24) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static SoftApDurationBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (SoftApDurationBucket) MessageNano.mergeFrom(new SoftApDurationBucket(), data);
        }

        public static SoftApDurationBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new SoftApDurationBucket().mergeFrom(input);
        }
    }

    public static final class SoftApReturnCodeCount extends MessageNano {
        public static final int SOFT_AP_FAILED_GENERAL_ERROR = 2;
        public static final int SOFT_AP_FAILED_NO_CHANNEL = 3;
        public static final int SOFT_AP_RETURN_CODE_UNKNOWN = 0;
        public static final int SOFT_AP_STARTED_SUCCESSFULLY = 1;
        private static volatile SoftApReturnCodeCount[] _emptyArray;
        public int count;
        public int returnCode;
        public int startResult;

        public static SoftApReturnCodeCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new SoftApReturnCodeCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public SoftApReturnCodeCount() {
            clear();
        }

        public SoftApReturnCodeCount clear() {
            this.returnCode = 0;
            this.count = 0;
            this.startResult = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.returnCode != 0) {
                output.writeInt32(1, this.returnCode);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            if (this.startResult != 0) {
                output.writeInt32(3, this.startResult);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.returnCode != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.returnCode);
            }
            if (this.count != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            if (this.startResult != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, this.startResult);
            }
            return size;
        }

        public SoftApReturnCodeCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.returnCode = input.readInt32();
                } else if (tag != 16) {
                    if (tag == 24) {
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                this.startResult = value;
                                break;
                        }
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    this.count = input.readInt32();
                }
            }
        }

        public static SoftApReturnCodeCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (SoftApReturnCodeCount) MessageNano.mergeFrom(new SoftApReturnCodeCount(), data);
        }

        public static SoftApReturnCodeCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new SoftApReturnCodeCount().mergeFrom(input);
        }
    }

    public static final class StaEvent extends MessageNano {
        public static final int AUTH_FAILURE_EAP_FAILURE = 4;
        public static final int AUTH_FAILURE_NONE = 1;
        public static final int AUTH_FAILURE_TIMEOUT = 2;
        public static final int AUTH_FAILURE_UNKNOWN = 0;
        public static final int AUTH_FAILURE_WRONG_PSWD = 3;
        public static final int DISCONNECT_API = 1;
        public static final int DISCONNECT_GENERIC = 2;
        public static final int DISCONNECT_P2P_DISCONNECT_WIFI_REQUEST = 5;
        public static final int DISCONNECT_RESET_SIM_NETWORKS = 6;
        public static final int DISCONNECT_ROAM_WATCHDOG_TIMER = 4;
        public static final int DISCONNECT_UNKNOWN = 0;
        public static final int DISCONNECT_UNWANTED = 3;
        public static final int STATE_ASSOCIATED = 6;
        public static final int STATE_ASSOCIATING = 5;
        public static final int STATE_AUTHENTICATING = 4;
        public static final int STATE_COMPLETED = 9;
        public static final int STATE_DISCONNECTED = 0;
        public static final int STATE_DORMANT = 10;
        public static final int STATE_FOUR_WAY_HANDSHAKE = 7;
        public static final int STATE_GROUP_HANDSHAKE = 8;
        public static final int STATE_INACTIVE = 2;
        public static final int STATE_INTERFACE_DISABLED = 1;
        public static final int STATE_INVALID = 12;
        public static final int STATE_SCANNING = 3;
        public static final int STATE_UNINITIALIZED = 11;
        public static final int TYPE_ASSOCIATION_REJECTION_EVENT = 1;
        public static final int TYPE_AUTHENTICATION_FAILURE_EVENT = 2;
        public static final int TYPE_CMD_ASSOCIATED_BSSID = 6;
        public static final int TYPE_CMD_IP_CONFIGURATION_LOST = 8;
        public static final int TYPE_CMD_IP_CONFIGURATION_SUCCESSFUL = 7;
        public static final int TYPE_CMD_IP_REACHABILITY_LOST = 9;
        public static final int TYPE_CMD_START_CONNECT = 11;
        public static final int TYPE_CMD_START_ROAM = 12;
        public static final int TYPE_CMD_TARGET_BSSID = 10;
        public static final int TYPE_CONNECT_NETWORK = 13;
        public static final int TYPE_FRAMEWORK_DISCONNECT = 15;
        public static final int TYPE_LINK_PROBE = 21;
        public static final int TYPE_MAC_CHANGE = 17;
        public static final int TYPE_NETWORK_AGENT_VALID_NETWORK = 14;
        public static final int TYPE_NETWORK_CONNECTION_EVENT = 3;
        public static final int TYPE_NETWORK_DISCONNECTION_EVENT = 4;
        public static final int TYPE_SCORE_BREACH = 16;
        public static final int TYPE_SUPPLICANT_STATE_CHANGE_EVENT = 5;
        public static final int TYPE_UNKNOWN = 0;
        public static final int TYPE_WIFI_DISABLED = 19;
        public static final int TYPE_WIFI_ENABLED = 18;
        public static final int TYPE_WIFI_USABILITY_SCORE_BREACH = 20;
        private static volatile StaEvent[] _emptyArray;
        public boolean associationTimedOut;
        public int authFailureReason;
        public ConfigInfo configInfo;
        public int frameworkDisconnectReason;
        public int lastFreq;
        public int lastLinkSpeed;
        public int lastPredictionHorizonSec;
        public int lastRssi;
        public int lastScore;
        public int lastWifiUsabilityScore;
        public int linkProbeFailureReason;
        public int linkProbeSuccessElapsedTimeMs;
        public boolean linkProbeWasSuccess;
        public boolean localGen;
        public int reason;
        public long startTimeMillis;
        public int status;
        public int supplicantStateChangesBitmask;
        public int type;

        public static final class ConfigInfo extends MessageNano {
            private static volatile ConfigInfo[] _emptyArray;
            public int allowedAuthAlgorithms;
            public int allowedGroupCiphers;
            public int allowedKeyManagement;
            public int allowedPairwiseCiphers;
            public int allowedProtocols;
            public boolean hasEverConnected;
            public boolean hiddenSsid;
            public boolean isEphemeral;
            public boolean isPasspoint;
            public int scanFreq;
            public int scanRssi;

            public static ConfigInfo[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ConfigInfo[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ConfigInfo() {
                clear();
            }

            public ConfigInfo clear() {
                this.allowedKeyManagement = 0;
                this.allowedProtocols = 0;
                this.allowedAuthAlgorithms = 0;
                this.allowedPairwiseCiphers = 0;
                this.allowedGroupCiphers = 0;
                this.hiddenSsid = false;
                this.isPasspoint = false;
                this.isEphemeral = false;
                this.hasEverConnected = false;
                this.scanRssi = -127;
                this.scanFreq = -1;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.allowedKeyManagement != 0) {
                    output.writeUInt32(1, this.allowedKeyManagement);
                }
                if (this.allowedProtocols != 0) {
                    output.writeUInt32(2, this.allowedProtocols);
                }
                if (this.allowedAuthAlgorithms != 0) {
                    output.writeUInt32(3, this.allowedAuthAlgorithms);
                }
                if (this.allowedPairwiseCiphers != 0) {
                    output.writeUInt32(4, this.allowedPairwiseCiphers);
                }
                if (this.allowedGroupCiphers != 0) {
                    output.writeUInt32(5, this.allowedGroupCiphers);
                }
                if (this.hiddenSsid) {
                    output.writeBool(6, this.hiddenSsid);
                }
                if (this.isPasspoint) {
                    output.writeBool(7, this.isPasspoint);
                }
                if (this.isEphemeral) {
                    output.writeBool(8, this.isEphemeral);
                }
                if (this.hasEverConnected) {
                    output.writeBool(9, this.hasEverConnected);
                }
                if (this.scanRssi != -127) {
                    output.writeInt32(10, this.scanRssi);
                }
                if (this.scanFreq != -1) {
                    output.writeInt32(11, this.scanFreq);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.allowedKeyManagement != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(1, this.allowedKeyManagement);
                }
                if (this.allowedProtocols != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(2, this.allowedProtocols);
                }
                if (this.allowedAuthAlgorithms != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(3, this.allowedAuthAlgorithms);
                }
                if (this.allowedPairwiseCiphers != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(4, this.allowedPairwiseCiphers);
                }
                if (this.allowedGroupCiphers != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(5, this.allowedGroupCiphers);
                }
                if (this.hiddenSsid) {
                    size += CodedOutputByteBufferNano.computeBoolSize(6, this.hiddenSsid);
                }
                if (this.isPasspoint) {
                    size += CodedOutputByteBufferNano.computeBoolSize(7, this.isPasspoint);
                }
                if (this.isEphemeral) {
                    size += CodedOutputByteBufferNano.computeBoolSize(8, this.isEphemeral);
                }
                if (this.hasEverConnected) {
                    size += CodedOutputByteBufferNano.computeBoolSize(9, this.hasEverConnected);
                }
                if (this.scanRssi != -127) {
                    size += CodedOutputByteBufferNano.computeInt32Size(10, this.scanRssi);
                }
                if (this.scanFreq != -1) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(11, this.scanFreq);
                }
                return size;
            }

            public ConfigInfo mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            return this;
                        case 8:
                            this.allowedKeyManagement = input.readUInt32();
                            break;
                        case 16:
                            this.allowedProtocols = input.readUInt32();
                            break;
                        case 24:
                            this.allowedAuthAlgorithms = input.readUInt32();
                            break;
                        case 32:
                            this.allowedPairwiseCiphers = input.readUInt32();
                            break;
                        case 40:
                            this.allowedGroupCiphers = input.readUInt32();
                            break;
                        case 48:
                            this.hiddenSsid = input.readBool();
                            break;
                        case 56:
                            this.isPasspoint = input.readBool();
                            break;
                        case 64:
                            this.isEphemeral = input.readBool();
                            break;
                        case 72:
                            this.hasEverConnected = input.readBool();
                            break;
                        case 80:
                            this.scanRssi = input.readInt32();
                            break;
                        case 88:
                            this.scanFreq = input.readInt32();
                            break;
                        default:
                            if (WireFormatNano.parseUnknownField(input, tag)) {
                                break;
                            } else {
                                return this;
                            }
                    }
                }
            }

            public static ConfigInfo parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ConfigInfo) MessageNano.mergeFrom(new ConfigInfo(), data);
            }

            public static ConfigInfo parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ConfigInfo().mergeFrom(input);
            }
        }

        public static StaEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new StaEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public StaEvent() {
            clear();
        }

        public StaEvent clear() {
            this.type = 0;
            this.reason = -1;
            this.status = -1;
            this.localGen = false;
            this.configInfo = null;
            this.lastRssi = -127;
            this.lastLinkSpeed = -1;
            this.lastFreq = -1;
            this.supplicantStateChangesBitmask = 0;
            this.startTimeMillis = 0;
            this.frameworkDisconnectReason = 0;
            this.associationTimedOut = false;
            this.authFailureReason = 0;
            this.lastScore = -1;
            this.lastWifiUsabilityScore = -1;
            this.lastPredictionHorizonSec = -1;
            this.linkProbeWasSuccess = false;
            this.linkProbeSuccessElapsedTimeMs = 0;
            this.linkProbeFailureReason = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.type != 0) {
                output.writeInt32(1, this.type);
            }
            if (this.reason != -1) {
                output.writeInt32(2, this.reason);
            }
            if (this.status != -1) {
                output.writeInt32(3, this.status);
            }
            if (this.localGen) {
                output.writeBool(4, this.localGen);
            }
            if (this.configInfo != null) {
                output.writeMessage(5, this.configInfo);
            }
            if (this.lastRssi != -127) {
                output.writeInt32(6, this.lastRssi);
            }
            if (this.lastLinkSpeed != -1) {
                output.writeInt32(7, this.lastLinkSpeed);
            }
            if (this.lastFreq != -1) {
                output.writeInt32(8, this.lastFreq);
            }
            if (this.supplicantStateChangesBitmask != 0) {
                output.writeUInt32(9, this.supplicantStateChangesBitmask);
            }
            if (this.startTimeMillis != 0) {
                output.writeInt64(10, this.startTimeMillis);
            }
            if (this.frameworkDisconnectReason != 0) {
                output.writeInt32(11, this.frameworkDisconnectReason);
            }
            if (this.associationTimedOut) {
                output.writeBool(12, this.associationTimedOut);
            }
            if (this.authFailureReason != 0) {
                output.writeInt32(13, this.authFailureReason);
            }
            if (this.lastScore != -1) {
                output.writeInt32(14, this.lastScore);
            }
            if (this.lastWifiUsabilityScore != -1) {
                output.writeInt32(15, this.lastWifiUsabilityScore);
            }
            if (this.lastPredictionHorizonSec != -1) {
                output.writeInt32(16, this.lastPredictionHorizonSec);
            }
            if (this.linkProbeWasSuccess) {
                output.writeBool(17, this.linkProbeWasSuccess);
            }
            if (this.linkProbeSuccessElapsedTimeMs != 0) {
                output.writeInt32(18, this.linkProbeSuccessElapsedTimeMs);
            }
            if (this.linkProbeFailureReason != 0) {
                output.writeInt32(19, this.linkProbeFailureReason);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.type != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.type);
            }
            if (this.reason != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.reason);
            }
            if (this.status != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.status);
            }
            if (this.localGen) {
                size += CodedOutputByteBufferNano.computeBoolSize(4, this.localGen);
            }
            if (this.configInfo != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(5, this.configInfo);
            }
            if (this.lastRssi != -127) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, this.lastRssi);
            }
            if (this.lastLinkSpeed != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, this.lastLinkSpeed);
            }
            if (this.lastFreq != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, this.lastFreq);
            }
            if (this.supplicantStateChangesBitmask != 0) {
                size += CodedOutputByteBufferNano.computeUInt32Size(9, this.supplicantStateChangesBitmask);
            }
            if (this.startTimeMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(10, this.startTimeMillis);
            }
            if (this.frameworkDisconnectReason != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, this.frameworkDisconnectReason);
            }
            if (this.associationTimedOut) {
                size += CodedOutputByteBufferNano.computeBoolSize(12, this.associationTimedOut);
            }
            if (this.authFailureReason != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(13, this.authFailureReason);
            }
            if (this.lastScore != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(14, this.lastScore);
            }
            if (this.lastWifiUsabilityScore != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(15, this.lastWifiUsabilityScore);
            }
            if (this.lastPredictionHorizonSec != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(16, this.lastPredictionHorizonSec);
            }
            if (this.linkProbeWasSuccess) {
                size += CodedOutputByteBufferNano.computeBoolSize(17, this.linkProbeWasSuccess);
            }
            if (this.linkProbeSuccessElapsedTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(18, this.linkProbeSuccessElapsedTimeMs);
            }
            if (this.linkProbeFailureReason != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(19, this.linkProbeFailureReason);
            }
            return size;
        }

        public StaEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                                this.type = value;
                                break;
                        }
                    case 16:
                        this.reason = input.readInt32();
                        break;
                    case 24:
                        this.status = input.readInt32();
                        break;
                    case 32:
                        this.localGen = input.readBool();
                        break;
                    case 42:
                        if (this.configInfo == null) {
                            this.configInfo = new ConfigInfo();
                        }
                        input.readMessage(this.configInfo);
                        break;
                    case 48:
                        this.lastRssi = input.readInt32();
                        break;
                    case 56:
                        this.lastLinkSpeed = input.readInt32();
                        break;
                    case 64:
                        this.lastFreq = input.readInt32();
                        break;
                    case 72:
                        this.supplicantStateChangesBitmask = input.readUInt32();
                        break;
                    case 80:
                        this.startTimeMillis = input.readInt64();
                        break;
                    case 88:
                        int value2 = input.readInt32();
                        switch (value2) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                this.frameworkDisconnectReason = value2;
                                break;
                        }
                    case 96:
                        this.associationTimedOut = input.readBool();
                        break;
                    case 104:
                        int value3 = input.readInt32();
                        switch (value3) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                this.authFailureReason = value3;
                                break;
                        }
                    case 112:
                        this.lastScore = input.readInt32();
                        break;
                    case 120:
                        this.lastWifiUsabilityScore = input.readInt32();
                        break;
                    case 128:
                        this.lastPredictionHorizonSec = input.readInt32();
                        break;
                    case 136:
                        this.linkProbeWasSuccess = input.readBool();
                        break;
                    case 144:
                        this.linkProbeSuccessElapsedTimeMs = input.readInt32();
                        break;
                    case 152:
                        int value4 = input.readInt32();
                        switch (value4) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                this.linkProbeFailureReason = value4;
                                break;
                        }
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static StaEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (StaEvent) MessageNano.mergeFrom(new StaEvent(), data);
        }

        public static StaEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new StaEvent().mergeFrom(input);
        }
    }

    public static final class WifiAwareLog extends MessageNano {
        public static final int ALREADY_ENABLED = 11;
        public static final int FOLLOWUP_TX_QUEUE_FULL = 12;
        public static final int INTERNAL_FAILURE = 2;
        public static final int INVALID_ARGS = 6;
        public static final int INVALID_NDP_ID = 8;
        public static final int INVALID_PEER_ID = 7;
        public static final int INVALID_SESSION_ID = 4;
        public static final int NAN_NOT_ALLOWED = 9;
        public static final int NO_OTA_ACK = 10;
        public static final int NO_RESOURCES_AVAILABLE = 5;
        public static final int PROTOCOL_FAILURE = 3;
        public static final int SUCCESS = 1;
        public static final int UNKNOWN = 0;
        public static final int UNKNOWN_HAL_STATUS = 14;
        public static final int UNSUPPORTED_CONCURRENCY_NAN_DISABLED = 13;
        private static volatile WifiAwareLog[] _emptyArray;
        public long availableTimeMs;
        public long enabledTimeMs;
        public HistogramBucket[] histogramAttachDurationMs;
        public NanStatusHistogramBucket[] histogramAttachSessionStatus;
        public HistogramBucket[] histogramAwareAvailableDurationMs;
        public HistogramBucket[] histogramAwareEnabledDurationMs;
        public HistogramBucket[] histogramNdpCreationTimeMs;
        public HistogramBucket[] histogramNdpSessionDataUsageMb;
        public HistogramBucket[] histogramNdpSessionDurationMs;
        public HistogramBucket[] histogramPublishSessionDurationMs;
        public NanStatusHistogramBucket[] histogramPublishStatus;
        public NanStatusHistogramBucket[] histogramRequestNdpOobStatus;
        public NanStatusHistogramBucket[] histogramRequestNdpStatus;
        public HistogramBucket[] histogramSubscribeGeofenceMax;
        public HistogramBucket[] histogramSubscribeGeofenceMin;
        public HistogramBucket[] histogramSubscribeSessionDurationMs;
        public NanStatusHistogramBucket[] histogramSubscribeStatus;
        public int maxConcurrentAttachSessionsInApp;
        public int maxConcurrentDiscoverySessionsInApp;
        public int maxConcurrentDiscoverySessionsInSystem;
        public int maxConcurrentNdiInApp;
        public int maxConcurrentNdiInSystem;
        public int maxConcurrentNdpInApp;
        public int maxConcurrentNdpInSystem;
        public int maxConcurrentNdpPerNdi;
        public int maxConcurrentPublishInApp;
        public int maxConcurrentPublishInSystem;
        public int maxConcurrentPublishWithRangingInApp;
        public int maxConcurrentPublishWithRangingInSystem;
        public int maxConcurrentSecureNdpInApp;
        public int maxConcurrentSecureNdpInSystem;
        public int maxConcurrentSubscribeInApp;
        public int maxConcurrentSubscribeInSystem;
        public int maxConcurrentSubscribeWithRangingInApp;
        public int maxConcurrentSubscribeWithRangingInSystem;
        public long ndpCreationTimeMsMax;
        public long ndpCreationTimeMsMin;
        public long ndpCreationTimeMsNumSamples;
        public long ndpCreationTimeMsSum;
        public long ndpCreationTimeMsSumOfSq;
        public int numApps;
        public int numAppsUsingIdentityCallback;
        public int numAppsWithDiscoverySessionFailureOutOfResources;
        public int numMatchesWithRanging;
        public int numMatchesWithoutRangingForRangingEnabledSubscribes;
        public int numSubscribesWithRanging;

        public static final class HistogramBucket extends MessageNano {
            private static volatile HistogramBucket[] _emptyArray;
            public int count;
            public long end;
            public long start;

            public static HistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new HistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public HistogramBucket() {
                clear();
            }

            public HistogramBucket clear() {
                this.start = 0;
                this.end = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.start != 0) {
                    output.writeInt64(1, this.start);
                }
                if (this.end != 0) {
                    output.writeInt64(2, this.end);
                }
                if (this.count != 0) {
                    output.writeInt32(3, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.start != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(1, this.start);
                }
                if (this.end != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(2, this.end);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(3, this.count);
                }
                return size;
            }

            public HistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.start = input.readInt64();
                    } else if (tag == 16) {
                        this.end = input.readInt64();
                    } else if (tag == 24) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static HistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (HistogramBucket) MessageNano.mergeFrom(new HistogramBucket(), data);
            }

            public static HistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new HistogramBucket().mergeFrom(input);
            }
        }

        public static final class NanStatusHistogramBucket extends MessageNano {
            private static volatile NanStatusHistogramBucket[] _emptyArray;
            public int count;
            public int nanStatusType;

            public static NanStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new NanStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public NanStatusHistogramBucket() {
                clear();
            }

            public NanStatusHistogramBucket clear() {
                this.nanStatusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.nanStatusType != 0) {
                    output.writeInt32(1, this.nanStatusType);
                }
                if (this.count != 0) {
                    output.writeInt32(2, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.nanStatusType != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.nanStatusType);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
                }
                return size;
            }

            public NanStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                    this.nanStatusType = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static NanStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (NanStatusHistogramBucket) MessageNano.mergeFrom(new NanStatusHistogramBucket(), data);
            }

            public static NanStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new NanStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static WifiAwareLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiAwareLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiAwareLog() {
            clear();
        }

        public WifiAwareLog clear() {
            this.numApps = 0;
            this.numAppsUsingIdentityCallback = 0;
            this.maxConcurrentAttachSessionsInApp = 0;
            this.histogramAttachSessionStatus = NanStatusHistogramBucket.emptyArray();
            this.maxConcurrentPublishInApp = 0;
            this.maxConcurrentSubscribeInApp = 0;
            this.maxConcurrentDiscoverySessionsInApp = 0;
            this.maxConcurrentPublishInSystem = 0;
            this.maxConcurrentSubscribeInSystem = 0;
            this.maxConcurrentDiscoverySessionsInSystem = 0;
            this.histogramPublishStatus = NanStatusHistogramBucket.emptyArray();
            this.histogramSubscribeStatus = NanStatusHistogramBucket.emptyArray();
            this.numAppsWithDiscoverySessionFailureOutOfResources = 0;
            this.histogramRequestNdpStatus = NanStatusHistogramBucket.emptyArray();
            this.histogramRequestNdpOobStatus = NanStatusHistogramBucket.emptyArray();
            this.maxConcurrentNdiInApp = 0;
            this.maxConcurrentNdiInSystem = 0;
            this.maxConcurrentNdpInApp = 0;
            this.maxConcurrentNdpInSystem = 0;
            this.maxConcurrentSecureNdpInApp = 0;
            this.maxConcurrentSecureNdpInSystem = 0;
            this.maxConcurrentNdpPerNdi = 0;
            this.histogramAwareAvailableDurationMs = HistogramBucket.emptyArray();
            this.histogramAwareEnabledDurationMs = HistogramBucket.emptyArray();
            this.histogramAttachDurationMs = HistogramBucket.emptyArray();
            this.histogramPublishSessionDurationMs = HistogramBucket.emptyArray();
            this.histogramSubscribeSessionDurationMs = HistogramBucket.emptyArray();
            this.histogramNdpSessionDurationMs = HistogramBucket.emptyArray();
            this.histogramNdpSessionDataUsageMb = HistogramBucket.emptyArray();
            this.histogramNdpCreationTimeMs = HistogramBucket.emptyArray();
            this.ndpCreationTimeMsMin = 0;
            this.ndpCreationTimeMsMax = 0;
            this.ndpCreationTimeMsSum = 0;
            this.ndpCreationTimeMsSumOfSq = 0;
            this.ndpCreationTimeMsNumSamples = 0;
            this.availableTimeMs = 0;
            this.enabledTimeMs = 0;
            this.maxConcurrentPublishWithRangingInApp = 0;
            this.maxConcurrentSubscribeWithRangingInApp = 0;
            this.maxConcurrentPublishWithRangingInSystem = 0;
            this.maxConcurrentSubscribeWithRangingInSystem = 0;
            this.histogramSubscribeGeofenceMin = HistogramBucket.emptyArray();
            this.histogramSubscribeGeofenceMax = HistogramBucket.emptyArray();
            this.numSubscribesWithRanging = 0;
            this.numMatchesWithRanging = 0;
            this.numMatchesWithoutRangingForRangingEnabledSubscribes = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numApps != 0) {
                output.writeInt32(1, this.numApps);
            }
            if (this.numAppsUsingIdentityCallback != 0) {
                output.writeInt32(2, this.numAppsUsingIdentityCallback);
            }
            if (this.maxConcurrentAttachSessionsInApp != 0) {
                output.writeInt32(3, this.maxConcurrentAttachSessionsInApp);
            }
            int i = 0;
            if (this.histogramAttachSessionStatus != null && this.histogramAttachSessionStatus.length > 0) {
                for (NanStatusHistogramBucket element : this.histogramAttachSessionStatus) {
                    if (element != null) {
                        output.writeMessage(4, element);
                    }
                }
            }
            if (this.maxConcurrentPublishInApp != 0) {
                output.writeInt32(5, this.maxConcurrentPublishInApp);
            }
            if (this.maxConcurrentSubscribeInApp != 0) {
                output.writeInt32(6, this.maxConcurrentSubscribeInApp);
            }
            if (this.maxConcurrentDiscoverySessionsInApp != 0) {
                output.writeInt32(7, this.maxConcurrentDiscoverySessionsInApp);
            }
            if (this.maxConcurrentPublishInSystem != 0) {
                output.writeInt32(8, this.maxConcurrentPublishInSystem);
            }
            if (this.maxConcurrentSubscribeInSystem != 0) {
                output.writeInt32(9, this.maxConcurrentSubscribeInSystem);
            }
            if (this.maxConcurrentDiscoverySessionsInSystem != 0) {
                output.writeInt32(10, this.maxConcurrentDiscoverySessionsInSystem);
            }
            if (this.histogramPublishStatus != null && this.histogramPublishStatus.length > 0) {
                for (NanStatusHistogramBucket element2 : this.histogramPublishStatus) {
                    if (element2 != null) {
                        output.writeMessage(11, element2);
                    }
                }
            }
            if (this.histogramSubscribeStatus != null && this.histogramSubscribeStatus.length > 0) {
                for (NanStatusHistogramBucket element3 : this.histogramSubscribeStatus) {
                    if (element3 != null) {
                        output.writeMessage(12, element3);
                    }
                }
            }
            if (this.numAppsWithDiscoverySessionFailureOutOfResources != 0) {
                output.writeInt32(13, this.numAppsWithDiscoverySessionFailureOutOfResources);
            }
            if (this.histogramRequestNdpStatus != null && this.histogramRequestNdpStatus.length > 0) {
                for (NanStatusHistogramBucket element4 : this.histogramRequestNdpStatus) {
                    if (element4 != null) {
                        output.writeMessage(14, element4);
                    }
                }
            }
            if (this.histogramRequestNdpOobStatus != null && this.histogramRequestNdpOobStatus.length > 0) {
                for (NanStatusHistogramBucket element5 : this.histogramRequestNdpOobStatus) {
                    if (element5 != null) {
                        output.writeMessage(15, element5);
                    }
                }
            }
            if (this.maxConcurrentNdiInApp != 0) {
                output.writeInt32(19, this.maxConcurrentNdiInApp);
            }
            if (this.maxConcurrentNdiInSystem != 0) {
                output.writeInt32(20, this.maxConcurrentNdiInSystem);
            }
            if (this.maxConcurrentNdpInApp != 0) {
                output.writeInt32(21, this.maxConcurrentNdpInApp);
            }
            if (this.maxConcurrentNdpInSystem != 0) {
                output.writeInt32(22, this.maxConcurrentNdpInSystem);
            }
            if (this.maxConcurrentSecureNdpInApp != 0) {
                output.writeInt32(23, this.maxConcurrentSecureNdpInApp);
            }
            if (this.maxConcurrentSecureNdpInSystem != 0) {
                output.writeInt32(24, this.maxConcurrentSecureNdpInSystem);
            }
            if (this.maxConcurrentNdpPerNdi != 0) {
                output.writeInt32(25, this.maxConcurrentNdpPerNdi);
            }
            if (this.histogramAwareAvailableDurationMs != null && this.histogramAwareAvailableDurationMs.length > 0) {
                for (HistogramBucket element6 : this.histogramAwareAvailableDurationMs) {
                    if (element6 != null) {
                        output.writeMessage(26, element6);
                    }
                }
            }
            if (this.histogramAwareEnabledDurationMs != null && this.histogramAwareEnabledDurationMs.length > 0) {
                for (HistogramBucket element7 : this.histogramAwareEnabledDurationMs) {
                    if (element7 != null) {
                        output.writeMessage(27, element7);
                    }
                }
            }
            if (this.histogramAttachDurationMs != null && this.histogramAttachDurationMs.length > 0) {
                for (HistogramBucket element8 : this.histogramAttachDurationMs) {
                    if (element8 != null) {
                        output.writeMessage(28, element8);
                    }
                }
            }
            if (this.histogramPublishSessionDurationMs != null && this.histogramPublishSessionDurationMs.length > 0) {
                for (HistogramBucket element9 : this.histogramPublishSessionDurationMs) {
                    if (element9 != null) {
                        output.writeMessage(29, element9);
                    }
                }
            }
            if (this.histogramSubscribeSessionDurationMs != null && this.histogramSubscribeSessionDurationMs.length > 0) {
                for (HistogramBucket element10 : this.histogramSubscribeSessionDurationMs) {
                    if (element10 != null) {
                        output.writeMessage(30, element10);
                    }
                }
            }
            if (this.histogramNdpSessionDurationMs != null && this.histogramNdpSessionDurationMs.length > 0) {
                for (HistogramBucket element11 : this.histogramNdpSessionDurationMs) {
                    if (element11 != null) {
                        output.writeMessage(31, element11);
                    }
                }
            }
            if (this.histogramNdpSessionDataUsageMb != null && this.histogramNdpSessionDataUsageMb.length > 0) {
                for (HistogramBucket element12 : this.histogramNdpSessionDataUsageMb) {
                    if (element12 != null) {
                        output.writeMessage(32, element12);
                    }
                }
            }
            if (this.histogramNdpCreationTimeMs != null && this.histogramNdpCreationTimeMs.length > 0) {
                for (HistogramBucket element13 : this.histogramNdpCreationTimeMs) {
                    if (element13 != null) {
                        output.writeMessage(33, element13);
                    }
                }
            }
            if (this.ndpCreationTimeMsMin != 0) {
                output.writeInt64(34, this.ndpCreationTimeMsMin);
            }
            if (this.ndpCreationTimeMsMax != 0) {
                output.writeInt64(35, this.ndpCreationTimeMsMax);
            }
            if (this.ndpCreationTimeMsSum != 0) {
                output.writeInt64(36, this.ndpCreationTimeMsSum);
            }
            if (this.ndpCreationTimeMsSumOfSq != 0) {
                output.writeInt64(37, this.ndpCreationTimeMsSumOfSq);
            }
            if (this.ndpCreationTimeMsNumSamples != 0) {
                output.writeInt64(38, this.ndpCreationTimeMsNumSamples);
            }
            if (this.availableTimeMs != 0) {
                output.writeInt64(39, this.availableTimeMs);
            }
            if (this.enabledTimeMs != 0) {
                output.writeInt64(40, this.enabledTimeMs);
            }
            if (this.maxConcurrentPublishWithRangingInApp != 0) {
                output.writeInt32(41, this.maxConcurrentPublishWithRangingInApp);
            }
            if (this.maxConcurrentSubscribeWithRangingInApp != 0) {
                output.writeInt32(42, this.maxConcurrentSubscribeWithRangingInApp);
            }
            if (this.maxConcurrentPublishWithRangingInSystem != 0) {
                output.writeInt32(43, this.maxConcurrentPublishWithRangingInSystem);
            }
            if (this.maxConcurrentSubscribeWithRangingInSystem != 0) {
                output.writeInt32(44, this.maxConcurrentSubscribeWithRangingInSystem);
            }
            if (this.histogramSubscribeGeofenceMin != null && this.histogramSubscribeGeofenceMin.length > 0) {
                for (HistogramBucket element14 : this.histogramSubscribeGeofenceMin) {
                    if (element14 != null) {
                        output.writeMessage(45, element14);
                    }
                }
            }
            if (this.histogramSubscribeGeofenceMax != null && this.histogramSubscribeGeofenceMax.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.histogramSubscribeGeofenceMax.length) {
                        break;
                    }
                    HistogramBucket element15 = this.histogramSubscribeGeofenceMax[i2];
                    if (element15 != null) {
                        output.writeMessage(46, element15);
                    }
                    i = i2 + 1;
                }
            }
            if (this.numSubscribesWithRanging != 0) {
                output.writeInt32(47, this.numSubscribesWithRanging);
            }
            if (this.numMatchesWithRanging != 0) {
                output.writeInt32(48, this.numMatchesWithRanging);
            }
            if (this.numMatchesWithoutRangingForRangingEnabledSubscribes != 0) {
                output.writeInt32(49, this.numMatchesWithoutRangingForRangingEnabledSubscribes);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numApps != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numApps);
            }
            if (this.numAppsUsingIdentityCallback != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.numAppsUsingIdentityCallback);
            }
            if (this.maxConcurrentAttachSessionsInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.maxConcurrentAttachSessionsInApp);
            }
            int i = 0;
            if (this.histogramAttachSessionStatus != null && this.histogramAttachSessionStatus.length > 0) {
                int size2 = size;
                for (NanStatusHistogramBucket element : this.histogramAttachSessionStatus) {
                    if (element != null) {
                        size2 += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                }
                size = size2;
            }
            if (this.maxConcurrentPublishInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, this.maxConcurrentPublishInApp);
            }
            if (this.maxConcurrentSubscribeInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, this.maxConcurrentSubscribeInApp);
            }
            if (this.maxConcurrentDiscoverySessionsInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, this.maxConcurrentDiscoverySessionsInApp);
            }
            if (this.maxConcurrentPublishInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, this.maxConcurrentPublishInSystem);
            }
            if (this.maxConcurrentSubscribeInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(9, this.maxConcurrentSubscribeInSystem);
            }
            if (this.maxConcurrentDiscoverySessionsInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(10, this.maxConcurrentDiscoverySessionsInSystem);
            }
            if (this.histogramPublishStatus != null && this.histogramPublishStatus.length > 0) {
                int size3 = size;
                for (NanStatusHistogramBucket element2 : this.histogramPublishStatus) {
                    if (element2 != null) {
                        size3 += CodedOutputByteBufferNano.computeMessageSize(11, element2);
                    }
                }
                size = size3;
            }
            if (this.histogramSubscribeStatus != null && this.histogramSubscribeStatus.length > 0) {
                int size4 = size;
                for (NanStatusHistogramBucket element3 : this.histogramSubscribeStatus) {
                    if (element3 != null) {
                        size4 += CodedOutputByteBufferNano.computeMessageSize(12, element3);
                    }
                }
                size = size4;
            }
            if (this.numAppsWithDiscoverySessionFailureOutOfResources != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(13, this.numAppsWithDiscoverySessionFailureOutOfResources);
            }
            if (this.histogramRequestNdpStatus != null && this.histogramRequestNdpStatus.length > 0) {
                int size5 = size;
                for (NanStatusHistogramBucket element4 : this.histogramRequestNdpStatus) {
                    if (element4 != null) {
                        size5 += CodedOutputByteBufferNano.computeMessageSize(14, element4);
                    }
                }
                size = size5;
            }
            if (this.histogramRequestNdpOobStatus != null && this.histogramRequestNdpOobStatus.length > 0) {
                int size6 = size;
                for (NanStatusHistogramBucket element5 : this.histogramRequestNdpOobStatus) {
                    if (element5 != null) {
                        size6 += CodedOutputByteBufferNano.computeMessageSize(15, element5);
                    }
                }
                size = size6;
            }
            if (this.maxConcurrentNdiInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(19, this.maxConcurrentNdiInApp);
            }
            if (this.maxConcurrentNdiInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(20, this.maxConcurrentNdiInSystem);
            }
            if (this.maxConcurrentNdpInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(21, this.maxConcurrentNdpInApp);
            }
            if (this.maxConcurrentNdpInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(22, this.maxConcurrentNdpInSystem);
            }
            if (this.maxConcurrentSecureNdpInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(23, this.maxConcurrentSecureNdpInApp);
            }
            if (this.maxConcurrentSecureNdpInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(24, this.maxConcurrentSecureNdpInSystem);
            }
            if (this.maxConcurrentNdpPerNdi != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(25, this.maxConcurrentNdpPerNdi);
            }
            if (this.histogramAwareAvailableDurationMs != null && this.histogramAwareAvailableDurationMs.length > 0) {
                int size7 = size;
                for (HistogramBucket element6 : this.histogramAwareAvailableDurationMs) {
                    if (element6 != null) {
                        size7 += CodedOutputByteBufferNano.computeMessageSize(26, element6);
                    }
                }
                size = size7;
            }
            if (this.histogramAwareEnabledDurationMs != null && this.histogramAwareEnabledDurationMs.length > 0) {
                int size8 = size;
                for (HistogramBucket element7 : this.histogramAwareEnabledDurationMs) {
                    if (element7 != null) {
                        size8 += CodedOutputByteBufferNano.computeMessageSize(27, element7);
                    }
                }
                size = size8;
            }
            if (this.histogramAttachDurationMs != null && this.histogramAttachDurationMs.length > 0) {
                int size9 = size;
                for (HistogramBucket element8 : this.histogramAttachDurationMs) {
                    if (element8 != null) {
                        size9 += CodedOutputByteBufferNano.computeMessageSize(28, element8);
                    }
                }
                size = size9;
            }
            if (this.histogramPublishSessionDurationMs != null && this.histogramPublishSessionDurationMs.length > 0) {
                int size10 = size;
                for (HistogramBucket element9 : this.histogramPublishSessionDurationMs) {
                    if (element9 != null) {
                        size10 += CodedOutputByteBufferNano.computeMessageSize(29, element9);
                    }
                }
                size = size10;
            }
            if (this.histogramSubscribeSessionDurationMs != null && this.histogramSubscribeSessionDurationMs.length > 0) {
                int size11 = size;
                for (HistogramBucket element10 : this.histogramSubscribeSessionDurationMs) {
                    if (element10 != null) {
                        size11 += CodedOutputByteBufferNano.computeMessageSize(30, element10);
                    }
                }
                size = size11;
            }
            if (this.histogramNdpSessionDurationMs != null && this.histogramNdpSessionDurationMs.length > 0) {
                int size12 = size;
                for (HistogramBucket element11 : this.histogramNdpSessionDurationMs) {
                    if (element11 != null) {
                        size12 += CodedOutputByteBufferNano.computeMessageSize(31, element11);
                    }
                }
                size = size12;
            }
            if (this.histogramNdpSessionDataUsageMb != null && this.histogramNdpSessionDataUsageMb.length > 0) {
                int size13 = size;
                for (HistogramBucket element12 : this.histogramNdpSessionDataUsageMb) {
                    if (element12 != null) {
                        size13 += CodedOutputByteBufferNano.computeMessageSize(32, element12);
                    }
                }
                size = size13;
            }
            if (this.histogramNdpCreationTimeMs != null && this.histogramNdpCreationTimeMs.length > 0) {
                int size14 = size;
                for (HistogramBucket element13 : this.histogramNdpCreationTimeMs) {
                    if (element13 != null) {
                        size14 += CodedOutputByteBufferNano.computeMessageSize(33, element13);
                    }
                }
                size = size14;
            }
            if (this.ndpCreationTimeMsMin != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(34, this.ndpCreationTimeMsMin);
            }
            if (this.ndpCreationTimeMsMax != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(35, this.ndpCreationTimeMsMax);
            }
            if (this.ndpCreationTimeMsSum != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(36, this.ndpCreationTimeMsSum);
            }
            if (this.ndpCreationTimeMsSumOfSq != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(37, this.ndpCreationTimeMsSumOfSq);
            }
            if (this.ndpCreationTimeMsNumSamples != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(38, this.ndpCreationTimeMsNumSamples);
            }
            if (this.availableTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(39, this.availableTimeMs);
            }
            if (this.enabledTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(40, this.enabledTimeMs);
            }
            if (this.maxConcurrentPublishWithRangingInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(41, this.maxConcurrentPublishWithRangingInApp);
            }
            if (this.maxConcurrentSubscribeWithRangingInApp != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(42, this.maxConcurrentSubscribeWithRangingInApp);
            }
            if (this.maxConcurrentPublishWithRangingInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(43, this.maxConcurrentPublishWithRangingInSystem);
            }
            if (this.maxConcurrentSubscribeWithRangingInSystem != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(44, this.maxConcurrentSubscribeWithRangingInSystem);
            }
            if (this.histogramSubscribeGeofenceMin != null && this.histogramSubscribeGeofenceMin.length > 0) {
                int size15 = size;
                for (HistogramBucket element14 : this.histogramSubscribeGeofenceMin) {
                    if (element14 != null) {
                        size15 += CodedOutputByteBufferNano.computeMessageSize(45, element14);
                    }
                }
                size = size15;
            }
            if (this.histogramSubscribeGeofenceMax != null && this.histogramSubscribeGeofenceMax.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.histogramSubscribeGeofenceMax.length) {
                        break;
                    }
                    HistogramBucket element15 = this.histogramSubscribeGeofenceMax[i2];
                    if (element15 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(46, element15);
                    }
                    i = i2 + 1;
                }
            }
            if (this.numSubscribesWithRanging != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(47, this.numSubscribesWithRanging);
            }
            if (this.numMatchesWithRanging != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(48, this.numMatchesWithRanging);
            }
            if (this.numMatchesWithoutRangingForRangingEnabledSubscribes != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(49, this.numMatchesWithoutRangingForRangingEnabledSubscribes);
            }
            return size;
        }

        public WifiAwareLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.numApps = input.readInt32();
                        break;
                    case 16:
                        this.numAppsUsingIdentityCallback = input.readInt32();
                        break;
                    case 24:
                        this.maxConcurrentAttachSessionsInApp = input.readInt32();
                        break;
                    case 34:
                        int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                        int i = this.histogramAttachSessionStatus == null ? 0 : this.histogramAttachSessionStatus.length;
                        NanStatusHistogramBucket[] newArray = new NanStatusHistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramAttachSessionStatus, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new NanStatusHistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new NanStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramAttachSessionStatus = newArray;
                        break;
                    case 40:
                        this.maxConcurrentPublishInApp = input.readInt32();
                        break;
                    case 48:
                        this.maxConcurrentSubscribeInApp = input.readInt32();
                        break;
                    case 56:
                        this.maxConcurrentDiscoverySessionsInApp = input.readInt32();
                        break;
                    case 64:
                        this.maxConcurrentPublishInSystem = input.readInt32();
                        break;
                    case 72:
                        this.maxConcurrentSubscribeInSystem = input.readInt32();
                        break;
                    case 80:
                        this.maxConcurrentDiscoverySessionsInSystem = input.readInt32();
                        break;
                    case 90:
                        int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 90);
                        int i2 = this.histogramPublishStatus == null ? 0 : this.histogramPublishStatus.length;
                        NanStatusHistogramBucket[] newArray2 = new NanStatusHistogramBucket[(i2 + arrayLength2)];
                        if (i2 != 0) {
                            System.arraycopy(this.histogramPublishStatus, 0, newArray2, 0, i2);
                        }
                        while (i2 < newArray2.length - 1) {
                            newArray2[i2] = new NanStatusHistogramBucket();
                            input.readMessage(newArray2[i2]);
                            input.readTag();
                            i2++;
                        }
                        newArray2[i2] = new NanStatusHistogramBucket();
                        input.readMessage(newArray2[i2]);
                        this.histogramPublishStatus = newArray2;
                        break;
                    case 98:
                        int arrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 98);
                        int i3 = this.histogramSubscribeStatus == null ? 0 : this.histogramSubscribeStatus.length;
                        NanStatusHistogramBucket[] newArray3 = new NanStatusHistogramBucket[(i3 + arrayLength3)];
                        if (i3 != 0) {
                            System.arraycopy(this.histogramSubscribeStatus, 0, newArray3, 0, i3);
                        }
                        while (i3 < newArray3.length - 1) {
                            newArray3[i3] = new NanStatusHistogramBucket();
                            input.readMessage(newArray3[i3]);
                            input.readTag();
                            i3++;
                        }
                        newArray3[i3] = new NanStatusHistogramBucket();
                        input.readMessage(newArray3[i3]);
                        this.histogramSubscribeStatus = newArray3;
                        break;
                    case 104:
                        this.numAppsWithDiscoverySessionFailureOutOfResources = input.readInt32();
                        break;
                    case 114:
                        int arrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 114);
                        int i4 = this.histogramRequestNdpStatus == null ? 0 : this.histogramRequestNdpStatus.length;
                        NanStatusHistogramBucket[] newArray4 = new NanStatusHistogramBucket[(i4 + arrayLength4)];
                        if (i4 != 0) {
                            System.arraycopy(this.histogramRequestNdpStatus, 0, newArray4, 0, i4);
                        }
                        while (i4 < newArray4.length - 1) {
                            newArray4[i4] = new NanStatusHistogramBucket();
                            input.readMessage(newArray4[i4]);
                            input.readTag();
                            i4++;
                        }
                        newArray4[i4] = new NanStatusHistogramBucket();
                        input.readMessage(newArray4[i4]);
                        this.histogramRequestNdpStatus = newArray4;
                        break;
                    case 122:
                        int arrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(input, 122);
                        int i5 = this.histogramRequestNdpOobStatus == null ? 0 : this.histogramRequestNdpOobStatus.length;
                        NanStatusHistogramBucket[] newArray5 = new NanStatusHistogramBucket[(i5 + arrayLength5)];
                        if (i5 != 0) {
                            System.arraycopy(this.histogramRequestNdpOobStatus, 0, newArray5, 0, i5);
                        }
                        while (i5 < newArray5.length - 1) {
                            newArray5[i5] = new NanStatusHistogramBucket();
                            input.readMessage(newArray5[i5]);
                            input.readTag();
                            i5++;
                        }
                        newArray5[i5] = new NanStatusHistogramBucket();
                        input.readMessage(newArray5[i5]);
                        this.histogramRequestNdpOobStatus = newArray5;
                        break;
                    case 152:
                        this.maxConcurrentNdiInApp = input.readInt32();
                        break;
                    case 160:
                        this.maxConcurrentNdiInSystem = input.readInt32();
                        break;
                    case 168:
                        this.maxConcurrentNdpInApp = input.readInt32();
                        break;
                    case 176:
                        this.maxConcurrentNdpInSystem = input.readInt32();
                        break;
                    case 184:
                        this.maxConcurrentSecureNdpInApp = input.readInt32();
                        break;
                    case 192:
                        this.maxConcurrentSecureNdpInSystem = input.readInt32();
                        break;
                    case 200:
                        this.maxConcurrentNdpPerNdi = input.readInt32();
                        break;
                    case 210:
                        int arrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(input, 210);
                        int i6 = this.histogramAwareAvailableDurationMs == null ? 0 : this.histogramAwareAvailableDurationMs.length;
                        HistogramBucket[] newArray6 = new HistogramBucket[(i6 + arrayLength6)];
                        if (i6 != 0) {
                            System.arraycopy(this.histogramAwareAvailableDurationMs, 0, newArray6, 0, i6);
                        }
                        while (i6 < newArray6.length - 1) {
                            newArray6[i6] = new HistogramBucket();
                            input.readMessage(newArray6[i6]);
                            input.readTag();
                            i6++;
                        }
                        newArray6[i6] = new HistogramBucket();
                        input.readMessage(newArray6[i6]);
                        this.histogramAwareAvailableDurationMs = newArray6;
                        break;
                    case 218:
                        int arrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(input, 218);
                        int i7 = this.histogramAwareEnabledDurationMs == null ? 0 : this.histogramAwareEnabledDurationMs.length;
                        HistogramBucket[] newArray7 = new HistogramBucket[(i7 + arrayLength7)];
                        if (i7 != 0) {
                            System.arraycopy(this.histogramAwareEnabledDurationMs, 0, newArray7, 0, i7);
                        }
                        while (i7 < newArray7.length - 1) {
                            newArray7[i7] = new HistogramBucket();
                            input.readMessage(newArray7[i7]);
                            input.readTag();
                            i7++;
                        }
                        newArray7[i7] = new HistogramBucket();
                        input.readMessage(newArray7[i7]);
                        this.histogramAwareEnabledDurationMs = newArray7;
                        break;
                    case 226:
                        int arrayLength8 = WireFormatNano.getRepeatedFieldArrayLength(input, 226);
                        int i8 = this.histogramAttachDurationMs == null ? 0 : this.histogramAttachDurationMs.length;
                        HistogramBucket[] newArray8 = new HistogramBucket[(i8 + arrayLength8)];
                        if (i8 != 0) {
                            System.arraycopy(this.histogramAttachDurationMs, 0, newArray8, 0, i8);
                        }
                        while (i8 < newArray8.length - 1) {
                            newArray8[i8] = new HistogramBucket();
                            input.readMessage(newArray8[i8]);
                            input.readTag();
                            i8++;
                        }
                        newArray8[i8] = new HistogramBucket();
                        input.readMessage(newArray8[i8]);
                        this.histogramAttachDurationMs = newArray8;
                        break;
                    case 234:
                        int arrayLength9 = WireFormatNano.getRepeatedFieldArrayLength(input, 234);
                        int i9 = this.histogramPublishSessionDurationMs == null ? 0 : this.histogramPublishSessionDurationMs.length;
                        HistogramBucket[] newArray9 = new HistogramBucket[(i9 + arrayLength9)];
                        if (i9 != 0) {
                            System.arraycopy(this.histogramPublishSessionDurationMs, 0, newArray9, 0, i9);
                        }
                        while (i9 < newArray9.length - 1) {
                            newArray9[i9] = new HistogramBucket();
                            input.readMessage(newArray9[i9]);
                            input.readTag();
                            i9++;
                        }
                        newArray9[i9] = new HistogramBucket();
                        input.readMessage(newArray9[i9]);
                        this.histogramPublishSessionDurationMs = newArray9;
                        break;
                    case 242:
                        int arrayLength10 = WireFormatNano.getRepeatedFieldArrayLength(input, 242);
                        int i10 = this.histogramSubscribeSessionDurationMs == null ? 0 : this.histogramSubscribeSessionDurationMs.length;
                        HistogramBucket[] newArray10 = new HistogramBucket[(i10 + arrayLength10)];
                        if (i10 != 0) {
                            System.arraycopy(this.histogramSubscribeSessionDurationMs, 0, newArray10, 0, i10);
                        }
                        while (i10 < newArray10.length - 1) {
                            newArray10[i10] = new HistogramBucket();
                            input.readMessage(newArray10[i10]);
                            input.readTag();
                            i10++;
                        }
                        newArray10[i10] = new HistogramBucket();
                        input.readMessage(newArray10[i10]);
                        this.histogramSubscribeSessionDurationMs = newArray10;
                        break;
                    case 250:
                        int arrayLength11 = WireFormatNano.getRepeatedFieldArrayLength(input, 250);
                        int i11 = this.histogramNdpSessionDurationMs == null ? 0 : this.histogramNdpSessionDurationMs.length;
                        HistogramBucket[] newArray11 = new HistogramBucket[(i11 + arrayLength11)];
                        if (i11 != 0) {
                            System.arraycopy(this.histogramNdpSessionDurationMs, 0, newArray11, 0, i11);
                        }
                        while (i11 < newArray11.length - 1) {
                            newArray11[i11] = new HistogramBucket();
                            input.readMessage(newArray11[i11]);
                            input.readTag();
                            i11++;
                        }
                        newArray11[i11] = new HistogramBucket();
                        input.readMessage(newArray11[i11]);
                        this.histogramNdpSessionDurationMs = newArray11;
                        break;
                    case 258:
                        int arrayLength12 = WireFormatNano.getRepeatedFieldArrayLength(input, 258);
                        int i12 = this.histogramNdpSessionDataUsageMb == null ? 0 : this.histogramNdpSessionDataUsageMb.length;
                        HistogramBucket[] newArray12 = new HistogramBucket[(i12 + arrayLength12)];
                        if (i12 != 0) {
                            System.arraycopy(this.histogramNdpSessionDataUsageMb, 0, newArray12, 0, i12);
                        }
                        while (i12 < newArray12.length - 1) {
                            newArray12[i12] = new HistogramBucket();
                            input.readMessage(newArray12[i12]);
                            input.readTag();
                            i12++;
                        }
                        newArray12[i12] = new HistogramBucket();
                        input.readMessage(newArray12[i12]);
                        this.histogramNdpSessionDataUsageMb = newArray12;
                        break;
                    case 266:
                        int arrayLength13 = WireFormatNano.getRepeatedFieldArrayLength(input, 266);
                        int i13 = this.histogramNdpCreationTimeMs == null ? 0 : this.histogramNdpCreationTimeMs.length;
                        HistogramBucket[] newArray13 = new HistogramBucket[(i13 + arrayLength13)];
                        if (i13 != 0) {
                            System.arraycopy(this.histogramNdpCreationTimeMs, 0, newArray13, 0, i13);
                        }
                        while (i13 < newArray13.length - 1) {
                            newArray13[i13] = new HistogramBucket();
                            input.readMessage(newArray13[i13]);
                            input.readTag();
                            i13++;
                        }
                        newArray13[i13] = new HistogramBucket();
                        input.readMessage(newArray13[i13]);
                        this.histogramNdpCreationTimeMs = newArray13;
                        break;
                    case 272:
                        this.ndpCreationTimeMsMin = input.readInt64();
                        break;
                    case 280:
                        this.ndpCreationTimeMsMax = input.readInt64();
                        break;
                    case 288:
                        this.ndpCreationTimeMsSum = input.readInt64();
                        break;
                    case 296:
                        this.ndpCreationTimeMsSumOfSq = input.readInt64();
                        break;
                    case 304:
                        this.ndpCreationTimeMsNumSamples = input.readInt64();
                        break;
                    case 312:
                        this.availableTimeMs = input.readInt64();
                        break;
                    case 320:
                        this.enabledTimeMs = input.readInt64();
                        break;
                    case 328:
                        this.maxConcurrentPublishWithRangingInApp = input.readInt32();
                        break;
                    case 336:
                        this.maxConcurrentSubscribeWithRangingInApp = input.readInt32();
                        break;
                    case 344:
                        this.maxConcurrentPublishWithRangingInSystem = input.readInt32();
                        break;
                    case 352:
                        this.maxConcurrentSubscribeWithRangingInSystem = input.readInt32();
                        break;
                    case 362:
                        int arrayLength14 = WireFormatNano.getRepeatedFieldArrayLength(input, 362);
                        int i14 = this.histogramSubscribeGeofenceMin == null ? 0 : this.histogramSubscribeGeofenceMin.length;
                        HistogramBucket[] newArray14 = new HistogramBucket[(i14 + arrayLength14)];
                        if (i14 != 0) {
                            System.arraycopy(this.histogramSubscribeGeofenceMin, 0, newArray14, 0, i14);
                        }
                        while (i14 < newArray14.length - 1) {
                            newArray14[i14] = new HistogramBucket();
                            input.readMessage(newArray14[i14]);
                            input.readTag();
                            i14++;
                        }
                        newArray14[i14] = new HistogramBucket();
                        input.readMessage(newArray14[i14]);
                        this.histogramSubscribeGeofenceMin = newArray14;
                        break;
                    case 370:
                        int arrayLength15 = WireFormatNano.getRepeatedFieldArrayLength(input, 370);
                        int i15 = this.histogramSubscribeGeofenceMax == null ? 0 : this.histogramSubscribeGeofenceMax.length;
                        HistogramBucket[] newArray15 = new HistogramBucket[(i15 + arrayLength15)];
                        if (i15 != 0) {
                            System.arraycopy(this.histogramSubscribeGeofenceMax, 0, newArray15, 0, i15);
                        }
                        while (i15 < newArray15.length - 1) {
                            newArray15[i15] = new HistogramBucket();
                            input.readMessage(newArray15[i15]);
                            input.readTag();
                            i15++;
                        }
                        newArray15[i15] = new HistogramBucket();
                        input.readMessage(newArray15[i15]);
                        this.histogramSubscribeGeofenceMax = newArray15;
                        break;
                    case 376:
                        this.numSubscribesWithRanging = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.ACTION_SHOW_SETTINGS_SUGGESTION:
                        this.numMatchesWithRanging = input.readInt32();
                        break;
                    case MetricsProto.MetricsEvent.TUNER_POWER_NOTIFICATION_CONTROLS:
                        this.numMatchesWithoutRangingForRangingEnabledSubscribes = input.readInt32();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static WifiAwareLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiAwareLog) MessageNano.mergeFrom(new WifiAwareLog(), data);
        }

        public static WifiAwareLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiAwareLog().mergeFrom(input);
        }
    }

    public static final class NumConnectableNetworksBucket extends MessageNano {
        private static volatile NumConnectableNetworksBucket[] _emptyArray;
        public int count;
        public int numConnectableNetworks;

        public static NumConnectableNetworksBucket[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NumConnectableNetworksBucket[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NumConnectableNetworksBucket() {
            clear();
        }

        public NumConnectableNetworksBucket clear() {
            this.numConnectableNetworks = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numConnectableNetworks != 0) {
                output.writeInt32(1, this.numConnectableNetworks);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numConnectableNetworks != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numConnectableNetworks);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            return size;
        }

        public NumConnectableNetworksBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numConnectableNetworks = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static NumConnectableNetworksBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (NumConnectableNetworksBucket) MessageNano.mergeFrom(new NumConnectableNetworksBucket(), data);
        }

        public static NumConnectableNetworksBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new NumConnectableNetworksBucket().mergeFrom(input);
        }
    }

    public static final class PnoScanMetrics extends MessageNano {
        private static volatile PnoScanMetrics[] _emptyArray;
        public int numPnoFoundNetworkEvents;
        public int numPnoScanAttempts;
        public int numPnoScanFailed;
        public int numPnoScanFailedOverOffload;
        public int numPnoScanStartedOverOffload;

        public static PnoScanMetrics[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PnoScanMetrics[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PnoScanMetrics() {
            clear();
        }

        public PnoScanMetrics clear() {
            this.numPnoScanAttempts = 0;
            this.numPnoScanFailed = 0;
            this.numPnoScanStartedOverOffload = 0;
            this.numPnoScanFailedOverOffload = 0;
            this.numPnoFoundNetworkEvents = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numPnoScanAttempts != 0) {
                output.writeInt32(1, this.numPnoScanAttempts);
            }
            if (this.numPnoScanFailed != 0) {
                output.writeInt32(2, this.numPnoScanFailed);
            }
            if (this.numPnoScanStartedOverOffload != 0) {
                output.writeInt32(3, this.numPnoScanStartedOverOffload);
            }
            if (this.numPnoScanFailedOverOffload != 0) {
                output.writeInt32(4, this.numPnoScanFailedOverOffload);
            }
            if (this.numPnoFoundNetworkEvents != 0) {
                output.writeInt32(5, this.numPnoFoundNetworkEvents);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numPnoScanAttempts != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numPnoScanAttempts);
            }
            if (this.numPnoScanFailed != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.numPnoScanFailed);
            }
            if (this.numPnoScanStartedOverOffload != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numPnoScanStartedOverOffload);
            }
            if (this.numPnoScanFailedOverOffload != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.numPnoScanFailedOverOffload);
            }
            if (this.numPnoFoundNetworkEvents != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(5, this.numPnoFoundNetworkEvents);
            }
            return size;
        }

        public PnoScanMetrics mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numPnoScanAttempts = input.readInt32();
                } else if (tag == 16) {
                    this.numPnoScanFailed = input.readInt32();
                } else if (tag == 24) {
                    this.numPnoScanStartedOverOffload = input.readInt32();
                } else if (tag == 32) {
                    this.numPnoScanFailedOverOffload = input.readInt32();
                } else if (tag == 40) {
                    this.numPnoFoundNetworkEvents = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static PnoScanMetrics parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (PnoScanMetrics) MessageNano.mergeFrom(new PnoScanMetrics(), data);
        }

        public static PnoScanMetrics parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new PnoScanMetrics().mergeFrom(input);
        }
    }

    public static final class ConnectToNetworkNotificationAndActionCount extends MessageNano {
        public static final int ACTION_CONNECT_TO_NETWORK = 2;
        public static final int ACTION_PICK_WIFI_NETWORK = 3;
        public static final int ACTION_PICK_WIFI_NETWORK_AFTER_CONNECT_FAILURE = 4;
        public static final int ACTION_UNKNOWN = 0;
        public static final int ACTION_USER_DISMISSED_NOTIFICATION = 1;
        public static final int NOTIFICATION_CONNECTED_TO_NETWORK = 3;
        public static final int NOTIFICATION_CONNECTING_TO_NETWORK = 2;
        public static final int NOTIFICATION_FAILED_TO_CONNECT = 4;
        public static final int NOTIFICATION_RECOMMEND_NETWORK = 1;
        public static final int NOTIFICATION_UNKNOWN = 0;
        public static final int RECOMMENDER_OPEN = 1;
        public static final int RECOMMENDER_UNKNOWN = 0;
        private static volatile ConnectToNetworkNotificationAndActionCount[] _emptyArray;
        public int action;
        public int count;
        public int notification;
        public int recommender;

        public static ConnectToNetworkNotificationAndActionCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ConnectToNetworkNotificationAndActionCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ConnectToNetworkNotificationAndActionCount() {
            clear();
        }

        public ConnectToNetworkNotificationAndActionCount clear() {
            this.notification = 0;
            this.action = 0;
            this.recommender = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.notification != 0) {
                output.writeInt32(1, this.notification);
            }
            if (this.action != 0) {
                output.writeInt32(2, this.action);
            }
            if (this.recommender != 0) {
                output.writeInt32(3, this.recommender);
            }
            if (this.count != 0) {
                output.writeInt32(4, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.notification != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.notification);
            }
            if (this.action != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.action);
            }
            if (this.recommender != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.recommender);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, this.count);
            }
            return size;
        }

        public ConnectToNetworkNotificationAndActionCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag != 0) {
                    if (tag != 8) {
                        if (tag != 16) {
                            if (tag == 24) {
                                int value = input.readInt32();
                                switch (value) {
                                    case 0:
                                    case 1:
                                        this.recommender = value;
                                        break;
                                }
                            } else if (tag == 32) {
                                this.count = input.readInt32();
                            } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                                return this;
                            }
                        } else {
                            int value2 = input.readInt32();
                            switch (value2) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    this.action = value2;
                                    break;
                            }
                        }
                    } else {
                        int value3 = input.readInt32();
                        switch (value3) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                this.notification = value3;
                                break;
                        }
                    }
                } else {
                    return this;
                }
            }
        }

        public static ConnectToNetworkNotificationAndActionCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ConnectToNetworkNotificationAndActionCount) MessageNano.mergeFrom(new ConnectToNetworkNotificationAndActionCount(), data);
        }

        public static ConnectToNetworkNotificationAndActionCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ConnectToNetworkNotificationAndActionCount().mergeFrom(input);
        }
    }

    public static final class SoftApConnectedClientsEvent extends MessageNano {
        public static final int BANDWIDTH_160 = 6;
        public static final int BANDWIDTH_20 = 2;
        public static final int BANDWIDTH_20_NOHT = 1;
        public static final int BANDWIDTH_40 = 3;
        public static final int BANDWIDTH_80 = 4;
        public static final int BANDWIDTH_80P80 = 5;
        public static final int BANDWIDTH_INVALID = 0;
        public static final int NUM_CLIENTS_CHANGED = 2;
        public static final int SOFT_AP_DOWN = 1;
        public static final int SOFT_AP_UP = 0;
        private static volatile SoftApConnectedClientsEvent[] _emptyArray;
        public int channelBandwidth;
        public int channelFrequency;
        public int eventType;
        public int numConnectedClients;
        public long timeStampMillis;

        public static SoftApConnectedClientsEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new SoftApConnectedClientsEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public SoftApConnectedClientsEvent() {
            clear();
        }

        public SoftApConnectedClientsEvent clear() {
            this.eventType = 0;
            this.timeStampMillis = 0;
            this.numConnectedClients = 0;
            this.channelFrequency = 0;
            this.channelBandwidth = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.eventType != 0) {
                output.writeInt32(1, this.eventType);
            }
            if (this.timeStampMillis != 0) {
                output.writeInt64(2, this.timeStampMillis);
            }
            if (this.numConnectedClients != 0) {
                output.writeInt32(3, this.numConnectedClients);
            }
            if (this.channelFrequency != 0) {
                output.writeInt32(4, this.channelFrequency);
            }
            if (this.channelBandwidth != 0) {
                output.writeInt32(5, this.channelBandwidth);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.eventType != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.eventType);
            }
            if (this.timeStampMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, this.timeStampMillis);
            }
            if (this.numConnectedClients != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numConnectedClients);
            }
            if (this.channelFrequency != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.channelFrequency);
            }
            if (this.channelBandwidth != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(5, this.channelBandwidth);
            }
            return size;
        }

        public SoftApConnectedClientsEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                                this.eventType = value;
                                break;
                        }
                    } else if (tag == 16) {
                        this.timeStampMillis = input.readInt64();
                    } else if (tag == 24) {
                        this.numConnectedClients = input.readInt32();
                    } else if (tag != 32) {
                        if (tag == 40) {
                            int value2 = input.readInt32();
                            switch (value2) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                    this.channelBandwidth = value2;
                                    break;
                            }
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        this.channelFrequency = input.readInt32();
                    }
                } else {
                    return this;
                }
            }
        }

        public static SoftApConnectedClientsEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (SoftApConnectedClientsEvent) MessageNano.mergeFrom(new SoftApConnectedClientsEvent(), data);
        }

        public static SoftApConnectedClientsEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new SoftApConnectedClientsEvent().mergeFrom(input);
        }
    }

    public static final class WpsMetrics extends MessageNano {
        private static volatile WpsMetrics[] _emptyArray;
        public int numWpsAttempts;
        public int numWpsCancellation;
        public int numWpsOtherConnectionFailure;
        public int numWpsOverlapFailure;
        public int numWpsStartFailure;
        public int numWpsSuccess;
        public int numWpsSupplicantFailure;
        public int numWpsTimeoutFailure;

        public static WpsMetrics[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WpsMetrics[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WpsMetrics() {
            clear();
        }

        public WpsMetrics clear() {
            this.numWpsAttempts = 0;
            this.numWpsSuccess = 0;
            this.numWpsStartFailure = 0;
            this.numWpsOverlapFailure = 0;
            this.numWpsTimeoutFailure = 0;
            this.numWpsOtherConnectionFailure = 0;
            this.numWpsSupplicantFailure = 0;
            this.numWpsCancellation = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numWpsAttempts != 0) {
                output.writeInt32(1, this.numWpsAttempts);
            }
            if (this.numWpsSuccess != 0) {
                output.writeInt32(2, this.numWpsSuccess);
            }
            if (this.numWpsStartFailure != 0) {
                output.writeInt32(3, this.numWpsStartFailure);
            }
            if (this.numWpsOverlapFailure != 0) {
                output.writeInt32(4, this.numWpsOverlapFailure);
            }
            if (this.numWpsTimeoutFailure != 0) {
                output.writeInt32(5, this.numWpsTimeoutFailure);
            }
            if (this.numWpsOtherConnectionFailure != 0) {
                output.writeInt32(6, this.numWpsOtherConnectionFailure);
            }
            if (this.numWpsSupplicantFailure != 0) {
                output.writeInt32(7, this.numWpsSupplicantFailure);
            }
            if (this.numWpsCancellation != 0) {
                output.writeInt32(8, this.numWpsCancellation);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numWpsAttempts != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numWpsAttempts);
            }
            if (this.numWpsSuccess != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.numWpsSuccess);
            }
            if (this.numWpsStartFailure != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numWpsStartFailure);
            }
            if (this.numWpsOverlapFailure != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.numWpsOverlapFailure);
            }
            if (this.numWpsTimeoutFailure != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, this.numWpsTimeoutFailure);
            }
            if (this.numWpsOtherConnectionFailure != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, this.numWpsOtherConnectionFailure);
            }
            if (this.numWpsSupplicantFailure != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, this.numWpsSupplicantFailure);
            }
            if (this.numWpsCancellation != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(8, this.numWpsCancellation);
            }
            return size;
        }

        public WpsMetrics mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numWpsAttempts = input.readInt32();
                } else if (tag == 16) {
                    this.numWpsSuccess = input.readInt32();
                } else if (tag == 24) {
                    this.numWpsStartFailure = input.readInt32();
                } else if (tag == 32) {
                    this.numWpsOverlapFailure = input.readInt32();
                } else if (tag == 40) {
                    this.numWpsTimeoutFailure = input.readInt32();
                } else if (tag == 48) {
                    this.numWpsOtherConnectionFailure = input.readInt32();
                } else if (tag == 56) {
                    this.numWpsSupplicantFailure = input.readInt32();
                } else if (tag == 64) {
                    this.numWpsCancellation = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WpsMetrics parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WpsMetrics) MessageNano.mergeFrom(new WpsMetrics(), data);
        }

        public static WpsMetrics parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WpsMetrics().mergeFrom(input);
        }
    }

    public static final class WifiPowerStats extends MessageNano {
        private static volatile WifiPowerStats[] _emptyArray;
        public double energyConsumedMah;
        public long idleTimeMs;
        public long loggingDurationMs;
        public double monitoredRailEnergyConsumedMah;
        public long numBytesRx;
        public long numBytesTx;
        public long numPacketsRx;
        public long numPacketsTx;
        public long rxTimeMs;
        public long scanTimeMs;
        public long sleepTimeMs;
        public long txTimeMs;
        public long wifiKernelActiveTimeMs;

        public static WifiPowerStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiPowerStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiPowerStats() {
            clear();
        }

        public WifiPowerStats clear() {
            this.loggingDurationMs = 0;
            this.energyConsumedMah = 0.0d;
            this.idleTimeMs = 0;
            this.rxTimeMs = 0;
            this.txTimeMs = 0;
            this.wifiKernelActiveTimeMs = 0;
            this.numPacketsTx = 0;
            this.numBytesTx = 0;
            this.numPacketsRx = 0;
            this.numBytesRx = 0;
            this.sleepTimeMs = 0;
            this.scanTimeMs = 0;
            this.monitoredRailEnergyConsumedMah = 0.0d;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.loggingDurationMs != 0) {
                output.writeInt64(1, this.loggingDurationMs);
            }
            if (Double.doubleToLongBits(this.energyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                output.writeDouble(2, this.energyConsumedMah);
            }
            if (this.idleTimeMs != 0) {
                output.writeInt64(3, this.idleTimeMs);
            }
            if (this.rxTimeMs != 0) {
                output.writeInt64(4, this.rxTimeMs);
            }
            if (this.txTimeMs != 0) {
                output.writeInt64(5, this.txTimeMs);
            }
            if (this.wifiKernelActiveTimeMs != 0) {
                output.writeInt64(6, this.wifiKernelActiveTimeMs);
            }
            if (this.numPacketsTx != 0) {
                output.writeInt64(7, this.numPacketsTx);
            }
            if (this.numBytesTx != 0) {
                output.writeInt64(8, this.numBytesTx);
            }
            if (this.numPacketsRx != 0) {
                output.writeInt64(9, this.numPacketsRx);
            }
            if (this.numBytesRx != 0) {
                output.writeInt64(10, this.numBytesRx);
            }
            if (this.sleepTimeMs != 0) {
                output.writeInt64(11, this.sleepTimeMs);
            }
            if (this.scanTimeMs != 0) {
                output.writeInt64(12, this.scanTimeMs);
            }
            if (Double.doubleToLongBits(this.monitoredRailEnergyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                output.writeDouble(13, this.monitoredRailEnergyConsumedMah);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.loggingDurationMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, this.loggingDurationMs);
            }
            if (Double.doubleToLongBits(this.energyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                size += CodedOutputByteBufferNano.computeDoubleSize(2, this.energyConsumedMah);
            }
            if (this.idleTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, this.idleTimeMs);
            }
            if (this.rxTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, this.rxTimeMs);
            }
            if (this.txTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, this.txTimeMs);
            }
            if (this.wifiKernelActiveTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, this.wifiKernelActiveTimeMs);
            }
            if (this.numPacketsTx != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, this.numPacketsTx);
            }
            if (this.numBytesTx != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, this.numBytesTx);
            }
            if (this.numPacketsRx != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, this.numPacketsRx);
            }
            if (this.numBytesRx != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(10, this.numBytesRx);
            }
            if (this.sleepTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(11, this.sleepTimeMs);
            }
            if (this.scanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(12, this.scanTimeMs);
            }
            if (Double.doubleToLongBits(this.monitoredRailEnergyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                return size + CodedOutputByteBufferNano.computeDoubleSize(13, this.monitoredRailEnergyConsumedMah);
            }
            return size;
        }

        public WifiPowerStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.loggingDurationMs = input.readInt64();
                        break;
                    case 17:
                        this.energyConsumedMah = input.readDouble();
                        break;
                    case 24:
                        this.idleTimeMs = input.readInt64();
                        break;
                    case 32:
                        this.rxTimeMs = input.readInt64();
                        break;
                    case 40:
                        this.txTimeMs = input.readInt64();
                        break;
                    case 48:
                        this.wifiKernelActiveTimeMs = input.readInt64();
                        break;
                    case 56:
                        this.numPacketsTx = input.readInt64();
                        break;
                    case 64:
                        this.numBytesTx = input.readInt64();
                        break;
                    case 72:
                        this.numPacketsRx = input.readInt64();
                        break;
                    case 80:
                        this.numBytesRx = input.readInt64();
                        break;
                    case 88:
                        this.sleepTimeMs = input.readInt64();
                        break;
                    case 96:
                        this.scanTimeMs = input.readInt64();
                        break;
                    case 105:
                        this.monitoredRailEnergyConsumedMah = input.readDouble();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static WifiPowerStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiPowerStats) MessageNano.mergeFrom(new WifiPowerStats(), data);
        }

        public static WifiPowerStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiPowerStats().mergeFrom(input);
        }
    }

    public static final class WifiWakeStats extends MessageNano {
        private static volatile WifiWakeStats[] _emptyArray;
        public int numIgnoredStarts;
        public int numSessions;
        public int numWakeups;
        public Session[] sessions;

        public static final class Session extends MessageNano {
            private static volatile Session[] _emptyArray;
            public Event initializeEvent;
            public int lockedNetworksAtInitialize;
            public int lockedNetworksAtStart;
            public Event resetEvent;
            public long startTimeMillis;
            public Event unlockEvent;
            public Event wakeupEvent;

            public static final class Event extends MessageNano {
                private static volatile Event[] _emptyArray;
                public int elapsedScans;
                public long elapsedTimeMillis;

                public static Event[] emptyArray() {
                    if (_emptyArray == null) {
                        synchronized (InternalNano.LAZY_INIT_LOCK) {
                            if (_emptyArray == null) {
                                _emptyArray = new Event[0];
                            }
                        }
                    }
                    return _emptyArray;
                }

                public Event() {
                    clear();
                }

                public Event clear() {
                    this.elapsedTimeMillis = 0;
                    this.elapsedScans = 0;
                    this.cachedSize = -1;
                    return this;
                }

                public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                    if (this.elapsedTimeMillis != 0) {
                        output.writeInt64(1, this.elapsedTimeMillis);
                    }
                    if (this.elapsedScans != 0) {
                        output.writeInt32(2, this.elapsedScans);
                    }
                    super.writeTo(output);
                }

                /* access modifiers changed from: protected */
                public int computeSerializedSize() {
                    int size = super.computeSerializedSize();
                    if (this.elapsedTimeMillis != 0) {
                        size += CodedOutputByteBufferNano.computeInt64Size(1, this.elapsedTimeMillis);
                    }
                    if (this.elapsedScans != 0) {
                        return size + CodedOutputByteBufferNano.computeInt32Size(2, this.elapsedScans);
                    }
                    return size;
                }

                public Event mergeFrom(CodedInputByteBufferNano input) throws IOException {
                    while (true) {
                        int tag = input.readTag();
                        if (tag == 0) {
                            return this;
                        }
                        if (tag == 8) {
                            this.elapsedTimeMillis = input.readInt64();
                        } else if (tag == 16) {
                            this.elapsedScans = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    }
                }

                public static Event parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                    return (Event) MessageNano.mergeFrom(new Event(), data);
                }

                public static Event parseFrom(CodedInputByteBufferNano input) throws IOException {
                    return new Event().mergeFrom(input);
                }
            }

            public static Session[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new Session[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public Session() {
                clear();
            }

            public Session clear() {
                this.startTimeMillis = 0;
                this.lockedNetworksAtStart = 0;
                this.lockedNetworksAtInitialize = 0;
                this.initializeEvent = null;
                this.unlockEvent = null;
                this.wakeupEvent = null;
                this.resetEvent = null;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.startTimeMillis != 0) {
                    output.writeInt64(1, this.startTimeMillis);
                }
                if (this.lockedNetworksAtStart != 0) {
                    output.writeInt32(2, this.lockedNetworksAtStart);
                }
                if (this.unlockEvent != null) {
                    output.writeMessage(3, this.unlockEvent);
                }
                if (this.wakeupEvent != null) {
                    output.writeMessage(4, this.wakeupEvent);
                }
                if (this.resetEvent != null) {
                    output.writeMessage(5, this.resetEvent);
                }
                if (this.lockedNetworksAtInitialize != 0) {
                    output.writeInt32(6, this.lockedNetworksAtInitialize);
                }
                if (this.initializeEvent != null) {
                    output.writeMessage(7, this.initializeEvent);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.startTimeMillis != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(1, this.startTimeMillis);
                }
                if (this.lockedNetworksAtStart != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, this.lockedNetworksAtStart);
                }
                if (this.unlockEvent != null) {
                    size += CodedOutputByteBufferNano.computeMessageSize(3, this.unlockEvent);
                }
                if (this.wakeupEvent != null) {
                    size += CodedOutputByteBufferNano.computeMessageSize(4, this.wakeupEvent);
                }
                if (this.resetEvent != null) {
                    size += CodedOutputByteBufferNano.computeMessageSize(5, this.resetEvent);
                }
                if (this.lockedNetworksAtInitialize != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(6, this.lockedNetworksAtInitialize);
                }
                if (this.initializeEvent != null) {
                    return size + CodedOutputByteBufferNano.computeMessageSize(7, this.initializeEvent);
                }
                return size;
            }

            public Session mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.startTimeMillis = input.readInt64();
                    } else if (tag == 16) {
                        this.lockedNetworksAtStart = input.readInt32();
                    } else if (tag == 26) {
                        if (this.unlockEvent == null) {
                            this.unlockEvent = new Event();
                        }
                        input.readMessage(this.unlockEvent);
                    } else if (tag == 34) {
                        if (this.wakeupEvent == null) {
                            this.wakeupEvent = new Event();
                        }
                        input.readMessage(this.wakeupEvent);
                    } else if (tag == 42) {
                        if (this.resetEvent == null) {
                            this.resetEvent = new Event();
                        }
                        input.readMessage(this.resetEvent);
                    } else if (tag == 48) {
                        this.lockedNetworksAtInitialize = input.readInt32();
                    } else if (tag == 58) {
                        if (this.initializeEvent == null) {
                            this.initializeEvent = new Event();
                        }
                        input.readMessage(this.initializeEvent);
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static Session parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (Session) MessageNano.mergeFrom(new Session(), data);
            }

            public static Session parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new Session().mergeFrom(input);
            }
        }

        public static WifiWakeStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiWakeStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiWakeStats() {
            clear();
        }

        public WifiWakeStats clear() {
            this.numSessions = 0;
            this.sessions = Session.emptyArray();
            this.numIgnoredStarts = 0;
            this.numWakeups = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numSessions != 0) {
                output.writeInt32(1, this.numSessions);
            }
            if (this.sessions != null && this.sessions.length > 0) {
                for (Session element : this.sessions) {
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                }
            }
            if (this.numIgnoredStarts != 0) {
                output.writeInt32(3, this.numIgnoredStarts);
            }
            if (this.numWakeups != 0) {
                output.writeInt32(4, this.numWakeups);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numSessions != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numSessions);
            }
            if (this.sessions != null && this.sessions.length > 0) {
                for (Session element : this.sessions) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                }
            }
            if (this.numIgnoredStarts != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numIgnoredStarts);
            }
            if (this.numWakeups != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, this.numWakeups);
            }
            return size;
        }

        public WifiWakeStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numSessions = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int i = this.sessions == null ? 0 : this.sessions.length;
                    Session[] newArray = new Session[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.sessions, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Session();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Session();
                    input.readMessage(newArray[i]);
                    this.sessions = newArray;
                } else if (tag == 24) {
                    this.numIgnoredStarts = input.readInt32();
                } else if (tag == 32) {
                    this.numWakeups = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiWakeStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiWakeStats) MessageNano.mergeFrom(new WifiWakeStats(), data);
        }

        public static WifiWakeStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiWakeStats().mergeFrom(input);
        }
    }

    public static final class WifiRttLog extends MessageNano {
        public static final int ABORTED = 9;
        public static final int FAILURE = 2;
        public static final int FAIL_AP_ON_DIFF_CHANNEL = 7;
        public static final int FAIL_BUSY_TRY_LATER = 13;
        public static final int FAIL_FTM_PARAM_OVERRIDE = 16;
        public static final int FAIL_INVALID_TS = 10;
        public static final int FAIL_NOT_SCHEDULED_YET = 5;
        public static final int FAIL_NO_CAPABILITY = 8;
        public static final int FAIL_NO_RSP = 3;
        public static final int FAIL_PROTOCOL = 11;
        public static final int FAIL_REJECTED = 4;
        public static final int FAIL_SCHEDULE = 12;
        public static final int FAIL_TM_TIMEOUT = 6;
        public static final int INVALID_REQ = 14;
        public static final int MISSING_RESULT = 17;
        public static final int NO_WIFI = 15;
        public static final int OVERALL_AWARE_TRANSLATION_FAILURE = 7;
        public static final int OVERALL_FAIL = 2;
        public static final int OVERALL_HAL_FAILURE = 6;
        public static final int OVERALL_LOCATION_PERMISSION_MISSING = 8;
        public static final int OVERALL_RTT_NOT_AVAILABLE = 3;
        public static final int OVERALL_SUCCESS = 1;
        public static final int OVERALL_THROTTLE = 5;
        public static final int OVERALL_TIMEOUT = 4;
        public static final int OVERALL_UNKNOWN = 0;
        public static final int SUCCESS = 1;
        public static final int UNKNOWN = 0;
        private static volatile WifiRttLog[] _emptyArray;
        public RttOverallStatusHistogramBucket[] histogramOverallStatus;
        public int numRequests;
        public RttToPeerLog rttToAp;
        public RttToPeerLog rttToAware;

        public static final class RttToPeerLog extends MessageNano {
            private static volatile RttToPeerLog[] _emptyArray;
            public HistogramBucket[] histogramDistance;
            public RttIndividualStatusHistogramBucket[] histogramIndividualStatus;
            public HistogramBucket[] histogramNumPeersPerRequest;
            public HistogramBucket[] histogramNumRequestsPerApp;
            public HistogramBucket[] histogramRequestIntervalMs;
            public int numApps;
            public int numIndividualRequests;
            public int numRequests;

            public static RttToPeerLog[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new RttToPeerLog[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public RttToPeerLog() {
                clear();
            }

            public RttToPeerLog clear() {
                this.numRequests = 0;
                this.numIndividualRequests = 0;
                this.numApps = 0;
                this.histogramNumRequestsPerApp = HistogramBucket.emptyArray();
                this.histogramNumPeersPerRequest = HistogramBucket.emptyArray();
                this.histogramIndividualStatus = RttIndividualStatusHistogramBucket.emptyArray();
                this.histogramDistance = HistogramBucket.emptyArray();
                this.histogramRequestIntervalMs = HistogramBucket.emptyArray();
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.numRequests != 0) {
                    output.writeInt32(1, this.numRequests);
                }
                if (this.numIndividualRequests != 0) {
                    output.writeInt32(2, this.numIndividualRequests);
                }
                if (this.numApps != 0) {
                    output.writeInt32(3, this.numApps);
                }
                int i = 0;
                if (this.histogramNumRequestsPerApp != null && this.histogramNumRequestsPerApp.length > 0) {
                    for (HistogramBucket element : this.histogramNumRequestsPerApp) {
                        if (element != null) {
                            output.writeMessage(4, element);
                        }
                    }
                }
                if (this.histogramNumPeersPerRequest != null && this.histogramNumPeersPerRequest.length > 0) {
                    for (HistogramBucket element2 : this.histogramNumPeersPerRequest) {
                        if (element2 != null) {
                            output.writeMessage(5, element2);
                        }
                    }
                }
                if (this.histogramIndividualStatus != null && this.histogramIndividualStatus.length > 0) {
                    for (RttIndividualStatusHistogramBucket element3 : this.histogramIndividualStatus) {
                        if (element3 != null) {
                            output.writeMessage(6, element3);
                        }
                    }
                }
                if (this.histogramDistance != null && this.histogramDistance.length > 0) {
                    for (HistogramBucket element4 : this.histogramDistance) {
                        if (element4 != null) {
                            output.writeMessage(7, element4);
                        }
                    }
                }
                if (this.histogramRequestIntervalMs != null && this.histogramRequestIntervalMs.length > 0) {
                    while (true) {
                        int i2 = i;
                        if (i2 >= this.histogramRequestIntervalMs.length) {
                            break;
                        }
                        HistogramBucket element5 = this.histogramRequestIntervalMs[i2];
                        if (element5 != null) {
                            output.writeMessage(8, element5);
                        }
                        i = i2 + 1;
                    }
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.numRequests != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.numRequests);
                }
                if (this.numIndividualRequests != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, this.numIndividualRequests);
                }
                if (this.numApps != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(3, this.numApps);
                }
                int i = 0;
                if (this.histogramNumRequestsPerApp != null && this.histogramNumRequestsPerApp.length > 0) {
                    int size2 = size;
                    for (HistogramBucket element : this.histogramNumRequestsPerApp) {
                        if (element != null) {
                            size2 += CodedOutputByteBufferNano.computeMessageSize(4, element);
                        }
                    }
                    size = size2;
                }
                if (this.histogramNumPeersPerRequest != null && this.histogramNumPeersPerRequest.length > 0) {
                    int size3 = size;
                    for (HistogramBucket element2 : this.histogramNumPeersPerRequest) {
                        if (element2 != null) {
                            size3 += CodedOutputByteBufferNano.computeMessageSize(5, element2);
                        }
                    }
                    size = size3;
                }
                if (this.histogramIndividualStatus != null && this.histogramIndividualStatus.length > 0) {
                    int size4 = size;
                    for (RttIndividualStatusHistogramBucket element3 : this.histogramIndividualStatus) {
                        if (element3 != null) {
                            size4 += CodedOutputByteBufferNano.computeMessageSize(6, element3);
                        }
                    }
                    size = size4;
                }
                if (this.histogramDistance != null && this.histogramDistance.length > 0) {
                    int size5 = size;
                    for (HistogramBucket element4 : this.histogramDistance) {
                        if (element4 != null) {
                            size5 += CodedOutputByteBufferNano.computeMessageSize(7, element4);
                        }
                    }
                    size = size5;
                }
                if (this.histogramRequestIntervalMs != null && this.histogramRequestIntervalMs.length > 0) {
                    while (true) {
                        int i2 = i;
                        if (i2 >= this.histogramRequestIntervalMs.length) {
                            break;
                        }
                        HistogramBucket element5 = this.histogramRequestIntervalMs[i2];
                        if (element5 != null) {
                            size += CodedOutputByteBufferNano.computeMessageSize(8, element5);
                        }
                        i = i2 + 1;
                    }
                }
                return size;
            }

            public RttToPeerLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.numRequests = input.readInt32();
                    } else if (tag == 16) {
                        this.numIndividualRequests = input.readInt32();
                    } else if (tag == 24) {
                        this.numApps = input.readInt32();
                    } else if (tag == 34) {
                        int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                        int i = this.histogramNumRequestsPerApp == null ? 0 : this.histogramNumRequestsPerApp.length;
                        HistogramBucket[] newArray = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramNumRequestsPerApp, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new HistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new HistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramNumRequestsPerApp = newArray;
                    } else if (tag == 42) {
                        int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                        int i2 = this.histogramNumPeersPerRequest == null ? 0 : this.histogramNumPeersPerRequest.length;
                        HistogramBucket[] newArray2 = new HistogramBucket[(i2 + arrayLength2)];
                        if (i2 != 0) {
                            System.arraycopy(this.histogramNumPeersPerRequest, 0, newArray2, 0, i2);
                        }
                        while (i2 < newArray2.length - 1) {
                            newArray2[i2] = new HistogramBucket();
                            input.readMessage(newArray2[i2]);
                            input.readTag();
                            i2++;
                        }
                        newArray2[i2] = new HistogramBucket();
                        input.readMessage(newArray2[i2]);
                        this.histogramNumPeersPerRequest = newArray2;
                    } else if (tag == 50) {
                        int arrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                        int i3 = this.histogramIndividualStatus == null ? 0 : this.histogramIndividualStatus.length;
                        RttIndividualStatusHistogramBucket[] newArray3 = new RttIndividualStatusHistogramBucket[(i3 + arrayLength3)];
                        if (i3 != 0) {
                            System.arraycopy(this.histogramIndividualStatus, 0, newArray3, 0, i3);
                        }
                        while (i3 < newArray3.length - 1) {
                            newArray3[i3] = new RttIndividualStatusHistogramBucket();
                            input.readMessage(newArray3[i3]);
                            input.readTag();
                            i3++;
                        }
                        newArray3[i3] = new RttIndividualStatusHistogramBucket();
                        input.readMessage(newArray3[i3]);
                        this.histogramIndividualStatus = newArray3;
                    } else if (tag == 58) {
                        int arrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                        int i4 = this.histogramDistance == null ? 0 : this.histogramDistance.length;
                        HistogramBucket[] newArray4 = new HistogramBucket[(i4 + arrayLength4)];
                        if (i4 != 0) {
                            System.arraycopy(this.histogramDistance, 0, newArray4, 0, i4);
                        }
                        while (i4 < newArray4.length - 1) {
                            newArray4[i4] = new HistogramBucket();
                            input.readMessage(newArray4[i4]);
                            input.readTag();
                            i4++;
                        }
                        newArray4[i4] = new HistogramBucket();
                        input.readMessage(newArray4[i4]);
                        this.histogramDistance = newArray4;
                    } else if (tag == 66) {
                        int arrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(input, 66);
                        int i5 = this.histogramRequestIntervalMs == null ? 0 : this.histogramRequestIntervalMs.length;
                        HistogramBucket[] newArray5 = new HistogramBucket[(i5 + arrayLength5)];
                        if (i5 != 0) {
                            System.arraycopy(this.histogramRequestIntervalMs, 0, newArray5, 0, i5);
                        }
                        while (i5 < newArray5.length - 1) {
                            newArray5[i5] = new HistogramBucket();
                            input.readMessage(newArray5[i5]);
                            input.readTag();
                            i5++;
                        }
                        newArray5[i5] = new HistogramBucket();
                        input.readMessage(newArray5[i5]);
                        this.histogramRequestIntervalMs = newArray5;
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static RttToPeerLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (RttToPeerLog) MessageNano.mergeFrom(new RttToPeerLog(), data);
            }

            public static RttToPeerLog parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new RttToPeerLog().mergeFrom(input);
            }
        }

        public static final class HistogramBucket extends MessageNano {
            private static volatile HistogramBucket[] _emptyArray;
            public int count;
            public long end;
            public long start;

            public static HistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new HistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public HistogramBucket() {
                clear();
            }

            public HistogramBucket clear() {
                this.start = 0;
                this.end = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.start != 0) {
                    output.writeInt64(1, this.start);
                }
                if (this.end != 0) {
                    output.writeInt64(2, this.end);
                }
                if (this.count != 0) {
                    output.writeInt32(3, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.start != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(1, this.start);
                }
                if (this.end != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(2, this.end);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(3, this.count);
                }
                return size;
            }

            public HistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.start = input.readInt64();
                    } else if (tag == 16) {
                        this.end = input.readInt64();
                    } else if (tag == 24) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static HistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (HistogramBucket) MessageNano.mergeFrom(new HistogramBucket(), data);
            }

            public static HistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new HistogramBucket().mergeFrom(input);
            }
        }

        public static final class RttOverallStatusHistogramBucket extends MessageNano {
            private static volatile RttOverallStatusHistogramBucket[] _emptyArray;
            public int count;
            public int statusType;

            public static RttOverallStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new RttOverallStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public RttOverallStatusHistogramBucket() {
                clear();
            }

            public RttOverallStatusHistogramBucket clear() {
                this.statusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.statusType != 0) {
                    output.writeInt32(1, this.statusType);
                }
                if (this.count != 0) {
                    output.writeInt32(2, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.statusType != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.statusType);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
                }
                return size;
            }

            public RttOverallStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                    this.statusType = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static RttOverallStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (RttOverallStatusHistogramBucket) MessageNano.mergeFrom(new RttOverallStatusHistogramBucket(), data);
            }

            public static RttOverallStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new RttOverallStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static final class RttIndividualStatusHistogramBucket extends MessageNano {
            private static volatile RttIndividualStatusHistogramBucket[] _emptyArray;
            public int count;
            public int statusType;

            public static RttIndividualStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new RttIndividualStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public RttIndividualStatusHistogramBucket() {
                clear();
            }

            public RttIndividualStatusHistogramBucket clear() {
                this.statusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.statusType != 0) {
                    output.writeInt32(1, this.statusType);
                }
                if (this.count != 0) {
                    output.writeInt32(2, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.statusType != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.statusType);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
                }
                return size;
            }

            public RttIndividualStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                    this.statusType = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static RttIndividualStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (RttIndividualStatusHistogramBucket) MessageNano.mergeFrom(new RttIndividualStatusHistogramBucket(), data);
            }

            public static RttIndividualStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new RttIndividualStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static WifiRttLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiRttLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiRttLog() {
            clear();
        }

        public WifiRttLog clear() {
            this.numRequests = 0;
            this.histogramOverallStatus = RttOverallStatusHistogramBucket.emptyArray();
            this.rttToAp = null;
            this.rttToAware = null;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numRequests != 0) {
                output.writeInt32(1, this.numRequests);
            }
            if (this.histogramOverallStatus != null && this.histogramOverallStatus.length > 0) {
                for (RttOverallStatusHistogramBucket element : this.histogramOverallStatus) {
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                }
            }
            if (this.rttToAp != null) {
                output.writeMessage(3, this.rttToAp);
            }
            if (this.rttToAware != null) {
                output.writeMessage(4, this.rttToAware);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numRequests != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numRequests);
            }
            if (this.histogramOverallStatus != null && this.histogramOverallStatus.length > 0) {
                for (RttOverallStatusHistogramBucket element : this.histogramOverallStatus) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                }
            }
            if (this.rttToAp != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(3, this.rttToAp);
            }
            if (this.rttToAware != null) {
                return size + CodedOutputByteBufferNano.computeMessageSize(4, this.rttToAware);
            }
            return size;
        }

        public WifiRttLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numRequests = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int i = this.histogramOverallStatus == null ? 0 : this.histogramOverallStatus.length;
                    RttOverallStatusHistogramBucket[] newArray = new RttOverallStatusHistogramBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.histogramOverallStatus, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new RttOverallStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new RttOverallStatusHistogramBucket();
                    input.readMessage(newArray[i]);
                    this.histogramOverallStatus = newArray;
                } else if (tag == 26) {
                    if (this.rttToAp == null) {
                        this.rttToAp = new RttToPeerLog();
                    }
                    input.readMessage(this.rttToAp);
                } else if (tag == 34) {
                    if (this.rttToAware == null) {
                        this.rttToAware = new RttToPeerLog();
                    }
                    input.readMessage(this.rttToAware);
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiRttLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiRttLog) MessageNano.mergeFrom(new WifiRttLog(), data);
        }

        public static WifiRttLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiRttLog().mergeFrom(input);
        }
    }

    public static final class WifiRadioUsage extends MessageNano {
        private static volatile WifiRadioUsage[] _emptyArray;
        public long loggingDurationMs;
        public long scanTimeMs;

        public static WifiRadioUsage[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiRadioUsage[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiRadioUsage() {
            clear();
        }

        public WifiRadioUsage clear() {
            this.loggingDurationMs = 0;
            this.scanTimeMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.loggingDurationMs != 0) {
                output.writeInt64(1, this.loggingDurationMs);
            }
            if (this.scanTimeMs != 0) {
                output.writeInt64(2, this.scanTimeMs);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.loggingDurationMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, this.loggingDurationMs);
            }
            if (this.scanTimeMs != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(2, this.scanTimeMs);
            }
            return size;
        }

        public WifiRadioUsage mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.loggingDurationMs = input.readInt64();
                } else if (tag == 16) {
                    this.scanTimeMs = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiRadioUsage parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiRadioUsage) MessageNano.mergeFrom(new WifiRadioUsage(), data);
        }

        public static WifiRadioUsage parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiRadioUsage().mergeFrom(input);
        }
    }

    public static final class ExperimentValues extends MessageNano {
        private static volatile ExperimentValues[] _emptyArray;
        public boolean linkSpeedCountsLoggingEnabled;
        public int wifiDataStallMinTxBad;
        public int wifiDataStallMinTxSuccessWithoutRx;
        public boolean wifiIsUnusableLoggingEnabled;

        public static ExperimentValues[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ExperimentValues[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ExperimentValues() {
            clear();
        }

        public ExperimentValues clear() {
            this.wifiIsUnusableLoggingEnabled = false;
            this.wifiDataStallMinTxBad = 0;
            this.wifiDataStallMinTxSuccessWithoutRx = 0;
            this.linkSpeedCountsLoggingEnabled = false;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.wifiIsUnusableLoggingEnabled) {
                output.writeBool(1, this.wifiIsUnusableLoggingEnabled);
            }
            if (this.wifiDataStallMinTxBad != 0) {
                output.writeInt32(2, this.wifiDataStallMinTxBad);
            }
            if (this.wifiDataStallMinTxSuccessWithoutRx != 0) {
                output.writeInt32(3, this.wifiDataStallMinTxSuccessWithoutRx);
            }
            if (this.linkSpeedCountsLoggingEnabled) {
                output.writeBool(4, this.linkSpeedCountsLoggingEnabled);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.wifiIsUnusableLoggingEnabled) {
                size += CodedOutputByteBufferNano.computeBoolSize(1, this.wifiIsUnusableLoggingEnabled);
            }
            if (this.wifiDataStallMinTxBad != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.wifiDataStallMinTxBad);
            }
            if (this.wifiDataStallMinTxSuccessWithoutRx != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.wifiDataStallMinTxSuccessWithoutRx);
            }
            if (this.linkSpeedCountsLoggingEnabled) {
                return size + CodedOutputByteBufferNano.computeBoolSize(4, this.linkSpeedCountsLoggingEnabled);
            }
            return size;
        }

        public ExperimentValues mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.wifiIsUnusableLoggingEnabled = input.readBool();
                } else if (tag == 16) {
                    this.wifiDataStallMinTxBad = input.readInt32();
                } else if (tag == 24) {
                    this.wifiDataStallMinTxSuccessWithoutRx = input.readInt32();
                } else if (tag == 32) {
                    this.linkSpeedCountsLoggingEnabled = input.readBool();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static ExperimentValues parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ExperimentValues) MessageNano.mergeFrom(new ExperimentValues(), data);
        }

        public static ExperimentValues parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ExperimentValues().mergeFrom(input);
        }
    }

    public static final class WifiIsUnusableEvent extends MessageNano {
        public static final int TYPE_DATA_STALL_BAD_TX = 1;
        public static final int TYPE_DATA_STALL_BOTH = 3;
        public static final int TYPE_DATA_STALL_TX_WITHOUT_RX = 2;
        public static final int TYPE_FIRMWARE_ALERT = 4;
        public static final int TYPE_IP_REACHABILITY_LOST = 5;
        public static final int TYPE_UNKNOWN = 0;
        private static volatile WifiIsUnusableEvent[] _emptyArray;
        public int firmwareAlertCode;
        public long lastLinkLayerStatsUpdateTime;
        public int lastPredictionHorizonSec;
        public int lastScore;
        public int lastWifiUsabilityScore;
        public long packetUpdateTimeDelta;
        public long rxSuccessDelta;
        public boolean screenOn;
        public long startTimeMillis;
        public long txBadDelta;
        public long txRetriesDelta;
        public long txSuccessDelta;
        public int type;

        public static WifiIsUnusableEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiIsUnusableEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiIsUnusableEvent() {
            clear();
        }

        public WifiIsUnusableEvent clear() {
            this.type = 0;
            this.startTimeMillis = 0;
            this.lastScore = -1;
            this.txSuccessDelta = 0;
            this.txRetriesDelta = 0;
            this.txBadDelta = 0;
            this.rxSuccessDelta = 0;
            this.packetUpdateTimeDelta = 0;
            this.lastLinkLayerStatsUpdateTime = 0;
            this.firmwareAlertCode = -1;
            this.lastWifiUsabilityScore = -1;
            this.lastPredictionHorizonSec = -1;
            this.screenOn = false;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.type != 0) {
                output.writeInt32(1, this.type);
            }
            if (this.startTimeMillis != 0) {
                output.writeInt64(2, this.startTimeMillis);
            }
            if (this.lastScore != -1) {
                output.writeInt32(3, this.lastScore);
            }
            if (this.txSuccessDelta != 0) {
                output.writeInt64(4, this.txSuccessDelta);
            }
            if (this.txRetriesDelta != 0) {
                output.writeInt64(5, this.txRetriesDelta);
            }
            if (this.txBadDelta != 0) {
                output.writeInt64(6, this.txBadDelta);
            }
            if (this.rxSuccessDelta != 0) {
                output.writeInt64(7, this.rxSuccessDelta);
            }
            if (this.packetUpdateTimeDelta != 0) {
                output.writeInt64(8, this.packetUpdateTimeDelta);
            }
            if (this.lastLinkLayerStatsUpdateTime != 0) {
                output.writeInt64(9, this.lastLinkLayerStatsUpdateTime);
            }
            if (this.firmwareAlertCode != -1) {
                output.writeInt32(10, this.firmwareAlertCode);
            }
            if (this.lastWifiUsabilityScore != -1) {
                output.writeInt32(11, this.lastWifiUsabilityScore);
            }
            if (this.lastPredictionHorizonSec != -1) {
                output.writeInt32(12, this.lastPredictionHorizonSec);
            }
            if (this.screenOn) {
                output.writeBool(13, this.screenOn);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.type != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.type);
            }
            if (this.startTimeMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, this.startTimeMillis);
            }
            if (this.lastScore != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.lastScore);
            }
            if (this.txSuccessDelta != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, this.txSuccessDelta);
            }
            if (this.txRetriesDelta != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, this.txRetriesDelta);
            }
            if (this.txBadDelta != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, this.txBadDelta);
            }
            if (this.rxSuccessDelta != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, this.rxSuccessDelta);
            }
            if (this.packetUpdateTimeDelta != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, this.packetUpdateTimeDelta);
            }
            if (this.lastLinkLayerStatsUpdateTime != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, this.lastLinkLayerStatsUpdateTime);
            }
            if (this.firmwareAlertCode != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(10, this.firmwareAlertCode);
            }
            if (this.lastWifiUsabilityScore != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, this.lastWifiUsabilityScore);
            }
            if (this.lastPredictionHorizonSec != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(12, this.lastPredictionHorizonSec);
            }
            if (this.screenOn) {
                return size + CodedOutputByteBufferNano.computeBoolSize(13, this.screenOn);
            }
            return size;
        }

        public WifiIsUnusableEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                this.type = value;
                                break;
                        }
                    case 16:
                        this.startTimeMillis = input.readInt64();
                        break;
                    case 24:
                        this.lastScore = input.readInt32();
                        break;
                    case 32:
                        this.txSuccessDelta = input.readInt64();
                        break;
                    case 40:
                        this.txRetriesDelta = input.readInt64();
                        break;
                    case 48:
                        this.txBadDelta = input.readInt64();
                        break;
                    case 56:
                        this.rxSuccessDelta = input.readInt64();
                        break;
                    case 64:
                        this.packetUpdateTimeDelta = input.readInt64();
                        break;
                    case 72:
                        this.lastLinkLayerStatsUpdateTime = input.readInt64();
                        break;
                    case 80:
                        this.firmwareAlertCode = input.readInt32();
                        break;
                    case 88:
                        this.lastWifiUsabilityScore = input.readInt32();
                        break;
                    case 96:
                        this.lastPredictionHorizonSec = input.readInt32();
                        break;
                    case 104:
                        this.screenOn = input.readBool();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static WifiIsUnusableEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiIsUnusableEvent) MessageNano.mergeFrom(new WifiIsUnusableEvent(), data);
        }

        public static WifiIsUnusableEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiIsUnusableEvent().mergeFrom(input);
        }
    }

    public static final class PasspointProfileTypeCount extends MessageNano {
        public static final int TYPE_EAP_AKA = 4;
        public static final int TYPE_EAP_AKA_PRIME = 5;
        public static final int TYPE_EAP_SIM = 3;
        public static final int TYPE_EAP_TLS = 1;
        public static final int TYPE_EAP_TTLS = 2;
        public static final int TYPE_UNKNOWN = 0;
        private static volatile PasspointProfileTypeCount[] _emptyArray;
        public int count;
        public int eapMethodType;

        public static PasspointProfileTypeCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PasspointProfileTypeCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PasspointProfileTypeCount() {
            clear();
        }

        public PasspointProfileTypeCount clear() {
            this.eapMethodType = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.eapMethodType != 0) {
                output.writeInt32(1, this.eapMethodType);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.eapMethodType != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.eapMethodType);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            return size;
        }

        public PasspointProfileTypeCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                this.eapMethodType = value;
                                break;
                        }
                    } else if (tag == 16) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    return this;
                }
            }
        }

        public static PasspointProfileTypeCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (PasspointProfileTypeCount) MessageNano.mergeFrom(new PasspointProfileTypeCount(), data);
        }

        public static PasspointProfileTypeCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new PasspointProfileTypeCount().mergeFrom(input);
        }
    }

    public static final class WifiLinkLayerUsageStats extends MessageNano {
        private static volatile WifiLinkLayerUsageStats[] _emptyArray;
        public long loggingDurationMs;
        public long radioBackgroundScanTimeMs;
        public long radioHs20ScanTimeMs;
        public long radioNanScanTimeMs;
        public long radioOnTimeMs;
        public long radioPnoScanTimeMs;
        public long radioRoamScanTimeMs;
        public long radioRxTimeMs;
        public long radioScanTimeMs;
        public long radioTxTimeMs;

        public static WifiLinkLayerUsageStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiLinkLayerUsageStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiLinkLayerUsageStats() {
            clear();
        }

        public WifiLinkLayerUsageStats clear() {
            this.loggingDurationMs = 0;
            this.radioOnTimeMs = 0;
            this.radioTxTimeMs = 0;
            this.radioRxTimeMs = 0;
            this.radioScanTimeMs = 0;
            this.radioNanScanTimeMs = 0;
            this.radioBackgroundScanTimeMs = 0;
            this.radioRoamScanTimeMs = 0;
            this.radioPnoScanTimeMs = 0;
            this.radioHs20ScanTimeMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.loggingDurationMs != 0) {
                output.writeInt64(1, this.loggingDurationMs);
            }
            if (this.radioOnTimeMs != 0) {
                output.writeInt64(2, this.radioOnTimeMs);
            }
            if (this.radioTxTimeMs != 0) {
                output.writeInt64(3, this.radioTxTimeMs);
            }
            if (this.radioRxTimeMs != 0) {
                output.writeInt64(4, this.radioRxTimeMs);
            }
            if (this.radioScanTimeMs != 0) {
                output.writeInt64(5, this.radioScanTimeMs);
            }
            if (this.radioNanScanTimeMs != 0) {
                output.writeInt64(6, this.radioNanScanTimeMs);
            }
            if (this.radioBackgroundScanTimeMs != 0) {
                output.writeInt64(7, this.radioBackgroundScanTimeMs);
            }
            if (this.radioRoamScanTimeMs != 0) {
                output.writeInt64(8, this.radioRoamScanTimeMs);
            }
            if (this.radioPnoScanTimeMs != 0) {
                output.writeInt64(9, this.radioPnoScanTimeMs);
            }
            if (this.radioHs20ScanTimeMs != 0) {
                output.writeInt64(10, this.radioHs20ScanTimeMs);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.loggingDurationMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, this.loggingDurationMs);
            }
            if (this.radioOnTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, this.radioOnTimeMs);
            }
            if (this.radioTxTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, this.radioTxTimeMs);
            }
            if (this.radioRxTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, this.radioRxTimeMs);
            }
            if (this.radioScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, this.radioScanTimeMs);
            }
            if (this.radioNanScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, this.radioNanScanTimeMs);
            }
            if (this.radioBackgroundScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, this.radioBackgroundScanTimeMs);
            }
            if (this.radioRoamScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, this.radioRoamScanTimeMs);
            }
            if (this.radioPnoScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, this.radioPnoScanTimeMs);
            }
            if (this.radioHs20ScanTimeMs != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(10, this.radioHs20ScanTimeMs);
            }
            return size;
        }

        public WifiLinkLayerUsageStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.loggingDurationMs = input.readInt64();
                        break;
                    case 16:
                        this.radioOnTimeMs = input.readInt64();
                        break;
                    case 24:
                        this.radioTxTimeMs = input.readInt64();
                        break;
                    case 32:
                        this.radioRxTimeMs = input.readInt64();
                        break;
                    case 40:
                        this.radioScanTimeMs = input.readInt64();
                        break;
                    case 48:
                        this.radioNanScanTimeMs = input.readInt64();
                        break;
                    case 56:
                        this.radioBackgroundScanTimeMs = input.readInt64();
                        break;
                    case 64:
                        this.radioRoamScanTimeMs = input.readInt64();
                        break;
                    case 72:
                        this.radioPnoScanTimeMs = input.readInt64();
                        break;
                    case 80:
                        this.radioHs20ScanTimeMs = input.readInt64();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static WifiLinkLayerUsageStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiLinkLayerUsageStats) MessageNano.mergeFrom(new WifiLinkLayerUsageStats(), data);
        }

        public static WifiLinkLayerUsageStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiLinkLayerUsageStats().mergeFrom(input);
        }
    }

    public static final class WifiUsabilityStatsEntry extends MessageNano {
        public static final int NETWORK_TYPE_CDMA = 2;
        public static final int NETWORK_TYPE_EVDO_0 = 3;
        public static final int NETWORK_TYPE_GSM = 1;
        public static final int NETWORK_TYPE_LTE = 6;
        public static final int NETWORK_TYPE_NR = 7;
        public static final int NETWORK_TYPE_TD_SCDMA = 5;
        public static final int NETWORK_TYPE_UMTS = 4;
        public static final int NETWORK_TYPE_UNKNOWN = 0;
        public static final int PROBE_STATUS_FAILURE = 3;
        public static final int PROBE_STATUS_NO_PROBE = 1;
        public static final int PROBE_STATUS_SUCCESS = 2;
        public static final int PROBE_STATUS_UNKNOWN = 0;
        private static volatile WifiUsabilityStatsEntry[] _emptyArray;
        public int cellularDataNetworkType;
        public int cellularSignalStrengthDb;
        public int cellularSignalStrengthDbm;
        public int deviceMobilityState;
        public boolean isSameBssidAndFreq;
        public boolean isSameRegisteredCell;
        public int linkSpeedMbps;
        public int predictionHorizonSec;
        public int probeElapsedTimeSinceLastUpdateMs;
        public int probeMcsRateSinceLastUpdate;
        public int probeStatusSinceLastUpdate;
        public int rssi;
        public int rxLinkSpeedMbps;
        public int seqNumInsideFramework;
        public int seqNumToFramework;
        public long timeStampMs;
        public long totalBackgroundScanTimeMs;
        public long totalBeaconRx;
        public long totalCcaBusyFreqTimeMs;
        public long totalHotspot2ScanTimeMs;
        public long totalNanScanTimeMs;
        public long totalPnoScanTimeMs;
        public long totalRadioOnFreqTimeMs;
        public long totalRadioOnTimeMs;
        public long totalRadioRxTimeMs;
        public long totalRadioTxTimeMs;
        public long totalRoamScanTimeMs;
        public long totalRxSuccess;
        public long totalScanTimeMs;
        public long totalTxBad;
        public long totalTxRetries;
        public long totalTxSuccess;
        public int wifiScore;
        public int wifiUsabilityScore;

        public static WifiUsabilityStatsEntry[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiUsabilityStatsEntry[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiUsabilityStatsEntry() {
            clear();
        }

        public WifiUsabilityStatsEntry clear() {
            this.timeStampMs = 0;
            this.rssi = 0;
            this.linkSpeedMbps = 0;
            this.totalTxSuccess = 0;
            this.totalTxRetries = 0;
            this.totalTxBad = 0;
            this.totalRxSuccess = 0;
            this.totalRadioOnTimeMs = 0;
            this.totalRadioTxTimeMs = 0;
            this.totalRadioRxTimeMs = 0;
            this.totalScanTimeMs = 0;
            this.totalNanScanTimeMs = 0;
            this.totalBackgroundScanTimeMs = 0;
            this.totalRoamScanTimeMs = 0;
            this.totalPnoScanTimeMs = 0;
            this.totalHotspot2ScanTimeMs = 0;
            this.wifiScore = 0;
            this.wifiUsabilityScore = 0;
            this.seqNumToFramework = 0;
            this.totalCcaBusyFreqTimeMs = 0;
            this.totalRadioOnFreqTimeMs = 0;
            this.totalBeaconRx = 0;
            this.predictionHorizonSec = 0;
            this.probeStatusSinceLastUpdate = 0;
            this.probeElapsedTimeSinceLastUpdateMs = 0;
            this.probeMcsRateSinceLastUpdate = 0;
            this.rxLinkSpeedMbps = 0;
            this.seqNumInsideFramework = 0;
            this.isSameBssidAndFreq = false;
            this.cellularDataNetworkType = 0;
            this.cellularSignalStrengthDbm = 0;
            this.cellularSignalStrengthDb = 0;
            this.isSameRegisteredCell = false;
            this.deviceMobilityState = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.timeStampMs != 0) {
                output.writeInt64(1, this.timeStampMs);
            }
            if (this.rssi != 0) {
                output.writeInt32(2, this.rssi);
            }
            if (this.linkSpeedMbps != 0) {
                output.writeInt32(3, this.linkSpeedMbps);
            }
            if (this.totalTxSuccess != 0) {
                output.writeInt64(4, this.totalTxSuccess);
            }
            if (this.totalTxRetries != 0) {
                output.writeInt64(5, this.totalTxRetries);
            }
            if (this.totalTxBad != 0) {
                output.writeInt64(6, this.totalTxBad);
            }
            if (this.totalRxSuccess != 0) {
                output.writeInt64(7, this.totalRxSuccess);
            }
            if (this.totalRadioOnTimeMs != 0) {
                output.writeInt64(8, this.totalRadioOnTimeMs);
            }
            if (this.totalRadioTxTimeMs != 0) {
                output.writeInt64(9, this.totalRadioTxTimeMs);
            }
            if (this.totalRadioRxTimeMs != 0) {
                output.writeInt64(10, this.totalRadioRxTimeMs);
            }
            if (this.totalScanTimeMs != 0) {
                output.writeInt64(11, this.totalScanTimeMs);
            }
            if (this.totalNanScanTimeMs != 0) {
                output.writeInt64(12, this.totalNanScanTimeMs);
            }
            if (this.totalBackgroundScanTimeMs != 0) {
                output.writeInt64(13, this.totalBackgroundScanTimeMs);
            }
            if (this.totalRoamScanTimeMs != 0) {
                output.writeInt64(14, this.totalRoamScanTimeMs);
            }
            if (this.totalPnoScanTimeMs != 0) {
                output.writeInt64(15, this.totalPnoScanTimeMs);
            }
            if (this.totalHotspot2ScanTimeMs != 0) {
                output.writeInt64(16, this.totalHotspot2ScanTimeMs);
            }
            if (this.wifiScore != 0) {
                output.writeInt32(17, this.wifiScore);
            }
            if (this.wifiUsabilityScore != 0) {
                output.writeInt32(18, this.wifiUsabilityScore);
            }
            if (this.seqNumToFramework != 0) {
                output.writeInt32(19, this.seqNumToFramework);
            }
            if (this.totalCcaBusyFreqTimeMs != 0) {
                output.writeInt64(20, this.totalCcaBusyFreqTimeMs);
            }
            if (this.totalRadioOnFreqTimeMs != 0) {
                output.writeInt64(21, this.totalRadioOnFreqTimeMs);
            }
            if (this.totalBeaconRx != 0) {
                output.writeInt64(22, this.totalBeaconRx);
            }
            if (this.predictionHorizonSec != 0) {
                output.writeInt32(23, this.predictionHorizonSec);
            }
            if (this.probeStatusSinceLastUpdate != 0) {
                output.writeInt32(24, this.probeStatusSinceLastUpdate);
            }
            if (this.probeElapsedTimeSinceLastUpdateMs != 0) {
                output.writeInt32(25, this.probeElapsedTimeSinceLastUpdateMs);
            }
            if (this.probeMcsRateSinceLastUpdate != 0) {
                output.writeInt32(26, this.probeMcsRateSinceLastUpdate);
            }
            if (this.rxLinkSpeedMbps != 0) {
                output.writeInt32(27, this.rxLinkSpeedMbps);
            }
            if (this.seqNumInsideFramework != 0) {
                output.writeInt32(28, this.seqNumInsideFramework);
            }
            if (this.isSameBssidAndFreq) {
                output.writeBool(29, this.isSameBssidAndFreq);
            }
            if (this.cellularDataNetworkType != 0) {
                output.writeInt32(30, this.cellularDataNetworkType);
            }
            if (this.cellularSignalStrengthDbm != 0) {
                output.writeInt32(31, this.cellularSignalStrengthDbm);
            }
            if (this.cellularSignalStrengthDb != 0) {
                output.writeInt32(32, this.cellularSignalStrengthDb);
            }
            if (this.isSameRegisteredCell) {
                output.writeBool(33, this.isSameRegisteredCell);
            }
            if (this.deviceMobilityState != 0) {
                output.writeInt32(34, this.deviceMobilityState);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.timeStampMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, this.timeStampMs);
            }
            if (this.rssi != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.rssi);
            }
            if (this.linkSpeedMbps != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.linkSpeedMbps);
            }
            if (this.totalTxSuccess != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, this.totalTxSuccess);
            }
            if (this.totalTxRetries != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, this.totalTxRetries);
            }
            if (this.totalTxBad != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, this.totalTxBad);
            }
            if (this.totalRxSuccess != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, this.totalRxSuccess);
            }
            if (this.totalRadioOnTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, this.totalRadioOnTimeMs);
            }
            if (this.totalRadioTxTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, this.totalRadioTxTimeMs);
            }
            if (this.totalRadioRxTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(10, this.totalRadioRxTimeMs);
            }
            if (this.totalScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(11, this.totalScanTimeMs);
            }
            if (this.totalNanScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(12, this.totalNanScanTimeMs);
            }
            if (this.totalBackgroundScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(13, this.totalBackgroundScanTimeMs);
            }
            if (this.totalRoamScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(14, this.totalRoamScanTimeMs);
            }
            if (this.totalPnoScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(15, this.totalPnoScanTimeMs);
            }
            if (this.totalHotspot2ScanTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(16, this.totalHotspot2ScanTimeMs);
            }
            if (this.wifiScore != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(17, this.wifiScore);
            }
            if (this.wifiUsabilityScore != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(18, this.wifiUsabilityScore);
            }
            if (this.seqNumToFramework != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(19, this.seqNumToFramework);
            }
            if (this.totalCcaBusyFreqTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(20, this.totalCcaBusyFreqTimeMs);
            }
            if (this.totalRadioOnFreqTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(21, this.totalRadioOnFreqTimeMs);
            }
            if (this.totalBeaconRx != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(22, this.totalBeaconRx);
            }
            if (this.predictionHorizonSec != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(23, this.predictionHorizonSec);
            }
            if (this.probeStatusSinceLastUpdate != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(24, this.probeStatusSinceLastUpdate);
            }
            if (this.probeElapsedTimeSinceLastUpdateMs != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(25, this.probeElapsedTimeSinceLastUpdateMs);
            }
            if (this.probeMcsRateSinceLastUpdate != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(26, this.probeMcsRateSinceLastUpdate);
            }
            if (this.rxLinkSpeedMbps != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(27, this.rxLinkSpeedMbps);
            }
            if (this.seqNumInsideFramework != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(28, this.seqNumInsideFramework);
            }
            if (this.isSameBssidAndFreq) {
                size += CodedOutputByteBufferNano.computeBoolSize(29, this.isSameBssidAndFreq);
            }
            if (this.cellularDataNetworkType != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(30, this.cellularDataNetworkType);
            }
            if (this.cellularSignalStrengthDbm != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(31, this.cellularSignalStrengthDbm);
            }
            if (this.cellularSignalStrengthDb != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(32, this.cellularSignalStrengthDb);
            }
            if (this.isSameRegisteredCell) {
                size += CodedOutputByteBufferNano.computeBoolSize(33, this.isSameRegisteredCell);
            }
            if (this.deviceMobilityState != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(34, this.deviceMobilityState);
            }
            return size;
        }

        public WifiUsabilityStatsEntry mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.timeStampMs = input.readInt64();
                        break;
                    case 16:
                        this.rssi = input.readInt32();
                        break;
                    case 24:
                        this.linkSpeedMbps = input.readInt32();
                        break;
                    case 32:
                        this.totalTxSuccess = input.readInt64();
                        break;
                    case 40:
                        this.totalTxRetries = input.readInt64();
                        break;
                    case 48:
                        this.totalTxBad = input.readInt64();
                        break;
                    case 56:
                        this.totalRxSuccess = input.readInt64();
                        break;
                    case 64:
                        this.totalRadioOnTimeMs = input.readInt64();
                        break;
                    case 72:
                        this.totalRadioTxTimeMs = input.readInt64();
                        break;
                    case 80:
                        this.totalRadioRxTimeMs = input.readInt64();
                        break;
                    case 88:
                        this.totalScanTimeMs = input.readInt64();
                        break;
                    case 96:
                        this.totalNanScanTimeMs = input.readInt64();
                        break;
                    case 104:
                        this.totalBackgroundScanTimeMs = input.readInt64();
                        break;
                    case 112:
                        this.totalRoamScanTimeMs = input.readInt64();
                        break;
                    case 120:
                        this.totalPnoScanTimeMs = input.readInt64();
                        break;
                    case 128:
                        this.totalHotspot2ScanTimeMs = input.readInt64();
                        break;
                    case 136:
                        this.wifiScore = input.readInt32();
                        break;
                    case 144:
                        this.wifiUsabilityScore = input.readInt32();
                        break;
                    case 152:
                        this.seqNumToFramework = input.readInt32();
                        break;
                    case 160:
                        this.totalCcaBusyFreqTimeMs = input.readInt64();
                        break;
                    case 168:
                        this.totalRadioOnFreqTimeMs = input.readInt64();
                        break;
                    case 176:
                        this.totalBeaconRx = input.readInt64();
                        break;
                    case 184:
                        this.predictionHorizonSec = input.readInt32();
                        break;
                    case 192:
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                this.probeStatusSinceLastUpdate = value;
                                break;
                        }
                    case 200:
                        this.probeElapsedTimeSinceLastUpdateMs = input.readInt32();
                        break;
                    case 208:
                        this.probeMcsRateSinceLastUpdate = input.readInt32();
                        break;
                    case 216:
                        this.rxLinkSpeedMbps = input.readInt32();
                        break;
                    case 224:
                        this.seqNumInsideFramework = input.readInt32();
                        break;
                    case 232:
                        this.isSameBssidAndFreq = input.readBool();
                        break;
                    case 240:
                        int value2 = input.readInt32();
                        switch (value2) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                this.cellularDataNetworkType = value2;
                                break;
                        }
                    case 248:
                        this.cellularSignalStrengthDbm = input.readInt32();
                        break;
                    case 256:
                        this.cellularSignalStrengthDb = input.readInt32();
                        break;
                    case 264:
                        this.isSameRegisteredCell = input.readBool();
                        break;
                    case 272:
                        int value3 = input.readInt32();
                        switch (value3) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                this.deviceMobilityState = value3;
                                break;
                        }
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        } else {
                            return this;
                        }
                }
            }
        }

        public static WifiUsabilityStatsEntry parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiUsabilityStatsEntry) MessageNano.mergeFrom(new WifiUsabilityStatsEntry(), data);
        }

        public static WifiUsabilityStatsEntry parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiUsabilityStatsEntry().mergeFrom(input);
        }
    }

    public static final class WifiUsabilityStats extends MessageNano {
        public static final int LABEL_BAD = 2;
        public static final int LABEL_GOOD = 1;
        public static final int LABEL_UNKNOWN = 0;
        public static final int TYPE_DATA_STALL_BAD_TX = 1;
        public static final int TYPE_DATA_STALL_BOTH = 3;
        public static final int TYPE_DATA_STALL_TX_WITHOUT_RX = 2;
        public static final int TYPE_FIRMWARE_ALERT = 4;
        public static final int TYPE_IP_REACHABILITY_LOST = 5;
        public static final int TYPE_UNKNOWN = 0;
        private static volatile WifiUsabilityStats[] _emptyArray;
        public int firmwareAlertCode;
        public int label;
        public WifiUsabilityStatsEntry[] stats;
        public long timeStampMs;
        public int triggerType;

        public static WifiUsabilityStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiUsabilityStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiUsabilityStats() {
            clear();
        }

        public WifiUsabilityStats clear() {
            this.label = 0;
            this.stats = WifiUsabilityStatsEntry.emptyArray();
            this.triggerType = 0;
            this.firmwareAlertCode = -1;
            this.timeStampMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.label != 0) {
                output.writeInt32(1, this.label);
            }
            if (this.stats != null && this.stats.length > 0) {
                for (WifiUsabilityStatsEntry element : this.stats) {
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                }
            }
            if (this.triggerType != 0) {
                output.writeInt32(3, this.triggerType);
            }
            if (this.firmwareAlertCode != -1) {
                output.writeInt32(4, this.firmwareAlertCode);
            }
            if (this.timeStampMs != 0) {
                output.writeInt64(5, this.timeStampMs);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.label != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.label);
            }
            if (this.stats != null && this.stats.length > 0) {
                for (WifiUsabilityStatsEntry element : this.stats) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                }
            }
            if (this.triggerType != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.triggerType);
            }
            if (this.firmwareAlertCode != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.firmwareAlertCode);
            }
            if (this.timeStampMs != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(5, this.timeStampMs);
            }
            return size;
        }

        public WifiUsabilityStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int arrayLength = input.readInt32();
                        switch (arrayLength) {
                            case 0:
                            case 1:
                            case 2:
                                this.label = arrayLength;
                                break;
                        }
                    } else if (tag != 18) {
                        if (tag == 24) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                    this.triggerType = value;
                                    break;
                            }
                        } else if (tag == 32) {
                            this.firmwareAlertCode = input.readInt32();
                        } else if (tag == 40) {
                            this.timeStampMs = input.readInt64();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                        int i = this.stats == null ? 0 : this.stats.length;
                        WifiUsabilityStatsEntry[] newArray = new WifiUsabilityStatsEntry[(i + arrayLength2)];
                        if (i != 0) {
                            System.arraycopy(this.stats, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new WifiUsabilityStatsEntry();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new WifiUsabilityStatsEntry();
                        input.readMessage(newArray[i]);
                        this.stats = newArray;
                    }
                } else {
                    return this;
                }
            }
        }

        public static WifiUsabilityStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiUsabilityStats) MessageNano.mergeFrom(new WifiUsabilityStats(), data);
        }

        public static WifiUsabilityStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiUsabilityStats().mergeFrom(input);
        }
    }

    public static final class DeviceMobilityStatePnoScanStats extends MessageNano {
        public static final int HIGH_MVMT = 1;
        public static final int LOW_MVMT = 2;
        public static final int STATIONARY = 3;
        public static final int UNKNOWN = 0;
        private static volatile DeviceMobilityStatePnoScanStats[] _emptyArray;
        public int deviceMobilityState;
        public int numTimesEnteredState;
        public long pnoDurationMs;
        public long totalDurationMs;

        public static DeviceMobilityStatePnoScanStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new DeviceMobilityStatePnoScanStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public DeviceMobilityStatePnoScanStats() {
            clear();
        }

        public DeviceMobilityStatePnoScanStats clear() {
            this.deviceMobilityState = 0;
            this.numTimesEnteredState = 0;
            this.totalDurationMs = 0;
            this.pnoDurationMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.deviceMobilityState != 0) {
                output.writeInt32(1, this.deviceMobilityState);
            }
            if (this.numTimesEnteredState != 0) {
                output.writeInt32(2, this.numTimesEnteredState);
            }
            if (this.totalDurationMs != 0) {
                output.writeInt64(3, this.totalDurationMs);
            }
            if (this.pnoDurationMs != 0) {
                output.writeInt64(4, this.pnoDurationMs);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.deviceMobilityState != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.deviceMobilityState);
            }
            if (this.numTimesEnteredState != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.numTimesEnteredState);
            }
            if (this.totalDurationMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, this.totalDurationMs);
            }
            if (this.pnoDurationMs != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(4, this.pnoDurationMs);
            }
            return size;
        }

        public DeviceMobilityStatePnoScanStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                this.deviceMobilityState = value;
                                break;
                        }
                    } else if (tag == 16) {
                        this.numTimesEnteredState = input.readInt32();
                    } else if (tag == 24) {
                        this.totalDurationMs = input.readInt64();
                    } else if (tag == 32) {
                        this.pnoDurationMs = input.readInt64();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    return this;
                }
            }
        }

        public static DeviceMobilityStatePnoScanStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (DeviceMobilityStatePnoScanStats) MessageNano.mergeFrom(new DeviceMobilityStatePnoScanStats(), data);
        }

        public static DeviceMobilityStatePnoScanStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new DeviceMobilityStatePnoScanStats().mergeFrom(input);
        }
    }

    public static final class WifiP2pStats extends MessageNano {
        private static volatile WifiP2pStats[] _emptyArray;
        public P2pConnectionEvent[] connectionEvent;
        public GroupEvent[] groupEvent;
        public int numPersistentGroup;
        public int numTotalPeerScans;
        public int numTotalServiceScans;

        public static WifiP2pStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiP2pStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiP2pStats() {
            clear();
        }

        public WifiP2pStats clear() {
            this.groupEvent = GroupEvent.emptyArray();
            this.connectionEvent = P2pConnectionEvent.emptyArray();
            this.numPersistentGroup = 0;
            this.numTotalPeerScans = 0;
            this.numTotalServiceScans = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = 0;
            if (this.groupEvent != null && this.groupEvent.length > 0) {
                for (GroupEvent element : this.groupEvent) {
                    if (element != null) {
                        output.writeMessage(1, element);
                    }
                }
            }
            if (this.connectionEvent != null && this.connectionEvent.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.connectionEvent.length) {
                        break;
                    }
                    P2pConnectionEvent element2 = this.connectionEvent[i2];
                    if (element2 != null) {
                        output.writeMessage(2, element2);
                    }
                    i = i2 + 1;
                }
            }
            if (this.numPersistentGroup != 0) {
                output.writeInt32(3, this.numPersistentGroup);
            }
            if (this.numTotalPeerScans != 0) {
                output.writeInt32(4, this.numTotalPeerScans);
            }
            if (this.numTotalServiceScans != 0) {
                output.writeInt32(5, this.numTotalServiceScans);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int i = super.computeSerializedSize();
            int i2 = 0;
            if (this.groupEvent != null && this.groupEvent.length > 0) {
                int size = i;
                for (GroupEvent element : this.groupEvent) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(1, element);
                    }
                }
                i = size;
            }
            if (this.connectionEvent != null && this.connectionEvent.length > 0) {
                while (true) {
                    int i3 = i2;
                    if (i3 >= this.connectionEvent.length) {
                        break;
                    }
                    P2pConnectionEvent element2 = this.connectionEvent[i3];
                    if (element2 != null) {
                        i += CodedOutputByteBufferNano.computeMessageSize(2, element2);
                    }
                    i2 = i3 + 1;
                }
            }
            if (this.numPersistentGroup != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(3, this.numPersistentGroup);
            }
            if (this.numTotalPeerScans != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(4, this.numTotalPeerScans);
            }
            if (this.numTotalServiceScans != 0) {
                return i + CodedOutputByteBufferNano.computeInt32Size(5, this.numTotalServiceScans);
            }
            return i;
        }

        public WifiP2pStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    int i = this.groupEvent == null ? 0 : this.groupEvent.length;
                    GroupEvent[] newArray = new GroupEvent[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.groupEvent, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new GroupEvent();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new GroupEvent();
                    input.readMessage(newArray[i]);
                    this.groupEvent = newArray;
                } else if (tag == 18) {
                    int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int i2 = this.connectionEvent == null ? 0 : this.connectionEvent.length;
                    P2pConnectionEvent[] newArray2 = new P2pConnectionEvent[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.connectionEvent, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length - 1) {
                        newArray2[i2] = new P2pConnectionEvent();
                        input.readMessage(newArray2[i2]);
                        input.readTag();
                        i2++;
                    }
                    newArray2[i2] = new P2pConnectionEvent();
                    input.readMessage(newArray2[i2]);
                    this.connectionEvent = newArray2;
                } else if (tag == 24) {
                    this.numPersistentGroup = input.readInt32();
                } else if (tag == 32) {
                    this.numTotalPeerScans = input.readInt32();
                } else if (tag == 40) {
                    this.numTotalServiceScans = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiP2pStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiP2pStats) MessageNano.mergeFrom(new WifiP2pStats(), data);
        }

        public static WifiP2pStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiP2pStats().mergeFrom(input);
        }
    }

    public static final class P2pConnectionEvent extends MessageNano {
        public static final int CLF_CANCEL = 3;
        public static final int CLF_INVITATION_FAIL = 5;
        public static final int CLF_NEW_CONNECTION_ATTEMPT = 7;
        public static final int CLF_NONE = 1;
        public static final int CLF_PROV_DISC_FAIL = 4;
        public static final int CLF_TIMEOUT = 2;
        public static final int CLF_UNKNOWN = 0;
        public static final int CLF_USER_REJECT = 6;
        public static final int CONNECTION_FAST = 3;
        public static final int CONNECTION_FRESH = 0;
        public static final int CONNECTION_LOCAL = 2;
        public static final int CONNECTION_REINVOKE = 1;
        public static final int WPS_DISPLAY = 1;
        public static final int WPS_KEYPAD = 2;
        public static final int WPS_LABEL = 3;
        public static final int WPS_NA = -1;
        public static final int WPS_PBC = 0;
        private static volatile P2pConnectionEvent[] _emptyArray;
        public int connectionType;
        public int connectivityLevelFailureCode;
        public int durationTakenToConnectMillis;
        public long startTimeMillis;
        public int wpsMethod;

        public static P2pConnectionEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new P2pConnectionEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public P2pConnectionEvent() {
            clear();
        }

        public P2pConnectionEvent clear() {
            this.startTimeMillis = 0;
            this.connectionType = 0;
            this.wpsMethod = -1;
            this.durationTakenToConnectMillis = 0;
            this.connectivityLevelFailureCode = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.startTimeMillis != 0) {
                output.writeInt64(1, this.startTimeMillis);
            }
            if (this.connectionType != 0) {
                output.writeInt32(2, this.connectionType);
            }
            if (this.wpsMethod != -1) {
                output.writeInt32(3, this.wpsMethod);
            }
            if (this.durationTakenToConnectMillis != 0) {
                output.writeInt32(4, this.durationTakenToConnectMillis);
            }
            if (this.connectivityLevelFailureCode != 0) {
                output.writeInt32(5, this.connectivityLevelFailureCode);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.startTimeMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, this.startTimeMillis);
            }
            if (this.connectionType != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.connectionType);
            }
            if (this.wpsMethod != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.wpsMethod);
            }
            if (this.durationTakenToConnectMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.durationTakenToConnectMillis);
            }
            if (this.connectivityLevelFailureCode != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(5, this.connectivityLevelFailureCode);
            }
            return size;
        }

        public P2pConnectionEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 8) {
                    if (tag != 16) {
                        if (tag == 24) {
                            int value = input.readInt32();
                            switch (value) {
                                case -1:
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                    this.wpsMethod = value;
                                    break;
                            }
                        } else if (tag != 32) {
                            if (tag == 40) {
                                int value2 = input.readInt32();
                                switch (value2) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                        this.connectivityLevelFailureCode = value2;
                                        break;
                                }
                            } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                                return this;
                            }
                        } else {
                            this.durationTakenToConnectMillis = input.readInt32();
                        }
                    } else {
                        int value3 = input.readInt32();
                        switch (value3) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                this.connectionType = value3;
                                break;
                        }
                    }
                } else {
                    this.startTimeMillis = input.readInt64();
                }
            }
        }

        public static P2pConnectionEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (P2pConnectionEvent) MessageNano.mergeFrom(new P2pConnectionEvent(), data);
        }

        public static P2pConnectionEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new P2pConnectionEvent().mergeFrom(input);
        }
    }

    public static final class GroupEvent extends MessageNano {
        public static final int GROUP_CLIENT = 1;
        public static final int GROUP_OWNER = 0;
        private static volatile GroupEvent[] _emptyArray;
        public int channelFrequency;
        public int groupRole;
        public int idleDurationMillis;
        public int netId;
        public int numConnectedClients;
        public int numCumulativeClients;
        public int sessionDurationMillis;
        public long startTimeMillis;

        public static GroupEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new GroupEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public GroupEvent() {
            clear();
        }

        public GroupEvent clear() {
            this.netId = 0;
            this.startTimeMillis = 0;
            this.channelFrequency = 0;
            this.groupRole = 0;
            this.numConnectedClients = 0;
            this.numCumulativeClients = 0;
            this.sessionDurationMillis = 0;
            this.idleDurationMillis = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.netId != 0) {
                output.writeInt32(1, this.netId);
            }
            if (this.startTimeMillis != 0) {
                output.writeInt64(2, this.startTimeMillis);
            }
            if (this.channelFrequency != 0) {
                output.writeInt32(3, this.channelFrequency);
            }
            if (this.groupRole != 0) {
                output.writeInt32(5, this.groupRole);
            }
            if (this.numConnectedClients != 0) {
                output.writeInt32(6, this.numConnectedClients);
            }
            if (this.numCumulativeClients != 0) {
                output.writeInt32(7, this.numCumulativeClients);
            }
            if (this.sessionDurationMillis != 0) {
                output.writeInt32(8, this.sessionDurationMillis);
            }
            if (this.idleDurationMillis != 0) {
                output.writeInt32(9, this.idleDurationMillis);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.netId != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.netId);
            }
            if (this.startTimeMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, this.startTimeMillis);
            }
            if (this.channelFrequency != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.channelFrequency);
            }
            if (this.groupRole != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, this.groupRole);
            }
            if (this.numConnectedClients != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, this.numConnectedClients);
            }
            if (this.numCumulativeClients != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, this.numCumulativeClients);
            }
            if (this.sessionDurationMillis != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, this.sessionDurationMillis);
            }
            if (this.idleDurationMillis != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(9, this.idleDurationMillis);
            }
            return size;
        }

        public GroupEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.netId = input.readInt32();
                } else if (tag == 16) {
                    this.startTimeMillis = input.readInt64();
                } else if (tag != 24) {
                    if (tag == 40) {
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                                this.groupRole = value;
                                break;
                        }
                    } else if (tag == 48) {
                        this.numConnectedClients = input.readInt32();
                    } else if (tag == 56) {
                        this.numCumulativeClients = input.readInt32();
                    } else if (tag == 64) {
                        this.sessionDurationMillis = input.readInt32();
                    } else if (tag == 72) {
                        this.idleDurationMillis = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    this.channelFrequency = input.readInt32();
                }
            }
        }

        public static GroupEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (GroupEvent) MessageNano.mergeFrom(new GroupEvent(), data);
        }

        public static GroupEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new GroupEvent().mergeFrom(input);
        }
    }

    public static final class WifiDppLog extends MessageNano {
        public static final int EASY_CONNECT_EVENT_FAILURE_AUTHENTICATION = 2;
        public static final int EASY_CONNECT_EVENT_FAILURE_BUSY = 5;
        public static final int EASY_CONNECT_EVENT_FAILURE_CONFIGURATION = 4;
        public static final int EASY_CONNECT_EVENT_FAILURE_GENERIC = 7;
        public static final int EASY_CONNECT_EVENT_FAILURE_INVALID_NETWORK = 9;
        public static final int EASY_CONNECT_EVENT_FAILURE_INVALID_URI = 1;
        public static final int EASY_CONNECT_EVENT_FAILURE_NOT_COMPATIBLE = 3;
        public static final int EASY_CONNECT_EVENT_FAILURE_NOT_SUPPORTED = 8;
        public static final int EASY_CONNECT_EVENT_FAILURE_TIMEOUT = 6;
        public static final int EASY_CONNECT_EVENT_FAILURE_UNKNOWN = 0;
        public static final int EASY_CONNECT_EVENT_SUCCESS_CONFIGURATION_SENT = 1;
        public static final int EASY_CONNECT_EVENT_SUCCESS_UNKNOWN = 0;
        private static volatile WifiDppLog[] _emptyArray;
        public DppConfiguratorSuccessStatusHistogramBucket[] dppConfiguratorSuccessCode;
        public DppFailureStatusHistogramBucket[] dppFailureCode;
        public HistogramBucketInt32[] dppOperationTime;
        public int numDppConfiguratorInitiatorRequests;
        public int numDppEnrolleeInitiatorRequests;
        public int numDppEnrolleeSuccess;

        public static final class DppConfiguratorSuccessStatusHistogramBucket extends MessageNano {
            private static volatile DppConfiguratorSuccessStatusHistogramBucket[] _emptyArray;
            public int count;
            public int dppStatusType;

            public static DppConfiguratorSuccessStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new DppConfiguratorSuccessStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public DppConfiguratorSuccessStatusHistogramBucket() {
                clear();
            }

            public DppConfiguratorSuccessStatusHistogramBucket clear() {
                this.dppStatusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.dppStatusType != 0) {
                    output.writeInt32(1, this.dppStatusType);
                }
                if (this.count != 0) {
                    output.writeInt32(2, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.dppStatusType != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.dppStatusType);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
                }
                return size;
            }

            public DppConfiguratorSuccessStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                    this.dppStatusType = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static DppConfiguratorSuccessStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (DppConfiguratorSuccessStatusHistogramBucket) MessageNano.mergeFrom(new DppConfiguratorSuccessStatusHistogramBucket(), data);
            }

            public static DppConfiguratorSuccessStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new DppConfiguratorSuccessStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static final class DppFailureStatusHistogramBucket extends MessageNano {
            private static volatile DppFailureStatusHistogramBucket[] _emptyArray;
            public int count;
            public int dppStatusType;

            public static DppFailureStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new DppFailureStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public DppFailureStatusHistogramBucket() {
                clear();
            }

            public DppFailureStatusHistogramBucket clear() {
                this.dppStatusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.dppStatusType != 0) {
                    output.writeInt32(1, this.dppStatusType);
                }
                if (this.count != 0) {
                    output.writeInt32(2, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.dppStatusType != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.dppStatusType);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
                }
                return size;
            }

            public DppFailureStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                    this.dppStatusType = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static DppFailureStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (DppFailureStatusHistogramBucket) MessageNano.mergeFrom(new DppFailureStatusHistogramBucket(), data);
            }

            public static DppFailureStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new DppFailureStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static WifiDppLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiDppLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiDppLog() {
            clear();
        }

        public WifiDppLog clear() {
            this.numDppConfiguratorInitiatorRequests = 0;
            this.numDppEnrolleeInitiatorRequests = 0;
            this.numDppEnrolleeSuccess = 0;
            this.dppConfiguratorSuccessCode = DppConfiguratorSuccessStatusHistogramBucket.emptyArray();
            this.dppFailureCode = DppFailureStatusHistogramBucket.emptyArray();
            this.dppOperationTime = HistogramBucketInt32.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numDppConfiguratorInitiatorRequests != 0) {
                output.writeInt32(1, this.numDppConfiguratorInitiatorRequests);
            }
            if (this.numDppEnrolleeInitiatorRequests != 0) {
                output.writeInt32(2, this.numDppEnrolleeInitiatorRequests);
            }
            if (this.numDppEnrolleeSuccess != 0) {
                output.writeInt32(3, this.numDppEnrolleeSuccess);
            }
            int i = 0;
            if (this.dppConfiguratorSuccessCode != null && this.dppConfiguratorSuccessCode.length > 0) {
                for (DppConfiguratorSuccessStatusHistogramBucket element : this.dppConfiguratorSuccessCode) {
                    if (element != null) {
                        output.writeMessage(4, element);
                    }
                }
            }
            if (this.dppFailureCode != null && this.dppFailureCode.length > 0) {
                for (DppFailureStatusHistogramBucket element2 : this.dppFailureCode) {
                    if (element2 != null) {
                        output.writeMessage(5, element2);
                    }
                }
            }
            if (this.dppOperationTime != null && this.dppOperationTime.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.dppOperationTime.length) {
                        break;
                    }
                    HistogramBucketInt32 element3 = this.dppOperationTime[i2];
                    if (element3 != null) {
                        output.writeMessage(7, element3);
                    }
                    i = i2 + 1;
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numDppConfiguratorInitiatorRequests != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numDppConfiguratorInitiatorRequests);
            }
            if (this.numDppEnrolleeInitiatorRequests != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.numDppEnrolleeInitiatorRequests);
            }
            if (this.numDppEnrolleeSuccess != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numDppEnrolleeSuccess);
            }
            int i = 0;
            if (this.dppConfiguratorSuccessCode != null && this.dppConfiguratorSuccessCode.length > 0) {
                int size2 = size;
                for (DppConfiguratorSuccessStatusHistogramBucket element : this.dppConfiguratorSuccessCode) {
                    if (element != null) {
                        size2 += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                }
                size = size2;
            }
            if (this.dppFailureCode != null && this.dppFailureCode.length > 0) {
                int size3 = size;
                for (DppFailureStatusHistogramBucket element2 : this.dppFailureCode) {
                    if (element2 != null) {
                        size3 += CodedOutputByteBufferNano.computeMessageSize(5, element2);
                    }
                }
                size = size3;
            }
            if (this.dppOperationTime != null && this.dppOperationTime.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.dppOperationTime.length) {
                        break;
                    }
                    HistogramBucketInt32 element3 = this.dppOperationTime[i2];
                    if (element3 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(7, element3);
                    }
                    i = i2 + 1;
                }
            }
            return size;
        }

        public WifiDppLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numDppConfiguratorInitiatorRequests = input.readInt32();
                } else if (tag == 16) {
                    this.numDppEnrolleeInitiatorRequests = input.readInt32();
                } else if (tag == 24) {
                    this.numDppEnrolleeSuccess = input.readInt32();
                } else if (tag == 34) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int i = this.dppConfiguratorSuccessCode == null ? 0 : this.dppConfiguratorSuccessCode.length;
                    DppConfiguratorSuccessStatusHistogramBucket[] newArray = new DppConfiguratorSuccessStatusHistogramBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.dppConfiguratorSuccessCode, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new DppConfiguratorSuccessStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new DppConfiguratorSuccessStatusHistogramBucket();
                    input.readMessage(newArray[i]);
                    this.dppConfiguratorSuccessCode = newArray;
                } else if (tag == 42) {
                    int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    int i2 = this.dppFailureCode == null ? 0 : this.dppFailureCode.length;
                    DppFailureStatusHistogramBucket[] newArray2 = new DppFailureStatusHistogramBucket[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.dppFailureCode, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length - 1) {
                        newArray2[i2] = new DppFailureStatusHistogramBucket();
                        input.readMessage(newArray2[i2]);
                        input.readTag();
                        i2++;
                    }
                    newArray2[i2] = new DppFailureStatusHistogramBucket();
                    input.readMessage(newArray2[i2]);
                    this.dppFailureCode = newArray2;
                } else if (tag == 58) {
                    int arrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                    int i3 = this.dppOperationTime == null ? 0 : this.dppOperationTime.length;
                    HistogramBucketInt32[] newArray3 = new HistogramBucketInt32[(i3 + arrayLength3)];
                    if (i3 != 0) {
                        System.arraycopy(this.dppOperationTime, 0, newArray3, 0, i3);
                    }
                    while (i3 < newArray3.length - 1) {
                        newArray3[i3] = new HistogramBucketInt32();
                        input.readMessage(newArray3[i3]);
                        input.readTag();
                        i3++;
                    }
                    newArray3[i3] = new HistogramBucketInt32();
                    input.readMessage(newArray3[i3]);
                    this.dppOperationTime = newArray3;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiDppLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiDppLog) MessageNano.mergeFrom(new WifiDppLog(), data);
        }

        public static WifiDppLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiDppLog().mergeFrom(input);
        }
    }

    public static final class WifiConfigStoreIO extends MessageNano {
        private static volatile WifiConfigStoreIO[] _emptyArray;
        public DurationBucket[] readDurations;
        public DurationBucket[] writeDurations;

        public static final class DurationBucket extends MessageNano {
            private static volatile DurationBucket[] _emptyArray;
            public int count;
            public int rangeEndMs;
            public int rangeStartMs;

            public static DurationBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new DurationBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public DurationBucket() {
                clear();
            }

            public DurationBucket clear() {
                this.rangeStartMs = 0;
                this.rangeEndMs = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.rangeStartMs != 0) {
                    output.writeInt32(1, this.rangeStartMs);
                }
                if (this.rangeEndMs != 0) {
                    output.writeInt32(2, this.rangeEndMs);
                }
                if (this.count != 0) {
                    output.writeInt32(3, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.rangeStartMs != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.rangeStartMs);
                }
                if (this.rangeEndMs != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, this.rangeEndMs);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(3, this.count);
                }
                return size;
            }

            public DurationBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.rangeStartMs = input.readInt32();
                    } else if (tag == 16) {
                        this.rangeEndMs = input.readInt32();
                    } else if (tag == 24) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static DurationBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (DurationBucket) MessageNano.mergeFrom(new DurationBucket(), data);
            }

            public static DurationBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new DurationBucket().mergeFrom(input);
            }
        }

        public static WifiConfigStoreIO[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiConfigStoreIO[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiConfigStoreIO() {
            clear();
        }

        public WifiConfigStoreIO clear() {
            this.readDurations = DurationBucket.emptyArray();
            this.writeDurations = DurationBucket.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = 0;
            if (this.readDurations != null && this.readDurations.length > 0) {
                for (DurationBucket element : this.readDurations) {
                    if (element != null) {
                        output.writeMessage(1, element);
                    }
                }
            }
            if (this.writeDurations != null && this.writeDurations.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.writeDurations.length) {
                        break;
                    }
                    DurationBucket element2 = this.writeDurations[i2];
                    if (element2 != null) {
                        output.writeMessage(2, element2);
                    }
                    i = i2 + 1;
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int i = super.computeSerializedSize();
            int i2 = 0;
            if (this.readDurations != null && this.readDurations.length > 0) {
                int size = i;
                for (DurationBucket element : this.readDurations) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(1, element);
                    }
                }
                i = size;
            }
            if (this.writeDurations != null && this.writeDurations.length > 0) {
                while (true) {
                    int i3 = i2;
                    if (i3 >= this.writeDurations.length) {
                        break;
                    }
                    DurationBucket element2 = this.writeDurations[i3];
                    if (element2 != null) {
                        i += CodedOutputByteBufferNano.computeMessageSize(2, element2);
                    }
                    i2 = i3 + 1;
                }
            }
            return i;
        }

        public WifiConfigStoreIO mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    int i = this.readDurations == null ? 0 : this.readDurations.length;
                    DurationBucket[] newArray = new DurationBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.readDurations, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new DurationBucket();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new DurationBucket();
                    input.readMessage(newArray[i]);
                    this.readDurations = newArray;
                } else if (tag == 18) {
                    int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int i2 = this.writeDurations == null ? 0 : this.writeDurations.length;
                    DurationBucket[] newArray2 = new DurationBucket[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.writeDurations, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length - 1) {
                        newArray2[i2] = new DurationBucket();
                        input.readMessage(newArray2[i2]);
                        input.readTag();
                        i2++;
                    }
                    newArray2[i2] = new DurationBucket();
                    input.readMessage(newArray2[i2]);
                    this.writeDurations = newArray2;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiConfigStoreIO parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiConfigStoreIO) MessageNano.mergeFrom(new WifiConfigStoreIO(), data);
        }

        public static WifiConfigStoreIO parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiConfigStoreIO().mergeFrom(input);
        }
    }

    public static final class HistogramBucketInt32 extends MessageNano {
        private static volatile HistogramBucketInt32[] _emptyArray;
        public int count;
        public int end;
        public int start;

        public static HistogramBucketInt32[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new HistogramBucketInt32[0];
                    }
                }
            }
            return _emptyArray;
        }

        public HistogramBucketInt32() {
            clear();
        }

        public HistogramBucketInt32 clear() {
            this.start = 0;
            this.end = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.start != 0) {
                output.writeInt32(1, this.start);
            }
            if (this.end != 0) {
                output.writeInt32(2, this.end);
            }
            if (this.count != 0) {
                output.writeInt32(3, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.start != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.start);
            }
            if (this.end != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.end);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, this.count);
            }
            return size;
        }

        public HistogramBucketInt32 mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.start = input.readInt32();
                } else if (tag == 16) {
                    this.end = input.readInt32();
                } else if (tag == 24) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static HistogramBucketInt32 parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (HistogramBucketInt32) MessageNano.mergeFrom(new HistogramBucketInt32(), data);
        }

        public static HistogramBucketInt32 parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new HistogramBucketInt32().mergeFrom(input);
        }
    }

    public static final class Int32Count extends MessageNano {
        private static volatile Int32Count[] _emptyArray;
        public int count;
        public int key;

        public static Int32Count[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new Int32Count[0];
                    }
                }
            }
            return _emptyArray;
        }

        public Int32Count() {
            clear();
        }

        public Int32Count clear() {
            this.key = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.key != 0) {
                output.writeInt32(1, this.key);
            }
            if (this.count != 0) {
                output.writeInt32(2, this.count);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.key != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.key);
            }
            if (this.count != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
            }
            return size;
        }

        public Int32Count mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.key = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static Int32Count parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (Int32Count) MessageNano.mergeFrom(new Int32Count(), data);
        }

        public static Int32Count parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new Int32Count().mergeFrom(input);
        }
    }

    public static final class LinkProbeStats extends MessageNano {
        public static final int LINK_PROBE_FAILURE_REASON_ALREADY_STARTED = 4;
        public static final int LINK_PROBE_FAILURE_REASON_MCS_UNSUPPORTED = 1;
        public static final int LINK_PROBE_FAILURE_REASON_NO_ACK = 2;
        public static final int LINK_PROBE_FAILURE_REASON_TIMEOUT = 3;
        public static final int LINK_PROBE_FAILURE_REASON_UNKNOWN = 0;
        private static volatile LinkProbeStats[] _emptyArray;
        public ExperimentProbeCounts[] experimentProbeCounts;
        public Int32Count[] failureLinkSpeedCounts;
        public LinkProbeFailureReasonCount[] failureReasonCounts;
        public Int32Count[] failureRssiCounts;
        public HistogramBucketInt32[] failureSecondsSinceLastTxSuccessHistogram;
        public HistogramBucketInt32[] successElapsedTimeMsHistogram;
        public Int32Count[] successLinkSpeedCounts;
        public Int32Count[] successRssiCounts;
        public HistogramBucketInt32[] successSecondsSinceLastTxSuccessHistogram;

        public static final class LinkProbeFailureReasonCount extends MessageNano {
            private static volatile LinkProbeFailureReasonCount[] _emptyArray;
            public int count;
            public int failureReason;

            public static LinkProbeFailureReasonCount[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new LinkProbeFailureReasonCount[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public LinkProbeFailureReasonCount() {
                clear();
            }

            public LinkProbeFailureReasonCount clear() {
                this.failureReason = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.failureReason != 0) {
                    output.writeInt32(1, this.failureReason);
                }
                if (this.count != 0) {
                    output.writeInt32(2, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.failureReason != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.failureReason);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
                }
                return size;
            }

            public LinkProbeFailureReasonCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    this.failureReason = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static LinkProbeFailureReasonCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (LinkProbeFailureReasonCount) MessageNano.mergeFrom(new LinkProbeFailureReasonCount(), data);
            }

            public static LinkProbeFailureReasonCount parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new LinkProbeFailureReasonCount().mergeFrom(input);
            }
        }

        public static final class ExperimentProbeCounts extends MessageNano {
            private static volatile ExperimentProbeCounts[] _emptyArray;
            public String experimentId;
            public int probeCount;

            public static ExperimentProbeCounts[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ExperimentProbeCounts[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ExperimentProbeCounts() {
                clear();
            }

            public ExperimentProbeCounts clear() {
                this.experimentId = "";
                this.probeCount = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (!this.experimentId.equals("")) {
                    output.writeString(1, this.experimentId);
                }
                if (this.probeCount != 0) {
                    output.writeInt32(2, this.probeCount);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (!this.experimentId.equals("")) {
                    size += CodedOutputByteBufferNano.computeStringSize(1, this.experimentId);
                }
                if (this.probeCount != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.probeCount);
                }
                return size;
            }

            public ExperimentProbeCounts mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 10) {
                        this.experimentId = input.readString();
                    } else if (tag == 16) {
                        this.probeCount = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static ExperimentProbeCounts parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ExperimentProbeCounts) MessageNano.mergeFrom(new ExperimentProbeCounts(), data);
            }

            public static ExperimentProbeCounts parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ExperimentProbeCounts().mergeFrom(input);
            }
        }

        public static LinkProbeStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new LinkProbeStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public LinkProbeStats() {
            clear();
        }

        public LinkProbeStats clear() {
            this.successRssiCounts = Int32Count.emptyArray();
            this.failureRssiCounts = Int32Count.emptyArray();
            this.successLinkSpeedCounts = Int32Count.emptyArray();
            this.failureLinkSpeedCounts = Int32Count.emptyArray();
            this.successSecondsSinceLastTxSuccessHistogram = HistogramBucketInt32.emptyArray();
            this.failureSecondsSinceLastTxSuccessHistogram = HistogramBucketInt32.emptyArray();
            this.successElapsedTimeMsHistogram = HistogramBucketInt32.emptyArray();
            this.failureReasonCounts = LinkProbeFailureReasonCount.emptyArray();
            this.experimentProbeCounts = ExperimentProbeCounts.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = 0;
            if (this.successRssiCounts != null && this.successRssiCounts.length > 0) {
                for (Int32Count element : this.successRssiCounts) {
                    if (element != null) {
                        output.writeMessage(1, element);
                    }
                }
            }
            if (this.failureRssiCounts != null && this.failureRssiCounts.length > 0) {
                for (Int32Count element2 : this.failureRssiCounts) {
                    if (element2 != null) {
                        output.writeMessage(2, element2);
                    }
                }
            }
            if (this.successLinkSpeedCounts != null && this.successLinkSpeedCounts.length > 0) {
                for (Int32Count element3 : this.successLinkSpeedCounts) {
                    if (element3 != null) {
                        output.writeMessage(3, element3);
                    }
                }
            }
            if (this.failureLinkSpeedCounts != null && this.failureLinkSpeedCounts.length > 0) {
                for (Int32Count element4 : this.failureLinkSpeedCounts) {
                    if (element4 != null) {
                        output.writeMessage(4, element4);
                    }
                }
            }
            if (this.successSecondsSinceLastTxSuccessHistogram != null && this.successSecondsSinceLastTxSuccessHistogram.length > 0) {
                for (HistogramBucketInt32 element5 : this.successSecondsSinceLastTxSuccessHistogram) {
                    if (element5 != null) {
                        output.writeMessage(5, element5);
                    }
                }
            }
            if (this.failureSecondsSinceLastTxSuccessHistogram != null && this.failureSecondsSinceLastTxSuccessHistogram.length > 0) {
                for (HistogramBucketInt32 element6 : this.failureSecondsSinceLastTxSuccessHistogram) {
                    if (element6 != null) {
                        output.writeMessage(6, element6);
                    }
                }
            }
            if (this.successElapsedTimeMsHistogram != null && this.successElapsedTimeMsHistogram.length > 0) {
                for (HistogramBucketInt32 element7 : this.successElapsedTimeMsHistogram) {
                    if (element7 != null) {
                        output.writeMessage(7, element7);
                    }
                }
            }
            if (this.failureReasonCounts != null && this.failureReasonCounts.length > 0) {
                for (LinkProbeFailureReasonCount element8 : this.failureReasonCounts) {
                    if (element8 != null) {
                        output.writeMessage(8, element8);
                    }
                }
            }
            if (this.experimentProbeCounts != null && this.experimentProbeCounts.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.experimentProbeCounts.length) {
                        break;
                    }
                    ExperimentProbeCounts element9 = this.experimentProbeCounts[i2];
                    if (element9 != null) {
                        output.writeMessage(9, element9);
                    }
                    i = i2 + 1;
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int i = super.computeSerializedSize();
            int i2 = 0;
            if (this.successRssiCounts != null && this.successRssiCounts.length > 0) {
                int size = i;
                for (Int32Count element : this.successRssiCounts) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(1, element);
                    }
                }
                i = size;
            }
            if (this.failureRssiCounts != null && this.failureRssiCounts.length > 0) {
                int size2 = i;
                for (Int32Count element2 : this.failureRssiCounts) {
                    if (element2 != null) {
                        size2 += CodedOutputByteBufferNano.computeMessageSize(2, element2);
                    }
                }
                i = size2;
            }
            if (this.successLinkSpeedCounts != null && this.successLinkSpeedCounts.length > 0) {
                int size3 = i;
                for (Int32Count element3 : this.successLinkSpeedCounts) {
                    if (element3 != null) {
                        size3 += CodedOutputByteBufferNano.computeMessageSize(3, element3);
                    }
                }
                i = size3;
            }
            if (this.failureLinkSpeedCounts != null && this.failureLinkSpeedCounts.length > 0) {
                int size4 = i;
                for (Int32Count element4 : this.failureLinkSpeedCounts) {
                    if (element4 != null) {
                        size4 += CodedOutputByteBufferNano.computeMessageSize(4, element4);
                    }
                }
                i = size4;
            }
            if (this.successSecondsSinceLastTxSuccessHistogram != null && this.successSecondsSinceLastTxSuccessHistogram.length > 0) {
                int size5 = i;
                for (HistogramBucketInt32 element5 : this.successSecondsSinceLastTxSuccessHistogram) {
                    if (element5 != null) {
                        size5 += CodedOutputByteBufferNano.computeMessageSize(5, element5);
                    }
                }
                i = size5;
            }
            if (this.failureSecondsSinceLastTxSuccessHistogram != null && this.failureSecondsSinceLastTxSuccessHistogram.length > 0) {
                int size6 = i;
                for (HistogramBucketInt32 element6 : this.failureSecondsSinceLastTxSuccessHistogram) {
                    if (element6 != null) {
                        size6 += CodedOutputByteBufferNano.computeMessageSize(6, element6);
                    }
                }
                i = size6;
            }
            if (this.successElapsedTimeMsHistogram != null && this.successElapsedTimeMsHistogram.length > 0) {
                int size7 = i;
                for (HistogramBucketInt32 element7 : this.successElapsedTimeMsHistogram) {
                    if (element7 != null) {
                        size7 += CodedOutputByteBufferNano.computeMessageSize(7, element7);
                    }
                }
                i = size7;
            }
            if (this.failureReasonCounts != null && this.failureReasonCounts.length > 0) {
                int size8 = i;
                for (LinkProbeFailureReasonCount element8 : this.failureReasonCounts) {
                    if (element8 != null) {
                        size8 += CodedOutputByteBufferNano.computeMessageSize(8, element8);
                    }
                }
                i = size8;
            }
            if (this.experimentProbeCounts != null && this.experimentProbeCounts.length > 0) {
                while (true) {
                    int i3 = i2;
                    if (i3 >= this.experimentProbeCounts.length) {
                        break;
                    }
                    ExperimentProbeCounts element9 = this.experimentProbeCounts[i3];
                    if (element9 != null) {
                        i += CodedOutputByteBufferNano.computeMessageSize(9, element9);
                    }
                    i2 = i3 + 1;
                }
            }
            return i;
        }

        public LinkProbeStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    int i = this.successRssiCounts == null ? 0 : this.successRssiCounts.length;
                    Int32Count[] newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.successRssiCounts, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.successRssiCounts = newArray;
                } else if (tag == 18) {
                    int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int i2 = this.failureRssiCounts == null ? 0 : this.failureRssiCounts.length;
                    Int32Count[] newArray2 = new Int32Count[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.failureRssiCounts, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length - 1) {
                        newArray2[i2] = new Int32Count();
                        input.readMessage(newArray2[i2]);
                        input.readTag();
                        i2++;
                    }
                    newArray2[i2] = new Int32Count();
                    input.readMessage(newArray2[i2]);
                    this.failureRssiCounts = newArray2;
                } else if (tag == 26) {
                    int arrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                    int i3 = this.successLinkSpeedCounts == null ? 0 : this.successLinkSpeedCounts.length;
                    Int32Count[] newArray3 = new Int32Count[(i3 + arrayLength3)];
                    if (i3 != 0) {
                        System.arraycopy(this.successLinkSpeedCounts, 0, newArray3, 0, i3);
                    }
                    while (i3 < newArray3.length - 1) {
                        newArray3[i3] = new Int32Count();
                        input.readMessage(newArray3[i3]);
                        input.readTag();
                        i3++;
                    }
                    newArray3[i3] = new Int32Count();
                    input.readMessage(newArray3[i3]);
                    this.successLinkSpeedCounts = newArray3;
                } else if (tag == 34) {
                    int arrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int i4 = this.failureLinkSpeedCounts == null ? 0 : this.failureLinkSpeedCounts.length;
                    Int32Count[] newArray4 = new Int32Count[(i4 + arrayLength4)];
                    if (i4 != 0) {
                        System.arraycopy(this.failureLinkSpeedCounts, 0, newArray4, 0, i4);
                    }
                    while (i4 < newArray4.length - 1) {
                        newArray4[i4] = new Int32Count();
                        input.readMessage(newArray4[i4]);
                        input.readTag();
                        i4++;
                    }
                    newArray4[i4] = new Int32Count();
                    input.readMessage(newArray4[i4]);
                    this.failureLinkSpeedCounts = newArray4;
                } else if (tag == 42) {
                    int arrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    int i5 = this.successSecondsSinceLastTxSuccessHistogram == null ? 0 : this.successSecondsSinceLastTxSuccessHistogram.length;
                    HistogramBucketInt32[] newArray5 = new HistogramBucketInt32[(i5 + arrayLength5)];
                    if (i5 != 0) {
                        System.arraycopy(this.successSecondsSinceLastTxSuccessHistogram, 0, newArray5, 0, i5);
                    }
                    while (i5 < newArray5.length - 1) {
                        newArray5[i5] = new HistogramBucketInt32();
                        input.readMessage(newArray5[i5]);
                        input.readTag();
                        i5++;
                    }
                    newArray5[i5] = new HistogramBucketInt32();
                    input.readMessage(newArray5[i5]);
                    this.successSecondsSinceLastTxSuccessHistogram = newArray5;
                } else if (tag == 50) {
                    int arrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                    int i6 = this.failureSecondsSinceLastTxSuccessHistogram == null ? 0 : this.failureSecondsSinceLastTxSuccessHistogram.length;
                    HistogramBucketInt32[] newArray6 = new HistogramBucketInt32[(i6 + arrayLength6)];
                    if (i6 != 0) {
                        System.arraycopy(this.failureSecondsSinceLastTxSuccessHistogram, 0, newArray6, 0, i6);
                    }
                    while (i6 < newArray6.length - 1) {
                        newArray6[i6] = new HistogramBucketInt32();
                        input.readMessage(newArray6[i6]);
                        input.readTag();
                        i6++;
                    }
                    newArray6[i6] = new HistogramBucketInt32();
                    input.readMessage(newArray6[i6]);
                    this.failureSecondsSinceLastTxSuccessHistogram = newArray6;
                } else if (tag == 58) {
                    int arrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                    int i7 = this.successElapsedTimeMsHistogram == null ? 0 : this.successElapsedTimeMsHistogram.length;
                    HistogramBucketInt32[] newArray7 = new HistogramBucketInt32[(i7 + arrayLength7)];
                    if (i7 != 0) {
                        System.arraycopy(this.successElapsedTimeMsHistogram, 0, newArray7, 0, i7);
                    }
                    while (i7 < newArray7.length - 1) {
                        newArray7[i7] = new HistogramBucketInt32();
                        input.readMessage(newArray7[i7]);
                        input.readTag();
                        i7++;
                    }
                    newArray7[i7] = new HistogramBucketInt32();
                    input.readMessage(newArray7[i7]);
                    this.successElapsedTimeMsHistogram = newArray7;
                } else if (tag == 66) {
                    int arrayLength8 = WireFormatNano.getRepeatedFieldArrayLength(input, 66);
                    int i8 = this.failureReasonCounts == null ? 0 : this.failureReasonCounts.length;
                    LinkProbeFailureReasonCount[] newArray8 = new LinkProbeFailureReasonCount[(i8 + arrayLength8)];
                    if (i8 != 0) {
                        System.arraycopy(this.failureReasonCounts, 0, newArray8, 0, i8);
                    }
                    while (i8 < newArray8.length - 1) {
                        newArray8[i8] = new LinkProbeFailureReasonCount();
                        input.readMessage(newArray8[i8]);
                        input.readTag();
                        i8++;
                    }
                    newArray8[i8] = new LinkProbeFailureReasonCount();
                    input.readMessage(newArray8[i8]);
                    this.failureReasonCounts = newArray8;
                } else if (tag == 74) {
                    int arrayLength9 = WireFormatNano.getRepeatedFieldArrayLength(input, 74);
                    int i9 = this.experimentProbeCounts == null ? 0 : this.experimentProbeCounts.length;
                    ExperimentProbeCounts[] newArray9 = new ExperimentProbeCounts[(i9 + arrayLength9)];
                    if (i9 != 0) {
                        System.arraycopy(this.experimentProbeCounts, 0, newArray9, 0, i9);
                    }
                    while (i9 < newArray9.length - 1) {
                        newArray9[i9] = new ExperimentProbeCounts();
                        input.readMessage(newArray9[i9]);
                        input.readTag();
                        i9++;
                    }
                    newArray9[i9] = new ExperimentProbeCounts();
                    input.readMessage(newArray9[i9]);
                    this.experimentProbeCounts = newArray9;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static LinkProbeStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (LinkProbeStats) MessageNano.mergeFrom(new LinkProbeStats(), data);
        }

        public static LinkProbeStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new LinkProbeStats().mergeFrom(input);
        }
    }

    public static final class NetworkSelectionExperimentDecisions extends MessageNano {
        private static volatile NetworkSelectionExperimentDecisions[] _emptyArray;
        public Int32Count[] differentSelectionNumChoicesCounter;
        public int experiment1Id;
        public int experiment2Id;
        public Int32Count[] sameSelectionNumChoicesCounter;

        public static NetworkSelectionExperimentDecisions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NetworkSelectionExperimentDecisions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NetworkSelectionExperimentDecisions() {
            clear();
        }

        public NetworkSelectionExperimentDecisions clear() {
            this.experiment1Id = 0;
            this.experiment2Id = 0;
            this.sameSelectionNumChoicesCounter = Int32Count.emptyArray();
            this.differentSelectionNumChoicesCounter = Int32Count.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.experiment1Id != 0) {
                output.writeInt32(1, this.experiment1Id);
            }
            if (this.experiment2Id != 0) {
                output.writeInt32(2, this.experiment2Id);
            }
            int i = 0;
            if (this.sameSelectionNumChoicesCounter != null && this.sameSelectionNumChoicesCounter.length > 0) {
                for (Int32Count element : this.sameSelectionNumChoicesCounter) {
                    if (element != null) {
                        output.writeMessage(3, element);
                    }
                }
            }
            if (this.differentSelectionNumChoicesCounter != null && this.differentSelectionNumChoicesCounter.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.differentSelectionNumChoicesCounter.length) {
                        break;
                    }
                    Int32Count element2 = this.differentSelectionNumChoicesCounter[i2];
                    if (element2 != null) {
                        output.writeMessage(4, element2);
                    }
                    i = i2 + 1;
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.experiment1Id != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.experiment1Id);
            }
            if (this.experiment2Id != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.experiment2Id);
            }
            int i = 0;
            if (this.sameSelectionNumChoicesCounter != null && this.sameSelectionNumChoicesCounter.length > 0) {
                int size2 = size;
                for (Int32Count element : this.sameSelectionNumChoicesCounter) {
                    if (element != null) {
                        size2 += CodedOutputByteBufferNano.computeMessageSize(3, element);
                    }
                }
                size = size2;
            }
            if (this.differentSelectionNumChoicesCounter != null && this.differentSelectionNumChoicesCounter.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.differentSelectionNumChoicesCounter.length) {
                        break;
                    }
                    Int32Count element2 = this.differentSelectionNumChoicesCounter[i2];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element2);
                    }
                    i = i2 + 1;
                }
            }
            return size;
        }

        public NetworkSelectionExperimentDecisions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.experiment1Id = input.readInt32();
                } else if (tag == 16) {
                    this.experiment2Id = input.readInt32();
                } else if (tag == 26) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                    int i = this.sameSelectionNumChoicesCounter == null ? 0 : this.sameSelectionNumChoicesCounter.length;
                    Int32Count[] newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.sameSelectionNumChoicesCounter, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.sameSelectionNumChoicesCounter = newArray;
                } else if (tag == 34) {
                    int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int i2 = this.differentSelectionNumChoicesCounter == null ? 0 : this.differentSelectionNumChoicesCounter.length;
                    Int32Count[] newArray2 = new Int32Count[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.differentSelectionNumChoicesCounter, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length - 1) {
                        newArray2[i2] = new Int32Count();
                        input.readMessage(newArray2[i2]);
                        input.readTag();
                        i2++;
                    }
                    newArray2[i2] = new Int32Count();
                    input.readMessage(newArray2[i2]);
                    this.differentSelectionNumChoicesCounter = newArray2;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static NetworkSelectionExperimentDecisions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (NetworkSelectionExperimentDecisions) MessageNano.mergeFrom(new NetworkSelectionExperimentDecisions(), data);
        }

        public static NetworkSelectionExperimentDecisions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new NetworkSelectionExperimentDecisions().mergeFrom(input);
        }
    }

    public static final class WifiNetworkRequestApiLog extends MessageNano {
        private static volatile WifiNetworkRequestApiLog[] _emptyArray;
        public HistogramBucketInt32[] networkMatchSizeHistogram;
        public int numApps;
        public int numConnectSuccess;
        public int numRequest;
        public int numUserApprovalBypass;
        public int numUserReject;

        public static WifiNetworkRequestApiLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiNetworkRequestApiLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiNetworkRequestApiLog() {
            clear();
        }

        public WifiNetworkRequestApiLog clear() {
            this.numRequest = 0;
            this.networkMatchSizeHistogram = HistogramBucketInt32.emptyArray();
            this.numConnectSuccess = 0;
            this.numUserApprovalBypass = 0;
            this.numUserReject = 0;
            this.numApps = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numRequest != 0) {
                output.writeInt32(1, this.numRequest);
            }
            if (this.networkMatchSizeHistogram != null && this.networkMatchSizeHistogram.length > 0) {
                for (HistogramBucketInt32 element : this.networkMatchSizeHistogram) {
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                }
            }
            if (this.numConnectSuccess != 0) {
                output.writeInt32(3, this.numConnectSuccess);
            }
            if (this.numUserApprovalBypass != 0) {
                output.writeInt32(4, this.numUserApprovalBypass);
            }
            if (this.numUserReject != 0) {
                output.writeInt32(5, this.numUserReject);
            }
            if (this.numApps != 0) {
                output.writeInt32(6, this.numApps);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numRequest != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numRequest);
            }
            if (this.networkMatchSizeHistogram != null && this.networkMatchSizeHistogram.length > 0) {
                for (HistogramBucketInt32 element : this.networkMatchSizeHistogram) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                }
            }
            if (this.numConnectSuccess != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numConnectSuccess);
            }
            if (this.numUserApprovalBypass != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, this.numUserApprovalBypass);
            }
            if (this.numUserReject != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, this.numUserReject);
            }
            if (this.numApps != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(6, this.numApps);
            }
            return size;
        }

        public WifiNetworkRequestApiLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numRequest = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int i = this.networkMatchSizeHistogram == null ? 0 : this.networkMatchSizeHistogram.length;
                    HistogramBucketInt32[] newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.networkMatchSizeHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.networkMatchSizeHistogram = newArray;
                } else if (tag == 24) {
                    this.numConnectSuccess = input.readInt32();
                } else if (tag == 32) {
                    this.numUserApprovalBypass = input.readInt32();
                } else if (tag == 40) {
                    this.numUserReject = input.readInt32();
                } else if (tag == 48) {
                    this.numApps = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiNetworkRequestApiLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiNetworkRequestApiLog) MessageNano.mergeFrom(new WifiNetworkRequestApiLog(), data);
        }

        public static WifiNetworkRequestApiLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiNetworkRequestApiLog().mergeFrom(input);
        }
    }

    public static final class WifiNetworkSuggestionApiLog extends MessageNano {
        private static volatile WifiNetworkSuggestionApiLog[] _emptyArray;
        public HistogramBucketInt32[] networkListSizeHistogram;
        public int numConnectFailure;
        public int numConnectSuccess;
        public int numModification;

        public static WifiNetworkSuggestionApiLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiNetworkSuggestionApiLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiNetworkSuggestionApiLog() {
            clear();
        }

        public WifiNetworkSuggestionApiLog clear() {
            this.numModification = 0;
            this.numConnectSuccess = 0;
            this.numConnectFailure = 0;
            this.networkListSizeHistogram = HistogramBucketInt32.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numModification != 0) {
                output.writeInt32(1, this.numModification);
            }
            if (this.numConnectSuccess != 0) {
                output.writeInt32(2, this.numConnectSuccess);
            }
            if (this.numConnectFailure != 0) {
                output.writeInt32(3, this.numConnectFailure);
            }
            if (this.networkListSizeHistogram != null && this.networkListSizeHistogram.length > 0) {
                for (HistogramBucketInt32 element : this.networkListSizeHistogram) {
                    if (element != null) {
                        output.writeMessage(4, element);
                    }
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numModification != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numModification);
            }
            if (this.numConnectSuccess != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.numConnectSuccess);
            }
            if (this.numConnectFailure != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numConnectFailure);
            }
            if (this.networkListSizeHistogram != null && this.networkListSizeHistogram.length > 0) {
                for (HistogramBucketInt32 element : this.networkListSizeHistogram) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                }
            }
            return size;
        }

        public WifiNetworkSuggestionApiLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numModification = input.readInt32();
                } else if (tag == 16) {
                    this.numConnectSuccess = input.readInt32();
                } else if (tag == 24) {
                    this.numConnectFailure = input.readInt32();
                } else if (tag == 34) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int i = this.networkListSizeHistogram == null ? 0 : this.networkListSizeHistogram.length;
                    HistogramBucketInt32[] newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.networkListSizeHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.networkListSizeHistogram = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiNetworkSuggestionApiLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiNetworkSuggestionApiLog) MessageNano.mergeFrom(new WifiNetworkSuggestionApiLog(), data);
        }

        public static WifiNetworkSuggestionApiLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiNetworkSuggestionApiLog().mergeFrom(input);
        }
    }

    public static final class WifiLockStats extends MessageNano {
        private static volatile WifiLockStats[] _emptyArray;
        public HistogramBucketInt32[] highPerfActiveSessionDurationSecHistogram;
        public long highPerfActiveTimeMs;
        public HistogramBucketInt32[] highPerfLockAcqDurationSecHistogram;
        public HistogramBucketInt32[] lowLatencyActiveSessionDurationSecHistogram;
        public long lowLatencyActiveTimeMs;
        public HistogramBucketInt32[] lowLatencyLockAcqDurationSecHistogram;

        public static WifiLockStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiLockStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiLockStats() {
            clear();
        }

        public WifiLockStats clear() {
            this.highPerfActiveTimeMs = 0;
            this.lowLatencyActiveTimeMs = 0;
            this.highPerfLockAcqDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.lowLatencyLockAcqDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.highPerfActiveSessionDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.lowLatencyActiveSessionDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.highPerfActiveTimeMs != 0) {
                output.writeInt64(1, this.highPerfActiveTimeMs);
            }
            if (this.lowLatencyActiveTimeMs != 0) {
                output.writeInt64(2, this.lowLatencyActiveTimeMs);
            }
            int i = 0;
            if (this.highPerfLockAcqDurationSecHistogram != null && this.highPerfLockAcqDurationSecHistogram.length > 0) {
                for (HistogramBucketInt32 element : this.highPerfLockAcqDurationSecHistogram) {
                    if (element != null) {
                        output.writeMessage(3, element);
                    }
                }
            }
            if (this.lowLatencyLockAcqDurationSecHistogram != null && this.lowLatencyLockAcqDurationSecHistogram.length > 0) {
                for (HistogramBucketInt32 element2 : this.lowLatencyLockAcqDurationSecHistogram) {
                    if (element2 != null) {
                        output.writeMessage(4, element2);
                    }
                }
            }
            if (this.highPerfActiveSessionDurationSecHistogram != null && this.highPerfActiveSessionDurationSecHistogram.length > 0) {
                for (HistogramBucketInt32 element3 : this.highPerfActiveSessionDurationSecHistogram) {
                    if (element3 != null) {
                        output.writeMessage(5, element3);
                    }
                }
            }
            if (this.lowLatencyActiveSessionDurationSecHistogram != null && this.lowLatencyActiveSessionDurationSecHistogram.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.lowLatencyActiveSessionDurationSecHistogram.length) {
                        break;
                    }
                    HistogramBucketInt32 element4 = this.lowLatencyActiveSessionDurationSecHistogram[i2];
                    if (element4 != null) {
                        output.writeMessage(6, element4);
                    }
                    i = i2 + 1;
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.highPerfActiveTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, this.highPerfActiveTimeMs);
            }
            if (this.lowLatencyActiveTimeMs != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, this.lowLatencyActiveTimeMs);
            }
            int i = 0;
            if (this.highPerfLockAcqDurationSecHistogram != null && this.highPerfLockAcqDurationSecHistogram.length > 0) {
                int size2 = size;
                for (HistogramBucketInt32 element : this.highPerfLockAcqDurationSecHistogram) {
                    if (element != null) {
                        size2 += CodedOutputByteBufferNano.computeMessageSize(3, element);
                    }
                }
                size = size2;
            }
            if (this.lowLatencyLockAcqDurationSecHistogram != null && this.lowLatencyLockAcqDurationSecHistogram.length > 0) {
                int size3 = size;
                for (HistogramBucketInt32 element2 : this.lowLatencyLockAcqDurationSecHistogram) {
                    if (element2 != null) {
                        size3 += CodedOutputByteBufferNano.computeMessageSize(4, element2);
                    }
                }
                size = size3;
            }
            if (this.highPerfActiveSessionDurationSecHistogram != null && this.highPerfActiveSessionDurationSecHistogram.length > 0) {
                int size4 = size;
                for (HistogramBucketInt32 element3 : this.highPerfActiveSessionDurationSecHistogram) {
                    if (element3 != null) {
                        size4 += CodedOutputByteBufferNano.computeMessageSize(5, element3);
                    }
                }
                size = size4;
            }
            if (this.lowLatencyActiveSessionDurationSecHistogram != null && this.lowLatencyActiveSessionDurationSecHistogram.length > 0) {
                while (true) {
                    int i2 = i;
                    if (i2 >= this.lowLatencyActiveSessionDurationSecHistogram.length) {
                        break;
                    }
                    HistogramBucketInt32 element4 = this.lowLatencyActiveSessionDurationSecHistogram[i2];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(6, element4);
                    }
                    i = i2 + 1;
                }
            }
            return size;
        }

        public WifiLockStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.highPerfActiveTimeMs = input.readInt64();
                } else if (tag == 16) {
                    this.lowLatencyActiveTimeMs = input.readInt64();
                } else if (tag == 26) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                    int i = this.highPerfLockAcqDurationSecHistogram == null ? 0 : this.highPerfLockAcqDurationSecHistogram.length;
                    HistogramBucketInt32[] newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.highPerfLockAcqDurationSecHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.highPerfLockAcqDurationSecHistogram = newArray;
                } else if (tag == 34) {
                    int arrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int i2 = this.lowLatencyLockAcqDurationSecHistogram == null ? 0 : this.lowLatencyLockAcqDurationSecHistogram.length;
                    HistogramBucketInt32[] newArray2 = new HistogramBucketInt32[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.lowLatencyLockAcqDurationSecHistogram, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length - 1) {
                        newArray2[i2] = new HistogramBucketInt32();
                        input.readMessage(newArray2[i2]);
                        input.readTag();
                        i2++;
                    }
                    newArray2[i2] = new HistogramBucketInt32();
                    input.readMessage(newArray2[i2]);
                    this.lowLatencyLockAcqDurationSecHistogram = newArray2;
                } else if (tag == 42) {
                    int arrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    int i3 = this.highPerfActiveSessionDurationSecHistogram == null ? 0 : this.highPerfActiveSessionDurationSecHistogram.length;
                    HistogramBucketInt32[] newArray3 = new HistogramBucketInt32[(i3 + arrayLength3)];
                    if (i3 != 0) {
                        System.arraycopy(this.highPerfActiveSessionDurationSecHistogram, 0, newArray3, 0, i3);
                    }
                    while (i3 < newArray3.length - 1) {
                        newArray3[i3] = new HistogramBucketInt32();
                        input.readMessage(newArray3[i3]);
                        input.readTag();
                        i3++;
                    }
                    newArray3[i3] = new HistogramBucketInt32();
                    input.readMessage(newArray3[i3]);
                    this.highPerfActiveSessionDurationSecHistogram = newArray3;
                } else if (tag == 50) {
                    int arrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                    int i4 = this.lowLatencyActiveSessionDurationSecHistogram == null ? 0 : this.lowLatencyActiveSessionDurationSecHistogram.length;
                    HistogramBucketInt32[] newArray4 = new HistogramBucketInt32[(i4 + arrayLength4)];
                    if (i4 != 0) {
                        System.arraycopy(this.lowLatencyActiveSessionDurationSecHistogram, 0, newArray4, 0, i4);
                    }
                    while (i4 < newArray4.length - 1) {
                        newArray4[i4] = new HistogramBucketInt32();
                        input.readMessage(newArray4[i4]);
                        input.readTag();
                        i4++;
                    }
                    newArray4[i4] = new HistogramBucketInt32();
                    input.readMessage(newArray4[i4]);
                    this.lowLatencyActiveSessionDurationSecHistogram = newArray4;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiLockStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiLockStats) MessageNano.mergeFrom(new WifiLockStats(), data);
        }

        public static WifiLockStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiLockStats().mergeFrom(input);
        }
    }

    public static final class WifiToggleStats extends MessageNano {
        private static volatile WifiToggleStats[] _emptyArray;
        public int numToggleOffNormal;
        public int numToggleOffPrivileged;
        public int numToggleOnNormal;
        public int numToggleOnPrivileged;

        public static WifiToggleStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiToggleStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiToggleStats() {
            clear();
        }

        public WifiToggleStats clear() {
            this.numToggleOnPrivileged = 0;
            this.numToggleOffPrivileged = 0;
            this.numToggleOnNormal = 0;
            this.numToggleOffNormal = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numToggleOnPrivileged != 0) {
                output.writeInt32(1, this.numToggleOnPrivileged);
            }
            if (this.numToggleOffPrivileged != 0) {
                output.writeInt32(2, this.numToggleOffPrivileged);
            }
            if (this.numToggleOnNormal != 0) {
                output.writeInt32(3, this.numToggleOnNormal);
            }
            if (this.numToggleOffNormal != 0) {
                output.writeInt32(4, this.numToggleOffNormal);
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numToggleOnPrivileged != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numToggleOnPrivileged);
            }
            if (this.numToggleOffPrivileged != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, this.numToggleOffPrivileged);
            }
            if (this.numToggleOnNormal != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, this.numToggleOnNormal);
            }
            if (this.numToggleOffNormal != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, this.numToggleOffNormal);
            }
            return size;
        }

        public WifiToggleStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numToggleOnPrivileged = input.readInt32();
                } else if (tag == 16) {
                    this.numToggleOffPrivileged = input.readInt32();
                } else if (tag == 24) {
                    this.numToggleOnNormal = input.readInt32();
                } else if (tag == 32) {
                    this.numToggleOffNormal = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiToggleStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiToggleStats) MessageNano.mergeFrom(new WifiToggleStats(), data);
        }

        public static WifiToggleStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiToggleStats().mergeFrom(input);
        }
    }

    public static final class PasspointProvisionStats extends MessageNano {
        public static final int OSU_FAILURE_ADD_PASSPOINT_CONFIGURATION = 22;
        public static final int OSU_FAILURE_AP_CONNECTION = 1;
        public static final int OSU_FAILURE_INVALID_URL_FORMAT_FOR_OSU = 8;
        public static final int OSU_FAILURE_NO_AAA_SERVER_TRUST_ROOT_NODE = 17;
        public static final int OSU_FAILURE_NO_AAA_TRUST_ROOT_CERTIFICATE = 21;
        public static final int OSU_FAILURE_NO_OSU_ACTIVITY_FOUND = 14;
        public static final int OSU_FAILURE_NO_POLICY_SERVER_TRUST_ROOT_NODE = 19;
        public static final int OSU_FAILURE_NO_PPS_MO = 16;
        public static final int OSU_FAILURE_NO_REMEDIATION_SERVER_TRUST_ROOT_NODE = 18;
        public static final int OSU_FAILURE_OSU_PROVIDER_NOT_FOUND = 23;
        public static final int OSU_FAILURE_PROVISIONING_ABORTED = 6;
        public static final int OSU_FAILURE_PROVISIONING_NOT_AVAILABLE = 7;
        public static final int OSU_FAILURE_RETRIEVE_TRUST_ROOT_CERTIFICATES = 20;
        public static final int OSU_FAILURE_SERVER_CONNECTION = 3;
        public static final int OSU_FAILURE_SERVER_URL_INVALID = 2;
        public static final int OSU_FAILURE_SERVER_VALIDATION = 4;
        public static final int OSU_FAILURE_SERVICE_PROVIDER_VERIFICATION = 5;
        public static final int OSU_FAILURE_SOAP_MESSAGE_EXCHANGE = 11;
        public static final int OSU_FAILURE_START_REDIRECT_LISTENER = 12;
        public static final int OSU_FAILURE_TIMED_OUT_REDIRECT_LISTENER = 13;
        public static final int OSU_FAILURE_UNEXPECTED_COMMAND_TYPE = 9;
        public static final int OSU_FAILURE_UNEXPECTED_SOAP_MESSAGE_STATUS = 15;
        public static final int OSU_FAILURE_UNEXPECTED_SOAP_MESSAGE_TYPE = 10;
        public static final int OSU_FAILURE_UNKNOWN = 0;
        private static volatile PasspointProvisionStats[] _emptyArray;
        public int numProvisionSuccess;
        public ProvisionFailureCount[] provisionFailureCount;

        public static final class ProvisionFailureCount extends MessageNano {
            private static volatile ProvisionFailureCount[] _emptyArray;
            public int count;
            public int failureCode;

            public static ProvisionFailureCount[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ProvisionFailureCount[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ProvisionFailureCount() {
                clear();
            }

            public ProvisionFailureCount clear() {
                this.failureCode = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.failureCode != 0) {
                    output.writeInt32(1, this.failureCode);
                }
                if (this.count != 0) {
                    output.writeInt32(2, this.count);
                }
                super.writeTo(output);
            }

            /* access modifiers changed from: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (this.failureCode != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, this.failureCode);
                }
                if (this.count != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, this.count);
                }
                return size;
            }

            public ProvisionFailureCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
                            int value = input.readInt32();
                            switch (value) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                case 20:
                                case 21:
                                case 22:
                                case 23:
                                    this.failureCode = value;
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static ProvisionFailureCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ProvisionFailureCount) MessageNano.mergeFrom(new ProvisionFailureCount(), data);
            }

            public static ProvisionFailureCount parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ProvisionFailureCount().mergeFrom(input);
            }
        }

        public static PasspointProvisionStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PasspointProvisionStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PasspointProvisionStats() {
            clear();
        }

        public PasspointProvisionStats clear() {
            this.numProvisionSuccess = 0;
            this.provisionFailureCount = ProvisionFailureCount.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.numProvisionSuccess != 0) {
                output.writeInt32(1, this.numProvisionSuccess);
            }
            if (this.provisionFailureCount != null && this.provisionFailureCount.length > 0) {
                for (ProvisionFailureCount element : this.provisionFailureCount) {
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                }
            }
            super.writeTo(output);
        }

        /* access modifiers changed from: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.numProvisionSuccess != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, this.numProvisionSuccess);
            }
            if (this.provisionFailureCount != null && this.provisionFailureCount.length > 0) {
                for (ProvisionFailureCount element : this.provisionFailureCount) {
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                }
            }
            return size;
        }

        public PasspointProvisionStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numProvisionSuccess = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int i = this.provisionFailureCount == null ? 0 : this.provisionFailureCount.length;
                    ProvisionFailureCount[] newArray = new ProvisionFailureCount[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.provisionFailureCount, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new ProvisionFailureCount();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new ProvisionFailureCount();
                    input.readMessage(newArray[i]);
                    this.provisionFailureCount = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static PasspointProvisionStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (PasspointProvisionStats) MessageNano.mergeFrom(new PasspointProvisionStats(), data);
        }

        public static PasspointProvisionStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new PasspointProvisionStats().mergeFrom(input);
        }
    }
}
