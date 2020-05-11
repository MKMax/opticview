package io.github.mkmax.mathfx.graph;

import static io.github.mkmax.jim.JimStatics.*;

import io.github.mkmax.mathfx.Disposable;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * An component of a {@link CartesianView} that is responsible for providing
 * the grid interface that lays atop the graph among other features.
 * <p>
 * The grid includes the guiding lines for a selected partition scheme and
 * the labels identifying said lines. This particular grid also enables the
 * user to manipulate the graph window by directly and seamlessly modifying
 * the guide positions which will force the graph window's re-computation.
 *
 * @author Maxim Kasyanenko
 */
public class CartesianGrid extends CartesianView.Component {

    /**
     * Specifies the type of index emitted by a partitioner. A
     * {@link CartesianGrid} supports three types: Minor, Major,
     * and Origin indices which determine the appearance of the
     * grid when rendered onto the component.
     */
    public enum IndexType {
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

    /**
     * A specialized collection of indices. An index is calculated point
     * emitted by a {@link Partitioner} with a {@link IndexType} and
     * a floating point value position.
     * <p>
     * This structure specializes in the reuse of these indices to improve
     * performance when calculating the grid layout.
     *
     * @author Maxim Kasyanenko
     */
    public static final class IndexBuffer {

        private static final int INITIAL_SIZE = 16;

        private static final IndexType INITIAL_INDEX_TYPE = IndexType.MINOR;

        /* Both of these members will have the same length attribute, therefore
         * typeBuffer.length and posBuffer.length are synonymous within this
         * class.
         */
        private IndexType[] typeBuffer;
        private double   [] posBuffer;

        /* The pointer to the next available element to read or write. Both
         * operations use the same pointer.
         */
        private int pointer = 0;

        /**
         * Creates a new {@link IndexBuffer} with an initial size of
         * {@value #INITIAL_SIZE} entries, each with an index type of
         * {@link IndexType#MINOR}.
         */
        public IndexBuffer () {
            typeBuffer = new IndexType[INITIAL_SIZE];
            posBuffer  = new double   [INITIAL_SIZE];
            Arrays.fill (typeBuffer, INITIAL_INDEX_TYPE);
        }

        public void reset () {
            pointer = 0;
        }

        public void setType (IndexType type) {
            typeBuffer[pointer] = Objects.requireNonNull (type, "a type must be specified for an index buffer entry");
        }

        public IndexType getType () {
            return typeBuffer[pointer];
        }

        public void setPosition (double pos) {
            posBuffer[pointer] = pos;
        }

        public double getPosition () {
            return posBuffer[pointer];
        }

        public void pull () {
            if (pointer > 0) --pointer;
        }

        public void push () {
            if (pointer == typeBuffer.length) {
                int nSize = typeBuffer.length * 2;
                typeBuffer = Arrays.copyOf (typeBuffer, nSize);
                posBuffer  = Arrays.copyOf (posBuffer, nSize);
                Arrays.fill (typeBuffer, pointer, nSize, INITIAL_INDEX_TYPE);
            }
            ++pointer;
        }
    }

    /**
     * A collection of different partition implementations specifically designed
     * for the architecture of a {@link CartesianGrid}.
     *
     * @author Maxim Kasyanenko
     */
    public enum Partitioner {
        /**
         * This is the default partitioner that uses the standard partitioning convention
         * established by other graphing utilities. The partitioning scheme is based on
         * the <a href="https://www.desmos.com/calculator">desmos graphing utility</a>.
         */
        DECIMAL {
            /** @see Partitioner#partition(double, double, double, double, double) for more information*/
            @Override public List<Index> partition (
                double iBegin,
                double iEnd,
                double fBegin,
                double fEnd,
                double ppu)
            {
                /* TODO: comment this code later */
                final double iMin = Math.min (iBegin, iEnd);
                final double iMax = Math.max (iBegin, iEnd);

                final double fMin = Math.min (fBegin, fEnd);
                final double fMax = Math.max (fBegin, fEnd);



                final double appu  = (fMax - fMin) / (iMax - iMin);
                final double unit  = ppu / appu;
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
         * {@code partition(graph.getBottom(), graph.getTop(), io.getHeight(), 0d, 256d)}
         * can be invoked to compute the axis markings on the Y axis.
         *
         * @see Index for more information about this structure
         *
         * @param iBegin the interval space (arbitrary axis) starting point.
         * @param iEnd the interval space (arbitrary axis) ending point.
         * @param fBegin the fragment space (component/UI axis) starting point.
         * @param fEnd the fragment space (component/UI axis) ending point.
         * @param ppu The minimum pixels (fragments) per unit (partition).
         * @return A collection of emitted partition indices. This shall never be null.
         */
        public List<Index> partition (
            double iBegin,
            double iEnd,
            double fBegin,
            double fEnd,
            double ppu)
        {
            throw new IllegalStateException ("partitioner must implement the partition(...) function");
        }
    }

}
