package com.project_1_2.group_16.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.models.Golfball;
import com.project_1_2.group_16.models.Tree;
import com.project_1_2.group_16.models.Wall;
import com.project_1_2.group_16.themes.WinterTheme;

/**
 * Class that handles collision detection for all game objects.
 */
public class Collision {

    /**
     * Return whether the ball is in the target-radius based on the coordinates of the Statevector.
     * Also takes velocity into account.
     * @param sv stateVector to pull position from
     * @return boolean, whether it is in the radius...
     */
    public boolean ballIsInTargetRadius(StateVector sv) {
        return Input.VT.dst(sv.x, sv.y) < Input.R && Physics.magnitude(sv.vx, sv.vy) < Input.VH;
    }

    /**
     * Check if the ball is in water.
     * @param sv used to pull the position
     * @return boolean, true if the ball is in water
     */
    public boolean ballIsInWater(StateVector sv) {
        return Input.THEME instanceof WinterTheme ? Terrain.getHeight(sv.x, sv.y) == -0.1f : Terrain.getHeight(sv.x, sv.y) < 0;
    }

    /**
     * Check if the ball has hit the tree.
     * @param sv used to pull the position
     * @return the tree the ball hit, if the ball didn't hit a tree it returns null
     */
    public Tree ballHitTree(StateVector sv) {
        for (Tree t : Input.TREES) {
            if (ballIsInTreeRadius(sv, t)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Return whether the ball is in the radius of the tree based on the coordinates of the Statevector.
     * @param sv stateVector to pull the position from
     * @param tree tree reference
     * @return
     */
    public boolean ballIsInTreeRadius(StateVector sv, Tree tree) {
        return new Vector2(tree.getPosition().x, tree.getPosition().z).dst(sv.x, sv.y) < tree.getRadius() + Golfball.SIZE/2;
    }

    /**
     * Checks if the wall is inside a wall.
     * @param sv the statevector of the ball
     * @return the wall hit, or null if not any
     */
    public Wall ballIsInWall(StateVector sv) {
        for (Wall w : Input.WALLS) {
            if (w.contains(sv.x, sv.y)) {
                return w;
            }
        }
        return null;
    }

    /**
     * Checks if a position is within a sandpit.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public boolean isInSandPit(float x, float y) {
        for (Sandpit pit : Input.SAND) {
            if (pit.getPosition().dst(x, y) < pit.getRadius()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Logic for water collision.
     * @param sv statevector of the ball
     * @param reference app reference, null if simulation
     */
    public void waterCollision(StateVector sv, App reference) {
        // reset position
        sv.x = reference == null ? Integer.MAX_VALUE : sv.prev.x;
        sv.y = reference == null ? Integer.MAX_VALUE : sv.prev.y;

        sv.stop = true;

        // stroke penalty
        if (reference != null) reference.GAME_SCREEN.increaseHitCounter(1);
    }

    /**
     * Logic for tree collision.
     * @param sv statevector of the ball
     * @param tree tree that was hit
     */
    public void treeCollision(StateVector sv, Tree tree) {
        Vector2 treePos = new Vector2(tree.getPosition().x, tree.getPosition().z);
        Vector2 position = new Vector2(sv.x, sv.y);
        Vector2 velocity = new Vector2(sv.vx, sv.vy);
        Vector2 normal = treePos.cpy().sub(position).nor();

        // https://stackoverflow.com/a/49059789
        velocity.sub(normal.scl(2*velocity.dot(normal)));
        sv.vx = velocity.x * Tree.frictionCoefficient;
        sv.vy = velocity.y * Tree.frictionCoefficient;
    }

    /**
     * Logic for wall collision.
     * @param sv statevector of the ball
     * @param wall wall that was hit
     * @param previousPosition previous position of the ball
     * @param reference app reference, null if simulation
     */
    public void wallCollision(StateVector sv, Wall wall, Vector2 previousPosition, App reference) {
        if (wall.getType() == Wall.MAZE_WALL) { // collision
            Vector2 position = new Vector2(sv.x, sv.y);
            Vector2 velocity = new Vector2(sv.vx, sv.vy);            

            // compute normal vector
            Vector2 normal;
            if (wall.closestWall(previousPosition.x, previousPosition.y) == Vector2.X) { // horizontal wall
                normal = new Vector2(position.x, position.y + 1).sub(position).nor();
            }
            else { // vertical wall
                normal = new Vector2(position.x + 1, position.y).sub(position).nor();
            }

            // https://stackoverflow.com/a/49059789
            velocity.sub(normal.scl(2*velocity.dot(normal)));
            sv.vx = velocity.x * Wall.frictionCoeficient;
            sv.vy = velocity.y * Wall.frictionCoeficient;
        }
        else { // water body
            waterCollision(sv, reference);
        }
    }
}
