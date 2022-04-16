package com.project_1_2.group_16.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Tree;

public class Collision {

    /**
     * Return whether the ball is in the target-radius based on the coordinates of the Statevector
     * calculated in a computationally efficient way
     * @param sv stateVector to pull position from
     * @return boolean, whether it is in the radius...
     */
    public static boolean ballIsInTargetRadius(StateVector sv) {
        return Input.VT.dst(sv.pos_x, sv.pos_y) < Input.R;
    }

    /**
     * Check if the ball is in water.
     * @param sv used to pull the position
     * @return boolean, true if the ball is in water
     */
    public static boolean ballIsInWater(StateVector sv) {
        return Terrain.getHeight(sv.pos_x, sv.pos_y) < 0;
    }

    /**
     * Check if the ball has hit the tree
     * @param sv used to pull the position
     * @return the tree the ball hit, if the ball didn't hit a tree it returns null
     */
    public static Tree ballHitTree(StateVector sv) {
        for (int i = 0; i < Input.TREES; i++) {
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
     * @param tree tree reference
     * @return
     */
    public static boolean ballIsInTreeRadius(StateVector sv, Tree tree) {
        return new Vector2(tree.getPosition().x, tree.getPosition().z).dst(sv.pos_x, sv.pos_y) < tree.getRadius();
    }

    /**
     * Checks if a position is within a sandpit
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public static boolean isInSandPit(float x, float y) {
        for (Sandpit pit : Terrain.sandPits) {
            if (pit.getPosition().dst(x, y) < pit.getRadius()) {
                return true;
            }
        }
        return false;
    }
}
