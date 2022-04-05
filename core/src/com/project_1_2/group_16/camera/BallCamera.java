package com.project_1_2.group_16.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.models.Golfball;

public class BallCamera implements InputProcessor {

    /**
     * Camera reference.
     */
    private Camera camera;

    /**
     * Golfball reference.
     */
    private Golfball golfBall;

    // utils
    private final Vector3 v = new Vector3();
    private float startX, startY;

    public BallCamera(Camera camera, Golfball golfBall) {
        this.camera = camera;
        this.golfBall = golfBall;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.startX = screenX;
		this.startY = screenY;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        final float deltaX = (screenX - this.startX) / Gdx.graphics.getWidth();
		final float deltaY = (this.startY - screenY) / Gdx.graphics.getHeight();
		this.startX = screenX;
		this.startY = screenY;
        this.v.set(camera.direction).crs(camera.up).y = 0f;
		this.camera.rotateAround(this.golfBall.getPosition(), this.v.nor(), deltaY * 360);
		this.camera.rotateAround(this.golfBall.getPosition(), Vector3.Y, deltaX * -360);
        this.camera.update();
        return true;
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
