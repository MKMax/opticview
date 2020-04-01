package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.FloatingPoint.F64_EPSILON_STRICT;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.joml.Vector2d;

public class Quad4x2dTest {

    @Test
    void testMapNormal_0 () {
        Quad4x2d from = new Quad4x2d (
             0d, -1d,
             1d,  0d,
            -1d,  0d,
             0d,  1d
        );

        Quad4x2d to = new Quad4x2d (
            0d, 0d,
            2d, 0d,
            0d, 2d,
            2d, 2d
        );

        Quad2dInterpolator interp = new BezierQuad2dInterpolator (from, to);
        Vector2d pointA = new Vector2d ( 0d, -1d);
        Vector2d pointB = new Vector2d ( 1d,  0d);
        Vector2d pointC = new Vector2d (-1d,  0d);
        Vector2d pointD = new Vector2d ( 0d,  1d);
        Vector2d pointE = new Vector2d ( 0d,  0d);

        interp.map (pointA);
        interp.map (pointB);
        interp.map (pointC);
        interp.map (pointD);
        interp.map (pointE);

        assertTrue(pointA.equals (new Vector2d (0d, 0d), F64_EPSILON_STRICT));
        assertTrue(pointB.equals (new Vector2d (2d, 0d), F64_EPSILON_STRICT));
        assertTrue(pointC.equals (new Vector2d (0d, 2d), F64_EPSILON_STRICT));
        assertTrue(pointD.equals (new Vector2d (2d, 2d), F64_EPSILON_STRICT));
        assertTrue(pointE.equals (new Vector2d (1d, 1d), F64_EPSILON_STRICT));

        interp.unmap (pointA);
        interp.unmap (pointB);
        interp.unmap (pointC);
        interp.unmap (pointD);
        interp.unmap (pointE);

        assertTrue (pointA.equals (new Vector2d ( 0d, -1d), F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d ( 1d,  0d), F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (-1d,  0d), F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d ( 0d,  1d), F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d ( 0d,  0d), F64_EPSILON_STRICT));
    }

    @Test
    void testMapNormal_1 () {
        Quad4x2d from = new Quad4x2d (
            1d, 1d,
            2d, 0d,
            1d, 2d,
            2d, 3d
        );

        Quad4x2d to = new Quad4x2d (
            0d, 0d,
            1d, 0d,
            0d, 1d,
            1d, 1d
        );

        Quad2dInterpolator interp = new BezierQuad2dInterpolator (from, to);
        Vector2d pointA = new Vector2d (1d, 1d);
        Vector2d pointB = new Vector2d (2d, 0d);
        Vector2d pointC = new Vector2d (1d, 2d);
        Vector2d pointD = new Vector2d (2d, 3d);
        Vector2d pointE = new Vector2d (1.5d, 1.5d);

        interp.map (pointA);
        interp.map (pointB);
        interp.map (pointC);
        interp.map (pointD);
        interp.map (pointE);

        assertTrue (pointA.equals (new Vector2d (0d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (1d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (0d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (1d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d (0.5d, 0.5d), F64_EPSILON_STRICT));

        interp.unmap (pointA);
        interp.unmap (pointB);
        interp.unmap (pointC);
        interp.unmap (pointD);
        interp.unmap (pointE);

        assertTrue (pointA.equals (new Vector2d (1d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (2d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (1d, 2d),     F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (2d, 3d),     F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d (1.5d, 1.5d), F64_EPSILON_STRICT));
    }
}
