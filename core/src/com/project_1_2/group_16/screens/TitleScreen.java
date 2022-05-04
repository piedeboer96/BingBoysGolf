package com.project_1_2.group_16.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.project_1_2.group_16.App;

public class TitleScreen extends ScreenAdapter {
    
    /**
     * Reference the the main application.
     */
    private App app;

    // different screens used for the titlescreen
    private List<InputScreen> screens;
    private InputScreen activeScreen;

    // utils for starting the game
    private boolean startGame;
    private int cntr;
    
    /**
     * Skin used for all screen components.
     */
    public Skin skin;

    private final Color CLEAR = new Color(59f/255f,87f/255f,72f/255f,1);

    public TitleScreen(App app) {
        this.app = app;
    }

    /**
     * Create all components.
     */
    public void create() {
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        this.screens = new ArrayList<InputScreen>();
        this.screens.add(new MainStage(this));
        this.screens.add(new AdvancedStage(this));
        this.screens.add(new TerrainStage(this));
    }

    @Override
    public void show() {
        this.setActiveScreen(InputScreen.MAIN);
    }

    @Override
    public void render(float delta) {
        // clear screen
        if (App.OS_IS_WIN) Gdx.gl.glViewport(0, 0, App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		ScreenUtils.clear(this.CLEAR);

        // draw the active inputscreen
        this.app.spriteBatch.begin();
        this.activeScreen.draw();
        this.app.spriteBatch.end();

        // start the game
        if (this.startGame && this.cntr++ > 1) {
            // parse all inputs to the input file
            for (InputScreen s : this.screens) {
                s.parseInputs();
            }
            
            this.app.GAME_SCREEN.create();
            this.app.setScreen(this.app.GAME_SCREEN);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Start the game.
     */
    public void startGame() {
        this.startGame = true;
    }

    /**
     * Set the screen to be displayed to the user
     * @param screen InputScreen.MAIN / ADVANCED_SETTINGS / TERRAIN_SETTINGS
     */
    public void setActiveScreen(int screen) {
        this.activeScreen = this.screens.get(screen);
        Gdx.input.setInputProcessor(this.activeScreen);
    }
}
