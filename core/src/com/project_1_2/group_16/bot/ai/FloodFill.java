package com.project_1_2.group_16.bot.ai;

import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.models.Wall;

import java.util.LinkedList;
import java.util.Queue;

import static com.project_1_2.group_16.App.*;

/**
 * Here we define our floodFill algorithm that helps us find the shortest distance to the target
 * from any point on the terrain. This is needed for creating some scoring system that our bot will use.
 */
public class FloodFill {

    int size_X, size_Y;
    boolean holeSet = false;
    int[][] floodFillTable;
    int[][] visitedNodes;
    public double flood_i, flood_j;
    float stepSize;

    public FloodFill(float stepSize) {
        this.stepSize = stepSize;
        size_X = (int)(FIELD_SIZE/stepSize);
        size_Y =  (int)(FIELD_SIZE/stepSize);
        this.floodFillTable = new int[size_X][size_Y];
        this.visitedNodes = new int[size_X][size_Y];
        fillGraphTable();
    }

    /**
     * FloodFill algorithm to be able to create a matrix that represents the shortest distance
     * from any point to some target point. It takes advanced parcours with walls, water, etc.
     * into account. The algorithm runs from the starting position of x and y.
     * @param input_x x coordinate
     * @param input_y y coordinae
     */
    public int[][] runFloodFill(float input_x, float input_y) {
        int x = coordinateToIndex(input_x);
        int y = coordinateToIndex(input_y);
        floodFillTable[x][y] = 0;
        flood_i = x;
        flood_j = y;

        // Creating queue for the bfs
        Queue<Coordinate> queue = new LinkedList<>();

        // Pushing the starting x, y coordinate to the Queue
        Coordinate coord = new Coordinate(x,y);
        queue.add(coord);

        // Marking {x, y} as visited in the visted nodes array
        visitedNodes[x][y] = 1;

        while (!queue.isEmpty()) {
            Coordinate current = queue.peek(); // grabs the first coordinate in the queue
            x = current.getX();
            y = current.getY();
            int oldCol = floodFillTable[x][y]; // the value of the current node in the matrix parcour array
            queue.remove(); // remove the coordinate you are going to check from the queue

            // Perform a search for all neighbouring nodes of the current node and set that value to matrix_value.
            // Once you have set the value for the neighbouring nodes you add those updated nodes to the visited nodes array.
            // And you add the coordinates to the queue.
            // down
            if(isValidStep(x + 1, y, oldCol, oldCol + 1)) {
                if(visitedNodes[x+1][y] == 0) {
                    Coordinate down = new Coordinate(x + 1, y);
                    queue.add(down);
                    visitedNodes[x+1][y] = 1;
                    floodFillTable[x+1][y] = floodFillTable[x][y] + 1;
                }
            }

            // up
            if(isValidStep(x- 1, y, oldCol, oldCol + 1)) {
                if(visitedNodes[x-1][y] == 0) {
                    Coordinate up = new Coordinate(x - 1, y);
                    queue.add(up);
                    visitedNodes[x-1][y] = 1;
                    floodFillTable[x-1][y] =  floodFillTable[x][y] + 1;
                }
            }

            // left
            if(isValidStep(x, y - 1, oldCol, oldCol + 1)) {
                if(visitedNodes[x][y-1] == 0) {
                    Coordinate left = new Coordinate(x, y-1);
                    queue.add(left);
                    visitedNodes[x][y-1] = 1;
                    floodFillTable[x][y-1] =  floodFillTable[x][y] + 1;
                }
            }

            // right
            if(isValidStep(x, y + 1, oldCol, oldCol + 1)) {
                if(visitedNodes[x][y+1] == 0) {
                    Coordinate right = new Coordinate(x, y+1);
                    queue.add(right);
                    visitedNodes[x][y+1] = 1;
                    floodFillTable[x][y+1] =  floodFillTable[x][y] + 1;
                }
            }
        }
        return floodFillTable;
    }

    /**
     * Fill graph table based on the field. All the coordinates that are not in water get the value Integer.Max_Value and
     * all coordinates in water get value -1
     */
    public void fillGraphTable() {
        float x, y;

        for (int i = 0; i < size_X; i++) {
            for (int j = 0; j < size_Y; j++) {
                x = -1 * FIELD_SIZE / 2 + stepSize * (i + 1);
                y = -1 * FIELD_SIZE / 2 + stepSize * (j + 1);

                floodFillTable[i][j] = getArrayValue(x, y, i, j);
            }
        }
        boolean [][] visitedNodes2 = new boolean[size_X][size_Y];
        for (int i=0; i < size_X; i++) {
            for (int j=0; j < size_Y; j++) {
                if (floodFillTable[i][j] == Integer.MAX_VALUE && !visitedNodes2[i][j]) {
                    if (i != size_X - 1) {
                        floodFillTable[i + 1][j] = Integer.MAX_VALUE;
                        visitedNodes2[i + 1][j] = true;
                    } else if (i != 0) {
                        floodFillTable[i - 1][j] = Integer.MAX_VALUE;
                        visitedNodes2[i - 1][j] = true;
                    } else if (j != size_Y - 1) {
                        floodFillTable[i][j + 1] = Integer.MAX_VALUE;
                        visitedNodes2[i][j + 1] = true;
                    } else if (j!=0) {
                        floodFillTable[i][j - 1] = Integer.MAX_VALUE;
                        visitedNodes2[i][j - 1] = true;
                    }
                    visitedNodes2[i][j] = true;
                }
            }
        }
    }

    public int coordinateToIndex(float coordinate){
        float rounded_coordinate = Math.round(coordinate);
        return (int)(((rounded_coordinate + FIELD_SIZE/2)/stepSize));
    }

    /**
     * Get method which returns the floodfill value based on an x and y coordinate
     * @param x x coordinate
     * @param y y coordinae
     */
    public int getFloodFillFitness(float x, float y) {
        int i = coordinateToIndex(y);
        int j = coordinateToIndex(x);
        return floodFillTable[i][j];
    }

    /**
     * This method creates the floodFillMatrix.
     * We know the distance from any point to your hole.
     * Water means -1.
     * @param x x coordinate
     * @param y y coordinate
     * @param i index in matrix parcour
     * @param j index in matrix parcour
     * @return
     */
    public int getArrayValue(float x, float y, int i, int j) {
        float terrainHeight = Terrain.getHeight(x,y);

        //if height smaller than 0 -> value = -1;
        if (terrainHeight<0 || isInWall(x, y)) {                       
            visitedNodes[i][j] = 1;
            return Integer.MAX_VALUE;
        } else {
            // if the coordinates are the coordinates of the hole -> value = 0
            if (Math.abs(Input.VT.x - x) < Input.R && Math.abs(Input.VT.y - y) < Input.R && !holeSet) { 
                holeSet = true;
                flood_i = i;
                flood_j = j;
                return 0;
            } else {
                return -1; // if height is bigger then 0 and the coordinates are not the holse coordinate -> value = Integer.MAX_Value
            }
        }
    }

    public boolean isInWall(float x, float y) {
        for (Wall i : Input.WALLS) {
            if (i.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * FloodFill helper method that checks if a step can be taken.
     * @param i x direction of step
     * @param j y direction of step
     * @param oldCol the old color
     * @param newCol the new color (calculated distance)
     * @return boolean
     */
    public boolean isValidStep(int i, int j, int oldCol, int newCol) {
        return !(i < 0 || i >= floodFillTable.length || j < 0 || j >= floodFillTable.length || (floodFillTable[i][j] != oldCol && floodFillTable[i][j]==Integer.MAX_VALUE));
    }

    /**
     * Represent a 2D cartesian coordinate.
     * It is used as an auxiliary method for our floadfill implementation.
     */
    static class Coordinate{

        int x, y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String toString() {
            return "x : " + this.x + " y: " + this.y;
        }
    }
}
