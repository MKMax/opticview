package io.github.mkmax.opticview.scene.graph;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.Predicate;

public class SampledRangeGraph extends Region {

    /* +--------------------------------------------------------------------+ */
    /* |                                DATA                                | */
    /* +--------------------------------------------------------------------+ */
    public static final class Data {

        /* +----------+ */
        /* | FUNCTION | */
        /* +----------+ */
        @FunctionalInterface
        public interface IFunction {
            double compute (double x);
        }

        /* +-----------+ */
        /* | LISTENERS | */
        /* +-----------+ */
        @FunctionalInterface
        public interface IEntryAddedListener {
            void onEntryAdded (Data data, Entry added);
        }

        @FunctionalInterface
        public interface IEntryRemovedListener {
            void onEntryRemoved (Data data, Entry removed);
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

            private final SimpleDoubleProperty
                start = new SimpleDoubleProperty (-1d),
                step = new SimpleDoubleProperty (0.25d),
                end = new SimpleDoubleProperty (1d);

            /* +--- START ---+ */
            public ReadOnlyDoubleProperty startProperty ()
            { return start; }
            public double getStart ()
            { return start.get (); }
            public void setStart (double nStartValue) {
                verifyRange (nStartValue, getStep (), getEnd ());
                start.set (nStartValue);
            }

            /* +--- STEP ---+ */
            public ReadOnlyDoubleProperty stepProperty ()
            { return step; }
            public double getStep ()
            { return step.get (); }
            public void setStep (double nStepValue) {
                verifyRange (getStart (), nStepValue, getEnd ());
                step.set (nStepValue);
            }

            /* +--- END ---+ */
            public ReadOnlyDoubleProperty endProperty ()
            { return end; }
            public double getEnd ()
            { return end.get (); }
            public void setEnd (double nEndValue) {
                verifyRange (getStart (), getStep (), nEndValue);
                end.set (nEndValue);
            }

            /* +--- RANGE ---+ */
            public void setRange (double pStart, double pStep, double pEnd) {
                verifyRange (pStart, pStep, pEnd);
                start.set (pStart);
                step.set (pStep);
                end.set (pEnd);
            }

            /* +--- INTERVAL VERIFICATION ---+ */
            private static final double STEP_EPSILON = 1e-8d;

            private static void verifyRange (double start, double step, double end) {
                if (Math.abs (step) < STEP_EPSILON)
                    throw new RuntimeException ("step cannot be zero");
                if (Math.signum (end - start) != Math.signum (step))
                    throw new RuntimeException ("step direction outbound of interval");
            }

            /* +--- INITIALIZATION  ---+ */
            Entry (IFunction func, String name, double start, double step, double end) {
                setFunction (func);
                setName (name);
                setRange (start, step, end);
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

        /* +----------------+ */
        /* | IMPLEMENTATION | */
        /* +----------------+ */
        private final ObservableList<Entry> entries = FXCollections.observableArrayList ();
        private final List<Entry> unmodifiableEntries = Collections.unmodifiableList (entries);

        public List<Entry> getUnmodifiableEntries () {
            return unmodifiableEntries;
        }

        private final ListChangeListener<Entry> onEntryListUpdated = (change) -> {
            while (change.next ()) {
                if (change.wasAdded ())
                    change.getAddedSubList ().forEach (i ->
                        entryAddedListeners.forEach (j -> j.onEntryAdded (this, i)));
                else if (change.wasRemoved ())
                    change.getRemoved ().forEach (i ->
                        entryRemovedListeners.forEach (j -> j.onEntryRemoved (this, i)));
            }
        };

        { entries.addListener (onEntryListUpdated); }

        /* +----------------+ */
        /* | DELETE ENTRIES | */
        /* +----------------+ */
        public void clearEntries () {
            entries.clear ();
        }

        /* +----------------+ */
        /* | CREATE ENTRIES | */
        /* +----------------+ */
        public Entry createEntry (IFunction func, String name, double start, double step, double end) {
            Objects.requireNonNull (func, "cannot create an entry with a null function");
            final Entry created = new Entry (func, name, start, step, end);
            entries.add (created);
            return created;
        }

        /* +--------------+ */
        /* | FIND ENTRIES | */
        /* +--------------+ */

        /* +--- by observable ---+ */
        public Entry findEntryByObservable (ObservableValue<?> obs) {
            return findEntry (e ->
                e.function == obs ||
                e.name == obs ||
                e.start == obs ||
                e.step == obs ||
                e.end == obs);
        }

        /* +--- general finds ---+ */
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

    /* +--------------------------------------------------------------------+ */
    /* |                           SAMPLE TOOLTIP                           | */
    /* +--------------------------------------------------------------------+ */
    private static final class SampleTooltip extends Tooltip {

        /* +--- NAME ---+ */
        private final StringProperty name = new SimpleStringProperty ();
        public StringProperty nameProperty ()
            { return name; }
        public String getName ()
            { return name.get (); }
        public void setName (String nName)
            { name.set (nName); }

        /* +--- VALUE ---+ */
        private final DoubleProperty value = new SimpleDoubleProperty ();
        public DoubleProperty valueProperty ()
            { return value; }
        public double getValue ()
            { return value.get (); }
        public void setValue (double nValue)
            { value.set (nValue); }

        /* +------------+ */
        /* | COMPONENTS | */
        /* +------------+ */
        private final VBox graphic = new VBox ();
        private final Label
            nameLabel = new Label (),
            valueLabel = new Label ();

        private final ChangeListener<Number> onValueChanged = (__obs, __old, now) ->
            valueLabel.setText (now == null ? "" : Double.toString (now.doubleValue ()));

        {
            /* bind labels to their content */
            nameLabel.textProperty ().bind (name);
            value.addListener (onValueChanged);

            /* add components to graphic and setup the graphic */
            graphic.setAlignment (Pos.TOP_LEFT);
            graphic.getChildren ().addAll (nameLabel, valueLabel);

            /* add graphic to the tooltip */
            setGraphic (graphic);
        }

        /* +----------------+ */
        /* | INITIALIZATION | */
        /* +----------------+ */
        SampleTooltip (String pName, double pValue) {
            setName (pName);
            setValue (pValue);
        }

        void destroy () {
            nameLabel.textProperty ().unbind ();
            value.removeListener (onValueChanged);
        }
    }

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String SAMPLED_RANGE_GRAPH = "sample-range-graph";
    }

    /* +--------------------------+ */
    /* | MEMBERS & INITIALIZATION | */
    /* +--------------------------+ */
    private static final int MAX_SAMPLES = 4096;

    /* Function Data */
    private final Data data = new Data ();
    private final Map<Data.Entry, Series<Number, Number>> entryPlotMap = new HashMap<> ();

    /* +----------+ */
    /* | HANDLERS | */
    /* +----------+ */

    /* +--- plotting/individual entry listeners ---+ */
    private final ChangeListener<Object> onEntryPlotRequired = (obs, __old, __now) ->
        plot (data.findEntryByObservable (obs));

    /* +--- entry listeners ---+ */
    private final Data.IEntryAddedListener onEntryAdded = (__data, entry) -> {
        /* entry should never be null */
        entry.functionProperty ().addListener (onEntryPlotRequired);
        entry.startProperty ().addListener (onEntryPlotRequired);
        entry.stepProperty ().addListener (onEntryPlotRequired);
        entry.endProperty ().addListener (onEntryPlotRequired);
        plot (entry);
    };

    private final Data.IEntryRemovedListener onEntryRemoved = (__data, entry) -> {
        /* entry should never be null */
        entry.functionProperty ().removeListener (onEntryPlotRequired);
        entry.startProperty ().removeListener (onEntryPlotRequired);
        entry.stepProperty ().removeListener (onEntryPlotRequired);
        entry.endProperty ().removeListener (onEntryPlotRequired);
        destroy (entry);
    };

    /* install data handlers */
    {
        data.addEntryAddedListener (onEntryAdded);
        data.addEntryRemovedListener (onEntryRemoved);
    }

    /* Chart Data */
    private final NumberAxis
        x = new NumberAxis (),
        y = new NumberAxis ();
    private final ObservableList<Series<Number, Number>> plots = FXCollections.observableArrayList ();

    /* layout & components */
    private final GridPane container = new GridPane ();
    private final LineChart<Number, Number> chart = new LineChart<> (x, y);
    private final Group
        xLabelContainer = new Group (),
        yLabelContainer = new Group ();
    private final Label
        xLabel = new Label (),
        yLabel = new Label ();

    /* dimension adjustment listeners */
    private final ChangeListener<Number> onRegionWidthChanged = (__obs, __old, now) ->
        container.setPrefWidth (now == null ? Double.NaN : now.doubleValue ());

    private final ChangeListener<Number> onRegionHeightChanged = (__obs, __old, now) ->
        container.setPrefHeight (now == null ? Double.NaN : now.doubleValue ());

    {
        widthProperty ().addListener (onRegionWidthChanged);
        heightProperty ().addListener (onRegionHeightChanged);
    }

    /* configuration & initialization */
    {
        /* style class */
        getStyleClass ().add (ComponentClass.SAMPLED_RANGE_GRAPH);

        /* configure the chart */
        chart.setData (plots);

        /* configure the X/Y labels */
        xLabelContainer.getChildren ().add (xLabel);
        yLabelContainer.getChildren ().add (yLabel);
        yLabel.setRotate (270d);

        /* configure the main container & the components */
        GridPane.setConstraints (xLabelContainer, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (yLabelContainer, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.ALWAYS);
        GridPane.setConstraints (chart, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        container.getChildren ().addAll (xLabelContainer, yLabelContainer, chart);

        /* add relevant components as children to the region */
        getChildren ().add (container);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    public void setXLabel (String text) {
        xLabel.setText (text);
    }

    public void setYLabel (String text) {
        yLabel.setText (text);
    }

    public Data getData () {
        return data;
    }

    /* +----------+ */
    /* | INTERNAL | */
    /* +----------+ */
    private void plot (Data.Entry entry) {
        Objects.requireNonNull (entry, "Cannot plot a null entry");
        Series<Number, Number> series = entryPlotMap.get (entry);
        if (series == null) { /* new plot */
            series = new Series<> ();
            series.nameProperty ().bind (entry.nameProperty ());
            entryPlotMap.put (entry, series);
            plots.add (series);
        }
        final Data.IFunction func = entry.getFunction ();
        final double
            start = entry.getStart (),
            step = entry.getStep (),
            end = entry.getEnd ();
        final double span = end - start;
        final double ratio = Math.abs (span / step);
        int samples = Math.min (
            (int) Math.floor (ratio),
            MAX_SAMPLES
        );
        if (Math.abs (ratio - Math.floor (ratio)) >= 1e-14d)
            samples = Math.min (samples + 1, MAX_SAMPLES);
        final double updatedStep = samples < MAX_SAMPLES ? step : span / MAX_SAMPLES;
        final List<XYChart.Data<Number, Number>> buffer = new ArrayList<> (samples);
        for (int i = 0; i < samples; ++i) {
            final double x = start + i * updatedStep;
            final double y = func.compute (x);
            if (Double.isNaN (y))
                continue;
            buffer.add (new XYChart.Data<> (x, y));
        }
        /* add the final data point (end point) */
        {
            final double x = end;
            final double y = func.compute (x);
            if (Double.isNaN (y))
                return;
            buffer.add (new XYChart.Data<> (x, y));
        }
        series.getData ().clear ();
        series.getData ().addAll (buffer);
    }

    private void destroy (Data.Entry entry) {
        Objects.requireNonNull (entry, "Cannot destroy a null entry");
        Series<Number, Number> removed = entryPlotMap.remove (entry);
        if (removed != null) {
            removed.nameProperty ().unbind ();
            plots.remove (removed);
        }
    }
}
