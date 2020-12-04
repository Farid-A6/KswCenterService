package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.BackupUtils;
import android.util.Range;
import android.util.RecurrenceRule;
import com.android.internal.util.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Objects;

public class NetworkPolicy implements Parcelable, Comparable<NetworkPolicy> {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<NetworkPolicy> CREATOR = new Parcelable.Creator<NetworkPolicy>() {
        public NetworkPolicy createFromParcel(Parcel in) {
            return new NetworkPolicy(in);
        }

        public NetworkPolicy[] newArray(int size) {
            return new NetworkPolicy[size];
        }
    };
    public static final int CYCLE_NONE = -1;
    private static final long DEFAULT_MTU = 1500;
    public static final long LIMIT_DISABLED = -1;
    public static final long SNOOZE_NEVER = -1;
    private static final int VERSION_INIT = 1;
    private static final int VERSION_RAPID = 3;
    private static final int VERSION_RULE = 2;
    public static final long WARNING_DISABLED = -1;
    public RecurrenceRule cycleRule;
    @UnsupportedAppUsage
    public boolean inferred;
    public long lastLimitSnooze;
    public long lastRapidSnooze;
    public long lastWarningSnooze;
    @UnsupportedAppUsage
    public long limitBytes;
    @Deprecated
    @UnsupportedAppUsage
    public boolean metered;
    @UnsupportedAppUsage
    public NetworkTemplate template;
    @UnsupportedAppUsage
    public long warningBytes;

    public static RecurrenceRule buildRule(int cycleDay, ZoneId cycleTimezone) {
        if (cycleDay != -1) {
            return RecurrenceRule.buildRecurringMonthly(cycleDay, cycleTimezone);
        }
        return RecurrenceRule.buildNever();
    }

    @Deprecated
    public NetworkPolicy(NetworkTemplate template2, int cycleDay, String cycleTimezone, long warningBytes2, long limitBytes2, boolean metered2) {
        this(template2, cycleDay, cycleTimezone, warningBytes2, limitBytes2, -1, -1, metered2, false);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NetworkPolicy(android.net.NetworkTemplate r16, int r17, java.lang.String r18, long r19, long r21, long r23, long r25, boolean r27, boolean r28) {
        /*
            r15 = this;
            java.time.ZoneId r0 = java.time.ZoneId.of(r18)
            r1 = r17
            android.util.RecurrenceRule r4 = buildRule(r1, r0)
            r2 = r15
            r3 = r16
            r5 = r19
            r7 = r21
            r9 = r23
            r11 = r25
            r13 = r27
            r14 = r28
            r2.<init>(r3, r4, r5, r7, r9, r11, r13, r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.NetworkPolicy.<init>(android.net.NetworkTemplate, int, java.lang.String, long, long, long, long, boolean, boolean):void");
    }

    @Deprecated
    public NetworkPolicy(NetworkTemplate template2, RecurrenceRule cycleRule2, long warningBytes2, long limitBytes2, long lastWarningSnooze2, long lastLimitSnooze2, boolean metered2, boolean inferred2) {
        this(template2, cycleRule2, warningBytes2, limitBytes2, lastWarningSnooze2, lastLimitSnooze2, -1, metered2, inferred2);
    }

    public NetworkPolicy(NetworkTemplate template2, RecurrenceRule cycleRule2, long warningBytes2, long limitBytes2, long lastWarningSnooze2, long lastLimitSnooze2, long lastRapidSnooze2, boolean metered2, boolean inferred2) {
        this.warningBytes = -1;
        this.limitBytes = -1;
        this.lastWarningSnooze = -1;
        this.lastLimitSnooze = -1;
        this.lastRapidSnooze = -1;
        this.metered = true;
        this.inferred = false;
        this.template = (NetworkTemplate) Preconditions.checkNotNull(template2, "missing NetworkTemplate");
        this.cycleRule = (RecurrenceRule) Preconditions.checkNotNull(cycleRule2, "missing RecurrenceRule");
        this.warningBytes = warningBytes2;
        this.limitBytes = limitBytes2;
        this.lastWarningSnooze = lastWarningSnooze2;
        this.lastLimitSnooze = lastLimitSnooze2;
        this.lastRapidSnooze = lastRapidSnooze2;
        this.metered = metered2;
        this.inferred = inferred2;
    }

    private NetworkPolicy(Parcel source) {
        this.warningBytes = -1;
        this.limitBytes = -1;
        this.lastWarningSnooze = -1;
        this.lastLimitSnooze = -1;
        this.lastRapidSnooze = -1;
        boolean z = true;
        this.metered = true;
        this.inferred = false;
        this.template = (NetworkTemplate) source.readParcelable((ClassLoader) null);
        this.cycleRule = (RecurrenceRule) source.readParcelable((ClassLoader) null);
        this.warningBytes = source.readLong();
        this.limitBytes = source.readLong();
        this.lastWarningSnooze = source.readLong();
        this.lastLimitSnooze = source.readLong();
        this.lastRapidSnooze = source.readLong();
        this.metered = source.readInt() != 0;
        this.inferred = source.readInt() == 0 ? false : z;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.template, flags);
        dest.writeParcelable(this.cycleRule, flags);
        dest.writeLong(this.warningBytes);
        dest.writeLong(this.limitBytes);
        dest.writeLong(this.lastWarningSnooze);
        dest.writeLong(this.lastLimitSnooze);
        dest.writeLong(this.lastRapidSnooze);
        dest.writeInt(this.metered ? 1 : 0);
        dest.writeInt(this.inferred ? 1 : 0);
    }

    public int describeContents() {
        return 0;
    }

    public Iterator<Range<ZonedDateTime>> cycleIterator() {
        return this.cycleRule.cycleIterator();
    }

    @UnsupportedAppUsage
    public boolean isOverWarning(long totalBytes) {
        return this.warningBytes != -1 && totalBytes >= this.warningBytes;
    }

    @UnsupportedAppUsage
    public boolean isOverLimit(long totalBytes) {
        return this.limitBytes != -1 && totalBytes + 3000 >= this.limitBytes;
    }

    @UnsupportedAppUsage
    public void clearSnooze() {
        this.lastWarningSnooze = -1;
        this.lastLimitSnooze = -1;
        this.lastRapidSnooze = -1;
    }

    public boolean hasCycle() {
        return this.cycleRule.cycleIterator().hasNext();
    }

    @UnsupportedAppUsage
    public int compareTo(NetworkPolicy another) {
        if (another == null || another.limitBytes == -1) {
            return -1;
        }
        if (this.limitBytes == -1 || another.limitBytes < this.limitBytes) {
            return 1;
        }
        return 0;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.template, this.cycleRule, Long.valueOf(this.warningBytes), Long.valueOf(this.limitBytes), Long.valueOf(this.lastWarningSnooze), Long.valueOf(this.lastLimitSnooze), Long.valueOf(this.lastRapidSnooze), Boolean.valueOf(this.metered), Boolean.valueOf(this.inferred)});
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkPolicy)) {
            return false;
        }
        NetworkPolicy other = (NetworkPolicy) obj;
        if (this.warningBytes == other.warningBytes && this.limitBytes == other.limitBytes && this.lastWarningSnooze == other.lastWarningSnooze && this.lastLimitSnooze == other.lastLimitSnooze && this.lastRapidSnooze == other.lastRapidSnooze && this.metered == other.metered && this.inferred == other.inferred && Objects.equals(this.template, other.template) && Objects.equals(this.cycleRule, other.cycleRule)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "NetworkPolicy{" + "template=" + this.template + " cycleRule=" + this.cycleRule + " warningBytes=" + this.warningBytes + " limitBytes=" + this.limitBytes + " lastWarningSnooze=" + this.lastWarningSnooze + " lastLimitSnooze=" + this.lastLimitSnooze + " lastRapidSnooze=" + this.lastRapidSnooze + " metered=" + this.metered + " inferred=" + this.inferred + "}";
    }

    public byte[] getBytesForBackup() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeInt(3);
        out.write(this.template.getBytesForBackup());
        this.cycleRule.writeToStream(out);
        out.writeLong(this.warningBytes);
        out.writeLong(this.limitBytes);
        out.writeLong(this.lastWarningSnooze);
        out.writeLong(this.lastLimitSnooze);
        out.writeLong(this.lastRapidSnooze);
        out.writeInt(this.metered ? 1 : 0);
        out.writeInt(this.inferred ? 1 : 0);
        return baos.toByteArray();
    }

    public static NetworkPolicy getNetworkPolicyFromBackup(DataInputStream in) throws IOException, BackupUtils.BadVersionException {
        RecurrenceRule buildRule;
        long lastRapidSnooze2;
        int version = in.readInt();
        if (version < 1 || version > 3) {
            throw new BackupUtils.BadVersionException("Unknown backup version: " + version);
        }
        NetworkTemplate template2 = NetworkTemplate.getNetworkTemplateFromBackup(in);
        if (version >= 2) {
            buildRule = new RecurrenceRule(in);
        } else {
            DataInputStream dataInputStream = in;
            buildRule = buildRule(in.readInt(), ZoneId.of(BackupUtils.readString(in)));
        }
        RecurrenceRule cycleRule2 = buildRule;
        long warningBytes2 = in.readLong();
        long limitBytes2 = in.readLong();
        long lastWarningSnooze2 = in.readLong();
        long lastLimitSnooze2 = in.readLong();
        if (version >= 3) {
            lastRapidSnooze2 = in.readLong();
        } else {
            lastRapidSnooze2 = -1;
        }
        return new NetworkPolicy(template2, cycleRule2, warningBytes2, limitBytes2, lastWarningSnooze2, lastLimitSnooze2, lastRapidSnooze2, in.readInt() == 1, in.readInt() == 1);
    }
}
