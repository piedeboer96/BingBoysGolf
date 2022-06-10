package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.io.Input;

/**
 * Wrapper class for the flap pole and hole.
 */
public class Flagpole {

    private Model model;
    private ModelInstance instance;
    private float r;
    private Vector3 pos = new Vector3();

    /**
     * Create a flagpole object, which represents the flagpole and hole visible on the course. 
     * Has attributes used both in front- and back-end.
     * @param pos the position of the hole
     * @param r the radius of the hole
     * @param off if the flagpole texture is set on or off
     */
    public Flagpole(Vector3 pos, float r, boolean off) {
        this.model = Input.THEME.flagModel(r, off);
        this.instance = new ModelInstance(this.model);
        this.r = r;
        this.pos.set(pos);
        this.instance.transform.setTranslation(this.pos);
    }

    /**
     * Get the position of the flagpole.
     * @return a three-dimensional vector
     */
    public Vector3 getPosition() {
        return this.pos;
    }

    /**
     * Get the model of the flagpole
     * @return a model object
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Get the model instance of the model
     * @return a modelinstance object
     */
    public ModelInstance getInstance() {
        return this.instance;
    }

    /**
     * Get the radius of the hole
     * @return a floating point number
     */
    public float getRadius() {
        return this.r;
    }

    /**
     * Rotate the flag to face towards the golfball
     * @param golfball the position of the golfball
     * @param face the direction the flag is facing
     */
    public void rotateTowardsGolfball(Vector3 golfball, Vector3 face) {
		this.instance.transform.rotate(Vector3.Y,(this.pos.x<golfball.x?90:270)+57.2958f*angleBetween(this.pos,golfball,face));
    }

    /**
	 * Calculates the angle between two points, as seen from an origin.
	 * @param origin the origin, the angle will be viewed from this point
	 * @param v1 the first three-dimensional vector
	 * @param v2 the second three-dimensional vector
	 * @return the angle between v1 and v2 between origin
	 */
	private float angleBetween(Vector3 origin, Vector3 v1, Vector3 v2) {
        // remove the y-coordinate from all vectors
		Vector2 origin2 = new Vector2(origin.x, origin.z);
		Vector2 v12 = new Vector2(v1.x, v1.z);
		Vector2 v22 = new Vector2(v2.x, v2.z);

        // distances between all points
		float a = origin2.dst(v12);
		float b = origin2.dst(v22);
		float c = v12.dst(v22);

        // calculate the angle using the law of cosines
		return (float)Math.acos((a*a + b*b - c*c) / (2 * a * b));
	}
}
