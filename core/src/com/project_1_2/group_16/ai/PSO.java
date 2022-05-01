package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.physics.Physics;

import java.util.ArrayList;

public class PSO {

    static float maxVelocity = 5;
    static float minVelocity = 0;
    static int population_size;

    static int iteration = 1;
    static float N = population_size;
    static float W = (float) (0.4*((iteration-N)/Math.pow(N, 2)) + 0.4f);
    static float c1 = -3*(iteration/N)+3.5f;
    static float c2 = 3*(iteration/N)+0.5f;

    static Particle globalBest;

    /**
     * Method which can be called to run the PSO
     * @param maxIterations the maximum amount of generations the PSO can run
     * @param particles the intialised particles
     */
    private static void runPSO(int maxIterations, Particle[] particles){
        int count = 0;
        N = particles.length;
//        W = (float) (0.4*((iteration-N)/Math.pow(N, 2)) + 0.4f);
//        c1 = -3*(iteration/N)+3.5f;
//        c2 = 3*(iteration/N)+0.5f;
        W = 0.72984f;
        c1 = 1f;
        c2 = 2.0f;
        while(count < maxIterations && globalBest.getFitness() > Input.R){
            count++;
            for(int i = 0; i < particles.length; i++){
                iteration = i + 1;
                Particle current = particles[i];

                float[] updated = getValidVelocity(updatedVelocity(current));

                    Particle updatedP = new Particle(new StateVector(Input.V0.x, Input.V0.y, updated[0], updated[1]));
                    updatedP.setlocalBest(current.getlocalBest());
                    current = updatedP;
                    if(current.getlocalBest().getFitness() > current.getFitness()){
                        current.setlocalBest(updatedP);
                        if(current.getlocalBest().getFitness()< globalBest.getFitness()){
                            globalBest = Particle.clone(current);
                        }
                    }
            }
        }
    }

    /**
     * method which updates the particle velocity based on the inertia, personal influence and social influence
     * @param particle
     * @return the updated velocity
     */
    public static float[] updatedVelocity(Particle particle){
        float[] updatedvxy = new float[2];
        float[] inertia = inertia(particle);
        float[] personalInfluence = personalInfluence(particle);
        float[] socialInfluence = socialInfluence(particle);
        updatedvxy[0] = particle.getVx() + inertia[0] + personalInfluence[0] + socialInfluence[0];
        updatedvxy[1] = particle.getVy() + inertia[1] + personalInfluence[1] + socialInfluence[1];
        return updatedvxy;
    }

    /**
     * method which checks if the velocity is valid
     * @param vxvy
     * @return a valid velocity
     */
    public static float[] getValidVelocity(float[] vxvy){
        if(Physics.magnitude(vxvy[0], vxvy[1]) > 5f){
            float[] vxy = new float[2];
            vxy[0] = (float) (vxvy[0]/Math.sqrt(50));
            vxy[1] = (float) (vxvy[1]/Math.sqrt(50));
            return vxy;
        }
        return vxvy;
    }

    /**
     * method which calculates the inertia
     * @param particle
     * @return inertia for xy
     */
    public static float[] inertia(Particle particle){
        float[] inertia = new float[2];
        inertia[0] = W * particle.getVx();
        inertia[1] = W * particle.getVy();
        return inertia;
    }

    /**
     * method which calculates the personal influence
     * @param particle
     * @return personal influence for xy
     */
    public static float[] personalInfluence(Particle particle){
        float[] personalInfluenceXY = new float[2];
        float U1x = (float) Math.random();
        float U1y = (float) Math.random();
        personalInfluenceXY[0] = c1*U1x*(particle.getlocalBest().getVx()-particle.getVx());
        personalInfluenceXY[1] = c1*U1y*(particle.getlocalBest().getVy()-particle.getVy());
        return personalInfluenceXY;
    }

    /**
     * method which calculates the social influence
     * @param particle
     * @return social influence for xy
     */
    public static float[] socialInfluence(Particle particle){
        float[] socialInfluence = new float[2];
        float U2x = (float) Math.random();
        float U2y = (float) Math.random();
        socialInfluence[0] = c2*U2x*(globalBest.getVx()-particle.getVx());
        socialInfluence[1] = c2*U2y*(globalBest.getVy()-particle.getVy());
        return socialInfluence;
    }

    public static Particle[] initializeParticles(){
        ArrayList<float[]> init_vel = Score.availableVelocities();
        population_size = init_vel.size();
        globalBest = new Particle(new StateVector(Input.V0.x, Input.V0.y, 0.1f, 0.1f));
        Particle[] particles  = new Particle[init_vel.size()];
        for(int i = 0; i < init_vel.size(); i++){
            Particle particle = new Particle(new StateVector(Input.V0.x, Input.V0.y, init_vel.get(i)[0], init_vel.get(i)[1]));
            particle.setlocalBest(particle);
            particles[i] = particle;
            if(particle.getFitness() < globalBest.getFitness()) {
                globalBest = Particle.clone(particle);
            }
        }
        return particles;
    }


    public static void main(String[] args) {
        System.out.println("starting...");
        runPSO(100, initializeParticles());
        globalBest.print();
        System.out.println("amount of simulations taken " + Game.simulCounter);
    }
}
