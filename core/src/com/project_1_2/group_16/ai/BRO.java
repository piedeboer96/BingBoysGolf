package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.Physics;
import com.project_1_2.group_16.math.StateVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Battle-Royale optimization algorithm to find a hole-in-one. BRO is an evolutionary-algorithm inspired by
 * battle-royale games which describes its agents as soldiers.
 */
public class BRO {

    public int popSize;
    public float startY;
    public float startX;
    public int maxIter;
    public Soldier bestSoldier;
    int threshold;
    ArrayList<Soldier> population = new ArrayList<Soldier>();
    ArrayList<Soldier> initialPop = new ArrayList<>();

    /**
     * Constructor for the improved BRO AI (BRO-HC)
     * @param popSize Its chosen population size
     * @param maxIter The max iterations it will run
     * @param threshold The preferred threshold for which a player is damaged then it respawns
     */
    public BRO(int popSize, int maxIter, int threshold, float startX, float startY){
        this.popSize = popSize;
        this.maxIter = maxIter;
        this.threshold = threshold;
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * Driver method for BRO algorithm
     */
    public List<Float> runBRO(){
        //Initialize variables and the population
        float upperBoundX, upperBoundY, lowerBoundX, lowerBoundY, ogUBx, ogLBx, ogUBy, ogLBy;
        initializePopulation();
        bestSoldier = findBestSoldierInPop();
        if(bestSoldier.fitness < Input.R){
            //System.out.println(bestSoldier.toString());
            ArrayList<Float> toReturn = new ArrayList<>();
            toReturn.add(bestSoldier.velX); toReturn.add(bestSoldier.velY);
            return toReturn;
        }
        float sdX = calcSDVelX();
        float sdY = calcSDVelY();
        upperBoundX = bestSoldier.velX + sdX; ogUBx = upperBoundX;
        upperBoundY = bestSoldier.velY + sdY; ogUBy = upperBoundY;
        lowerBoundX = bestSoldier.velX - sdX; ogLBx = lowerBoundX;
        lowerBoundY = bestSoldier.velY - sdY; ogLBy = lowerBoundY;
        float shrink = (float) (Math.ceil(Math.log10(maxIter)));
        float delta = (Math.round(maxIter/shrink));
        int iter = 0;
        //Main loop to find the solution
        outerloop:
        while(iter<maxIter){
            iter++;
            if(iter==51){
                break outerloop;
            }
            Soldier possibleBest = findBestSoldierInPop();
            //Do local search of the current best soldier
            if(possibleBest.fitness < bestSoldier.fitness){
                bestSoldier = possibleBest;
                if(possibleBest.fitness < Input.R){
                    break outerloop;
                }
            }
            Soldier temp = doLocalSearch(bestSoldier);
            if(temp.fitness < bestSoldier.fitness){
                bestSoldier = new Soldier(temp);
                population.add(bestSoldier);
                population.remove(findWorstSoldierInPop());
            }
            if(bestSoldier.fitness<Input.R){
                break outerloop;
            }
            //If solution not found in the local search, update all player's positions using BRO algorithm
            for(int i=0; i<population.size(); i++){
                Soldier s = population.get(i);
                if(s==bestSoldier){
                    continue;
                }
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
                    dam.velX = 6;
                    while(Physics.magnitude(dam.velX, dam.velY) > 5) {
                        dam.velX = (float) (Math.random() * (upperBoundX - lowerBoundX) + lowerBoundX);
                        dam.velY = (float) (Math.random() * (upperBoundY - lowerBoundY) + lowerBoundY);
                    }
                    dam.damageCounter = 0;
                    vic.damageCounter = 0;
                }
                dam.calcFitness();
                if(dam.fitness < Input.R){
                    bestSoldier = new Soldier(dam);
                    break outerloop;
                }
            }
            //Update upper and lower bound
            if(iter>=delta) {
                sdX = calcSDVelX();
                sdY = calcSDVelY();
                upperBoundX = bestSoldier.velX + sdX;
                upperBoundY = bestSoldier.velY + sdY;
                lowerBoundX = bestSoldier.velX - sdX;
                lowerBoundY = bestSoldier.velY - sdY;
                delta += Math.round(delta / 2);

                if (upperBoundX > ogUBx) {
                    upperBoundX = ogUBx;
                } else if (upperBoundY > ogUBy) {
                    upperBoundY = ogUBy;
                } else if (lowerBoundX > ogLBx) {
                    lowerBoundX = ogLBx;
                } else if (lowerBoundY > ogLBy) {
                    lowerBoundY = ogLBy;
                }
            }
        }
        //System.out.println("best is " + bestSoldier.toString());
        ArrayList<Float> toReturn = new ArrayList<>();
        toReturn.add(bestSoldier.velX); toReturn.add(bestSoldier.velY);
        return toReturn;
    }

    /**
     * Method to initiate a type of local search for the best soldier in the population
     * @param s The best Soldier in the population
     * @return
     */
    public Soldier doLocalSearch(Soldier s){
        ArrayList<float[]> neighbourHood = new ArrayList<float[]>();
        Soldier toReturn = s;
        float stepSize = 0.2f;
        float vx = s.velX;
        float vy = s.velY;
        neighbourHood.add(new float[] {vx+stepSize, vy});
        neighbourHood.add(new float[] {vx-stepSize, vy});
        neighbourHood.add(new float[] {vx, vy+stepSize});
        neighbourHood.add(new float[] {vx, vy-stepSize});
        neighbourHood.add(new float[] {vx+stepSize, vy+stepSize});
        neighbourHood.add(new float[] {vx+stepSize, vy-stepSize});
        neighbourHood.add(new float[] {vx-stepSize, vy+stepSize});
        neighbourHood.add(new float[] {vx-stepSize, vy-stepSize});

        float bestFitness = Integer.MAX_VALUE;
        for(float[] f : neighbourHood){
            Game g = new Game();
            g.setNumericalSolver(NumericalSolver.RK4);
            StateVector sv = new StateVector(startX, startY, f[0], f[1]);
            Soldier sold = new Soldier(f[0], f[1], startX, startY);
            g.runEngine(sv, null, null, sold);
            if(sold.fitness < Input.R * 3.15f){
                return sold;
            }
            if(sold.fitness < bestFitness){
                bestFitness = sold.fitness;
                toReturn = sold;
            }
        }
        return toReturn;
    }

    /**
     * Method to initialize population for BRO algorithm
     */
    public void initializePopulation(){
        List<float[]> temp = AIHelper.availableVelocities(startX, startY);
        for(float[] f : temp){
            population.add(new Soldier(f[0], f[1], startX, startY));
        }
        for(int i=population.size(); i<popSize; i++){
            float[] f = AIHelper.validVelocity(-5f, 5f, startX, startY);
            population.add(new Soldier(f[0], f[1], startX, startY));
        }
        for(Soldier s : population){
            initialPop.add(s);
        }
    }

    /**
     * For the given soldier, find the nearest soldier based on euclidian distances of velX and velY
     * @param soldier the given soldier in which the nearest soldier is to be found
     * @return the nearest soldier
     */
    public Soldier findNearestSoldier(Soldier soldier){
        float distance = Integer.MAX_VALUE;
        Soldier nearestSoldier = soldier;
        for(Soldier s : population){
            if(s==soldier){
                continue;
            }
            float curDistance = AIHelper.calculateEucledianDistance(soldier.velX, soldier.velY, s.velX, s.velY);
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
        Soldier toReturn = null;
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
     * Finds worst soldier in population based on the fitness of all the soldiers in the population
     * @return worse soldier
     */
    public Soldier findWorstSoldierInPop(){
        Soldier toReturn = null;
        float bestFitness = Integer.MIN_VALUE;
        for(Soldier s : population){
            if(s.fitness > bestFitness){
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
}
