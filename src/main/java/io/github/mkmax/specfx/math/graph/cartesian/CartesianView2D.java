package io.github.mkmax.specfx.math.graph.cartesian;

import io.github.mkmax.jim.Disposable;

/**
 * A complex component that provides a rich set of utilities to easily
 * render 2D functions in Cartesian Space while also providing a powerful
 * interface to the user with which they can interact with the plotted
 * graphs.
 *
 * @author Maxim Kasyanenko
 */
public class CartesianView2D extends MappedPane2D implements Disposable {

    /* The grid manages all of the guides and labels that the user sees. */
    private final CartesianGrid2D  grid  = new CartesianGrid2D ();

    /* The following block initializes all of the components of the view.
     * It should be noted that this block runs before any constructor.
     */
    {
        getChildren ().addAll (grid);
        grid.bind (this);
    }

    /**
     * Creates a new {@link CartesianView2D} given the boundaries
     * of the graph window.
     * 
     * @see MappedPane2D#MappedPane2D(double, double, double, double) for
     * more information.
     * 
     * @param pLeft the left-bounding value of the graph window.
     * @param pRight the right-bounding value of the graph window.
     * @param pBottom the bottom-bounding value of the graph window.
     * @param pTop the top-bounding value of the graph window.
     */
    public CartesianView2D (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        super (pLeft, pRight, pBottom, pTop);
    }

    /**
     * Creates a new {@link CartesianView2D} given the uniform
     * boundary of the graph window.
     *
     * @see MappedPane2D#MappedPane2D(double) for more information.
     *
     * @param pUniform the uniform bound of the graph window.
     */
    public CartesianView2D (double pUniform) {
        super (pUniform);
    }

    /**
     * Creates a new {@link CartesianView2D} with a norma uniform
     * bound graph window.
     *
     * @see MappedPane2D#MappedPane2D() for more information.
     */
    public CartesianView2D () {
        super ();
    }

    /**
     * Disposes this view and its components.
     */
    @Override
    public void dispose () {
        /* ensure that we release any listeners from the MappedPane2D */
        super.dispose ();

        /* now we can dispose things manually */
        grid.dispose ();
    }
}