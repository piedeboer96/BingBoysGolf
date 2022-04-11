package com.project_1_2.group_16.gamelogic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.misc.ANSI;
import com.project_1_2.group_16.models.Tree;

import bsh.EvalError;
import bsh.Interpreter;

public class Terrain {

    public static float kineticFriction = 0.08f;
    public static float staticFriction = 0.20f;

    public static String heightFunction;

    public static int NUMBER_OF_SANDPITS;
    public static int NUMBER_OF_TREES;

    public static final List<Sandpit> sandPits = new ArrayList<Sandpit>();
    public static final List<Tree> trees = new ArrayList<Tree>();

    public static final Interpreter BSH = new Interpreter();
    private static String eval;

    private static final float WATER_EDGE = App.FIELD_SIZE / 2 + App.TILE_SIZE;

    /**
     * Here the height method is defined that gives the height based on x,y coordinates.
     * @param x x coordinate
     * @param y y coordinate
     * @return height
     */
    public static float getHeight(float x, float y) {
        // make everything outside of the rendered area water
        if (Math.abs(x) >  WATER_EDGE || Math.abs(y) > WATER_EDGE) {
            return -1;
        }
        
        // evaluate height function
        eval = ((("float x = "+x).concat("; float y = ")+y).concat("; ")+heightFunction).concat(";");
        try {
            return (float)(double)(BSH.eval(eval));
        } catch (EvalError e) {
            System.out.println(ANSI.RED+"eval error"+ANSI.RESET+", interpreted: "+eval); 
            System.exit(0);
        } catch (ClassCastException e) {
            System.out.println(ANSI.RED+"cast exception"+ANSI.RESET+", please use double values for height function"); 
            System.exit(0);
        }
        return 0;
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
        return kineticFriction;
    }
    
    /**
     * Return the static friction coefficient based on x,y location pulled from state vector.
     * @param sv StateVector
     * @return static friction coefficient
     */
    public static float getStaticFriction(StateVector sv) {
        if (Collision.isInSandPit(sv.pos_x, sv.pos_y)) return Sandpit.staticFriction;
        return staticFriction;
    }

    /**
     * Add sandpits to the course
     */
    public static void initSandPits() {
        Vector2 sV; float sX, sZ;
        for (int i = 0; i < NUMBER_OF_SANDPITS; i++) {
            do { //TODO
                sX = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
                sZ = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
                sV = new Vector2(sX, sZ);
            } while (sV.dst(App.gV) < 4 || sV.dst(App.tV) < 4);
            sandPits.add(new Sandpit(sX - 2f, sX + 2f, sZ - 2f, sZ + 2f));
        }
    }

    /**
     * Add trees to the course
     * @param model tree model
     * @param golfball golfball-reference
     */
    public static void initTrees(Model model) {
        Vector2 trV; float trX, trZ;
		for (int i = 0; i < NUMBER_OF_TREES; i++) {
			do {
				trX = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
				trZ = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
				trV = new Vector2(trX, trZ);
			} while (Terrain.getHeight(trX, trZ) < 0.1 || trV.dst(App.gV) < 1 || trV.dst(App.tV) < 1);
			float trR = (float)(Math.random() * 0.3 + .2);
			trees.add(new Tree(model, new Vector3(trV.x, Terrain.getHeight(trV.x, trV.y) - 0.1f, trV.y), trR));
		}
    }
}
