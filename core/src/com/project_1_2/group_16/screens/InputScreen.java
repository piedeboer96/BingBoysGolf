package com.project_1_2.group_16.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.project_1_2.group_16.App;

/**
 * Abstract class that represents the stage of an inputscreen.
 * The title screen can display children of this class seperately.
 */
public abstract class InputScreen extends Stage {

    /**
     * Reference to the screen for which this stage was created.
     */
    protected TitleScreen screen;

    /**
     * Reference to the sub-class of this inputscreen.
     */
    protected Stage stage;

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

    private TextButton loadLevel;
    private TextButton saveLevel;
    
    public InputScreen(TitleScreen screen) {
        this.screen = screen;
        this.init();
        this.setupLevelButtons();
    }

    /**
     * Init all components that this stage uses.
     */
    protected abstract void init();

    /**
     * Parse all inputs from the user to the input file.
     */
    protected abstract void parseInputs();

    /**
     * Set all values to the current values from the input file.
     */
    protected abstract void setValues();

    /**
     * Respond to key presses.
     * @param keyCode the code of the key pressed
     */
    protected abstract void keyInput(int keyCode);

    @Override
    public boolean keyDown(int keyCode) {
        this.keyInput(keyCode);
        return true;
    }

    /**
     * Setup the load/save level buttons on this input screen.
     */
    private void setupLevelButtons() {
        // load level button
        this.loadLevel = new TextButton("Load Level", this.screen.skin);
        this.loadLevel.setHeight(40);
        this.loadLevel.setWidth(240);
        this.loadLevel.setPosition(App.SCREEN_WIDTH/2 - 10, 40, Align.right);
        this.loadLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("[BLUE]TODO");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.loadLevel);

        // save level button
        this.saveLevel = new TextButton("Save Level", this.screen.skin);
        this.saveLevel.setHeight(40);
        this.saveLevel.setWidth(240);
        this.saveLevel.setPosition(App.SCREEN_WIDTH/2 + 10, 40, Align.left);
        this.saveLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("[BLUE]TODO");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.saveLevel);
    }
}
