package android.graphics;

import com.android.internal.telephony.IccCardConstants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PixelFormat {
    @Deprecated
    public static final int A_8 = 8;
    public static final int HSV_888 = 55;
    @Deprecated
    public static final int JPEG = 256;
    @Deprecated
    public static final int LA_88 = 10;
    @Deprecated
    public static final int L_8 = 9;
    public static final int OPAQUE = -1;
    public static final int RGBA_1010102 = 43;
    @Deprecated
    public static final int RGBA_4444 = 7;
    @Deprecated
    public static final int RGBA_5551 = 6;
    public static final int RGBA_8888 = 1;
    public static final int RGBA_F16 = 22;
    public static final int RGBX_8888 = 2;
    @Deprecated
    public static final int RGB_332 = 11;
    public static final int RGB_565 = 4;
    public static final int RGB_888 = 3;
    public static final int TRANSLUCENT = -3;
    public static final int TRANSPARENT = -2;
    public static final int UNKNOWN = 0;
    @Deprecated
    public static final int YCbCr_420_SP = 17;
    @Deprecated
    public static final int YCbCr_422_I = 20;
    @Deprecated
    public static final int YCbCr_422_SP = 16;
    public int bitsPerPixel;
    public int bytesPerPixel;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Format {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Opacity {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0042, code lost:
        r5.bitsPerPixel = 16;
        r5.bytesPerPixel = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void getPixelFormatInfo(int r4, android.graphics.PixelFormat r5) {
        /*
            r0 = 20
            r1 = 16
            r2 = 1
            if (r4 == r0) goto L_0x005f
            r0 = 22
            r3 = 8
            if (r4 == r0) goto L_0x0058
            r0 = 43
            if (r4 == r0) goto L_0x0050
            r0 = 55
            if (r4 == r0) goto L_0x0048
            switch(r4) {
                case 1: goto L_0x0050;
                case 2: goto L_0x0050;
                case 3: goto L_0x0048;
                case 4: goto L_0x0042;
                default: goto L_0x0018;
            }
        L_0x0018:
            switch(r4) {
                case 6: goto L_0x0042;
                case 7: goto L_0x0042;
                case 8: goto L_0x003d;
                case 9: goto L_0x003d;
                case 10: goto L_0x0042;
                case 11: goto L_0x003d;
                default: goto L_0x001b;
            }
        L_0x001b:
            switch(r4) {
                case 16: goto L_0x005f;
                case 17: goto L_0x0036;
                default: goto L_0x001e;
            }
        L_0x001e:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "unknown pixel format "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0036:
            r0 = 12
            r5.bitsPerPixel = r0
            r5.bytesPerPixel = r2
            goto L_0x0064
        L_0x003d:
            r5.bitsPerPixel = r3
            r5.bytesPerPixel = r2
            goto L_0x0064
        L_0x0042:
            r5.bitsPerPixel = r1
            r0 = 2
            r5.bytesPerPixel = r0
            goto L_0x0064
        L_0x0048:
            r0 = 24
            r5.bitsPerPixel = r0
            r0 = 3
            r5.bytesPerPixel = r0
            goto L_0x0064
        L_0x0050:
            r0 = 32
            r5.bitsPerPixel = r0
            r0 = 4
            r5.bytesPerPixel = r0
            goto L_0x0064
        L_0x0058:
            r0 = 64
            r5.bitsPerPixel = r0
            r5.bytesPerPixel = r3
            goto L_0x0064
        L_0x005f:
            r5.bitsPerPixel = r1
            r5.bytesPerPixel = r2
        L_0x0064:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.PixelFormat.getPixelFormatInfo(int, android.graphics.PixelFormat):void");
    }

    public static boolean formatHasAlpha(int format) {
        if (!(format == 1 || format == 10 || format == 22 || format == 43)) {
            switch (format) {
                case -3:
                case -2:
                    break;
                default:
                    switch (format) {
                        case 6:
                        case 7:
                        case 8:
                            break;
                        default:
                            return false;
                    }
            }
        }
        return true;
    }

    public static boolean isPublicFormat(int format) {
        if (format == 22 || format == 43) {
            return true;
        }
        switch (format) {
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }

    public static String formatToString(int format) {
        if (format == 20) {
            return "YCbCr_422_I";
        }
        if (format == 22) {
            return "RGBA_F16";
        }
        if (format == 43) {
            return "RGBA_1010102";
        }
        if (format == 55) {
            return "HSV_888";
        }
        if (format == 256) {
            return "JPEG";
        }
        switch (format) {
            case -3:
                return "TRANSLUCENT";
            case -2:
                return "TRANSPARENT";
            default:
                switch (format) {
                    case 0:
                        return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
                    case 1:
                        return "RGBA_8888";
                    case 2:
                        return "RGBX_8888";
                    case 3:
                        return "RGB_888";
                    case 4:
                        return "RGB_565";
                    default:
                        switch (format) {
                            case 6:
                                return "RGBA_5551";
                            case 7:
                                return "RGBA_4444";
                            case 8:
                                return "A_8";
                            case 9:
                                return "L_8";
                            case 10:
                                return "LA_88";
                            case 11:
                                return "RGB_332";
                            default:
                                switch (format) {
                                    case 16:
                                        return "YCbCr_422_SP";
                                    case 17:
                                        return "YCbCr_420_SP";
                                    default:
                                        return Integer.toString(format);
                                }
                        }
                }
        }
    }
}
