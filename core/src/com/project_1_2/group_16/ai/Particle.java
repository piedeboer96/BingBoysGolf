package com.project_1_2.group_16.ai;

import java.util.Arrays;

public class Particle {
    private float[] vxy;
    private float[] xy;
    private float score = Integer.MAX_VALUE;
    private boolean scoreSet = false;
    private boolean xySet = false;
    private boolean bestSet = false;

    private Particle localBest;

    public Particle(float vx, float vy){
        this.vxy = new float[2];
        this.vxy[0] = vx;
        this.vxy[1] = vy;
    }

    public void setVxy(float[] vxy){
        this.vxy = vxy;
        this.xy = new float[2];
    }

    public void setXY(float[] xy){
        xySet = true;
        this.xy = xy;
    }

    public void setScore(float score){
        scoreSet = true;
        this.score = score;
    }

    public float getVx(){
        return vxy[0];
    }

    public float getVy(){
        return vxy[1];
    }

    public float getX(){
        return xy[0];
    }

    public float getY(){
        return xy[1];
    }

    public float getScore(){
        return score;
    }

    public boolean isScoreSet(){
        return scoreSet;
    }

    public boolean isXYSet(){
        return xySet;
    }

    public void setlocalBest(Particle best){
        localBest = best;
    }

    public Particle getlocalBest(){
        return localBest;
    }

    public static Particle clone(Particle particle){
        Particle clone = new Particle(particle.getVx(), particle.getVy());
        float[] clonexy = new float[2];
        clonexy[0] = particle.getX();
        clonexy[1] = particle.getY();
        clone.setXY(clonexy);
        clone.setScore(particle.getScore());
        return clone;
    }

    public void print(){
        System.out.println("current vx: " + this.vxy[0] + " current vy: " + this.vxy[1] + " current score: " + this.getScore() + " x,y: " + Arrays.toString(xy));
        //System.out.println("local best --> " + " current vx: " + localBest.vxy[0] + " current vy: " + localBest.vxy[1] + " current score: " + localBest.getScore() + " x,y: " + localBest.getX() + "," + localBest.getY()); 
    }
}


