package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;

/**
 * This class contains the thread used in PSO, for multithreading purpose.
 */
public class ParticleThread implements Runnable {

    public static int closedThreads = 0;
    Thread thread;
    float x, y, vx, vy;
    public boolean hasFound;
    Particle particle, localBest;
    private final Game game;

    /**
     * Constructor for the particle thread
     * @param x x position of ball
     * @param y y position of ball
     * @param vx velocity x
     * @param vy velocity y
     * @param localBest the current local best particle
     */
    public ParticleThread(float x, float y, float vx, float vy, Particle localBest, Game game){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.game = game;
        this.hasFound = false;
        this.localBest = localBest;
    }

    @Override
    public void run() {
        particle = new Particle(new StateVector(this.x, this.y, this.vx, this.vy), this.game);
        particle.setlocalBest(this.localBest);
        closedThreads++;
        hasFound = true;
    }

    public void start() {
        if(this.thread == null){
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public Particle getParticle() {
        return this.particle;
    }
}
