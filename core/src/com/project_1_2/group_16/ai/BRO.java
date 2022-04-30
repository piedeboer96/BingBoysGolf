package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;

import java.util.ArrayList;

//
public class BRO {
    public int popSize;
    public int maxIter;
    public Soldier bestSoldier;
    boolean stop = false;
    int threshold;
    ArrayList<Soldier> population = new ArrayList<Soldier>();
    public BRO(int popSize, int maxIter, int threshold){
        this.popSize = popSize;
        this.maxIter = maxIter;
        this.threshold = threshold;
    }

    /**
     * Driver method for BRO algorithm
     */
    public void runBRO(){
        float upperBoundX, upperBoundY, lowerBoundX, lowerBoundY, ogUBx, ogLBx, ogUBy, ogLBy;
        initializePopulation();
        bestSoldier = findBestSoldierInPop();
        float sdX = calcSDVelX();
        float sdY = calcSDVelY();
        upperBoundX = bestSoldier.velX + sdX; ogUBx = upperBoundX;
        upperBoundY = bestSoldier.velY + sdY; ogUBy = upperBoundY;
        lowerBoundX = bestSoldier.velX - sdX; ogLBx = lowerBoundX;
        lowerBoundY = bestSoldier.velY - sdY; ogLBy = lowerBoundY;
        float shrink = (float) (Math.ceil(Math.log10(maxIter)));
        float delta = (Math.round(maxIter/shrink));
        int iter = 0;
        outerloop:
        while(iter<maxIter){
            System.out.println(iter++);
            bestSoldier = findBestSoldierInPop();
            System.out.println(bestSoldier.toString());
            for(int i=0; i<population.size(); i++){
                Soldier s = population.get(i);
                Soldier nearest = findNearestSoldier(s);
                Soldier vic, dam;
                if(s.fitness < nearest.fitness){
                    vic = s;
                    dam = nearest;
                }else {
                    dam = s;
                    vic = nearest;
                }
                if(dam.damageCounter < threshold){
                    dam.velX += Math.random() * (bestSoldier.velX - dam.velX);
                    dam.velY += Math.random() * (bestSoldier.velY - dam.velY);
                    dam.damageCounter++;
                    vic.damageCounter = 0;
                }
                else {
                    dam.velX = (float) (Math.random() * (upperBoundX - lowerBoundX) + lowerBoundX);
                    dam.velY = (float) (Math.random() * (upperBoundY - lowerBoundY) + lowerBoundY);
                    dam.damageCounter = 0;
                }
                dam.calcFitness();
                if(dam.fitness < Input.R){
                    break outerloop;
                }
            }

            if(iter>=delta){
                sdX = calcSDVelX();
                sdY = calcSDVelY();
                upperBoundX = bestSoldier.velX + sdX;
                upperBoundY = bestSoldier.velY + sdY;
                lowerBoundX = bestSoldier.velX - sdX;
                lowerBoundY = bestSoldier.velY - sdY;
                delta += Math.round(delta/2);
            }
            if(upperBoundX>ogUBx){
                upperBoundX = ogUBx;
            }else if (upperBoundY>ogUBy){
                upperBoundY = ogUBy;
            }else if (lowerBoundX>ogLBx){
                lowerBoundX = ogLBx;
            }else if(lowerBoundY>ogLBy){
                lowerBoundY = ogLBy;
            }
        }
        bestSoldier = findBestSoldierInPop();
        System.out.println(bestSoldier.toString());
    }

    /**
     * Method to initialize population for BRO algorithm
     */
    public void initializePopulation(){
        ArrayList<float[]> temp = Score.availableVelocities();
        for(float[] f : temp){
            population.add(new Soldier(f[0], f[1]));
        }
//        for(int i=0; i<popSize; i++){
//            float[] f = Score.validVelocity(-5f, 5f);
//            population.add(new Soldier(f[0], f[1]));
//        }
    }

    /**
     * For the given soldier, find the nearest soldier based on euclidian distances of velX and velY
     * @param soldier the given soldier in which the nearest soldier is to be found
     * @return the nearest soldier
     */
    public Soldier findNearestSoldier(Soldier soldier){
        float distance = Integer.MAX_VALUE;
        Soldier nearestSoldier = new Soldier(0,0);
        for(Soldier s : population){
            float curDistance = Score.calculateEucledianDistance(soldier.velX, soldier.velY, s.velX, s.velY);
            if(curDistance < distance){
                nearestSoldier = s;
                distance = curDistance;
            }
        }
        return nearestSoldier;
    }

    /**
     * Finds best soldier in population based on the fitness of all the soldiers in the population
     * @return best soldier
     */
    public Soldier findBestSoldierInPop(){
        Soldier toReturn = new Soldier(0, 0);
        float bestFitness = Integer.MAX_VALUE;
        for(Soldier s : population){
            if(s.fitness < bestFitness){
                bestFitness = s.fitness;
                toReturn = s;
            }
        }
        return toReturn;
    }

    /**
     * Calculates the Standard Deviation of all x velocities
     * @return the SD of all x velocities
     */
    public float calcSDVelX (){
        float [] xVels = new float[population.size()];
        //put all x velocities in an array
        for(int i = 0; i<population.size(); i++){
            xVels[i] = population.get(i).velX;
        }
        float mean = 0;
        //calculate mean
        for(int i = 0; i<xVels.length; i++){
            mean += xVels[i];
        }
        mean = mean/xVels.length;

        //calculate sd
        float sd = 0;
        for(int i=0; i<xVels.length; i++){
            sd += Math.pow(xVels[i] - mean, 2);
        }
        sd = sd/xVels.length-1;
        return (float) (Math.sqrt(sd));
    }

    /**
      Calculates the Standard Deviation of all y velocities
     * @return the SD of all y velocities
     */
    public float calcSDVelY (){
        float [] yVels = new float[population.size()];
        //put all x velocities in an array
        for(int i = 0; i<population.size(); i++){
            yVels[i] = population.get(i).velY;
        }
        float mean = 0;
        //calculate mean
        for(int i = 0; i<yVels.length; i++){
            mean += yVels[i];
        }
        mean = mean/yVels.length;

        //calculate sd
        float sd = 0;
        for(int i=0; i<yVels.length; i++){
            sd += Math.pow(yVels[i] - mean, 2);
        }
        sd = sd/yVels.length-1;
        return (float) (Math.sqrt(sd));
    }
    public static void main (String[] args){
        System.out.println("starting...");
        BRO bro = new BRO(55, 100, 4);
        bro.runBRO();
    }

}
