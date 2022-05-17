package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.StateVector;

/**
 * Wrapper class for the golfball.
 */
public class Golfball {

    /**
     * The radius of the golfball.
     */
    public static final float SIZE = 0.1f;

    /**
     * The 'state' of the golfball. Encapsulates its position and velocity.
     */
    public final StateVector STATE = new StateVector(0, 0, 0, 0);
    
    private Model model;
    private ModelInstance instance;
    private Camera cam;
    private Vector3 util = new Vector3();
    
    /**
     * Create a golfball object, used both in front- and back-end.
     * @param builder a modelbuilder for creating the model
     * @param theme the textures of the golfball
     */
    public Golfball() {
		this.model = App.THEME.golfballModel(Golfball.SIZE);
        this.instance = new ModelInstance(model);
    }

    /**
     * Get the position of the golfball as a three-dimensional vector.
     * @return
     */
    public Vector3 getPosition() {
        return this.instance.transform.getTranslation(new Vector3());
    }

    /**
     * Get the model of the golfball
     * @return a model object
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Get the instance of the model of the golfball
     * @return a modelinstance object
     */
    public ModelInstance getInstance() {
        return this.instance;
    }

    /**
     * Set the camera to follow the golfball
     * @param cam a camera reference
     */
    public void setCamera(Camera cam) {
        this.cam = cam;
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
        this.util.set(this.getPosition());
		this.setPosition(this.util.x + dX, Terrain.getHeight(this.util.x + dX, this.util.z + dZ) + SIZE / 2, this.util.z + dZ);
		this.cam.translate(this.getPosition().sub(this.util));
		this.cam.update();
        return this.getPosition();
    }

    /**
     * Move the golball. The y-coordinate gets calculated based on the height function.
     * @param x x-coordinate
     * @param z z-coordinate
     * @return position for chaining
     */
    public Vector3 moveTo(float x, float z) {
        this.util.set(this.getPosition());
        return this.move(x - this.util.x, z - this.util.z);
    }

    /**
     * Update the golfball to the statevector.
     */
    public void updateState() {
        this.moveTo(this.STATE.x, this.STATE.y);
    }

    public static boolean treeCollide = false;
}
