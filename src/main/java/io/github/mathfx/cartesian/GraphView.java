package io.github.mathfx.cartesian;

import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.ObservableGroup;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;

public final class GraphView extends StackPane implements Disposable {

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */

    private final DoubleProperty
        left   = new SimpleDoubleProperty (),
        right  = new SimpleDoubleProperty (),
        bottom = new SimpleDoubleProperty (),
        top    = new SimpleDoubleProperty ();

    private double Gx, Kx;
    private double Gy, Ky;

    private final ObservableGroup<Number> windowGroup     = new ObservableGroup<> (left, right, bottom, top);
    private final ObservableGroup<Number> sizeGroup       = new ObservableGroup<> (widthProperty (), heightProperty ());
    private final ObservableGroup<Number> projectionGroup = new ObservableGroup<> (windowGroup, sizeGroup);

    private final GuidePanel guidepanel = new GuidePanel ();

    public GraphView (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        left  .set (pLeft);
        right .set (pRight);
        bottom.set (pBottom);
        top   .set (pTop);
        reproject ();
        postInit ();
    }

    public GraphView (double pUniform) {
        this (
            -pUniform,
             pUniform,
            -pUniform,
             pUniform);
    }

    public GraphView () {
        this (1d);
    }

    private void postInit () {
        getChildren ().addAll (
            guidepanel);
        projectionGroup.add (this::reproject);
    }

    /* +-----------------+ */
    /* | GETTERS/SETTERS | */
    /* +-----------------+ */

    /* LEFT */
    public double getLeft () {
        return left.get ();
    }

    public void setLeft (double nLeft) {
        left.set (nLeft);
    }

    public DoubleProperty leftProperty () {
        return left;
    }

    /* RIGHT */
    public double getRight () {
        return right.get ();
    }

    public void setRight (double nRight) {
        right.set (nRight);
    }

    public DoubleProperty rightProperty () {
        return right;
    }

    /* BOTTOM */
    public double getBottom () {
        return bottom.get ();
    }

    public void setBottom (double nBottom) {
        bottom.set (nBottom);
    }

    public DoubleProperty bottomProperty () {
        return bottom;
    }

    /* TOP */
    public double getTop () {
        return top.get ();
    }

    public void setTop (double nTop) {
        top.set (nTop);
    }

    public DoubleProperty topProperty () {
        return top;
    }

    /* +---------+ */
    /* | UTILITY | */
    /* +---------+ */

    public ObservableGroup<Number> getWindowGroup () {
        return windowGroup;
    }

    public ObservableGroup<Number> getSizeGroup () {
        return sizeGroup;
    }

    public ObservableGroup<Number> getProjectionGroup () {
        return projectionGroup;
    }

    /**
     * Maps a window space X coordinate to component or viewport
     * space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param p The window space X coordinate.
     * @return The viewport space X coordinate.
     */
    public double projectX (double p) {
        return Gx * p + Kx;
    }

    /**
     * Maps a window space Y coordinate to component or viewport
     * space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param p The window space Y coordinate.
     * @return The viewport space Y coordinate.
     */
    public double projectY (double p) {
        return Gy * p + Ky;
    }

    /**
     * Maps a component or viewport space X coordinate back to
     * window space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param q The viewport space X coordinate.
     * @return The window space X coordinate.
     */
    public double unprojectX (double q) {
        return (q - Kx) / Gx;
    }

    /**
     * Maps a component or viewport space Y coordinate back to
     * window space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param q The viewport space Y coordinate.
     * @return The window space Y coordinate.
     */
    public double unprojectY (double q) {
        return (q - Ky) / Gy;
    }

    /* +----------------------------------+ */
    /* | PRIVATE, LISTENERS, & MANAGEMENT | */
    /* +----------------------------------+ */

    @Override
    public void dispose () {
        windowGroup.dispose ();
        sizeGroup.dispose ();
        projectionGroup.dispose ();
    }

    private void reproject () {
        final double width  = getWidth ();
        final double height = getHeight ();

        final double left   = getLeft ();
        final double right  = getRight ();
        final double bottom = getBottom ();
        final double top    = getTop ();

        Gx = width / (right - left);
        Kx = -Gx * left;

        Gy = height / (bottom - top);
        Ky = -Gy * top;
    }
}
