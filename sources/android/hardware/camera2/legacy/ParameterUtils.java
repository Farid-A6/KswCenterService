package android.hardware.camera2.legacy;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.ParamsUtils;
import android.hardware.camera2.utils.SizeAreaComparator;
import android.media.tv.TvContract;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterUtils {
    private static final double ASPECT_RATIO_TOLERANCE = 0.05000000074505806d;
    public static final Camera.Area CAMERA_AREA_DEFAULT = new Camera.Area(new Rect(NORMALIZED_RECTANGLE_DEFAULT), 1);
    private static final boolean DEBUG = false;
    public static final Rect NORMALIZED_RECTANGLE_DEFAULT = new Rect(-1000, -1000, 1000, 1000);
    public static final int NORMALIZED_RECTANGLE_MAX = 1000;
    public static final int NORMALIZED_RECTANGLE_MIN = -1000;
    public static final Rect RECTANGLE_EMPTY = new Rect(0, 0, 0, 0);
    private static final String TAG = "ParameterUtils";
    private static final int ZOOM_RATIO_MULTIPLIER = 100;

    public static class ZoomData {
        public final Rect previewCrop;
        public final Rect reportedCrop;
        public final int zoomIndex;

        public ZoomData(int zoomIndex2, Rect previewCrop2, Rect reportedCrop2) {
            this.zoomIndex = zoomIndex2;
            this.previewCrop = previewCrop2;
            this.reportedCrop = reportedCrop2;
        }
    }

    public static class MeteringData {
        public final Camera.Area meteringArea;
        public final Rect previewMetering;
        public final Rect reportedMetering;

        public MeteringData(Camera.Area meteringArea2, Rect previewMetering2, Rect reportedMetering2) {
            this.meteringArea = meteringArea2;
            this.previewMetering = previewMetering2;
            this.reportedMetering = reportedMetering2;
        }
    }

    public static class WeightedRectangle {
        public final Rect rect;
        public final int weight;

        public WeightedRectangle(Rect rect2, int weight2) {
            this.rect = (Rect) Preconditions.checkNotNull(rect2, "rect must not be null");
            this.weight = weight2;
        }

        public MeteringRectangle toMetering() {
            int weight2 = clip(this.weight, 0, 1000, this.rect, TvContract.PreviewPrograms.COLUMN_WEIGHT);
            return new MeteringRectangle(clipLower(this.rect.left, 0, this.rect, "left"), clipLower(this.rect.top, 0, this.rect, "top"), clipLower(this.rect.width(), 0, this.rect, "width"), clipLower(this.rect.height(), 0, this.rect, "height"), weight2);
        }

        public Face toFace(int id, Point leftEyePosition, Point rightEyePosition, Point mouthPosition) {
            int idSafe = clipLower(id, 0, this.rect, "id");
            return new Face(this.rect, clip(this.weight, 1, 100, this.rect, "score"), idSafe, leftEyePosition, rightEyePosition, mouthPosition);
        }

        public Face toFace() {
            return new Face(this.rect, clip(this.weight, 1, 100, this.rect, "score"));
        }

        private static int clipLower(int value, int lo, Rect rect2, String name) {
            return clip(value, lo, Integer.MAX_VALUE, rect2, name);
        }

        private static int clip(int value, int lo, int hi, Rect rect2, String name) {
            if (value < lo) {
                Log.w(ParameterUtils.TAG, "toMetering - Rectangle " + rect2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + name + " too small, clip to " + lo);
                return lo;
            } else if (value <= hi) {
                return value;
            } else {
                Log.w(ParameterUtils.TAG, "toMetering - Rectangle " + rect2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + name + " too small, clip to " + hi);
                return hi;
            }
        }
    }

    public static Size convertSize(Camera.Size size) {
        Preconditions.checkNotNull(size, "size must not be null");
        return new Size(size.width, size.height);
    }

    public static List<Size> convertSizeList(List<Camera.Size> sizeList) {
        Preconditions.checkNotNull(sizeList, "sizeList must not be null");
        List<Size> sizes = new ArrayList<>(sizeList.size());
        for (Camera.Size s : sizeList) {
            sizes.add(new Size(s.width, s.height));
        }
        return sizes;
    }

    public static Size[] convertSizeListToArray(List<Camera.Size> sizeList) {
        Preconditions.checkNotNull(sizeList, "sizeList must not be null");
        Size[] array = new Size[sizeList.size()];
        int ctr = 0;
        for (Camera.Size s : sizeList) {
            array[ctr] = new Size(s.width, s.height);
            ctr++;
        }
        return array;
    }

    public static boolean containsSize(List<Camera.Size> sizeList, int width, int height) {
        Preconditions.checkNotNull(sizeList, "sizeList must not be null");
        for (Camera.Size s : sizeList) {
            if (s.height == height && s.width == width) {
                return true;
            }
        }
        return false;
    }

    public static Size getLargestSupportedJpegSizeByArea(Camera.Parameters params) {
        Preconditions.checkNotNull(params, "params must not be null");
        return SizeAreaComparator.findLargestByArea(convertSizeList(params.getSupportedPictureSizes()));
    }

    public static String stringFromArea(Camera.Area area) {
        if (area == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Rect r = area.rect;
        sb.setLength(0);
        sb.append("([");
        sb.append(r.left);
        sb.append(',');
        sb.append(r.top);
        sb.append("][");
        sb.append(r.right);
        sb.append(',');
        sb.append(r.bottom);
        sb.append(']');
        sb.append(',');
        sb.append(area.weight);
        sb.append(')');
        return sb.toString();
    }

    public static String stringFromAreaList(List<Camera.Area> areaList) {
        StringBuilder sb = new StringBuilder();
        if (areaList == null) {
            return null;
        }
        int i = 0;
        for (Camera.Area area : areaList) {
            if (area == null) {
                sb.append("null");
            } else {
                sb.append(stringFromArea(area));
            }
            if (i != areaList.size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        return sb.toString();
    }

    public static int getClosestAvailableZoomCrop(Camera.Parameters params, Rect activeArray, Size streamSize, Rect cropRegion, Rect reportedCropRegion, Rect previewCropRegion) {
        Rect actualCrop;
        boolean isBest;
        Rect rect = activeArray;
        Rect rect2 = reportedCropRegion;
        Rect rect3 = previewCropRegion;
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(rect, "activeArray must not be null");
        Preconditions.checkNotNull(streamSize, "streamSize must not be null");
        Preconditions.checkNotNull(rect2, "reportedCropRegion must not be null");
        Preconditions.checkNotNull(rect3, "previewCropRegion must not be null");
        Rect actualCrop2 = new Rect(cropRegion);
        if (!actualCrop2.intersect(rect)) {
            Log.w(TAG, "getClosestAvailableZoomCrop - Crop region out of range; setting to active array size");
            actualCrop2.set(rect);
        }
        Rect cropRegionAsPreview = shrinkToSameAspectRatioCentered(getPreviewCropRectangleUnzoomed(activeArray, streamSize), actualCrop2);
        int bestZoomIndex = -1;
        List<Rect> availableReportedCropRegions = getAvailableZoomCropRectangles(params, activeArray);
        List<Rect> availablePreviewCropRegions = getAvailablePreviewZoomCropRectangles(params, activeArray, streamSize);
        if (availableReportedCropRegions.size() == availablePreviewCropRegions.size()) {
            Rect bestPreviewCropRegion = null;
            Rect bestReportedCropRegion = null;
            int i = 0;
            while (true) {
                if (i >= availableReportedCropRegions.size()) {
                    break;
                }
                Rect currentPreviewCropRegion = availablePreviewCropRegions.get(i);
                Rect currentReportedCropRegion = availableReportedCropRegions.get(i);
                if (bestZoomIndex == -1) {
                    actualCrop = actualCrop2;
                    isBest = true;
                } else {
                    actualCrop = actualCrop2;
                    isBest = currentPreviewCropRegion.width() >= cropRegionAsPreview.width() && currentPreviewCropRegion.height() >= cropRegionAsPreview.height();
                }
                if (!isBest) {
                    break;
                }
                bestPreviewCropRegion = currentPreviewCropRegion;
                bestReportedCropRegion = currentReportedCropRegion;
                bestZoomIndex = i;
                i++;
                actualCrop2 = actualCrop;
                Rect rect4 = activeArray;
            }
            if (bestZoomIndex != -1) {
                rect2.set(bestReportedCropRegion);
                rect3.set(bestPreviewCropRegion);
                return bestZoomIndex;
            }
            throw new AssertionError("Should've found at least one valid zoom index");
        }
        throw new AssertionError("available reported/preview crop region size mismatch");
    }

    private static Rect getPreviewCropRectangleUnzoomed(Rect activeArray, Size previewSize) {
        float cropW;
        float cropH;
        if (previewSize.getWidth() > activeArray.width()) {
            throw new IllegalArgumentException("previewSize must not be wider than activeArray");
        } else if (previewSize.getHeight() <= activeArray.height()) {
            float aspectRatioArray = (((float) activeArray.width()) * 1.0f) / ((float) activeArray.height());
            float aspectRatioPreview = (((float) previewSize.getWidth()) * 1.0f) / ((float) previewSize.getHeight());
            if (((double) Math.abs(aspectRatioPreview - aspectRatioArray)) < ASPECT_RATIO_TOLERANCE) {
                cropH = (float) activeArray.height();
                cropW = (float) activeArray.width();
            } else if (aspectRatioPreview < aspectRatioArray) {
                cropH = (float) activeArray.height();
                cropW = cropH * aspectRatioPreview;
            } else {
                cropW = (float) activeArray.width();
                cropH = cropW / aspectRatioPreview;
            }
            Matrix translateMatrix = new Matrix();
            RectF cropRect = new RectF(0.0f, 0.0f, cropW, cropH);
            translateMatrix.setTranslate(activeArray.exactCenterX(), activeArray.exactCenterY());
            translateMatrix.postTranslate(-cropRect.centerX(), -cropRect.centerY());
            translateMatrix.mapRect(cropRect);
            return ParamsUtils.createRect(cropRect);
        } else {
            throw new IllegalArgumentException("previewSize must not be taller than activeArray");
        }
    }

    private static Rect shrinkToSameAspectRatioCentered(Rect reference, Rect shrinkTarget) {
        float cropW;
        float cropH;
        float aspectRatioReference = (((float) reference.width()) * 1.0f) / ((float) reference.height());
        float aspectRatioShrinkTarget = (((float) shrinkTarget.width()) * 1.0f) / ((float) shrinkTarget.height());
        if (aspectRatioShrinkTarget < aspectRatioReference) {
            cropH = (float) reference.height();
            cropW = cropH * aspectRatioShrinkTarget;
        } else {
            cropW = (float) reference.width();
            cropH = cropW / aspectRatioShrinkTarget;
        }
        Matrix translateMatrix = new Matrix();
        RectF shrunkRect = new RectF(shrinkTarget);
        translateMatrix.setScale(cropW / ((float) reference.width()), cropH / ((float) reference.height()), shrinkTarget.exactCenterX(), shrinkTarget.exactCenterY());
        translateMatrix.mapRect(shrunkRect);
        return ParamsUtils.createRect(shrunkRect);
    }

    public static List<Rect> getAvailableZoomCropRectangles(Camera.Parameters params, Rect activeArray) {
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(activeArray, "activeArray must not be null");
        return getAvailableCropRectangles(params, activeArray, ParamsUtils.createSize(activeArray));
    }

    public static List<Rect> getAvailablePreviewZoomCropRectangles(Camera.Parameters params, Rect activeArray, Size previewSize) {
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(activeArray, "activeArray must not be null");
        Preconditions.checkNotNull(previewSize, "previewSize must not be null");
        return getAvailableCropRectangles(params, activeArray, previewSize);
    }

    private static List<Rect> getAvailableCropRectangles(Camera.Parameters params, Rect activeArray, Size streamSize) {
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(activeArray, "activeArray must not be null");
        Preconditions.checkNotNull(streamSize, "streamSize must not be null");
        Rect unzoomedStreamCrop = getPreviewCropRectangleUnzoomed(activeArray, streamSize);
        if (!params.isZoomSupported()) {
            return new ArrayList(Arrays.asList(new Rect[]{unzoomedStreamCrop}));
        }
        List<Rect> zoomCropRectangles = new ArrayList<>(params.getMaxZoom() + 1);
        Matrix scaleMatrix = new Matrix();
        RectF scaledRect = new RectF();
        for (Integer intValue : params.getZoomRatios()) {
            float shrinkRatio = 100.0f / ((float) intValue.intValue());
            ParamsUtils.convertRectF(unzoomedStreamCrop, scaledRect);
            scaleMatrix.setScale(shrinkRatio, shrinkRatio, activeArray.exactCenterX(), activeArray.exactCenterY());
            scaleMatrix.mapRect(scaledRect);
            zoomCropRectangles.add(ParamsUtils.createRect(scaledRect));
        }
        return zoomCropRectangles;
    }

    public static float getMaxZoomRatio(Camera.Parameters params) {
        if (!params.isZoomSupported()) {
            return 1.0f;
        }
        List<Integer> zoomRatios = params.getZoomRatios();
        return (((float) zoomRatios.get(zoomRatios.size() - 1).intValue()) * 1.0f) / 100.0f;
    }

    private static SizeF getZoomRatio(Size activeArraySize, Size cropSize) {
        Preconditions.checkNotNull(activeArraySize, "activeArraySize must not be null");
        Preconditions.checkNotNull(cropSize, "cropSize must not be null");
        Preconditions.checkArgumentPositive(cropSize.getWidth(), "cropSize.width must be positive");
        Preconditions.checkArgumentPositive(cropSize.getHeight(), "cropSize.height must be positive");
        return new SizeF((((float) activeArraySize.getWidth()) * 1.0f) / ((float) cropSize.getWidth()), (((float) activeArraySize.getHeight()) * 1.0f) / ((float) cropSize.getHeight()));
    }

    public static ZoomData convertScalerCropRegion(Rect activeArraySize, Rect cropRegion, Size previewSize, Camera.Parameters params) {
        Rect activeArraySizeOnly = new Rect(0, 0, activeArraySize.width(), activeArraySize.height());
        Rect userCropRegion = cropRegion;
        if (userCropRegion == null) {
            userCropRegion = activeArraySizeOnly;
        }
        Rect userCropRegion2 = userCropRegion;
        Rect reportedCropRegion = new Rect();
        Rect previewCropRegion = new Rect();
        return new ZoomData(getClosestAvailableZoomCrop(params, activeArraySizeOnly, previewSize, userCropRegion2, reportedCropRegion, previewCropRegion), previewCropRegion, reportedCropRegion);
    }

    public static MeteringData convertMeteringRectangleToLegacy(Rect activeArray, MeteringRectangle meteringRect, ZoomData zoomData) {
        Camera.Area meteringArea;
        Rect previewCrop = zoomData.previewCrop;
        float scaleH = 2000.0f / ((float) previewCrop.height());
        Matrix transform = new Matrix();
        transform.setTranslate((float) (-previewCrop.left), (float) (-previewCrop.top));
        transform.postScale(2000.0f / ((float) previewCrop.width()), scaleH);
        transform.postTranslate(-1000.0f, -1000.0f);
        Rect normalizedRegionUnbounded = ParamsUtils.mapRect(transform, meteringRect.getRect());
        Rect normalizedIntersected = new Rect(normalizedRegionUnbounded);
        if (!normalizedIntersected.intersect(NORMALIZED_RECTANGLE_DEFAULT)) {
            Log.w(TAG, "convertMeteringRectangleToLegacy - metering rectangle too small, no metering will be done");
            normalizedIntersected.set(RECTANGLE_EMPTY);
            meteringArea = new Camera.Area(RECTANGLE_EMPTY, 0);
        } else {
            meteringArea = new Camera.Area(normalizedIntersected, meteringRect.getMeteringWeight());
        }
        Rect previewMetering = meteringRect.getRect();
        if (!previewMetering.intersect(previewCrop)) {
            previewMetering.set(RECTANGLE_EMPTY);
        }
        return new MeteringData(meteringArea, previewMetering, convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, new Camera.Area(normalizedRegionUnbounded, meteringRect.getMeteringWeight()), false).rect);
    }

    public static WeightedRectangle convertCameraAreaToActiveArrayRectangle(Rect activeArray, ZoomData zoomData, Camera.Area area) {
        return convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, area, true);
    }

    public static Face convertFaceFromLegacy(Camera.Face face, Rect activeArray, ZoomData zoomData) {
        Preconditions.checkNotNull(face, "face must not be null");
        WeightedRectangle faceRect = convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, new Camera.Area(face.rect, 1));
        Point leftEye = face.leftEye;
        Point rightEye = face.rightEye;
        Point mouth = face.mouth;
        if (leftEye == null || rightEye == null || mouth == null || leftEye.x == -2000 || leftEye.y == -2000 || rightEye.x == -2000 || rightEye.y == -2000 || mouth.x == -2000 || mouth.y == -2000) {
            return faceRect.toFace();
        }
        Point leftEye2 = convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye, true);
        return faceRect.toFace(face.id, leftEye2, convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye2, true), convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye2, true));
    }

    private static Point convertCameraPointToActiveArrayPoint(Rect activeArray, ZoomData zoomData, Point point, boolean usePreviewCrop) {
        WeightedRectangle adjustedRect = convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, new Camera.Area(new Rect(point.x, point.y, point.x, point.y), 1), usePreviewCrop);
        return new Point(adjustedRect.rect.left, adjustedRect.rect.top);
    }

    private static WeightedRectangle convertCameraAreaToActiveArrayRectangle(Rect activeArray, ZoomData zoomData, Camera.Area area, boolean usePreviewCrop) {
        Rect previewCrop = zoomData.previewCrop;
        Rect reportedCrop = zoomData.reportedCrop;
        Matrix transform = new Matrix();
        transform.setTranslate(1000.0f, 1000.0f);
        transform.postScale((((float) previewCrop.width()) * 1.0f) / 2000.0f, (((float) previewCrop.height()) * 1.0f) / 2000.0f);
        transform.postTranslate((float) previewCrop.left, (float) previewCrop.top);
        Rect cropToIntersectAgainst = usePreviewCrop ? previewCrop : reportedCrop;
        Rect reportedMetering = ParamsUtils.mapRect(transform, area.rect);
        if (!reportedMetering.intersect(cropToIntersectAgainst)) {
            reportedMetering.set(RECTANGLE_EMPTY);
        }
        if (area.weight < 0) {
            Log.w(TAG, "convertCameraAreaToMeteringRectangle - rectangle " + stringFromArea(area) + " has too small weight, clip to 0");
        }
        return new WeightedRectangle(reportedMetering, area.weight);
    }

    private ParameterUtils() {
        throw new AssertionError();
    }
}
