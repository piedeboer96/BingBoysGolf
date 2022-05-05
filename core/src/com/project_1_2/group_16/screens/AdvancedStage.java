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

public class AdvancedStage extends InputScreen {

    private TextButton back;

    private Label mukLabel;
    private TextField mukField;

    private Label musLabel;
    private TextField musField;

    private Label muksLabel;
    private TextField muksField;

    private Label mussLabel;
    private TextField mussField;
    
    public AdvancedStage(TitleScreen screen) {
        super(screen);
    }

    @Override
    protected void init() {
        // background
        this.addActor(App.THEME.getAdvancedBackground());

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

        // input for muk
        this.mukField = new TextField(Float.toString(Input.MUK), this.screen.skin);
        this.mukField.setPosition(0.4f*App.SCREEN_WIDTH, 600, Align.center);
        this.mukLabel = new Label("MUK", this.screen.skin);
        this.mukLabel.setColor(Color.BLACK);
        this.mukLabel.setPosition(this.mukField.getX(Align.center), this.mukField.getY(Align.center) + this.mukField.getHeight(), Align.center);
        this.addActor(this.mukField); this.addActor(this.mukLabel);

        // input for mus
        this.musField = new TextField(Float.toString(Input.MUS), this.screen.skin);
        this.musField.setPosition(0.6f*App.SCREEN_WIDTH, 600, Align.center);
        this.musLabel = new Label("MUS", this.screen.skin);
        this.musLabel.setColor(Color.BLACK);
        this.musLabel.setPosition(this.musField.getX(Align.center), this.musField.getY(Align.center) + this.musField.getHeight(), Align.center);
        this.addActor(this.musField); this.addActor(this.musLabel);

        // input for muks
        this.muksField = new TextField(Float.toString(Input.MUKS), this.screen.skin);
        this.muksField.setPosition(0.4f*App.SCREEN_WIDTH, 400, Align.center);
        this.muksLabel = new Label("MUKS", this.screen.skin);
        this.muksLabel.setColor(Color.BLACK);
        this.muksLabel.setPosition(this.muksField.getX(Align.center), this.muksField.getY(Align.center) + this.muksField.getHeight(), Align.center);
        this.addActor(this.muksField); this.addActor(this.muksLabel);

        // input for muss
        this.mussField = new TextField(Float.toString(Input.MUSS), this.screen.skin);
        this.mussField.setPosition(0.6f*App.SCREEN_WIDTH, 400, Align.center);
        this.mussLabel = new Label("MUSS", this.screen.skin);
        this.mussLabel.setColor(Color.BLACK);
        this.mussLabel.setPosition(this.mussField.getX(Align.center), this.mussField.getY(Align.center) + this.mussField.getHeight(), Align.center);
        this.addActor(this.mussField); this.addActor(this.mussLabel);
    }

    @Override
    protected void parseInputs() {
        Input.MUK = Float.parseFloat(this.mukField.getText());
        Input.MUS = Float.parseFloat(this.musField.getText());
        Input.MUKS = Float.parseFloat(this.muksField.getText());
        Input.MUSS = Float.parseFloat(this.mussField.getText());
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
