package com.project_1_2.group_16.gamelogic;

public class Sandpit {

    public static final float kineticFriction = 10f * Terrain.kineticGrass;
    public static final float staticFriction = 10f * Terrain.staticGrass;
    
    float minX, minY;
    float maxX, maxY;

    public Sandpit(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
}
