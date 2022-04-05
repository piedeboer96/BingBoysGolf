package com.project_1_2.group_16.physics;

import java.util.HashMap;

import com.project_1_2.group_16.App;

import bsh.EvalError;
import bsh.Interpreter;

public class Terrain {

    public float[] initialPos;
    public float[] targetPos;
    public float targetRadius;
    public float[] frictionCoefficientGrass;
    public String heightFunction;
    public float[] sandPosX;
    public float[] sandPosY;
    public float[] frictionCoefficientSand;
    public int treeCount = 0;
    public HashMap<Integer, float[]> trees;
    private Interpreter bsh = new Interpreter();

    /**
     * Create a terrain based on the input module values.
     * @param initialPos x and y initial coordinates of the ball
     * @param targetPos x and y coordinates of the radius
     * @param targetRadius radius of the target
     * @param frictionGrass kinetic and static friction of the grass (0=kinetic, 1=static)
     * @param heightF formula of the height function based on a string
     * @param sandPosX lower and upper bound x coordinate of sandpit (exclusive)
     * @param sandPosY lower and upper bound y coordinate of sandpit (exclusive)
     * @param frictionSand kinetic and static friction of the sand (0=kinetic, 1=static)
     * @param trees trees put in a hashMap, O(1)
     */
    public Terrain(float[] initialPos, float[] targetPos, float targetRadius, float[] frictionGrass, String heightF, float[] sandPosX, float[] sandPosY, float[] frictionSand, HashMap<Integer, float[]> trees) {
        this.initialPos = initialPos;
        this.targetPos = targetPos;
        this.targetRadius = targetRadius;
        this.frictionCoefficientGrass = frictionGrass;
        this.heightFunction = heightF;
        this.sandPosX = sandPosX;
        this.sandPosY = sandPosY;
        this.frictionCoefficientSand = frictionSand;
        this.trees = trees;
        this.treeCount = trees.size();
    }

    /**
     * Here the height method is defined that gives the height based on x,y coordinates.
     * @param x_pos x coordinate
     * @param y_pos y coordinate
     * @return height
     */
    public float getHeight(float x, float y) {
        // make everything outside of the rendered area water
        if (Math.abs(x - initialPos[0]) > App.FIELD_SIZE / 2 + App.TILE_SIZE ||
            Math.abs(y - initialPos[1]) > App.FIELD_SIZE / 2 + App.TILE_SIZE) {
            return -1;
        }

        String eval = "double x = "+x+"; double y = "+y+"; "+heightFunction;
        try {
            return (float)(double)bsh.eval(eval);
        } catch (EvalError e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Return the slope in dh/dx and dh/dy.
     * @param coordinates array of coordinates
     * @param h step-size
     * @return array with dh/dx and dh/dy
     */
    public float[] getSlope (float[] coordinates, float h) {
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
    public float getKineticFriction(StateVector sv) {
        float sandPosX0 = sandPosX[0];
        float sandPosX1 = sandPosX[1];
        float sandPosY0 = sandPosY[0];
        float sandPosY1 = sandPosY[1];
        if(sv.pos_x > sandPosX0 && sv.pos_x < sandPosX1 && sv.pos_y > sandPosY0 && sv.pos_y < sandPosY1) {
            return frictionCoefficientSand[0];
        }
        return frictionCoefficientGrass[0];
    }
    
    /**
     * Return the static friction coefficient based on x,y location pulled from state vector.
     * @param sv StateVector
     * @return static friction coefficient
     */
    public float getStaticFriction(StateVector sv) {
        float sandPosX0 = sandPosX [0];
        float sandPosX1 = sandPosX [1];
        float sandPosY0 = sandPosY [0];
        float sandPosY1 = sandPosY [1];
        if(sv.pos_x > sandPosX0 && sv.pos_x < sandPosX1 && sv.pos_y > sandPosY0 && sv.pos_y < sandPosY1) {
            return frictionCoefficientSand[1];
        }
        return frictionCoefficientGrass[1];
    }

    /**
     * Return whether the ball is in the target-radius based on the coordinates of the Statevector
     * calculated in a computationally efficient way
     * @param sv stateVector to pull position from
     * @return boolean, whether it is in the radius...
     */
    public boolean ballIsInTargetRadius(StateVector sv) {
        float R = targetRadius;
        float dx = Math.abs(sv.pos_x - targetPos[0]);
        float dy = Math.abs(sv.pos_y - targetPos[1]);

        if (dx + dy <= R) {
            return true;
        } else if (dx > R) {
            return false;
        } else if (dy > R) {
            return false;
        } else if ((( dx*dx) + (dy*dy)) <= (R*R)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return whether the ball is in the radius of the tree based on the coordinates of the Statevector
     * calculated in a computationally efficient way
     * @param sv stateVector to pull the position from
     * @param treeCenterX x coordinate of the
     * @param treeCenterY
     * @param treeRadius
     * @return
     */
    public boolean ballIsInTreeRadius(StateVector sv, float treeCenterX, float treeCenterY, float treeRadius) {
        float R = treeRadius;
        float dx = Math.abs(sv.pos_x - treeCenterX);
        float dy = Math.abs(sv.pos_y - treeCenterY);

        if (dx + dy <= R) {
            return true;
        } else if (dx > R) {
            return false;
        } else if (dy > R) {
            return false;
        } else if (((dx*dx) + (dy*dy)) <= (R*R)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the ball is in water.
     * @param sv used to pull the position
     * @return boolean, true if the ball is in water
     */
    public boolean ballIsInWater(StateVector sv) {
        if (getHeight(sv.pos_x, sv.pos_y) < 0) {
            return true;
        }
        return false;
    }

    /**
     * Check if the ball has hit the tree
     * @param sv used to pull the position
     * @return boolean, true if the ball inside the radius of the tree
     */
    public boolean ballHitTree(StateVector sv) {
        for (int i = 1; i <= treeCount; i++) {
            float[] treeArray = trees.get(i);
            if (ballIsInTreeRadius(sv, treeArray[0], treeArray[1], treeArray[2])) {
                return true;
            }
        }
        return false;
    }
}
