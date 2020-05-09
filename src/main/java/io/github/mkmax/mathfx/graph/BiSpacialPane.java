package io.github.mkmax.mathfx.graph;

import io.github.mkmax.mathfx.Disposable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * An extension to the {@link Pane} component that provides
 * mapping from an arbitrary region in {@code R^2} space to
 * the region bounded by {@code X in [0, getWidth()]} and
 * {@code Y in [0, getHeight()]}.
 *
 * @author Maxim Kasyanenko
 */
public class BiSpacialPane extends Pane implements Disposable {

    /**
     * Provides a way to listen for changes in the transform matrix
     * of a {@link BiSpacialPane}.
     *
     * @author Maxim Kasyanenko
     */
    public interface TransformListener {
        /**
         * Invoked whenever {@code of} changes its transform matrix.
         * <p>
         * Modifying the state of {@code of} while in this method will
         * result in an infinite loop and a possible stack overflow.
         *
         * @param of The pane whose transform matrix changed. The state
         *           of this object <b>must not</b> be modified.
         */
        void changed (BiSpacialPane of);
    }

    /* Represents the constants used to evaluate the linear mapping from
     * the arbitrary R^2 space to the component's R^2 space. Gx and Gy are
     * the gradients (or slopes) for the X and Y axis mappings respectively.
     * Kx and Ky are the intercepts for the X and Y axis mappings respectively.
     */
    private double Gx, Kx;
    private double Gy, Ky;

    /* The four properties that define the arbitrary R^2 space that will be mapped
     * onto the component's R^2 space (X in [0, getWidth()]; Y in [0, getHeight()]).
     * We use properties to take advantage of listeners to reliably recompute
     * the transform matrix above (the mapping equations) upon any change in these
     * four properties and the dimension properties of the Pane component, i.e.,
     * widthProperty() and heightProperty().
     */
    protected final DoubleProperty
        left   = new SimpleDoubleProperty (),
        right  = new SimpleDoubleProperty (),
        bottom = new SimpleDoubleProperty (),
        top    = new SimpleDoubleProperty ();

    /* I've debated whether this feature should be added, but binding enables
     * any BiSpacialPane to automatically update the arbitrary and component
     * region state as well as the transform matrix state whenever the state
     * of this object changes. The only issue is that whenever a property is
     * changed, it will emit a change event that will be captured by the
     * updateMapping listener. There are two options to handle this situation:
     * (1) create a flag to prevent the listener from doing anything, or (2)
     * let the listener recompute the transform matrix every single time a
     * member is set. To facilitate correct practices, I will opt the
     * binding mechanism to follow the latter solution by continuously
     * recomputing the transform matrix as the members are copied.
     */
    private final List<BiSpacialPane> bindings = new ArrayList<> ();

    /* A collection of callbacks that are invoked whenever updateMapping(...) below
     * receives a change event from any one property of the arbitrary or component
     * region.
     */
    private final List<TransformListener> transformListeners = new ArrayList<> ();

    /* The listener which updates the transform matrix whenever the arbitrary window
     * or the component dimensions change. We use a lambda object instead of function
     * to (1) avoid creating unnecessary duplicate lambdas, and (2) to allow the dispose()
     * function to remove this listener from any properties to prevent memory leaks.
     */
    private final ChangeListener<Object> updateMapping = (__obs, __old, __now) -> {
        /* we don't care about the parameters so they are marked with underscores */
        final double width   = getWidth ();
        final double height  = getHeight ();

        final double wLeft   = left  .get ();
        final double wRight  = right .get ();
        final double wBottom = bottom.get ();
        final double wTop    = top   .get ();

        Gx = width / (wRight - wLeft);
        Kx = -Gx * wLeft;

        /* because UIs flips the Y axis, we say that the top is zero and the bottom is the actual height */
        Gy = height / (wBottom - wTop);
        Ky = -Gy * wTop;

        /* we warn the client implementation that changing the state
         * of this pane within any of the callbacks will result in an
         * infinite loop as we are not going to attempt to prevent such
         * errors.
         */
        transformListeners.forEach (i -> i.changed (BiSpacialPane.this));
    };

    /* +--------------+ */
    /* | CONSTRUCTORS | */
    /* +--------------+ */

    /**
     * Creates a new {@link BiSpacialPane} given the specified arbitrary
     * region.
     *
     * @param pLeft the left-most value of the arbitrary region.
     * @param pRight the right-most value of the arbitrary region.
     * @param pBottom the bottom-most value of the arbitrary region.
     * @param pTop the top-most value of the arbitrary region.
     */
    public BiSpacialPane (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        left  .set (pLeft);
        right .set (pRight);
        bottom.set (pBottom);
        top   .set (pTop);

        /* Make sure to register the listeners or this thing breaks */
        widthProperty  ().addListener (updateMapping);
        heightProperty ().addListener (updateMapping);
        left             .addListener (updateMapping);
        right            .addListener (updateMapping);
        bottom           .addListener (updateMapping);
        top              .addListener (updateMapping);

        /* We invoke the updateMapping listener to initialize the mapping equations*/
        updateMapping.changed (null, null, null);
    }

    /**
     * Creates a new {@link BiSpacialPane} with a square arbitrary region
     * centered at the origin. This is equivalent to
     * {@code BiSpacialPane(-pUniform, pUniform, -pUniform, pUniform)}.
     *
     * @param pUniform The left, right, bottom, and top component of the
     *                 arbitrary region. This value is negated for {@code left}
     *                 and {@code right}.
     */
    public BiSpacialPane (double pUniform) {
        this (
            -pUniform,
             pUniform,
            -pUniform,
             pUniform);
    }

    /**
     * Creates a new {@link BiSpacialPane} with a unit square arbitrary
     * region centered at the origin. This is equivalent to
     * {@code BiSpacialPane(1)}.
     */
    public BiSpacialPane () {
        this (1d);
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

    /* RIGHT */
    public double getRight () {
        return right.get ();
    }

    public void setRight (double nRight) {
        right.set (nRight);
    }

    /* BOTTOM */
    public double getBottom () {
        return bottom.get ();
    }

    public void setBottom (double nBottom) {
        bottom.set (nBottom);
    }

    /* TOP */
    public double getTop () {
        return top.get ();
    }

    public void setTop (double nTop) {
        top.set (nTop);
    }

    /* +---------+ */
    /* | UTILITY | */
    /* +---------+ */

    /**
     * Registers a listener that will be notified whenever the transform
     * from the arbitrary to component region changes.
     * <p>
     * If {@code ls == null}, this method does nothing. However, if
     * {@code ls} has already been registered, it will be registered again
     * and will receive multiple updates per event.
     *
     * @see TransformListener for a warning about what should be avoided
     *                        when handling events.
     *
     * @param ls The listener to register.
     */
    public void addTransformListener (TransformListener ls) {
        if (ls != null) transformListeners.add (ls);
    }

    /**
     * Removes the specified transform listener.
     * <p>
     * If {@code ls} has been registered multiple times, this
     * function will remove the first instance that was registered
     * only.
     *
     * @see TransformListener for a warning about what should be avoided
     *                        when handling events.
     *
     * @param ls The listener to remove.
     */
    public void removeTransformListener (TransformListener ls) {
        transformListeners.remove (ls);
    }

    /**
     * Maps an X coordinate within the arbitrary region to the component region.
     *
     * @param p The X coordinate in the arbitrary region.
     * @return The corresponding X coordinate in the component region.
     */
    public double mapX (double p) {
        return Gx * p + Kx;
    }

    /**
     * Maps a Y coordinate within the arbitrary region to the component region.
     *
     * @param p The Y coordinate in the arbitrary region.
     * @return The corresponding Y coordinate in the component region.
     */
    public double mapY (double p) {
        return Gy * p + Ky;
    }

    /**
     * Maps an X coordinate within the component region to the arbitrary region.
     * <p>
     * This is the inverse of {@link #mapX(double)}.
     *
     * @param q The X coordinate in the component region.
     * @return The corresponding X coordinate in the arbitrary region.
     */
    public double unmapX (double q) {
        return (q - Kx) / Gx;
    }

    /**
     * Maps a Y coordinate within the component region to the arbitrary region.
     * <p>
     * This is the inverse of {@link #mapY(double)}.
     *
     * @param q The Y coordinate in the component region.
     * @return The corresponding Y coordinate in the arbitrary region.
     */
    public double unmapY (double q) {
        return (q - Ky) / Gy;
    }

    /**
     * Releases event listeners to prevent memory leaks. Calling this
     * method will invalidate this object and any interaction will be
     * undefined. Thus, it is recommended to lose any reference to this
     * object and allow it to be garbage collected.
     */
    @Override
    public void dispose () {
        widthProperty  ().removeListener (updateMapping);
        heightProperty ().removeListener (updateMapping);
        left             .removeListener (updateMapping);
        right            .removeListener (updateMapping);
        bottom           .removeListener (updateMapping);
        top              .removeListener (updateMapping);

        transformListeners.clear ();
    }

    private void
}
