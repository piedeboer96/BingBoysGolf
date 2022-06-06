package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.gamelogic.Terrain;

/**
 * Wrapper class for trees.
 */
public class Tree {

    /**
     * Energy lost by hitting trees.
     */
    public static final float treeCoefficient = 0.8f;

    public static boolean recentlyHitTree;

    private ModelInstance instance;
    private Vector3 pos;
    private float r;

    /**
     * Create a tree object
     * @param x the x-coordinate of the tree
     * @param y the y-coordinate of the tree
     * @param r the radius of the tree
     */
    public Tree(float x, float y, float r) {
        this.pos = new Vector3(x, 0, y);
        this.r = r;
    }

    /**
     * Set the model of this tree.
     * @param model a model reference
     */
    public void setModel(Model model) {
        this.instance = new ModelInstance(model);
        this.pos.y = Terrain.getHeight(this.pos.x, this.pos.z) - 0.1f;
        this.instance.transform.translate(this.pos);
        this.instance.transform.rotate(Vector3.Y, (float)(Math.random()*360));
        this.instance.transform.scale(0.5f * r, 0.5f * r, 0.5f * r);
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
