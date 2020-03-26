package io.github.mkmax.util.math;

import static io.github.mkmax.util.math.FloatingPoint.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.joml.Matrix3x2d;
import org.joml.Vector2d;

public class Quad2dvTest {

    @Test
    void testMap_0 () {
        Quad2dv qA = new Quad2dv (-1d,  1d, -1d,  1d);
        Quad2dv qB = new Quad2dv ( 0d,  2d,  0d,  2d);

        Matrix3x2d map  = qA.map (qB);
        Vector2d pointA = new Vector2d (-1d, -1d);
        Vector2d pointB = new Vector2d ( 1d, -1d);
        Vector2d pointC = new Vector2d ( 1d,  1d);
        Vector2d pointD = new Vector2d (-1d,  1d);
        Vector2d pointE = new Vector2d ( 0d,  0d);

        map.transformPosition (pointA);
        map.transformPosition (pointB);
        map.transformPosition (pointC);
        map.transformPosition (pointD);
        map.transformPosition (pointE);

        assertTrue (pointA.equals (new Vector2d (0d, 0d), F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (2d, 0d), F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (2d, 2d), F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (0d, 2d), F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d (1d, 1d), F64_EPSILON_STRICT));
    }

    @Test
    void testMap_1 () {
        Quad2dv qA = new Quad2dv (
            0d, -1d,
            1d,  0d,
            0d,  1d
        );
        Quad2dv qB = new Quad2dv (0d, 1d, 0d, 1d);

        Matrix3x2d map  = qA.map (qB);
        Vector2d pointA = new Vector2d ( 0d, -1d);
        Vector2d pointB = new Vector2d ( 1d,  0d);
        Vector2d pointC = new Vector2d ( 0d,  1d);
        Vector2d pointD = new Vector2d (-1d,  0d);
        Vector2d pointE = new Vector2d ( 0d,  0d);

        map.transformPosition (pointA);
        map.transformPosition (pointB);
        map.transformPosition (pointC);
        map.transformPosition (pointD);
        map.transformPosition (pointE);

        assertTrue (pointA.equals (new Vector2d (0d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector2d (1d, 0d),     F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector2d (1d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector2d (0d, 1d),     F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector2d (0.5d, 0.5d), F64_EPSILON_STRICT));
    }
}
