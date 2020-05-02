package io.github.mathfx.cartesian;

import io.github.mathfx.cartesian.part.DecimalPartitionScheme;
import io.github.mathfx.cartesian.part.PartitionScheme;
import io.github.mathfx.cartesian.part.PartitionScheme.Index;
import io.github.mathfx.util.Disposable;
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
import javafx.scene.shape.Rectangle;

import java.util.*;

public class GuideContainer extends Pane implements Disposable {

    private static final StyleableFactory<GuideContainer> FACTORY
        = new StyleableFactory<> (Pane.getClassCssMetaData ());

    /* +----------------------------+ */
    /* | CSS META DATA DECLARATIONS | */
    /* +----------------------------+ */

    private static final String HORIZONTAL_PIXELS_PER_UNIT_PROPERTY    = "-mfx-horizontal-pixels-per-unit";
    private static final String VERTICAL_PIXELS_PER_UNIT_PROPERTY      = "-mfx-vertical-pixels-per-unit";
    private static final String SCIENTIFIC_NOTATION_PRECISION_PROPERTY = "-mfx-scientific-notation-precision";
    private static final String DECIMAL_NOTATION_PRECISION_PROPERTY    = "-mfx-decimal-notation-precision";

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
    }

    /* +----------------------+ */
    /* | STYLEABLE PROPERTIES | */
    /* +----------------------+ */

    /* HORIZONTAL PIXELS PER UNIT */
    final StyleableObjectProperty<Number> horizontalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
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
    final StyleableObjectProperty<Number> verticalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
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
    final StyleableObjectProperty<Number> scientificNotationPrecision = FACTORY.<Number>buildStyleableProperty ()
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
    final StyleableObjectProperty<Number> decimalNotationPrecision = FACTORY.<Number>buildStyleableProperty ()
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

    /* +-----------------+ */
    /* | REGULAR MEMBERS | */
    /* +-----------------+ */

    private static final double MIN_PIXELS_PER_UNIT = 64d;
    private static final double MAX_PIXELS_PER_UNIT = 65536d;

    /* the numeric only vertical guides (i.e., from bottom to top) guides */
    private final List<Rectangle> vGuides = new ArrayList<> ();
    private final List<Label>     vLabels = new ArrayList<> ();

    /* the numeric only horizontal (i.e., from left to right) guides */
    private final List<Rectangle> hGuides = new ArrayList<> ();
    private final List<Label>     hLabels = new ArrayList<> ();

    private PartitionScheme         partitioner = DecimalPartitionScheme.getInstance ();
    private ScientificDecimalFormat scientific  = new ScientificDecimalFormat ();
    private PrecisionDecimalFormat  precision   = new PrecisionDecimalFormat ();

    private final ChangeListener<Parent> parentListener = (__obs, __old, now) -> {
        if (now instanceof GraphView) {
            final GraphView gv = (GraphView) now;
            gv.getOrthoGroup ().add (this::update);
        }
    };

    {
        parentProperty ().addListener (parentListener);
    }

    private final DoubleProperty     width       = new SimpleDoubleProperty ();
    private final DoubleProperty     height      = new SimpleDoubleProperty ();
    private final ObservableGroup<?> parentGroup = ObservableGroup.observeParentSize (this, width, height);
    private final ObservableGroup<?> styleGroup  = new ObservableGroup<> (
        horizontalPixelsPerUnit, verticalPixelsPerUnit, scientificNotationPrecision, decimalNotationPrecision);
    private final ObservableGroup<?> updateGroup = new ObservableGroup<> (parentGroup, styleGroup);

    public GuideContainer () {
        updateGroup.add (this::update);
    }

    @Override
    public void dispose () {
        parentProperty ().removeListener (parentListener);
        parentGroup.dispose ();
        styleGroup.dispose ();
        updateGroup.dispose ();
    }

    private void update () {
        setWidth (width.get ());
        setHeight (height.get ());

        if (getParent () instanceof GraphView) {
            final GraphView gv = (GraphView) getParent ();
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

            while (vGuides.size () < hIndices)
        }
    }

    private double clampPixelsPerUnit (double ppu) {
        return Math.max (Math.min (ppu, MAX_PIXELS_PER_UNIT), MIN_PIXELS_PER_UNIT);
    }
}
