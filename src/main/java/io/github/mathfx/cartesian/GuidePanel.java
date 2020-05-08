package io.github.mathfx.cartesian;

import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.NumberUtil;
import io.github.mathfx.util.ObservableGroup;
import io.github.mathfx.util.css.StyleableFactory;
import io.github.mathfx.util.format.PrecisionDecimalFormat;
import io.github.mathfx.util.format.ScientificDecimalFormat;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class GuidePanel extends Canvas implements Disposable {

    private static final StyleableFactory<GuidePanel> FACTORY
        = new StyleableFactory<> (Canvas.getClassCssMetaData ());

    /* +---------------------------+ */
    /* | CSS META DATA DECLARATION | */
    /* +---------------------------+ */

    private static final double MIN_PPU = 64d;
    private static final double DEF_PPU = 256d;
    private static final double MAX_PPU = 65536d;

    private static final PartitionScheme DEF_PARTITION_SCHEME = PartitionScheme.DECIMAL;

    static {
        FACTORY.buildEnumCssMetaData (PartitionScheme.class)
            .setProperty ("-mfx-partition-scheme")
            .setInitialValue (DEF_PARTITION_SCHEME)
            .build ();
        FACTORY.buildSizeCssMetaData ()
            .setProperty ("-mfx-horizontal-pixels-per-unit")
            .setInitialValue (DEF_PPU)
            .build ();
        FACTORY.buildSizeCssMetaData ()
            .setProperty ("-mfx-vertical-pixels-per-unit")
            .setInitialValue (DEF_PPU)
            .build ();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
        return FACTORY.getCssMetaData ();
    }

    /* +---------------------------------+ */
    /* | STYLEABLE PROPERTY DECLARATIONS | */
    /* +---------------------------------+ */

    /* PARTITION SCHEME */
    private final StyleableObjectProperty<PartitionScheme> partitionScheme = FACTORY.<PartitionScheme>buildStyleableProperty ()
        .setBean (this)
        .setName ("partitionScheme")
        .setProperty ("-mfx-partition-scheme")
        .build ();

    public PartitionScheme getPartitionScheme () {
        return partitionScheme.get () == null ? DEF_PARTITION_SCHEME : partitionScheme.get ();
    }

    public void setPartitionScheme (PartitionScheme nScheme) {
        this.partitionScheme.set (nScheme == null ? DEF_PARTITION_SCHEME : nScheme);
    }

    /* HORIZONTAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> horizontalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("horizontalPixelsPerUnit")
        .setProperty ("-mfx-horizontal-pixels-per-unit")
        .build ();

    public double getHorizontalPixelsPerUnit () {
        return horizontalPixelsPerUnit.get () == null ? DEF_PPU : horizontalPixelsPerUnit.get ().doubleValue ();
    }

    public void setHorizontalPixelsPerUnit (double nHppu) {
        horizontalPixelsPerUnit.set (NumberUtil.clamp (nHppu, MIN_PPU, MAX_PPU));
    }

    /* VERTICAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> verticalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("verticalPixelsPerUnit")
        .setProperty ("-mfx-vertical-pixels-per-unit")
        .build ();

    public double getVerticalPixelsPerUnit () {
        return verticalPixelsPerUnit.get () == null ? DEF_PPU : verticalPixelsPerUnit.get ().doubleValue ();
    }

    public void setVerticalPixelsPerUnit (double nVppu) {
        verticalPixelsPerUnit.set (NumberUtil.clamp (nVppu, MIN_PPU, MAX_PPU));
    }

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */

    private final GraphicsContext         gc           = getGraphicsContext2D ();

    private final ScientificDecimalFormat sciFormat    = new ScientificDecimalFormat ();
    private final PrecisionDecimalFormat  decFormat    = new PrecisionDecimalFormat ();

    private final DoubleProperty          parentWidth  = new SimpleDoubleProperty ();
    private final DoubleProperty          parentHeight = new SimpleDoubleProperty ();
    private final ObservableGroup<Object> parentGroup  = ObservableGroup.observeParentSize (this, parentWidth, parentHeight);

    private final ChangeListener<Parent>  parentListener = (__obs, __old, now) -> {
        if (now instanceof GraphView) ((GraphView) now).getProjectionGroup ().add (this::render);
    };

    public GuidePanel () {
        parentProperty ().addListener (parentListener);
        parentGroup.add (this::render);
    }

    /* +----------------------------------+ */
    /* | PRIVATE, LISTENERS, & MANAGEMENT | */
    /* +----------------------------------+ */

    @Override
    public void dispose () {
        parentProperty ().removeListener (parentListener);
        parentGroup.dispose ();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
        return getClassCssMetaData ();
    }

    private void render () {
        /* first we must position the panel to cover the entire parent */
        setLayoutX (0d);
        setLayoutY (0d);
        setWidth (parentWidth.get ());
        setHeight (parentHeight.get ());

        /* next we obtain the GraphView window rectangle, or a unit rectangle
         * if the parent is not a GraphView. I chose to continue rendering the
         * guide panel */
        double wLeft   = -1d,
               wRight  =  1d,
               wBottom = -1d,
               wTop    =  1d;
        if (getParent () instanceof GraphView) {
            final GraphView gv = (GraphView) getParent ();
            wLeft   = gv.getLeft ();
            wRight  = gv.getRight ();
            wBottom = gv.getBottom ();
            wTop    = gv.getTop ();
        }

        /* start by clearing the dirty canvas */
        gc.clearRect (0d, 0d, getWidth (), getHeight ());

        /* compute guide indices and render the (1) the lines, (2) labels */
        final List<PartitionIndex> hIndices =
    }
}