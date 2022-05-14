package com.project_1_2.group_16.gamelogic;

import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.ai.Neighbour;
import com.project_1_2.group_16.ai.Particle;
import com.project_1_2.group_16.ai.Score;
import com.project_1_2.group_16.ai.Soldier;
import com.project_1_2.group_16.math.*;
import com.project_1_2.group_16.models.Tree;

/**
 * Class that handles most of the game logic.
 */
public class Game {

    /**
     * Step size.
     */
    public static final float h = 0.05f;

    /**
     * Friction caused by hitting trees.
     */
    public static final float treeFriction = -0.75f;

    /**
     * Number of simulations.
     */
    public static int simulCounter = 0;

    // numerical solver
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
        if (hittree != null) {
            Vector2 vT = new Vector2(hittree.getPosition().x, hittree.getPosition().z);
            Vector2 vB = new Vector2(sv.x, sv.y);

            if (vB.x > vT.x + hittree.getRadius() * 0.5) {
                sv.vx *= treeFriction;
            }
            else if (vB.x < vT.x - hittree.getRadius() * 0.5) {
                sv.vx *= treeFriction;
            }
            else if (vB.y > vT.y + hittree.getRadius() * 0.5) {
                sv.vy *= treeFriction;
            }
            else if (vB.y < vT.y - hittree.getRadius() * 0.5) {
                sv.vy *= treeFriction;
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
    public void endGame(App app) { // TODO
        String message = "Congratulations! ";
        switch (app.GAME_SCREEN.increaseHitCounter(0)) {
            case 1: message += " You got a hole-in-one! Unbelievable!"; break;
            default: message += "You finished the hole in "+app.GAME_SCREEN.increaseHitCounter(0)+" shots!";
        }
        System.out.println(message);
    }

    /**
     * Helper method for doing bot simulations.
     * @param sv statevector
     * @param p particle
     * @param n neighbour
     * @param s soldier
     */
    public void runEngine(StateVector sv, Particle p, Neighbour n, Soldier s) {
        simulCounter++;
        while (!sv.stop) {
            run(sv, null);
            float temp = Score.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y);
            if (p!=null) {
                if (temp < p.getFitness() && Physics.magnitude(sv.vx, sv.vy) < Collision.MAX_HOLE_VELOCITY) {
                    p.fitness = temp;
                }
            } 
            else if (n!=null) {
                if (temp < n.getFitness()) {
                    n.fitness = temp;
                }
            } 
            else if (s!=null) {
                if (temp < s.fitness && Physics.magnitude(sv.vx, sv.vy) < Collision.MAX_HOLE_VELOCITY) {
                    s.fitness = temp;
                }
            }
        }
    }
}
