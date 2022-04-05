package com.project_1_2.group_16.physics;

public class Physics {

    /**
     * Mass of the ball (in kg).
     */
    public static final float MASS_BALL = 0.0459f;
    
    /**
     * Gravitational constant (in m per sec^2).
     */
    public static final float GRAV_CONSTANT = 9.81f;

    /**
     * Find the magnitude based on two coordinates,
     * for example in case of a vector it represents the length of the vector.
     * It is equal to vector magnitude or Pythagoras
     * @param x1 coordinate 1
     * @param x2 coordinate 2
     * @return length
     */
    public static float magnitude(float x1, float x2) {
        float x1_square = x1 * x1;
        float x2_square = x2 * x2;
        float sum = x1_square + x2_square;
        return (float) Math.sqrt(sum);
    }
}
