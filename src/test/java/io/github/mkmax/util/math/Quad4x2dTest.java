package io.github.mkmax.util.math;

import static io.github.mkmax.util.math.FloatingPoint.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.mkmax.util.math.geo.Quad4x2d;
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

        Quad4x2d.Interpolator map = from.interpolate (to);
        Vector2d pointA = new Vector2d ( 0d, -1d);
        Vector2d pointB = new Vector2d ( 1d,  0d);
        Vector2d pointC = new Vector2d (-1d,  0d);
        Vector2d pointD = new Vector2d ( 0d,  1d);
        Vector2d pointE = new Vector2d ( 0d,  0d);

        map.transform (pointA);
        map.transform (pointB);
        map.transform (pointC);
        map.transform (pointD);
        map.transform (pointE);

        assertTrue(pointA.equals (new Vector2d (0d, 0d), F64_EPSILON_STRICT));
        assertTrue(pointB.equals (new Vector2d (2d, 0d), F64_EPSILON_STRICT));
        assertTrue(pointC.equals (new Vector2d (0d, 2d), F64_EPSILON_STRICT));
        assertTrue(pointD.equals (new Vector2d (2d, 2d), F64_EPSILON_STRICT));
        assertTrue(pointE.equals (new Vector2d (1d, 1d), F64_EPSILON_STRICT));
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

        Quad4x2d.Interpolator map = from.interpolate (to);
        Vector2d pointA = new Vector2d (1d, 1d);
        Vector2d pointB = new Vector2d (2d, 0d);
        Vector2d pointC = new Vector2d (1d, 2d);
        Vector2d pointD = new Vector2d (2d, 3d);
        Vector2d pointE = new Vector2d (1.5d, 1.5d);

        map.transform (pointA);
        map.transform (pointB);
        map.transform (pointC);
        map.transform (pointD);
        map.transform (pointE);

        assertTrue (pointA.equals (new Vector2d (0d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (1d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (0d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (1d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d (0.5d, 0.5d), F64_EPSILON_STRICT));
    }
}
