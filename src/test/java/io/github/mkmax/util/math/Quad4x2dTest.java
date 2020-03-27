package io.github.mkmax.util.math;

import org.joml.Vector2d;
import org.junit.jupiter.api.Test;

public class Quad4x2dTest {

    @Test
    void testInterpolate_0 () {
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

        map.transform (pointA);
        map.transform (pointB);
        map.transform (pointC);
        map.transform (pointD);

        System.out.println (pointA);
        System.out.println (pointB);
        System.out.println (pointC);
        System.out.println (pointD);
    }
}
