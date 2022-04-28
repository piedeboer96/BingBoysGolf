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

    public SA(int kmax, float neigbourStepSize){
        this.kmax = kmax;
        this.Temperature = getTemperature(kmax);
        this.neigbourStepSize = neigbourStepSize;
        this.recalculate = true;
        setState(findInitalState(init_velocities));
        stop = false;
    }

    public List<Float> runSA2(){
        for(int i = 0; i < kmax && !stop; i++){
            Neighbour randomNeigbour = getNeighbour(state);
                double cost = state.getFitness()-randomNeigbour.getFitness();
                if(cost >= 0){
                    setState(randomNeigbour);
                    System.out.println("Fitness: " + state.getFitness() + " vx: " + state.getVx() + " vy: "+ state.getVy());
                }else {
                    if (Math.random() < getProbability(state, randomNeigbour)) {
                        setState(randomNeigbour);
                        System.out.println("Fitness: " + state.getFitness() + " vx: " + state.getVx() + " vy: " + state.getVy());
                    }
                }
                if(state.getFitness() <= Input.R){
                    stop = true;
                }
        }
        ArrayList<Float> vxvy = new ArrayList<>();
        vxvy.add(getState().getVx());
        vxvy.add(getState().getVy());
        return vxvy;
    }

    public Neighbour findInitalState(int init_vector_amount){
        float[] vxvy = validVelocity();
        Neighbour bestFit = new Neighbour(new StateVector(Input.V0.x, Input.V0.y, vxvy[0], vxvy[1]));
        Neighbour current;
        for(int i = 0; i < init_vector_amount; i++){
            current = getRandomNeighbour();
            System.out.println("RandomNeighbour: " + "vx: "  + current.getVx() + " vy: " + current.getVy());
            if(current.getFitness() < bestFit.getFitness()){
                bestFit = current;
            }
        }
        System.out.println(bestFit.getVx() + " " + bestFit.getVy());
        return  bestFit;
    }

    
    public List<Float> runSA() {
        int testvar = 0;
       for(int i = 0; i < kmax; i++) {
            Temperature = getTemperature(i);
            Neighbour currentNeighbour = Neighbour.clone(getNeighbour(getState()));
            float rand = (float)Math.random();

            if(getProbability(currentNeighbour, getState()) >= rand){
                System.out.println(" fitness: " + currentNeighbour.getFitness() +" vx: " + currentNeighbour.getVx() + " vy: " + currentNeighbour.getVy());
                setState(currentNeighbour);
                testvar++;
                this.recalculate = true;
            }else{
                this.recalculate = false;
            }
           System.out.println(state.getVx() + " " + state.getVy());

        }
        System.out.println("Amount of hits: " +  testvar);
        ArrayList<Float> vxvy = new ArrayList<>();
        vxvy.add(getState().getVx());
        vxvy.add(getState().getVy());
       return vxvy;
    }

    private static boolean viableVector(StateVector sv){
        if((float)Math.sqrt(sv.vx*sv.vx+sv.vy+sv.vy) <= MAXVEL){
            return true;
        }
        return  false;
    }

    public static float[] validVelocity(){
        float[] vxy = new float[2];
        vxy[0] = (float)(0 + Math.random()*(MAXVEL));
        vxy[1] = (float)(0 + Math.random()*(MAXVEL));
        if(Physics.magnitude(vxy[0], vxy[1]) > 5){
            vxy[0] = (float) (vxy[0]/Math.sqrt(50));
            vxy[1] = (float) (vxy[1]/Math.sqrt(50));
        }
        return vxy;
    }

    private Neighbour getRandomNeighbour(){
        float[] vxvy = validVelocity();
        return new Neighbour(new StateVector(Input.V0.x, Input.V0.y, vxvy[0], vxvy[1]));
    }

    
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


    public Neighbour getState() {
        return state;
    }

    private void setState(Neighbour updated_state){
        this.state = new Neighbour(updated_state.getSv());
    }
    
    private float getProbability(Neighbour state, Neighbour updated) {
       return (float)Math.exp(-1*(state.getFitness() - updated.getFitness()) / Temperature);
    }

    public double getTemperature(int k) {
        return 1 - (k+1)/kmax;
    }

    public static void main(String[] args) {
//        FloodFill.fillGraphTable();
//        FloodFill.floodFill((int)FloodFill.flood_i,(int)FloodFill.flood_j);
//        System.out.println(Arrays.deepToString(FloodFill.matrixParcour));
        float[] initial_vxvy = Score.validVelocity(0, 5);
        long start = System.currentTimeMillis();
        SA test = new SA(500,  0.1f);

        System.out.println(test.runSA2());
        System.out.println(test.state.getFitness());
        long end = System.currentTimeMillis();
        System.out.println(test.state.getX() +" " + test.state.getY());
        System.out.println("Holex: " + hole_x + " Holey: " + hole_y);
        System.out.println("RunTime: " + (end - start) + " ms");

//        StateVector testv = new StateVector(Input.V0.x, Input.V0.y, 0.029642947f, -0.23318267f);
//        Game test = new Game();
//        test.setNumericalSolver(NumericalSolver.RK4);
//        test.runEngine(testv, null);
//        System.out.println(testv.toString());
//        System.out.println(Score.calculateEucledianDistance(testv.x, testv.y, Input.VT.x, Input.VT.y));
//
//        Neighbour testn = new Neighbour(new StateVector(Input.V0.x, Input.V0.y, 0.029642947f, -0.23318267f));
//        System.out.println(testn.getFitness());

    }
}
