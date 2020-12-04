package android.content.pm;

import android.content.pm.PackageManagerInternal;
import com.android.server.LocalServices;
import java.util.List;

public class PackageList implements PackageManagerInternal.PackageListObserver, AutoCloseable {
    private final List<String> mPackageNames;
    private final PackageManagerInternal.PackageListObserver mWrappedObserver;

    public PackageList(List<String> packageNames, PackageManagerInternal.PackageListObserver observer) {
        this.mPackageNames = packageNames;
        this.mWrappedObserver = observer;
    }

    public void onPackageAdded(String packageName, int uid) {
        if (this.mWrappedObserver != null) {
            this.mWrappedObserver.onPackageAdded(packageName, uid);
        }
    }

    public void onPackageRemoved(String packageName, int uid) {
        if (this.mWrappedObserver != null) {
            this.mWrappedObserver.onPackageRemoved(packageName, uid);
        }
    }

    public void close() throws Exception {
        ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).removePackageListObserver(this);
    }

    public List<String> getPackageNames() {
        return this.mPackageNames;
    }
}
