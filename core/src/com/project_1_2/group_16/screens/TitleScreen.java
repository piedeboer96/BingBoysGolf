package com.project_1_2.group_16.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.project_1_2.group_16.App;

public class TitleScreen extends ScreenAdapter {
    
    public App app;

    public MainStage main;
    public AdvancedStage advanced;
    public TerrainStage terrain;

    public Class<?> activeScreen;

    public boolean startGame;
    private int cntr;

    public TitleScreen(App app) {
        this.app = app;
    }

    public void create() {
        this.main = new MainStage(this);
        this.advanced = new AdvancedStage(this);
        this.terrain = new TerrainStage(this);

        this.activeScreen = this.main.getClass();
    }

    @Override
    public void render(float delta) {
        if (App.OS_IS_WIN) Gdx.gl.glViewport(0, 0, App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		ScreenUtils.clear(Color.WHITE);

        this.app.spriteBatch.begin();
        if (this.activeScreen.isInstance(this.main)) {
            Gdx.input.setInputProcessor(this.main);
            this.main.draw();
        }
        if (this.activeScreen.isInstance(this.advanced)) {
            Gdx.input.setInputProcessor(this.advanced);
            this.advanced.draw();
        }
        if (this.activeScreen.isInstance(this.terrain)) {
            Gdx.input.setInputProcessor(this.terrain);
            this.terrain.draw();
        }
        this.app.spriteBatch.end();

        if (this.startGame && this.cntr++ > 1) {
            this.main.parseInputs();
            this.advanced.parseInputs();
            this.terrain.parseInputs();
            
            this.app.GAME_SCREEN.create();
            this.app.setScreen(this.app.GAME_SCREEN);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
