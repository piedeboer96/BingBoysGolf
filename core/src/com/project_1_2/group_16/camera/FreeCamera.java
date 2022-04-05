package com.project_1_2.group_16.camera;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

// parts of code taken from: https://stackoverflow.com/a/34058580
public class FreeCamera implements InputProcessor {

    /**
     * Camera reference.
     */
    private Camera camera;

    /**
     * The camera sensitivity.
     */
	private final float rotateSpeed = 0.1f;

    /**
     * How quick the camera moves.
     */
    private final float movementSpeed = 1.5f;

    // utils
    private final Vector3 v = new Vector3();
    private int dragX, dragY;

    public FreeCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
		// rotating on the y axis
		float x = this.dragX - screenX;
		this.camera.rotate(Vector3.Y, x * rotateSpeed);
	
		// rotating on the x and z axis
		float y = (float)Math.sin((double)(this.dragY -screenY) / 180f);
		if (Math.abs(this.camera.direction.y + y * (rotateSpeed * 5.0f)) < 0.9) {
			this.camera.direction.y +=  y * (rotateSpeed * 5.0f) ;
		}
	
        // update camera
		this.camera.update();
		this.dragX = screenX;
		this.dragY = screenY;
		return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.mouseMoved(screenX, screenY);
        return true;
    }

    /**
     * The controls for the free camera.
     * @param input input reference
     * @param dT the time since the last frame
     */
    public void move(Input input, float dT) {
        if (input.isKeyPressed(Keys.W)) { // move forward
            this.v.set(this.camera.direction);
            this.v.x *= movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= movementSpeed * dT;
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.UP)) { // move forward
            this.v.set(this.camera.direction);
            this.v.x *= movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= movementSpeed * dT;
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.S)) { // move backward
            this.v.set(this.camera.direction);
            this.v.x *= -movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= -movementSpeed * dT;
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.DOWN)) { // move backward
            this.v.set(this.camera.direction);
            this.v.x *= -movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= -movementSpeed * dT;
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.A)) { // move left
            this.v.set(this.camera.direction);
            this.v.x *= movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= movementSpeed * dT;
            this.v.rotate(Vector3.Y, 90);
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.LEFT)) { // move left
            this.v.set(this.camera.direction);
            this.v.x *= movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= movementSpeed * dT;
            this.v.rotate(Vector3.Y, 90);
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.D)) { // move right
            this.v.set(this.camera.direction);
            this.v.x *= movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= movementSpeed * dT;
            this.v.rotate(Vector3.Y, -90);
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.RIGHT)) { // move right
            this.v.set(this.camera.direction);
            this.v.x *= movementSpeed * dT;
            this.v.y = 0f;
            this.v.z *= movementSpeed * dT;
            this.v.rotate(Vector3.Y, -90);
            this.camera.translate(v);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.SPACE)) { // move up
            this.camera.translate(0, movementSpeed * dT, 0);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.SHIFT_LEFT)) { // move down
            this.camera.translate(0, -movementSpeed * dT, 0);
            this.camera.update();
        }
        if (input.isKeyPressed(Keys.SHIFT_RIGHT)) { // move down
            this.camera.translate(0, -movementSpeed * dT, 0);
            this.camera.update();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
