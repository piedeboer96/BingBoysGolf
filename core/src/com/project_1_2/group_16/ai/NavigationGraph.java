package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Golfball;

import java.util.Arrays;

import static com.project_1_2.group_16.App.*;

public class NavigationGraph {

    //Empty matrix for flood-fill
    static int size_X = 100, size_Y = 100;
    static boolean holeSet = false;
    public static int[][] matrixParcour = new int[size_X][size_Y];

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

    /**
     *
     *
     * @param i
     * @param j
     * @param newCol
     */
    public static void fload_fill(int i, int j, int newCol){

        int oldCol = matrixParcour[(int)flood_i][(int)flood_j];


        System.out.println("old col " + oldCol);

        if(oldCol== newCol){
            return;
        }


        dfs(i, j, 0, 1);
    }

    public static void dfs(int i , int j, int oldCol, int newCol){



        if(i < 0 || i >= matrixParcour.length || j < 0 || j >= matrixParcour.length || (matrixParcour[i][j] != oldCol && matrixParcour[i][j]!=99)) {
            System.out.println("ret");
            return;
        }else{
            matrixParcour[i][j] = newCol;

//            System.out.println("hit: " + oldCol++);

            oldCol++;

            dfs(i+1, j, oldCol, oldCol++);
            dfs( i, j+1, oldCol, oldCol++);
            dfs( i-1, j, oldCol, oldCol++);
            dfs( i, j-1, oldCol, oldCol++);



        }
    }

//


    public static void main(String[] args) {

    }




}
