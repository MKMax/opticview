package io.github.mkmax.specfx.math.graph.cartesian;

import io.github.mkmax.jim.Disposable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class MappedPane2D extends Pane implements Disposable {

    /**
     * A functional interface whose instances may be registered to
     * be notified whenever this {@link MappedPane2D}'s transform
     * matrix changes state.
     *
     * @author Maxim Kasyanenko
     */
    public interface UpdateListener {
        void updated ();
    }

    /* Any time the updateMapping callback is called to update the transform
     * matrix, these listeners will receive a notification of the update
     * immediately after the transform matrix has been recomputed.
     */
    private final List<UpdateListener> updateListeners = new ArrayList<> ();

    /* Represents the constants used to evaluate the linear mapping from
     * the arbitrary R^2 space to the component's R^2 space. Gx and Gy are
     * the gradients (or slopes) for the X and Y axis mappings respectively.
     * Kx and Ky are the intercepts for the X and Y axis mappings respectively.
     */
    private double
        Gx, Kx,
        Gy, Ky;

    /* The four properties that define the arbitrary R^2 space that will be mapped
     * onto the component's R^2 space (X in [0, getWidth()]; Y in [0, getHeight()]).
     * We use properties to take advantage of listeners to reliably recompute
     * the transform matrix above (the mapping equations) upon any change in these
     * four properties and the dimension properties of the Pane component, i.e.,
     * widthProperty() and heightProperty().
     */
    protected final DoubleProperty
        width  = new SimpleDoubleProperty (), /* we need the width property to be able to bind to different properties */
        height = new SimpleDoubleProperty (), /* we need the height property to be able to bind to different properties */
        left   = new SimpleDoubleProperty (),
        right  = new SimpleDoubleProperty (),
        bottom = new SimpleDoubleProperty (),
        top    = new SimpleDoubleProperty ();

    /* These two properties are a reference to the current property that the
     * width and height member properties are bound to. For some reason
     * JavaFX doesn't provide a getBoundTo() method, or something similar, to
     * identify the binding point, so we have to improvise. These are used
     * in the updateMapping callback to ensure we update the component's
     * dimensions when we're not bound to widthProperty() and heightProperty().
     */
    private ObservableValue<? extends Number> widthBindingPoint;
    private ObservableValue<? extends Number> heightBindingPoint;

    /* The listener which updates the transform matrix whenever the arbitrary window
     * or the component dimensions change. We use a lambda object instead of function
     * to (1) avoid creating unnecessary duplicate lambdas, and (2) to allow the dispose()
     * function to remove this listener from any properties to prevent memory leaks.
     */
    private final ChangeListener<Object> updateMapping = (__obs, __old, __now) -> {
        /* we don't care about the parameters so they are marked with underscores */
        final double cWidth  = width.get ();
        final double cHeight = height.get ();

        /* make sure we also update the component width if we are bound to an external
         * component's dimensions. The reason we want to avoid updating the width when
         * we aren't bound is that it would cause infinite recursion and the program would
         * die.
         *
         * NOTE: Using binding points may not be correct practice and instead we
         * should compare the dimension values.
         */
        if (widthBindingPoint != widthProperty ())
            setWidth (cWidth);
        if (heightBindingPoint != heightProperty ())
            setHeight (cHeight);

        final double wLeft   = left  .get ();
        final double wRight  = right .get ();
        final double wBottom = bottom.get ();
        final double wTop    = top   .get ();

        Gx = cWidth / (wRight - wLeft);
        Kx = -Gx * wLeft;

        /* because UIs flips the Y axis, we say that the top is zero and the bottom is the actual height */
        Gy = cHeight / (wBottom - wTop);
        Ky = -Gy * wTop;

        updateListeners.forEach (UpdateListener::updated);
    };

    /* +--------------+ */
    /* | CONSTRUCTORS | */
    /* +--------------+ */

    /**
     * Creates a new {@link MappedPane2D} given the specified arbitrary
     * region.
     *
     * @param pLeft the left-most value of the arbitrary region.
     * @param pRight the right-most value of the arbitrary region.
     * @param pBottom the bottom-most value of the arbitrary region.
     * @param pTop the top-most value of the arbitrary region.
     */
    public MappedPane2D (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        width .bind (widthBindingPoint = widthProperty ());
        height.bind (heightBindingPoint = heightProperty ());
        left  .set (pLeft);
        right .set (pRight);
        bottom.set (pBottom);
        top   .set (pTop);

        /* Make sure to register the listeners or this thing breaks */
        width .addListener (updateMapping);
        height.addListener (updateMapping);
        left  .addListener (updateMapping);
        right .addListener (updateMapping);
        bottom.addListener (updateMapping);
        top   .addListener (updateMapping);

        /* We invoke the updateMapping listener to initialize the mapping equations*/
        updateMapping.changed (null, null, null);
    }

    /**
     * Creates a new {@link MappedPane2D} with a square arbitrary region
     * centered at the origin. This is equivalent to
     * {@code MappedPane2D(-pUniform, pUniform, -pUniform, pUniform)}.
     *
     * @param pUniform The left, right, bottom, and top component of the
     *                 arbitrary region. This value is negated for {@code left}
     *                 and {@code right}.
     */
    public MappedPane2D (double pUniform) {
        this (
            -pUniform,
             pUniform,
            -pUniform,
             pUniform);
    }

    /**
     * Creates a new {@link MappedPane2D} with a unit square arbitrary
     * region centered at the origin. This is equivalent to
     * {@code MappedPane2D(1)}.
     */
    public MappedPane2D () {
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
     * Registers an {@link UpdateListener} with this {@link MappedPane2D}.
     * <p>
     * An {@link UpdateListener} is invoked every time the transform
     * matrix for this pane is recomputed. That is, any time the state
     * is changed so that {@link #mapX(double)}, {@link #mapY(double)},
     * {@link #unmapX(double)}, etc., yield a different result, an update
     * listener is invoked. Multiple registrations are allowed, and an
     * update listener is invoked as many times as it has been registered.
     *
     * @param listener the update listener to register.
     */
    public void addUpdateListener (UpdateListener listener) {
        if (listener != null) updateListeners.add (listener);
    }

    /**
     * Removes the given {@link UpdateListener} from this {@link MappedPane2D}.
     * <p>
     * If multiple instances of the same update listener were registered, only
     * one of the instances is removed. To remove all instances, use
     * {@link #removeAllUpdateListeners(UpdateListener)}.
     *
     * @param listener the listener to remove from this {@link MappedPane2D}.
     */
    public void removeUpdateListener (UpdateListener listener) {
        updateListeners.remove (listener);
    }

    /**
     * Removes all instances of the given {@link UpdateListener} from
     * this {@link MappedPane2D}.
     *
     * @param listener the listener to completely remove from this {@link MappedPane2D}.
     */
    public void removeAllUpdateListeners (UpdateListener listener) {
        updateListeners.removeIf (i -> i == listener);
    }

    /**
     * Binds <b>this</b> {@link MappedPane2D} to {@code lead}.
     * <p>
     * Binding allows this object to automatically inherit the state
     * of the {@code lead} pane whenever it changes. When bound, a pane
     * will not receive updates from its own width/height properties. It
     * is also worthy to note that, when bound, many setter methods will
     * now throw an exception as modifying a bound property is undefined.
     *
     * @param lead The pane to bind to.
     */
    public void bind (MappedPane2D lead) {
        /* binding will automatically fetch the correct values so we don't have to do anything else */
        left  .bind (lead.left);
        right .bind (lead.right);
        bottom.bind (lead.bottom);
        top   .bind (lead.top);
        width .bind (widthBindingPoint = lead.width);
        height.bind (heightBindingPoint = lead.height);
    }

    /**
     * Unbinds <b>this</b> {@link MappedPane2D} from another pane,
     * if any.
     * <p>
     * This will restore the width/height dependence of this pane to
     * its own properties and it will once again become an independent
     * pane component.
     */
    public void unbind () {
        /* unbinding will leave the properties the same, however,
         * the width/height will be rebound to withProperty() and
         * heightProperty() respectively, automatically updating
         * the width/height attributes to the current width/height
         * of the component.
         */
        left  .unbind ();
        right .unbind ();
        bottom.unbind ();
        top   .unbind ();
        width .bind (widthBindingPoint = widthProperty ());
        height.bind (heightBindingPoint = heightProperty ());
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
        /* ensure the properties are not bound to another MappedPane2D */
        width .unbind ();
        height.unbind ();
        left  .unbind ();
        right .unbind ();
        bottom.unbind ();
        top   .unbind ();

        /* now remove the listeners */
        width .removeListener (updateMapping);
        height.removeListener (updateMapping);
        left  .removeListener (updateMapping);
        right .removeListener (updateMapping);
        bottom.removeListener (updateMapping);
        top   .removeListener (updateMapping);
    }
}
