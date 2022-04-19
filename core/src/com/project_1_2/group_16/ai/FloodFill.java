package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Golfball;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static com.project_1_2.group_16.App.*;

/**
 * Here we define our floodFill algorithm that helps us find the shortest distance to the target
 * from any point on the terrain. This is needed for creating some scoring system that our bot will use.
 */
public class FloodFill {

    //Fields
    static int size_X = 100, size_Y = 100;
    static boolean holeSet = false;
    public static int[][] matrixParcour = new int[size_X][size_Y];
    static int[][] visitedNodes = new int[matrixParcour.length][matrixParcour.length];
    public static int count = 0;
    public static double flood_i, flood_j;


    /**
     * get method which returns the floodfill value based on an x and y coordinate
     * @param x x coordinate
     * @param y y coordinae
     */
    public static int getMatrixValue(float x, float y){
        int i = (int)((x + FIELD_SIZE/2 - TILE_SIZE/2)/TILE_SIZE)-1;
        int j = (int)((x + FIELD_SIZE/2 - TILE_SIZE/2)/TILE_SIZE)-1;
        return matrixParcour[i][j];
    }
    //FloodFill
    /**
     * FloodFill algorithm to be able to create a matrix that represents the shortest distance
     * from any point to some target point. It takes advanced parcours with walls, water, etc.
     * into account. The algorithm runs from the starting position of x and y.
     * @param x x coordinate
     * @param y y coordinae
     */
    public static void floodFill(int x, int y){

        // Creating queue for the bfs
        Queue<Coordinate> queue = new LinkedList<>();

        // Pushing the starting x, y coordinate to the Queue
        Coordinate coord = new Coordinate(x,y);
        queue.add(coord);

        // Marking {x, y} as visited in the visted nodes array
        visitedNodes[x][y] = 1;

        while (!queue.isEmpty())
        {


            int matrix_value = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1; //calculates which the neighbours should get
            Coordinate current = queue.peek(); //grabs the first coordinate in the queue
            x = current.getX();
            y = current.getY();
            int oldCol = matrixParcour[x][y]; //the value of the current node in the matrixparcour array
            queue.remove(); //remove the coordinate you are going to check from the queue

            /*Perform a search for all neighbouring nodes of the current node and set that value to matrix_value.
                Once you have set the value for the neighbouring nodes you add those updated nodes to the visited nodes array.
                And you add the coordinates to the queue.
            */
            //down
            if(isValidStep(x + 1, y, oldCol, oldCol + 1)){
                if(visitedNodes[x+1][y] == 0){
                    Coordinate down = new Coordinate(x + 1, y);
                    queue.add(down);
                    visitedNodes[x+1][y] = 1;
                    matrixParcour[x+1][y] = matrix_value;
                }
            }

            //up
            if(isValidStep(x- 1, y, oldCol, oldCol + 1)){
                if(visitedNodes[x-1][y] == 0){
                    Coordinate up = new Coordinate(x - 1, y);
                    queue.add(up);
                    visitedNodes[x-1][y] = 1;
                    matrixParcour[x-1][y] =  matrix_value;
                }
            }

            //left
            if(isValidStep(x, y - 1, oldCol, oldCol + 1)){
                if(visitedNodes[x][y-1] == 0){
                    Coordinate left = new Coordinate(x, y-1);
                    queue.add(left);
                    visitedNodes[x][y-1] = 1;
                    matrixParcour[x][y-1] =  matrix_value;
                }

            }

            //right
            if(isValidStep(x, y + 1, oldCol, oldCol + 1)){
                if(visitedNodes[x][y+1] == 0){
                    Coordinate right = new Coordinate(x, y+1);
                    queue.add(right);
                    visitedNodes[x][y+1] = 1;
                    matrixParcour[x][y+1] =  matrix_value;
                }

            }
        }
    }

    /**
     * method which can be called to run the floodfill algorithm and fill the matrixparcour array
     */
    public static void runFloodFill(){
        long start = System.currentTimeMillis();
        fillGraphTable();
        floodFill((int)flood_i,(int)flood_j);
        long end = System.currentTimeMillis();
        //System.out.println("runtime: " + (end-start));
        //System.out.println(Arrays.deepToString(FloodFill.matrixParcour));
    }

    //Auxiliary
    /**
     * Represent a 2D cartesian coordinate.
     * It is used as an auxiliary method for our floadfill implementation.
     */
    static class Coordinate{
        int x, y;
        public Coordinate(int x, int y){
            this.x = x;
            this.y = y;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        public String toString(){
            return "x : " + this.x + " y: " + this.y;
        }
    }

    /**
     * Fill graph table based on the field. All the coordinates that are not in water get the value Integer.Max_Value and
     * all coordinates in water get value -1
     */
    public static void fillGraphTable(){
        float x, y;
        for(int i = 0; i < FIELD_DETAIL; i++) {
            for(int j = 0; j < FIELD_DETAIL; j++) {
                x = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (i + 1);
                y = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (j + 1);
                matrixParcour[i][j] = getArrayValue(x, y,i,j);
            }
        }
    }

    /**
     * Returns the a value based on what terrain type something is currently
     * @param x x coordinate
     * @param y y coordinate
     * @param i index in matrix parcour
     * @param j index in matrix parcour
     * @return
     */
    public static int getArrayValue(float x, float y, int i, int j){
        float height = Terrain.getHeight(x, y) - Golfball.SIZE;


        if(height < 0){ //if height smaller than 0 -> value = -1;
            return -1;
        }else{
            if(Math.abs(Input.VT.x - x) < Input.R && Math.abs(Input.VT.y - y) < Input.R && !holeSet){ //if the coordinates are the coordinates of the hole -> value = 0
                holeSet = true;
                flood_i = i;
                flood_j = j;
                return 0;
            }else{
                return Integer.MAX_VALUE; //if height is bigger then 0 and the coordinates are not the holse coordinate -> value = Integer.MAX_Value
            }
        }
    }

    /**
     * FloodFill helper method that checks if a step can be taken.
     * @param i x direction of step
     * @param j y direction of step
     * @param oldCol the old color
     * @param newCol the new color (calculated distance)
     * @return boolean
     */
    public static boolean isValidStep(int i, int j, int oldCol, int newCol){
        if(i < 0 || i >= matrixParcour.length || j < 0 || j >= matrixParcour.length || (matrixParcour[i][j] != oldCol && matrixParcour[i][j]!=Integer.MAX_VALUE && matrixParcour[i][j] == -1)) {
            return false;
        }
        return true;
    }

}


