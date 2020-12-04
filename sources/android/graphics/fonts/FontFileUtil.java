package android.graphics.fonts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FontFileUtil {
    private static final int ANALYZE_ERROR = -1;
    private static final int OS2_TABLE_TAG = 1330851634;
    private static final int SFNT_VERSION_1 = 65536;
    private static final int SFNT_VERSION_OTTO = 1330926671;
    private static final int TTC_TAG = 1953784678;

    private FontFileUtil() {
    }

    public static int unpackWeight(int packed) {
        return 65535 & packed;
    }

    public static boolean unpackItalic(int packed) {
        return (65536 & packed) != 0;
    }

    public static boolean isSuccess(int packed) {
        return packed != -1;
    }

    private static int pack(int weight, boolean italic) {
        return (italic ? 65536 : 0) | weight;
    }

    public static final int analyzeStyle(ByteBuffer buffer, int ttcIndex, FontVariationAxis[] varSettings) {
        int italic;
        int weight;
        boolean z;
        ByteBuffer byteBuffer = buffer;
        int i = ttcIndex;
        FontVariationAxis[] fontVariationAxisArr = varSettings;
        boolean z2 = false;
        if (fontVariationAxisArr != null) {
            italic = -1;
            weight = -1;
            for (FontVariationAxis axis : fontVariationAxisArr) {
                if ("wght".equals(axis.getTag())) {
                    weight = (int) axis.getStyleValue();
                } else if ("ital".equals(axis.getTag())) {
                    italic = axis.getStyleValue() == 1.0f ? 1 : 0;
                }
            }
        } else {
            italic = -1;
            weight = -1;
        }
        if (weight == -1 || italic == -1) {
            ByteOrder originalOrder = buffer.order();
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            int fontFileOffset = 0;
            try {
                if (byteBuffer.getInt(0) == TTC_TAG) {
                    if (i >= byteBuffer.getInt(8)) {
                        return -1;
                    }
                    fontFileOffset = byteBuffer.getInt((i * 4) + 12);
                }
                int sfntVersion = byteBuffer.getInt(fontFileOffset);
                if (sfntVersion == 65536 || sfntVersion == SFNT_VERSION_OTTO) {
                    int numTables = byteBuffer.getShort(fontFileOffset + 4);
                    int os2TableOffset = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= numTables) {
                            break;
                        }
                        int tableOffset = fontFileOffset + 12 + (i2 * 16);
                        if (byteBuffer.getInt(tableOffset) == OS2_TABLE_TAG) {
                            os2TableOffset = byteBuffer.getInt(tableOffset + 8);
                            break;
                        }
                        i2++;
                    }
                    if (os2TableOffset == -1) {
                        int pack = pack(400, false);
                        byteBuffer.order(originalOrder);
                        return pack;
                    }
                    int weightFromOS2 = byteBuffer.getShort(os2TableOffset + 4);
                    boolean italicFromOS2 = (byteBuffer.getShort(os2TableOffset + 62) & 1) != 0;
                    int i3 = weight == -1 ? weightFromOS2 : weight;
                    if (italic == -1) {
                        z = italicFromOS2;
                    } else {
                        z = true;
                        if (italic != 1) {
                            z = false;
                        }
                    }
                    int pack2 = pack(i3, z);
                    byteBuffer.order(originalOrder);
                    return pack2;
                }
                byteBuffer.order(originalOrder);
                return -1;
            } finally {
                byteBuffer.order(originalOrder);
            }
        } else {
            if (italic == 1) {
                z2 = true;
            }
            return pack(weight, z2);
        }
    }
}
