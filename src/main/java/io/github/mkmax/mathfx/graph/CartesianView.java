package io.github.mkmax.mathfx.graph;

import io.github.mkmax.mathfx.Disposable;
import javafx.beans.NamedArg;

/**
 * A complex UI component that permits the rendering of mathematical
 * functions in {@code R^2}, i.e., {@code F: R -> R} or of similar
 * fashion.
 * <p>
 * The sub- and extension components (collectively called layers)
 * are managed explicitly by a {@code CartesianView} instance. The
 * view will attempt to sync the position and dimensions of all
 * layers with itself, meaning that all layers will be positioned
 * at {@code (0, 0)} and all layers will try to have the same
 * width and height as the view. If a layer cannot be resized
 * past a certain point, it will be anchored to the top left
 * corner of the view.
 *
 * @author Maxim Kasyanenko
 */
public class CartesianView extends BiSpacialPane implements Disposable {

    /**
     * An extension to the {@link BiSpacialPane} that provides additional
     * proprietary features that are required for a {@link CartesianView}
     * to properly manage its components.
     * <p>
     * This class is intended to provide an abstracted link to the
     * {@link CartesianView} component to relieve the components from
     * implementing common and tedious event listeners such as
     * mouse or keyboard listeners.
     *
     * @author Maxim Kasyanenko
     */
    public static class Component extends BiSpacialPane {

    }

    private final CartesianGrid grid = new CartesianGrid ();

    {
        getChildren ().addAll (
            grid);
        grid.bind (this);
    }

    /**
     * Creates a new {@link CartesianView} with the given graph window.
     *
     * @see BiSpacialPane for more information.
     *
     * @param pLeft the left-most bound of the graph window.
     * @param pRight the right-most bound of the graph window.
     * @param pBottom the bottom-most bound of the graph window.
     * @param pTop the top-most bound of the graph window.
     */
    public CartesianView (
        @NamedArg ("left")   double pLeft,
        @NamedArg ("right")  double pRight,
        @NamedArg ("bottom") double pBottom,
        @NamedArg ("top")    double pTop)
    {
        super (pLeft, pRight, pBottom, pTop);
    }

    /**
     * Creates a new {@link CartesianView} with a uniform graph window
     * centered about the origin.
     *
     * @see BiSpacialPane for more information.
     *
     * @param pUniform the uniform size of the graph window.
     */
    public CartesianView (@NamedArg ("uniform") double pUniform) {
        super (pUniform);
    }

    /**
     * Creates a new {@link CartesianView} with the default graph window
     * bounds.
     *
     * @see BiSpacialPane for more information.
     */
    public CartesianView () {
        super ();
    }

    /**
     * Releases any event listeners and other acquired resources to prevent
     * memory leaks. A call to this method invalidates this {@link CartesianView}
     * and from here on out, any interaction with this object is undefined.
     */
    @Override
    public void dispose () {
        grid.dispose ();
    }
}