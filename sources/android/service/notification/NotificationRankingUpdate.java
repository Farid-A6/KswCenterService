package android.service.notification;

import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;

public class NotificationRankingUpdate implements Parcelable {
    public static final Parcelable.Creator<NotificationRankingUpdate> CREATOR = new Parcelable.Creator<NotificationRankingUpdate>() {
        public NotificationRankingUpdate createFromParcel(Parcel parcel) {
            return new NotificationRankingUpdate(parcel);
        }

        public NotificationRankingUpdate[] newArray(int size) {
            return new NotificationRankingUpdate[size];
        }
    };
    private final NotificationListenerService.RankingMap mRankingMap;

    public NotificationRankingUpdate(NotificationListenerService.Ranking[] rankings) {
        this.mRankingMap = new NotificationListenerService.RankingMap(rankings);
    }

    public NotificationRankingUpdate(Parcel in) {
        this.mRankingMap = (NotificationListenerService.RankingMap) in.readParcelable(getClass().getClassLoader());
    }

    public NotificationListenerService.RankingMap getRankingMap() {
        return this.mRankingMap;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.mRankingMap.equals(((NotificationRankingUpdate) o).mRankingMap);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.mRankingMap, flags);
    }
}
