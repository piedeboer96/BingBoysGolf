package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.bot.AdvancedBot;
import com.project_1_2.group_16.bot.Agent;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.bot.BotHelper;

import java.util.*;


/**
 * Points are considered the Vx and Vy parameters of the bot.
 */

public class NelderMead extends AdvancedBot {

    //FIELDS
    Game game;

    private double alpha;                   //reflection parameter
    private double gamma;                   //expansion parameter
    private double beta;                    //contraction parameter
    private double delta;                   //shrink parameter

    private int max_iterations;

    //Constructor
    public NelderMead(float startX, float startY, Game game, int max_iterations,double alpha, double beta, double gamma, double delta, boolean useFloodFill) {
        super(startX, startY, game, useFloodFill);
        this.max_iterations = max_iterations;

        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.delta = delta;

        this.game = game;
    }


    @Override
    public List<Float> runBot() {
        NelderAgent solution = runNelderAgentV2();
        ArrayList<Float> toReturn = new ArrayList<>();
        toReturn.add(solution.velX);
        toReturn.add(solution.velY);
        return toReturn;
    }

    NelderAgent runNelderAgentV2(){
        NelderAgent[] agents = getSortedInitalAgents();
        Centroid centroid = computeCentroid(agents);

        //for loop
        //ordering
        for(int i = 0; i < this.max_iterations; i++){
            agents = orderAgents(agents);
            System.out.println("Best agent-->  x: " + agents[2].velX + " y: " + agents[2].velY + " fitness: " + agents[2].fitness);
            System.out.println("middle agent-->  x: " + agents[1].velX + " y: " + agents[1].velY + " fitness: " + agents[1].fitness);
            System.out.println("worst agent-->  x: " + agents[0].velX + " y: " + agents[0].velY + " fitness: " + agents[0].fitness);

            //centroid
            centroid = computeCentroid(agents);

            //transformation
            /*
                1. Reflect:
                2. Expand
                3. Contract
                    Outside
                    Inside
                4. Shrink
             */
            NelderAgent reflection_point = reflection(agents, centroid);

            //toremove
            NelderAgent prev_best_agent = agents[2];


            //Reflect
            if(reflection_point.fitness >= agents[2].fitness && reflection_point.fitness < agents[1].fitness){
                System.out.println("REFLECT");
                agents[0] = reflection_point;
            }else{
                //expand
                if(reflection_point.fitness < agents[2].fitness){
                    NelderAgent expansion_point = expansion(agents, reflection_point, centroid);
                    if(expansion_point.fitness < reflection_point.fitness){
                        System.out.println("EXPAND");
                        agents[0] = expansion_point;
                    }else{
                        System.out.println("REFLECT --> COMARISON EXPAND");
                        agents[0] = reflection_point;
                    }
                }else{
                    //contract
                    if(reflection_point.fitness >= agents[1].fitness){

                        //outside
                        if(reflection_point.fitness >= agents[2].fitness && reflection_point.fitness < agents[0].fitness){
                            NelderAgent contraction_point = contractionOutside(agents, centroid, reflection_point);
                            if(contraction_point.fitness < reflection_point.fitness){
                                System.out.println("CONTRACT OUTSIDE");
                                agents[0] = contraction_point;
                            }
                            else{
                                System.out.println("SHRINK --> OUTSIDE CONTRACT");
                                agents[0] = shrinkage(agents, agents[0]);
                                agents[1] = shrinkage(agents, agents[1]);
                            }
                        }else{
                            //inside
                            if(reflection_point.fitness>= agents[0].fitness){
                                NelderAgent contraction_point = contractionInside(agents, centroid);
                                if(contraction_point.fitness < agents[0].fitness){
                                    System.out.println("CONTRACT INSIDE");
                                    agents[0] = contraction_point;
                                }
                                else{
                                    System.out.println("SHRINK --> INSIDE CONTRACT");
                                    agents[0] = shrinkage(agents, agents[0]);
                                    agents[1] = shrinkage(agents, agents[1]);
                                }
                            }
                        }
                        //inside
                    }else{
                        System.out.println("SHRINK ");
                        //shrink
                        agents[0] = shrinkage(agents, agents[0]);
                        agents[1] = shrinkage(agents, agents[1]);
                    }
                }
            }

            //check to stop
            //IF CONVERGED RETURN ELSE REITERATE


            //______________________remove converged______________________
            if(agents[2].fitness < Input.R || (agents[0].fitness == agents[1].fitness && agents[1].fitness == agents[2].fitness)) {
                System.out.println("Converged at iteration: " + i + " Best agent --> vx: "+ agents[2].velX + " vy: " + agents[2].velY);
                return agents[2];
            }
        }

        return agents[0];
    }

    //Simplex
    class Simplex {

        NelderAgent h;      //highest function value (worst)
        NelderAgent s;      //middle
        NelderAgent l;      //lowest function value (best)

        public Simplex(NelderAgent agentOne, NelderAgent agentTwo, NelderAgent agentThree) {
            this.h = agentOne;
            this.s = agentTwo;
            this.l = agentThree;
        }
    }

    /**
     * Centroid class.
     */
    class Centroid {
        double cy;
        double cx;

        public Centroid(double cx, double cy) {
            this.cx = cx;
            this.cy = cy;
        }

        public double getCx() {
            return cx;
        }

        public double getCy() {
            return cy;
        }

    }

    /**
     * Compute centroid mean. This is also an agent.
     * It's vx and vy parameters are based on the 'centroid mean'.
     * @param agents sorted list of agents
     * @return centroid mean agent
     */
    Centroid computeCentroid(Agent[] agents) {

        double n = agents.length;

        double vx_sum = 0;
        double vy_sum = 0;

        for (int i = 1; i < agents.length; i++) {
            vx_sum += agents[i].velX;
            vy_sum += agents[i].velY;
        }

        double vx_c = (vx_sum/n);
        double vy_c = (vy_sum/n);

        return new Centroid(vx_c,vy_c);
    }

    /**
     * Calculate new agent that represents reflection point.
     * @param agents list of sorted agents
     * @param centroid centroid
     * @return agent representing reflection point
     */
    NelderAgent reflection(NelderAgent[] agents, Centroid centroid) {

        double vx_c = centroid.getCx();
        double vy_c = centroid.getCy();

        double vx_h = agents[0].velX;
        double vy_h = agents[0].velY;

        double vx_r = vx_c + alpha * (vx_c - vx_h);
        double vy_r = vy_c + alpha * (vy_c - vy_h);

        return (new NelderAgent( new StateVector(getStartX(), getStartY(), (float)vx_r, (float)vy_r) , this.game));
    }

    /**
     * Calculate new agent that represents expansion
     * @param agents list of sorted agents
     * @param centroid centroid
     * @return agent representing reflection point
     */
    NelderAgent expansion(NelderAgent[] agents, NelderAgent reflectionAgent, Centroid centroid) {
        //~(Ht)
        //xh replaced by xr
        double vx_c = centroid.getCx();
        double vy_c = centroid.getCy();

        double vx_r = reflectionAgent.velX;
        double vy_r = reflectionAgent.velY;

        double vx_e = vx_c+ gamma*(vx_c-vx_r);
        double vy_e = vy_c + gamma*(vy_c-vy_r);

        return (new NelderAgent( new StateVector(getStartX(), getStartY(), (float)vx_e, (float)vy_e) , this.game));
    }


    /**
     * Calculate new agent that represents contration outside
     * @param agents list of sorted agents
     * @param centroid centroid
     * @return agent representing contraction point
     */
    NelderAgent contractionInside(NelderAgent[] agents, Centroid centroid) {

        double vx_c = centroid.getCx();
        double vy_c = centroid.getCy();

        double vx_h = agents[0].velX;
        double vy_h = agents[0].velY;

        double vx_contract = vx_c + beta*(vx_h - vx_c);
        double vy_contract = vy_c + beta*(vy_h - vy_c);

        return new NelderAgent( new StateVector(getStartX(), getStartY(), (float)vx_contract, (float)vy_contract) , this.game);
    }


    /**
     * Calculate new agent that represents contration inside
     * @param agents list of sorted agents
     * @param centroid centroid
     * @param reflection_point reflection point
     * @return agent representing contraction point
     */
    NelderAgent contractionOutside(NelderAgent[] agents, Centroid centroid, NelderAgent reflection_point) {

        double vx_c = centroid.getCx();
        double vy_c = centroid.getCy();

        double vx_h = reflection_point.velX;
        double vy_h = reflection_point.velY;

        double vx_contract = vx_c + beta*(vx_h - vx_c);
        double vy_contract = vy_c + beta*(vy_h - vy_c);

        return new NelderAgent( new StateVector(getStartX(), getStartY(), (float)vx_contract, (float)vy_contract) , this.game);
    }

    /**
     * Calculate new agent that represents expansion
     * @param agents list of sorted agents
     * @param agents current agent
     * @return agent representing reflection point
     */
    NelderAgent shrinkage(NelderAgent[] agents, NelderAgent cur_agent) {
        //xh replaced by xr

        double vx_l = agents[2].velX;
        double vy_l = agents[2].velY;

        double vx_j_prev = cur_agent.velX;
        double vy_j_prev = cur_agent.velY;

        double vx_j = vx_l + delta*(vx_j_prev - vx_l);
        double vy_j = vy_l + delta*(vy_j_prev - vy_l);

        return (new NelderAgent( new StateVector(getStartX(), getStartY(), (float)vx_j, (float)vy_j) , this.game));
    }

    /**
     * Sort array of agents based on fitness value.
     * @param agents array of agents
     * @return sorted array of agents based on fitness value
     */
    NelderAgent[] orderAgents(NelderAgent[] agents) {

        for(int j = 0; j < 3; j++){
            for(int i = 0; i < 2; i++){
                if(agents[i].fitness < agents[i+1].fitness){
                    NelderAgent intermediate_agent = agents[i];
                    agents[i] = agents[i+1];
                    agents[i+1] = intermediate_agent;
                }
            }
        }
        return agents;
    }


    /**
     * Generate sorted list of initial agetns
     * @return array of sorted agents
     */
    NelderAgent[] getSortedInitalAgents(){

        List<float[]> velocities = BotHelper.availableVelocities(getStartX(), getStartY());
        NelderAgent[] bestAgents = new NelderAgent[velocities.size()];
        int count= 0;
        NelderAgent[] initAgents = new NelderAgent[3];


       boolean isset_bestagent = false;
       NelderAgent best_agent = null;
        outer:
        for(int i = 0; i < bestAgents.length; i++){
            bestAgents[i] = new NelderAgent(new StateVector(getStartX(), getStartY(), velocities.get(i)[0], velocities.get(i)[1]), this.game);
            if(isset_bestagent){

                    if(bestAgents[i].fitness < best_agent.fitness){
                        best_agent = bestAgents[i];
                    }

            }else{
                best_agent = bestAgents[i];
                isset_bestagent = true;
            }
        }

        NelderAgent agentOne = new NelderAgent(new StateVector(getStartX(), getStartY(), -4f, +4f), getGame());
        NelderAgent agentTwo = new NelderAgent(new StateVector(getStartX(), getStartY(), +4f, -4f), getGame());
        NelderAgent agentThree = new NelderAgent(new StateVector(getStartX(), getStartY(), 4f, 4f), getGame());

        initAgents[0] = agentThree;
        initAgents[1] = agentTwo;
        initAgents[2] = agentOne;

        NelderAgent[] ordered_agents = orderAgents(initAgents);



        System.out.println("Best init agents-->  x: " + ordered_agents[0].velX + " y: " + ordered_agents[0].velY + " fitness: " + ordered_agents[0].fitness);



//        NelderAgent agentOne = new NelderAgent(new StateVector(getStartX(), getStartY(), velocities.get(0)[0], velocities.get(0)[1]), this.game);
//        NelderAgent agentTwo = new NelderAgent(new StateVector(getStartX(), getStartY(), velocities.get(1)[0], velocities.get(1)[1]), this.game);
//        NelderAgent agentThree = new NelderAgent(new StateVector(getStartX(), getStartY(), velocities.get(2)[0], velocities.get(2)[1]), this.game);


//        NelderAgent[] agents = new NelderAgent[3];
//        agents[0] = agentOne;
//        agents[1] = agentTwo;
//        agents[2] = agentThree;
//
//        for(int i=0; i<agents.length; i++){
//            System.out.println("fit before: " + agents[i].fitness);
//        }
//
//
//        NelderAgent[] sortedAgents = orderAgents(agents);
//
//        for(int i=0; i<agents.length; i++){
//            System.out.println("fit after: " + sortedAgents[i].fitness);
//        }
//        System.out.println("______________________________________________________");
        return ordered_agents;
    }




    /**
     * Build initial simplex from sorted list of agents
     * @param agents
     * @return
     */
    Simplex buildInitSimplex(NelderAgent[] agents){
        return new Simplex(agents[0], agents[1], agents[2]);
    }

    /**
     * This methods runs the Nelder Mead algorithm
     *
     * For more info: Algorithms for Optimization (page 107)
     */
    NelderAgent runNelderMead() {

        //BUILD INITIAL SIMPLEX WITH SORTED AGENTS.

        NelderAgent[] agents = getSortedInitalAgents();


        System.out.println(agents[0].fitness);
        System.out.println(agents[1].fitness);
        System.out.println(agents[2].fitness);



//        Simplex init_simplex = buildInitSimplex(agents);

        for(int i = 0; i < max_iterations; i++){
            //GET SORTED INITIAL AGENTS
            agents = orderAgents(agents);

            System.out.println("Best agent-->  x: " + agents[2].velX + " y: " + agents[2].velY + " fitness: " + agents[2].fitness);
            System.out.println("middle agent-->  x: " + agents[1].velX + " y: " + agents[1].velY + " fitness: " + agents[1].fitness);
            System.out.println("worst agent-->  x: " + agents[0].velX + " y: " + agents[0].velY + " fitness: " + agents[0].fitness);

            System.out.println();

            //COMPUTING THE CENTROID
            Centroid centroid = computeCentroid(agents);

            //COMPUTE REFLECTION POINT
            NelderAgent reflection_point = reflection(agents, centroid);

            //CHECK IF YR < YL, IN OUR CASE YR = FITNESS OF XR, ...
            NelderAgent lowest_point = agents[0];
            if(reflection_point.fitness < lowest_point.fitness){
                //EXPAND
                NelderAgent expansion_point = expansion(agents,reflection_point,centroid);

                // IF YE < YR REPLACE XH WITH XE
                if(expansion_point.fitness < reflection_point.fitness){
                    agents[0] = expansion_point;
                }
                else{
                    agents[0] = reflection_point;
                }
            }
            else{


                // IF YR >= YH DO NOTHING ELSE REPLACE XH WITH XR

                if(reflection_point.fitness >= agents[1].fitness) {
                    //COMPUTE CONTRACTION POINT
                    NelderAgent contraction_point = contractionInside(agents, centroid);

                    //IF REFLECTION >= BEST AGENT THEN DO replacement
                    if(reflection_point.fitness < agents[0].fitness){
                        agents[0] = reflection_point;
                    }

                    contraction_point = contractionInside(agents, centroid);

                    //IF CONTRACTION POINT > BEST AGENT
                    if(contraction_point.fitness > agents[0].fitness) {
                        //SHRINK BY REPLACING ALL XI WITH (XI+XL)/2

                        agents[0] = shrinkage(agents, agents[0]);
                        agents[1] = shrinkage(agents, agents[1]);

                    } else {
                        agents[0] = contraction_point;
                    }
                }else{
                    agents[2] = reflection_point;
                }
            }

            //IF CONVERGED RETURN ELSE REITERATE
            if(agents[2].fitness < Input.R || Math.abs(agents[2].fitness - agents[2].fitness)<Input.R) {
                System.out.println("Converged at iteration: " + i + " Best agent --> vx: "+ agents[2].velX + " vy: " + agents[2].velY);
                return agents[2];
            }
        }

        return agents[2];
    }

}




class NelderAgent extends Agent {

    public NelderAgent(StateVector sv, Game game) {
        super(sv, game);
    }
    public NelderAgent createClone (){
        return new NelderAgent(new StateVector(startX, startY, velX, velY), getGame());
    }

}