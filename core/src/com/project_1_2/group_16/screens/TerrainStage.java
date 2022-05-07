package com.project_1_2.group_16.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.gamelogic.Spline;
import com.project_1_2.group_16.gamelogic.Terrain;

public class TerrainStage extends InputScreen {

    private TextButton back;

    private Label functionLabel;
    private TextField functionField;

    private Label treeLabel;
    private TextField treeField;
    
    private Label sandLabel;
    private TextField sandField;

    private ButtonGroup<CheckBox> group;
    private CheckBox useFunction;
    private CheckBox useSpline;

    private TextButton render;
    private RenderTile[][] renderGrid;
    private boolean firstTime = true;

    private float[][] input;

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
        this.functionField.setPosition(App.SCREEN_WIDTH / 2, 1000, Align.center);
        this.functionLabel = new Label("Height function", this.screen.skin);
        this.functionLabel.setColor(Color.BLACK);
        this.functionLabel.setPosition(this.functionField.getX(Align.center), this.functionField.getY(Align.center) + this.functionField.getHeight(), Align.center);
        this.addActor(this.functionField); this.addActor(this.functionLabel);

        // input for the number of trees
        this.treeField = new TextField(Integer.toString(Input.TREES), this.screen.skin);
        this.treeField.setPosition(0.2f*App.SCREEN_WIDTH, 1000, Align.center);
        this.treeLabel = new Label("No. Trees", this.screen.skin);
        this.treeLabel.setColor(Color.BLACK);
        this.treeLabel.setPosition(this.treeField.getX(Align.center), this.treeField.getY(Align.center) + this.treeField.getHeight(), Align.center);
        this.addActor(this.treeField); this.addActor(this.treeLabel);

        // input for the number of sandpits
        this.sandField = new TextField(Integer.toString(Input.SAND), this.screen.skin);
        this.sandField.setPosition(0.8f*App.SCREEN_WIDTH, 1000, Align.center);
        this.sandLabel = new Label("No. Sandpits", this.screen.skin);
        this.sandLabel.setColor(Color.BLACK);
        this.sandLabel.setPosition(this.sandField.getX(Align.center), this.sandField.getY(Align.center) + this.sandField.getHeight(), Align.center);
        this.addActor(this.sandField); this.addActor(this.sandLabel);

        // render button
        this.render = new TextButton("Render", this.screen.skin);
        this.render.setPosition(this.functionField.getX(Align.right)+10, this.functionField.getY(Align.bottom), Align.bottomLeft);
        this.render.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Terrain.setSpline(functionField.getText(), input);
                preRenderFunction(firstTime);
                if (firstTime) firstTime = false;
            }
        });
        this.addActor(this.render);

        // function button
        this.useFunction = new CheckBox("Use function", this.screen.skin);
        this.useFunction.setPosition(App.SCREEN_WIDTH / 2 - this.functionField.getWidth() / 4, 950, Align.center);
        this.addActor(this.useFunction);

        // spline button
        this.useSpline = new CheckBox("Use spline", this.screen.skin);
        this.useSpline.setPosition(App.SCREEN_WIDTH / 2 + this.functionField.getWidth() / 4, 950, Align.center);
        this.addActor(this.useSpline);

        // button group
        this.group = new ButtonGroup<CheckBox>(this.useFunction, this.useSpline);
        this.group.setMinCheckCount(1);
        this.group.setMaxCheckCount(1);
        this.group.setUncheckLast(true);
        if (Input.USE_SPLINES) {
            this.useSpline.setChecked(true);
        }
        else {
            this.useFunction.setChecked(true);
        }

        // formula pre-render
        this.renderGrid = new RenderTile[Spline.SPLINE_SIZE*4][Spline.SPLINE_SIZE*4];
        float size = -2+(0.4f*App.SCREEN_WIDTH)/this.renderGrid.length;
        for (int i = 0; i < this.renderGrid.length; i++) {
            for (int j = 0; j < this.renderGrid.length; j++) {
                this.renderGrid[i][j] = new RenderTile(i, j, size);
            }
        }

        // input spline
        this.input = new float[Spline.SPLINE_SIZE][Spline.SPLINE_SIZE];
    }

    @Override
    protected void parseInputs() {
        Input.H = this.functionField.getText();
        Input.TREES = Integer.parseInt(this.treeField.getText());
        Input.SAND = Integer.parseInt(this.sandField.getText());
        Input.USE_SPLINES = this.useSpline.isChecked();

        if (Input.USE_SPLINES) Terrain.setSpline(Input.H, this.input).createSpline();
        else Terrain.setSpline(Input.H, this.input);
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

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println(screenX + " " + screenY);
        return true;
    }

    /**
     * Render the current height function to the screen.
     * @param firstTime
     */
    private void preRenderFunction(boolean firstTime) {
        for (int i = 0; i < this.renderGrid.length; i++) {
            for (int j = 0; j < this.renderGrid.length; j++) {
                this.renderGrid[i][j].configure(this.input);
                if (firstTime) this.addActor(this.renderGrid[i][j]);
            }
        }
    }

    static class RenderTile extends Actor {

        private int i, j;
        private float px, py;
        private float x, y, z, size;
        private Color color;
        private static ShapeRenderer renderer = new ShapeRenderer();
        private static boolean projectionMatrixSet;

        public RenderTile(int i, int j, float size) {
            this.i = i;
            this.j = j;
            this.px = -App.FIELD_SIZE/2 + j*(App.FIELD_SIZE*1f/(4*Spline.SPLINE_SIZE-1));
            this.py = -App.FIELD_SIZE/2 + i*(App.FIELD_SIZE*1f/(4*Spline.SPLINE_SIZE-1));
            this.x = 0.3f*App.SCREEN_WIDTH + j*(size+2);
            this.y = 135 + i*(size+2);
            this.size = size;
        }

        /**
         * Configure the tile to the current height function.
         * @param input the input matrix
         */
        public void configure(float[][] input) {
            this.z = Terrain.spline.getHeightFunction(this.px, this.py) + input[this.i/4][this.j/4];
            if (this.z < 0) this.color = App.THEME.waterColorLight();
            else this.color = App.THEME.grassColorLight(this.z);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.end();
            if (!projectionMatrixSet) {
                renderer.setProjectionMatrix(batch.getProjectionMatrix());
            }
            renderer.begin(ShapeType.Filled);
            renderer.setColor(this.color);
            renderer.rect(this.x, this.y, this.size, this.size);
            renderer.end();
            batch.begin();
        }
    }
}

// TODO
// normalise screen_height positions to ratios
// know which square got dragged
// UI for drawing on render
