package com.project_1_2.group_16.gamelogic;

import com.badlogic.gdx.math.Vector2;

public class Sandpit {

    private Vector2 pos = new Vector2();
    private float r;

    /**
     * Create a sandpit object
     * @param x x-coordinate
     * @param y y-coordinate
     * @param r radius of the sandpit
     */
    public Sandpit(float x, float y, float r) {
        pos.set(x, y);
        this.r = r;
    }

    /**
     * Get the position of the sandpit
     * @return a 2d vector
     */
    public Vector2 getPosition() {
        return this.pos;
    }

    /**
     * Get the radius of the sandpit
     * @return a floating point number
     */
    public float getRadius() {
        return this.r;
    }
}
