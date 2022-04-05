package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Flagpole {

    /**
     * The model.
     */
    public Model model;

    /**
     * The modelinstance.
     */
    public ModelInstance instance;

    /**
     * Radius of the hole
     */
    public float r;

    // util
    private final Vector3 pos = new Vector3();
    
    public Flagpole(Model model, Vector3 pos, float r) {
        this.model = model;
        this.instance = new ModelInstance(this.model);
        this.r = r;
        this.pos.set(pos);

        this.instance.transform.setTranslation(this.pos);
        this.instance.transform.scale(0.001f, 0.001f, 0.001f);
    }

    /**
     * Get the position of the flagpole.
     * @return a three-dimensional vector
     */
    public Vector3 getPosition() {
        return this.pos;
    }

    /**
     * Rotate the flag to face towards the golfball
     * @param golfball the position of the golfball
     * @param face the direction the flag is facing
     */
    public void rotateTowardsGolfball(Vector3 golfball, Vector3 face) {
        if (this.pos.x < golfball.x) {
			this.instance.transform.rotate(Vector3.Y, 90+57.2958f*angleBetween(this.pos, golfball, face));
		}
		else {
			this.instance.transform.rotate(Vector3.Y, 270-57.2958f*angleBetween(this.pos, golfball, face));
		}
    }

    /**
	 * Calculates the angle between two points, as seen from an origin.
	 * @param origin the origin, the angle will be viewed from this point
	 * @param v1 the first three-dimensional vector
	 * @param v2 the second three-dimensional vector
	 * @return the angle between v1 and v2 between origin
	 */
	private float angleBetween(Vector3 origin, Vector3 v1, Vector3 v2) {
		Vector2 origin2 = new Vector2(origin.x, origin.z);
		Vector2 v12 = new Vector2(v1.x, v1.z);
		Vector2 v22 = new Vector2(v2.x, v2.z);
		float a = origin2.dst(v12);
		float b = origin2.dst(v22);
		float c = v12.dst(v22);
		return (float)Math.acos((a*a + b*b - c*c) / (2 * a * b)); // law of cosines
	}
}
