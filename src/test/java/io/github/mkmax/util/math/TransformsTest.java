package io.github.mkmax.util.math;

import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransformsTest {

    static void assertVectorEquals (Vector3d vec, double x, double y, double z) {
        assertTrue (FloatingPoint.strictEq (vec.x, x));
        assertTrue (FloatingPoint.strictEq (vec.y, y));
        assertTrue (FloatingPoint.strictEq (vec.z, z));
    }

    @Test
    void testOffsetRotation2d_0 () {
        Matrix3d rotation = Matrix3ext.rotate2d (Math.PI / 2, 0d, 0d);
        Vector3d subject  = new Vector3d (1d, 1d, 1d);

        rotation.transform (subject);

        assertVectorEquals (subject, -1d, 1d, 1d);
    }

    @Test
    void testOffsetRotation2d_1 () {
        Matrix3d rotation   = Matrix3ext.rotate2d (Math.PI / 2, 1d, 1d);
        Vector3d stationary = new Vector3d (1d, 1d, 1d);
        Vector3d dynamic    = new Vector3d (2d, 2d, 1d);

        rotation.transform (stationary);
        rotation.transform (dynamic);

        assertVectorEquals (stationary, 1d, 1d, 1d);
        assertVectorEquals (dynamic, 0d, 2d, 1d);
    }
}
