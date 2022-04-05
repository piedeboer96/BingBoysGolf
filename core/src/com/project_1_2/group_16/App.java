package com.project_1_2.group_16;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.project_1_2.group_16.camera.BallCamera;
import com.project_1_2.group_16.camera.FreeCamera;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.misc.ANSI;
import com.project_1_2.group_16.models.Flagpole;
import com.project_1_2.group_16.models.Golfball;
import com.project_1_2.group_16.models.Tile;

public class App extends ApplicationAdapter {

	// cameras
	private PerspectiveCamera freeCam;
	private PerspectiveCamera ballCam;
	private FreeCamera freeMovement;
	private BallCamera ballMovement;
	private boolean useFreeCam;

	// batches
	private ModelBatch modelBatch;
	private ShapeRenderer shapeBatch;
	private SpriteBatch spriteBatch;

	// models
	private AssetManager assets;
	private ModelBuilder modelBuilder;
	private Array<ModelInstance> instances;
	private Array<ModelInstance> trees;
	private Array<Tile> tiles;

	// golfball
	private Golfball golfball;

	// flagpole
	public static Flagpole flagpole;

	// crosshair and text font
	private Vector2 ch1, ch2, ch3, ch4;
	private BitmapFont font;
	public static int hitsCounter;

	// environment (lighting)
	private Environment environment;

	// constants
	public static final Color BACKGROUND = new Color(178f / 255, 255f / 255, 255f / 255, 1f);
	public static final ColorAttribute AMBIENT_LIGHT = new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f);
	public static final DirectionalLight SUN_LIGHT = new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f);
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static final int FIELD_SIZE = 20;
	public static final float FIELD_DETAIL = FIELD_SIZE * 5f;
	public static final float RENDER_DISTANCE = FIELD_SIZE * 2f;
	public static final float TILE_SIZE = FIELD_SIZE / FIELD_DETAIL;
	public static boolean os_is_windows;

	// physics
	public static float pos_x;
	public static float pos_y;
	public static boolean ballRolls;
	public static boolean ballInWater;
	public static boolean staticStop;
	public static boolean targetHit;

	// sounds
	private Sound hitSound;
    private Sound dropSound;

	// util
	private final Vector3 v = new Vector3();
	
	@Override
	public void create() {
		// input variables
		float tX = 5f; // hole pos
		float tZ = 5f;

		float r = 0.1f; // hole radius

		// set fullscreen
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		Gdx.input.setCursorCatched(true);

		// init batches and other things
		this.modelBatch = new ModelBatch();
		this.shapeBatch = new ShapeRenderer();
		this.spriteBatch = new SpriteBatch();
		this.modelBuilder = new ModelBuilder();
		this.instances = new Array<ModelInstance>();
		this.trees = new Array<ModelInstance>();
		this.tiles = new Array<Tile>();
		this.font = new BitmapFont();
		this.assets = new AssetManager();

		// create environment
		this.environment = new Environment();
		this.environment.set(AMBIENT_LIGHT);
		this.environment.add(SUN_LIGHT);

		// terrain generation
		for(int i = 0; i < FIELD_DETAIL; i++) {
			for(int j = 0; j < FIELD_DETAIL; j++) {
				float x = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (i + 1);
				float z = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (j + 1);
				generateTerrain(x, z);
			}
		}

		// create crosshair
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		this.ch1 = new Vector2(SCREEN_WIDTH / 2 + SCREEN_WIDTH / 150, SCREEN_HEIGHT / 2);
		this.ch2 = new Vector2(SCREEN_WIDTH / 2 - SCREEN_WIDTH / 150, SCREEN_HEIGHT / 2);
		this.ch3 = new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 100);
		this.ch4 = new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 100);

		// create golf ball
		this.golfball = new Golfball(this.modelBuilder, null);
		this.golfball.setPosition(0, Terrain.getHeight(0, 0), 0);
		this.instances.add(this.golfball.instance);
		Vector3 v = this.golfball.getPosition();

		// create flag
		assets.load("flag-model.obj", Model.class);
		assets.finishLoading();
		Model flagModel = assets.get("flag-model.obj", Model.class);
		App.flagpole = new Flagpole(flagModel, new Vector3(tX, Terrain.getHeight(tX, tZ), tZ), r);
		App.flagpole.rotateTowardsGolfball(this.golfball.getPosition(), Vector3.Z);
		this.instances.add(App.flagpole.instance);

		// create hole
		Material holeMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));
		Model holeModel = this.modelBuilder.createCylinder(r, 0.1f, r, 20, holeMaterial, Usage.Position);
		ModelInstance hole = new ModelInstance(holeModel);
		hole.transform.setTranslation(tX, Terrain.getHeight(tX, tZ) - 0.04f, tZ);
		this.instances.add(hole);

		// create trees TODO
		//assets.load("tree_model.g3dj", Model.class);
		//assets.finishLoading();
		//Model tree = assets.get("tree_model.g3dj", Model.class);
		//for (int i = 1; i <= Game.terrain.treeCount; i++) {
		//	ModelInstance treeInstance = new ModelInstance(tree);
		//	float trX = Game.terrain.trees.get(i)[0];
		//	float trZ = Game.terrain.trees.get(i)[1];
		//	float trR = Game.terrain.trees.get(i)[2];
		//	treeInstance.transform.translate(trX, Game.terrain.getHeight(trX, trZ) - 0.1f, trZ);
		//	treeInstance.transform.rotate(Vector3.Y, (float)(Math.random()*360));
		//	treeInstance.transform.scale(0.5f * trR, 0.5f * trR, 0.5f * trR);
		//	this.trees.add(treeInstance);
		//}

		// create ball camera
		this.ballCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.ballCam.position.set(v.x, v.y + 0.25f, v.z - 0.5f);
		this.ballCam.near = 0.1f;
		this.ballCam.far = RENDER_DISTANCE;
		this.ballCam.lookAt(v);
		this.ballCam.update();
		this.golfball.cam = this.ballCam;
		this.ballMovement = new BallCamera(this.ballCam, this.golfball);
		Gdx.input.setInputProcessor(this.ballMovement);

		// create free camera
		this.freeCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.freeCam.position.set(0, Terrain.getHeight(0, 0) + 0.25f, -0.5f);
		this.freeCam.near = 0.1f;
		this.freeCam.far = RENDER_DISTANCE;
		this.freeCam.update();
		this.freeMovement = new FreeCamera(this.freeCam);

		// sounds
		this.hitSound = Gdx.audio.newSound(Gdx.files.internal("hit_sound.wav"));
        this.dropSound = Gdx.audio.newSound(Gdx.files.internal("water_sound.wav"));
	}

	@Override
	public void render() {
		// clear screen
		if (os_is_windows) Gdx.gl.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		ScreenUtils.clear(BACKGROUND);
		
		// update models
		this.modelBatch.begin(this.useFreeCam ? this.freeCam : this.ballCam);
		this.modelBatch.render(this.instances, this.environment);
		this.modelBatch.render(this.trees, this.environment);
		for (Tile tile : this.tiles) {
			this.v.set(tile.transform.getTranslation(this.v));
			if ((this.useFreeCam ? this.freeCam : this.ballCam).frustum.boundsInFrustum(this.v, tile.getDimensions())) {
				this.modelBatch.render(tile, this.environment);
			}
		}
		this.modelBatch.end();

		// draw labels
		this.v.set(this.golfball.getPosition());
		this.spriteBatch.begin();
		this.font.setColor(Color.PINK);
		this.font.draw(this.spriteBatch, "Current Position:", SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 5f);
		this.font.draw(this.spriteBatch, "X = "+this.v.x, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 20f);
		this.font.draw(this.spriteBatch, "Y = "+this.v.z, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 35f);
		this.font.draw(this.spriteBatch, "Z = "+this.v.y, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 50f);
		this.font.draw(this.spriteBatch, "Velocity: ", SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 75f);
		this.font.draw(this.spriteBatch, "Vx = "+Game.sv.velocity_x, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 90f);
		this.font.draw(this.spriteBatch, "Vy = "+Game.sv.velocity_y, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 105f);
		this.font.draw(this.spriteBatch, "Shots: "+hitsCounter, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 130f);
		this.font.draw(this.spriteBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 5f, SCREEN_HEIGHT - 5f);
		this.spriteBatch.end();

		// draw crosshair
		this.shapeBatch.begin(ShapeType.Line);
		this.shapeBatch.setColor(Color.LIGHT_GRAY);
		this.shapeBatch.line(this.ch1, this.ch2);
		this.shapeBatch.line(this.ch3, this.ch4);
		this.shapeBatch.end();

		// update golfball
		if (ballRolls) {
			//Game.runEuler(); 
			Game.runRK4();
		}
		if(staticStop) { // ball has come to a rest
			if(targetHit) { // ball has hit the hole
				ballRolls = false;

				pos_x = Game.sv.pos_x;
				pos_y = Game.sv.pos_y;
				Game.sv.velocity_x = 0;
				Game.sv.velocity_y = 0;
			}
			ballRolls=false;

			pos_x = Game.sv.pos_x;
			pos_y = Game.sv.pos_y;
			Game.sv.velocity_x = 0;
			Game.sv.velocity_y = 0;
		}
		this.golfball.move(pos_x - this.v.x, pos_y - this.v.z);

		// controls
		if (this.useFreeCam) this.freeMovement.move(Gdx.input, Gdx.graphics.getDeltaTime());
		this.controls(Gdx.input);
	}

	/**
	 * The controls for the app.
	 * @param input
	 */
	private void controls(Input input) {
		if (input.isKeyJustPressed(Keys.ESCAPE)) { // close game
			long init = System.nanoTime();
			Gdx.app.exit();
			System.out.println("closed app in "+ANSI.RED+(System.nanoTime() - init)+ANSI.RESET+" nanoseconds.");
			System.exit(0);
		}
		if (input.isKeyJustPressed(Keys.C)) { // switch camera
			if (this.useFreeCam) { // switch to ball cam
				Gdx.input.setInputProcessor(this.ballMovement);
				this.useFreeCam = false;
			}
			else { // switch to free cam
				Gdx.input.setInputProcessor(this.freeMovement);
				this.useFreeCam = true;
			}
		}
		if (input.isKeyJustPressed(Keys.M)) { // move the ball
			App.ballRolls = true;
			this.hitSound.play();
			if (hitsCounter == 0) {
                hitsCounter++;
            }
		}
	}

	/**
	 * Terrain generation.
	 * @param x x-coordinate of the tile
	 * @param z z-coordinate of the tile
	 */
	public void generateTerrain(float x, float z) {
		float height = Terrain.getHeight(x, z) - Golfball.SIZE;

		Material boxMaterial;
		if (height < 0 - TILE_SIZE / 2) { // water texture
			boxMaterial = new Material(ColorAttribute.createDiffuse(new Color(0.1098f, 0.6392f, 0.9254f, 1f)));
		}
		//else if (x > Game.terrain.sandPosX[0] && x < Game.terrain.sandPosX[1] &&
		//		 z > Game.terrain.sandPosY[0] && z < Game.terrain.sandPosY[1]) { // sandpit texture
		//	boxMaterial = new Material(ColorAttribute.createDiffuse(new Color(0.9411f, 0.9411f, 0.4313f, 1f)));
		//		 }
		else { // grass texture (depending on height)
			float greenValue = (100 + height * 100) / 255f; 
			boxMaterial = new Material(ColorAttribute.createDiffuse(new Color(0.1568f, greenValue, 0.1568f, 1f)));
		}

		// create model
		Model box = 
		this.modelBuilder.createBox(TILE_SIZE, TILE_SIZE, TILE_SIZE, boxMaterial, Usage.Position + Usage.Normal);
		Tile tile = new Tile(box, x, z);
		this.tiles.add(tile);
	}

	@Override
	public void dispose() { 
		this.modelBatch.dispose();
		this.shapeBatch.dispose();
		this.spriteBatch.dispose();
		this.font.dispose();
		this.assets.dispose();
		this.dropSound.dispose();
		this.hitSound.dispose();
	}
}
