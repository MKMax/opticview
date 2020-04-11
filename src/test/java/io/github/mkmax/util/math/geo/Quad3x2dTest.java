package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.Float64.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.joml.Vector2d;

public class Quad3x2dTest {

    @Test
    void testMap_0 () {
        Quad3x2d qA = new Quad3x2d (
            -1d, -1d,
             1d, -1d,
             1d,  1d
        );
        Quad3x2d qB = new Quad3x2d (
            0d, 0d,
            2d, 0d,
            2d, 2d
        );

        Quad2dInterpolator interp = new Affine3Quad2dInterpolator (qA, qB);
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

        assertTrue (pointA.equals (new Vector2d (0d, 0d), F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (2d, 0d), F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (2d, 2d), F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (0d, 2d), F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d (1d, 1d), F64_EPSILON_STRICT));
    }

    @Test
    void testMap_1 () {
        Quad3x2d qA = new Quad3x2d (
            0d, -1d,
            1d,  0d,
            0d,  1d
        );

        Quad3x2d qB = new Quad3x2d (
            0d, 0d,
            1d, 0d,
            1d, 1d
        );

        Quad2dInterpolator interp = new Affine3Quad2dInterpolator (qA, qB);
        Vector2d pointA = new Vector2d ( 0d, -1d);
        Vector2d pointB = new Vector2d ( 1d,  0d);
        Vector2d pointC = new Vector2d ( 0d,  1d);
        Vector2d pointD = new Vector2d (-1d,  0d);
        Vector2d pointE = new Vector2d ( 0d,  0d);

        interp.map (pointA);
        interp.map (pointB);
        interp.map (pointC);
        interp.map (pointD);
        interp.map (pointE);

        assertTrue (pointA.equals (new Vector2d (0d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (1d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (1d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (0d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d (0.5d, 0.5d), F64_EPSILON_STRICT));
    }
}
