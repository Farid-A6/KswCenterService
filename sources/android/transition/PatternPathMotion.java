package android.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.PathParser;
import com.android.internal.R;

public class PatternPathMotion extends PathMotion {
    private Path mOriginalPatternPath;
    private final Path mPatternPath = new Path();
    private final Matrix mTempMatrix = new Matrix();

    public PatternPathMotion() {
        this.mPatternPath.lineTo(1.0f, 0.0f);
        this.mOriginalPatternPath = this.mPatternPath;
    }

    public PatternPathMotion(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PatternPathMotion);
        try {
            String pathData = a.getString(0);
            if (pathData != null) {
                setPatternPath(PathParser.createPathFromPathData(pathData));
                return;
            }
            throw new RuntimeException("pathData must be supplied for patternPathMotion");
        } finally {
            a.recycle();
        }
    }

    public PatternPathMotion(Path patternPath) {
        setPatternPath(patternPath);
    }

    public Path getPatternPath() {
        return this.mOriginalPatternPath;
    }

    public void setPatternPath(Path patternPath) {
        Path path = patternPath;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        float[] pos = new float[2];
        pathMeasure.getPosTan(length, pos, (float[]) null);
        float endX = pos[0];
        float endY = pos[1];
        pathMeasure.getPosTan(0.0f, pos, (float[]) null);
        float startX = pos[0];
        float startY = pos[1];
        if (startX == endX && startY == endY) {
            throw new IllegalArgumentException("pattern must not end at the starting point");
        }
        this.mTempMatrix.setTranslate(-startX, -startY);
        float dx = endX - startX;
        float dy = endY - startY;
        float scale = 1.0f / ((float) Math.hypot((double) dx, (double) dy));
        this.mTempMatrix.postScale(scale, scale);
        PathMeasure pathMeasure2 = pathMeasure;
        float f = startX;
        float f2 = length;
        float[] fArr = pos;
        this.mTempMatrix.postRotate((float) Math.toDegrees(-Math.atan2((double) dy, (double) dx)));
        path.transform(this.mTempMatrix, this.mPatternPath);
        this.mOriginalPatternPath = path;
    }

    public Path getPath(float startX, float startY, float endX, float endY) {
        double dx = (double) (endX - startX);
        double dy = (double) (endY - startY);
        float length = (float) Math.hypot(dx, dy);
        double angle = Math.atan2(dy, dx);
        this.mTempMatrix.setScale(length, length);
        this.mTempMatrix.postRotate((float) Math.toDegrees(angle));
        this.mTempMatrix.postTranslate(startX, startY);
        Path path = new Path();
        this.mPatternPath.transform(this.mTempMatrix, path);
        return path;
    }
}
