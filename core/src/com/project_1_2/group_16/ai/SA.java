package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.physics.Physics;

import java.util.*;

public class SA {
    public static final float SOLVER_STEP_SIZE = 0.1f;
    public static final float MAXVEL = 5f;
    public static float hole_x = Input.VT.x;
    public static float hole_y = Input.VT.y;
    private static float start_x = Input.V0.x;
    private static float start_y = Input.V0.y;
    private static int init_velocities = 10;

    private float neigbourStepSize;
    private double Temperature;
    private int kmax;
    private boolean recalculate;
    private Neighbour state;
    private ArrayList<Neighbour> current_neighbours;
    private boolean stop;
    private Neighbour bestState;

    /**
     * Constructor for SA object
     * TODO: NEED TO WORK ON IT SO IT TAKES ITS STARTING POSITIONS AS A PARAMETER
     * @param kmax max iterations
     * @param neigbourStepSize step size for generating neighbours
     */
    public SA(int kmax, float neigbourStepSize){
        this.kmax = kmax;
        this.neigbourStepSize = neigbourStepSize;
        this.recalculate = true;
        setState(findInitalState(init_velocities));

        stop = false;
    }

    /**
     * Method used for running the SA
     * @return array of x and y velocities
     */
    public List<Float> runSA(){
        if(bestState.getFitness() > Input.R) {
            outerloop:
            for (int i = 0; i < kmax; i++) {
                Temperature = getTemperature(i);
                Neighbour randomNeighbour = getNeighbour(state);
                if (randomNeighbour.getFitness() < Input.R) {
                    bestState = randomNeighbour;
                    setState(randomNeighbour);
                    break outerloop;
                }
                double cost = state.getFitness() - randomNeighbour.getFitness();
                if (cost >= 0) {
                    setState(randomNeighbour);
                    System.out.println("Fitness: " + state.getFitness() + " vx: " + state.getVx() + " vy: "+ state.getVy());
                } else {
                    if (Math.random() < getProbability(state, randomNeighbour)) {
                        setState(randomNeighbour);
                        System.out.println("Fitness: " + state.getFitness() + " vx: " + state.getVx() + " vy: " + state.getVy());
                    }
                }
                if (state.getFitness() < Input.R) {
                    bestState = state;
                    break outerloop;
                }
                if(state.getFitness() < bestState.getFitness()){
                    bestState = new Neighbour(state);
                }
            }
        }else {
            System.out.println("found on first hit!");
        }
        ArrayList<Float> vxvy = new ArrayList<>();
        vxvy.add(bestState.getVx());
        vxvy.add(bestState.getVy());
        return vxvy;
    }


    /**
     * Method which generates random vectors and picks the best vector which is used at the start of SA
     * @param init_vector_amount initial amount of vector
     * @return the vector with highest fitness
     */
    public Neighbour findInitalState(int init_vector_amount){
        ArrayList<float[]> initialCandidates = Score.availableVelocities();
        Neighbour bestNeighbour = null;
        double bestFitness = Integer.MAX_VALUE;
        for(int i=0; i<initialCandidates.size(); i++){
            Neighbour temp = new Neighbour(new StateVector(Input.V0.x, Input.V0.y, initialCandidates.get(i)[0], initialCandidates.get(i)[1]));
            if(temp.getFitness() < bestFitness){
                if(temp.getFitness() < Input.R * 3.15f){
                    this.bestState = new Neighbour(temp);
                    return temp;
                }
                bestFitness = temp.getFitness();
                bestNeighbour = temp;
            }
        }
        System.out.println("yeahyeahyeah");
        System.out.println("here is " + bestNeighbour.getVx() + " " + bestNeighbour.getVy());
//        float[] velocities = Score.bestVelocity();
//        bestNeighbour = new Neighbour(new StateVector(Input.V0.x, Input.V0.y, velocities[0], velocities[1]));
//        System.out.println(Arrays.toString(velocities));
        this.bestState = new Neighbour(bestNeighbour);
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
     * Method which creates a valid velocity
     * @return a valid velocity
     */
    public static float[] validVelocity(){
        float[] vxy = new float[2];
        vxy[0] = (float)(0 + Math.random()*(MAXVEL));
        vxy[1] = (float)(0 + Math.random()*(MAXVEL));
        if(Physics.magnitude(vxy[0], vxy[1]) > 5 ){
            vxy[0] = (float) (vxy[0]/Math.sqrt(50));
            vxy[1] = (float) (vxy[1]/Math.sqrt(50));
        }
        return vxy;
    }

    /**
     * Method used to create neighbours from the current state based on a neigbourStepSize
     * @param state the current state
     * @return a random neigbour
     */
    private Neighbour getNeighbour(Neighbour state) {
        Neighbour toReturn = null;
        ArrayList<StateVector> newVectors = null;
//        neigbourStepSize = (float) Math.random();
        if (this.recalculate) {
            current_neighbours = new ArrayList<>();
            newVectors = new ArrayList<>();
            float vx = state.getVx();
            float vy = state.getVy();
            newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx + neigbourStepSize, vy));
            newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx - neigbourStepSize, vy));
            newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx, vy + neigbourStepSize));
            newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx, vy - neigbourStepSize));
            newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx - neigbourStepSize, vy - neigbourStepSize));
            newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx + neigbourStepSize, vy - neigbourStepSize));
            newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx - neigbourStepSize, vy + neigbourStepSize));

            ArrayList<StateVector> viableVectors = new ArrayList<>();
            for (int i = 0; i < newVectors.size(); i++) {
                if (newVectors.get(i).vx >= 0.01f && newVectors.get(i).vy >= 0.01f && viableVector(newVectors.get(i))) {
                    viableVectors.add(newVectors.get(i));
                }
            }

            for (int i = 0; i < viableVectors.size(); i++) {
                Neighbour neighbour = new Neighbour(viableVectors.get(i));
                if (neighbour.getFitness() < state.getFitness()) {
                    if (neighbour.getFitness() < Input.R) {
                        return neighbour;
                    }
                    current_neighbours.add(neighbour);
                }
            }
        }
        return ((current_neighbours.size() <= 0) ? new Neighbour(newVectors.get((int) Math.random() * newVectors.size())) : current_neighbours.get((int) Math.random() * current_neighbours.size()));
    }


    /**
     *
     * @return
     */
    public Neighbour getState() {
        return state;
    }

    /**
     *
     * @param updated_state
     */
    private void setState(Neighbour updated_state){
        this.state = new Neighbour(updated_state);
    }

    /**
     *
     * @param state
     * @param updated
     * @return
     */
    private float getProbability(Neighbour state, Neighbour updated) {
        return (float)Math.exp(-1*(state.getFitness() - updated.getFitness()) / Temperature);
    }

    /**
     * @param k
     * @return
     */
    public double getTemperature(int k) {
        return 1 - (k+1)/kmax;
    }

    public static void main(String[] args) {
//        initTreesForTesting();
        long start = System.currentTimeMillis();
        SA test = new SA(1000, 0.2f);
        System.out.println("best is " + test.runSA());
        System.out.println(test.bestState.getFitness());
        long end = System.currentTimeMillis();
        System.out.println("Runtime: " + (end - start) + " ms");
        System.out.println("amount of simulations taken " + Game.simulCounter);
    }
}
