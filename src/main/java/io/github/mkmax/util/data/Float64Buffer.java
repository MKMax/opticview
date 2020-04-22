package io.github.mkmax.util.data;

import io.github.mkmax.util.math.Int32;

public class Float64Buffer {

    private static final int DEFAULT_SIZE = 8;

    private double[] buffer;

    public Float64Buffer (int size) {
        ensureCapacity (size);
    }

    public Float64Buffer () {
        this (DEFAULT_SIZE);
    }

    public double read (int srcIndex) {
        return buffer[ensureIndex (srcIndex)];
    }

    public int read (double[] out, int srcIndex) {
        if (out == null || out.length == 0)
            return 0;
        ensureIndex (srcIndex);
        final int length = Math.min (buffer.length - srcIndex, out.length);
        System.arraycopy (buffer, srcIndex, out, 0, length);
        return length;
    }

    public int read (double[] out, int srcIndex, int destIndex) {
        if (out == null || destIndex < 0 || out.length - destIndex <= 0)
            return 0;
        ensureIndex (srcIndex);
        final int length = Math.min (buffer.length - srcIndex, out.length - destIndex);
        System.arraycopy (buffer, srcIndex, out, destIndex, length);
        return length;
    }

    public void write (int index, double val) {
        buffer[ensureIndex (index)] = val;
    }

    public int getCapacity () {
        return buffer.length;
    }

    public int ensureCapacity (int size) {
        return ensureCapacity (size, false);
    }

    public int ensureCapacity (int size, boolean allowDownsize) {
        int nSize = Int32.clog (Math.max (size, DEFAULT_SIZE));
        if (nSize < buffer.length && !allowDownsize || nSize == buffer.length)
            return buffer.length;
        double[] nBuffer = new double[nSize];
        System.arraycopy (buffer, 0, nBuffer, 0, Math.min (buffer.length, nSize));
        buffer = nBuffer;
        return buffer.length;
    }

    public int ensureIndex (int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException ("index < 0");
        if (index >= buffer.length)
            throw new IndexOutOfBoundsException ("index >= getCapacity()");
        return index;
    }
}
