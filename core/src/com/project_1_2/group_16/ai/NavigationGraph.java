package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Golfball;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static com.project_1_2.group_16.App.*;

public class NavigationGraph {

    //Empty matrix for flood-fill
    static int size_X = 100, size_Y = 100;
    static boolean holeSet = false;
    public static int[][] matrixParcour = new int[size_X][size_Y];
    static int[][] visitedNodes = new int[matrixParcour.length][matrixParcour.length];
    public static int count = 0;

    public static double flood_i, flood_j;


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

    //nice comment
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
                return 99;
            }
        }
    }

    public static void fload_fill(int x, int y){
        // Creating queue for bfs
        Queue<Coordinate> queu = new LinkedList<>();

        // Pushing Coordinate of {x, y}
        Coordinate coord = new Coordinate(x,y);
        queu.add(coord);

        // Marking {x, y} as visited
        visitedNodes[x][y] = 1;



        while (!queu.isEmpty())
        {
            count++;
            Coordinate current = queu.peek();
            x = current.getX();
            y = current.getY();
            int oldCol = matrixParcour[x][y];
            queu.remove();



            //down
            if(isValidStep(x + 1, y, oldCol, oldCol + 1)){
                if(visitedNodes[x+1][y] == 0){
                    Coordinate down = new Coordinate(x + 1, y);
                    queu.add(down);
                    visitedNodes[x+1][y] = 1;
                    matrixParcour[x+1][y] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }

            }

            //up
            if(isValidStep(x- 1, y, oldCol, oldCol + 1)){
                if(visitedNodes[x-1][y] == 0){
                    Coordinate up = new Coordinate(x - 1, y);
                    queu.add(up);
                    visitedNodes[x-1][y] = 1;
                    matrixParcour[x-1][y] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }

            }

            //left
            if(isValidStep(x, y - 1, oldCol, oldCol + 1)){
                if(visitedNodes[x][y-1] == 0){
                    Coordinate left = new Coordinate(x, y-1);
                    queu.add(left);
                    visitedNodes[x][y-1] = 1;
                    matrixParcour[x][y-1] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }

            }
            //right
            if(isValidStep(x, y + 1, oldCol, oldCol + 1)){
                if(visitedNodes[x][y+1] == 0){
                    Coordinate right = new Coordinate(x, y+1);
                    queu.add(right);
                    visitedNodes[x][y+1] = 1;
                    matrixParcour[x][y+1] = (int) (Math.abs(flood_i - x)+ Math.abs(flood_j-y))+1;
                }

            }
        }
    }

    public static boolean isValidStep(int i, int j, int oldCol, int newCol){
        if(i < 0 || i >= matrixParcour.length || j < 0 || j >= matrixParcour.length || (matrixParcour[i][j] != oldCol && matrixParcour[i][j]!=99 && matrixParcour[i][j] == -1)) {
            return false;
        }
        return true;
    }
}

class Coordinate{
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
