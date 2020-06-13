package io.github.mkmax.fx.math.graph.cartesian;

import static io.github.mkmax.jim.JimStatics.equal;
import io.github.mkmax.jim.OpenTuple2;
import io.github.mkmax.fx.scene.MappedRegion;

import io.github.mkmax.jim.OpenTuple3;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.*;

public class CartesianGrid extends MappedRegion {

    /* +-----------------+ */
    /* | COUPLED CLASSES | */
    /* +-----------------+ */

    public interface AxisPartitioner {
        Collection<OpenTuple2<String, Number>> partition (
            double axisIntervalBegin,
            double axisIntervalEnd,
            double pixelToIntervalRatio,
            double minPixelsPerTick);
    }

    public static final class CoreGuideStyleClasses {
        public static final String ORIGIN = "origin";
        public static final String MAJOR  = "major";
        public static final String MINOR  = "minor";
    }

    public static final class CoreAxisPartitioners {
        public static final AxisPartitioner DECIMAL = (begin, end, ratio, ppt) -> {
            /* TODO: comment this code later */
            final double min = Math.min (begin, end);
            final double max = Math.max (begin, end);

            final double unit = ppt / Math.abs (ratio);
            final double log10 = Math.ceil (Math.log10 (unit));
            final double pow10 = Math.pow (10, log10);
            final double norm = unit / pow10;

            double majstep, minstep;
            if (norm <= 0.2d) {
                majstep = 0.2d * pow10;
                minstep = 0.25d * majstep;
            } else if (norm <= 0.5d) {
                majstep = 0.5d * pow10;
                minstep = 0.2d * majstep;
            } else {
                majstep = 1.0d * pow10;
                minstep = 0.2d * majstep;
            }

            double majstart, minstart;
            if (min < 0) {
                majstart = min - (min % majstep);
                minstart = min - (min % minstep);
            } else {
                majstart = min + (majstep - min % majstep);
                minstart = min + (minstep - min % minstep);
            }

            if (equal (min, majstart))
                majstart += majstep;
            if (equal (min, minstart))
                minstart += minstep;
            if (minstart >= max)
                return Collections.emptyList ();

            /* TODO: refactor later for clarity and readability */
            final int total = (int) Math.floor ((max - minstart) / minstep) + 1;
            final var indices = new ArrayList<OpenTuple2<String, Number>> (total);
            int majpoint = 0;
            for (int minpoint = 0; minpoint < total; ++minpoint) {
                String style = CoreGuideStyleClasses.MINOR;
                double minpos = minstart + minpoint * minstep;
                double majpos = majstart + majpoint * majstep;

                if (equal (minpos, majpos)) {
                    style = CoreGuideStyleClasses.MAJOR;
                    ++majpoint;
                }

                if (equal (minpos, 0d))
                    style = CoreGuideStyleClasses.ORIGIN;

                indices.add (new OpenTuple2<> (style, minpos));
            }
            return indices;
        };

        public static final AxisPartitioner DEFAULT = DECIMAL;
    }

    /* +--------------------------------+ */
    /* | STYLESHEET METADATA/PROPERTIES | */
    /* +--------------------------------+ */

    private static final StyleablePropertyFactory<CartesianGrid>
        FACTORY = new StyleablePropertyFactory<> (MappedRegion.getClassCssMetaData ());

    /* +------------+ */
    /* | PROPERTIES | */
    /* +------------+ */

    /* HORIZONTAL AXIS PARTITIONER */
    private final ObjectProperty<AxisPartitioner>
        horizontalAxisPartitioner = new SimpleObjectProperty<> (CoreAxisPartitioners.DEFAULT);

    public AxisPartitioner getHorizontalAxisPartitioner () {
        return horizontalAxisPartitioner.get ();
    }

    /* VERTICAL AXIS PARTITIONER */
    private final ObjectProperty<AxisPartitioner>
        verticalAxisPartitioner = new SimpleObjectProperty<> (CoreAxisPartitioners.DEFAULT);

    public AxisPartitioner getVerticalAxisPartitioner () {
        return verticalAxisPartitioner.get ();
    }

    /* MIN PIXELS PER TICK */
    private final double MIN_PIXELS_PER_TICK = 64d;
    private final double MAX_PIXELS_PER_TICK = 65536d;
    private final double DEF_PIXELS_PER_TICK = 256d;
    private final DoubleProperty minPixelsPerTick = new SimpleDoubleProperty (DEF_PIXELS_PER_TICK);

    public double getMinPixelsPerTick () {
        return minPixelsPerTick.get ();
    }

    public void setMinPixelsPerTick (double nMinPixelsPerTick) {
        minPixelsPerTick.set (nMinPixelsPerTick);
    }

    /* +---------+ */
    /* | MEMBERS | */
    /* +---------+ */

    private final Pane lines = new Pane ();
    private final Pane labels = new Pane ();

    private final List<OpenTuple3<String, Line, Text>> horizontalGuides = new ArrayList<> ();
    private final List<OpenTuple3<String, Line, Text>> verticalGuides = new ArrayList<> ();

    private final Map<String, AxisPartitioner> axisPartitionerTable = new HashMap<> ();
    private final Map<String, String> axisStyleClassTable = new HashMap<> ();

    /* member initialization */
    {
        axisPartitionerTable.put ("decimal", CoreAxisPartitioners.DECIMAL);

        axisStyleClassTable.put ("origin", CoreGuideStyleClasses.ORIGIN);
        axisStyleClassTable.put ("major", CoreGuideStyleClasses.MAJOR);
        axisStyleClassTable.put ("minor", CoreGuideStyleClasses.MINOR);
    }

    /* +-------------------------------+ */
    /* | CONSTRUCTORS & INITIALIZATION | */
    /* +-------------------------------+ */

    public CartesianGrid (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        super (pLeft, pRight, pBottom, pTop);
    }

    public CartesianGrid (double pUniform) {
        super (pUniform);
    }

    public CartesianGrid () {
        super ();
    }

    /* constructor agnostic initialization */
    {
        super.addHorizontalListener (this::onHorizontalMappingUpdated);
        super.addVerticalListener (this::onVerticalMappingUpdated);
        getChildren ().addAll (lines, labels);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */

    private void onHorizontalMappingUpdated () {
        final double
            left  = getLeft (),
            right = getRight (),
            width = getWidth ();
        var indices = getHorizontalAxisPartitioner ().partition (
            left, right, width / (right - left), getMinPixelsPerTick ());
        while (verticalGuides.size () < indices.size ())
            verticalGuides.add (new OpenTuple3<> (null, null, null));
        /* Java's collection library is outdated garbage */
        int i = 0;
        for (OpenTuple2<String, Number> index : indices) {
            var guide = verticalGuides.get (i);
            if (guide._1 == null) {
                guide._1 = new Line ();
                lines.getChildren ().add (guide._1);
            }
            if (guide._2 == null) {
                guide._2 = new Text ();
                labels.getChildren ().add (guide._2);
            }
            /* modify the style of the line: */
            guide._1.getStyleClass ().add (index._0);
            guide._1.getStyleClass ().remove (guide._0);

            /* modify the style of the label: */
            guide._2.getStyleClass ().add (index._0);
            guide._2.getStyleClass ().remove (guide._0);

            /* TODO: Position the line and label */

            /* replace the style class reference: */
            guide._0 = index._0;
            ++i;
        }
    }

    public void onVerticalMappingUpdated () {

    }
}
