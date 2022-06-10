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
import com.project_1_2.group_16.misc.InfoDialog;
import com.project_1_2.group_16.misc.MazeGenerator;
import com.project_1_2.group_16.models.Wall;

/**
 * Stage used for the terrain settings.
 */
public class TerrainStage extends InputScreen {

    private TextButton back;

    private Label functionLabel;
    private TextField functionField;

    private ButtonGroup<CheckBox> group;
    private CheckBox defaultObstacles;
    private CheckBox randomObstacles;

    private Label treeLabel;
    private TextField treeField;
    
    private Label sandLabel;
    private TextField sandField;

    private TextButton render;
    private RenderTile[][] renderGrid;
    private boolean firstTime = true;

    private float[][] input;

    private TextButton reset;
    private Label brushLabel;
    private TextField brushField;

    private TextButton randomMaze;
    private Label complexityLabel;
    private TextField complexityField;

    private TextButton info;

    public TerrainStage(TitleScreen screen) {
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

        // input for the height function
        this.functionField = new TextField("", this.screen.skin);
        this.functionField.setHeight(50);
        this.functionField.setWidth(500);
        this.functionField.setPosition(App.SCREEN_WIDTH / 2, 1000, Align.center);
        this.functionLabel = new Label("Height function", this.screen.skin);
        this.functionLabel.setColor(Color.BLACK);
        this.functionLabel.setPosition(this.functionField.getX(Align.center), this.functionField.getY(Align.center) + this.functionField.getHeight(), Align.center);
        this.addActor(this.functionField); this.addActor(this.functionLabel);

        // input for the number of trees
        this.treeField = new TextField("", this.screen.skin);
        this.treeField.setPosition(0.2f*App.SCREEN_WIDTH, 1000, Align.center);
        this.treeLabel = new Label("No. Trees", this.screen.skin);
        this.treeLabel.setColor(Color.BLACK);
        this.treeLabel.setPosition(this.treeField.getX(Align.center), this.treeField.getY(Align.center) + this.treeField.getHeight(), Align.center);
        this.addActor(this.treeField); this.addActor(this.treeLabel);

        // input for the number of sandpits
        this.sandField = new TextField("", this.screen.skin);
        this.sandField.setPosition(0.8f*App.SCREEN_WIDTH, 1000, Align.center);
        this.sandLabel = new Label("No. Sandpits", this.screen.skin);
        this.sandLabel.setColor(Color.BLACK);
        this.sandLabel.setPosition(this.sandField.getX(Align.center), this.sandField.getY(Align.center) + this.sandField.getHeight(), Align.center);
        this.addActor(this.sandField); this.addActor(this.sandLabel);

        // default obstacles checkbox
        this.defaultObstacles = new CheckBox("Default obstacles", this.screen.skin);
        this.defaultObstacles.setPosition(App.SCREEN_WIDTH / 2 - this.functionField.getWidth() / 4, 950, Align.center);
        this.addActor(this.defaultObstacles);

        // random obstacles checkbox
        this.randomObstacles = new CheckBox("Random obstacles", this.screen.skin);
        this.randomObstacles.setPosition(App.SCREEN_WIDTH / 2 + this.functionField.getWidth() / 4, 950, Align.center);
        this.addActor(this.randomObstacles);

        // button group
        this.group = new ButtonGroup<CheckBox>(this.defaultObstacles, this.randomObstacles);
        this.group.setMinCheckCount(1);
        this.group.setMaxCheckCount(1);
        this.group.setUncheckLast(true);

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

        // info button
        this.info = new TextButton("info", this.screen.skin);
        this.info.setPosition(this.render.getX(Align.bottomRight) + 5, this.render.getY(Align.bottom));
        this.info.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InfoDialog infoDialog = new InfoDialog(screen.skin);
                infoDialog.addText("When \"Default obstacles\" is selected, obstacles will be rendered as specified");
                infoDialog.addText("in the level currently loaded (if no level loaded there will be no obstacles).");
                infoDialog.addText("When \"Random obstacles\" is selected, all obstacles specified in the level currently");
                infoDialog.addText("loaded will be overwritten with randomly-generated obstacles. The quantity of these");
                infoDialog.addText("obstacles can be specified in the textboxes in the corners of the screen.");
                infoDialog.addText("");
                infoDialog.addText("Clicking the \"render\" button will render the current terrain from a top-down view.");
                infoDialog.addText("");
                infoDialog.addText("Set the added height in the brush-textfield and drag over the map to color");
                infoDialog.addText("some tiles gray, then press the \"render\" button to commit these changes.");
                infoDialog.addText("");
                infoDialog.addText("(pressing the render button will take the current value of the brush-textfield,");
                infoDialog.addText("and add it to the current gray-colored tiles. To apply multiple height changes make");
                infoDialog.addText("sure to commit each height-level seperately)");
                infoDialog.addText("");
                infoDialog.addText("To generate a maze, input the desired complexity in the \"Maze Complexity\" field,");
                infoDialog.addText("that is visible after pressing the render button at least once. This complexity");
                infoDialog.addText("refers to the number of cells that the maze is subdivided in when generating.");
                infoDialog.addText("To generate the maze simply press the \"Generate maze\" button.");
                infoDialog.show(stage);
            }
        });
        this.addActor(this.info);

        // function pre-render
        this.renderGrid = new RenderTile[Spline.SPLINE_SIZE*4][Spline.SPLINE_SIZE*4];
        float size = -2+(0.4f*App.SCREEN_WIDTH)/this.renderGrid.length;
        for (int i = 0; i < this.renderGrid.length; i++) {
            for (int j = 0; j < this.renderGrid.length; j++) {
                this.renderGrid[i][j] = new RenderTile(i, j, size);
            }
        }

        // reset button
        this.reset = new TextButton("Reset spline", this.screen.skin);
        this.reset.setPosition(0.7f*App.SCREEN_WIDTH + 10, 135, Align.bottomLeft);
        this.reset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                input = new float[Spline.SPLINE_SIZE][Spline.SPLINE_SIZE];
                preRenderFunction(false);
            }
        });
        
        // brush input
        this.brushField = new TextField("0", this.screen.skin);
        this.brushField.setWidth(this.reset.getWidth());
        this.brushField.setPosition(this.reset.getX(Align.center), this.reset.getY(Align.center) + this.reset.getHeight() + 10, Align.center);
        this.brushLabel = new Label("Brush", this.screen.skin);
        this.brushLabel.setColor(Color.BLACK);
        this.brushLabel.setPosition(this.brushField.getX(), this.brushField.getY() + this.brushField.getHeight());

        // generate maze button
        this.randomMaze = new TextButton("Generate maze", this.screen.skin);
        this.randomMaze.setPosition(0.7f*App.SCREEN_WIDTH + 10, 250, Align.bottomLeft);
        this.randomMaze.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MazeGenerator.generateMaze(Integer.parseInt(complexityField.getText()));
                for (InputScreen s : screen.getScreens()) {
                    s.setValues();
                }
                parseInputs();
                preRenderFunction(false);
            }
        });

        // complexity input
        this.complexityField = new TextField("3", this.screen.skin);
        this.complexityField.setWidth(this.randomMaze.getWidth());
        this.complexityField.setPosition(this.randomMaze.getX(Align.center), this.randomMaze.getY(Align.center) + this.randomMaze.getHeight() + 10, Align.center);
        this.complexityLabel = new Label("Maze Complexity", this.screen.skin);
        this.complexityLabel.setColor(Color.BLACK);
        this.complexityLabel.setPosition(this.complexityField.getX(), this.complexityField.getY() + this.complexityField.getHeight());
    }

    @Override
    protected void parseInputs() {
        Input.H = this.functionField.getText();
        Input.NUMBER_OF_TREES = Integer.parseInt(this.treeField.getText());
        Input.NUMBER_OF_SANDPITS = Integer.parseInt(this.sandField.getText());
        Input.RANDOM_OBSTACLES = this.randomObstacles.isChecked();

        Terrain.setSpline(Input.H, this.input).createSpline();
    }

    @Override
    protected void setValues() {
        this.functionField.setText(Input.H);
        this.treeField.setText("0");
        this.sandField.setText("0");
        this.defaultObstacles.setChecked(true);
        this.randomObstacles.setChecked(false);
        this.input = Input.BICUBIC_INPUT;
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
        if (this.firstTime) return false;
        screenY = App.SCREEN_HEIGHT - screenY;
        for (int i = 0; i < this.renderGrid.length; i++) {
            for (int j = 0; j < this.renderGrid.length; j++) {
                if (this.renderGrid[i][j].contains(screenX, screenY)) {
                    this.renderGrid[i][j].setColor(Color.GRAY);
                    if (i < this.renderGrid.length - 1) this.renderGrid[i+1][j].setColor(Color.GRAY);
                    if (i > 0) this.renderGrid[i-1][j].setColor(Color.GRAY);
                    if (j < this.renderGrid.length - 1) this.renderGrid[i][j+1].setColor(Color.GRAY);
                    if (j > 0) this.renderGrid[i][j-1].setColor(Color.GRAY);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Render the current height function to the screen.
     * @param firstTime
     */
    private void preRenderFunction(boolean firstTime) {
        // render buttons
        if (firstTime) {
            this.addActor(this.reset);
            this.addActor(this.brushField);
            this.addActor(this.brushLabel);

            this.addActor(this.randomMaze);
            this.addActor(this.complexityField);
            this.addActor(this.complexityLabel);
        }

        // update hole and ball position
        this.screen.getScreens().get(0).parseInputs();

        // render / update tiles
        float brush = Float.parseFloat(this.brushField.getText());
        for (int i = 0; i < this.renderGrid.length; i++) {
            for (int j = 0; j < this.renderGrid.length; j++) {
                if (!firstTime) this.renderGrid[i][j].updateInput(this.input, brush);
                if (this.renderGrid[i][j].containsWall()) this.renderGrid[i][j].setColor(Color.LIGHT_GRAY);
                else if (this.renderGrid[i][j].containsHole(this.renderGrid.length)) this.renderGrid[i][j].setColor(Color.RED);
                else if (this.renderGrid[i][j].containsBall(this.renderGrid.length)) this.renderGrid[i][j].setColor(Color.WHITE);
                else this.renderGrid[i][j].configure(this.input);
                if (firstTime) this.addActor(this.renderGrid[i][j]);
            }
        }
    }

    /**
     * Helper class for drawing a pre-render of the terrain to the screen.
     */
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
         * Checks if this tile contains a pair of coordinates
         * @param x x-coordinate
         * @param y y-coordinate
         * @return
         */
        public boolean contains(float x, float y) {
            if (x < this.x) return false;
            if (y < this.y) return false;
            if (x > this.x + this.size) return false;
            if (y > this.y + this.size) return false;
            return true;
        }

        /**
         * If this tile contains a wall.
         * @return
         */
        public boolean containsWall() {
            for (Wall w : Input.WALLS) {
                if (w.contains(this.px, this.py)) return true;
            }
            return false;
        }

        /**
         * If this tile contains the hole.
         * @param size the total number of tiles
         * @return
         */
        public boolean containsHole(float size) {
            return (int)(Math.abs(Input.VT.x + App.FIELD_SIZE/2)*size/App.FIELD_SIZE) == j && 
                   (int)(Math.abs(Input.VT.y + App.FIELD_SIZE/2)*size/App.FIELD_SIZE) == i;
        }

        /**
         * If this tile contains the ball.
         * @param size the total number of tiles
         * @return
         */
        public boolean containsBall(float size) {
            return (int)(Math.abs(Input.V0.x + App.FIELD_SIZE/2)*size/App.FIELD_SIZE) == j && 
                   (int)(Math.abs(Input.V0.y + App.FIELD_SIZE/2)*size/App.FIELD_SIZE) == i;
        }

        /**
         * Configure the tile to the current height function.
         * @param input the input matrix
         */
        public void configure(float[][] input) {
            this.z = Terrain.spline.getHeightFunction(this.px, this.py) + input[this.i/4][this.j/4];
            if (this.z < 0) this.color = Input.THEME.waterColorLight();
            else this.color = Input.THEME.grassColorLight(this.z);
        }

        /**
         * Update the user input with the state of this tile.
         * @param input the input matrix
         * @param brush the brush value
         */
        public void updateInput(float[][] input, float brush) {
            if (this.color.equals(Color.GRAY)) input[this.i/4][this.j/4] += brush / 5f;
        }

        /**
         * Set the color of this tile.
         * @param color
         */
        public void setColor(Color color) {
            this.color = color;
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
