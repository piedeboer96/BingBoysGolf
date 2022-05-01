package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.physics.Physics;

import javax.swing.plaf.nimbus.State;
import java.lang.reflect.Array;
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

    /**
     * Constructor for SA object
     * @param kmax max iterations
     * @param neigbourStepSize step size for generating neighbours
     */
    public SA(int kmax, float neigbourStepSize){
        this.kmax = kmax;
        this.Temperature = getTemperature(kmax);
        this.neigbourStepSize = neigbourStepSize;
        this.recalculate = true;
        setState(findInitalState(init_velocities));
        stop = false;
    }

    /**
     * Method used for runing the SA
     * @return array of x and y velocities
     */
    public List<Float> runSA(){
        outerloop:
        for(int i = 0; i < kmax; i++){
            Neighbour randomNeighbour = getNeighbour(state);
            if(randomNeighbour.getFitness() < Input.R){
                setState(randomNeighbour);
                break outerloop;
            }
                double cost = state.getFitness()-randomNeighbour.getFitness();
                if(cost >= 0){
                    setState(randomNeighbour);
                    //System.out.println("Fitness: " + state.getFitness() + " vx: " + state.getVx() + " vy: "+ state.getVy());
                }else {
                    if (Math.random() < getProbability(state, randomNeighbour)) {
                        setState(randomNeighbour);
                        //System.out.println("Fitness: " + state.getFitness() + " vx: " + state.getVx() + " vy: " + state.getVy());
                    }
                }
                if(state.getFitness() < Input.R){
                    break outerloop;
                }
        }
        ArrayList<Float> vxvy = new ArrayList<>();
        vxvy.add(getState().getVx());
        vxvy.add(getState().getVy());
        return vxvy;
    }


    /**
     * Method which generates random vectors and picks the best vector which is used at the start of SA
     * @param init_vector_amount initial amount of vector
     * @return the vector with highest fitness
     */
    public Neighbour findInitalState(int init_vector_amount){
        float[] vxvy = validVelocity();
        Neighbour bestFit = new Neighbour(new StateVector(Input.V0.x, Input.V0.y, vxvy[0], vxvy[1]));
        Neighbour current;
        for(int i = 0; i < init_vector_amount; i++){
            current = getRandomNeighbour();
            System.out.println("RandomNeighbour: " + "vx: "  + current.getVx() + " vy: " + current.getVy() + " Fitness: " + current.getFitness());
            if(current.getFitness() < bestFit.getFitness()){
                bestFit = current;
            }
        }
        System.out.println(bestFit.getVx() + " " + bestFit.getVy());
        return  bestFit;
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
     * Method which creates a random neighbour
     * @return random neighbour
     */
    private Neighbour getRandomNeighbour(){
        float[] vxvy = Score.validVelocity(-5.0f, 5.0f);
        return new Neighbour(new StateVector(Input.V0.x, Input.V0.y, vxvy[0], vxvy[1]));
    }

    /**
     * Method used to create neighbours from the current state based on a neigbourStepSize
     * @param state the current state
     * @return a random neigbour
     */
    private Neighbour getNeighbour(Neighbour state) {
       if(this.recalculate){
           current_neighbours = new ArrayList<>();
           ArrayList<StateVector> newVectors = new ArrayList<>();
                float vx = state.getVx();
                float vy = state.getVy();
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx + neigbourStepSize, vy));
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx - neigbourStepSize, vy));
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx, vy + neigbourStepSize));
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx, vy - neigbourStepSize));
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx - neigbourStepSize, vy - neigbourStepSize));
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx + neigbourStepSize, vy - neigbourStepSize));
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx - neigbourStepSize, vy + neigbourStepSize));
                newVectors.add(new StateVector(Input.V0.x, Input.V0.y, vx + neigbourStepSize, vy + neigbourStepSize));

           ArrayList<StateVector> viableVectors = new ArrayList<>();
           for(int i = 0; i < newVectors.size(); i++){
               if(newVectors.get(i).vx >= 0.01f && newVectors.get(i).vy >= 0.01f && viableVector(newVectors.get(i))){
                   viableVectors.add(newVectors.get(i));
               }
           }

           for(int i = 0; i < viableVectors.size(); i++){
              Neighbour neighbour = new Neighbour(viableVectors.get(i));
              if(neighbour.getFitness() < state.getFitness()){
                  current_neighbours.add(neighbour);
              }
           }
           Collections.shuffle(current_neighbours);
       }
        int random = (int)Math.random()*current_neighbours.size();
       return  ((current_neighbours.size() <= 0) ? getRandomNeighbour() :  current_neighbours.get(random));
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
        this.state = new Neighbour(updated_state.getSv());
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

        long start = System.currentTimeMillis();
        SA test = new SA(500,  0.34f);
        System.out.println("best is " + test.runSA());
        System.out.println(test.state.getFitness());
        long end = System.currentTimeMillis();
        System.out.println("Runtime: " + (end - start) + " ms");
        System.out.println("amount of simulations taken " + Game.simulCounter);

    }
}
