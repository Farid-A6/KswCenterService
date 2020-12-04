package android.media;

import android.content.Context;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

class Utils {
    private static final String TAG = "Utils";

    Utils() {
    }

    public static <T extends Comparable<? super T>> void sortDistinctRanges(Range<T>[] ranges) {
        Arrays.sort(ranges, new Comparator<Range<T>>() {
            public int compare(Range<T> lhs, Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                }
                if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                    return 1;
                }
                throw new IllegalArgumentException("sample rate ranges must be distinct (" + lhs + " and " + rhs + ")");
            }
        });
    }

    public static <T extends Comparable<? super T>> Range<T>[] intersectSortedDistinctRanges(Range<T>[] one, Range<T>[] another) {
        int ix = 0;
        Vector<Range<T>> result = new Vector<>();
        int length = another.length;
        for (int i = 0; i < length; i++) {
            Range<T> range = another[i];
            while (ix < one.length && one[ix].getUpper().compareTo(range.getLower()) < 0) {
                ix++;
            }
            while (ix < one.length && one[ix].getUpper().compareTo(range.getUpper()) < 0) {
                result.add(range.intersect(one[ix]));
                ix++;
            }
            if (ix == one.length) {
                break;
            }
            if (one[ix].getLower().compareTo(range.getUpper()) <= 0) {
                result.add(range.intersect(one[ix]));
            }
        }
        return (Range[]) result.toArray(new Range[result.size()]);
    }

    public static <T extends Comparable<? super T>> int binarySearchDistinctRanges(Range<T>[] ranges, T value) {
        return Arrays.binarySearch(ranges, Range.create(value, value), new Comparator<Range<T>>() {
            public int compare(Range<T> lhs, Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                }
                if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                    return 1;
                }
                return 0;
            }
        });
    }

    static int gcd(int a, int b) {
        if (a == 0 && b == 0) {
            return 1;
        }
        if (b < 0) {
            b = -b;
        }
        if (a < 0) {
            a = -a;
        }
        while (a != 0) {
            int c = b % a;
            b = a;
            a = c;
        }
        return b;
    }

    static Range<Integer> factorRange(Range<Integer> range, int factor) {
        if (factor == 1) {
            return range;
        }
        return Range.create(Integer.valueOf(divUp(range.getLower().intValue(), factor)), Integer.valueOf(range.getUpper().intValue() / factor));
    }

    static Range<Long> factorRange(Range<Long> range, long factor) {
        if (factor == 1) {
            return range;
        }
        return Range.create(Long.valueOf(divUp(range.getLower().longValue(), factor)), Long.valueOf(range.getUpper().longValue() / factor));
    }

    private static Rational scaleRatio(Rational ratio, int num, int den) {
        int common = gcd(num, den);
        return new Rational((int) (((double) ratio.getNumerator()) * ((double) (num / common))), (int) (((double) ratio.getDenominator()) * ((double) (den / common))));
    }

    static Range<Rational> scaleRange(Range<Rational> range, int num, int den) {
        if (num == den) {
            return range;
        }
        return Range.create(scaleRatio(range.getLower(), num, den), scaleRatio(range.getUpper(), num, den));
    }

    static Range<Integer> alignRange(Range<Integer> range, int align) {
        return range.intersect(Integer.valueOf(divUp(range.getLower().intValue(), align) * align), Integer.valueOf((range.getUpper().intValue() / align) * align));
    }

    static int divUp(int num, int den) {
        return ((num + den) - 1) / den;
    }

    static long divUp(long num, long den) {
        return ((num + den) - 1) / den;
    }

    private static long lcm(int a, int b) {
        if (a != 0 && b != 0) {
            return (((long) a) * ((long) b)) / ((long) gcd(a, b));
        }
        throw new IllegalArgumentException("lce is not defined for zero arguments");
    }

    static Range<Integer> intRangeFor(double v) {
        return Range.create(Integer.valueOf((int) v), Integer.valueOf((int) Math.ceil(v)));
    }

    static Range<Long> longRangeFor(double v) {
        return Range.create(Long.valueOf((long) v), Long.valueOf((long) Math.ceil(v)));
    }

    static Size parseSize(Object o, Size fallback) {
        try {
            return Size.parseSize((String) o);
        } catch (ClassCastException | NumberFormatException e) {
            Log.w(TAG, "could not parse size '" + o + "'");
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        }
    }

    static int parseIntSafely(Object o, int fallback) {
        if (o == null) {
            return fallback;
        }
        try {
            return Integer.parseInt((String) o);
        } catch (ClassCastException | NumberFormatException e) {
            Log.w(TAG, "could not parse integer '" + o + "'");
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        }
    }

    static Range<Integer> parseIntRange(Object o, Range<Integer> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Integer.valueOf(Integer.parseInt(s.substring(0, ix), 10)), Integer.valueOf(Integer.parseInt(s.substring(ix + 1), 10)));
            }
            int value = Integer.parseInt(s);
            return Range.create(Integer.valueOf(value), Integer.valueOf(value));
        } catch (NullPointerException e) {
            return fallback;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            Log.w(TAG, "could not parse integer range '" + o + "'");
            return fallback;
        }
    }

    static Range<Long> parseLongRange(Object o, Range<Long> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Long.valueOf(Long.parseLong(s.substring(0, ix), 10)), Long.valueOf(Long.parseLong(s.substring(ix + 1), 10)));
            }
            long value = Long.parseLong(s);
            return Range.create(Long.valueOf(value), Long.valueOf(value));
        } catch (NullPointerException e) {
            return fallback;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            Log.w(TAG, "could not parse long range '" + o + "'");
            return fallback;
        }
    }

    static Range<Rational> parseRationalRange(Object o, Range<Rational> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Rational.parseRational(s.substring(0, ix)), Rational.parseRational(s.substring(ix + 1)));
            }
            Rational value = Rational.parseRational(s);
            return Range.create(value, value);
        } catch (NullPointerException e) {
            return fallback;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            Log.w(TAG, "could not parse rational range '" + o + "'");
            return fallback;
        }
    }

    static Pair<Size, Size> parseSizeRange(Object o) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Pair.create(Size.parseSize(s.substring(0, ix)), Size.parseSize(s.substring(ix + 1)));
            }
            Size value = Size.parseSize(s);
            return Pair.create(value, value);
        } catch (NullPointerException e) {
            return null;
        } catch (ClassCastException | IllegalArgumentException | NumberFormatException e2) {
            Log.w(TAG, "could not parse size range '" + o + "'");
            return null;
        }
    }

    public static File getUniqueExternalFile(Context context, String subdirectory, String fileName, String mimeType) {
        File externalStorage = Environment.getExternalStoragePublicDirectory(subdirectory);
        externalStorage.mkdirs();
        try {
            return FileUtils.buildUniqueFile(externalStorage, mimeType, fileName);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Unable to get a unique file name: " + e);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0047, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
        if (r1 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004d, code lost:
        if (r2 != null) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0053, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0054, code lost:
        r2.addSuppressed(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0058, code lost:
        r1.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String getFileDisplayNameFromUri(android.content.Context r8, android.net.Uri r9) {
        /*
            java.lang.String r0 = r9.getScheme()
            java.lang.String r1 = "file"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x0011
            java.lang.String r1 = r9.getLastPathSegment()
            return r1
        L_0x0011:
            java.lang.String r1 = "content"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x0061
            java.lang.String r1 = "_display_name"
            java.lang.String[] r4 = new java.lang.String[]{r1}
            android.content.ContentResolver r2 = r8.getContentResolver()
            r5 = 0
            r6 = 0
            r7 = 0
            r3 = r9
            android.database.Cursor r1 = r2.query(r3, r4, r5, r6, r7)
            r2 = 0
            if (r1 == 0) goto L_0x005c
            int r3 = r1.getCount()     // Catch:{ Throwable -> 0x0049 }
            if (r3 == 0) goto L_0x005c
            r1.moveToFirst()     // Catch:{ Throwable -> 0x0049 }
            java.lang.String r3 = "_display_name"
            int r3 = r1.getColumnIndex(r3)     // Catch:{ Throwable -> 0x0049 }
            java.lang.String r3 = r1.getString(r3)     // Catch:{ Throwable -> 0x0049 }
            if (r1 == 0) goto L_0x0046
            r1.close()
        L_0x0046:
            return r3
        L_0x0047:
            r3 = move-exception
            goto L_0x004b
        L_0x0049:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0047 }
        L_0x004b:
            if (r1 == 0) goto L_0x005b
            if (r2 == 0) goto L_0x0058
            r1.close()     // Catch:{ Throwable -> 0x0053 }
            goto L_0x005b
        L_0x0053:
            r5 = move-exception
            r2.addSuppressed(r5)
            goto L_0x005b
        L_0x0058:
            r1.close()
        L_0x005b:
            throw r3
        L_0x005c:
            if (r1 == 0) goto L_0x0061
            r1.close()
        L_0x0061:
            java.lang.String r1 = r9.toString()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.Utils.getFileDisplayNameFromUri(android.content.Context, android.net.Uri):java.lang.String");
    }
}
