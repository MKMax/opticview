package io.github.mkmax.specfx.math.graph.cartesian;

import static io.github.mkmax.jim.JimStatics.*;
import io.github.mkmax.jim.Disposable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

import java.util.*;

/**
 * An component of a {@link CartesianView2D} that is responsible for providing
 * the grid interface that lays atop the graph among other features.
 * <p>
 * The grid includes the guiding lines for a selected partition scheme and
 * the labels identifying said lines. This particular grid also enables the
 * user to manipulate the graph window by directly and seamlessly modifying
 * the guide positions which will force the graph window's re-computation.
 *
 * @author Maxim Kasyanenko
 */
public final class CartesianGrid2D extends MappedPane2D implements Disposable {

    /**
     * A utility structure used by the {@link PartitionScheme} to report a
     * collection of partition marks made on a certain axis which will
     * be used to render grid lines and labels.
     *
     * @author Maxim Kasyanenko
     */
    public static final class Index {

        /**
         * Specifies the type of index emitted. A {@link CartesianGrid2D}
         * supports three types: Minor, Major, and Origin indices which
         * determine the appearance of the grid line and label when rendered
         * onto the component.
         */
        public enum Type {
            /**
             * A minor index is the most common mark, and it is used to
             * represent the smallest logical partition unit currently
             * displayed on the grid.
             */
            MINOR,

            /**
             * A major index is used to represent a macro partition to better
             * aid visibility when a user is analyzing a graph.
             */
            MAJOR,

            /**
             * An origin index is used to represent the two X & Y axes on the
             * grid. This is used to aid in the visibility of the graph and
             * assist the user in orientation when traversing it.
             */
            ORIGIN
        }

        /* We opt to simply make the two members public and final because
         * it is redundant to provide getters/setters for objects of disposable
         * lifespan. This class in general could be substituted by records provided
         * in Java 14.
         */
        public final Type   type;

        /* It should be noted that this position represents the graph space
         * position, NOT the component position. In other words, this value should
         * first be transformed to obtain the actual position on the UI component.
         */
        public final double pos;

        /**
         * Creates a new {@link Index} given the type and position.
         *
         * @param pType the type of index that this is.
         * @param pPos the position of this index in graph space.
         */
        public Index (Type pType, double pPos) {
            type = Objects.requireNonNull (pType, "the partition type must be specified");
            pos  = pPos;
        }
    }
    /**
     * A collection of different partition implementations specifically designed
     * for the architecture of a {@link CartesianGrid2D}.
     *
     * @author Maxim Kasyanenko
     */
    public enum PartitionScheme {
        /**
         * This is the default partitioner that uses the standard partitioning convention
         * established by other graphing utilities. The partitioning scheme is based on
         * the <a href="https://www.desmos.com/calculator">desmos graphing utility</a>.
         */
        DECIMAL {
            /** @see PartitionScheme#partition(double, double, double, double, double) for more information */
            @Override public List<Index> partition (
                double iBegin,
                double iEnd,
                double fBegin,
                double fEnd,
                double ppt)
            {
                /* TODO: comment this code later */
                final double iMin = Math.min (iBegin, iEnd);
                final double iMax = Math.max (iBegin, iEnd);

                final double fMin = Math.min (fBegin, fEnd);
                final double fMax = Math.max (fBegin, fEnd);



                final double appu  = (fMax - fMin) / (iMax - iMin);
                final double unit  = ppt / appu;
                final double log10 = Math.ceil (Math.log10 (unit));
                final double pow10 = Math.pow (10, log10);
                final double norm  = unit / pow10;



                double majorStep, minorStep;

                if (norm <= 0.2d) {
                    majorStep = 0.2d * pow10;
                    minorStep = 0.25d * majorStep;
                }
                else if (norm <= 0.5d) {
                    majorStep = 0.5d * pow10;
                    minorStep = 0.2d * majorStep;
                }
                else {
                    majorStep = 1.0d * pow10;
                    minorStep = 0.2d * majorStep;
                }



                double majorStart, minorStart;

                if (iMin < 0) {
                    majorStart = iMin - (iMin % majorStep);
                    minorStart = iMin - (iMin % minorStep);
                }
                else {
                    majorStart = iMin + (majorStep - iMin % majorStep);
                    minorStart = iMin + (minorStep - iMin % minorStep);
                }



                if (equal (iMin, majorStart))
                    majorStart += majorStep;
                if (equal (iMin, minorStart))
                    minorStart += minorStep;

                if (minorStart >= iMax)
                    return Collections.emptyList ();



                final int total = (int) Math.floor ((iMax - minorStart) / minorStep) + 1;
                final List<Index> indices = new ArrayList<> (total);

                int maj = 0;
                for (int min = 0; min < total; ++min) {
                    Index.Type type = Index.Type.MINOR;
                    double minpos = minorStart + min * minorStep;
                    double majpos = majorStart + maj * majorStep;

                    if (equal (minpos, majpos)) {
                        type = Index.Type.MAJOR;
                        ++maj;
                    }

                    if (equal (minpos, 0d))
                        type = Index.Type.ORIGIN;

                    indices.add (new Index (type, minpos));
                }

                return indices;
            }
        };

        /**
         * Given the interval of some arbitrary axis and component space axis,
         * this function shall compute the indices located on the interval space
         * (arbitrary axis).
         * <p>
         * To give a bit of context, interval space and fragment space are
         * "historical" terms that referred to the graph's window space and the
         * UI/pixel space (analogous to OpenGL fragments) respectively. The method
         * {@code partition(graph.getLeft(), graph.getRight(), 0d, ui.getWidth(), 256d)}
         * could be invoked to compute the indices on the interval [graph.getLeft(), graph.getRight()]
         * which would map to [0d, ui.getWidth()] with a minimum of 256 pixels for
         * each partition. In other words, this would generate the axis markings on
         * the X axis of the graph. Similarly,
         * {@code partition(graph.getBottom(), graph.getTop(), ui.getHeight(), 0d, 256d)}
         * can be invoked to compute the axis markings on the Y axis.
         *
         * @see Index for more information about this structure
         *
         * @param iBegin the interval space (arbitrary axis) starting point.
         * @param iEnd the interval space (arbitrary axis) ending point.
         * @param fBegin the fragment space (component/UI axis) starting point.
         * @param fEnd the fragment space (component/UI axis) ending point.
         * @param ppt The minimum pixels (fragments) per tick (partition).
         * @return A collection of emitted partition indices. This shall never be null.
         */
        public List<Index> partition (
            double iBegin,
            double iEnd,
            double fBegin,
            double fEnd,
            double ppt)
        {
            throw new IllegalStateException ("partition scheme must implement the partition(...) method");
        }
    }

    /* +------------+ */
    /* | PROPERTIES | */
    /* +------------+ */

    private static final double
        MIN_PPT = 64d,
        MAX_PPT = 65536d,
        DEF_PPT = 256d;

    private static final PartitionScheme DEF_PARTITION_SCHEME = PartitionScheme.DECIMAL;

    /* HORIZONTAL PIXELS PER TICK (HPPT) */
    private final ObjectProperty<Number> hppt = new SimpleObjectProperty<> (DEF_PPT);

    public double getHorizontalPixelsPerTick () {
        return hppt.get () == null ? DEF_PPT : clamp (hppt.get ().doubleValue (), MIN_PPT, MAX_PPT);
    }

    public void setHorizontalPixelsPerTick (double nHppt) {
        hppt.set (clamp (nHppt, MIN_PPT, MAX_PPT));
    }

    /* VERTICAL PIXELS PER TICK (VPPT) */
    private final ObjectProperty<Number> vppt = new SimpleObjectProperty<> (DEF_PPT);

    public double getVerticalPixelsPerTick () {
        return vppt.get () == null ? DEF_PPT : clamp (vppt.get ().doubleValue (), MIN_PPT, MAX_PPT);
    }

    public void setVerticalPixelsPerUnit (double nVppt) {
        vppt.set (clamp (nVppt, MIN_PPT, MAX_PPT));
    }

    /* HORIZONTAL PARTITION SCHEME */
    private final ObjectProperty<PartitionScheme> horizontalPartitionScheme = new SimpleObjectProperty<> (DEF_PARTITION_SCHEME);

    public PartitionScheme getHorizontalPartitionScheme () {
        return horizontalPartitionScheme.get () == null ?
            DEF_PARTITION_SCHEME : horizontalPartitionScheme.get ();
    }

    public void setHorizontalPartitionScheme (PartitionScheme nScheme) {
        horizontalPartitionScheme.set (nScheme == null ? DEF_PARTITION_SCHEME : nScheme);
    }

    /* VERTICAL PARTITION SCHEME */
    private final ObjectProperty<PartitionScheme> verticalPartitionScheme = new SimpleDoubleProperty (DEF_PARTITION_SCHEME);

    public PartitionScheme getVerticalPartitionScheme () {
        return verticalPartitionScheme.get () == null ?
            DEF_PARTITION_SCHEME : verticalPartitionScheme.get ();
    }

    public void setVerticalPartitionScheme (PartitionScheme nScheme) {
        verticalPartitionScheme.set (nScheme == null ? DEF_PARTITION_SCHEME : nScheme);
    }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */

    private final Pane lineContainer  = new Pane ();
    private final Pane labelContainer = new Pane ();

    {

    }

    /**
     * Creates a new instance of a {@link CartesianGrid2D} that may only
     * be invoked by a class within the {@link io.github.mkmax.specfx.math.graph.cartesian.two}
     * package. Usually, the class that instantiates this grid, is the
     * {@link CartesianView2D} itself.
     */
    CartesianGrid2D () {
        addUpdateListener (this::update);
    }

    /* +--------------+ */
    /* | LISTENERS/UI | */
    /* +--------------+ */

    private void update () {
        final List<Index> hIndices = getHorizontalPartitionScheme ().partition (
            getLeft (), getRight (), 0d, getWidth (), getHorizontalPixelsPerTick ());
        final List<Index> vIndices = getVerticalPartitionScheme ().partition (
            getBottom (), getTop (), getHeight (), 0d, getVerticalPixelsPerTick ());

    }
}