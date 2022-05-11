package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.math.StateVector;

public class ParticleThread implements Runnable {
    public static int closedThreads = 0;
    Thread thread;
    float x, y, vx, vy;
    int index;
    public boolean hasFound;
    Particle particle, localBest;
    public ParticleThread(float x, float y, float vx, float vy, int index, Particle localBest){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.hasFound = false;
        this.index = index;
        this.localBest = localBest;
    }

    @Override
    public void run() {
        particle = new Particle(new StateVector(this.x, this.y, this.vx, this.vy));
        particle.setlocalBest(this.localBest);
        closedThreads++;
        hasFound = true;
    }

    public void start () {
        if(this.thread == null){
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public int getIndex(){
        return index;
    }

    public Particle getParticle(){
        return this.particle;
    }
}
