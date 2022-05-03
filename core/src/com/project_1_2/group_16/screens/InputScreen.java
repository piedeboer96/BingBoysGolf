package com.project_1_2.group_16.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class InputScreen extends Stage {

    /**
     * Reference to the screen for which this stage was created.
     */
    protected TitleScreen screen;

    /**
     * The main screen.
     */
    public static final int MAIN = 0;

    /**
     * The advanced-settings screen.
     */
    public static final int ADVANCED_SETTINGS = 1;

    /**
     * The terrain-settings screen.
     */
    public static final int TERRAIN_SETTINGS = 2;
    
    public InputScreen(TitleScreen screen) {
        this.screen = screen;
        this.init();
    }

    /**
     * Init all components that this stage uses.
     */
    protected void init() {}

    /**
     * Parse all inputs from the user to the input file.
     */
    protected void parseInputs() {}

    /**
     * Respond to key presses.
     * @param keyCode the code of the key pressed
     */
    protected void keyInput(int keyCode) {}

    /**
     * Get the background color of this stage
     * @return a Color reference
     */
    protected Color getBackground() {
        return Color.WHITE;
    }

    @Override
    public boolean keyDown(int keyCode) {
        this.keyInput(keyCode);
        return true;
    }
}
