package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.FloatingPoint.F64_EPSILON_STRICT;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.joml.Vector2d;

public class Quad2x2dTest {

    @Test
    void testMap_0 () {
        Quad2x2d from = new Quad2x2d (
            -1d, -1d,
             1d,  1d
        );

        Quad2x2d to = new Quad2x2d (
              0d,   0d,
            128d, 128d
        );

        Quad2dInterpolator interp = new Affine2Quad2dInterpolator (from, to);
        Vector2d pointA = new Vector2d (-1d, -1d);
        Vector2d pointB = new Vector2d ( 1d, -1d);
        Vector2d pointC = new Vector2d ( 1d,  1d);
        Vector2d pointD = new Vector2d (-1d,  1d);
        Vector2d pointE = new Vector2d ( 0d,  0d);

        interp.map (pointA);
        interp.map (pointB);
        interp.map (pointC);
        interp.map (pointD);
        interp.map (pointE);

        assertTrue (pointA.equals (new Vector2d (  0d,   0d), F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (128d,   0d), F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (128d, 128d), F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (  0d, 128d), F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d ( 64d,  64d), F64_EPSILON_STRICT));

        interp.unmap (pointA);
        interp.unmap (pointB);
        interp.unmap (pointC);
        interp.unmap (pointD);
        interp.unmap (pointE);

        assertTrue (pointA.equals (new Vector2d (-1d, -1d), F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d ( 1d, -1d), F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d ( 1d,  1d), F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (-1d,  1d), F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d ( 0d,  0d), F64_EPSILON_STRICT));
    }
}
