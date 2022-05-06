package com.project_1_2.group_16.gamelogic;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.misc.ANSI;
import com.project_1_2.group_16.models.Tree;
import bsh.EvalError;
import bsh.Interpreter;

public class Terrain {

    public static final List<Sandpit> sandPits = new ArrayList<Sandpit>();
    public static final List<Tree> trees = new ArrayList<Tree>();
    public static final float WATER_EDGE = App.FIELD_SIZE / 2 + App.TILE_SIZE;
    public static final Interpreter BSH = new Interpreter();
    public static final Collision collision = new Collision();
    private static String eval;

    /**
     * Evaluates the height-formula for a set of coordinates. 
     * @param x x coordinate
     * @param y y coordinate
     * @returns max(z-coordinate, -0.01)
     * 
     * @throws EvalError if the height function can't be evaluated
     * @throws ClassCastException if the height function doesn't return a {@code double} value
     */
    public static float getHeight(float x, float y) {
        // make everything outside of the rendered area water
        if (Math.abs(x) >  WATER_EDGE || Math.abs(y) > WATER_EDGE) {
            return -1;
        }
        
        // evaluate height function
        eval = ((("float x = "+x).concat("; float y = ")+y).concat("; ")+Input.H).concat(";");
        try {
            return Math.max((float)(double)(BSH.eval(eval)), -0.01f);
        } catch (EvalError e) {
            System.out.println(ANSI.RED+"eval error"+ANSI.RESET+", interpreted: "+eval); 
            System.exit(0);
        } catch (NullPointerException e) {
            System.out.println(ANSI.RED+"return error"+ANSI.RESET+", height function doesn't return anything");
            System.exit(0);
        } catch (ClassCastException e) {
            System.out.println(ANSI.RED+"cast exception"+ANSI.RESET+", please use double values for height function"); 
            System.exit(0);
        } catch (RuntimeException e) {
            System.out.println(ANSI.RED+"unexpected error"+ANSI.RESET+", please re-evaluate the height function");
            System.out.print(ANSI.RED+"error stack: "+ANSI.RESET);
            e.printStackTrace();
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
        float[] slopes = {
            (getHeight(coordinates[0] + h, coordinates[1]) - getHeight(coordinates[0] - h, coordinates[1]))/(2*h),
            (getHeight(coordinates[0], coordinates[1]+h) - getHeight(coordinates[0], coordinates[1] - h))/(2*h)
        };
        return slopes;
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
     * Add sandpits to the course
     */
    public static void initSandPits() {
        Vector2 sV; float sX, sZ;
        for (int i = 0; i < Input.SAND; i++) {
            do {
                sX = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
                sZ = (float)(Math.random() * (App.FIELD_SIZE - App.TILE_SIZE) - App.FIELD_SIZE / 2);
                sV = new Vector2(sX, sZ);
            } while (getHeight(sX, sZ) < 0 || sV.dst(Input.V0) < 2 || sV.dst(Input.VT) < 2);
            sandPits.add(new Sandpit(sX, sZ, 1f));
            sandPits.add(new Sandpit(sX + (float)Math.random() * 1f - 0.5f, sZ + (float)Math.random() * 1f - 0.5f, 1f));
            sandPits.add(new Sandpit(sX + (float)Math.random() * 1f - 0.5f, sZ + (float)Math.random() * 1f - 0.5f, 1f));
        }
    }

    /**
     * Add trees to the course
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
			trees.add(new Tree(model, new Vector3(trV.x, Terrain.getHeight(trV.x, trV.y) - 0.1f, trV.y), trR));
		}
    }
    public static void initTreesForTesting() {
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
            trees.add(new Tree(new Vector3(trV.x, Terrain.getHeight(trV.x, trV.y) - 0.1f, trV.y), trR));
        }
    }
}
