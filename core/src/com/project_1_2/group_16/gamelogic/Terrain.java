package com.project_1_2.group_16.gamelogic;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.math.StateVector;

public class Terrain {

    public static final float kineticGrass = 0.05f;
    public static final float staticGrass = 0.25f;

    /**
     * Here the height method is defined that gives the height based on x,y coordinates.
     * @param x x coordinate
     * @param y y coordinate
     * @return height
     */
    public static float getHeight(float x, float y) {
        // make everything outside of the rendered area water
        if (Math.abs(x) > App.FIELD_SIZE / 2 + App.TILE_SIZE ||
            Math.abs(y) > App.FIELD_SIZE / 2 + App.TILE_SIZE) {
            return -1;
        }
        return (float)(0.5*(Math.sin((x-y)/7)+0.9));
    }
    
    /**
     * Return the slope in dh/dx and dh/dy.
     * @param coordinates array of coordinates
     * @param h step-size
     * @return array with dh/dx and dh/dy
     */
    public static float[] getSlope(float[] coordinates, float h) {
        float[] slopes = new float[2];
        slopes[0] = (getHeight(coordinates[0] + h, coordinates[1]) - getHeight(coordinates[0] - h, coordinates[1]))/(2*h);
        slopes[1] = (getHeight(coordinates[0], coordinates[1]+h) - getHeight(coordinates[0], coordinates[1] - h))/(2*h);
        return slopes;
    }

    /**
     * Return the kinetic friction coefficient based on x,y location pulled from state vector.
     * @param sv StateVector
     * @return kinetic friction coefficient
     */
    public static float getKineticFriction(StateVector sv) {
        // TODO add sandpits
        return kineticGrass;
    }
    
    /**
     * Return the static friction coefficient based on x,y location pulled from state vector.
     * @param sv StateVector
     * @return static friction coefficient
     */
    public static float getStaticFriction(StateVector sv) {
        // TODO add sandpits
        return staticGrass;
    }
}
