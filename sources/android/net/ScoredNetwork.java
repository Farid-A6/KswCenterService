package android.net;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public class ScoredNetwork implements Parcelable {
    public static final String ATTRIBUTES_KEY_BADGING_CURVE = "android.net.attributes.key.BADGING_CURVE";
    public static final String ATTRIBUTES_KEY_HAS_CAPTIVE_PORTAL = "android.net.attributes.key.HAS_CAPTIVE_PORTAL";
    public static final String ATTRIBUTES_KEY_RANKING_SCORE_OFFSET = "android.net.attributes.key.RANKING_SCORE_OFFSET";
    public static final Parcelable.Creator<ScoredNetwork> CREATOR = new Parcelable.Creator<ScoredNetwork>() {
        public ScoredNetwork createFromParcel(Parcel in) {
            return new ScoredNetwork(in);
        }

        public ScoredNetwork[] newArray(int size) {
            return new ScoredNetwork[size];
        }
    };
    public final Bundle attributes;
    public final boolean meteredHint;
    public final NetworkKey networkKey;
    public final RssiCurve rssiCurve;

    public ScoredNetwork(NetworkKey networkKey2, RssiCurve rssiCurve2) {
        this(networkKey2, rssiCurve2, false);
    }

    public ScoredNetwork(NetworkKey networkKey2, RssiCurve rssiCurve2, boolean meteredHint2) {
        this(networkKey2, rssiCurve2, meteredHint2, (Bundle) null);
    }

    public ScoredNetwork(NetworkKey networkKey2, RssiCurve rssiCurve2, boolean meteredHint2, Bundle attributes2) {
        this.networkKey = networkKey2;
        this.rssiCurve = rssiCurve2;
        this.meteredHint = meteredHint2;
        this.attributes = attributes2;
    }

    private ScoredNetwork(Parcel in) {
        this.networkKey = NetworkKey.CREATOR.createFromParcel(in);
        boolean z = true;
        if (in.readByte() == 1) {
            this.rssiCurve = RssiCurve.CREATOR.createFromParcel(in);
        } else {
            this.rssiCurve = null;
        }
        this.meteredHint = in.readByte() != 1 ? false : z;
        this.attributes = in.readBundle();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        this.networkKey.writeToParcel(out, flags);
        if (this.rssiCurve != null) {
            out.writeByte((byte) 1);
            this.rssiCurve.writeToParcel(out, flags);
        } else {
            out.writeByte((byte) 0);
        }
        out.writeByte(this.meteredHint ? (byte) 1 : 0);
        out.writeBundle(this.attributes);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoredNetwork that = (ScoredNetwork) o;
        if (!Objects.equals(this.networkKey, that.networkKey) || !Objects.equals(this.rssiCurve, that.rssiCurve) || !Objects.equals(Boolean.valueOf(this.meteredHint), Boolean.valueOf(that.meteredHint)) || !bundleEquals(this.attributes, that.attributes)) {
            return false;
        }
        return true;
    }

    private boolean bundleEquals(Bundle bundle1, Bundle bundle2) {
        if (bundle1 == bundle2) {
            return true;
        }
        if (bundle1 == null || bundle2 == null || bundle1.size() != bundle2.size()) {
            return false;
        }
        for (String key : bundle1.keySet()) {
            if (!Objects.equals(bundle1.get(key), bundle2.get(key))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.networkKey, this.rssiCurve, Boolean.valueOf(this.meteredHint), this.attributes});
    }

    public String toString() {
        StringBuilder out = new StringBuilder("ScoredNetwork{networkKey=" + this.networkKey + ", rssiCurve=" + this.rssiCurve + ", meteredHint=" + this.meteredHint);
        if (this.attributes != null && !this.attributes.isEmpty()) {
            out.append(", attributes=" + this.attributes);
        }
        out.append('}');
        return out.toString();
    }

    public boolean hasRankingScore() {
        return this.rssiCurve != null || (this.attributes != null && this.attributes.containsKey(ATTRIBUTES_KEY_RANKING_SCORE_OFFSET));
    }

    public int calculateRankingScore(int rssi) throws UnsupportedOperationException {
        if (hasRankingScore()) {
            int offset = 0;
            int i = 0;
            if (this.attributes != null) {
                offset = 0 + this.attributes.getInt(ATTRIBUTES_KEY_RANKING_SCORE_OFFSET, 0);
            }
            if (this.rssiCurve != null) {
                i = this.rssiCurve.lookupScore(rssi) << 8;
            }
            int score = i;
            try {
                return Math.addExact(score, offset);
            } catch (ArithmeticException e) {
                return score < 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
        } else {
            throw new UnsupportedOperationException("Either rssiCurve or rankingScoreOffset is required to calculate the ranking score");
        }
    }

    public int calculateBadge(int rssi) {
        if (this.attributes == null || !this.attributes.containsKey(ATTRIBUTES_KEY_BADGING_CURVE)) {
            return 0;
        }
        return ((RssiCurve) this.attributes.getParcelable(ATTRIBUTES_KEY_BADGING_CURVE)).lookupScore(rssi);
    }
}
