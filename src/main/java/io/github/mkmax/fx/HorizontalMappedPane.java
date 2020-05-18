package io.github.mkmax.fx;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A specialized {@link Pane} component that automatically maps
 * an arbitrary region {@code [x0, x1]} to {@code [0, getWidth()]}.
 * <p>
 * {@code x0} and {@code x1} above are for illustration purposes, the
 * members represent the aforementioned values are {@link #left} and
 * {@link #right}. To ensure minimal confusion, let {@code Arbitrary Space}
 * be defined as {@code [x0, x1]} and let {@code Component Space} be defined
 * as {@code [0, getWidth()]}.
 *
 * @author Maxim Kasyanenko
 */
public class HorizontalMappedPane
    extends    Pane
    implements ObservableValue<HorizontalMappedPane.ObservableState>
{
    /**
     * A struct-like utility class that is used to send out
     * change events whenever the state of a {@link HorizontalMappedPane}
     * is modified.
     *
     * @author Maxim Kasyanenko
     */
    public static final class ObservableState {

        /* a back reference to the HorizontalMappedPane that issued this state */
        private final HorizontalMappedPane origin;

        /* the properties of interest maintained by HorizontalMappedPane */
        private double width, left, right;

        /**
         * Creates a new {@link ObservableState} object with the given
         * {@link HorizontalMappedPane} responsible for maintaining it.
         *
         * @param pOrigin the source {@link HorizontalMappedPane}.
         */
        private ObservableState (HorizontalMappedPane pOrigin) {
            origin = Objects.requireNonNull (pOrigin, "origin must be specified");
        }

        /**
         * Returns the {@link HorizontalMappedPane} that maintains this
         * state object.
         *
         * @return the source {@link HorizontalMappedPane}.
         */
        public HorizontalMappedPane getOrigin () {
            return origin;
        }

        /**
         * Returns the value of the width property within
         * this current state.
         *
         * @return the value of the width property.
         */
        public double getWidth () {
            return width;
        }

        /**
         * Returns the value of the left property within
         * this current state.
         *
         * @return the value of the left property.
         */
        public double getLeft () {
            return left;
        }

        /**
         * Returns the value of the right property within
         * this current state.
         *
         * @return the value of the right property.
         */
        public double getRight () {
            return right;
        }

        /**
         * Copies the width, left, and right property values of the
         * specified {@link ObservableState}.
         *
         * @param other the other {@link ObservableState} whose state to
         *              copy.
         */
        private void copy (ObservableState other) {
            width = other.width;
            left  = other.left;
            right = other.right;
        }
    }

    /* the collection of state change listeners that will be invoked eagerly */
    private final List<InvalidationListener> stateInvalidationListeners = new ArrayList<> ();

    /* flag used to stop repeated invocations of the invalidation listeners unnecessarily */
    private boolean valid = true;

    /**
     * Adds an {@link InvalidationListener} that will be notified whenever
     * the state of this {@link HorizontalMappedPane} becomes invalid.
     * <p>
     * If {@code listener == null}, this method does nothing.
     *
     * @param listener the listener to register.
     */
    @Override
    public void addListener (InvalidationListener listener) {
        if (listener != null) stateInvalidationListeners.add (listener);
    }

    /**
     * Removes a single registered instance of an {@link InvalidationListener}
     * from this {@link HorizontalMappedPane}.
     * <p>
     * If {@code listener == null || notInList(listener)}, this method does
     * nothing.
     *
     * @param listener the listener whose instance to remove once.
     */
    @Override
    public void removeListener (InvalidationListener listener) {
        stateInvalidationListeners.remove (listener);
    }

    /* the collection of state change listeners */
    private final List<ChangeListener<? super ObservableState>> stateChangeListeners = new ArrayList<> ();

    /**
     * Adds a {@link ChangeListener} that will be notified each time the
     * state of this {@link HorizontalMappedPane} changes and will receive
     * the state transformation.
     * <p>
     * If {@code listener == null}, this method does nothing.
     *
     * @param listener the listener to register.
     */
    @Override
    public void addListener (ChangeListener<? super ObservableState> listener) {
        if (listener != null) stateChangeListeners.add (listener);
    }

    /**
     * Removes a single instance of a {@link ChangeListener} from this
     * {@link HorizontalMappedPane}.
     * <p>
     * If {@code listener == null || notInList(listener)}, this method does
     * nothing.
     *
     * @param listener the listener whose instance to remove once.
     */
    @Override
    public void removeListener (ChangeListener<? super ObservableState> listener) {
        stateChangeListeners.remove (listener);
    }

    /* the states used to emit change events using ObservableValue */
    private final ObservableState
        oldState = new ObservableState (this),
        curState = new ObservableState (this);

    /**
     * Returns the current state of this {@link HorizontalMappedPane}.
     *
     * @return the current state of this {@link HorizontalMappedPane}
     */
    @Override
    public ObservableState getValue () {
        valid = true;
        return curState;
    }

    /**
     * A more client-friendly alias for the ambiguous {@link #getValue()}.
     *
     * @return the current state of this {@link HorizontalMappedPane}
     */
    public ObservableState getCurrentState () {
        return getValue ();
    }

    /* the interval in arbitrary space which is mapped to component space */
    private final DoubleProperty
        width = new SimpleDoubleProperty (), /* we will use this for binding purposes */
        left  = new SimpleDoubleProperty (),
        right = new SimpleDoubleProperty ();

    /* maintains the binding point of the width property that is used in the
     * onSystemChanged callback below. We need to store the binding point to
     * ensure we do not accidentally modify the width again in the callback
     * if the observable that launched the event was actually the widthProperty()
     * itself. Doing so would most likely cause an infinite recursive call if
     * the JavaFX bean implementation doesn't implement cycle detection.
     */
    private ReadOnlyDoubleProperty widthBindingPoint = widthProperty ();

    /* the linear transform that maps the arbitrary space to component space
     * and is in the form of f(x) = Gx + K
     */
    private double G, inverseG, K;

    /* whenever width, left, or right properties change, this callback will
     * be invoked to update the transform equation and invoke listeners.
     */
    private final ChangeListener<Number> onSystemChanged = (obs, old, now) -> {
        /* ensure that if this pane is bound elsewhere, we update the width
         * accordingly:
         */
        if (obs == width && widthBindingPoint != widthProperty ())
            setWidth (now.doubleValue ());

        /* push the current state into old state */
        oldState.copy (curState);
        curState.width = width.get ();
        curState.left  = left.get ();
        curState.right = right.get ();

        /* compute the linear transform; recall that we use the points
         * (curState.left, 0) and (curState.right, curState.width) which
         * maps from arbitrary space to component space.
         */
        G        = curState.width / (curState.right - curState.left);
        K        = -G * curState.left;
        inverseG = 1d / G; /* inverseG is used to compute the inverse of the map(double) method */

        if (valid) {
            stateInvalidationListeners.forEach (i -> i.invalidated (this));
            valid = false;
        }
        stateChangeListeners.forEach (i -> i.changed (this, oldState, curState));
    };

    /**
     * Creates a new {@link HorizontalMappedPane} whose arbitrary space
     * interval is {@code [pLeft, pRight]}.
     * <p>
     *
     * @see HorizontalMappedPane for more information about the terms
     * <i>arbitrary space</i> and <i>component space</i> as those are
     * important.
     *
     * @param pLeft the left-most value of the arbitrary interval. This
     *              is not bound.
     * @param pRight the right-most value of the arbitrary interval. This
     *               is not bound.
     */
    public HorizontalMappedPane (double pLeft, double pRight) {
        /* we bind() and set() first to avoid unnecessary calls to
         * the onSystemChanged callback.
         * NOTE: bind() will automatically trigger set()
         */
        width.bind (widthBindingPoint = widthProperty ());
        left .set  (pLeft);
        right.set  (pRight);

        /* now we add the listeners and invoke it with mock
         * parameters.
         */
        width.addListener (onSystemChanged);
        left .addListener (onSystemChanged);
        right.addListener (onSystemChanged);
        onSystemChanged.changed (null, null, null);
    }

    /**
     * Creates a new {@link HorizontalMappedPane} whose arbitrary
     * space interval is {@code [-1, 1]}.
     *
     * @see #HorizontalMappedPane(double, double) for more details
     */
    public HorizontalMappedPane () {
        this (-1d, 1d);
    }

    /**
     * Maps an index {@code p} from <i>arbitrary space</i> to
     * <i>component space</i> on the horizontal axis. This is
     * the inverse of {@link #unmap(double)}.
     *
     * @param p the index to map.
     * @return the result of mapping this index.
     */
    public double map (double p) {
        return G * p + K;
    }

    /**
     * Maps an index {@code q} from <i>component space</i> to
     * <i>arbitrary space</i> on the horizontal axis. This is
     * the inverse of {@link #map(double)}.
     *
     * @param q the index to un-map.
     * @return the result of un-mapping this index.
     */
    public double unmap (double q) {
        return inverseG * (q - K);
    }
}
