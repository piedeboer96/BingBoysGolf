package com.project_1_2.group_16.ai;

import java.util.Arrays;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.physics.Physics;

/**
 * Class to utilize our Particle Swarm Optimization.
 */

public class PSO {
    static float maxVelocity = 5;
    static float minVelocity = 0;
    static int population_size = 30;
    static float c1 = 1.0f;
    static float c2 = 2f;
    static Particle globalBest;
    static float holex = Input.VT.x;
    static float holey = Input.VT.y;
    static float startx = Input.V0.x;
    static float starty = Input.V0.y;
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Particle[] particles = initializeParticles(population_size);
        runPSO(150, particles);
        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("runtime: " + (end - start) + " ms");
        EngineSimulator test = new EngineSimulator(startx, starty, globalBest.getVx(), globalBest.getVy());
        test.runRK4ai();
        System.out.println("Simulation run--> vx: " + globalBest.getVx() + " vy: " + globalBest.getVy() + " x,y: " + test.endPos_X +"," +  test.endPos_Y  + " Eucledian distance: " + globalBest.getScore());

    }

    /**
     *This method initialisez the particles that are going to be used for the PSO
     * @param population_size the population size of the particle swarm
     * @return
     */
    public static Particle[] initializeParticles(int population_size){
        EngineSimulator es;
        globalBest = new Particle(0, 0);
        globalBest.setScore(Integer.MAX_VALUE);
        globalBest.setlocalBest(globalBest);
        Particle[] population = new Particle[population_size];
        for(int i = 0; i < population_size; i++){
            float[] vxy = validVelocity();
            Particle current = new Particle(vxy[0], vxy[1]);
            es = new EngineSimulator(startx, starty, current.getVx(), current.getVy()); // add positions
            es.runRK4ai();
            float[] xy = {es.endPos_X, es.endPos_Y};
            current.setXY(xy);
            current.setScore(calculateEucledianDistance(xy[0], xy[1], holex, holey));//floodfill
            population[i] = current;
            current.setlocalBest(Particle.clone(current));
            if(current.getScore() < globalBest.getScore()){
                globalBest = Particle.clone(current);
            }
        }
        return population;
    }

    /**
     * Method which can be called to run the PSO 
     * @param maxIterations the maximum amount of generations the PSO can run
     * @param particles the intialised particles
     */
    private static void runPSO(int maxIterations, Particle[] particles){
        int count = 0;
        EngineSimulator es;
        while(count < maxIterations && globalBest.getScore() > Input.R){
            count++;
            System.out.println("Generation: " + count);
            for(int i = 0; i < particles.length; i++){
                Particle current = particles[i];
                float[] updatedVelocity = isvalidVelocity(current, updatedVelocity(current));
                current.setVxy(updatedVelocity);
                es = new EngineSimulator(startx, starty, current.getVx(), current.getVy()); // add positions
                es.runRK4ai();
                float[] xy = {es.endPos_X, es.endPos_Y};
                current.setXY(xy);
                current.setScore(calculateEucledianDistance(xy[0], xy[1], holex, holey));//floodfill
                
                if(current.getScore() < current.getlocalBest().getScore()){
                    current.setlocalBest(Particle.clone(current));
                    if(current.getlocalBest().getScore() < globalBest.getScore()){
                        globalBest = Particle.clone(current);
                        System.out.print("GLOBAL best update: ");
                        globalBest.print();
                    }
                }
            }
        }
    }

    /**
     * Method which calculates the eucledian distance
     * @param xt  the x position of the hole
     * @param yt the y position of the hole
     * @param endx the x position of the particle
     * @param endy the y position of the particle
     * @return the eucledian distance
     */
    public static float calculateEucledianDistance(float xt, float yt, float endx, float endy){
        //will become floodfill output
        return (float) Math.sqrt(Math.pow(xt-endx, 2) + Math.pow(yt-endy, 2));
    }


    /**
     * Calculates a random valid velocity
     * @return a velocity
     */
    public static float[] validVelocity(){
        float[] vxy = new float[2];
        vxy[0] = (float)(minVelocity + Math.random()*(maxVelocity-minVelocity));
        vxy[1] = (float)(minVelocity + Math.random()*(maxVelocity-minVelocity));
        if(Physics.magnitude(vxy[0], vxy[1]) > 5){
            vxy[0] = (float) (vxy[0]/Math.sqrt(50));
            vxy[1] = (float) (vxy[1]/Math.sqrt(50));
        }
        return vxy;
    }

    /**
     * method which checks if the velocity is valid
     * @param particle 
     * @param vxy a velocity x and y
     * @return a valid velocity
     */
    public static float[] isvalidVelocity(Particle particle, float[] vxy){
        if(Physics.magnitude(vxy[0], vxy[1]) > 5){
            vxy[0] = (float) (vxy[0]/Math.sqrt(50));
            vxy[1] = (float) (vxy[1]/Math.sqrt(50));
        }
        return vxy;
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

    //makes the particle move in the same direction with the same velocity

    /**
     * method which calculates the inertia
     * @param particle
     * @return inertia for xy
     */
    public static float[] inertia(Particle particle){
        float[] inertia = new float[2];
        float Wx = (float) Math.random();
        float Wy = (float) Math.random();

        inertia[0] = Wx * particle.getVx();
        inertia[1] = Wy * particle.getVy();
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

    public static Particle[] findBestParticles(int amount, Particle[] particles){
        Particle[] fittestParticles = new Particle[amount];
        Particle currentBest = Particle.clone(particles[0]);
        for(int j = 0; j < amount; j++){
            for(int i = 0; i < particles.length; i++){
                if(j == 0){
                        if(particles[i].getScore() <= currentBest.getScore()){
                            currentBest = Particle.clone(particles[i]);
                        }
                }else{
                    if(particles[i].getScore() <= fittestParticles[j-1].getScore()){
                        
                    }
                    else{
                        currentBest = Particle.clone(particles[i]);
                        if(particles[i].getScore() < currentBest.getScore()){
                            currentBest = Particle.clone(particles[i]);
                        }
                    }
                    
                }   
            }
            fittestParticles[j] = Particle.clone(currentBest);
        }
        return fittestParticles; 
    }
    
}

