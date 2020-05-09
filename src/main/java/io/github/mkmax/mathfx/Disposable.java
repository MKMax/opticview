package io.github.mkmax.mathfx;

/**
 * Implemented by classes that require explicit notification of
 * it's disposable to release any acquired resources.
 *
 * @author Maxim Kasyanenko
 */
public interface Disposable {

    /**
     * Notifies the object that it should release any acquired
     * resources and be prepared for garbage collection. After
     * calling this method, any behaviour that is standard to
     * said object is now undefined.
     */
    void dispose ();

}
