package com.project_1_2.group_16.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InfoDialog extends Dialog {

    private List<String> text = new ArrayList<String>();

    public InfoDialog(Skin skin) {
        super("information", skin);
    }

    public void addText(String text) {
        this.text.add(text);
    }

    @Override
    public Dialog show(Stage stage) {
        // close button
        this.button("close");

        // content
        Table content = this.getContentTable();
        for (String s : this.text) {
            content.add(new Label(s, this.getSkin())).fillX().expandX().row();
        }

        return super.show(stage);
    }

    @Override
    public void result(Object obj) {
        this.remove();
    }
}
