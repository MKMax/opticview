package io.github.mkmax.opticview.scene.graph;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class GraphData {

    /* +----------+ */
    /* | FUNCTION | */
    /* +----------+ */
    @FunctionalInterface
    public interface IFunction {
        double apply (double x);
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */
    @FunctionalInterface
    public interface IEntryAddedListener {
        void onEntryAdded (GraphData gd, Entry added);
    }

    @FunctionalInterface
    public interface IEntryRemovedListener {
        void onEntryRemoved (GraphData gd, Entry removed);
    }

    @FunctionalInterface
    public interface IAnyEntryFunctionChangedListener {
        void onEntryFunctionChanged (GraphData gd, Entry source, IFunction old, IFunction now);
    }

    @FunctionalInterface
    public interface IAnyEntryNameChangedListener {
        void onEntryNameChanged (GraphData gd, Entry source, String old, String now);
    }

    /* +-------+ */
    /* | ENTRY | */
    /* +-------+ */
    public static final class Entry {

        /* +--- FUNCTION ---+ */
        private final SimpleObjectProperty<IFunction> function = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<IFunction> functionProperty ()
            { return function; };
        public IFunction getFunction ()
            { return function.get (); }
        public void setFunction (IFunction nFunc)
            { function.set (Objects.requireNonNull (nFunc, "A non-null function must be specified ")); }

        /* +--- NAME ---+ */
        private final SimpleStringProperty name = new SimpleStringProperty ();
        public StringProperty nameProperty ()
            { return name; }
        public String getName ()
            { return name.get (); }
        public void setName (String nName)
            { name.set (nName); }

        /* +--- INITIALIZATION  ---+ */
        Entry (IFunction func, String name) {
            setFunction (func);
            setName (name);
        }
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */

    /* +--- ENTRY ADDED LISTENERS ---+ */
    private final List<IEntryAddedListener> entryAddedListeners = new ArrayList<> ();

    public void addEntryAddedListener (IEntryAddedListener lis)
        { if (lis != null) entryAddedListeners.add (lis); }
    public void removeEntryAddedListener (IEntryAddedListener lis)
        { entryAddedListeners.remove (lis); }

    /* +--- ENTRY REMOVED LISTENERS ---+  */
    private final List<IEntryRemovedListener> entryRemovedListeners = new ArrayList<> ();

    public void addEntryRemovedListener (IEntryRemovedListener lis)
        { if (lis != null) entryRemovedListeners.add (lis); }
    public void removeEntryRemovedListener (IEntryRemovedListener lis)
        { entryRemovedListeners.remove (lis); }

    /* +--- ENTRY FUNCTION CHANGED LISTENERS ---+ */
    private final List<IAnyEntryFunctionChangedListener> entryFunctionChangedListeners = new ArrayList<> ();

    public void addAnyEntryFunctionChangedListener (IAnyEntryFunctionChangedListener lis)
        { if (lis != null) entryFunctionChangedListeners.add (lis); }
    public void removeAnyEntryFunctionChangedListener (IAnyEntryFunctionChangedListener lis)
        { entryFunctionChangedListeners.remove (lis); }

    /* +--- ENTRY NAME CHANGED LISTENERS ---+ */
    private final List<IAnyEntryNameChangedListener> entryNameChangedListeners = new ArrayList<> ();

    public void addAnyEntryNameChangedListener (IAnyEntryNameChangedListener lis)
        { if (lis != null) entryNameChangedListeners.add (lis); }
    public void removeAnyEntryNameChangedListener (IAnyEntryNameChangedListener lis)
        { entryNameChangedListeners.remove (lis); }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    private final ObservableList<Entry> entries = FXCollections.observableArrayList ();

    /* @TODO(max): use a class to generalize event dispatching */
    private final ChangeListener<IFunction> anyEntryFunctionChangedListener = (obs, old, now) -> {
        final Entry entry = findEntry (e -> e.function == obs);
        Objects.requireNonNull (entry, "Failed to find graph data entry that emitted function change event");
        entryFunctionChangedListeners.forEach (i ->
            i.onEntryFunctionChanged (this, entry, old, now));
    };

    private final ChangeListener<String> anyEntryNameChangedListener = (obs, old, now) -> {
        final Entry entry = findEntry (e -> e.name == obs);
        Objects.requireNonNull (entry, "Failed to find graph data entry that emitted name change event");
        entryNameChangedListeners.forEach (i ->
            i.onEntryNameChanged (this, entry, old, now));
    };

    private final ListChangeListener<Entry> onEntryListUpdated = (change) -> {
        while (change.next ()) {
            if (change.wasAdded ())
                change.getAddedSubList ().forEach (i -> {
                    i.functionProperty ().addListener (anyEntryFunctionChangedListener);
                    i.nameProperty ().addListener (anyEntryNameChangedListener);
                    entryAddedListeners.forEach (j -> j.onEntryAdded (this, i));
                });
            else if (change.wasRemoved ())
                change.getRemoved ().forEach (i -> {
                    i.functionProperty ().removeListener (anyEntryFunctionChangedListener);
                    i.nameProperty ().removeListener (anyEntryNameChangedListener);
                    entryRemovedListeners.forEach (j -> j.onEntryRemoved (this, i));
                });
        }
    };

    { entries.addListener (onEntryListUpdated); }

    /* +-------------------+ */
    /* | QUERIES & UPDATES | */
    /* +-------------------+ */
    public Entry findEntry (Predicate<? super Entry> pred) {
        Objects.requireNonNull (pred, "predicate");
        for (Entry e : entries)
            if (pred.test (e))
                return e;
        return null;
    }

    public Collection<Entry> findEntries (Predicate<? super Entry> pred) {
        Objects.requireNonNull (pred, "predicate");
        final Collection<Entry> out = new ArrayList<> ();
        for (Entry e : entries)
            if (pred.test (e)) out.add (e);
        return out;
    }
}
