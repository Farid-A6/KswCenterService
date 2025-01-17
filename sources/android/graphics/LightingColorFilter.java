package android.graphics;

import android.annotation.UnsupportedAppUsage;

public class LightingColorFilter extends ColorFilter {
    private int mAdd;
    private int mMul;

    private static native long native_CreateLightingFilter(int i, int i2);

    public LightingColorFilter(int mul, int add) {
        this.mMul = mul;
        this.mAdd = add;
    }

    public int getColorMultiply() {
        return this.mMul;
    }

    @UnsupportedAppUsage
    public void setColorMultiply(int mul) {
        if (this.mMul != mul) {
            this.mMul = mul;
            discardNativeInstance();
        }
    }

    public int getColorAdd() {
        return this.mAdd;
    }

    @UnsupportedAppUsage
    public void setColorAdd(int add) {
        if (this.mAdd != add) {
            this.mAdd = add;
            discardNativeInstance();
        }
    }

    /* access modifiers changed from: package-private */
    public long createNativeInstance() {
        return native_CreateLightingFilter(this.mMul, this.mAdd);
    }
}
