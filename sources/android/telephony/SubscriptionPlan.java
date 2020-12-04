package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Range;
import android.util.RecurrenceRule;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Objects;

public final class SubscriptionPlan implements Parcelable {
    public static final long BYTES_UNKNOWN = -1;
    public static final long BYTES_UNLIMITED = Long.MAX_VALUE;
    public static final Parcelable.Creator<SubscriptionPlan> CREATOR = new Parcelable.Creator<SubscriptionPlan>() {
        public SubscriptionPlan createFromParcel(Parcel source) {
            return new SubscriptionPlan(source);
        }

        public SubscriptionPlan[] newArray(int size) {
            return new SubscriptionPlan[size];
        }
    };
    public static final int LIMIT_BEHAVIOR_BILLED = 1;
    public static final int LIMIT_BEHAVIOR_DISABLED = 0;
    public static final int LIMIT_BEHAVIOR_THROTTLED = 2;
    public static final int LIMIT_BEHAVIOR_UNKNOWN = -1;
    public static final long TIME_UNKNOWN = -1;
    private final RecurrenceRule cycleRule;
    /* access modifiers changed from: private */
    public int dataLimitBehavior;
    /* access modifiers changed from: private */
    public long dataLimitBytes;
    /* access modifiers changed from: private */
    public long dataUsageBytes;
    /* access modifiers changed from: private */
    public long dataUsageTime;
    /* access modifiers changed from: private */
    public CharSequence summary;
    /* access modifiers changed from: private */
    public CharSequence title;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LimitBehavior {
    }

    private SubscriptionPlan(RecurrenceRule cycleRule2) {
        this.dataLimitBytes = -1;
        this.dataLimitBehavior = -1;
        this.dataUsageBytes = -1;
        this.dataUsageTime = -1;
        this.cycleRule = (RecurrenceRule) Preconditions.checkNotNull(cycleRule2);
    }

    private SubscriptionPlan(Parcel source) {
        this.dataLimitBytes = -1;
        this.dataLimitBehavior = -1;
        this.dataUsageBytes = -1;
        this.dataUsageTime = -1;
        this.cycleRule = (RecurrenceRule) source.readParcelable((ClassLoader) null);
        this.title = source.readCharSequence();
        this.summary = source.readCharSequence();
        this.dataLimitBytes = source.readLong();
        this.dataLimitBehavior = source.readInt();
        this.dataUsageBytes = source.readLong();
        this.dataUsageTime = source.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.cycleRule, flags);
        dest.writeCharSequence(this.title);
        dest.writeCharSequence(this.summary);
        dest.writeLong(this.dataLimitBytes);
        dest.writeInt(this.dataLimitBehavior);
        dest.writeLong(this.dataUsageBytes);
        dest.writeLong(this.dataUsageTime);
    }

    public String toString() {
        return "SubscriptionPlan{" + "cycleRule=" + this.cycleRule + " title=" + this.title + " summary=" + this.summary + " dataLimitBytes=" + this.dataLimitBytes + " dataLimitBehavior=" + this.dataLimitBehavior + " dataUsageBytes=" + this.dataUsageBytes + " dataUsageTime=" + this.dataUsageTime + "}";
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.cycleRule, this.title, this.summary, Long.valueOf(this.dataLimitBytes), Integer.valueOf(this.dataLimitBehavior), Long.valueOf(this.dataUsageBytes), Long.valueOf(this.dataUsageTime)});
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SubscriptionPlan)) {
            return false;
        }
        SubscriptionPlan other = (SubscriptionPlan) obj;
        if (Objects.equals(this.cycleRule, other.cycleRule) && Objects.equals(this.title, other.title) && Objects.equals(this.summary, other.summary) && this.dataLimitBytes == other.dataLimitBytes && this.dataLimitBehavior == other.dataLimitBehavior && this.dataUsageBytes == other.dataUsageBytes && this.dataUsageTime == other.dataUsageTime) {
            return true;
        }
        return false;
    }

    public RecurrenceRule getCycleRule() {
        return this.cycleRule;
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public CharSequence getSummary() {
        return this.summary;
    }

    public long getDataLimitBytes() {
        return this.dataLimitBytes;
    }

    public int getDataLimitBehavior() {
        return this.dataLimitBehavior;
    }

    public long getDataUsageBytes() {
        return this.dataUsageBytes;
    }

    public long getDataUsageTime() {
        return this.dataUsageTime;
    }

    public Iterator<Range<ZonedDateTime>> cycleIterator() {
        return this.cycleRule.cycleIterator();
    }

    public static class Builder {
        private final SubscriptionPlan plan;

        public Builder(ZonedDateTime start, ZonedDateTime end, Period period) {
            this.plan = new SubscriptionPlan(new RecurrenceRule(start, end, period));
        }

        public static Builder createNonrecurring(ZonedDateTime start, ZonedDateTime end) {
            if (end.isAfter(start)) {
                return new Builder(start, end, (Period) null);
            }
            throw new IllegalArgumentException("End " + end + " isn't after start " + start);
        }

        public static Builder createRecurring(ZonedDateTime start, Period period) {
            if (!period.isZero() && !period.isNegative()) {
                return new Builder(start, (ZonedDateTime) null, period);
            }
            throw new IllegalArgumentException("Period " + period + " must be positive");
        }

        @SystemApi
        @Deprecated
        public static Builder createRecurringMonthly(ZonedDateTime start) {
            return new Builder(start, (ZonedDateTime) null, Period.ofMonths(1));
        }

        @SystemApi
        @Deprecated
        public static Builder createRecurringWeekly(ZonedDateTime start) {
            return new Builder(start, (ZonedDateTime) null, Period.ofDays(7));
        }

        @SystemApi
        @Deprecated
        public static Builder createRecurringDaily(ZonedDateTime start) {
            return new Builder(start, (ZonedDateTime) null, Period.ofDays(1));
        }

        public SubscriptionPlan build() {
            return this.plan;
        }

        public Builder setTitle(CharSequence title) {
            CharSequence unused = this.plan.title = title;
            return this;
        }

        public Builder setSummary(CharSequence summary) {
            CharSequence unused = this.plan.summary = summary;
            return this;
        }

        public Builder setDataLimit(long dataLimitBytes, int dataLimitBehavior) {
            if (dataLimitBytes < 0) {
                throw new IllegalArgumentException("Limit bytes must be positive");
            } else if (dataLimitBehavior >= 0) {
                long unused = this.plan.dataLimitBytes = dataLimitBytes;
                int unused2 = this.plan.dataLimitBehavior = dataLimitBehavior;
                return this;
            } else {
                throw new IllegalArgumentException("Limit behavior must be defined");
            }
        }

        public Builder setDataUsage(long dataUsageBytes, long dataUsageTime) {
            if (dataUsageBytes < 0) {
                throw new IllegalArgumentException("Usage bytes must be positive");
            } else if (dataUsageTime >= 0) {
                long unused = this.plan.dataUsageBytes = dataUsageBytes;
                long unused2 = this.plan.dataUsageTime = dataUsageTime;
                return this;
            } else {
                throw new IllegalArgumentException("Usage time must be positive");
            }
        }
    }
}
