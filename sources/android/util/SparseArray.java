package android.util;

import android.annotation.UnsupportedAppUsage;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import libcore.util.EmptyArray;

public class SparseArray<E> implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int[] mKeys;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mSize;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private Object[] mValues;

    public SparseArray() {
        this(10);
    }

    public SparseArray(int initialCapacity) {
        this.mGarbage = false;
        if (initialCapacity == 0) {
            this.mKeys = EmptyArray.INT;
            this.mValues = EmptyArray.OBJECT;
        } else {
            this.mValues = ArrayUtils.newUnpaddedObjectArray(initialCapacity);
            this.mKeys = new int[this.mValues.length];
        }
        this.mSize = 0;
    }

    public SparseArray<E> clone() {
        SparseArray<E> clone = null;
        try {
            clone = (SparseArray) super.clone();
            clone.mKeys = (int[]) this.mKeys.clone();
            clone.mValues = (Object[]) this.mValues.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return clone;
        }
    }

    public E get(int key) {
        return get(key, (Object) null);
    }

    public E get(int key, E valueIfKeyNotFound) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i < 0 || this.mValues[i] == DELETED) {
            return valueIfKeyNotFound;
        }
        return this.mValues[i];
    }

    public void delete(int key) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0 && this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
        }
    }

    public E removeReturnOld(int key) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i < 0 || this.mValues[i] == DELETED) {
            return null;
        }
        E old = this.mValues[i];
        this.mValues[i] = DELETED;
        this.mGarbage = true;
        return old;
    }

    public void remove(int key) {
        delete(key);
    }

    public void removeAt(int index) {
        if (index >= this.mSize && UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else if (this.mValues[index] != DELETED) {
            this.mValues[index] = DELETED;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int index, int size) {
        int end = Math.min(this.mSize, index + size);
        for (int i = index; i < end; i++) {
            removeAt(i);
        }
    }

    private void gc() {
        int n = this.mSize;
        int[] keys = this.mKeys;
        Object[] values = this.mValues;
        int o = 0;
        for (int i = 0; i < n; i++) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    public void put(int key, E value) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
            return;
        }
        int i2 = ~i;
        if (i2 >= this.mSize || this.mValues[i2] != DELETED) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
                i2 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
            }
            this.mKeys = GrowingArrayUtils.insert(this.mKeys, this.mSize, i2, key);
            this.mValues = GrowingArrayUtils.insert((T[]) this.mValues, this.mSize, i2, value);
            this.mSize++;
            return;
        }
        this.mKeys[i2] = key;
        this.mValues[i2] = value;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public int keyAt(int index) {
        if (index < this.mSize || !UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            if (this.mGarbage) {
                gc();
            }
            return this.mKeys[index];
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public E valueAt(int index) {
        if (index < this.mSize || !UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            if (this.mGarbage) {
                gc();
            }
            return this.mValues[index];
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public void setValueAt(int index, E value) {
        if (index < this.mSize || !UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            if (this.mGarbage) {
                gc();
            }
            this.mValues[index] = value;
            return;
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public int indexOfKey(int key) {
        if (this.mGarbage) {
            gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
    }

    public int indexOfValue(E value) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfValueByValue(E value) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (value == null) {
                if (this.mValues[i] == null) {
                    return i;
                }
            } else if (value.equals(this.mValues[i])) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        int n = this.mSize;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void append(int key, E value) {
        if (this.mSize == 0 || key > this.mKeys[this.mSize - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
            }
            this.mKeys = GrowingArrayUtils.append(this.mKeys, this.mSize, key);
            this.mValues = GrowingArrayUtils.append((T[]) this.mValues, this.mSize, value);
            this.mSize++;
            return;
        }
        put(key, value);
    }

    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(keyAt(i));
            buffer.append('=');
            Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }
}
