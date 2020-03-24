package io.github.mkmax.util.math;

import org.joml.Math;
import org.joml.Matrix3d;

public class JomlExtension {

    /* +------------------------+ */
    /* | Translation Extensions | */
    /* +------------------------+ */

    public static Matrix3d translate2d (double tx, double ty) {
        return translate2d (tx, ty, new Matrix3d ());
    }

    public static Matrix3d translate2d (double tx, double ty, Matrix3d dest) {
        return dest.set (
            1d, 0d, 0d,
            0d, 1d, 0d,
            tx, ty, 1d
        );
    }

    /* +---------------------+ */
    /* | Rotation Extensions | */
    /* +---------------------+ */

    public static Matrix3d rotate2d (double angle, double cx, double cy) {
        return rotate2d (angle, cx, cy, new Matrix3d ());
    }

    public static Matrix3d rotate2d (double angle, double cx, double cy, Matrix3d dest) {
        final double sin = Math.sin (angle);
        final double cos = Math.cos (angle);

        final double ax =  cos;
        final double ay =  sin;
        final double bx = -sin;
        final double by =  cos;

        final double zx = -(ax * cx + bx * cy) + cx;
        final double zy = -(ay * cx + by * cy) + cy;

        return dest.set (
            ax, ay, 0d,
            bx, by, 0d,
            zx, zy, 1d
        );
    }

}
