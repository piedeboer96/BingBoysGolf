package com.project_1_2.group_16.gamelogic;

import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Flagpole;
import com.project_1_2.group_16.models.Tree;

public class Collision {

    /**
     * Return whether the ball is in the target-radius based on the coordinates of the Statevector
     * calculated in a computationally efficient way
     * @param sv stateVector to pull position from
     * @return boolean, whether it is in the radius...
     */
    public static boolean ballIsInTargetRadius(StateVector sv, Flagpole t) {

        float R = t.getRadius();
        float dx = Math.abs(sv.pos_x - t.getPosition().x);
        float dy = Math.abs(sv.pos_y - t.getPosition().z);

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
     * Check if the ball is in water.
     * @param sv used to pull the position
     * @return boolean, true if the ball is in water
     */
    public static boolean ballIsInWater(StateVector sv) {
        if (Terrain.getHeight(sv.pos_x, sv.pos_y) < 0) {
            return true;
        }
        return false;
    }

    /**
     * Check if the ball has hit the tree
     * @param sv used to pull the position
     * @return the tree the ball hit, if the ball didn't hit a tree it returns null
     */
    public static Tree ballHitTree(StateVector sv) {
        for (int i = 0; i < Terrain.NUMBER_OF_TREES; i++) {
            if (ballIsInTreeRadius(sv, Terrain.trees.get(i))) {
                return Terrain.trees.get(i);
            }
        }
        return null;
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
    public static boolean ballIsInTreeRadius(StateVector sv, Tree tree) {
        float R = tree.getRadius();
        float dx = Math.abs(sv.pos_x - tree.getPosition().x);
        float dy = Math.abs(sv.pos_y - tree.getPosition().z);

        if (dx + dy < R) {
            return true;
        } else if (dx > R) {
            return false;
        } else if (dy > R) {
            return false;
        } else if (((dx*dx) + (dy*dy)) < (R*R)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a position is within a sandpit
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public static boolean isInSandPit(float x, float y) {
        float r, dx, dy;
        for (Sandpit pit : Terrain.sandPits) {
            r = pit.getRadius();
            dx = Math.abs(x - pit.getPosition().x);
            dy = Math.abs(y - pit.getPosition().y);
            if (dx + dy < r) {
                return true;
            } 
            else if (((dx*dx) + (dy*dy)) < (r*r)) {
                return true;
            }
        }
        return false;
    }
}
