package com.project_1_2.group_16.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Tree;

/**
 * Class that represents the terrain of the golf course.
 */
public class Terrain {

    /**
     * The edge of the map.
     */
    public static final float MAP_EDGE = App.FIELD_SIZE / 2;
    
    /**
     * Collision detection for the terrain.
     */
    public static final Collision collision = new Collision();

    /**
     * Spline used for calculating height coordinates.
     */
    public static Spline spline;

    /**
     * Evaluates the height-formula for a set of coordinates. 
     * @param x x coordinate
     * @param y y coordinate
     * @return max(z-coordinate, -0.01)
     */
    public static float getHeight(float x, float y) {
        // make everything outside of the rendered area water
        if (Math.abs(x) > Terrain.MAP_EDGE || Math.abs(y) > Terrain.MAP_EDGE) {
            return -1;
        }

        return Math.max(spline.getHeight(x, y), -0.01f);
    }

    /**
     * Set the spline to be used as terrain.
     * @param heightFunction the height function of the terrain
     * @param input any custom input on the terrain
     * @return the new spline object
     */
    public static Spline setSpline(String heightFunction, float[][] input) {
        spline = new Spline(heightFunction, input);
        return spline;
    }
    
    /**
     * Return the slope in dh/dx and dh/dy using two-point centered point difference formula.
     * @param coordinates array of coordinates
     * @return array with dh/dx and dh/dy
     */
    public static float[] getSlope(float[] coordinates) {
        float h = 1f/1000000f;
        return new float[] {
            (getHeight(coordinates[0] + h, coordinates[1]) - getHeight(coordinates[0] - h, coordinates[1]))/(2*h),
            (getHeight(coordinates[0], coordinates[1]+h) - getHeight(coordinates[0], coordinates[1] - h))/(2*h)
        };
    }

    /**
     * Return the kinetic friction coefficient based on x,y location pulled from state vector.
     * @param sv StateVector
     * @return kinetic friction coefficient
     */
    public static float getKineticFriction(StateVector sv) {
        return collision.isInSandPit(sv.x, sv.y) ? Input.MUKS : Input.MUK;
    }
    
    /**
     * Return the static friction coefficient based on x,y location pulled from state vector.
     * @param sv StateVector
     * @return static friction coefficient
     */
    public static float getStaticFriction(StateVector sv) {
        return collision.isInSandPit(sv.x, sv.y) ? Input.MUSS : Input.MUS;
    }

    /**
     * Create a tree with a random position and size.
     * Position can't be on water (unless necessary), or within distance 1 of the ball or hole.
     */
    public static void createRandomTree() {
        Vector2 trV; float trX, trZ; int j = 0;
        do {
            j++;
            trX = 0.95f * (float)(Math.random() * App.FIELD_SIZE - App.FIELD_SIZE / 2);
            trZ = 0.95f* (float)(Math.random() * App.FIELD_SIZE - App.FIELD_SIZE / 2);
            trV = new Vector2(trX, trZ);
        } while ((j < 50 && Terrain.getHeight(trX, trZ) < 0.1) || trV.dst(Input.V0) < 1 || trV.dst(Input.VT) < 1);
        float trR = (float)(Math.random() * 0.2 + .2);
        Input.TREES.add(new Tree(trX, trZ, trR));
    }

    /**
     * Create a sandpit with a random position and size.
     * Position can't be on water (unless necessary), or within distance 2 of the ball or hole.
     */
    public static void createRandomSandpit() {
        Vector2 sV; float sX, sZ; int j = 0;
        do {
            j++;
            sX = 0.95f * (float)(Math.random() * App.FIELD_SIZE - App.FIELD_SIZE / 2);
            sZ = 0.95f * (float)(Math.random() * App.FIELD_SIZE - App.FIELD_SIZE / 2);
            sV = new Vector2(sX, sZ);
        } while ((j < 50 && getHeight(sX, sZ) < 0) || sV.dst(Input.V0) < 2 || sV.dst(Input.VT) < 2);
        Input.SAND.add(new Sandpit(sX, sZ, 1f + (float)(Math.random()*0.75)));
    }
}
