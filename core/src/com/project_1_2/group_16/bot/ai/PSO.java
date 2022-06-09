package com.project_1_2.group_16.bot.ai;

import com.badlogic.gdx.files.FileHandle;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.bot.AdvancedBot;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Spline;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.misc.LevelDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the Particle swarm optimization algorithm.
 */
public class PSO extends AdvancedBot {
    private int population_size;

    private final int maxIterations;
    public List<Particle> particles;

    private int iteration = 1;
    private final float N = population_size;
    private final float W = (float) (0.4*((iteration-N)/Math.pow(N, 2)) + 0.4f);
    private final float c1 = -3*(iteration/N)+3.5f;
    private final float c2 = 3*(iteration/N)+0.5f;

    private Particle globalBest;

    /**
     * Constructor for PSO
     * @param maxIterations maximum numbers of iterations
     * @param population_size
     * @param startX starting x position of the ball
     * @param startY starting y position of the ball
     */
    public PSO(int maxIterations, int population_size, float startX, float startY, Game game, boolean useFloodFill){
        super(startX, startY, game, useFloodFill);
        this.maxIterations = maxIterations;
        this.population_size = population_size;
        particles = initializeParticles();
    }


    @Override
    public List<Float> runBot() {
        int count = 0;
        outerloop:
        while (count < maxIterations && globalBest.fitness > Input.R) {
            count++;

            Particle localSearch = doLocalSearch(globalBest);
            if (localSearch.fitness < globalBest.fitness) {
                globalBest = localSearch;
            }
            if (globalBest.fitness < Input.R) {
                break outerloop;
            }
            ParticleThread[] threads = new ParticleThread[particles.size()];
            for (int i = 0; i < particles.size(); i++) {
                iteration = i + 1;
                ParticleThread.closedThreads = 0;

                Particle current = particles.get(i);
                float[] updated = getValidVelocity(updatedVelocity(current));

                threads[i] = new ParticleThread(getStartX(), getStartY(), updated[0], updated[1], current.getlocalBest(), getGame());
                threads[i].start();
            }
            particles = runThreads(threads);
        }
        ArrayList<Float> toReturn = new ArrayList<>();

        toReturn.add(globalBest.velX);
        toReturn.add(globalBest.velY);

        return toReturn;
    }

    /**
     * Method used to create particles using multiple threads.
     * @param threads ParticleThread
     * @return Arraylist of particles
     */
    public List<Particle> runThreads(ParticleThread[] threads){
        ArrayList<Particle> particleslocal = new ArrayList<>();
        boolean stop = false;
        while(!stop){
            int count = 0;
            for(int i = 0; i < threads.length; i++){
                if(threads[i].hasFound){
                    count++;
                }
            }
            if(count >= threads.length){
                stop = true;
            }
        }
        for(int i = 0; i < threads.length; i++){
            if (threads[i].getParticle().getlocalBest().fitness > threads[i].getParticle().fitness) {
                threads[i].getParticle().setlocalBest(threads[i].getParticle());
                if (threads[i].getParticle().getlocalBest().fitness < globalBest.fitness) {
                    globalBest = threads[i].getParticle().createClone();
                }
            }
            particleslocal.add(threads[i].getParticle());
        }
        return particleslocal;
    }

    /**
     * method which updates the particle velocity based on the inertia, personal influence and social influence
     * @param particle The Particle in which the velocity is updated for
     * @return the updated velocity
     */
    public float[] updatedVelocity(Particle particle){
        float[] updatedvxy = new float[2];
        float[] inertia = inertia(particle);
        float[] personalInfluence = personalInfluence(particle);
        float[] socialInfluence = socialInfluence(particle);
        updatedvxy[0] = particle.velX + inertia[0] + personalInfluence[0] + socialInfluence[0];
        updatedvxy[1] = particle.velY + inertia[1] + personalInfluence[1] + socialInfluence[1];
        return updatedvxy;
    }

    /**
     * Method which does a local search for a particle
     * @param p particle
     * @return the best local particle
     */
    public Particle doLocalSearch(Particle p){
        ArrayList<float[]> neighbourHood = new ArrayList<float[]>();
        if(p.getlocalBest() == null){
            p.setlocalBest(p);
        }
        Particle toReturn = p;
        float stepSize = 0.2f;
        float vx = p.velX;
        float vy = p.velY;
        neighbourHood.add(new float[] {vx+stepSize, vy});
        neighbourHood.add(new float[] {vx-stepSize, vy});
        neighbourHood.add(new float[] {vx, vy+stepSize});
        neighbourHood.add(new float[] {vx, vy-stepSize});
        neighbourHood.add(new float[] {vx+stepSize, vy+stepSize});
        neighbourHood.add(new float[] {vx+stepSize, vy-stepSize});
        neighbourHood.add(new float[] {vx-stepSize, vy+stepSize});
        neighbourHood.add(new float[] {vx-stepSize, vy-stepSize});

        double bestFitness = Integer.MAX_VALUE;
        ParticleThread[] threads = new ParticleThread[neighbourHood.size()];
        int index = 0;
        for(float[] f : neighbourHood){
            threads[index] = new ParticleThread(getStartX(), getStartY(), f[0], f[1],  p.getlocalBest(), getGame());
            threads[index].start();
            index++;
        }
        List<Particle> particle_local = runThreads(threads);
        for(Particle pt : particle_local){
            if(pt.fitness < Input.R * 3.15f){
                return pt;
            }
            if(p.fitness < bestFitness){
                toReturn = pt;
                bestFitness = pt.fitness;
            }
        }
        return toReturn;
    }

    /**
     * method which checks if the velocity is valid
     * @param vxvy
     * @return a valid velocity
     */
    public float[] getValidVelocity(float[] vxvy){
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
     * @param particle the particle into which to calculate the inertia (personal weight/W) for
     * @return inertia for xy
     */
    public float[] inertia(Particle particle){
        float[] inertia = new float[2];
        inertia[0] = W * particle.velX;
        inertia[1] = W * particle.velY;
        return inertia;
    }

    /**
     * method which calculates the personal influence
     * @param particle the particle to which to calculate the personal influence
     * @return personal influence for xy
     */
    public float[] personalInfluence(Particle particle){
        float[] personalInfluenceXY = new float[2];
        float U1x = (float) Math.random();
        float U1y = (float) Math.random();
        personalInfluenceXY[0] = c1*U1x*(particle.getlocalBest().velX-particle.velX);
        personalInfluenceXY[1] = c1*U1y*(particle.getlocalBest().velY-particle.velY);
        return personalInfluenceXY;
    }

    /**
     * method which calculates the social influence
     * @param particle the particle to which to calculate the social influence for
     * @return social influence for xy
     */
    public float[] socialInfluence(Particle particle){
        float[] socialInfluence = new float[2];
        float U2x = (float) Math.random();
        float U2y = (float) Math.random();
        socialInfluence[0] = c2*U2x*(globalBest.velX-particle.velX);
        socialInfluence[1] = c2*U2y*(globalBest.velY-particle.velY);
        return socialInfluence;
    }

    /**
     * Method which initializes the particles
     * @return arraylist of particles
     */
    public List<Particle> initializeParticles(){
        List<float[]> init_vel = BotHelper.availableVelocities(getStartX(), getStartY());
        globalBest = new Particle(new StateVector(getStartX(), getStartY(), 0.1f, 0.1f), getGame());
        ArrayList<Particle>particles  = new ArrayList<Particle>();
        for(int i = 0; i < init_vel.size(); i++){
            Particle particle = new Particle(new StateVector(getStartX(), getStartY(), init_vel.get(i)[0], init_vel.get(i)[1]), getGame());
            particle.setlocalBest(particle);
            particles.add(particle);
            if(particle.fitness < globalBest.fitness) {
                globalBest = particle.createClone();
            }
        }
        for(int i = particles.size(); i< population_size; i++){
            float[] velocities = BotHelper.validVelocity(-5.0f, 5.0f, getStartX(), getStartY());
            particles.add(new Particle(new StateVector(getStartX(), getStartY(), velocities[0], velocities[1]), getGame()));
            particles.get(i).setlocalBest(particles.get(i));
            if(particles.get(i).fitness < globalBest.fitness) {
                globalBest = particles.get(i).createClone();
            }
        }
        return particles;
    }
}
