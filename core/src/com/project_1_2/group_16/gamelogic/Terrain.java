package com.project_1_2.group_16.gamelogic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Golfball;
import com.project_1_2.group_16.models.Tree;

public class Terrain {

    public static final float kineticGrass = 0.08f;
    public static final float staticGrass = 0.20f;

    public static final List<Sandpit> sandPits = new ArrayList<Sandpit>();
    public static final List<Tree> trees = new ArrayList<Tree>();

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
        //return (float)(0.5*(Math.sin((x-y)/7)+0.9));
        return (float)(0.4 * (0.9 - Math.pow(Math.E, -1*((x*x + y*y) / 8))));
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
        if (Collision.isInSandPit(sv.pos_x, sv.pos_y)) return Sandpit.kineticFriction;
        return kineticGrass;
    }
    
    /**
     * Return the static friction coefficient based on x,y location pulled from state vector.
     * @param sv StateVector
     * @return static friction coefficient
     */
    public static float getStaticFriction(StateVector sv) {
        if (Collision.isInSandPit(sv.pos_x, sv.pos_y)) return Sandpit.staticFriction;
        return staticGrass;
    }

    /**
     * Add sandpits to the course
     */
    public static void initSandPits() {
        sandPits.add(new Sandpit(-2f, -1f, -5f, 5f));
    }

    /**
     * Add trees to the course
     * @param model tree model
     * @param golfball golfball-reference
     */
    public static void initTrees(Model model, Golfball golfball) {
        Vector2 trV; float trX, trZ;
        Vector2 gV = new Vector2(golfball.getPosition().x, golfball.getPosition().z);
        Vector2 tV = new Vector2(App.flagpole.getPosition().x, App.flagpole.getPosition().z);
		for (int i = 0; i < App.NUMBER_OF_TREES; i++) {
			do {
				trX = (float)(Math.random() * (App.FIELD_SIZE - 0.5) - App.FIELD_SIZE / 2);
				trZ = (float)(Math.random() * (App.FIELD_SIZE - 0.5) - App.FIELD_SIZE / 2);
				trV = new Vector2(trX, trZ);
			} while (Terrain.getHeight(trX, trZ) < 0.1 || trV.dst(gV) < 1 || trV.dst(tV) < 1);
			float trR = (float)(Math.random() * 0.3 + .2);
			trees.add(new Tree(model, new Vector3(trV.x, Terrain.getHeight(trV.x, trV.y) - 0.1f, trV.y), trR));
		}
    }
}
