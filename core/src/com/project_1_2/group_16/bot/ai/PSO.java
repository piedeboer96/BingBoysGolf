package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.bot.Agent;
import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.bot.AdvancedBot;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class contains the Particle swarm optimization algorithm.
 */
public class PSO extends AdvancedBot {
    private int population_size;

    private final int maxIterations;
    public List<Particle> particles;
    boolean globelbestupdate;

    private int iteration = 1;
    private float N;
    private float W;
    private float c1;
    private float c2;

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
        W = 0.7f;
        c1 = 1.5f;
        c2 = 2f;
        this.globelbestupdate = true;
    }

    /**
     * Runs the PSO-Bot
     * @return the velocity pair
     */
    @Override
    public List<Float> runBot() {
        int count = 0;
        outerloop:
        while (count < maxIterations && globalBest.fitness > Input.R) {
            count++;
            System.out.println("GLOBAL BEST CURREN IT: " + globalBest.toString());
            particles = updateParticles();
            this.iteration++;
            if(globelbestupdate){
                Particle localSearch = doLocalSearch(globalBest);
                if (localSearch.fitness < globalBest.fitness) {
                    globalBest = localSearch;

                }
                globelbestupdate = false;
            }
            if (globalBest.fitness < Input.R) {
                break outerloop;
            }
        }

        ArrayList<Float> toReturn = new ArrayList<>();
        toReturn.add(globalBest.velX);
        toReturn.add(globalBest.velY);

        return toReturn;
    }

    /**
     * Method which updates the particles velocities
     * @return List of particles
     */
    public List<Particle> updateParticles(){
        List<Particle> updated_particles = new ArrayList<>();
        //update velocities
        List<ParticleThread> thread = new ArrayList<>();
        for(int i = 0; i < particles.size(); i++){
            Particle current = particles.get(i);
            float[] updated = getValidVelocity(updatedVelocity(current));
            thread.add(new ParticleThread(new StateVector(getStartX(), getStartY(), updated[0], updated[1]), getGame()));
            thread.get(i).start();
        }
        while (Thread.activeCount() > 1){
        }
        //start all get all the particles from the thread array
        for(int i = 0; i < particles.size(); i++){
            Particle temp = thread.get(i).getParticle();
            if(temp.fitness == Integer.MAX_VALUE){
                globalBest.setlocalBest(globalBest);
                float[] velocity = getValidVelocity(updatedVelocity(globalBest));
                temp = new Particle(new StateVector(getStartX(), getStartY(), velocity[0], velocity[1]), getGame());

            }
            updated_particles.add(temp);
        }

        int index = 0;
        for(Particle particle : updated_particles){
            particle.setlocalBest(particles.get(index).getlocalBest());
            if(particle.fitness < particle.getlocalBest().fitness){
                particle.setlocalBest(particle);
                if(particle.fitness < globalBest.fitness ){
                    globalBest = particle.createClone();
                    globelbestupdate = true;
                }
            }
            index++;
        }
        return updated_particles;

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
        float stepSize = 0.1f;
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
        Particle[] threads = new Particle[neighbourHood.size()];
        int index = 0;
        for(float[] f : neighbourHood){
            f = getValidVelocity(f);
            threads[index] = new Particle(new StateVector(getStartX(), getStartY(), f[0], f[1]), getGame());
            index++;
        }

        for(Particle pt : threads){
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
        if(Float.isNaN(vxvy[0]) || Float.isNaN(vxvy[1])){
            float[] vxy = new float[2];
            vxy[0] = globalBest.velX;
            vxy[1] = globalBest.velY;
            return vxy;
        }
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
        //List<float[]> init_vel = BotHelper.availableVelocities(getStartX(), getStartY());
        globalBest = new Particle(new StateVector(getStartX(), getStartY(), 0.1f, 0.1f), getGame());

        ArrayList<ParticleThread> threadV2s = new ArrayList<>();
        List<Particle> updated_particles = new ArrayList<>();
        for(int i = 0; i < population_size; i++){
            float[] valid_velocity = getValidVelocity(BotHelper.validVelocity(-5f, 5f, getStartX(), getStartY()));
            threadV2s.add(new ParticleThread(new StateVector(getStartX(), getStartY(), valid_velocity[0], valid_velocity[1]), getGame()));
            threadV2s.get(i).start();
        }
        while (Thread.activeCount() > 1){

        }

        //buildeing the initial particles
        int index = 0;
        for(ParticleThread i : threadV2s){
            updated_particles.add(i.getParticle());
            updated_particles.get(index).setlocalBest(i.getParticle());
            if(updated_particles.get(index).fitness < globalBest.fitness){
                globalBest = i.getParticle().createClone();
                globelbestupdate = true;
            }
            index++;
        }

        //sorting the particles
        List<Particle> sorted_updated_particles = new ArrayList<>();
        if(updated_particles.size() > population_size){
            Collections.sort(updated_particles, new AgentComparator());
            for(int i = 0; i < population_size; i++){
                sorted_updated_particles.add(updated_particles.get(i));
            }
            updated_particles = sorted_updated_particles;
        }
        else if(updated_particles.size() == population_size){

        }else{
            particles = updated_particles;
            for(int i = particles.size(); i< population_size; i++){
                float[] velocities = BotHelper.validVelocity(-5.0f, 5.0f, getStartX(), getStartY());
                particles.add(new Particle(new StateVector(getStartX(), getStartY(), velocities[0], velocities[1]), getGame()));
                particles.get(i).setlocalBest(particles.get(i));
                if(particles.get(i).fitness < globalBest.fitness) {
                    globalBest = particles.get(i).createClone();
                    globelbestupdate = true;
                }
            }
        }

        return updated_particles;
    }
}

/**
 * Comparator class used to compare Objects of the type Agent, with respect to fitness value
 */
class AgentComparator implements Comparator<Agent> {

    public int compare(Agent s1, Agent s2)
    {
        if (s1.fitness== s2.fitness)
            return 0;
        else if (s1.fitness > s2.fitness)
            return 1;
        else
            return -1;
    }
}

