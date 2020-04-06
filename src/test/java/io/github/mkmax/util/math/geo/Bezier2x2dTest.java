package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.Floats.F64_EPSILON_STRICT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.joml.Vector2d;

public class Bezier2x2dTest {

    @Test
    void testIntersect_0 () {
        Bezier2x2d AB = new Bezier2x2d (-1d, -1d,  1d,  1d);
        Bezier2x2d CD = new Bezier2x2d (-1d,  1d,  1d, -1d);

        Vector2d intersection = new Vector2d ();
        assertTrue (AB.intersect (CD, intersection));
        assertTrue (intersection.equals (new Vector2d (0d, 0d), F64_EPSILON_STRICT));
    }

    @Test
    void testIntersect_1 () {
        Bezier2x2d AB = new Bezier2x2d (-1d,  0d,  1d,  0d);
        Bezier2x2d CD = new Bezier2x2d ( 0d, -1d,  0d,  1d);

        Vector2d intersection = new Vector2d ();
        assertTrue (AB.intersect (CD, intersection));
        assertTrue (intersection.equals (new Vector2d (0d, 0d), F64_EPSILON_STRICT));
    }

    @Test
    void testIntersect_2 () {
        Bezier2x2d AB = new Bezier2x2d (-1d, -1d,  1d, -1d);
        Bezier2x2d CD = new Bezier2x2d (-1d,  1d,  1d,  1d);

        Vector2d intersection = new Vector2d (0d, 0d);
        assertFalse (AB.intersect (CD, intersection));
        assertTrue (intersection.equals (new Vector2d (0d, 0d), F64_EPSILON_STRICT));
    }
}
