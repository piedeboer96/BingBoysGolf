package com.project_1_2.group_16.gamelogic;

public class Sandpit {

    public static float kineticFriction = 10f * Terrain.kineticFriction;
    public static float staticFriction = 10f * Terrain.staticFriction;

    float x, y;
    float r;

    public Sandpit(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
}
