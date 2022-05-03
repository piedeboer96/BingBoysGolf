package com.project_1_2.group_16.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.project_1_2.group_16.App;

public class AdvancedStage extends Stage {
    
    private TitleScreen screen;

    private TextButton back;
    
    public AdvancedStage(TitleScreen screen) {
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






    }

    public void parseInputs() {

    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Keys.ESCAPE) {
            this.screen.activeScreen = this.screen.main.getClass();
        }
        return true;
    }
}
