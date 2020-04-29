package io.github.mathfx.cartesian;

import io.github.mathfx.cartesian.PartitionScheme.Parcel;
import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.ObservableGroup;
import io.github.mkmax.util.math.Float64;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.util.*;

public class GuideContainer extends Pane implements Disposable {

    public static final String ORIGIN_GUIDE_CLASS = "origin-guide";
    public static final String MAJOR_GUIDE_CLASS  = "major-guide";
    public static final String MINOR_GUIDE_CLASS  = "minor-guide";

    public static final Map<PartitionScheme.Type, String> TYPE_STYLE_CLASS_MAP = Map.of (
        PartitionScheme.Type.MINOR, MINOR_GUIDE_CLASS,
        PartitionScheme.Type.MAJOR, MAJOR_GUIDE_CLASS,
        PartitionScheme.Type.ORIGIN, ORIGIN_GUIDE_CLASS
    );

    public static final StyleablePropertyFactory<GuideContainer> FACTORY
        = new StyleablePropertyFactory<> (Pane.getClassCssMetaData ());

    /* +----------------------------+ */
    /* | CSS META DATA DECLARATIONS | */
    /* +----------------------------+ */

    private static final class Default {
        private static final PartitionSchemes VERT_PART_SCHEME = PartitionSchemes.DECIMAL;
        private static final PartitionSchemes HOR_PART_SCHEME  = PartitionSchemes.DECIMAL;
        private static final double           MIN_VERT_PPU     = 4d;
        private static final double           DEF_VERT_PPU     = 128d;
        private static final double           MAX_VERT_PPU     = 65536d;
        private static final double           MIN_HOR_PPU      = 4d;
        private static final double           DEF_HOR_PPU      = 128d;
        private static final double           MAX_HOR_PPU      = 65536d;
    }

    static {
        FACTORY.createEnumCssMetaData (PartitionSchemes.class, "-mfx-vertical-partition-scheme",   gc -> gc.verticalPartitionScheme,   Default.VERT_PART_SCHEME);
        FACTORY.createEnumCssMetaData (PartitionSchemes.class, "-mfx-horizontal-partition-scheme", gc -> gc.horizontalPartitionScheme, Default.HOR_PART_SCHEME);
        FACTORY.createSizeCssMetaData (                        "-mfx-vertical-pixels-per-unit",    gc -> gc.verticalPixelsPerUnit,     Default.DEF_VERT_PPU);
        FACTORY.createSizeCssMetaData (                        "-mfx-horizontal-pixels-per-unit",  gc -> gc.horizontalPixelsPerUnit,   Default.DEF_HOR_PPU);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
        return FACTORY.getCssMetaData ();
    }

    /* +----------------------+ */
    /* | STYLEABLE PROPERTIES | */
    /* +----------------------+ */

    /* VERTICAL PARTITION SCHEME */
    private final StyleableObjectProperty<PartitionSchemes> verticalPartitionScheme =
        (StyleableObjectProperty<PartitionSchemes>) FACTORY.createStyleableEnumProperty (this, "verticalPartitionScheme", "-mfx-vertical-partition-scheme", gc -> gc.verticalPartitionScheme, PartitionSchemes.class, Default.VERT_PART_SCHEME);

    public PartitionSchemes getVerticalPartitionScheme () {
        return verticalPartitionScheme.get ();
    }

    public void setVerticalPartitionScheme (PartitionSchemes nScheme) {
        verticalPartitionScheme.set (nScheme == null ? Default.VERT_PART_SCHEME : nScheme);
    }

    public ReadOnlyObjectProperty<PartitionSchemes> verticalPartitionSchemeProperty () {
        return verticalPartitionScheme;
    }

    /* HORIZONTAL PARTITION SCHEME */
    private final StyleableObjectProperty<PartitionSchemes> horizontalPartitionScheme =
        (StyleableObjectProperty<PartitionSchemes>) FACTORY.createStyleableEnumProperty (this, "horizontalPartitionScheme", "-mfx-horizontal-partition-scheme", gc -> gc.horizontalPartitionScheme, PartitionSchemes.class, Default.HOR_PART_SCHEME);

    public PartitionSchemes getHorizontalPartitionScheme () {
        return horizontalPartitionScheme.get ();
    }

    public void setHorizontalPartitionScheme (PartitionSchemes nScheme) {
        horizontalPartitionScheme.set (nScheme == null ? Default.HOR_PART_SCHEME : nScheme);
    }

    public ReadOnlyObjectProperty<PartitionSchemes> horizontalPartitionSchemeProperty () {
        return horizontalPartitionScheme;
    }

    /* VERTICAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> verticalPixelsPerUnit =
        (StyleableObjectProperty<Number>) FACTORY.createStyleableNumberProperty (this, "verticalPixelsPerUnit", "-mfx-vertical-pixels-per-unit", gc -> gc.verticalPixelsPerUnit, Default.DEF_VERT_PPU);

    public Number getVerticalPixelsPerUnit () {
        return verticalPixelsPerUnit.get ();
    }

    public void setVerticalPixelsPerUnit (Number nVppu) {
        verticalPixelsPerUnit.set (nVppu == null ? Default.MIN_VERT_PPU : Float64.clamp (nVppu.doubleValue (), Default.MIN_VERT_PPU, Default.MAX_VERT_PPU));
    }

    public ReadOnlyObjectProperty<Number> verticalPixelsPerUnitProperty () {
        return verticalPixelsPerUnit;
    }

    /* HORIZONTAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> horizontalPixelsPerUnit =
        (StyleableObjectProperty<Number>) FACTORY.createStyleableNumberProperty (this, "horizontalPixelsPerUnit", "-mfx-horizontal-pixels-per-unit", gc -> gc.horizontalPixelsPerUnit, Default.DEF_HOR_PPU);

    public Number getHorizontalPixelsPerUnit () {
        return horizontalPixelsPerUnit.get ();
    }

    public void setHorizontalPixelsPerUnit (Number nHppu) {
        horizontalPixelsPerUnit.set (nHppu == null ? Default.MIN_HOR_PPU : Float64.clamp (nHppu.doubleValue (), Default.MIN_HOR_PPU, Default.MAX_HOR_PPU));
    }

    public ReadOnlyObjectProperty<Number> horizontalPixelsPerUnitProperty () {
        return horizontalPixelsPerUnit;
    }

    /* +------------------+ */
    /* | STANDARD MEMBERS | */
    /* +------------------+ */

    private final List<HorizontalGuide> hGuides = new ArrayList<> ();
    private final List<VerticalGuide> vGuides = new ArrayList<> ();

    private final DoubleProperty width  = new SimpleDoubleProperty ();
    private final DoubleProperty height = new SimpleDoubleProperty ();

    private final ObservableGroup.SimpleListener relayoutFn = this::relayout;

    private final ChangeListener<Parent> parentListener = (__obs, old, now) -> {
        if (now instanceof GraphView) {
            final GraphView gv = (GraphView) now;
            gv.getOrthoGroup ().add (relayoutFn);
        }
    };

    private final ObservableGroup<Object> parentGroup = ObservableGroup.observeParentSize (this, width, height);
    private final ObservableGroup<Object> styleGroup = new ObservableGroup<> (
        verticalPartitionScheme, horizontalPartitionScheme, verticalPixelsPerUnit, horizontalPixelsPerUnit);
    private final ObservableGroup<?> layoutGroup = new ObservableGroup<> (parentGroup, styleGroup);

    {
        parentProperty().addListener(parentListener);
    }

    public GuideContainer () {
        layoutGroup.add (relayoutFn);
    }

    @Override
    public void dispose () {
        parentGroup.dispose ();
        styleGroup.dispose ();
        layoutGroup.dispose ();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
        return getClassCssMetaData ();
    }

    private void relayout () {
        setWidth (width.get ());
        setHeight (height.get ());

        if (getParent() instanceof GraphView) {
            final GraphView gv = (GraphView) getParent ();
            final double left   = gv.getLeft (),
                         right  = gv.getRight (),
                         bottom = gv.getBottom (),
                         top    = gv.getTop ();
            final Parcel[] hParcels = getHorizontalPartitionScheme ().IMPL
                .partition (bottom, top, getHeight (), 0d, getVerticalPixelsPerUnit ().doubleValue ());
            if (hParcels.length > hGuides.size ()) {
                while (hGuides.size () < hParcels.length) {
                    final HorizontalGuide nGuide = new HorizontalGuide ();
                    hGuides.add (nGuide);
                    getChildren ().add (nGuide);
                }
            }
            for (int i = 0; i < hParcels.length; ++i) {
                final Parcel parcel = hParcels[i];
                final double ortho = gv.projectY (parcel.getPos ());
                final double parallel = gv.projectX (0d);
                final HorizontalGuide guide = hGuides.get (i);
                guide.setOrtho (ortho);
                guide.setParallel (parallel);
                guide.getStyleClass ().clear ();
                /* TODO: SET GUIDE TEXT */
                guide.getStyleClass ().add (TYPE_STYLE_CLASS_MAP.get (parcel.getType ()));
                guide.setVisible (true);
            }
            for (int i = hGuides.size () - 1; i >= hParcels.length; --i) {
                hGuides.get (i).setVisible (false);
            }



            final Parcel[] vParcels =  getVerticalPartitionScheme ().IMPL
                .partition (left, right, 0d, getWidth (), getHorizontalPixelsPerUnit ().doubleValue ());
            if (vParcels.length > vGuides.size ()) {
                while (vGuides.size () < vParcels.length) {
                    final VerticalGuide nGuide = new VerticalGuide ();
                    vGuides.add (nGuide);
                    getChildren ().add (nGuide);
                }
            }
            for (int i = 0; i < vParcels.length; ++i) {
                final Parcel parcel = vParcels[i];
                final double ortho = gv.projectX (parcel.getPos ());
                final double parallel = gv.projectY (0d);
                final VerticalGuide guide = vGuides.get (i);
                guide.setOrtho (ortho);
                guide.setParallel (parallel);
                guide.getStyleClass ().clear ();
                /* TODO: SET GUIDE TEXT */
                guide.getStyleClass ().add (TYPE_STYLE_CLASS_MAP.get (parcel.getType ()));
                guide.setVisible (true);
            }
            for (int i = vGuides.size () - 1; i >= vParcels.length; --i) {
                vGuides.get (i).setVisible (false);
            }
        }
    }
}
