package com.project_1_2.group_16.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.io.Input;
import com.project_1_2.group_16.io.LevelSelector;
import com.project_1_2.group_16.misc.InfoDialog;

/**
 * Stage used as the main title screen.
 */
public class MainStage extends InputScreen {

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

    private TextButton controls;
    private TextButton loadLevel;

    public MainStage(TitleScreen screen) {
        super(screen);
        super.stage = this;
    }

    @Override
    protected void init() {
        // background
        this.addActor(this.screen.getBackground());

        // play button
        this.play = new TextButton("Play", this.screen.skin);
        this.play.setHeight(50);
        this.play.setWidth(320);
        this.play.setPosition(App.SCREEN_WIDTH / 2 + this.play.getWidth() / 2, 75, Align.right);
        this.play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });
        this.addActor(this.play);

        // controls
        this.controls = new TextButton("Controls", this.screen.skin);
        this.controls.setHeight(50);
        this.controls.setWidth(200);
        this.controls.setPosition(this.play.getX(Align.right) + 10, 75, Align.left);
        this.controls.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("MAIN CONTROLS:");
                infoDialog.addText("ESC - Quit the application");
                infoDialog.addText("C - Switch camera");
                infoDialog.addText("R - Reset ball");
                infoDialog.addText("Ctrl + S - Save level");
                infoDialog.addText("");
                infoDialog.addText("BALL CAMERA CONTROLS:");
                infoDialog.addText("Drag the mouse to move the camera");
                infoDialog.addText("Hold SPACE to shoot the ball");
                infoDialog.addText("(the ball will be shot in the direction of the camera)");
                infoDialog.addText("");
                infoDialog.addText("CINEMATIC CAMERA CONTROLS:");
                infoDialog.addText("W A S D / Arrow keys - Move around");
                infoDialog.addText("SPACE - Go up");
                infoDialog.addText("SHIFT - Go down");
                infoDialog.addText("");
                infoDialog.addText("BOT CONTROLS");
                infoDialog.addText("1 - Simulated Annealing");
                infoDialog.addText("2 - Battle Royale Optimization");
                infoDialog.addText("3 - Particle Swarm Optimization");
                infoDialog.addText("4 - Rule-Based bot");
                infoDialog.addText("5 - Random bot");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.controls);

        // button for loading a level
        this.loadLevel = new TextButton("Load Level", this.screen.skin);
        this.loadLevel.setHeight(50);
        this.loadLevel.setWidth(200);
        this.loadLevel.setPosition(this.play.getX(Align.left) - 10, 75, Align.right);
        this.loadLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LevelSelector selector = new LevelSelector(screen);
                selector.show(stage);
            }
        });
        this.addActor(this.loadLevel);

        // button for accessing terrain settings
        this.terrainSettings = new TextButton("Terrain Settings", this.screen.skin);
        this.terrainSettings.setHeight(50);
        this.terrainSettings.setWidth(250);
        this.terrainSettings.setPosition(0.9f*App.SCREEN_WIDTH, 500, Align.center);
        this.terrainSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.setActiveScreen(InputScreen.TERRAIN_SETTINGS);
            }
        });
        this.addActor(this.terrainSettings);

        // button for accessing advanced settings
        this.advancedSettings = new TextButton("Advanced Settings", this.screen.skin);
        this.advancedSettings.setHeight(50);
        this.advancedSettings.setWidth(250);
        this.advancedSettings.setPosition(0.9f*App.SCREEN_WIDTH, 300, Align.center);
        this.advancedSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.setActiveScreen(InputScreen.ADVANCED_SETTINGS);
            }
        });
        this.addActor(this.advancedSettings);

        // input for the starting position (x)
        this.v0xField = new TextField("", this.screen.skin);
        this.v0xField.setPosition(0.1f*App.SCREEN_WIDTH, 600, Align.center);
        this.v0xLabel = new Label("Initial x position", this.screen.skin);
        this.v0xLabel.setColor(Color.BLACK);
        this.v0xLabel.setPosition(this.v0xField.getX(Align.center), this.v0xField.getY(Align.center) + this.v0xField.getHeight(), Align.center);
        this.addActor(this.v0xField); this.addActor(this.v0xLabel);

        // input for the starting position (y)
        this.v0yField = new TextField("", this.screen.skin);
        this.v0yField.setPosition(0.2f*App.SCREEN_WIDTH, 600, Align.center);
        this.v0yLabel = new Label("Initial y position", this.screen.skin);
        this.v0yLabel.setColor(Color.BLACK);
        this.v0yLabel.setPosition(this.v0yField.getX(Align.center), this.v0yField.getY(Align.center) + this.v0yField.getHeight(), Align.center);
        this.addActor(this.v0yField); this.addActor(this.v0yLabel);

        // input for the hole position (x)
        this.vtxField = new TextField("", this.screen.skin);
        this.vtxField.setPosition(0.1f*App.SCREEN_WIDTH, 400, Align.center);
        this.vtxLabel = new Label("Hole x position", this.screen.skin);
        this.vtxLabel.setColor(Color.BLACK);
        this.vtxLabel.setPosition(this.vtxField.getX(Align.center), this.vtxField.getY(Align.center) + this.vtxField.getHeight(), Align.center);
        this.addActor(this.vtxField); this.addActor(this.vtxLabel);

        // input for the hole position (y)
        this.vtyField = new TextField("", this.screen.skin);
        this.vtyField.setPosition(0.2f*App.SCREEN_WIDTH, 400, Align.center);
        this.vtyLabel = new Label("Hole y position", this.screen.skin);
        this.vtyLabel.setColor(Color.BLACK);
        this.vtyLabel.setPosition(this.vtyField.getX(Align.center), this.vtyField.getY(Align.center) + this.vtyField.getHeight(), Align.center);
        this.addActor(this.vtyField); this.addActor(this.vtyLabel);

        // input for the hole radius
        this.rField = new TextField("", this.screen.skin);
        this.rField.setPosition((this.vtxField.getX(Align.center)+this.vtyField.getX(Align.center))/2, 200, Align.center);
        this.rLabel = new Label("Hole radius", this.screen.skin);
        this.rLabel.setColor(Color.BLACK);
        this.rLabel.setPosition(this.rField.getX(Align.center), this.rField.getY(Align.center) + this.rField.getHeight(), Align.center);
        this.addActor(this.rField); this.addActor(this.rLabel);

        // label to notify the user that the game is loading
        this.loading = new Label("Loading game... (this may take a few seconds)", this.screen.skin);
        this.loading.setColor(Color.BLACK);
        this.loading.setPosition(App.SCREEN_WIDTH - 10, 0, Align.bottomRight);
    }

    @Override
    protected void parseInputs() {
        Input.V0 = new Vector2(Float.parseFloat(this.v0xField.getText()), Float.parseFloat(this.v0yField.getText()));
        Input.VT = new Vector2(Float.parseFloat(this.vtxField.getText()), Float.parseFloat(this.vtyField.getText()));
        Input.R = Float.parseFloat(this.rField.getText());
    }

    @Override
    protected void setValues() {
        this.v0xField.setText(Float.toString(Input.V0.x));
        this.v0yField.setText(Float.toString(Input.V0.y));
        this.vtxField.setText(Float.toString(Input.VT.x));
        this.vtyField.setText(Float.toString(Input.VT.y));
        this.rField.setText(Float.toString(Input.R));
    }

    @Override
    protected void keyInput(int keyCode) {
        if (keyCode == Keys.ESCAPE) {
            Gdx.app.exit();
            System.exit(0);
        }
        if (keyCode == Keys.ENTER) {
            this.startGame();
        }
    }

    /**
     * Start the game.
     */
    private void startGame() {
        this.addActor(this.loading);
        this.screen.startGame();
    }
}
