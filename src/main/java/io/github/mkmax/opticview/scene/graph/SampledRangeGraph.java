package io.github.mkmax.opticview.scene.graph;

import io.github.mkmax.opticview.scene.controls.UnitListCell;
import io.github.mkmax.opticview.units.IUnit;
import io.github.mkmax.opticview.util.ListUtils;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.util.Duration;

import java.util.*;

public class SampledRangeGraph extends Region {

    /* +--------------------------------------------------------------------+ */
    /* |                            DATA WRAPPER                            | */
    /* +--------------------------------------------------------------------+ */
    private static final class DataWrapper {

        /* +------------+ */
        /* | PROPERTIES | */
        /* +------------+ */
        private final StringProperty parentSeriesName = new SimpleStringProperty ();
        public StringProperty parentSeriesNameProperty ()
            { return parentSeriesName; }
        public String getParentSeriesName ()
            { return parentSeriesName.get(); }
        public void setParentSeriesName (String nSeriesName)
            { parentSeriesName.set (nSeriesName); }

        /* +------------+ */
        /* | COMPONENTS | */
        /* +------------+ */
        private final Tooltip tooltip = new Tooltip ();
        private final GridPane container = new GridPane ();
        private final Label seriesLabel = new Label ();
        private final Label
            xLabel = new Label ("X:"),
            yLabel = new Label ("Y:");
        private final Label
            xValueLabel = new Label (),
            yValueLabel = new Label ();

        /* setup the components to be added to the tooltip "graphic" */
        {
            /* series label */
            GridPane.setConstraints (seriesLabel, 0, 0, 2, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

            /* X/Y labels and value labels */
            GridPane.setConstraints (xLabel, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
            GridPane.setConstraints (yLabel, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
            GridPane.setConstraints (xValueLabel, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
            GridPane.setConstraints (yValueLabel, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

            /* add all of the components to the container */
            container.getChildren ().addAll (
                seriesLabel,
                xLabel, xValueLabel,
                yLabel, yValueLabel
            );

            /* setup the tooltip itself */
            tooltip.setHideDelay (Duration.seconds (0d));
            tooltip.setShowDelay (Duration.seconds (0d));
            tooltip.setGraphic (container);
        }

        /* +--------------------------+ */
        /* | INITIALIZATION & MEMBERS | */
        /* +--------------------------+ */
        private final Series<Number, Number> owner;
        private final XYChart.Data<Number, Number> data;

        public DataWrapper (Series<Number, Number> pOwner, XYChart.Data<Number, Number> pData) {
            owner = Objects.requireNonNull (pOwner);
            data = Objects.requireNonNull (pData);
            data.XValueProperty ().addListener (onXValueChanged);
            data.YValueProperty ().addListener (onYValueChanged);
            data.nodeProperty ().addListener (onDataNodeChanged);

            /* setup the tooltip data now */
            seriesLabel.textProperty ().bind (pOwner.nameProperty ());
            xValueLabel.setText (data.getXValue () == null ? "NaN" : data.getXValue ().toString ());
            yValueLabel.setText (data.getYValue () == null ? "NaN" : data.getYValue ().toString ());
            if (data.getNode () != null)
                Tooltip.install (data.getNode (), tooltip);
        }

        /* +----------+ */
        /* | HANDLERS | */
        /* +----------+ */

        /* +--- PROPERTY CHANGE HANDLERS ---+ */
        private final ChangeListener<String> onParentSeriesNameChanged = (__obs, __old, now) ->
            seriesLabel.setText (now);

        /* +--- DATA HANDLERS ---+ */
        private final ChangeListener<Number> onXValueChanged = (__obs, old, now) ->
            xValueLabel.setText (now == null ? "NaN" : now.toString ());

        private final ChangeListener<Number> onYValueChanged = (__obs, old, now) ->
            yValueLabel.setText (now == null ? "NaN" : now.toString ());

        private final ChangeListener<Node> onDataNodeChanged = (__obs, old, now) -> {
            if (old != null)
                Tooltip.uninstall (old, tooltip);
            if (now != null)
                Tooltip.install (now, tooltip);
        };

        /* +-----------+ */
        /* | INTERFACE | */
        /* +-----------+ */
        public void dispose () {
            seriesLabel.textProperty ().unbind ();
            data.XValueProperty ().removeListener (onXValueChanged);
            data.YValueProperty ().removeListener (onYValueChanged);
            data.nodeProperty ().removeListener (onDataNodeChanged);
        }
    }


    /* +--------------------------------------------------------------------+ */
    /* |                           SERIES WRAPPER                           | */
    /* +--------------------------------------------------------------------+ */
    private static final class SeriesWrapper {

        /* +--------------------------+ */
        /* | INITIALIZATION & MEMBERS | */
        /* +--------------------------+ */
        private final ObservableList<DataWrapper> data = FXCollections.observableArrayList ();
        private final ObservableList<DataWrapper> immutableData =
            FXCollections.observableList (Collections.unmodifiableList (data));
        private final Series<Number, Number> series = new Series<> ();

        public SeriesWrapper () {
            /* already initialized */
        }

        /* +----------+ */
        /* | HANDLERS | */
        /* +----------+ */
        private final ListChangeListener<XYChart.Data<Number, Number>> onSeriesDataChanged = (change) -> {
            while (change.next ()) {
                if (change.wasAdded ()) {
                    final List<? extends XYChart.Data<Number, Number>> addedList = change.getAddedSubList ();
                    final List<DataWrapper> transferBuffer = new ArrayList<> (addedList.size ());
                    addedList.forEach (i ->
                        transferBuffer.add (new DataWrapper (series, i)));
                    data.addAll (transferBuffer);
                }
                else if (change.wasRemoved ())
                    data.removeIf (w -> {
                        if (change.getRemoved ().contains (w.data)) {
                            w.dispose ();
                            return true;
                        }
                        return false;
                    });
            }
        };

        /* attach handlers to their targets */
        {
            series.getData ().addListener (onSeriesDataChanged);
        }

        /* +-----------+ */
        /* | INTERFACE | */
        /* +-----------+ */
        public ObservableList<DataWrapper> getImmutableWrappedData () {
            return immutableData;
        }

        public Series<Number, Number> getSeries () {
            return series;
        }

        public void dispose () {
            data.forEach (DataWrapper::dispose);
            data.clear ();
            series.getData ().removeListener (onSeriesDataChanged);
            series.getData ().clear ();
        }
    }


    /* +--------------------------------------------------------------------+ */
    /* |                           FUNCTION ENTRY                           | */
    /* +--------------------------------------------------------------------+ */
    public static final class FunctionEntry {
        private static final double STEP_EPSILON = 1e-3d;

        /* +------------+ */
        /* | INTERFACES | */
        /* +------------+ */

        /* +--- FUNCTION ---+ */
        @FunctionalInterface
        public interface IRealFunction {
            double eval (double x);
        }

        /* +--- RANGE LISTENER ---+ */
        @FunctionalInterface
        public interface IRangeChangeListener {
            void onRangeChanged (FunctionEntry source);
        }

        /* +--- FUNCTION LISTENER ---+ */
        @FunctionalInterface
        public interface IFunctionChangeListener {
            void onFunctionChanged (FunctionEntry source);
        }

        /* +------------+ */
        /* | PROPERTIES | */
        /* +------------+ */

        /* +--- NAME ---+ */
        private final StringProperty name = new SimpleStringProperty ();

        public StringProperty nameProperty ()
            { return name; }
        public String getName ()
            { return name.get (); }
        public void setName (String nName)
            { name.set (nName); }

        /* +--- FUNCTION ---+ */
        private final ObjectProperty<IRealFunction> function = new SimpleObjectProperty<> ();

        public ReadOnlyObjectProperty<IRealFunction> functionProperty ()
            { return function; }
        public IRealFunction getFunction ()
            { return function.get (); }
        public void setFunction (IRealFunction nFunction)
            { function.set (Objects.requireNonNull (nFunction, "a function must be specified")); }

        /* +--- RANGE ---+ */
        private final DoubleProperty
            start = new SimpleDoubleProperty (),
            step  = new SimpleDoubleProperty (),
            end   = new SimpleDoubleProperty ();

        public DoubleProperty startProperty ()
            { return start; }
        public DoubleProperty stepProperty ()
            { return step; }
        public DoubleProperty endProperty ()
            { return end; }

        public double getStart ()
            { return start.get (); }
        public double getStep ()
            { return step.get (); }
        public double getEnd ()
            { return end.get (); }

        public void setStart (double nStart) {
            validateRange (nStart, getStart (), getEnd ());
            start.setValue (nStart);
        }

        public void setStep (double nStep) {
            validateRange (getStart (), nStep, getEnd ());
            step.setValue (nStep);
        }

        public void setEnd (double nEnd) {
            validateRange (getStart (), getStep (), nEnd);
            end.setValue (nEnd);
        }

        public void setRange (double pStart, double pStep, double pEnd) {
            validateRange (pStart, pStep, pEnd);
            start.setValue (pStart);
            step.setValue (pStep);
            end.setValue (pEnd);
        }

        /* +-----------+ */
        /* | LISTENERS | */
        /* +-----------+ */

        /* +--- RANGE ---+ */
        private final List<IRangeChangeListener> rangeChangeListeners = new ArrayList<> ();

        public void addRangeListener (IRangeChangeListener lis)
            { if (lis != null) rangeChangeListeners.add (lis); }
        public void removeRangeListener (IRangeChangeListener lis)
            { rangeChangeListeners.remove (lis); }

        /* +--- FUNCTION ---+ */
        private final List<IFunctionChangeListener> functionChangeListeners = new ArrayList<> ();

        public void addFunctionListener (IFunctionChangeListener lis)
            { if (lis != null) functionChangeListeners.add (lis); }
        public void removeFunctionListener (IFunctionChangeListener lis)
            { functionChangeListeners.remove (lis); }

        /* +--------------------------+ */
        /* | INITIALIZATION & MEMBERS | */
        /* +--------------------------+ */
        private Series<Number, Number> associatedSeries;
        private final IUnit
            inputUnits,
            outputUnits;

        FunctionEntry (
            IRealFunction pFunction,
            String pName,
            IUnit pInputUnits,
            IUnit pOutputUnits,
            double pStart,
            double pStep,
            double pEnd)
        {
            inputUnits = Objects.requireNonNull (pInputUnits, "input units must be specified");
            outputUnits = Objects.requireNonNull (pOutputUnits, "output units must be specified");
            setFunction (pFunction);
            setName (pName);
            setRange (pStart, pStep, pEnd);
        }

        /* +----------+ */
        /* | HANDLERS | */
        /* +----------+ */
        private final ChangeListener<IRealFunction> onFunctionChanged = (__obs, __old, __now) ->
            functionChangeListeners.forEach (i -> i.onFunctionChanged (this));

        private final ChangeListener<Number> onRangeChanged = (__obs, __old, __now) ->
            rangeChangeListeners.forEach (i -> i.onRangeChanged (this));

        /* install the required handlers */
        {
            function.addListener (onFunctionChanged);
            start.addListener (onRangeChanged);
            step.addListener (onRangeChanged);
            end.addListener (onRangeChanged);
        }

        /* +-----------+ */
        /* | INTERFACE | */
        /* +-----------+ */

        /* +--- SERIES ---+ */
        public Series<Number, Number> getAssociatedSeries () {
            return associatedSeries;
        }

        public void setAssociatedSeries (Series<Number, Number> series) {
            associatedSeries = series;
        }

        /* +-- UNITS ---+ */
        public IUnit getInputUnits () {
            return inputUnits;
        }

        public IUnit getOutputUnits () {
            return outputUnits;
        }

        /* +--- DISPOSE ---+ s*/
        public void dispose () {
            function.removeListener (onFunctionChanged);
            start.removeListener (onRangeChanged);
            step.removeListener (onRangeChanged);
            end.removeListener (onRangeChanged);
        }

        /* +-----------+ */
        /* | UTILITIES | */
        /* +-----------+ */
        private static void validateRange (
            double pStart,
            double pStep,
            double pEnd) throws RuntimeException
        {
            if (Math.abs (pStep) < STEP_EPSILON)
                throw new RuntimeException ("step is too small (under " + STEP_EPSILON + ")");
            if (Math.signum (pEnd - pStart) != Math.signum (pStep))
                throw new RuntimeException ("step is heading out of interval bounds");
        }
    }


    /* +--------------------------------------------------------------------+ */
    /* |                            FUNCTION SET                            | */
    /* +--------------------------------------------------------------------+ */
    public static final class FunctionData {

        /* +-----------+ */
        /* | LISTENERS | */
        /* +-----------+ */
        @FunctionalInterface
        public interface IEntryAdditionListener {
            void onEntryAdded (FunctionData source, FunctionEntry added);
        }

        @FunctionalInterface
        public interface IEntryRemovalListener {
            void onEntryRemoved (FunctionData source, FunctionEntry removed);
        }

        /* +--------------------------+ */
        /* | MEMBERS & INITIALIZATION | */
        /* +--------------------------+ */
        private final ObservableList<FunctionEntry> entries = FXCollections.observableArrayList ();
        private final ObservableList<FunctionEntry> immutableEntries
            = FXCollections.observableList (Collections.unmodifiableList (entries));

        /* +-----------+ */
        /* | INTERFACE | */
        /* +-----------+ */
        public ObservableList<FunctionEntry> getImmutableEntries () {
            return immutableEntries;
        }

        /* +-----------+ */
        /* | LISTENERS | */
        /* +-----------+ */

        /* +--- ENTRY ADDITION LISTENERS ---+ */
        private final List<IEntryAdditionListener> entryAdditionListeners = new ArrayList<> ();

        public void addEntryAdditionListener (IEntryAdditionListener lis)
            { if (lis != null) entryAdditionListeners.add (lis); }
        public void removeEntryAdditionListener (IEntryAdditionListener lis)
            { entryAdditionListeners.remove (lis); }

        /* +--- ENTRY REMOVAL LISTENERS ---+ */
        private final List<IEntryRemovalListener> entryRemovalListeners = new ArrayList<> ();

        public void addEntryRemovalListener (IEntryRemovalListener lis)
            { if (lis != null) entryRemovalListeners.add (lis); }
        public void removeEntryRemovalListener (IEntryRemovalListener lis)
            { entryRemovalListeners.remove (lis); }

        /* +----------+ */
        /* | HANDLERS | */
        /* +----------+ */
        private final ListChangeListener<FunctionEntry> onEntriesChanged = (change) -> {
            while (change.next ()) {
                if (change.wasAdded ())
                    change.getAddedSubList ().forEach (i ->
                        entryAdditionListeners.forEach (j -> j.onEntryAdded (this, i)));
                else if (change.wasRemoved ())
                    change.getRemoved ().forEach (i ->
                        entryRemovalListeners.forEach (j -> j.onEntryRemoved (this, i)));
            }
        };

        /* install entries listener */
        {
            entries.addListener (onEntriesChanged);
        }

        /* +-----------+ */
        /* | INTERFACE | */
        /* +-----------+ */

        /* +--- CREATE ENTRY ---+ */
        public void createEntry (
            FunctionEntry.IRealFunction func,
            String                      name,
            IUnit                       inputUnits,
            IUnit                       outputUnits,
            double                      start,
            double                      step,
            double                      end)
        {
            Objects.requireNonNull (func, "a function must be specified");
            Objects.requireNonNull (inputUnits, "the input units must be specified");
            Objects.requireNonNull (outputUnits, "the output units must be specified");
            entries.add (new FunctionEntry (func, name, inputUnits, outputUnits, start, step, end));
        }

        /* +--- CLEAR ENTRIES ---+ */
        public void clearEntries () {
            entries.clear ();
        }

        /* +--- DISPOSING ---+ */
        public void dispose () {
            entries.removeListener (onEntriesChanged);
            entries.forEach (i ->
                entryRemovalListeners.forEach (j -> j.onEntryRemoved (this, i)));
            entries.clear ();
        }
    }


    /* +--------------------------------------------------------------------+ */
    /* |                         SAMPLED RANGE GRAPH                        | */
    /* +--------------------------------------------------------------------+ */

    /* +---------------+ */
    /* | STYLE CLASSES | */
    /* +---------------+ */
    public static final class ComponentClass {
        public static final String
            SAMPLED_RANGE_GRAPH  = "sampled-range-graph",
            AXIS_LABEL_CONTAINER = "axis-label-container",
            AXIS_LABEL_UNITS_BOX = "axis-label-units-box";
    }

    /* +--------------------------+ */
    /* | MEMBERS & INITIALIZATION | */
    /* +--------------------------+ */

    /* constants */
    private static final int MAX_SAMPLES = 4096;

    /* plot data */
    private final FunctionData data = new FunctionData ();
    private final Map<FunctionEntry, SeriesWrapper> entryToSeries = new HashMap<> ();
    private final ObservableList<Series<Number, Number>> plots = FXCollections.observableArrayList ();

    private final ObservableList<IUnit>
        supportedInputUnits,
        supportedOutputUnits;

    public SampledRangeGraph (
        final List<IUnit> pSupportedInputUnits,
        final List<IUnit> pSupportedOutputUnits,
        IUnit initialInputUnits,
        IUnit initialOutputUnits)
    {
        supportedInputUnits =
            FXCollections.observableList (
                Collections.unmodifiableList (Objects.requireNonNull (pSupportedInputUnits)));
        supportedOutputUnits =
            FXCollections.observableList (
                Collections.unmodifiableList (Objects.requireNonNull (pSupportedOutputUnits)));

        /* ensure that the units within each list actually are interconvertible and not null */
        validateUnits (supportedInputUnits,
            "input units may not be null",
            "input units are not interconvertible");
        validateUnits (supportedOutputUnits,
            "output units may not be null",
            "output units are not interconvertible");

        if (initialInputUnits != null && !supportedInputUnits.contains (initialInputUnits))
            throw new RuntimeException ("initial input unit is not contained in supported units");
        if (initialOutputUnits != null && !supportedOutputUnits.contains (initialOutputUnits))
            throw new RuntimeException ("initial output unit is not contained in supported units");

        xLabelUnitsBox.setItems (supportedInputUnits);
        yLabelUnitsBox.setItems (supportedOutputUnits);

        setInputUnits (initialInputUnits);
        setOutputUnits (initialOutputUnits);
    }

    private static void validateUnits (
        List<? extends IUnit> units,
        String nullityErrorMessage,
        String convertibilityErrorMessage)
    {
        ListUtils.pairCombinationIterator (Objects.requireNonNull (units), (a, b) -> {
            if (a == null || b == null)
                throw new RuntimeException (nullityErrorMessage);
            if (!(a.isConvertibleTo (b)   &&
                  a.isConvertibleFrom (b) &&
                  b.isConvertibleTo (a)   &&
                  b.isConvertibleFrom (a)))
                throw new RuntimeException (convertibilityErrorMessage);
        });
    }

    /* +------------+ */
    /* | COMPONENTS | */
    /* +------------+ */

    /* Axes */
    private final NumberAxis
        x = new NumberAxis (),
        y = new NumberAxis ();

    /* Main Container & Chart */
    private final GridPane container = new GridPane ();
    private final LineChart<Number, Number> chart = new LineChart<> (x, y);

    /* Axis Labels & Controls */
    private final Group
        xLabelContainer = new Group (),
        yLabelContainer = new Group ();
    private final HBox
        xLabelBox = new HBox (),
        yLabelBox = new HBox ();
    private final ComboBox<IUnit>
        xLabelUnitsBox = new ComboBox<> (),
        yLabelUnitsBox = new ComboBox<> ();
    private final Label
        xLabel = new Label (),
        yLabel = new Label ();

    /* configuration & initialization */
    {
        /* configure the region first */
        getStyleClass ().add (ComponentClass.SAMPLED_RANGE_GRAPH);
        getChildren ().add (container);

        /* configure the chart */
        chart.setData (plots);

        /* configure the X/Y labels */
        xLabelContainer.getStyleClass ().add (ComponentClass.AXIS_LABEL_CONTAINER);
        yLabelContainer.getStyleClass ().add (ComponentClass.AXIS_LABEL_CONTAINER);
        xLabelUnitsBox.getStyleClass ().add (ComponentClass.AXIS_LABEL_UNITS_BOX);
        yLabelUnitsBox.getStyleClass ().add (ComponentClass.AXIS_LABEL_UNITS_BOX);

        xLabelContainer.getChildren ().add (xLabelBox);
        yLabelContainer.getChildren ().add (yLabelBox);

        xLabelBox.getChildren ().addAll (xLabel, xLabelUnitsBox);
        yLabelBox.getChildren ().addAll (yLabel, yLabelUnitsBox);

        xLabelBox.setAlignment (Pos.CENTER);
        yLabelBox.setAlignment (Pos.CENTER);
        yLabelBox.setRotate (270d);

        xLabelUnitsBox.setButtonCell (new UnitListCell<> (UnitListCell.DisplayMode.MNEMONIC_ONLY));
        xLabelUnitsBox.setCellFactory (view -> new UnitListCell<> ());

        yLabelUnitsBox.setButtonCell (new UnitListCell<> (UnitListCell.DisplayMode.MNEMONIC_ONLY));
        yLabelUnitsBox.setCellFactory (view -> new UnitListCell<> ());

        /* configure the container & set constraints */
        GridPane.setConstraints (xLabelContainer, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        GridPane.setConstraints (yLabelContainer, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.ALWAYS);
        GridPane.setConstraints (chart, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

        /* fill the container and add it to the main region */
        container.getChildren ().addAll (xLabelContainer, yLabelContainer, chart);
    }

    /* +------------+ */
    /* | PROPERTIES | */
    /* +------------+ */

    /* +--- UNITS ---+ */
    private final ObjectProperty<IUnit>
        inputUnits = xLabelUnitsBox.valueProperty (),
        outputUnits = yLabelUnitsBox.valueProperty ();

    public ReadOnlyObjectProperty<IUnit> inputUnitProperty ()
        { return inputUnits; }
    public ReadOnlyObjectProperty<IUnit> outputUnitProperty ()
        { return outputUnits; }

    public IUnit getInputUnits ()
        { return inputUnits.get (); }
    public IUnit getOutputUnits ()
        { return outputUnits.get (); }

    public void setInputUnits (IUnit nInputUnits)
        { inputUnits.set (nInputUnits); }
    public void setOutputUnits (IUnit nOutputUnits)
        { outputUnits.set (nOutputUnits); }

    /* +----------+ */
    /* | HANDLERS | */
    /* +----------+ */

    /* dimension handling */
    private final ChangeListener<Number> onRegionWidthChanged = (__obs, __old, now) ->
        container.setPrefWidth (now == null ? Double.NaN : now.doubleValue ());

    private final ChangeListener<Number> onRegionHeightChanged = (__obs, __old, now) ->
        container.setPrefHeight (now == null ? Double.NaN : now.doubleValue ());

    /* function entry listeners */
    private final FunctionEntry.IFunctionChangeListener onFunctionChanged =
        this::plotWithCurrentUnits;

    private final FunctionEntry.IRangeChangeListener onRangeChanged =
        this::plotWithCurrentUnits;

    private final FunctionData.IEntryAdditionListener onEntryAdded = (__data, entry) -> {
        /* entry should never be null */
        entry.addFunctionListener (onFunctionChanged);
        entry.addRangeListener (onRangeChanged);
        plotWithCurrentUnits (entry);
    };

    private final FunctionData.IEntryRemovalListener onEntryRemoved = (__data, entry) -> {
        /* entry should never be null */
        entry.removeFunctionListener (onFunctionChanged);
        entry.removeRangeListener (onRangeChanged);
        destroy (entry);
    };

    /* units listeners */
    private final ChangeListener<IUnit> onInputUnitsChanged = (__obs, __old, __now) ->
        data.getImmutableEntries ().forEach (this::plotWithCurrentUnits);

    private final ChangeListener<IUnit> onOutputUnitsChanged = (__obs, __old, __now) ->
        data.getImmutableEntries ().forEach (this::plotWithCurrentUnits);

    /* install handlers */
    {
        widthProperty ().addListener (onRegionWidthChanged);
        heightProperty ().addListener (onRegionHeightChanged);

        data.addEntryAdditionListener (onEntryAdded);
        data.addEntryRemovalListener (onEntryRemoved);

        inputUnits.addListener (onInputUnitsChanged);
        outputUnits.addListener (onOutputUnitsChanged);
    }

    /* +-----------+ */
    /* | INTERFACE | */
    /* +-----------+ */
    public FunctionData getFunctionData () {
        return data;
    }

    public void setXLabel (String text) {
        xLabel.setText (text);
    }

    public void setYLabel (String text) {
        yLabel.setText (text);
    }

    /* +----------+ */
    /* | INTERNAL | */
    /* +----------+ */
    private void plotWithCurrentUnits (FunctionEntry entry) {
        plot (entry, getInputUnits (), getOutputUnits ());
    }

    private void plot (FunctionEntry entry, IUnit inputUnits, IUnit outputUnits) {
        /* parameter validation */
        Objects.requireNonNull (entry, "Cannot plot a null entry");
        Objects.requireNonNull (inputUnits, "input units cannot be null");
        Objects.requireNonNull (outputUnits, "output units cannot be null");

        /* verify that the entry's input/output units can be converted to supported ones */
        for (IUnit in : supportedInputUnits)
            if (!in.isConvertibleFrom (entry.getInputUnits ()))
                throw new RuntimeException ("cannot plot an entry with unsupported input units");

        for (IUnit out : supportedOutputUnits)
            if (!out.isConvertibleFrom (entry.getOutputUnits ()))
                throw new RuntimeException ("cannot plot an entry with unsupported output units");

        /* series lookup/creation */
        SeriesWrapper wrapper = entryToSeries.get (entry);
        if (wrapper == null) { /* new plot */
            wrapper = new SeriesWrapper ();
            wrapper.getSeries ().nameProperty ().bind (entry.nameProperty ());
            entryToSeries.put (entry, wrapper);
            plots.add (wrapper.getSeries ());
        }

        /* entry data queries & range validation */
        final FunctionEntry.IRealFunction func = entry.getFunction ();
        final IUnit
            entryInputUnits = entry.getInputUnits (),
            entryOutputUnits = entry.getOutputUnits ();
        final double
            start = entry.getStart (),
            step = entry.getStep (),
            end = entry.getEnd ();
        final double span = end - start;
        final double ratio = Math.abs (span / step);
        int samples = Math.min (
            (int) Math.ceil (ratio) + 1,
            MAX_SAMPLES
        );
        final double realstep = samples < MAX_SAMPLES ? step : span / MAX_SAMPLES;

        /* creating plot data */
        final List<XYChart.Data<Number, Number>> buffer = new ArrayList<> (samples);
        for (int i = 0; i < samples; ++i) {
            final double x = i + 1 == samples ? end : start + i * realstep;
            final double y = func.eval (x);
            if (Double.isNaN (y))
                continue;
            final double realX = getInputUnits ().convertFrom (entryInputUnits, x);
            final double realY = getOutputUnits ().convertFrom (entryOutputUnits, y);
            buffer.add (new XYChart.Data<> (realX, realY));
        }

        /* submitting plot data */
        wrapper.getSeries ().getData ().clear ();
        wrapper.getSeries ().getData ().addAll (buffer);
    }

    private void destroy (FunctionEntry entry) {
        Objects.requireNonNull (entry, "Cannot destroy a null entry");
        SeriesWrapper removed = entryToSeries.remove (entry);
        if (removed != null) {
            removed.getSeries ().nameProperty ().unbind ();
            removed.getSeries ().getData ().clear ();
            plots.remove (removed.getSeries ());
        }
    }
}
