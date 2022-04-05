package com.project_1_2.group_16.gamelogic;

import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Flagpole;

import static com.project_1_2.group_16.gamelogic.Terrain.*;

public class Collision {

    /**
     * Return whether the ball is in the target-radius based on the coordinates of the Statevector
     * calculated in a computationally efficient way
     * @param sv stateVector to pull position from
     * @return boolean, whether it is in the radius...
     */
    public static boolean ballIsInTargetRadius(StateVector sv, Flagpole t) {

        float R = t.r;
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
     * Return whether the ball is in the radius of the tree based on the coordinates of the Statevector
     * calculated in a computationally efficient way
     * @param sv stateVector to pull the position from
     * @param treeCenterX x coordinate of the
     * @param treeCenterY
     * @param treeRadius
     * @return
     */
    public static boolean ballIsInTreeRadius(StateVector sv, float treeCenterX, float treeCenterY, float treeRadius) {
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
    public static boolean ballIsInWater(StateVector sv) {
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
    public static boolean ballHitTree(StateVector sv) {
        //for (int i = 1; i <= treeCount; i++) {
        //    float[] treeArray = trees.get(i);
        //    if (ballIsInTreeRadius(sv, treeArray[0], treeArray[1], treeArray[2])) {
        //        return true;
        //    }
        //}
        return false;
    }
}
