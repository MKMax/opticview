package io.github.mkmax.fxe.math.graph.cartesian.axes;

import java.util.Objects;

public class AxisMark {

    public enum Degree {
        MAJOR,
        MINOR
    }

    /* The degree is the "importance" of the mark, something that the
     * renderer takes into account when considering the color and/or boldness
     * of an axis mark. Typically, a higher degree indicates a more "colorful"
     * or "bold" mark.
     */
    private Degree  degree;

    /* Describes the recommended position of the axis mark on the interval
     * on which it was calculated on. In other words, this is a single
     * value describing the location in "function space", of where a mark
     * should be displayed. A renderer should transform this value into
     * viewport space before rendering.
     */
    private double  position;

    /* Tells whether this axis mark possibly overlaps with another.
     */
    private boolean overlap;

    protected AxisMark () {
        /* AxisMarker implementations should
         * typically reuse these objects.
         */
        degree   = Degree.MAJOR;
        position = 0d;
        overlap  = false;
    }

    protected AxisMark (
        Degree pDegree,
        double pPosition)
    {
        /* If AxisMarker implementions do not reuse AxisMark objects,
         * then by all means create them like this.
         */
        setDegree (pDegree);
        setPosition (pPosition);
    }

    public Degree getDegree () {
        return degree;
    }

    protected void setDegree (Degree nDegree) {
        degree = Objects.requireNonNull (nDegree);
    }

    public double getPosition () {
        return position;
    }

    protected void setPosition (double nPosition) {
        position = nPosition;
    }

    public boolean doesOverlap () {
        return overlap;
    }

    protected void setOverlap (boolean nOverlap) {
        overlap = nOverlap;
    }
}
