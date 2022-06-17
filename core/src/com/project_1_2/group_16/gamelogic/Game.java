package com.project_1_2.group_16.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.bot.Agent;
import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.math.*;
import com.project_1_2.group_16.math.ode.Euler;
import com.project_1_2.group_16.math.ode.NumericalSolver;
import com.project_1_2.group_16.math.ode.RK2;
import com.project_1_2.group_16.math.ode.RK4;
import com.project_1_2.group_16.models.Tree;
import com.project_1_2.group_16.models.Wall;

public class Game {

    /**
     * Step size.
     */
    public static float h = 0.05f;

    /**
     * Determines whether we use the FloodFill table as a heuristic.
     */
    public static boolean useFloodFill;

    /**
     * Number of simulations.
     */
    public static int simulCounter = 0;

    private NumericalSolver solver;

    private boolean recentlyHitTree;
    private boolean recentlyHitWall;

    /**
     * Run the physics engine.
     * @param sv the state vector
     * @param reference a reference to an App object. leave null if doing simulations.
     */
    public void run(final StateVector sv, App reference) {
        // save previous position for collision detection
        Vector2 previousPosition = new Vector2(sv.x, sv.y);

        // update state vector with numerical solver
        this.solver.solve(h, sv);

        // check water collision
        if (Terrain.collision.ballIsInWater(sv)) {
            Terrain.collision.waterCollision(sv, reference);
        }

        // check tree collision
        Tree hittree = Terrain.collision.ballHitTree(sv);
        if (hittree == null && this.recentlyHitTree) {
            this.recentlyHitTree = false;
            for (Tree t : Input.TREES) {
                t.recentlyHit = false;
            }
        }
        else if (hittree != null && !hittree.recentlyHit) {
            this.recentlyHitTree = true;
            hittree.recentlyHit = true;

            Terrain.collision.treeCollision(sv, hittree);
        }

        // check wall collision
        Wall hitWall = Terrain.collision.ballIsInWall(sv);
        if (hitWall != null && !hitWall.recentlyHit) {
            this.recentlyHitWall = true;
            hitWall.recentlyHit = true;

            Terrain.collision.wallCollision(sv, hitWall, previousPosition, reference);
        }
        else if (hitWall == null && this.recentlyHitWall) {
            this.recentlyHitWall = false;
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

            float temp;
            if (a!=null){
                if(useFloodFill){
                    temp = BotHelper.getFloodFillFitness(sv.x, sv.y);
                    if(temp <= 1){
                        useFloodFill = false;
                    }
                }else{
                    temp = BotHelper.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y);
                }
                if(temp < a.fitness && Physics.magnitude(sv.vx, sv.vy) < Input.VH){
                    a.fitness = temp;
                }
            }
        }
        if(useFloodFill && a != null){
            a.fitness = BotHelper.getFloodFillFitness(sv.x, sv.y);
        }
    }
}
