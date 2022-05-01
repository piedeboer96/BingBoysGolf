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
import com.project_1_2.group_16.physics.Physics;

public class Game {

    /**
     * Step size.
     */
    public static float h = 0.08f;

    public static int simulCounter = 0;

    /**
     * Friction caused by hitting trees.
     */
    public static final float treeFriction = -0.75f;

    /**
     * Collision handler.
     */
    public final Collision collision = new Collision();

    private NumericalSolver solver;

    /**
     * Run the physics engine.
     * @param sv the state vector
     * @param reference a reference to an App object. leave null if doing simulations.
     */
    public void run(final StateVector sv, App reference) {
        // update state vector with numerical solver
        if(reference != null) {
            this.solver.solve(h, sv);
        }else {
            this.solver.solve(h*1.5f, sv);
        }

        // check water collision
        if (this.collision.ballIsInWater(sv)) {

            // reset position
                if(reference == null){
                    sv.x = Integer.MAX_VALUE;
                    sv.y = Integer.MAX_VALUE;
                }else{
                    sv.x = sv.prev.x;
                   sv.y = sv.prev.y;
                }


            sv.stop = true;

            // stroke penalty
            if (reference != null) reference.hitsCounter++;
        }

        // check tree collision
        Tree hittree = this.collision.ballHitTree(sv);
        if (hittree != null) {
            System.out.println("tree");

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

        // check hole collision
        if (this.collision.ballIsInTargetRadius(sv)) {
            if (reference != null) this.endGame(reference);
            sv.stop=true;
            System.out.println("HITHITHITHITHIHTIHTIHTIHTIHTIHTIHTIHTIHT");
        }


        // check for a stop
        if (Physics.magnitude(sv.vx, sv.vy) < h) {
            float[] partialDerivatives = this.solver.getPartialDerivatives();
            if ((Physics.magnitude(partialDerivatives[0], partialDerivatives[1]) < Terrain.getStaticFriction(sv))) {
                sv.stop = true;
            }
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
        String message = "Congratulations! ";
        switch (app.hitsCounter) {
            case 1: message += " You got a hole-in-one! Unbelievable!"; break;
            default: message += "You finished the hole in "+app.hitsCounter+" shots!";
        }
        System.out.println(message);
    }


    public void runEngine(StateVector sv, App reference, Particle p, Neighbour n, Soldier s){
//        System.out.println(sv);
        simulCounter++;
        while(!sv.stop){
            run(sv, reference);
            float temp = Score.calculateEucledianDistance(Input.VT.x, Input.VT.y, sv.x, sv.y);
            if(p!=null) {
                if (temp < p.getFitness()){
                    p.fitness = temp;
                }
            } else if(n!=null){
                if (temp < n.getFitness()){
                    n.fitness = temp;
                }
            } else if(s!=null){
                if(temp < s.fitness){
                    s.fitness = temp;
                }
            }
        }
//        System.out.println("end " + sv);
    }
}
