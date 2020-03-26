package io.github.mkmax.util.math;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.joml.Matrix3d;
import org.joml.Vector3d;

public class Quad6dTest {

    @Test
    void testMap_0 () {
        Quad6d qA = new Quad6d (
            new Vector3d (0d, 0d, 1d),
            new Vector3d (1d, 0d, 1d),
            new Vector3d (1d, 1d, 1d)
        );

        Quad6d qB = new Quad6d (
            new Vector3d (1d, 1d, 1d),
            new Vector3d (2d, 1d, 1d),
            new Vector3d (2d, 2d, 1d)
        );

        Matrix3d map    = qA.map (qB);
        Vector3d pointA = new Vector3d (0d, 0d, 1d);
        Vector3d pointB = new Vector3d (1d, 0d, 1d);
        Vector3d pointC = new Vector3d (1d, 1d, 1d);
        Vector3d pointD = new Vector3d (0.5d, 0.5d, 1d);
        Vector3d pointE = new Vector3d (0d, 1d, 1d);

        map.transform (pointA);
        map.transform (pointB);
        map.transform (pointC);
        map.transform (pointD);
        map.transform (pointE);

        assertTrue (pointA.equals (new Vector3d (1d, 1d, 1d), FloatingPoint.F64_EPSILON_STRICT));
        assertTrue (pointB.equals (new Vector3d (2d, 1d, 1d), FloatingPoint.F64_EPSILON_STRICT));
        assertTrue (pointC.equals (new Vector3d (2d, 2d, 1d), FloatingPoint.F64_EPSILON_STRICT));
        assertTrue (pointD.equals (new Vector3d (1.5d, 1.5d, 1d), FloatingPoint.F64_EPSILON_STRICT));
        assertTrue (pointE.equals (new Vector3d (1d, 2d, 1d), FloatingPoint.F64_EPSILON_STRICT));
    }

    @Test
    void testMap_1 () {
        Quad6d qA = new Quad6d (
            new Vector3d (0d, -1d, 1d),
            new Vector3d (1d,  0d, 1d),
            new Vector3d (0d,  1d, 1d)
        );

        Quad6d qB = new Quad6d (
            new Vector3d (0d, 0d, 1d),
            new Vector3d (1d, 0d, 1d),
            new Vector3d (1d, 1d, 1d)
        );

        Matrix3d map = qA.map (qB);
        Vector3d pointA = new Vector3d ( 0d, -1d, 1d);
        Vector3d pointB = new Vector3d ( 1d,  0d, 1d);
        Vector3d pointC = new Vector3d ( 0d,  1d, 1d);
        Vector3d pointD = new Vector3d ( 0d,  0d, 1d);
        Vector3d pointE = new Vector3d (-1d,  0d, 1d);

        System.out.println (map);

        map.transform (pointA);
        map.transform (pointB);
        map.transform (pointC);
        map.transform (pointD);
        map.transform (pointE);

        System.out.println (pointA);
        System.out.println (pointB);
        System.out.println (pointC);
        System.out.println (pointD);
        System.out.println (pointE);
    }
}
