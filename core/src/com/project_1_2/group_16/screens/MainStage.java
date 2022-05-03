package com.project_1_2.group_16.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;

public class MainStage extends Stage {

    private TitleScreen screen;

    private TextButton play;
    private TextButton advancedSettings;
    private TextButton terrainSettings;

    private Label v0xLabel;
    private Label v0yLabel;
    private TextField v0xField;
    private TextField v0yField;

    private Label vtxLabel;
    private Label vtyLabel;
    private TextField vtxField;
    private TextField vtyField;

    private Label rLabel;
    private TextField rField;

    private Label loading;
    
    public MainStage(TitleScreen screen) {
        this.screen = screen;
        this.init();
    }

    private void init() {
        // play button
        this.play = new TextButton("Play", this.screen.app.skin);
        this.play.setHeight(50);
        this.play.setWidth(500);
        this.play.moveBy(App.SCREEN_WIDTH / 2 - this.play.getWidth() / 2, 50);
        this.play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });
        this.addActor(this.play);

        // button for accessing terrain settings
        this.terrainSettings = new TextButton("Terrain Settings", this.screen.app.skin);
        this.terrainSettings.setHeight(50);
        this.terrainSettings.setWidth(250);
        this.terrainSettings.moveBy(0.9f*App.SCREEN_WIDTH - this.terrainSettings.getWidth()/2, 500);
        this.terrainSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.activeScreen = screen.terrain.getClass();
            }
        });
        this.addActor(this.terrainSettings);

        // button for accessing advanced settings
        this.advancedSettings = new TextButton("Advanced Settings", this.screen.app.skin);
        this.advancedSettings.setHeight(50);
        this.advancedSettings.setWidth(250);
        this.advancedSettings.moveBy(0.9f*App.SCREEN_WIDTH - this.advancedSettings.getWidth()/2, 300);
        this.advancedSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.activeScreen = screen.advanced.getClass();
            }
        });
        this.addActor(this.advancedSettings);

        // input for the starting position (x)
        this.v0xField = new TextField(Float.toString(Input.V0.x), this.screen.app.skin);
        this.v0xField.moveBy(0.1f*App.SCREEN_WIDTH - this.v0xField.getWidth()/2, 600);
        this.v0xLabel = new Label("V0X", this.screen.app.skin);
        this.v0xLabel.setColor(Color.BLACK);
        this.v0xLabel.moveBy(this.v0xField.getX() + this.v0xField.getWidth()/2 - this.v0xLabel.getWidth()/2, this.v0xField.getY() + this.v0xField.getHeight());
        this.addActor(this.v0xField); this.addActor(this.v0xLabel);

        // input for the starting position (y)
        this.v0yField = new TextField(Float.toString(Input.V0.y), this.screen.app.skin);
        this.v0yField.moveBy(0.1f*App.SCREEN_WIDTH + this.v0yField.getWidth()/2 + 0.05f*App.SCREEN_WIDTH, 600);
        this.v0yLabel = new Label("V0Y", this.screen.app.skin);
        this.v0yLabel.setColor(Color.BLACK);
        this.v0yLabel.moveBy(this.v0yField.getX() + this.v0yField.getWidth()/2 - this.v0yLabel.getWidth()/2, this.v0yField.getY() + this.v0yField.getHeight());
        this.addActor(this.v0yField); this.addActor(this.v0yLabel);

        // input for the hole position (x)
        this.vtxField = new TextField(Float.toString(Input.VT.x), this.screen.app.skin);
        this.vtxField.moveBy(0.1f*App.SCREEN_WIDTH - this.vtxField.getWidth()/2, 400);
        this.vtxLabel = new Label("VTX", this.screen.app.skin);
        this.vtxLabel.setColor(Color.BLACK);
        this.vtxLabel.moveBy(this.vtxField.getX() + this.vtxField.getWidth()/2 - this.vtxLabel.getWidth()/2, this.vtxField.getY() + this.vtxField.getHeight());
        this.addActor(this.vtxField); this.addActor(this.vtxLabel);

        // input for the hole position (y)
        this.vtyField = new TextField(Float.toString(Input.VT.y), this.screen.app.skin);
        this.vtyField.moveBy(0.1f*App.SCREEN_WIDTH + this.vtyField.getWidth()/2 + 0.05f*App.SCREEN_WIDTH, 400);
        this.vtyLabel = new Label("VTY", this.screen.app.skin);
        this.vtyLabel.setColor(Color.BLACK);
        this.vtyLabel.moveBy(this.vtyField.getX() + this.vtyField.getWidth()/2 - this.vtyLabel.getWidth()/2, this.vtyField.getY() + this.vtyField.getHeight());
        this.addActor(this.vtyField); this.addActor(this.vtyLabel);

        // input for the hole radius
        this.rField = new TextField(Float.toString(Input.R), this.screen.app.skin);
        this.rField.moveBy((this.vtxField.getX()+this.vtxField.getWidth()/2+this.vtyField.getX()+this.vtyField.getWidth()/2)/2-this.rField.getWidth()/2, 200);
        this.rLabel = new Label("R", this.screen.app.skin);
        this.rLabel.setColor(Color.BLACK);
        this.rLabel.moveBy(this.rField.getX() + this.rField.getWidth()/2 - this.rLabel.getWidth()/2, this.rField.getY() + this.rField.getHeight());
        this.addActor(this.rField); this.addActor(this.rLabel);

        // ...

        // label to notify the user that the game is loading
        this.loading = new Label("Loading game...", this.screen.app.skin);
        this.loading.setColor(Color.BLACK);
        this.loading.moveBy(App.SCREEN_WIDTH - this.loading.getWidth() - 10, 0);
    }

    public void parseInputs() {
        Input.V0 = new Vector2(Float.parseFloat(this.v0xField.getText()), Float.parseFloat(this.v0yField.getText()));
        Input.VT = new Vector2(Float.parseFloat(this.vtxField.getText()), Float.parseFloat(this.vtyField.getText()));
        Input.R = Float.parseFloat(this.rField.getText());
    }

    private void startGame() {
        this.addActor(this.loading);
        this.screen.startGame = true;
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Keys.ESCAPE) {
            Gdx.app.exit();
            System.exit(0);
        }
        if (keyCode == Keys.ENTER) {
            this.startGame();
        }
        return true;
    }
}
