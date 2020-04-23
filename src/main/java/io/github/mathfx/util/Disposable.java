package io.github.mathfx.util;

/**
 * Implemented by graphical components which may be created and
 * destroyed during runtime.
 * <p>
 * This interface exists to allow components to detach any listeners
 * that they have bound to prevent possible memory leaks. For example,
 * if a component is listening to the width and height properties of
 * the parent, once the component is not needed, a call to <code>dispose()</code>
 * will ensure that those listeners are removed from the parent's width
 * and height properties.
 *
 * @author Maxim Kasyanenko
 */
public interface Disposable {

    /**
     * Disposes this component.
     * <p>
     * Any attempts at modification or usage of this component after this
     * function is called is undefined and may result in unexpected exceptions.
     * It is advised that any reference to this component be removed as soon
     * as this function has finished executing.
     */
    void dispose ();

}
