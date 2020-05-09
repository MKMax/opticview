package io.github.mkmax.mathfx.graph;

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
public class CartesianView extends BiSpacialPane {

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

}