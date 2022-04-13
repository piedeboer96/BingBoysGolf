package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Golfball;

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

    //FloodFill
    /**
     * FloodFill algorithm to be able to create a matrix that represents the shortest distance
     * from any point to some target point. It takes advanced parcours with walls, water, etc.
     * into accout.
     * @param x x coordinate
     * @param y y coordinae
     */
    public static void floodFill(int x, int y){

        // Creating queue for bfs
        Queue<Coordinate> queue = new LinkedList<>();


        // Pushing Coordinate of {x, y}
        Coordinate coord = new Coordinate(x,y);
        queue.add(coord);

        // Marking {x, y} as visited
        visitedNodes[x][y] = 1;

        while (!queue.isEmpty())
        {
            count++;
            Coordinate current = queue.peek();
            x = current.getX();
            y = current.getY();
            int oldCol = matrixParcour[x][y];
            queue.remove();

            //down
            if(isValidStep(x + 1, y, oldCol, oldCol + 1)){
                if(visitedNodes[x+1][y] == 0){
                    Coordinate down = new Coordinate(x + 1, y);
                    queue.add(down);
                    visitedNodes[x+1][y] = 1;
                    matrixParcour[x+1][y] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }
            }

            //up
            if(isValidStep(x- 1, y, oldCol, oldCol + 1)){
                if(visitedNodes[x-1][y] == 0){
                    Coordinate up = new Coordinate(x - 1, y);
                    queue.add(up);
                    visitedNodes[x-1][y] = 1;
                    matrixParcour[x-1][y] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }
            }

            //left
            if(isValidStep(x, y - 1, oldCol, oldCol + 1)){
                if(visitedNodes[x][y-1] == 0){
                    Coordinate left = new Coordinate(x, y-1);
                    queue.add(left);
                    visitedNodes[x][y-1] = 1;
                    matrixParcour[x][y-1] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }

            }

            //right
            if(isValidStep(x, y + 1, oldCol, oldCol + 1)){
                if(visitedNodes[x][y+1] == 0){
                    Coordinate right = new Coordinate(x, y+1);
                    queue.add(right);
                    visitedNodes[x][y+1] = 1;
                    matrixParcour[x][y+1] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }

            }
        }
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
     * Fill graph table based on the field.
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
     * @param x
     * @param y
     * @param i
     * @param j
     * @return
     */
    public static int getArrayValue(float x, float y, int i, int j){
        float height = Terrain.getHeight(x, y) - Golfball.SIZE;

        if(height < 0){
            return -1;
        }else{
            if(Math.abs(App.tV.x - x) < App.tR && Math.abs(App.tV.y - y) < App.tR && !holeSet){
                holeSet = true;
                flood_i = i;
                flood_j = j;
                System.out.println("FLOOD i: " + flood_i + " j: " + flood_j);
                return 0;
            }else{
                return Integer.MAX_VALUE;
            }
        }
    }

    /**
     * FloodFill helper method that checks if a valid step can be taken.
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


