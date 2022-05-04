package com.project_1_2.group_16.screens;

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
    protected void init() {
        // all backgrounds are created using this code:
        // https://stackoverflow.com/questions/16886228/java-libgdx-how-do-i-resize-my-textures-in-libgdx
    }

    /**
     * Parse all inputs from the user to the input file.
     */
    protected void parseInputs() {}

    /**
     * Respond to key presses.
     * @param keyCode the code of the key pressed
     */
    protected void keyInput(int keyCode) {}

    @Override
    public boolean keyDown(int keyCode) {
        this.keyInput(keyCode);
        return true;
    }
}
