package com.project_1_2.group_16.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.bot.Agent;
import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.math.*;
import com.project_1_2.group_16.models.Tree;
import com.project_1_2.group_16.models.Wall;

public class Game {

    /**
     * Step size.
     */
    public static float h = 0.05f;

    public static boolean useFloodFill = true;

    /**
     * Number of simulations.
     */
    public static int simulCounter = 0;

    private NumericalSolver solver;

    /**
     * Run the physics engine.
     * @param sv the state vector
     * @param reference a reference to an App object. leave null if doing simulations.
     */
    public void run(final StateVector sv, App reference) {
        // update state vector with numerical solver
        this.solver.solve(h, sv);

        // check water collision
        if (Terrain.collision.ballIsInWater(sv)) {
            // reset position
            sv.x = reference == null ? Integer.MAX_VALUE : sv.prev.x;
            sv.y = reference == null ? Integer.MAX_VALUE : sv.prev.y;

            sv.stop = true;

            // stroke penalty
            if (reference != null) reference.GAME_SCREEN.increaseHitCounter(1);
        }

        // check tree collision
        Tree hittree = Terrain.collision.ballHitTree(sv);
        if (hittree == null) {
            for (Tree t : Input.TREES) {
                t.recentlyHit = false;
            }
        }
        else if (!hittree.recentlyHit) {
            hittree.recentlyHit = true;

            Vector2 tree = new Vector2(hittree.getPosition().x, hittree.getPosition().z);
            Vector2 position = new Vector2(sv.x, sv.y);
            Vector2 velocity = new Vector2(sv.vx, sv.vy);
            Vector2 tNegativeOne = position.cpy().sub(velocity);

            double deltaNormal = (tree.y - position.y) / (tree.x - position.x);
            double deltaOrthoNormal = -1.0 / deltaNormal;

            double bNormal = tNegativeOne.y - deltaNormal * tNegativeOne.x;
            double bOrthoNormal = position.y - deltaOrthoNormal * position.x;

            float xProject = (float)((bOrthoNormal - bNormal) / (deltaNormal - deltaOrthoNormal));
            float yProject = (float)(deltaNormal * xProject + bNormal);
            Vector2 tProject = new Vector2(xProject, yProject);

            float opposite = tProject.dst(tNegativeOne);
            float adjacent = tProject.dst(position);

            double inputAngle = Math.atan(opposite / adjacent);
            double rotationAngle = Math.PI - 2 * inputAngle;

            if (velocity.x >= 0 && velocity.y >= 0) velocity.rotateRad((float)rotationAngle);
            else if (velocity.x >= 0 && velocity.y <= 0) velocity.rotateRad((float)(2 * Math.PI - rotationAngle));
            else if (velocity.x <= 0 && velocity.y >= 0) velocity.rotateRad((float)(2 * Math.PI - rotationAngle));
            else velocity.rotateRad((float)rotationAngle);

            sv.vx = -velocity.x * Tree.treeCoefficient;
            sv.vy = -velocity.y * Tree.treeCoefficient;
        }

        // check wall collision
        Wall hitWall = Terrain.collision.ballIsInWall(sv);
        if (hitWall != null && !hitWall.recentlyHit) {
            hitWall.recentlyHit = true;

            Vector2 position = new Vector2(sv.x, sv.y);
            Vector2 velocity = new Vector2(sv.vx, sv.vy);
            Vector2 tNegativeOne = position.cpy().sub(velocity);

            Vector2 wallVector = hitWall.closestWall(sv.x, sv.y);
            
            float adjacent, opposite;
            double inputAngle, rotationAngle;
            if (wallVector == Vector2.X) { // horizontal
                adjacent = Math.abs(position.x - tNegativeOne.x);
                opposite = Math.abs(position.y - tNegativeOne.y);

                inputAngle = Math.atan(opposite / adjacent);
                rotationAngle = Math.PI - 2 * inputAngle;

                if (velocity.x >= 0 && velocity.y >= 0) velocity.rotateRad((float)rotationAngle);
                else if (velocity.x >= 0 && velocity.y <= 0) velocity.rotateRad((float)(2 * Math.PI - rotationAngle));
                else if (velocity.x <= 0 && velocity.y >= 0) velocity.rotateRad((float)(2 * Math.PI - rotationAngle));
                else velocity.rotateRad((float)rotationAngle);
            }
            else { // vertical
                adjacent = Math.abs(position.y - tNegativeOne.y);
                opposite = Math.abs(position.x - tNegativeOne.x);

                inputAngle = Math.atan(opposite / adjacent);
                rotationAngle = Math.PI - 2 * inputAngle;

                if (velocity.x >= 0 && velocity.y >= 0) velocity.rotateRad((float)(2 * Math.PI - rotationAngle));
                else if (velocity.x >= 0 && velocity.y <= 0) velocity.rotateRad((float)rotationAngle);
                else if (velocity.x <= 0 && velocity.y >= 0) velocity.rotateRad((float)rotationAngle);
                else velocity.rotateRad((float)(2 * Math.PI - rotationAngle));
            }

            sv.vx = -velocity.x * Wall.frictionCoeficient;
            sv.vy = -velocity.y * Wall.frictionCoeficient;
        }
        else if (hitWall == null) { // TODO OPTIMISE!!! GETS CALLED EVERY FRAME EVEN WHEN NO WHERE NEAR!!!
            for (Wall w : Input.WALLS) {
                w.recentlyHit = false;
            }
        }

        // check for hole collision
        if (Terrain.collision.ballIsInTargetRadius(sv)) {
            if (reference != null) this.endGame(reference);
            sv.stop = true;
        }

        // check for a stop
        if (Physics.magnitude(sv.vx, sv.vy) < h) {
            float[] partialDerivatives = this.solver.getPartialDerivatives();
            sv.stop = Physics.magnitude(partialDerivatives[0], partialDerivatives[1]) < Terrain.getStaticFriction(sv);
        }
    }

    /**
     * Set the numerical solver to be used by the physics engine.
     * @param solver
     */
    public void setNumericalSolver(int solver) {
        switch (solver) {
            case NumericalSolver.EULER: this.solver = new Euler(); break;
            case NumericalSolver.RK2: this.solver = new RK2(); break;
            case NumericalSolver.RK4: this.solver = new RK4(); break;
        }
    }

    /**
     * Display a message that the user has completed the course
     */
    public void endGame(App app) {
        app.GAME_SCREEN.createFlag(false);
    }

    /**
     * Helper method for doing bot simulations, runs the Physics Engine until the Ball stops
     * @param sv StateVector of the ball
     * @param a agent, iteratively updates an Agent's fitness
     */
    public void runEngine(StateVector sv, Agent a) {
        simulCounter++;
        while(!sv.stop) {
            run(sv, null);
            //float temp = BotHelper.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y);
           float temp;

           if(useFloodFill){
               temp = BotHelper.getFloodFillFitness(sv.x, sv.y);
               if(temp <= 1){
                   useFloodFill = false;
               }
           }else{
               temp = BotHelper.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y);
           }

            if (a!=null){

                if(temp < a.fitness && Physics.magnitude(sv.vx, sv.vy) < Input.VH){
                    a.fitness = temp;
                }
            }
        }
    }
}
