package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

// This class will create a custom model instance which will be used to store triangle models
// It is useful to do frustum culling which improves the performance of the GUI
public class TriangleInstance extends ModelInstance {

    private final Vector3 dim = new Vector3();
    static BoundingBox bounds = new BoundingBox();

    /**
     * Store the model inside the model instance and calculate bounds for the frustum culling
     * @param  triangleModel is the two connected triangles that the triangle class has made
     */
    public TriangleInstance(Model triangleModel) {
        super(triangleModel);

        calculateBoundingBox( TriangleInstance.bounds);
        TriangleInstance.bounds.getDimensions(dim);
    }

    /**
     * Get the dimensions of the triangle model.
     * @return a three-dimensional vector
     */
    public Vector3 getDimensions() {

        return this.dim;
    }

}
