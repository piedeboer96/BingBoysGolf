package com.project_1_2.group_16.gamelogic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Tree;

/**
 * Class that represents the terrain of the golf course.
 */
public class Terrain {

    /**
     * The edge of the map.
     */
    public static final float MAP_EDGE = App.FIELD_SIZE / 2 + App.TILE_SIZE;
    
    /**
     * All sandpits.
     */
    public static final List<Sandpit> sandPits = new ArrayList<Sandpit>();

    /**
     * All trees.
     */
    public static final List<Tree> trees = new ArrayList<Tree>();
    
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
     * Add sandpits to the course.
     */
    public static void initSandPits() {
        Vector2 sV; float sX, sZ; int j;
        for (int i = 0; i < Input.SAND; i++) {
            j = 0;
            do {
                sX = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
                sZ = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
                sV = new Vector2(sX, sZ);
            } while ((j < 50 && getHeight(sX, sZ) < 0) || sV.dst(Input.V0) < 2 || sV.dst(Input.VT) < 2);
            sandPits.add(new Sandpit(sX, sZ, 1f));
            sandPits.add(new Sandpit(sX + (float)Math.random() * 1f - 0.5f, sZ + (float)Math.random() * 1f - 0.5f, 1f));
            sandPits.add(new Sandpit(sX + (float)Math.random() * 1f - 0.5f, sZ + (float)Math.random() * 1f - 0.5f, 1f));
        }
    }

    /**
     * Add trees to the course.
     * @param model tree model
     */
    public static void initTrees(Model model) {
        Vector2 trV; float trX, trZ; int j = 0;
		for (int i = 0; i < Input.TREES; i++) {
            j = 0;
			do {
                j++;
				trX = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
				trZ = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
				trV = new Vector2(trX, trZ);
			} while ((j < 50 && Terrain.getHeight(trX, trZ) < 0.1) || trV.dst(Input.V0) < 1 || trV.dst(Input.VT) < 1);
			float trR = (float)(Math.random() * 0.3 + .2);
			if (model!=null) trees.add(new Tree(model, new Vector3(trV.x, Terrain.getHeight(trV.x, trV.y) - 0.1f, trV.y), trR));
            else trees.add(new Tree(new Vector3(trV.x, Terrain.getHeight(trV.x, trV.y) - 0.1f, trV.y), trR));
		}
    }
}
