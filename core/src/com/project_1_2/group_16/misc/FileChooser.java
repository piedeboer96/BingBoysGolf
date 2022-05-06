package com.project_1_2.group_16.misc;

import java.io.File;
import java.io.FileFilter;
import java.util.Stack;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Allows the user to pick a file from their directories.
 * Adapted from: https://jvm-gaming.org/t/libgdx-scene2d-ui-filechooser-dialog/53228
 */
public class FileChooser extends Dialog {

    /**
     * The file type the file chooser should be looking for.
     * Only files of this type and directories will show up.
     */
    public static final String FILE_TYPE = ".json";

    /**
     * The starting directory of the file chooser.
     */
    public static final String START_DIR = System.getProperty("user.home");

    private Skin skin;

    private TextField output;

    private TextButton select;
    private TextButton back;
    private TextButton cancel;

    private FileHandle directory;
    private Stack<FileHandle> directoryStack;

    private Label fileNameLabel;
    private TextField fileNameInput;

    private Label fileListLabel;
    private List<FileHandle> fileList;
    private ScrollPane scrollPane;

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
        this.directory = new FileHandle(START_DIR);
        this.directoryStack = new Stack<FileHandle>();
        this.directoryStack.push(new FileHandle(this.directory.path()));

        // file list
        this.fileListLabel = new Label("", this.skin);
        this.fileList = new List<FileHandle>(skin);
        this.fileList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileHandle selected = fileList.getSelected();
                if (selected != null && !selected.isDirectory()) {
                    fileNameInput.setText(selected.path());
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

    /**
     * Build the dialog.
     */
    private void buildDialog() {
        Table content = this.getContentTable();
        content.clear();

        // file list
        content.add(this.fileListLabel).top().left().expandX().fillX().row();
        this.scrollPane = new ScrollPane(this.fileList, this.skin);
        this.scrollPane.layout();
        content.add(this.scrollPane).size(600, 25*this.fileList.getItems().size).fill().expand().row();

        // file name
        content.add(fileNameLabel).fillX().expandX().row();
        content.add(fileNameInput).fillX().expandX().row();
    }

    /**
     * Change the file chooser to a different directory.
     * @param directory the new directory
     */
    private void changeDirectory(FileHandle directory) {
        this.directory = directory;

        // only accept relevant files and directories
        FileHandle[] elements = this.directory.list(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(FILE_TYPE);
            }
            
        });

        // update file list
        this.fileListLabel.setText(this.directory.path());
        this.fileList.setItems(elements);

        // update selected file if necessary
        FileHandle selected = fileList.getSelected();
        if (selected != null && !selected.isDirectory()) {
            fileNameInput.setText(selected.path());
        }
    }

    @Override
    protected void result(Object object) {
        if (object.equals("select")) {
            this.cancel();
            FileHandle selected = this.fileList.getSelected();
            if (selected != null) {
                if (selected.isDirectory()) { // switch directory
                    this.changeDirectory(selected);
                    this.directoryStack.push(new FileHandle(selected.path()));
                }
                else { // take selected file
                    this.output.setText(this.fileNameInput.getText());
                    this.remove();
                }
            }
        }
        else if (object.equals("back")) {
            this.cancel();
            if (this.directoryStack.size() != 1) {
                this.directoryStack.pop(); // go back up the directory stack
                this.changeDirectory(this.directoryStack.peek());
            }
        }
        else if (object.equals("cancel")) {
            this.remove(); // close the dialog
        }
    }

    @Override
    public Dialog show(Stage stage) {
        stage.setKeyboardFocus(this.fileNameInput);
        stage.setScrollFocus(this.scrollPane);
        return super.show(stage);
    }
}
