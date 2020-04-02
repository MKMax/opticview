package io.github.mkmax.fxe.math.graph.cartesian.axes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.mkmax.util.data.Iteration;
import io.github.mkmax.util.math.FloatingPoint;
import org.junit.jupiter.api.Test;
import java.util.List;


public class DecimalAxisMarkerTest {

    @Test
    void testGetMarks_0 () {
        DecimalAxisMarkerd dac = new DecimalAxisMarkerd ();

        final List<AxisMarkerd.Markd> marksA = Iteration.toArrayList (
            dac.computeMajorMarks (-2.5d, 2.5d));

        assertEquals (5, marksA.size ());
        assertTrue (FloatingPoint.strictEq (-2d, marksA.get (0).getPosition ()));
        assertTrue (FloatingPoint.strictEq (-1d, marksA.get (1).getPosition ()));
        assertTrue (FloatingPoint.strictEq ( 0d, marksA.get (2).getPosition ()));
        assertTrue (FloatingPoint.strictEq ( 1d, marksA.get (3).getPosition ()));
        assertTrue (FloatingPoint.strictEq ( 2d, marksA.get (4).getPosition ()));

        dac.setPartitionLimit (2);

        final List<AxisMarkerd.Markd> marksB = Iteration.toArrayList (
            dac.computeMajorMarks (-2.5d, 2.5d));

        assertEquals (1, marksB.size ());
        assertTrue (FloatingPoint.strictEq (0d, marksB.get (0).getPosition ()));
    }
}
