package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.ui.UIPos;
import io.github.mkmax.opticview.util.Numbers;
import io.github.mkmax.opticview.ui.OrthoRegion;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.*;

public final class GraphGrid extends OrthoRegion {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                       GRAPH GRID                                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /* +--------+ */
    /* | SPACER | */
    /* +--------+ */
    private final SimpleObjectProperty<GraphSpacer> spacer = new SimpleObjectProperty<> (GraphSpacers.DECIMAL);
    public ReadOnlyObjectProperty<GraphSpacer> spacerProperty ()
        { return spacer; }
    public GraphSpacer getSpacer ()
        { return spacer.get (); }
    public void setSpacer (GraphSpacer nspacer)
        { spacer.set (Objects.requireNonNull (nspacer, "Graph spacer must be specified")); }

    /* +--------+ */
    /* | MINGAP | */
    /* +--------+ */
    private final double
        MIN_MINGAP = 128d,
        MAX_MINGAP = 65536d;
    private final SimpleDoubleProperty mingap = new SimpleDoubleProperty (512d);
    public ReadOnlyDoubleProperty mingapProperty ()
        { return mingap; }
    public double getMingap ()
        { return mingap.get (); }
    public void setMingap (double nmingap)
        { mingap.set (Numbers.clamp (nmingap, MIN_MINGAP, MAX_MINGAP)); }

    /* +-------------------------+ */
    /* | DECIMAL FORMAT SETTINGS | */
    /* +-------------------------+ */
    private final DecimalFormat decformat = new DecimalFormat ();
    private final DecimalFormat sciformat = new DecimalFormat ();

    /* DECIMAL RANGE START */
    private final SimpleDoubleProperty decrangestart = new SimpleDoubleProperty (1e-3d);
    public DoubleProperty decimalRangeStartProperty ()
        { return decrangestart; }
    public double getDecimalRangeStart ()
        { return decrangestart.get (); }
    public void setDecimalRangeStart (double ndrs)
        { decrangestart.set (ndrs); }

    /* DECIMAL RANGE END */
    private final SimpleDoubleProperty decrangeend = new SimpleDoubleProperty (1e3d);
    public DoubleProperty decimalRangeEndProperty ()
        { return decrangeend; }
    public double getDecimalRangeEnd ()
        { return decrangeend.get (); }
    public void setDecimalRangeEnd (double ndre)
        { decrangeend.set (ndre); }

    /* DECIMAL PRECISION */
    private final int
        MIN_DECIMAL_PRECISION = 0,
        MAX_DECIMAL_PRECISION = 16;
    private final SimpleIntegerProperty decprecision = new SimpleIntegerProperty (3);
    public IntegerProperty decimalPrecisionProperty ()
        { return decprecision; }
    public int getDecimalPrecision ()
        { return decprecision.get (); }
    public void setDecimalPrecision (int ndecprecision)
        { decprecision.set (Numbers.clamp (ndecprecision, MIN_DECIMAL_PRECISION, MAX_DECIMAL_PRECISION)); }

    /* SCIENTIFIC PRECISION */
    private final int
        MIN_SCIENTIFIC_PRECISION = 0,
        MAX_SCIENTIFIC_PRECISION = 16;
    private final SimpleIntegerProperty sciprecision = new SimpleIntegerProperty (3);
    public IntegerProperty scientificPrecisionProperty ()
        { return sciprecision; }
    public int getScientificPrecision ()
        { return sciprecision.get (); }
    public void setScientificPrecision (int nsciprecision)
        { sciprecision.set (Numbers.clamp (nsciprecision, MIN_SCIENTIFIC_PRECISION, MAX_SCIENTIFIC_PRECISION)); }

    /* CONFIGURATORS */
    private void configureDecimalFormat () {
        final int decprec = getDecimalPrecision ();
        if (decprec > 0)
            decformat.applyPattern ("0." + "0".repeat (decprec));
        decformat.applyPattern ("0");
    }

    private void configureScientificFormat () {
        final int sciprec = getScientificPrecision ();
        if (sciprec > 0)
            sciformat.applyPattern ("0." + "0".repeat (sciprec) + "E0");
        sciformat.applyPattern ("0E0");
    }

    /* INITIALIZE FORMAT OBJECTS */
    {
        configureDecimalFormat ();
        configureScientificFormat ();
    }

    /* +------------------+ */
    /* | STYLE COLLECTION | */
    /* +------------------+ */
    private static final GuideStyle
        ORIGIN = new GuideStyle (UIPos.BOTTOM_CENTER),
        MAJOR  = new GuideStyle (UIPos.BOTTOM_CENTER),
        MINOR  = new GuideStyle (UIPos.BOTTOM_CENTER);
    /* @TODO(max): revise guide style collection later */
    /* indices are arranged with respect to GuideSpacer.StandardStyleHints */
    private final GuideStyle[] styles = { ORIGIN, MAJOR, MINOR };

    /* SETUP CORE GUIDE STYLES */
    {
        ORIGIN.setForeground (Color.rgb (255, 255, 255));
        ORIGIN.setBackground (Color.rgb (  0,   0,   0));
        MAJOR .setForeground (Color.rgb (255, 255, 255));
        MAJOR .setBackground (Color.rgb ( 64,  64,  64));
        MINOR .setForeground (Color.rgb (255, 255, 255));
        MINOR .setBackground (Color.rgb (128, 128, 128));
    }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */
    private final Pane linepane = new Pane ();
    private final Pane textpane = new Pane ();

    { getChildren ().addAll (linepane, textpane); }

    /* automatically generated and managed numeric guides */
    private final List<Guide> numhorguides = new ArrayList<> ();
    private final List<Guide> numverguides = new ArrayList<> ();

    private void recomputeHorizontalGuides () {
        final double
            width = getWidth (),
            begin = getLeft (),
            end   = getRight ();
        linepane.setPrefWidth (width);
        textpane.setPrefWidth (width);
        final Collection<GraphSpacer.Point> points = getSpacer ().computePoints (begin, end, width, getMingap ());
        int i = 0;
        final Iterator<GraphSpacer.Point> it = points.iterator ();
        while (it.hasNext ()) {
            final GraphSpacer.Point point = it.next ();
            final Guide guide;
            if (numhorguides.size () <= i)
                guide = new Guide (
                    styles[point.stylehint % styles.length],
                    Orientation.VERTICAL,
                    linepane,
                    textpane);
        }
    }

    private void recomputeVerticalGuides () {

    }

    {
        addHorizontalRemapListener (this::recomputeHorizontalGuides);
        addVerticalRemapListener (this::recomputeVerticalGuides);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                      GUIDE STYLE                                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final class GuideStyle {
        /* +----------------+ */
        /* | TEXT POSITION | */
        /* +----------------+ */
        private final SimpleObjectProperty<UIPos> textposition = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<UIPos> textPositionProperty ()
            { return textposition; }
        public UIPos getTextPosition ()
            { return textposition.get (); }
        public void setTextPosition (UIPos nlabelposition)
            { textposition.set (Objects.requireNonNull (nlabelposition, "A label position must be specified for a guide style"));}

        /* +------------+ */
        /* | FOREGROUND | */
        /* +------------+ */
        private final SimpleObjectProperty<Color> foreground = new SimpleObjectProperty<> ();
        public ObjectProperty<Color> foregroundProperty ()
            { return foreground; }
        public Color getForeground ()
            { return foreground.get (); }
        public Color getForegroundElse (Color def)
            { return foreground.get () == null ? def : foreground.get (); }
        public void setForeground (Color nforeground)
            { foreground.set (nforeground); }

        /* +------------+ */
        /* | BACKGROUND | */
        /* +------------+ */
        private final SimpleObjectProperty<Color> background = new SimpleObjectProperty<> ();
        public ObjectProperty<Color> backgroundProperty ()
            { return background; }
        public Color getBackground ()
            { return background.get (); }
        public Color getBackgroundElse (Color def)
            { return background.get () == null ? def : background.get (); }
        public void setBackground (Color nbackground)
            { background.set (nbackground); }

        /* +------+ */
        /* | FONT | */
        /* +------+ */
        private final SimpleObjectProperty<Font> font = new SimpleObjectProperty<> ();
        public ObjectProperty<Font> fontProperty ()
            { return font; }
        public Font getFont ()
            { return font.get (); }
        public Font getFontElse (Font def)
            { return font.get () == null ? def : font.get (); }
        public void setFont (Font nfont)
            { font.set (nfont); }

        /* +-----------------+ */
        /* | LINE VISIBILITY | */
        /* +-----------------+ */
        private final SimpleBooleanProperty linevisible = new SimpleBooleanProperty (true);
        public BooleanProperty lineVisibleProperty ()
            { return linevisible; }
        public boolean isLineVisible ()
            { return linevisible.get (); }
        public void setLineVisible (boolean nlinevisibility)
            { linevisible.set (nlinevisibility); }

        /* +--------------+ */
        /* | TEXT VISIBLE | */
        /* +--------------+ */
        private final SimpleBooleanProperty textvisible = new SimpleBooleanProperty (true);
        public BooleanProperty textVisibleProperty ()
            { return textvisible; }
        public boolean isTextVisible ()
            { return textvisible.get (); }
        public void setTextVisible (boolean nlabelvisible)
            { textvisible.set (nlabelvisible); }

        /* +----------------+ */
        /* | INITIALIZATION | */
        /* +----------------+ */
        public GuideStyle (UIPos textpos) {
            setTextPosition (textpos);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                         GUIDE                                             //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static class Guide {
        /* +-------+ */
        /* | STYLE | */
        /* +-------+ */
        private final SimpleObjectProperty<GuideStyle> style = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<GuideStyle> styleProperty ()
            { return style; }
        public GuideStyle getStyle ()
            { return style.get (); }
        public void setStyle (GuideStyle nstyle)
            { style.set (Objects.requireNonNull (nstyle, "A style must be specified for a guide"));}

        /* +-------------+ */
        /* | ORIENTATION | */
        /* +-------------+ */
        private final SimpleObjectProperty<Orientation> orientation = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<Orientation> orientationProperty ()
            { return orientation; }
        public Orientation getOrientation ()
            { return orientation.get (); }
        public void setOrientation (Orientation norient)
            { orientation.set (Objects.requireNonNull (norient, "An orientation must be specified for a guide")); }

        /* +--------------------------------+ */
        /* | LINE VISIBILITY (aux to style) | */
        /* +--------------------------------+ */
        private final SimpleBooleanProperty linevisible = new SimpleBooleanProperty (false);
        public BooleanProperty lineVisibleProperty ()
        { return linevisible; }
        public boolean isLineVisible ()
        { return linevisible.get (); }
        public void setLineVisible (boolean nlinevisibility)
        { linevisible.set (nlinevisibility); }

        /* +-----------------------------+ */
        /* | TEXT VISIBLE (aux to style) | */
        /* +-----------------------------+ */
        private final SimpleBooleanProperty textvisible = new SimpleBooleanProperty (false);
        public BooleanProperty textVisibleProperty ()
        { return textvisible; }
        public boolean isTextVisible ()
        { return textvisible.get (); }
        public void setTextVisible (boolean nlabelvisible)
        { textvisible.set (nlabelvisible); }

        /* FUSED VISIBILITY SETTER */
        public void setVisible (boolean val) {
            linevisible.set (val);
            textvisible.set (val);
        }

        /* +------+ */
        /* | TEXT | */
        /* +------+ */
        private final SimpleStringProperty text = new SimpleStringProperty ();
        public StringProperty textProperty ()
            { return text; }
        public String getText ()
            { return text.get (); }
        public void setText (String ncontent)
            { text.set (ncontent); }

        /* +------------+ */
        /* | POSITION X | */
        /* +------------+ */
        private final SimpleDoubleProperty x = new SimpleDoubleProperty ();
        public DoubleProperty xProperty ()
            { return x; }
        public double getX ()
            { return x.get (); }
        public void setX (double nx)
            { x.set (nx); }

        /* +------------+ */
        /* | POSITION Y | */
        /* +------------+ */
        private final SimpleDoubleProperty y = new SimpleDoubleProperty ();
        public DoubleProperty yProperty ()
            { return y; }
        public double getY ()
            { return y.get (); }
        public void setY (double ny)
            { y.set (ny); }

        /* +----------------+ */
        /* | LINE & CONTENT | */
        /* +----------------+ */
        private final Line uiline = new Line ();
        private final Text uitext = new Text ();
        private final Rectangle uitextbg = new Rectangle ();

        /* STYLE (GuideStyle) HANDLERS */
        private final ChangeListener<UIPos> style_labelposchangelistener = (__obs, __old, __now) ->
            layoutText ();
        private final ChangeListener<Color> style_fgchangelistener = (__obs, __old, __now) ->
            uitext.setStroke (getStyle ().getForeground ());
        private final ChangeListener<Color> style_bgchangelistener = (__obs, __old, __now) -> {
            uiline.setStroke (getStyle ().getBackground ());
            uitextbg.setFill (getStyle ().getBackground ());
        };
        private final ChangeListener<Font> style_fontchangelistener = (__obs, __old, __now) -> {
            uitext.setFont (getStyle ().getFont ());
            layoutText ();
        };
        private final ChangeListener<Boolean> style_linevischangelistener = (__obs, __old, __now) ->
            uiline.setVisible (linevisible.get () && getStyle ().isLineVisible ());
        private final ChangeListener<Boolean> style_textvischangelistener = (__obs, __old, __now) -> {
            uitext.setVisible (textvisible.get () && getStyle ().isTextVisible ());
            uitextbg.setVisible (textvisible.get () && getStyle ().isTextVisible ());
        };
        private final ChangeListener<GuideStyle> stylechangelistener = (__obs, old, now) -> {
            old.textposition .removeListener (style_labelposchangelistener);
            old.foreground   .removeListener (style_fgchangelistener);
            old.background   .removeListener (style_bgchangelistener);
            old.font         .removeListener (style_fontchangelistener);
            old.linevisible  .removeListener (style_linevischangelistener);
            old.textvisible  .removeListener (style_textvischangelistener);
            now.textposition .addListener (style_labelposchangelistener);
            now.foreground   .addListener (style_fgchangelistener);
            now.background   .addListener (style_bgchangelistener);
            now.font         .addListener (style_fontchangelistener);
            now.linevisible  .addListener (style_linevischangelistener);
            now.textvisible  .addListener (style_textvischangelistener);
        };

        {
            style.addListener (stylechangelistener);
            linevisible.addListener (style_linevischangelistener);
            textvisible.addListener (style_textvischangelistener);
        }

        /* LAYOUT LISTENERS */
        private final ChangeListener<Orientation> orientationchangelistener = (__obs, __old, __now) ->
            layoutLine ();
        private final ChangeListener<String> textchangelistener = (__obs, __old, __now) ->
            layoutText ();
        private final ChangeListener<Number> poschangelistener = (__obs, __old, __now) ->
            layout ();

        {
            orientation.addListener (orientationchangelistener);
            text.addListener (textchangelistener);
            x.addListener (poschangelistener);
            y.addListener (poschangelistener);
        }

        /* +-------------+ */
        /* | LINE PANELS | */
        /* +-------------+ */
        private final SimpleObjectProperty<Pane> linepane = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<Pane> linePaneProperty ()
            { return linepane; }
        public Pane getLinePane ()
            { return linepane.get (); }
        public void setLinePane (Pane nlinepane)
            { linepane.set (Objects.requireNonNull (nlinepane, "A valid line pane must be specified")); }

        /* +------------+ */
        /* | TEXT PANEL | */
        /* +------------+ */
        private final SimpleObjectProperty<Pane> textpane = new SimpleObjectProperty<> ();
        public ObjectProperty<Pane> textPaneProperty ()
            { return textpane; }
        public Pane getTextPane ()
            { return textpane.get (); }
        public void setTextPane (Pane ntextpane)
            { textpane.set (Objects.requireNonNull (ntextpane, "A valid text pane must be specified")); }

        /* +----------------+ */
        /* | PANE LISTENERS | */
        /* +----------------+ */
        private final ChangeListener<Number> linepanewidthlistener = (__obs, __old, __now) ->
            { if (getOrientation () == Orientation.HORIZONTAL) layoutLine (); };
        private final ChangeListener<Number> linepaneheightlistener = (__obs, __old, __now) ->
            { if (getOrientation () == Orientation.VERTICAL) layoutLine(); };
        private final ChangeListener<Pane> linepanechangelistener = (__obs, old, now) -> {
            old.getChildren ().remove (uiline);
            old.widthProperty ().removeListener (linepanewidthlistener);
            old.heightProperty ().removeListener (linepaneheightlistener);
            now.getChildren ().add (uiline);
            now.widthProperty ().addListener (linepanewidthlistener);
            now.heightProperty ().addListener (linepaneheightlistener);
        };
        private final ChangeListener<Pane> textpanechangelistener = (__obs, old, now) -> {
            old.getChildren ().removeAll (uitext, uitextbg);
            now.getChildren ().addAll (uitext, uitextbg);
        };

        {
            linepane.addListener (linepanechangelistener);
            textpane.addListener (textpanechangelistener);
        }

        /* +----------------+ */
        /* | INITIALIZATION | */
        /* +----------------+ */
        Guide (GuideStyle style, Orientation or, Pane linepane, Pane textpane) {
            setStyle (style);
            setOrientation (or);
            setLinePane (linepane);
            setTextPane (textpane);
        }

        /* +-----------------------+ */
        /* | LAYOUT IMPLEMENTATION | */
        /* +-----------------------+ */
        public void layout () {
            layoutLine ();
            layoutText ();
        }

        private void layoutLine () {
            double sx, sy, ex, ey;
            if (getOrientation () == Orientation.VERTICAL) {
                final double
                    x = getX (),
                    height = getLinePane ().getHeight ();
                sx = x;
                sy = 0d;
                ex = x;
                ey = height;
            }
            else {
                final double
                    y = getY (),
                    width = getLinePane ().getWidth ();
                sx = 0d;
                sy = y;
                ex = width;
                ey = y;
            }
            uiline.setStartX (sx);
            uiline.setStartY (sy);
            uiline.setEndX (ex);
            uiline.setEndY (ey);
        }

        private void layoutText () {
            final UIPos textpos = getStyle ().getTextPosition ();
            final double
                x = getX (),
                y = getY (),
                tw = uitext.getLayoutBounds ().getWidth (),
                th = uitext.getLayoutBounds ().getHeight ();
            final double
                tx = x + textpos.XMUL * tw,
                ty = y + textpos.YMUL * th;
            uitext.setLayoutX (tx);
            uitext.setLayoutY (ty);

            uitextbg.setLayoutX (tx);
            uitextbg.setLayoutY (ty);
            uitextbg.setWidth (tw);
            uitextbg.setHeight (th);
        }
    }
}
