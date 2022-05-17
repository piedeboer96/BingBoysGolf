package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Wrapper class for trees.
 */
public class Tree {

    /**
     * Energy lost by hitting trees.
     */
    public static final float treeCoefficient = 0.8f;

    public static boolean recentlyHitTree;

    private Model model;
    private ModelInstance instance;
    private Vector3 pos = new Vector3();
    private float r;

    /**
     * Create a tree object
     * @param model the tree model
     * @param pos the position of the tree
     * @param r the radius of the tree
     */
    public Tree(Model model, Vector3 pos, float r) {
        this.model = model;
        this.instance = new ModelInstance(model);
        this.pos.set(pos);
        this.r = r;

        this.instance.transform.translate(pos.x, pos.y, pos.z);
		this.instance.transform.rotate(Vector3.Y, (float)(Math.random()*360));
		this.instance.transform.scale(0.5f * r, 0.5f * r, 0.5f * r);
    }

    /**
     * Create tree object without interfering with graphics
     */
    public Tree(Vector3 pos, float r){
        this.pos.set(pos);
        this.r = r;
    }
    
    /**
     * Get the model of the tree
     * @return a model object
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Get the instance of the model of the tree
     * @return a modelinstance object
     */
    public ModelInstance getInstance() {
        return this.instance;
    }

    /**
     * Get the position of the tree
     * @return a 3d vector
     */
    public Vector3 getPosition() {
        return this.pos;
    }

    /**
     * Get the radius of the tree
     * @return a floating point number
     */
    public float getRadius() {
        return this.r;
    }
}
