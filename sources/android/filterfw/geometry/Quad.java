package android.filterfw.geometry;

import android.annotation.UnsupportedAppUsage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Quad {
    @UnsupportedAppUsage
    public Point p0;
    @UnsupportedAppUsage
    public Point p1;
    @UnsupportedAppUsage
    public Point p2;
    @UnsupportedAppUsage
    public Point p3;

    @UnsupportedAppUsage
    public Quad() {
    }

    @UnsupportedAppUsage
    public Quad(Point p02, Point p12, Point p22, Point p32) {
        this.p0 = p02;
        this.p1 = p12;
        this.p2 = p22;
        this.p3 = p32;
    }

    public boolean IsInUnitRange() {
        return this.p0.IsInUnitRange() && this.p1.IsInUnitRange() && this.p2.IsInUnitRange() && this.p3.IsInUnitRange();
    }

    public Quad translated(Point t) {
        return new Quad(this.p0.plus(t), this.p1.plus(t), this.p2.plus(t), this.p3.plus(t));
    }

    public Quad translated(float x, float y) {
        return new Quad(this.p0.plus(x, y), this.p1.plus(x, y), this.p2.plus(x, y), this.p3.plus(x, y));
    }

    public Quad scaled(float s) {
        return new Quad(this.p0.times(s), this.p1.times(s), this.p2.times(s), this.p3.times(s));
    }

    public Quad scaled(float x, float y) {
        return new Quad(this.p0.mult(x, y), this.p1.mult(x, y), this.p2.mult(x, y), this.p3.mult(x, y));
    }

    public Rectangle boundingBox() {
        List<Float> xs = Arrays.asList(new Float[]{Float.valueOf(this.p0.x), Float.valueOf(this.p1.x), Float.valueOf(this.p2.x), Float.valueOf(this.p3.x)});
        List<Float> ys = Arrays.asList(new Float[]{Float.valueOf(this.p0.y), Float.valueOf(this.p1.y), Float.valueOf(this.p2.y), Float.valueOf(this.p3.y)});
        float x0 = ((Float) Collections.min(xs)).floatValue();
        float y0 = ((Float) Collections.min(ys)).floatValue();
        return new Rectangle(x0, y0, ((Float) Collections.max(xs)).floatValue() - x0, ((Float) Collections.max(ys)).floatValue() - y0);
    }

    public float getBoundingWidth() {
        List<Float> xs = Arrays.asList(new Float[]{Float.valueOf(this.p0.x), Float.valueOf(this.p1.x), Float.valueOf(this.p2.x), Float.valueOf(this.p3.x)});
        return ((Float) Collections.max(xs)).floatValue() - ((Float) Collections.min(xs)).floatValue();
    }

    public float getBoundingHeight() {
        List<Float> ys = Arrays.asList(new Float[]{Float.valueOf(this.p0.y), Float.valueOf(this.p1.y), Float.valueOf(this.p2.y), Float.valueOf(this.p3.y)});
        return ((Float) Collections.max(ys)).floatValue() - ((Float) Collections.min(ys)).floatValue();
    }

    public String toString() {
        return "{" + this.p0 + ", " + this.p1 + ", " + this.p2 + ", " + this.p3 + "}";
    }
}
