package com.project_1_2.group_16.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.misc.LevelDecoder;

/**
 * The first screen that appears when the app is launched.
 */
public class TitleScreen extends ScreenAdapter {
    
    /**
     * Reference to the main application.
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

    public TitleScreen(App app) {
        this.app = app;
    }

    /**
     * Create all components.
     */
    public void create() {
        // https://stackoverflow.com/a/38401475
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        this.skin.getFont("default-font").getData().markupEnabled = true;

        this.screens = new ArrayList<InputScreen>();
        this.screens.add(new MainStage(this));
        this.screens.add(new AdvancedStage(this));
        this.screens.add(new TerrainStage(this));

        // set default values
        this.loadLevel(new FileHandle((App.OS_IS_WIN ? "./" : "../")+"default_level.json"));
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
     * Get all different screens this titlescreen has.
     * @return
     */
    public List<InputScreen> getScreens() {
        return this.screens;
    }

    /**
     * Get the background for all input screens
     * @return
     */
    public Image getBackground() {
        // https://stackoverflow.com/questions/16886228/java-libgdx-how-do-i-resize-my-textures-in-libgdx
        Pixmap p1 = new Pixmap(Gdx.files.internal("background.png"));
        Pixmap p2 = new Pixmap(App.SCREEN_WIDTH, App.SCREEN_HEIGHT, p1.getFormat());
        p2.drawPixmap(p1, 0, 0, p1.getWidth(), p1.getHeight(), 0, 0, p2.getWidth(), p2.getHeight());
        return new Image(new Texture(p2));
    }

    /**
     * Set the screen to be displayed to the user
     * @param screen InputScreen.MAIN / ADVANCED_SETTINGS / TERRAIN_SETTINGS
     */
    public void setActiveScreen(int screen) {
        this.activeScreen = this.screens.get(screen);
        Gdx.input.setInputProcessor(this.activeScreen);
    }

    /**
     * Load a pre-computed level.
     * @param level file containing the level data
     */
    public void loadLevel(FileHandle level) {
        // decode level
        LevelDecoder.decode(level);

        // update screens
        for (InputScreen s : this.screens) {
            s.setValues();
        }
    }
}
