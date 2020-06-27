package io.github.mkmax.opticview.ui.sci;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class GraphData {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                       FUNCTION                                            //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @FunctionalInterface
    public interface Function {
        double map (double x);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                         ENTRY                                             //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final class Entry {

        /* +----------+ */
        /* | FUNCTION | */
        /* +----------+ */
        private final SimpleObjectProperty<Function> function = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<Function> functionProperty ()
            { return function; }
        public Function getFunction ()
            { return function.get (); }
        public void setFunction (Function nfunc)
            { function.set (Objects.requireNonNull (nfunc, "Function object must be specified")); }

        /* +-------+ */
        /* | COLOR | */
        /* +-------+ */
        private final SimpleObjectProperty<Color> prefColor = new SimpleObjectProperty<> ();
        public ObjectProperty<Color> prefColorProperty ()
            { return prefColor; }
        public Color getPrefColor ()
            { return prefColor.get (); }
        public Color getPrefColorElse (Color def)
            { return prefColor.get () == null ? def : prefColor.get (); }
        public void setPrefColor (Color ncolor)
            { prefColor.set (ncolor); }

        /* +------+ */
        /* | NAME | */
        /* +------+ */
        private final SimpleStringProperty prefName = new SimpleStringProperty ();
        public StringProperty prefNameProperty ()
            { return prefName; }
        public String getPrefName ()
            { return prefName.get (); }
        public String getPrefNameElse (String def)
            { return prefName.get () == null ? def : prefName.get (); }
        public void setPrefName (String nname)
            { prefName.set (nname); }

        /* +-------+ */
        /* | ENTRY | */
        /* +-------+ */
        Entry (
            Function reqfunc,
            Color    prefcolor,
            String   prefname)
        {
            setFunction (reqfunc);
            setPrefColor (prefcolor);
            setPrefName (prefname);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                   CUSTOM LISTENERS                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @FunctionalInterface
    public interface EntryAdditionListener {
        void onAdd (GraphData gd, Entry added);
    }

    @FunctionalInterface
    public interface EntryRemovalListener {
        void onRemove (GraphData gd, Entry removed);
    }

    @FunctionalInterface
    public interface AnyEntryPropChangeListener {
        void onChange (GraphData gd, Entry src);
    }

    @FunctionalInterface
    public interface SpecificEntryPropChangeListener<T> {
        void onChange (GraphData gd, Entry src, T old, T now);
    }

    private static final class BroadcastEntryPropChange<T> implements ChangeListener<T> {
        private final GraphData gd;
        private final List<AnyEntryPropChangeListener> any;
        private final List<SpecificEntryPropChangeListener<T>> specific;

        BroadcastEntryPropChange (
            GraphData pgd,
            List<AnyEntryPropChangeListener> pany,
            List<SpecificEntryPropChangeListener<T>> pspecific)
        {
            gd = Objects.requireNonNull (pgd);
            any = Objects.requireNonNull (pany);
            specific = Objects.requireNonNull (pspecific);
        }

        @Override
        public void changed (ObservableValue<? extends T> obs, T old, T now) {
            final Entry entrybyobs = gd.findEntry (e -> e.function == obs);
            if (entrybyobs == null)
                throw new RuntimeException ("Failed to find function entry by event source observable");
            specific.forEach (lis -> lis.onChange (gd, entrybyobs, old, now));
            any.forEach (lis -> lis.onChange (gd, entrybyobs));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      GRAPH DATA                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /* +---------+ */
    /* | ENTRIES | */
    /* +---------+ */
    private final ObservableList<Entry> entries = FXCollections.observableArrayList ();
    private final List<Entry> immuentries = Collections.unmodifiableList (entries);

    public List<Entry> getImmutableEntries () {
        return immuentries;
    }

    public Entry findEntry (Predicate<? super Entry> pred) {
        if (pred != null)
            for (Entry e : entries)
                if (pred.test (e)) return e;
        return null;
    }

    /* +--------------------------+ */
    /* | ENTRY ADDITION LISTENERS | */
    /* +--------------------------+ */
    private final List<EntryAdditionListener> additionlisteners = new ArrayList<> ();
    public void registerEntryAdditionListener (EntryAdditionListener lis)
        { if (lis != null) additionlisteners.add (lis); }
    public void removeEntryAdditionListener (EntryAdditionListener lis)
        { additionlisteners.remove (lis); }

    /* +-------------------------+ */
    /* | ENTRY REMOVAL LISTENERS | */
    /* +-------------------------+ */
    private final List<EntryRemovalListener> removallisteners = new ArrayList<> ();
    public void registerEntryRemovalListener (EntryRemovalListener lis)
        { if (lis != null) removallisteners.add (lis); }
    public void removeEntryRemovalListener (EntryRemovalListener lis)
        { removallisteners.remove (lis); }

    /* +---------------------------------------+ */
    /* | ANY ENTRIES PROPERTY CHANGE LISTENERS | */
    /* +---------------------------------------+ */
    private final List<AnyEntryPropChangeListener> anychangelisteners = new ArrayList<> ();
    public void registerAnyEntryPropertyChangeListener (AnyEntryPropChangeListener lis)
        { if (lis != null) anychangelisteners.add (lis); }
    public void removeAnyEntryPropertyChangeListener (AnyEntryPropChangeListener lis)
        { anychangelisteners.remove (lis); }

    /* +--------------------------------------------+ */
    /* | ENTRIES FUNCTION PROPERTY CHANGE LISTENERS | */
    /* +--------------------------------------------+ */
    private final List<SpecificEntryPropChangeListener<Function>> funcchangelisteners = new ArrayList<> ();
    public void registerEntryFunctionPropertyChangeListener (SpecificEntryPropChangeListener<Function> lis)
        { if (lis != null) funcchangelisteners.add (lis); }
    public void removeEntryFunctionPropertyChangeListener (SpecificEntryPropChangeListener<Function> lis)
        { funcchangelisteners.remove (lis); }

    /* +-----------------------------------------+ */
    /* | ENTRIES COLOR PROPERTY CHANGE LISTENERS | */
    /* +-----------------------------------------+ */
    private final List<SpecificEntryPropChangeListener<Color>> prefcolorchangelisteners = new ArrayList<> ();
    public void registerEntryPrefColorPropertyChangeListener (SpecificEntryPropChangeListener<Color> lis)
        { if (lis != null) prefcolorchangelisteners.add (lis); }
    public void removeEntryPrefColorPropertyChangeListener (SpecificEntryPropChangeListener<Color> lis)
        { prefcolorchangelisteners.remove (lis); }

    /* +------------------------------------------+ */
    /* | ENTRIES STRING PROPERTY CHANGE LISTENERS | */
    /* +------------------------------------------+ */
    private final List<SpecificEntryPropChangeListener<String>> prefnamechangelisteners = new ArrayList<> ();
    public void registerEntryPrefNamePropertyChangeListener (SpecificEntryPropChangeListener<String> lis)
        { if (lis != null) prefnamechangelisteners.add (lis); }
    public void removeEntryPrefNamePropertyChangeListener (SpecificEntryPropChangeListener<String> lis)
        { prefnamechangelisteners.remove (lis); }

    /* +---------------------+ */
    /* | BROADCAST LISTENERS | */
    /* +---------------------+ */
    private final BroadcastEntryPropChange<Function> funcbroadcast
        = new BroadcastEntryPropChange<> (this, anychangelisteners, funcchangelisteners);
    private final BroadcastEntryPropChange<Color> prefcolorbroadcast
        = new BroadcastEntryPropChange<> (this, anychangelisteners, prefcolorchangelisteners);
    private final BroadcastEntryPropChange<String> prefnamebroadcast
        = new BroadcastEntryPropChange<> (this, anychangelisteners, prefnamechangelisteners);

    private final ListChangeListener<Entry> entrieschangelistener = (change) -> {
        while (change.next ()) {
            change.getAddedSubList ().forEach (e -> {
                if (e == null)
                    throw new RuntimeException ("Illegal null function entry registered");
                e.functionProperty ().addListener (funcbroadcast);
                e.prefColorProperty ().addListener (prefcolorbroadcast);
                e.prefNameProperty ().addListener (prefnamebroadcast);
                GraphData.this.additionlisteners.forEach (lis -> lis.onAdd (GraphData.this, e));
            });
            change.getRemoved ().forEach (e -> {
                e.functionProperty ().removeListener (funcbroadcast);
                e.prefColorProperty ().removeListener (prefcolorbroadcast);
                e.prefNameProperty ().removeListener (prefnamebroadcast);
                GraphData.this.removallisteners.forEach (lis -> lis.onRemove (GraphData.this, e));
            });
        }
    };

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */
    {
        entries.addListener (entrieschangelistener);
    }
}