package com.project_1_2.group_16.camera;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.misc.PowerStatus;
import com.project_1_2.group_16.models.Golfball;

public class BallCamera implements InputProcessor {

    private Camera camera;
    private Golfball golfBall;
    private PowerStatus powerStatus;
    private Vector3 util = new Vector3();
    private float startX, startY;

    public BallCamera(Camera camera, Golfball golfBall) {
        this.camera = camera;
        this.golfBall = golfBall;
    }

    /**
     * Get the power status of shooting the ball.
     * @return
     */
    public PowerStatus getPowerStatus() {
        return this.powerStatus;
    }

    /**
     * Set the power status of shooting the ball.
     * @param powerStatus the new power status
     */
    public void setPowerStatus(PowerStatus powerStatus) {
        this.powerStatus = powerStatus;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.startX = screenX;
		this.startY = screenY;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float deltaX = (screenX - this.startX) / App.SCREEN_WIDTH;
		float deltaY = (this.startY - screenY) / App.SCREEN_HEIGHT;
		this.startX = screenX;
		this.startY = screenY;
        this.util.set(camera.direction).crs(camera.up).y = 0f;
		this.camera.rotateAround(this.golfBall.getPosition(), this.util.nor(), deltaY * 360);
		this.camera.rotateAround(this.golfBall.getPosition(), Vector3.Y, deltaX * -360);
        this.camera.update();
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            this.powerStatus = PowerStatus.POWER_UP;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            this.powerStatus = PowerStatus.SHOOT;
            return true;
        }
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
