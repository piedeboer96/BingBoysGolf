package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.gamelogic.Terrain;

public class Golfball {

    /**
     * The model of the golfball.
     */
    public Model model;

    /**
     * The modelinstance of the golfball.
     */
    public ModelInstance instance;

    /**
     * The reference to the camera that is attached to the golfball.
     */
    public Camera cam;

    /**
     * The radius of the golfball.
     */
    public static final float SIZE = 0.1f;

    // util
    private final Vector3 v = new Vector3();
    
    public Golfball(ModelBuilder builder, Camera cam) {
        Material golfBallMaterial = new Material(ColorAttribute.createDiffuse(Color.WHITE));
		this.model = builder.createSphere(SIZE, SIZE, SIZE, 10, 10, golfBallMaterial, Usage.Position + Usage.Normal);
        this.instance = new ModelInstance(model);
        this.cam = cam;
    }

    /**
     * Get the position of the golfball as a three-dimensional vector.
     * @return
     */
    public Vector3 getPosition() {
        return this.instance.transform.getTranslation(new Vector3());
    }

    /**
     * Set the position of the golfball.
     * @param v a three-dimensional vector
     * @return position for chaining
     */
    public Vector3 setPosition(Vector3 v) {
        return this.setPosition(v.x, v.y, v.z);
    }

    /**
     * Set the position of the golfball.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return position for chaining
     */
    public Vector3 setPosition(float x, float y, float z) {
        this.instance.transform.setTranslation(x, y, z);
        return this.getPosition();
    }

    /**
     * Translate the position of the golfball.
     * @param v a three-dimensional vector
     * @return position for chaining
     */
    public Vector3 translate(Vector3 v) {
        return this.translate(v.x, v.y, v.z);
    }

    /**
     * Translate the position of the golfball.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return position for chaining
     */
    public Vector3 translate(float x, float y, float z) {
        this.instance.transform.translate(x, y, z);
        return this.getPosition();
    }

    /**
     * Move the golfball. The y-coordinate gets calculated based on the height function.
     * @param dX delta X
     * @param dZ delta Z
     * @return position for chaining
     */
    public Vector3 move(float dX, float dZ) {
        this.v.set(this.getPosition());
		this.setPosition(this.v.x + dX, Terrain.getHeight(this.v.x + dX, this.v.z + dZ) + SIZE / 2, this.v.z + dZ);
		this.cam.translate(this.getPosition().sub(this.v));
		this.cam.update();
        return this.getPosition();
    }
}
