package io.github.mkmax.opticview.ui.graph;

import io.github.mkmax.opticview.ui.graph.GraphData.*;
import io.github.mkmax.opticview.util.IDisposable;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class GraphLegend extends GraphStack.Device {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                        LISTING                                            //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static final class Listing extends HBox implements IDisposable {
        private static final double SPACING = 5d;
        private static final Insets PADDING = new Insets (5d);
        private static final Pos ALIGNMENT = Pos.CENTER_LEFT;

        /* +------------+ */
        /* | PROPERTIES | */
        /* +------------+ */
        private Entry entry;

        public Entry getEntry () {
            return entry;
        }

        public void setEntry (Entry nentry) {
            Objects.requireNonNull (nentry, "A non-null entry object must be specified");
            if (entry != null) {
                entry.prefColorProperty ().removeListener (onColorChanged);
                entry.prefNameProperty ().removeListener (onNameChanged);
            }
            entry = nentry;
            entry.prefColorProperty ().addListener (onColorChanged);
            entry.prefNameProperty ().addListener (onNameChanged);
            /* update the display */
            colorDisp.setFill (entry.getPrefColor ());
            nameDisp.setText (entry.getPrefName ());
        }

        /* +--------------------------+ */
        /* | INITIALIZATION & MEMBERS | */
        /* +--------------------------+ */
        private final Circle colorDisp = new Circle (4d);
        private final Text nameDisp = new Text ();

        public Listing (Entry pentry) {
            setEntry (pentry);
            /* setup and add components to box */
            setPadding (PADDING);
            setAlignment (ALIGNMENT);
            setSpacing (SPACING);
            getChildren ().addAll (colorDisp, nameDisp);
        }

        /* +----------------------+ */
        /* | LISTENERS & HANDLERS | */
        /* +----------------------+ */
        private final ChangeListener<Color> onColorChanged = (__obs, __old, now) ->
            colorDisp.setFill (now);
        private final ChangeListener<String> onNameChanged = (__obs, __old, now) ->
            nameDisp.setText (now);

        @Override
        public void dispose () {
            entry.prefColorProperty ().removeListener (onColorChanged);
            entry.prefNameProperty ().removeListener (onNameChanged);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                     GRAPH LEGEND                                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static final double
        INIT_LISTING_BOX_X_OFFSET = 15d,
        INIT_LISTING_BOX_Y_OFFSET = 15d;
    private static final Background LISTING_BOX_BACKGROUND = new Background (
        new BackgroundFill (Color.WHITE, null, null));
    private static final Border LISTING_BOX_BORDER = new Border (
        new BorderStroke (Color.BLACK, BorderStrokeStyle.SOLID, null, null));

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */
    private final VBox listingbox = new VBox ();
    private final Map<Entry, Listing> entrymap = new HashMap<> ();

    public GraphLegend (GraphData ref) {
        setGraphData (ref);
        /* setup the legend stack */
        listingbox.setLayoutX (INIT_LISTING_BOX_X_OFFSET);
        listingbox.setLayoutY (INIT_LISTING_BOX_Y_OFFSET);
        listingbox.setBackground (LISTING_BOX_BACKGROUND);
        listingbox.setBorder (LISTING_BOX_BORDER);
        getChildren ().add (listingbox);
    }

    /* +----------------------+ */
    /* | OVERRIDES & INTERNAL | */
    /* +----------------------+ */

    /* ENTRY TRANSACTION IMPLEMENTATION */
    private final Consumer<Entry> entryAdditionDelegate = this::recognizeEntry;
    private final EntryAdditionListener entryAdditionListener = (__gd, entry) -> recognizeEntry (entry);
    private final EntryRemovalListener entryRemovalListener = (__gd, entry) -> dismissEntry (entry);

    private void recognizeEntry (Entry entry) {
        final Listing listing = new Listing (entry);
        entrymap.put (entry, listing);
        listingbox.getChildren ().add (listing);
    }

    private void dismissEntry (Entry entry) {
        listingbox.getChildren ().remove (entrymap.remove (entry));
    }

    public void purgeEntries () {
        listingbox.getChildren ().clear ();
        entrymap.clear ();
    }

    /* OVERRIDES */
    @Override
    protected void onGraphDataChanged (GraphData old, GraphData now) {
        if (old != null) {
            old.removeEntryAdditionListener (entryAdditionListener);
            old.removeEntryRemovalListener (entryRemovalListener);
            purgeEntries ();
        }
        now.registerEntryAdditionListener (entryAdditionListener);
        now.registerEntryRemovalListener (entryRemovalListener);
        now.forEachEntry (entryAdditionDelegate);
    }

    @Override
    public void dispose () {
        super.dispose ();
        final GraphData gd = getGraphData ();
        gd.removeEntryAdditionListener (entryAdditionListener);
        gd.removeEntryRemovalListener (entryRemovalListener);
        purgeEntries ();
        purgeEntries ();
    }
}
