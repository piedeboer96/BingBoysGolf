package com.project_1_2.group_16.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;

public class TerrainStage extends Stage {
    
    private TitleScreen screen;

    private TextButton back;

    private Label functionLabel;
    private TextField functionField;

    private Label treeLabel;
    private TextField treeField;
    
    private Label sandLabel;
    private TextField sandField;
    
    public TerrainStage(TitleScreen screen) {
        this.screen = screen;
        this.init();
    }

    private void init() {
        // back button
        this.back = new TextButton("Back", this.screen.app.skin);
        this.back.setHeight(50);
        this.back.setWidth(500);
        this.back.moveBy(App.SCREEN_WIDTH / 2 - this.back.getWidth() / 2, 50);
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.activeScreen = screen.main.getClass();
            }
        });
        this.addActor(this.back);

        // input for the height function
        this.functionField = new TextField(Input.H, this.screen.app.skin);
        this.functionField.setHeight(50);
        this.functionField.setWidth(500);
        this.functionField.moveBy(App.SCREEN_WIDTH / 2 - this.functionField.getWidth() / 2, 600);
        this.functionLabel = new Label("Height function", this.screen.app.skin);
        this.functionLabel.setColor(Color.BLACK);
        this.functionLabel.moveBy(this.functionField.getX() + this.functionField.getWidth()/2 - this.functionLabel.getWidth()/2, this.functionField.getY() + this.functionField.getHeight());
        this.addActor(this.functionField); this.addActor(this.functionLabel);

        this.treeField = new TextField(Integer.toString(Input.TREES), this.screen.app.skin);
        this.treeField.moveBy(0.4f*App.SCREEN_WIDTH - this.treeField.getWidth()/2, 400);
        this.treeLabel = new Label("Trees", this.screen.app.skin);
        this.treeLabel.setColor(Color.BLACK);
        this.treeLabel.moveBy(this.treeField.getX() + this.treeField.getWidth()/2 - this.treeLabel.getWidth()/2, this.treeField.getY() + this.treeField.getHeight());
        this.addActor(this.treeField); this.addActor(this.treeLabel);


        this.sandField = new TextField(Integer.toString(Input.SAND), this.screen.app.skin);
        this.sandField.moveBy(0.6f*App.SCREEN_WIDTH - this.treeField.getWidth()/2, 400);
        this.sandLabel = new Label("Sand", this.screen.app.skin);
        this.sandLabel.setColor(Color.BLACK);
        this.sandLabel.moveBy(this.sandField.getX() + this.sandField.getWidth()/2 - this.sandLabel.getWidth()/2, this.sandField.getY() + this.sandField.getHeight());
        this.addActor(this.sandField); this.addActor(this.sandLabel);

        // ...
    }

    public void parseInputs() {
        Input.H = this.functionField.getText();
        Input.TREES = Integer.parseInt(this.treeField.getText());
        Input.SAND = Integer.parseInt(this.sandField.getText());
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Keys.ESCAPE) {
            this.screen.activeScreen = this.screen.main.getClass();
        }
        return true;
    }
}
