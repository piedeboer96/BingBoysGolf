package com.project_1_2.group_16.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;

public class TerrainStage extends InputScreen {

    private TextButton back;

    private Label functionLabel;
    private TextField functionField;

    private Label treeLabel;
    private TextField treeField;
    
    private Label sandLabel;
    private TextField sandField;
    
    public TerrainStage(TitleScreen screen) {
        super(screen);
    }

    @Override
    protected void init() {
        // background
        this.addActor(App.THEME.getTerrainBackground());

        // back button
        this.back = new TextButton("Back", this.screen.skin);
        this.back.setHeight(50);
        this.back.setWidth(500);
        this.back.setPosition(App.SCREEN_WIDTH / 2, 75, Align.center);
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.setActiveScreen(InputScreen.MAIN);
            }
        });
        this.addActor(this.back);

        // input for the height function
        this.functionField = new TextField(Input.H, this.screen.skin);
        this.functionField.setHeight(50);
        this.functionField.setWidth(500);
        this.functionField.setPosition(App.SCREEN_WIDTH / 2, 600, Align.center);
        this.functionLabel = new Label("Height function", this.screen.skin);
        this.functionLabel.setColor(Color.BLACK);
        this.functionLabel.setPosition(this.functionField.getX(Align.center), this.functionField.getY(Align.center) + this.functionField.getHeight(), Align.center);
        this.addActor(this.functionField); this.addActor(this.functionLabel);

        // input for the number of trees
        this.treeField = new TextField(Integer.toString(Input.TREES), this.screen.skin);
        this.treeField.setPosition(0.4f*App.SCREEN_WIDTH, 400, Align.center);
        this.treeLabel = new Label("Trees", this.screen.skin);
        this.treeLabel.setColor(Color.BLACK);
        this.treeLabel.setPosition(this.treeField.getX(Align.center), this.treeField.getY(Align.center) + this.treeField.getHeight(), Align.center);
        this.addActor(this.treeField); this.addActor(this.treeLabel);

        // input for the number of sandpits
        this.sandField = new TextField(Integer.toString(Input.SAND), this.screen.skin);
        this.sandField.setPosition(0.6f*App.SCREEN_WIDTH, 400, Align.center);
        this.sandLabel = new Label("Sand", this.screen.skin);
        this.sandLabel.setColor(Color.BLACK);
        this.sandLabel.setPosition(this.sandField.getX(Align.center), this.sandField.getY(Align.center) + this.sandField.getHeight(), Align.center);
        this.addActor(this.sandField); this.addActor(this.sandLabel);

        // ...
    }

    @Override
    protected void parseInputs() {
        Input.H = this.functionField.getText();
        Input.TREES = Integer.parseInt(this.treeField.getText());
        Input.SAND = Integer.parseInt(this.sandField.getText());
    }

    @Override
    protected void keyInput(int keyCode) {
        if (keyCode == Keys.ESCAPE) {
            this.screen.setActiveScreen(InputScreen.MAIN);
        }
        if (keyCode == Keys.ENTER) {
            this.screen.setActiveScreen(InputScreen.MAIN);
        }
    }
}
