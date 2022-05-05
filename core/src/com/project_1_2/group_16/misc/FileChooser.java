package com.project_1_2.group_16.misc;

import java.io.File;
import java.io.FileFilter;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class FileChooser extends Dialog {

    private Skin skin;

    private TextField output;

    private TextButton select;
    private TextButton back;
    private TextButton cancel;

    private FileHandle directory;

    private Label fileNameLabel;
    private TextField fileNameInput;

    private Label fileListLabel;
    private List<FileHandle> fileList;
    private ScrollPane list;

    public FileChooser(String title, Skin skin, TextField output) {
        super(title, skin);
        this.skin = skin;
        this.output = output;

        // create buttons
        this.select = new TextButton("select", this.skin);
        this.back = new TextButton("back", this.skin);
        this.cancel = new TextButton("cancel", this.skin);

        // add buttons to dialog
        this.button(this.select, "select");
        this.button(this.back, "back");
        this.button(this.cancel, "cancel");
        this.key(Keys.ENTER, "select");
        this.key(Keys.BACKSPACE, "back");
        this.key(Keys.ESCAPE, "cancel");

        // set default directory
        this.directory = new FileHandle(System.getProperty("user.home"));

        // file list
        this.fileListLabel = new Label("file list", this.skin);
        this.fileList = new List<FileHandle>(skin);
        this.fileList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fileList.getSelected();
                if (!fileList.getSelected().isDirectory()) {
                    fileNameInput.setText(fileList.getSelected().path());
                }
            }
        });
        
        // file input
        this.fileNameLabel = new Label("File name:", this.skin);
        this.fileNameInput = new TextField("", this.skin);

        // build the dialog
        this.changeDirectory(this.directory);
        this.buildDialog();
    }

    private void buildDialog() {
        Table content = this.getContentTable();
        content.clear();

        // file list
        content.add(this.fileListLabel).top().left().expandX().fillX().row();
        this.list = new ScrollPane(this.fileList, this.skin);
        this.list.layout();
        content.add(this.list).size(600, 25*this.fileList.getItems().size).fill().expand().row();

        // file name
        content.add(fileNameLabel).fillX().expandX().row();
        content.add(fileNameInput).fillX().expandX().row();
    }

    @Override
    protected void result(Object object) {
        if (object.equals("select")) {
            this.cancel();
            if (this.fileList.getSelected().isDirectory()) {
                this.changeDirectory(this.fileList.getSelected());
            }
            else {
                this.output.setText(this.fileNameInput.getText());
                this.remove();
            }
        }
        else if (object.equals("back")) {
            this.cancel();
            if (this.fileList.getSelected().parent() != null) {
                this.changeDirectory(this.fileList.getSelected().parent());
            }
        }
        else if (object.equals("cancel")) {
            // close dialog
            this.remove();
        }
    }

    private void changeDirectory(FileHandle directory) {
        this.directory = directory;

        FileHandle[] elements = this.directory.list(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(".txt");
            }
            
        });
        this.fileList.getItems().clear();
        this.fileList.getItems().addAll(elements);
        this.fileList.setSelected(null);
    }

    @Override
    public Dialog show(Stage stage) {
        stage.setKeyboardFocus(this.fileNameInput);
        stage.setScrollFocus(list);
    
        return super.show(stage);
    }
}
