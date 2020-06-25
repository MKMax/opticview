package io.github.mkmax.opticview;

import javafx.beans.value.ChangeListener;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GraphGrid extends OrthoRegion {

    private static class GuideStyle {

        public final Font  font;
        public final Color fg;
        public final Color bg;

        public GuideStyle (
            Font  pFont,
            Color pFg,
            Color pBg)
        {
            font = Objects.requireNonNull (pFont);
            fg = Objects.requireNonNull (pFg);
            bg = Objects.requireNonNull (pBg);
        }
    }

    private final class Guide {
        final Line line = new Line ();
        final Text text = new Text ();
        final Rectangle textbg = new Rectangle ();
    }

    /* grid settings */
    private static final DecimalFormat sciform = new DecimalFormat ("0.000E0");
    private static final DecimalFormat preform = new DecimalFormat ("0.000");
    private static final double SCI_THRESHOLD_MIN = 1e-3d;
    private static final double SCI_THRESHOLD_MAX = 1e3d;
    private static final double MIN_GUIDE_GAP = 256d;
    private static final double LINE_WIDTH = 1.0d;
    private static final GuideStyle ORIGIN_STYLE = new GuideStyle (
        Font.getDefault (),
        Color.rgb (224, 224, 224),
        Color.rgb ( 64,  64,  64));
    private static final GuideStyle MAJOR_STYLE = new GuideStyle (
        Font.getDefault (),
        Color.rgb ( 32,  32,  32),
        Color.rgb (192, 192, 192));
    private static final GuideStyle MINOR_STYLE = new GuideStyle (
        Font.getDefault (),
        Color.rgb ( 32,  32,  32),
        Color.rgb (224, 224, 224));

    /* grid content */
    private final Pane linePane = new Pane ();
    private final Pane textPane = new Pane ();

    private final List<Guide> horGuides = new ArrayList<> ();
    private final List<Guide> verGuides = new ArrayList<> ();

    private final ChangeListener<Number> maplistener = (__obs, __old, __now) -> update ();

    {
        widthProperty  ().addListener (maplistener);
        heightProperty ().addListener (maplistener);
        leftProperty   ().addListener (maplistener);
        rightProperty  ().addListener (maplistener);
        bottomProperty ().addListener (maplistener);
        topProperty    ().addListener (maplistener);
        getChildren ().addAll (linePane, textPane); /* order matters */
    }

    private void update () {
        final double
            width  = getWidth (),
            height = getHeight (),
            left   = getLeft (),
            right  = getRight (),
            bottom = getBottom (),
            top    = getTop ();
        final double
            hinterval = Math.abs (right - left),
            vinterval = Math.abs (top - bottom);
        final var hindices = computeGuideIndicesSp (width, left, right, MIN_GUIDE_GAP);
        final var vindices = computeGuideIndicesSp (height, bottom, top, MIN_GUIDE_GAP);

        /* ensure both panes are correct */
        linePane.setPrefWidth (width);
        linePane.setPrefHeight (height);

        textPane.setPrefWidth (width);
        textPane.setPrefHeight (height);

        /* ensure enough guides */
        while (hindices.size () > verGuides.size ())
        {
            Guide nguide = new Guide ();
            linePane.getChildren ().add (nguide.line);
            textPane.getChildren ().addAll (nguide.textbg, nguide.text);
            verGuides.add (nguide);
        }
        while (vindices.size () > horGuides.size ()) {
            Guide nguide = new Guide ();
            linePane.getChildren ().add (nguide.line);
            textPane.getChildren ().addAll (nguide.textbg, nguide.text);
            horGuides.add (nguide);
        }

        /* layout the vertical guides on the grid */
        for (int i = 0; i < hindices.size (); ++i) {
            Index idx = hindices.get (i);
            Guide guide = verGuides.get (i);

            final double x = mapx (idx.pos);
            final double y = mapy (0d);

            guide.line.setStrokeWidth (LINE_WIDTH);
            guide.line.setStartX (x);
            guide.line.setStartY (0d);
            guide.line.setEndX (x);
            guide.line.setEndY (getHeight ());
            guide.line.setStroke (idx.style.bg);
            guide.line.setVisible (true);

            guide.text.setFont (idx.style.font);
            guide.text.setText (
                hinterval <= SCI_THRESHOLD_MIN || SCI_THRESHOLD_MAX <= hinterval ?
                    sciform.format (idx.pos) :
                    preform.format (idx.pos));
            final double text_w = guide.text.getLayoutBounds ().getWidth ();
            final double text_h = guide.text.getLayoutBounds ().getHeight ();
            final double text_x = x - 0.5d * text_w;
            final double text_y = y;
            final double text_rx = Math.max (Math.min (text_x, width - text_w), 0d);
            final double text_ry = Math.max (Math.min (text_y, height - text_h), 0d);
            guide.text.setTextOrigin (VPos.TOP);
            guide.text.setTextAlignment (TextAlignment.LEFT);
            guide.text.setX (text_rx);
            guide.text.setY (text_ry);
            guide.text.setFill (idx.style.fg);
            guide.text.setVisible (true);

            guide.textbg.setX (text_rx);
            guide.textbg.setY (text_ry);
            guide.textbg.setWidth (text_w);
            guide.textbg.setHeight (text_h);
            guide.textbg.setFill (idx.style.bg);
            guide.textbg.setVisible (true);
        }
        for (int i = verGuides.size () - 1; i >= hindices.size (); --i) {
            Guide guide = verGuides.get (i);
            guide.line.setVisible (false);
            guide.text.setVisible (false);
            guide.textbg.setVisible (false);
        }

        /* layout the horizontal guides on the grid */
        for (int i = 0; i < vindices.size (); ++i) {
            Index idx = vindices.get (i);
            Guide guide = horGuides.get (i);

            final double x = mapx (0d);
            final double y = mapy (idx.pos);

            guide.line.setStrokeWidth (LINE_WIDTH);
            guide.line.setStartX (0d);
            guide.line.setStartY (y);
            guide.line.setEndX (getWidth ());
            guide.line.setEndY (y);
            guide.line.setStroke (idx.style.bg);
            guide.line.setVisible (true);

            guide.text.setText (
                vinterval <= SCI_THRESHOLD_MIN || SCI_THRESHOLD_MAX <= vinterval ?
                    sciform.format (idx.pos) :
                    preform.format (idx.pos));
            final double text_w = guide.text.getLayoutBounds ().getWidth ();
            final double text_h = guide.text.getLayoutBounds ().getHeight ();
            final double text_x = x - 0.5d * text_w;
            final double text_y = y;
            final double text_rx = Math.max (Math.min (text_x, width - text_w), 0d);
            final double text_ry = Math.max (Math.min (text_y, height - text_h), 0d);
            guide.text.setTextOrigin (VPos.TOP);
            guide.text.setTextAlignment (TextAlignment.LEFT);
            guide.text.setX (text_rx);
            guide.text.setY (text_ry);
            guide.text.setFill (idx.style.fg);
            guide.text.setVisible (true);

            guide.textbg.setX (text_rx);
            guide.textbg.setY (text_ry);
            guide.textbg.setWidth (text_w);
            guide.textbg.setHeight (text_h);
            guide.textbg.setFill (idx.style.bg);
            guide.textbg.setVisible (true);
        }
        for (int i = horGuides.size () - 1; i >= vindices.size (); --i) {
            Guide guide = horGuides.get (i);
            guide.line.setVisible (false);
            guide.text.setVisible (false);
            guide.textbg.setVisible (false);
        }
    }





    private static final class Index {

        public final GuideStyle style;
        public final double pos;

        public Index (GuideStyle pStyle, double pPos) {
            style = Objects.requireNonNull (pStyle);
            pos = pPos;
        }
    }

    private static List<Index> computeGuideIndicesSp (
        double span,
        double begin,
        double end,
        double mingap)
    {
        return computeGuideIndices (begin, end, span / (end - begin), mingap);
    }

    private static List<Index> computeGuideIndices (
        double begin,
        double end,
        double ratio,
        double mingap)
    {
        final double min = Math.min (begin, end);
        final double max = Math.max (begin, end);

        final double unit  = mingap / Math.abs (ratio);
        final double log10 = Math.ceil (Math.log10 (unit));
        final double pow10 = Math.pow (10, log10);
        final double norm  = unit / pow10;



        double majstep, minstep;
        if (norm <= 0.2d) {
            majstep = 0.2d * pow10;
            minstep = 0.25d * majstep;
        }
        else if (norm <= 0.5d) {
            majstep = 0.5d * pow10;
            minstep = 0.2d * majstep;
        }
        else {
            majstep = 1.0d * pow10;
            minstep = 0.2d * majstep;
        }



        double majstart, minstart;
        if (min < 0) {
            majstart = min - (min % majstep);
            minstart = min - (min % minstep);
        }
        else {
            majstart = min + (majstep - min % majstep);
            minstart = min + (minstep - min % minstep);
        }



        if (f64eq (min, majstart))
            majstart += majstep;
        if (f64eq (min, minstart))
            minstart += minstep;
        if (minstart >= max)
            return Collections.emptyList ();



        final int total = (int) Math.floor ((max - minstart) / minstep) + 1;
        final var indices = new ArrayList<Index> (total);
        int majpt = 0;
        for (int minpt = 0; minpt < total; ++minpt) {
            GuideStyle style = MINOR_STYLE;
            double minpos = minstart + minpt * minstep;
            double majpos = majstart + majpt * majstep;

            if (f64eq (minpos, majpos)) {
                style = MAJOR_STYLE;
                ++majpt;
            }

            if (f64eq (minpos, 0d))
                style = ORIGIN_STYLE;

            indices.add (new Index (style, minpos));
        }
        return indices;
    }

    private static boolean f64eq (double a, double b) {
        return Math.abs (a - b) <= 1e-15d;
    }
}
