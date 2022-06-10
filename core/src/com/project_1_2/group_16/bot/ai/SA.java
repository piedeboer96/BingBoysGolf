package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.bot.BotHelper;
import com.project_1_2.group_16.bot.AdvancedBot;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.math.StateVector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains our Simulated annealing optimazation algorithm.
 */
public class SA extends AdvancedBot {

    public static final float MAXVEL = 5f;
    private float neigbourStepSize;
    private double Temperature;
    private int kmax;
    private boolean recalculate;
    private Neighbour state;
    private ArrayList<Neighbour> current_neighbours;
    private Neighbour bestState;

    /**
     * Constructor for SA object
     * @param kmax max iterations
     * @param neigbourStepSize step size for generating neighbours
     */
    public SA(int kmax, float neigbourStepSize, float startX, float startY, Game game, boolean useFloodFill){
        super(startX, startY, game, useFloodFill);
        this.kmax = kmax;
        this.neigbourStepSize = neigbourStepSize;
        this.recalculate = true;
        setState(findInitalState());
    }
    @Override
    public List<Float> runBot() {
        if(bestState.fitness > Input.R) {
            outerloop:
            for (int i = 0; i < kmax; i++) {
                Temperature = getTemperature(i);
                Neighbour randomNeighbour = getNeighbour(state);
                if (randomNeighbour.fitness < Input.R) {
                    bestState = randomNeighbour;
                    setState(randomNeighbour);
                    break outerloop;
                }
                double cost = state.fitness - randomNeighbour.fitness;
                if (cost >= 0) {
                    setState(randomNeighbour);
                } else {
                    if (Math.random() < getProbability(state, randomNeighbour)) {
                        setState(randomNeighbour);
                    }
                }
                if (state.fitness < Input.R) {
                    bestState = state;
                    break outerloop;
                }
                if(state.fitness < bestState.fitness){
                    bestState = state.createClone();
                }
            }
        }
        ArrayList<Float> toReturn = new ArrayList<>();

        toReturn.add(bestState.getVx());
        toReturn.add(bestState.getVy());

        return toReturn;
    }

    /**
     * Method which generates random vectors and picks the best vector which is used at the start of SA
     * @return the vector with highest fitness
     */
    public Neighbour findInitalState(){
        List<float[]> initialCandidates = BotHelper.availableVelocities(getStartX(), getStartY());
        Neighbour bestNeighbour = null;
        double bestFitness = Integer.MAX_VALUE;
        for(int i=0; i<initialCandidates.size(); i++){
            Neighbour temp = new Neighbour(new StateVector(getStartX(), getStartY(), initialCandidates.get(i)[0], initialCandidates.get(i)[1]), getGame());
            if(temp.fitness < bestFitness){
                if(temp.fitness < Input.R * 3.15f){
                    this.bestState = temp.createClone();
                    return temp;
                }
                bestFitness = temp.fitness;
                bestNeighbour = temp;
            }
        }
        this.bestState = bestNeighbour.createClone();
        return bestNeighbour;
    }

    /**
     * Method which checks if the resulting vector is viable
     * @param sv a statevector
     * @return
     */
    private static boolean viableVector(StateVector sv){
        if((float)Math.sqrt(sv.vx*sv.vx+sv.vy+sv.vy) <= MAXVEL){
            return true;
        }
        return  false;
    }

    /**
     * Method used to create neighbours from the current state based on a neigbourStepSize
     * @param state the current state
     * @return a random neigbour
     */
    private Neighbour getNeighbour(Neighbour state) {
        ArrayList<StateVector> newVectors = null;
        ArrayList<StateVector> viableVectors = null;
        if (this.recalculate) {
            current_neighbours = new ArrayList<>();
            newVectors = new ArrayList<>();
            float vx = state.getVx();
            float vy = state.getVy();
            newVectors.add(new StateVector(getStartX(), getStartY(), vx + neigbourStepSize, vy));
            newVectors.add(new StateVector(getStartX(), getStartY(), vx - neigbourStepSize, vy));
            newVectors.add(new StateVector(getStartX(), getStartY(), vx, vy + neigbourStepSize));
            newVectors.add(new StateVector(getStartX(), getStartY(), vx, vy - neigbourStepSize));
            newVectors.add(new StateVector(getStartX(), getStartY(), vx - neigbourStepSize, vy - neigbourStepSize));
            newVectors.add(new StateVector(getStartX(), getStartY(), vx + neigbourStepSize, vy - neigbourStepSize));
            newVectors.add(new StateVector(getStartX(), getStartY(), vx - neigbourStepSize, vy + neigbourStepSize));

            viableVectors = new ArrayList<>();
            for (int i = 0; i < newVectors.size(); i++) {
                if (viableVector(newVectors.get(i))) {
                    viableVectors.add(newVectors.get(i));
                }
            }
            for (int i = 0; i < viableVectors.size(); i++) {
                Neighbour neighbour = new Neighbour(viableVectors.get(i), this.getGame());
                if (neighbour.fitness < Input.R){
                    return neighbour;
                }
                current_neighbours.add(neighbour);
            }
        }
        return ((current_neighbours.size() <= 0) ? new Neighbour(newVectors.get((int) Math.random() * newVectors.size()), getGame()) : current_neighbours.get((int) Math.random() * current_neighbours.size()));
    }

    public Neighbour getState() {
        return state;
    }

    private void setState(Neighbour updated_state){
        this.state = updated_state.createClone();
    }

    /**
     * Method which calculates the probability based on the current state and the update neighbour
     * @param state current state
     * @param updated update neighbour
     * @return the probability
     */
    private float getProbability(Neighbour state, Neighbour updated) {
        return (float)Math.exp(-1*(state.fitness - updated.fitness) / Temperature);
    }

    /**
     * The temperature decreases from an initial positive value to zero. Simulating the annealing proces.
     * @param k the current iteration
     * @return the current temprature
     */
    public double getTemperature(int k) {
        return 1 - (k+1)/kmax;
    }


}
