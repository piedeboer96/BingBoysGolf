package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the Particle swarm optimization algorithm.
 */
public class PSO {

    float maxVelocity = 5;
    float minVelocity = -5;
    int population_size = 0;
    float startX = 0;
    float startY = 0;

    int maxIterations;
    ArrayList<Particle> particles;
    int m;

    int iteration = 1;
    float N = population_size;
    float W = (float) (0.4*((iteration-N)/Math.pow(N, 2)) + 0.4f);
    float c1 = -3*(iteration/N)+3.5f;
    float c2 = 3*(iteration/N)+0.5f;

    Particle globalBest;

    /**
     * Constructor for PSO
     * @param maxIterations maximum numbers of iterations
     * @param population_size
     * @param startX starting x position of the ball
     * @param startY starting y position of the ball
     */
    public PSO(int maxIterations, int population_size, float startX, float startY){
        this.maxIterations = maxIterations;
        this.population_size = population_size;
        this.startX = startX;
        this.startY = startY;
        particles = initializeParticles();
    }

    /**
     * Method which can be called to run the PSO
     */
    public List<Float> runPSO() {
        int count = 0;
        N = particles.size();
        W = 0.72984f;
        c1 = 1f;
        c2 = 2.0f;
        outerloop:
        while (count < maxIterations && globalBest.getFitness() > Input.R) {

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
                W = (float) (0.4 * ((iteration - N) / Math.pow(N, 2)) + 0.4f);
                c1 = -3 * (iteration / N) + 3.5f;
                c2 = 3 * (iteration / N) + 0.5f;
                ParticleThread.closedThreads = 0;

                Particle current = particles.get(i);
                float[] updated = getValidVelocity(updatedVelocity(current));

                threads[i] = new ParticleThread(startX, startY, updated[0], updated[1], current.getlocalBest());
                threads[i].start();
            }
            particles = runThreads(threads);
        }
        ArrayList<Float> toReturn = new ArrayList<>();
        toReturn.add(globalBest.getVx());
        toReturn.add(globalBest.getVy());
        return toReturn;
    }

    /**
     * Method used to create particles using multiple threads.
     * @param threads ParticleThread
     * @return Arraylist of particles
     */
    public ArrayList<Particle> runThreads(ParticleThread[] threads){
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
            if (threads[i].getParticle().getlocalBest().getFitness() > threads[i].getParticle().getFitness()) {
                threads[i].getParticle().setlocalBest(threads[i].getParticle());
                if (threads[i].getParticle().getlocalBest().getFitness() < globalBest.getFitness()) {
                    globalBest = Particle.clone(threads[i].getParticle());
                }
            }
            particleslocal.add(threads[i].getParticle());
        }
        return particleslocal;
    }

    /**
     * method which updates the particle velocity based on the inertia, personal influence and social influence
     * @param particle
     * @return the updated velocity
     */
    public float[] updatedVelocity(Particle particle){
        float[] updatedvxy = new float[2];
        float[] inertia = inertia(particle);
        float[] personalInfluence = personalInfluence(particle);
        float[] socialInfluence = socialInfluence(particle);
        updatedvxy[0] = particle.getVx() + inertia[0] + personalInfluence[0] + socialInfluence[0];
        updatedvxy[1] = particle.getVy() + inertia[1] + personalInfluence[1] + socialInfluence[1];
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
        float vx = p.getVx();
        float vy = p.getVy();
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
            threads[index] = new ParticleThread(startX, startY, f[0], f[1],  p.getlocalBest());
            threads[index].start();
            index++;
        }
        ArrayList<Particle> particle_local = runThreads(threads);
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
     * @param particle
     * @return inertia for xy
     */
    public float[] inertia(Particle particle){
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
    public float[] personalInfluence(Particle particle){
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
    public float[] socialInfluence(Particle particle){
        float[] socialInfluence = new float[2];
        float U2x = (float) Math.random();
        float U2y = (float) Math.random();
        socialInfluence[0] = c2*U2x*(globalBest.getVx()-particle.getVx());
        socialInfluence[1] = c2*U2y*(globalBest.getVy()-particle.getVy());
        return socialInfluence;
    }

    /**
     * Method which initializes the particles
     * @return arraylist of particles
     */
    public ArrayList<Particle> initializeParticles(){
        ArrayList<float[]> init_vel = Score.availableVelocities(startX, startY);
        globalBest = new Particle(new StateVector(startX, startY, 0.1f, 0.1f));
        ArrayList<Particle>particles  = new ArrayList<Particle>();
        for(int i = 0; i < init_vel.size(); i++){
            Particle particle = new Particle(new StateVector(startX, startY, init_vel.get(i)[0], init_vel.get(i)[1]));
            particle.setlocalBest(particle);
            particles.add(particle);
            if(particle.getFitness() < globalBest.getFitness()) {
                globalBest = Particle.clone(particle);
            }
        }
        for(int i = particles.size(); i< population_size; i++){
            float[] velocities = Score.validVelocity(-5.0f, 5.0f, startX, startY);
            particles.add(new Particle(new StateVector(startX, startY, velocities[0], velocities[1])));
            particles.get(i).setlocalBest(particles.get(i));
            if(particles.get(i).fitness < globalBest.getFitness()) {
                globalBest = Particle.clone(particles.get(i));
            }
        }
        return particles;
    }
}
