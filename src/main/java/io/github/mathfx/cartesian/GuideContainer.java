package io.github.mathfx.cartesian;

import io.github.mathfx.cartesian.part.DecimalPartitionScheme;
import io.github.mathfx.cartesian.part.PartitionScheme;
import io.github.mathfx.cartesian.part.PartitionScheme.Index;
import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.Interval;
import io.github.mathfx.util.ObservableGroup;
import io.github.mathfx.util.css.StyleableFactory;
import io.github.mathfx.util.format.PrecisionDecimalFormat;
import io.github.mathfx.util.format.ScientificDecimalFormat;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.StyleableObjectProperty;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GuideContainer extends StackPane implements Disposable {

    private static final String VERTICAL_STYLE_CLASS   = "vertical";
    private static final String HORIZONTAL_STYLE_CLASS = "horizontal";
    private static final String GUIDE_STYLE_CLASS      = "guide";
    private static final String LABEL_STYLE_CLASS      = "label";

    private static final StyleableFactory<GuideContainer> FACTORY
        = new StyleableFactory<> (Pane.getClassCssMetaData ());

    /* +----------------------------+ */
    /* | CSS META DATA DECLARATIONS | */
    /* +----------------------------+ */

    private static final String HORIZONTAL_PIXELS_PER_UNIT_PROPERTY               = "-mfx-horizontal-pixels-per-unit";
    private static final String VERTICAL_PIXELS_PER_UNIT_PROPERTY                 = "-mfx-vertical-pixels-per-unit";
    private static final String SCIENTIFIC_NOTATION_PRECISION_PROPERTY            = "-mfx-scientific-notation-precision";
    private static final String DECIMAL_NOTATION_PRECISION_PROPERTY               = "-mfx-decimal-notation-precision";
    private static final String ABS_MIN_SCIENTIFIC_NOTATION_TOGGLE_LIMIT_PROPERTY = "-mfx-abs-min-scientific-notation-toggle-limit";
    private static final String ABS_MAX_SCIENTIFIC_NOTATION_TOGGLE_LIMIT_PROPERTY = "-mfx-abs-max-scientific-notation-toggle-limit";

    static {
        FACTORY.buildSizeCssMetaData ()
            .setProperty (HORIZONTAL_PIXELS_PER_UNIT_PROPERTY)
            .setInitialValue (256)
            .build ();
        FACTORY.buildSizeCssMetaData ()
            .setProperty (VERTICAL_PIXELS_PER_UNIT_PROPERTY)
            .setInitialValue (256)
            .build ();
        FACTORY.buildSizeCssMetaData ()
            .setProperty (SCIENTIFIC_NOTATION_PRECISION_PROPERTY)
            .setInitialValue (3)
            .build ();
        FACTORY.buildSizeCssMetaData ()
            .setProperty (DECIMAL_NOTATION_PRECISION_PROPERTY)
            .setInitialValue (3)
            .build ();
        FACTORY.buildSizeCssMetaData ()
            .setProperty (ABS_MIN_SCIENTIFIC_NOTATION_TOGGLE_LIMIT_PROPERTY)
            .setInitialValue (1e-3d)
            .build ();
         FACTORY.buildSizeCssMetaData ()
            .setProperty (ABS_MAX_SCIENTIFIC_NOTATION_TOGGLE_LIMIT_PROPERTY)
            .setInitialValue (1e3d)
            .build ();
    }

    /* +----------------------+ */
    /* | STYLEABLE PROPERTIES | */
    /* +----------------------+ */

    /* HORIZONTAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> horizontalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("nPpu")
        .setProperty (HORIZONTAL_PIXELS_PER_UNIT_PROPERTY)
        .build ();

    public double getHorizontalPixelsPerUnit () {
        return horizontalPixelsPerUnit.get ().doubleValue ();
    }

    public void setHorizontalPixelsPerUnit (double nPpu) {
        horizontalPixelsPerUnit.set (nPpu);
    }

    /* VERTICAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> verticalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("verticalPixelsPerUnit")
        .setProperty (VERTICAL_PIXELS_PER_UNIT_PROPERTY)
        .build ();

    public double getVerticalPixelsPerUnit () {
        return verticalPixelsPerUnit.get ().doubleValue ();
    }

    public void setVerticalPixelsPerUnit (double nPpu) {
        this.verticalPixelsPerUnit.set (nPpu);
    }

    /* SCIENTIFIC NOTATION PRECISION */
    private final StyleableObjectProperty<Number> scientificNotationPrecision = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("scientificNotationPrecision")
        .setProperty (SCIENTIFIC_NOTATION_PRECISION_PROPERTY)
        .build ();

    public int getScientificNotationPrecision () {
        return scientificNotationPrecision.get ().intValue ();
    }

    public void setScientificNotationPrecision (int nPrecision) {
        scientificNotationPrecision.set (nPrecision);
    }

    /* DECIMAL NOTATION PRECISION */
    private final StyleableObjectProperty<Number> decimalNotationPrecision = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("decimalNotationPrecision")
        .setProperty (DECIMAL_NOTATION_PRECISION_PROPERTY)
        .build ();

    public int getDecimalNotationPrecision () {
        return decimalNotationPrecision.get ().intValue ();
    }

    public void setDecimalNotationPrecision (int nPrecision) {
        decimalNotationPrecision.set (nPrecision);
    }

    /* ABS MIN SCIENTIFIC NOTATION TOGGLE LIMIT */
    private final StyleableObjectProperty<Number> absMinScientificNotationToggleLimit
        = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("absMinScientificNotationToggleLimit")
        .setProperty (ABS_MIN_SCIENTIFIC_NOTATION_TOGGLE_LIMIT_PROPERTY)
        .build ();

    public Number getAbsMinScientificNotationToggleLimit () {
        return absMinScientificNotationToggleLimit.get ();
    }

    public void setAbsMinScientificNotationToggleLimit (double nMin) {
        this.absMinScientificNotationToggleLimit.set (nMin);
    }

    /* ABS MAX SCIENTIFIC NOTATION TOGGLE LIMIT */
    private final StyleableObjectProperty<Number> absMaxScientificNotationToggleLimit
        = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("absMaxScientificNotationToggleLimit")
        .setProperty (ABS_MAX_SCIENTIFIC_NOTATION_TOGGLE_LIMIT_PROPERTY)
        .build ();

    public Number getAbsMaxScientificNotationToggleLimit () {
        return absMaxScientificNotationToggleLimit.get ();
    }

    public void setAbsMaxScientificNotationToggleLimit (double nMax) {
        this.absMaxScientificNotationToggleLimit.set (nMax);
    }

    /* +------------------+ */
    /* | STANDARD MEMBERS | */
    /* +------------------+ */

    private static final double MIN_PIXELS_PER_UNIT = 64d;
    private static final double MAX_PIXELS_PER_UNIT = 65536d;

    private final ChangeListener<Parent> parentListener = (__obs, __old, now) -> {
        if (now instanceof GraphView) ((GraphView) now).getOrthoGroup ().add (this::update);
    };

    {
        parentProperty ().addListener (parentListener);
    }

    private final ScientificDecimalFormat scientific  = new ScientificDecimalFormat ();
    private final PrecisionDecimalFormat  precision   = new PrecisionDecimalFormat ();
    private       PartitionScheme         partitioner = DecimalPartitionScheme.getInstance ();

    private final DoubleProperty     width       = new SimpleDoubleProperty ();
    private final DoubleProperty     height      = new SimpleDoubleProperty ();
    private final ObservableGroup<?> parentGroup = ObservableGroup.observeParentSize (this, width, height);
    private final ObservableGroup<?> styleGroup  = new ObservableGroup<> (
        horizontalPixelsPerUnit, verticalPixelsPerUnit, scientificNotationPrecision, decimalNotationPrecision,
        absMinScientificNotationToggleLimit, absMaxScientificNotationToggleLimit);

    private final List<Rectangle> hGuides = new ArrayList<> ();
    private final List<Label>     hLabels = new ArrayList<> ();
    private final List<Rectangle> vGuides = new ArrayList<> ();
    private final List<Label>     vLabels = new ArrayList<> ();

    private final Pane guidePanel = new Pane ();
    private final Pane labelPanel = new Pane ();

    public GuideContainer () {
        getChildren ().addAll (guidePanel, labelPanel);
    }

    @Override
    public void dispose () {
        parentProperty ().removeListener (parentListener);
        parentGroup.dispose ();
        styleGroup.dispose ();
    }

    private void update () {
        setWidth (width.get ());
        setHeight (height.get ());

        if (getParent () instanceof GraphView) {
            final GraphView gv = (GraphView) getParent ();
            final Interval hWinInterval = gv.getHorizontalWindowInterval ();
            final Interval hComInterval = gv.getHorizontalComponentInterval ();
            final Interval vWinInterval = gv.getVerticalWindowInterval ();
            final Interval vComInterval = gv.getVerticalComponentInterval ();

            boolean hUseScientific =
                hWinInterval.distance () <= Math.abs(getAbsMinScientificNotationToggleLimit ().doubleValue ()) ||
                hWinInterval.distance () >= Math.abs(getAbsMaxScientificNotationToggleLimit ().doubleValue ());
            boolean vUseScientific =
                vWinInterval.distance () <= Math.abs (getAbsMinScientificNotationToggleLimit ().doubleValue ()) ||
                vWinInterval.distance () >= Math.abs (getAbsMaxScientificNotationToggleLimit ().doubleValue ());

            final double hppu = clampPixelsPerUnit (getHorizontalPixelsPerUnit ());
            final double vppu = clampPixelsPerUnit (getVerticalPixelsPerUnit ());
            scientific.setPrecision (getScientificNotationPrecision ());
            precision.setPrecision (getDecimalNotationPrecision ());

            final List<Index> hIndices = partitioner.partition (
                gv.getHorizontalWindowInterval (),
                gv.getHorizontalComponentInterval (),
                hppu);
            final List<Index> vIndices = partitioner.partition (
                gv.getVerticalWindowInterval (),
                gv.getVerticalComponentInterval (),
                vppu);

            /* vertical */
            while (vGuides.size () < hIndices.size ()) {
                final Rectangle rect = new Rectangle ();
                rect.getStyleClass ().addAll (VERTICAL_STYLE_CLASS, GUIDE_STYLE_CLASS);
                vGuides.add (rect);
                guidePanel.getChildren ().add (rect);
            }
            while (vLabels.size () < hIndices.size ()) {
                final Label label = new Label ();
                label.getStyleClass ().addAll ();
            }

        }
        else {
            /* TODO: Hide everything */
        }
    }

    public double clampPixelsPerUnit (double ppu) {
        return Math.max (Math.min (ppu, MAX_PIXELS_PER_UNIT), MIN_PIXELS_PER_UNIT);
    }
}