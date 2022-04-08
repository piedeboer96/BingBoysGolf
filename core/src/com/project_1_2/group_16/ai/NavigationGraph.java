package com.project_1_2.group_16.ai;

import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.models.Golfball;

import static com.project_1_2.group_16.App.*;

public class NavigationGraph {

    //Empty matrix for flood-fill
    int size_X, size_Y = (int) FIELD_DETAIL;
    int[][] matrixParcour = new int[size_X][size_Y];


    public void fillGraphTable(){
        float x, y;
        for(int i = 0; i < FIELD_DETAIL; i++) {
            for(int j = 0; j < FIELD_DETAIL; j++) {
                x = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (i + 1);
                y = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (j + 1);
                matrixParcour[i][j] = getArrayValue(x, y);
            }
        }
    }

    public int getArrayValue(float x, float y){
        float height = Terrain.getHeight(x, y) - Golfball.SIZE;
        if(height < 0){
            return -1;
        }else{
            return 1;
        }
    }





}
