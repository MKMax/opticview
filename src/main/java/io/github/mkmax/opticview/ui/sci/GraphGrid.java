package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.ui.UIPos;
import io.github.mkmax.opticview.util.Numbers;
import io.github.mkmax.opticview.ui.OrthoRegion;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
    public ReadOnlyIntegerProperty decimalPrecisionProperty ()
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
    public ReadOnlyIntegerProperty scientificPrecisionProperty ()
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

    private String toDecimalFormat (double value) {
        return decformat.format (value);
    }

    public String toScientificFormat (double value) {
        return sciformat.format (value);
    }

    /* INITIALIZE FORMAT OBJECTS */
    {
        configureDecimalFormat ();
        configureScientificFormat ();
    }

    /* +-----------------------+ */
    /* | APPEARANCE MANAGEMENT | */
    /* +-----------------------+ */
    private static final GuideAppearance ORIGIN = new GuideAppearance (
        UIPos.BOTTOM_CENTER,
        Color.rgb (255, 255, 255),
        Color.rgb ( 64,  64,  64));
    private static final GuideAppearance MAJOR = new GuideAppearance (
        UIPos.BOTTOM_CENTER,
        Color.rgb (255, 255, 255),
        Color.rgb (128, 128, 128));
    private static final GuideAppearance MINOR = new GuideAppearance (
        UIPos.BOTTOM_CENTER,
        Color.rgb (255, 255, 255),
        Color.rgb (224, 224, 224));
    private static final GuideAppearance[] APPEARANCES = { ORIGIN, MAJOR, MINOR };

    /* +------------------+ */
    /* | GUIDE MANAGEMENT | */
    /* +------------------+ */

    /* automatically computed & managed guides */
    private final List<Guide> nhorguides = new ArrayList<> ();
    private final List<Guide> nverguides = new ArrayList<> ();

    private void recomputeHorizontal () {
        final double
            width = getWidth (),
            left  = getLeft (),
            right = getRight ();
        final double span = Math.abs (right - left);
        final List<GraphSpacer.Point> points = getSpacer ().computePoints (left, right, width, getMingap ());
        final ListIterator<GraphSpacer.Point> it = points.listIterator ();
        while (it.hasNext ()) {
            final int i = it.nextIndex ();
            final GraphSpacer.Point point = it.next ();
            final GuideAppearance appearance = APPEARANCES[point.appearanceclass % APPEARANCES.length];
            final Guide guide;
            if (nverguides.size () == i) {
                guide = new Guide (appearance, Orientation.VERTICAL);
                nverguides.add (guide);
            }
            else {
                guide = nverguides.get (i);
                guide.setAppearance (appearance);
            }
            guide.setText (
                getDecimalRangeStart () <= span && span <= getDecimalRangeEnd () ?
                    toDecimalFormat (point.position) :
                    toScientificFormat (point.position));
            guide.setX (mapx (point.position));
            guide.setY (mapy (0d));
            guide.setLineVisible (true);
            guide.setTextVisible (true);
        }
        for (int i = nverguides.size () - 1; i >= points.size (); --i) {
            final Guide guide = nverguides.get (i);
            guide.setLineVisible (false);
            guide.setTextVisible (false);
        }
    }

    private void recomputeVertical () {
        final double
            height = getHeight (),
            bottom = getBottom (),
            top    = getTop ();
        final double span = Math.abs (top - bottom);
        final List<GraphSpacer.Point> points = getSpacer ().computePoints (bottom, top, height, getMingap ());
        final ListIterator<GraphSpacer.Point> it = points.listIterator ();
        while (it.hasNext ()) {
            final int i = it.nextIndex ();
            final GraphSpacer.Point point = it.next ();
            final GuideAppearance appearance = APPEARANCES[point.appearanceclass % APPEARANCES.length];
            final Guide guide;
            if (nhorguides.size () == i) {
                guide = new Guide (appearance, Orientation.HORIZONTAL);
                nhorguides.add (guide);
            }
            else {
                guide = nhorguides.get (i);
                guide.setAppearance (appearance);
            }
            guide.setText (
                getDecimalRangeStart () <= span && span <= getDecimalRangeEnd () ?
                    toDecimalFormat (point.position) :
                    toScientificFormat (point.position));
            guide.setX (mapx (0d));
            guide.setY (mapy (point.position));
            guide.setLineVisible (true);
            guide.setTextVisible (true);
        }
        for (int i = nhorguides.size () - 1; i >= points.size (); --i) {
            final Guide guide = nhorguides.get (i);
            guide.setLineVisible (false);
            guide.setTextVisible (false);
        }
    }

    {
        addHorizontalRemapListener (this::recomputeHorizontal);
        addVerticalRemapListener (this::recomputeVertical);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                   GRAPH APPEARANCE                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final class GuideAppearance {
        /* +------------------+ */
        /* | CONTENT POSITION | */
        /* +------------------+ */
        private final SimpleObjectProperty<UIPos> contentpos = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<UIPos> contentPositionProperty ()
            { return contentpos; }
        public UIPos getContentPosition ()
            { return contentpos.get (); }
        public void setContentPosition (UIPos ncontentpos)
            { contentpos.set (Objects.requireNonNull (ncontentpos, "A content position must be specified"));}

        /* +------------------+ */
        /* | FOREGROUND COLOR | */
        /* +------------------+ */
        private final SimpleObjectProperty<Color> foregroundcolor = new SimpleObjectProperty<> ();
        public ObjectProperty<Color> foregroundColorProperty ()
            { return foregroundcolor; }
        public Color getForegroundColor ()
            { return foregroundcolor.get (); }
        public void setForegroundColor (Color nfgcolor)
            { foregroundcolor.set (nfgcolor); }

        /* +------------------+ */
        /* | FOREGROUND COLOR | */
        /* +------------------+ */
        private final SimpleObjectProperty<Color> backgroundcolor = new SimpleObjectProperty<> ();
        public ObjectProperty<Color> backgroundColorProperty ()
            { return backgroundcolor; }
        public Color getBackgroundColor ()
            { return backgroundcolor.get (); }
        public void setBackgroundColor (Color nbgcolor)
            { backgroundcolor.set (nbgcolor); }

        /* +----------------+ */
        /* | INITIALIZATION | */
        /* +----------------+ */
        GuideAppearance (
            UIPos pcontentpos,
            Color pforegroundcolor,
            Color pbackgroundcolor)
        {
            setContentPosition (pcontentpos);
            setForegroundColor (pforegroundcolor);
            setBackgroundColor (pbackgroundcolor);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                       TEXT GUIDE                                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public final class Guide {
        /* +------------+ */
        /* | APPEARANCE | */
        /* +------------+ */
        private final SimpleObjectProperty<GuideAppearance> appearance = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<GuideAppearance> appearanceProperty ()
            { return appearance; }
        public GuideAppearance getAppearance ()
            { return appearance.get (); }
        public void setAppearance (GuideAppearance nappearance)
            { appearance.set (Objects.requireNonNull (nappearance, "An appearance must be specified")); }

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

        /* +-------------+ */
        /* | ORIENTATION | */
        /* +-------------+ */
        private final SimpleObjectProperty<Orientation> orientation = new SimpleObjectProperty<> ();
        public ReadOnlyObjectProperty<Orientation> orientationProperty ()
            { return orientation; }
        public Orientation getOrientation ()
            { return orientation.get (); }
        public void setOrientation (Orientation norientation)
            { orientation.set (Objects.requireNonNull (norientation, "An orientation must be specified")); }

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

        /* +------+ */
        /* | TEXT | */
        /* +------+ */
        private final SimpleStringProperty text = new SimpleStringProperty ();
        public StringProperty textProperty ()
            { return text; }
        public String getText ()
            { return text.get (); }
        public void setText (String ntext)
            { text.set (ntext); }

        /* +------------+ */
        /* | COMPONENTS | */
        /* +------------+ */
        private final Line uiline = new Line ();
        private final Text uitext = new Text ();
        private final Rectangle uitextbg = new Rectangle ();

        /* configure & add components to grid */
        {
            uiline.setViewOrder (0d);
            uitext.setViewOrder (1d);
            uitextbg.setViewOrder (1d);
            getChildren ().addAll (uiline, uitext, uitextbg);
        }

        /* appearance listeners */
        private final ChangeListener<Object> appearance_update = (__obs, __old, __now) -> {
            updateAppearance ();
            layout ();
        };
        private final ChangeListener<GuideAppearance> appearancelistener = (__obs, old, now) -> {
            if (old != null) {
                old.contentpos     .removeListener (appearance_update);
                old.foregroundcolor.removeListener (appearance_update);
                old.backgroundcolor.removeListener (appearance_update);
            }
            /* now should never be null */
            now.contentpos     .addListener (appearance_update);
            now.foregroundcolor.addListener (appearance_update);
            now.backgroundcolor.addListener (appearance_update);
            /* invoke to automatically change everything */
            appearance_update.changed (null, null, null);
        };

        /* layout listeners */
        private final ChangeListener<Orientation> orientationlistener = (__obs, __old, __now) ->
            layoutLine ();
        private final ChangeListener<Boolean> linevislistener = (__obs, __old, __now) ->
            uiline.setVisible (isLineVisible ());
        private final ChangeListener<Boolean> textvislistener = (__obs, __old, __now) -> {
            uitext.setVisible (isTextVisible ());
            uitextbg.setVisible (isTextVisible ());
        };
        private final ChangeListener<String> textlistener = (__obs, __old, __now) -> {
            uitext.setText (getText ());
            layoutText ();
        };
        private final ChangeListener<Number> positionlistener = (__obs, __old, __now) ->
            layout ();
        private final ChangeListener<Number> widthlistener = (__obs, __old, __now) ->
            { if (getOrientation () == Orientation.HORIZONTAL) layoutLine (); };
        private final ChangeListener<Number> heightlistener = (__obs, __old, __now) ->
            { if (getOrientation () == Orientation.VERTICAL) layoutLine(); };

        /* install listeners */
        {
            appearance.addListener (appearancelistener);
            orientation.addListener (orientationlistener);
            linevisible.addListener (linevislistener);
            textvisible.addListener (textvislistener);
            text.addListener (textlistener);
            x.addListener (positionlistener);
            y.addListener (positionlistener);
            widthProperty ().addListener (widthlistener);
            heightProperty ().addListener (heightlistener);
        }

        private void updateLineAppearance () {
            final GuideAppearance appearance = getAppearance ();
            uiline.setStroke (appearance.getBackgroundColor ());
        }

        private void updateTextAppearance () {
            final GuideAppearance appearance = getAppearance ();
            uitext.setStroke (appearance.getForegroundColor ());
            uitextbg.setFill (appearance.getBackgroundColor ());
        }

        private void updateAppearance () {
            updateLineAppearance ();
            updateTextAppearance ();
        }

        /* +----------------+ */
        /* | INITIALIZATION | */
        /* +----------------+ */
        Guide (GuideAppearance pappearance, Orientation porientation) {
            setAppearance (pappearance);
            setOrientation (porientation);
        }

        /* +--------+ */
        /* | LAYOUT | */
        /* +--------+ */
        public void layout () {
            layoutLine ();
            layoutText ();
        }

        private void layoutLine () {
            double sx, sy, ex, ey;
            if (getOrientation () == Orientation.VERTICAL) {
                final double
                    x = getX (),
                    height = getHeight ();
                sx = x;
                sy = 0d;
                ex = x;
                ey = height;
            }
            else {
                final double
                    y = getY (),
                    width = getWidth ();
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
            final UIPos textpos = getAppearance ().getContentPosition ();
            final double
                x = getX (),
                y = getY (),
                tw = uitext.getLayoutBounds ().getWidth (),
                th = uitext.getLayoutBounds ().getHeight ();
            final double
                tx = x + textpos.XMUL * tw,
                ty = y + textpos.YMUL * th;
            final double
                real_tx = Numbers.clamp (tx, 0d, getWidth () - tw),
                real_ty = Numbers.clamp (ty, 0d, getHeight () - th);
            uitext.setLayoutX (real_tx);
            uitext.setLayoutY (real_ty);

            uitextbg.setLayoutX (real_tx);
            uitextbg.setLayoutY (real_ty);
            uitextbg.setWidth (tw);
            uitextbg.setHeight (th);
        }
    }
}
