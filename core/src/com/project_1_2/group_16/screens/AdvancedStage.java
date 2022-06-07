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
import com.project_1_2.group_16.misc.InfoDialog;

/**
 * Stage used for the advanced settings.
 */
public class AdvancedStage extends InputScreen {

    private TextButton back;

    private Label mukLabel;
    private TextField mukField;
    private TextButton mukInfo;

    private Label musLabel;
    private TextField musField;
    private TextButton musInfo;

    private Label muksLabel;
    private TextField muksField;
    private TextButton muksInfo;

    private Label mussLabel;
    private TextField mussField;
    private TextButton mussInfo;

    private Label holeVelocityLabel;
    private TextField holeVelocityField;
    private TextButton holeVelocityInfo;

    private Label gravityLabel;
    private TextField gravityField;
    private TextButton gravityInfo;

    public AdvancedStage(TitleScreen screen) {
        super(screen);
        super.stage = this;
    }

    @Override
    protected void init() {
        // background
        this.addActor(this.screen.getBackground());

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
        this.mukField = new TextField("", this.screen.skin);
        this.mukField.setPosition(0.4f*App.SCREEN_WIDTH, 700, Align.center);
        this.mukLabel = new Label("MUK", this.screen.skin);
        this.mukLabel.setColor(Color.BLACK);
        this.mukLabel.setPosition(this.mukField.getX(Align.center), this.mukField.getY(Align.center) + this.mukField.getHeight(), Align.center);
        this.addActor(this.mukField); this.addActor(this.mukLabel);

        // muk info
        this.mukInfo = new TextButton("?", this.screen.skin);
        this.mukInfo.setPosition(this.mukField.getX(Align.bottomRight) + 5, this.mukField.getY(Align.bottom));
        this.mukInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("Kinetic Friction-Coefficient");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.mukInfo);

        // input for mus
        this.musField = new TextField("", this.screen.skin);
        this.musField.setPosition(0.6f*App.SCREEN_WIDTH, 700, Align.center);
        this.musLabel = new Label("MUS", this.screen.skin);
        this.musLabel.setColor(Color.BLACK);
        this.musLabel.setPosition(this.musField.getX(Align.center), this.musField.getY(Align.center) + this.musField.getHeight(), Align.center);
        this.addActor(this.musField); this.addActor(this.musLabel);

        // mus info
        this.musInfo = new TextButton("?", this.screen.skin);
        this.musInfo.setPosition(this.musField.getX(Align.bottomRight) + 5, this.musField.getY(Align.bottom));
        this.musInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("Static Friction-Coefficient");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.musInfo);

        // input for muks
        this.muksField = new TextField("", this.screen.skin);
        this.muksField.setPosition(0.4f*App.SCREEN_WIDTH, 500, Align.center);
        this.muksLabel = new Label("MUKS", this.screen.skin);
        this.muksLabel.setColor(Color.BLACK);
        this.muksLabel.setPosition(this.muksField.getX(Align.center), this.muksField.getY(Align.center) + this.muksField.getHeight(), Align.center);
        this.addActor(this.muksField); this.addActor(this.muksLabel);

        // muks info
        this.muksInfo = new TextButton("?", this.screen.skin);
        this.muksInfo.setPosition(this.muksField.getX(Align.bottomRight) + 5, this.muksField.getY(Align.bottom));
        this.muksInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("Kinetic Friction-Coefficient (sandpits)");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.muksInfo);

        // input for muss
        this.mussField = new TextField("", this.screen.skin);
        this.mussField.setPosition(0.6f*App.SCREEN_WIDTH, 500, Align.center);
        this.mussLabel = new Label("MUSS", this.screen.skin);
        this.mussLabel.setColor(Color.BLACK);
        this.mussLabel.setPosition(this.mussField.getX(Align.center), this.mussField.getY(Align.center) + this.mussField.getHeight(), Align.center);
        this.addActor(this.mussField); this.addActor(this.mussLabel);

        // muss info
        this.mussInfo = new TextButton("?", this.screen.skin);
        this.mussInfo.setPosition(this.mussField.getX(Align.bottomRight) + 5, this.mussField.getY(Align.bottom));
        this.mussInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("Static Friction-Coefficient (sandpits)");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.mussInfo);

        // input for hole velocity
        this.holeVelocityField = new TextField("", this.screen.skin);
        this.holeVelocityField.setPosition(0.4f*App.SCREEN_WIDTH, 300, Align.center);
        this.holeVelocityLabel = new Label("Hole velocity", this.screen.skin);
        this.holeVelocityLabel.setColor(Color.BLACK);
        this.holeVelocityLabel.setPosition(this.holeVelocityField.getX(Align.center), this.holeVelocityField.getY(Align.center) + this.holeVelocityField.getHeight(), Align.center);
        this.addActor(this.holeVelocityField); this.addActor(this.holeVelocityLabel);

        // hole velocity info
        this.holeVelocityInfo = new TextButton("?", this.screen.skin);
        this.holeVelocityInfo.setPosition(this.holeVelocityField.getX(Align.bottomRight) + 5, this.holeVelocityField.getY(Align.bottom));
        this.holeVelocityInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("Maximum velocity allowed for a hole to count");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.holeVelocityInfo);

        // gravity input
        this.gravityField = new TextField("", this.screen.skin);
        this.gravityField.setPosition(0.6f*App.SCREEN_WIDTH, 300, Align.center);
        this.gravityLabel = new Label("Gravity", this.screen.skin);
        this.gravityLabel.setColor(Color.BLACK);
        this.gravityLabel.setPosition(this.gravityField.getX(Align.center), this.gravityField.getY(Align.center) + this.gravityField.getHeight(), Align.center);
        this.addActor(this.gravityField); this.addActor(this.gravityLabel);

        // gravity info
        this.gravityInfo = new TextButton("?", this.screen.skin);
        this.gravityInfo.setPosition(this.gravityField.getX(Align.bottomRight) + 5, this.gravityField.getY(Align.bottom));
        this.gravityInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("Gravity Constant");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.gravityInfo);
    }

    @Override
    protected void parseInputs() {
        Input.MUK = Float.parseFloat(this.mukField.getText());
        Input.MUS = Float.parseFloat(this.musField.getText());
        Input.MUKS = Float.parseFloat(this.muksField.getText());
        Input.MUSS = Float.parseFloat(this.mussField.getText());
        Input.VH = Float.parseFloat(this.holeVelocityField.getText());
        Input.GRAVITY = Float.parseFloat(this.gravityField.getText());
    }

    @Override
    protected void setValues() {
        this.mukField.setText(Float.toString(Input.MUK));
        this.musField.setText(Float.toString(Input.MUS));
        this.muksField.setText(Float.toString(Input.MUKS));
        this.mussField.setText(Float.toString(Input.MUSS));
        this.holeVelocityField.setText(Float.toString(Input.VH));
        this.gravityField.setText(Float.toString(Input.GRAVITY));
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
