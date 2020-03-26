package io.github.mkmax.util.math;

import org.joml.Matrix3dc;

public class Matrix3ext {

    /* +------------------------+ */
    /* | Different determinants | */
    /* +------------------------+ */

    /* "upper left" determinant */
    public static double detul (Matrix3dc mat) {
        return mat.m00 () * mat.m11 () - mat.m10 () * mat.m01 ();
    }

    /* "center left" determinant */
    public static double detcl (Matrix3dc mat) {
        return mat.m00 () * mat.m12 () - mat.m10 () * mat.m02 ();
    }

    /* "lower left" determinant */
    public double detll (Matrix3dc mat) {
        return mat.m01 () * mat.m12 () - mat.m11 () * mat.m02 ();
    }

    /* "upper right" determinant */
    public double detur (Matrix3dc mat) {
        return mat.m10 () * mat.m21 () - mat.m20 () * mat.m11 ();
    }

    /* "center right" determinant */
    public double detcr (Matrix3dc mat) {
        return mat.m10 () * mat.m22 () - mat.m20 () * mat.m12 ();
    }

    /* "lower right" determinant */
    public double detlr (Matrix3dc mat) {
        return mat.m11 () * mat.m22 () - mat.m21 () * mat.m12 ();
    }

}
