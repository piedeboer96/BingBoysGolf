package com.project_1_2.group_16.physics.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.project_1_2.group_16.gamelogic.Terrain;

public class Tile extends ModelInstance {

    private final Vector3 pos = new Vector3();

    private final Vector3 dim = new Vector3();

    private static final BoundingBox bounds = new BoundingBox();

    public Tile(Model model, float x, float z) {
        super(model);

        // move the tile
        this.pos.set(x, Terrain.getHeight(x, z) - Golfball.SIZE, z);
        this.transform.translate(this.pos);

        // calculate bounds
        calculateBoundingBox(Tile.bounds);
        Tile.bounds.getDimensions(this.dim);
    }
    
    /**
     * Get the position of the tile.
     * @return
     */
    public Vector3 getPosition() {
        return this.pos;
    }

    /**
     * Get the dimensions of the tile.
     * @return a three-dimensional vector
     */
    public Vector3 getDimensions() {
        return this.dim;
    }
}
