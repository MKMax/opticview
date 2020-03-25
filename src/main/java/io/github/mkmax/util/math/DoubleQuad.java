package io.github.mkmax.util.math;

import org.joml.Matrix3d;

public class DoubleQuad {

    /* The quad is represented as 3 position vectors in triangle form using a 3x3 matrix. */
    private Matrix3d quad;

    public DoubleQuad () {
        quad = new Matrix3d (
            -1d, -1d, 1d,
             1d, -1d, 1d,
             0d,  1d, 1d
        );
    }

}
