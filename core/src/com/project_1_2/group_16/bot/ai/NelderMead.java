// package com.project_1_2.group_16.bot.ai;

// import com.project_1_2.group_16.math.StateVector;

// import java.util.ArrayList;

// public class NelderMead {

//     //SOLUTIONS
//     double currentSolution;
//     double bestSolution;


//     //PARAMETERS
//     double centroidMean;

//     private double alpha;                   //reflection
//     private double gamma;                   //expansion
//     private double beta;                    //contraction
//     private double delta;                   //shrink

//     double maxIterations;


//     //ORDERED....
//     ArrayList<StateVector> sortedPoints;


//     double getFitness(StateVector sv) {


//     }


//     //TODO: Triangle should become agent.
//     class Simplex {

//         StateVector h;      //worst
//         StateVector s;      //second
//         StateVector l;      //best


//     }


//     Simplex buildSimplex(ArrayList<StateVector> ordered) {


//     }


//     class Point {
//         double x;
//         double y;
//     }


//     //ORDERING - List of points - ORDER BASED ON
//     // indices
//     // 0 = worst, 1 = second worrst , 2 = best
//     ArrayList<StateVector> order(Simplex simplex) {


//     }


//     ///
//     StateVector computeCentroid(Simplex simplex) {


//     }


//     //TRANSORMATIONS
//     void reflection() {

//         StateVector r;

//         centroidMean + alpha * (centroidMean -)


//     }

//     void expansion() {

//     }

//     void contraction() {


//     }

//     void shrinkage() {


//     }
//     //-----------------------


//     void minimizeNelderMead(Function function, int iterations, double tollerance) {


//     }


// }
