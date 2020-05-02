package io.github.mathfx.cartesian;

import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.Interval;
import io.github.mathfx.util.ObservableGroup;
import io.github.mathfx.util.css.CssMetaDataBuilder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public final class GraphView extends StackPane implements Disposable {

    private final GuideContainer guides = new GuideContainer ();

    private final DoubleProperty left   = new SimpleDoubleProperty (-1d);
    private final DoubleProperty right  = new SimpleDoubleProperty ( 1d);
    private final DoubleProperty bottom = new SimpleDoubleProperty (-1d);
    private final DoubleProperty top    = new SimpleDoubleProperty ( 1d);

    private final ObservableGroup<Number> orthoGroup      = new ObservableGroup<> (left, right, bottom, top);
    private final ObservableGroup<Number> dimensionsGroup = new ObservableGroup<> (widthProperty (), heightProperty ());
    private final ObservableGroup<Number> projectionGroup = new ObservableGroup<> (orthoGroup, dimensionsGroup);

    private double Mx = 1d;
    private double Kx = 0d;

    private double My = 1d;
    private double Ky = 0d;

    public GraphView () {
        projectionGroup.add (this::reproject);

        getChildren ().addAll (guides);
    }

    /* +-----------------+ */
    /* | GETTERS/SETTERS | */
    /* +-----------------+ */

    /* LEFT */
    public double getLeft () { return left.get (); }
    public void setLeft (double nLeft) { left.set (nLeft); }
    public DoubleProperty leftProperty () { return left; }

    /* RIGHT */
    public double getRight () { return right.get (); }
    public void setRight (double nRight) { right.set (nRight); }
    public DoubleProperty rightProperty () { return right; }

    /* BOTTOM */
    public double getBottom () { return bottom.get (); }
    public void setBottom (double nBottom) { bottom.set (nBottom); }
    public DoubleProperty bottomProperty () { return bottom; }

    /* TOP */
    public double getTop () { return top.get (); }
    public void setTop (double nTop) { top.set (nTop); }
    public DoubleProperty topProperty () { return top; }

    /**
     * Returns an observable group that monitors the changes in
     * the {@code left, right, bottom,} and {@code top} properties.
     * <p>
     * This may be used by components that are contained within this
     * {@link GraphView} to observe changes in the viewing window and
     * change accordingly. A good example of such component is the
     * {@link GuideContainer}.
     *
     * @return The observable group monitoring the window bounds.
     */
    public ObservableGroup<Number> getOrthoGroup () {
        return orthoGroup;
    }

    /* +---------+ */
    /* | UTILITY | */
    /* +---------+ */

    /**
     * Returns the interval on which the X coordinates are displayed
     * in the graph window.
     * <p>
     * This is equivalent to {@code new Interval(getLeft(), getRight())}.
     *
     * @return The horizontal interval.
     */
    public Interval getHorizontalWindowInterval () {
        return new Interval (getLeft (), getRight ());
    }

    /**
     * Returns the interval on which the Y coordinates are displayed
     * in the graph window.
     * <p>
     * This is equivalent to {@code new Interval(getBottom(), getTop())}.
     *
     * @return The vertical interval.
     */
    public Interval getVerticalWindowInterval () {
        return new Interval (getBottom (), getTop ());
    }

    /**
     * Returns the interval of the component's horizontal span.
     *
     * @return The horizontal span interval.
     */
    public Interval getHorizontalComponentInterval () {
        return new Interval (0d, getWidth ());
    }

    /**
     * Returns the interval of the component's vertical span.
     *
     * @return The vertical span interval.
     */
    public Interval getVerticalComponentInterval () {
        return new Interval (0d, getHeight ());
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
        return Mx * p + Kx;
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
        return My * p + Ky;
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
        return (q - Kx) / Mx;
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
        return (q - Ky) / My;
    }

    @Override
    public void dispose () {
        /* I opt to dispose each group individually instead of invoking
         * projectionGroup.dispose(true) to make the code more clear of
         * its purpose.
         */
        orthoGroup.dispose ();
        dimensionsGroup.dispose ();
        projectionGroup.dispose ();
    }

    public void reproject () {
        final double width  = getWidth ();
        final double height = getHeight ();

        final double left   = getLeft ();
        final double right  = getRight ();
        final double bottom = getBottom ();
        final double top    = getTop ();

        Mx = width / (right - left);
        Kx = -Mx * left;

        My = height / (bottom - top);
        Ky = -My * top;
    }
}
