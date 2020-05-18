package io.github.mkmax.fx;

import io.github.mkmax.jim.Disposable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of {@link javafx.beans.value.ObservableValue} objects
 * and merges any change events emitted by said objects into a single chain
 * of event handlers.
 * <p>
 * When designing complex components, it is often required to listen to many
 * events and execute some method when some related values are changed. For example,
 * when a component requires an update each time it is resized, it would be
 * beneficial to group the {@code widthProperty()} and {@code heightProperty()}
 * so that only a single method is executed intercepting the change event.
 * <br><br>
 * Furthermore, grouping such properties and the respective event handlers reduces
 * verbosity of the client code, which is ultimately the aim of this class. Sure,
 * you can achieve the same functionality mentioned previously using multiple
 * {@link javafx.beans.value.ChangeListener} objects, but maintaining that becomes
 * a hassle rather quickly.
 *
 * @author Maxim Kasyanenko
 */
public final class ObservableGroup<T> implements Disposable {

    /**
     * A callback to simply be notified when a change event is fired from
     * any property registered in a particular group.
     * <p>
     * The difference between {@code SimpleListener} and {@link ChangeListener}
     * is that the simple listener does not care for the observable that
     * emitted the event nor for the old/new values.
     *
     * @author Maxim Kasyanenko
     */
    public interface SimpleListener {
        /**
         * Invoked when any registered observable fires a change event.
         */
        void changed ();
    }

    private final List<ObservableValue<? extends T>> properties = new ArrayList<> ();
    private final List<ChangeListener<? super T>> fullListeners = new ArrayList<> ();
    private final List<SimpleListener> simpleListeners = new ArrayList<> ();
    private final ChangeListener<T> master = (obs, old, now) -> {
        fullListeners.forEach (i -> i.changed (obs, old, now));
        simpleListeners.forEach (SimpleListener::changed);
    };

    /**
     * Creates a new {@code ObservableGroup} without any initial
     * properties or change listeners.
     */
    public ObservableGroup () {
    }

    /**
     * Creates a new {@code ObservableGroup} with some initial properties
     * but no change listeners.
     *
     * @param obs An initial handful of properties to be grouped.
     */
    @SafeVarargs
    public ObservableGroup (ObservableValue<? extends T>... obs) {
        addAll (obs);
    }

    /**
     * Adds the specified {@link ObservableValue} to this group.
     * <p>
     * If {@code obs == null} or is already present in this group, it
     * is discarded and this method does nothing.
     *
     * @param obs The observable to add to this group.
     */
    public final void add (ObservableValue<? extends T> obs) {
        if (obs != null && !properties.contains (obs)) {
            obs.addListener (master);
            properties.add (obs);
        }
    }

    /**
     * Adds all of the specified {@link ObservableValue} using
     * {@link #add(ObservableValue)}.
     * <p>
     * If {@code obs == null} or it does not contain any elements, it
     * is discarded and this method does nothing.
     *
     * @see #add(ObservableValue) for more details on registering properties.
     * @param obs A batch of properties to be added to this group.
     */
    @SafeVarargs
    public final void addAll (ObservableValue<? extends T>... obs) {
        if (obs != null) for (var i : obs) add (i);
    }

    /**
     * Removes the specified observable from this group.
     * <p>
     * If {@code obs == null}, it is discarded and this method
     * does nothing. If {@code obs} was not registered in the first
     * place, this method also does nothing.
     *
     * @param obs The observable to remove from this group.
     */
    public final void remove (ObservableValue<? extends T> obs) {
        if (obs != null) {
            obs.removeListener (master);
            properties.remove (obs);
        }
    }

    /**
     * Appends the specified listener to the event handler chain.
     * <p>
     * If {@code lis == null}, it will be discarded and this function
     * does nothing. However, conforming to the original specification
     * for {@link ObservableValue#addListener(ChangeListener)}, this
     * method will permit duplicate listeners to be registered and for
     * any event it will be fired as many times as it was added.
     *
     * @param lis A full change listener to append to the event handler
     *            chain.
     */
    public final void add (ChangeListener<? super T> lis) {
        if (lis != null) fullListeners.add (lis);
    }

    /**
     * Appends all of the specified listeners to the event handler chain
     * using {@link #add(ChangeListener)}.
     * <p>
     * If {@code lis == null} or it does not contain eny elements, it
     * is discarded and this method does nothing. If any change listener
     * specified in the batch is already registered, adhering to the
     * original specification of {@link ObservableValue#addListener(ChangeListener)},
     * it will be permitted for registration and for any change event
     * fired it will receive as many invocations as it had been registered
     * for.
     *
     * @see #add(ChangeListener) for moredetails on registering listeners.
     * @param lis A batch of listeners to be added to this group.
     */
    @SafeVarargs
    public final void addAll (ChangeListener<? super T>... lis) {
        if (lis != null) for (var i : lis) add (i);
    }

    /**
     * Removes a single reference of the specified listener from this group.
     * <p>
     * If {@code lis == null} or it was never registered, it is discarded
     * and this method does nothing. If this listener was registered multiple
     * times, only a single reference is removed while the others remain put.
     *
     * @param lis The listener to remove from this group.
     */
    public final void remove (ChangeListener<? super T> lis) {
        if (lis != null) fullListeners.remove (lis);
    }

    /**
     * Appends a simplified listener to the back of the event
     * handler chain.
     * <p>
     * If {@code lis == null}, it is discarded and this method
     * does nothing. Similarly to {@link #add(ChangeListener)},
     * multiple registrations of the same listener will enable
     * the listener to be invoked multiple times for each
     * event fired by any one property.
     * <br><br>
     * It may also be useful to know that simple listeners are
     * always invoked <i>after</i> the full change listeners.
     *
     * @see #add(ChangeListener) for more similar details.
     * @param lis A simple change listener to append to the event handler
     *            chain.
     */
    public final void add (SimpleListener lis) {
        if (lis != null) simpleListeners.add (lis);
    }

    /**
     * Appends all of the specified listeners to the event handler
     * chain using {@link #add(SimpleListener)}.
     * <p>
     * If {@code lis == null} or it does not contain any elements,
     * it is discarded and this method does nothing. If any change
     * listener in the batch has already been specified, then
     * adhering to the specification of {@link ObservableValue#addListener(ChangeListener)},
     * it will be registered and invoked multiple times per event
     * fired by any one property.
     * <br><br>
     * It may be useful to know that simple listeners are always
     * invoked <i>after</i> the full change listeners.
     *
     * @see #add(SimpleListener) for more details.
     * @param lis A batch of listeners to be added to this group.
     */
    public final void addAll (SimpleListener... lis) {
        if (lis != null) for (var i : lis) add (i);
    }

    /**
     * Removes a single reference of the specified listener from this group.
     * <p>
     * If {@code lis == null} or it is not registered in this group, it
     * is discarded and this method does nothing. If {@code lis} has been
     * registered multiple times, only a single reference is removed
     * while the others stay put.
     *
     * @param lis The listener to remove from this group.
     */
    public final void remove (SimpleListener lis) {
        if (lis != null) simpleListeners.remove (lis);
    }

    /**
     * Removes any listeners bounds to this group and clears the collection
     * of properties registered in this group, effectively purging this
     * group of any properties or listeners.
     */
    @Override
    public void dispose () {
        properties.forEach (i -> i.removeListener (master));
        properties.clear ();
        fullListeners.clear ();
        simpleListeners.clear ();
    }
}
