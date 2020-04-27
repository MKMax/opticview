package io.github.mathfx.util;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * Creates a dynamically changing {@link ObservableGroup} whose properties
     * change whenever the parent node of the {@code child} parameter changes.
     * <p>
     * The resulting {@link ObservableGroup} will always receive change events
     * of the parent component's dimension, even if the parent component changes.
     *
     * @param child The child component whose parent to monitor (must not be null).
     * @param widthOut An optional property to write new parent widths to.
     * @param heightOut An optional property to write new parent heights to.
     * @return An {@link ObservableGroup} that monitors the child's parent node for
     *                                    dimension changes.
     */
    public static ObservableGroup<?> observeParentSize (
        Node                     child,
        Property<? super Double> widthOut,
        Property<? super Double> heightOut)
    {
        Objects.requireNonNull (child, "child component must not be null");

        final ObservableGroup<Object> result = new ObservableGroup<> (child.parentProperty ());
        if (child.getParent () instanceof Region) {
            final Region region = (Region) child.getParent ();
            result.add (region.widthProperty ());
            result.add (region.heightProperty ());
        }

        final ChangeListener<Object> listener = (obs, old, now) -> {
            if (obs == child.parentProperty ()) {
                if (old instanceof Region) {
                    final Region region = (Region) old;
                    result.remove (region.widthProperty ());
                    result.remove (region.heightProperty ());
                }
                if (now instanceof Region) {
                    final Region region = (Region) now;
                    result.add (region.widthProperty ());
                    result.add (region.heightProperty ());
                    if (widthOut != null) widthOut.setValue (region.getWidth ());
                    if (heightOut != null) heightOut.setValue (region.getHeight ());
                }
            }
            else if (child.getParent () instanceof Region && now instanceof Number) {
                final Region region = (Region) child.getParent ();
                final Number change = (Number) now;
                if (obs == region.widthProperty () && widthOut != null)
                    widthOut.setValue (change.doubleValue ());
                else if (obs == region.heightProperty () && heightOut != null)
                    heightOut.setValue (change.doubleValue ());
            }
        };

        result.add (listener);

        return result;
    }

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
    private final List<ObservableGroup<? extends T>> groups = new ArrayList<> ();
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
     * Creates a new {@code ObservableGroup} with some initial subgroups
     * but no change listeners.
     *
     * @param sub An initial batch of subgroups.
     */
    @SafeVarargs
    public ObservableGroup (ObservableGroup<? extends T>... sub) {
        addAll (sub);
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
     * Adds the specified {@link ObservableGroup} to this group.
     * <p>
     * If {@code sub == null} or is already present in this group,
     * it is discarded and this method does nothing. Adding subgroups
     * to groups allows component designers to provide an easy
     * event interface for multiple events that span across multiple
     * groups.
     *
     * @param sub The sub group to add
     */
    public final void add (ObservableGroup<? extends T> sub) {
        if (sub != null && !groups.contains (sub)) {
            sub.add (master);
            groups.add (sub);
        }
    }

    /**
     * Adds all of the specified {@link ObservableGroup} objects using
     * {@link #add(ObservableGroup)}.
     * <p>
     * If {@code sub == null} or it does not contain any elements, it
     * is discarded and this method does nothing.
     *
     * @see #add(ObservableGroup) for more details on registering groups.
     * @param sub A batch of subgroups to be added to this group.
     */
    @SafeVarargs
    public final void addAll (ObservableGroup<? extends T>... sub) {
        if (sub != null) for (var i : sub) add (i);
    }

    /**
     * Removes the specified subgroup from this group.
     * <p>
     * If {@code sub == null} or was never registered, this method
     * does nothing.
     *
     * @param sub The subgroup to remove from this group.
     */
    public final void remove (ObservableGroup<? extends T> sub) {
        if (sub != null) {
            sub.remove (master);
            groups.remove (sub);
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
     * Disposes of any listeners and properties in this group. However,
     * all registered subgroups are themselves <strong>not</strong>
     * disposed. To dispose subgroups also, use {@link #dispose(boolean)}.
     */
    @Override
    public void dispose () {
        dispose (false);
    }

    /**
     * Disposes of any listeners and properties in this group. If
     * {@code disposeSubgroups == true}, this method invocation
     * will also call {@code dispose(true)} on all subgroups that
     * are still registered.
     *
     * @param disposeSubgroups If true, this method will also invoke
     *                         {@code dispose(true)} on any remaining
     *                         subgroup. If false, all subgroups are
     *                         simply removed from the groups container.
     */
    public void dispose (boolean disposeSubgroups) {
        if (disposeSubgroups)
            groups.forEach (i -> i.dispose (true));
        groups.forEach (i -> i.remove (master));
        groups.clear ();

        properties.forEach (i -> i.removeListener (master));
        properties.clear ();

        fullListeners.clear ();
        simpleListeners.clear ();
    }
}
