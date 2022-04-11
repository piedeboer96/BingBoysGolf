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
import com.project_1_2.group_16.gamelogic.Collision;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.StateVector;
import com.project_1_2.group_16.misc.ANSI;
import com.project_1_2.group_16.models.Flagpole;
import com.project_1_2.group_16.models.Golfball;
import com.project_1_2.group_16.models.Tile;
import com.project_1_2.group_16.themes.DefaultTheme;
import com.project_1_2.group_16.themes.Theme;

public class App extends ApplicationAdapter {

	// cameras
	private PerspectiveCamera freeCam;
	private PerspectiveCamera ballCam;
	private FreeCamera freeMovement;
	private BallCamera ballMovement;
	private boolean useFreeCam;
	private float xDir, zDir;
	private float power = 1;
	public static boolean goUp;

	// batches
	private ModelBatch modelBatch;
	private ShapeRenderer shapeBatch;
	private SpriteBatch spriteBatch;

	// models
	private AssetManager assets;
	private ModelBuilder modelBuilder;
	private Array<ModelInstance> instances;
	private Array<Tile> tiles;

	// golfball
	public Golfball golfball;

	// flagpole
	public static Flagpole flagpole;

	// crosshair and text font
	private Vector2 ch1, ch2, ch3, ch4;
	private BitmapFont font;

	// environment
	private Environment environment;
	private Theme theme;

	// an integer that determines if the player is the user or the bot. If 0 then the user else if 1 then the basic bot else if 2 then the advanced bot.
	public static int userOrBot = 0;
	// constants
	public static Color BACKGROUND;
	public static final ColorAttribute AMBIENT_LIGHT = new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f);
	public static final DirectionalLight SUN_LIGHT = new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f);
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static final int FIELD_SIZE = 20;
	public static final float FIELD_DETAIL = FIELD_SIZE * 5f;
	public static final float RENDER_DISTANCE = FIELD_SIZE * 2f;
	public static final float TILE_SIZE = FIELD_SIZE / FIELD_DETAIL;
	public static boolean os_is_windows;
	private Sound hitSound;
    private Sound dropSound;

	// physics
	public static float pos_x;
	public static float pos_y;
	public static boolean staticStop;
	public static boolean allowHit;
	public static int hitsCounter;
	public static Vector2 prevPos;

	// input
	public static Vector2 gV = new Vector2(-3f, 0f);
	public static Vector2 tV = new Vector2(4f, 1f);
	public static float tR = 0.1f;

	// util
	private final Vector3 v = new Vector3();
	private float colorutil;
	
	@Override
	public void create() {
		// set fullscreen
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		Gdx.input.setCursorCatched(true);

		pos_x = gV.x;
		pos_y = gV.y;

		// init batches and other things
		this.modelBatch = new ModelBatch();
		this.shapeBatch = new ShapeRenderer();
		this.spriteBatch = new SpriteBatch();
		this.modelBuilder = new ModelBuilder();
		this.instances = new Array<ModelInstance>();
		this.tiles = new Array<Tile>();
		this.font = new BitmapFont();
		this.assets = new AssetManager();

		// create environment
		this.theme = new DefaultTheme();
		this.environment = new Environment();
		this.environment.set(AMBIENT_LIGHT);
		this.environment.add(SUN_LIGHT);
		BACKGROUND = this.theme.skyColor();

		// terrain generation
		Terrain.initSandPits();
		float x, z;
		for(int i = 0; i < FIELD_DETAIL; i++) {
			for(int j = 0; j < FIELD_DETAIL; j++) {
				x = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (i + 1);
				z = -FIELD_SIZE / 2 + TILE_SIZE / 2 + TILE_SIZE * (j + 1);
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
		this.golfball = new Golfball(this.modelBuilder, null, this.theme);
		this.golfball.setPosition(gV.x, Terrain.getHeight(gV.x, gV.y), gV.y);
		this.instances.add(this.golfball.instance);

		// create flag
		App.flagpole = new Flagpole
		(this.modelBuilder, new Vector3(tV.x, Terrain.getHeight(tV.x, tV.y), tV.y), tR, this.theme);
		App.flagpole.rotateTowardsGolfball(this.golfball.getPosition(), Vector3.Z);
		this.instances.add(App.flagpole.instance);

		// create trees
		assets.load(this.theme.treeModel(), Model.class);
		assets.finishLoading();
		Model tree = assets.get(this.theme.treeModel(), Model.class);
		Terrain.initTrees(tree);
		for (int i = 0; i < Terrain.NUMBER_OF_TREES; i++) {
			this.instances.add(Terrain.trees.get(i).instance);
		}

		// create ball camera
		this.ballCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.ballCam.position.set(gV.x, Terrain.getHeight(gV.x, gV.y) + 0.25f, gV.y - 0.5f);
		this.ballCam.near = 0.1f;
		this.ballCam.far = RENDER_DISTANCE;
		this.ballCam.lookAt(gV.x, Terrain.getHeight(gV.x, gV.y), gV.y);
		this.ballCam.update();
		this.golfball.cam = this.ballCam;
		this.ballMovement = new BallCamera(this.ballCam, this.golfball);
		Gdx.input.setInputProcessor(this.ballMovement);

		// create free camera
		this.freeCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.freeCam.position.set(gV.x, Terrain.getHeight(gV.x, gV.y) + 0.25f, gV.y - 0.5f);
		this.freeCam.near = 0.1f;
		this.freeCam.far = RENDER_DISTANCE;
		this.freeCam.update();
		this.freeMovement = new FreeCamera(this.freeCam);

		// sounds
		this.hitSound = Gdx.audio.newSound(Gdx.files.internal("hit_sound.wav"));
		this.dropSound = Gdx.audio.newSound(Gdx.files.internal("water_sound.wav"));

		// allow gameplay
		Game.runRK4();
		App.allowHit = true;
	}

	@Override
	public void render() {
		// clear screen
		if (App.os_is_windows) Gdx.gl.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		ScreenUtils.clear(BACKGROUND);
		
		// update models
		this.modelBatch.begin(this.useFreeCam ? this.freeCam : this.ballCam);
		this.modelBatch.render(this.instances, this.environment);
		for (Tile tile : this.tiles) {
			this.v.set(tile.transform.getTranslation(this.v));
			if ((this.useFreeCam ? this.freeCam : this.ballCam).frustum.boundsInFrustum(this.v, tile.getDimensions())) {
				this.modelBatch.render(tile, this.environment);
			}
		}
		this.modelBatch.end();

		this.xDir = this.ballCam.direction.x;
		this.zDir = this.ballCam.direction.z;

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
		this.font.draw(this.spriteBatch, "xDir: "+this.xDir, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 155f);
		this.font.draw(this.spriteBatch, "yDir: "+this.zDir, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 170f);
		this.font.draw(this.spriteBatch, "power: "+(this.power - 1) / 4, SCREEN_WIDTH - 115f, SCREEN_HEIGHT - 185f);
		this.font.draw(this.spriteBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 5f, SCREEN_HEIGHT - 5f);
		this.spriteBatch.end();

		// draw power gauge
		if (this.power > 1 && App.allowHit) {
			this.shapeBatch.begin(ShapeType.Filled);
			this.colorutil = 510 * (this.power - 1) / 4;
			if (this.colorutil > 255) {
				this.shapeBatch.setColor(1f, (510f - this.colorutil) / 255f, 0f, 1f);
			}
			else {
				this.shapeBatch.setColor(this.colorutil / 255f, 1f, 0f, 1f);
			}
			this.shapeBatch.circle(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, this.power * 10f);
			this.shapeBatch.end();
		}

		// draw crosshair
		this.shapeBatch.begin(ShapeType.Line);
		this.shapeBatch.setColor(Color.LIGHT_GRAY);
		this.shapeBatch.line(this.ch1, this.ch2);
		this.shapeBatch.line(this.ch3, this.ch4);
		this.shapeBatch.end();

		// update golfball
		if (App.allowHit == false) {
			Game.run();
		}
		if(App.staticStop) { // ball has come to a rest
			App.allowHit = true;
		}
		this.golfball.moveTo(pos_x, pos_y);

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

		if (this.ballMovement.powerUp) { // power
			if (App.goUp) this.power += 0.05f;
			else this.power -= 0.05f;
			if (this.power >= 5) {
				App.goUp = false;
			}
			if (this.power < 1) {
				App.goUp = true;
			}
		}
		if (this.ballMovement.shoot) { // shoot the ball
			this.shoot(this.xDir * this.power, this.zDir * this.power);
			this.power = 1f;
			this.ballMovement.shoot = false;
		}
	}

	/**
	 * Shoot the ball
	 * @param vX velocity in the x direction
	 * @param vY velocity in the y direction
	 * @return if the shot was successful
	 */
	public boolean shoot(float vX, float vY) {
		if (App.allowHit) {
			this.v.set(this.golfball.getPosition());
			Game.sv = new StateVector(this.v.x, this.v.z, vX, vY);

			// sound effect and shot counter
			this.hitSound.play();
			hitsCounter++;

			// hit the ball
			App.prevPos = new Vector2(this.v.x, this.v.z);
			App.allowHit = false;
			App.staticStop = false;
			return true;
		}
		return false;
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
			boxMaterial = new Material(ColorAttribute.createDiffuse(this.theme.waterColor()));
		}
		else if (Collision.isInSandPit(x, z)) { // sandpit texture
			boxMaterial = new Material(ColorAttribute.createDiffuse(this.theme.sandPitColor()));
		}
		else { // grass texture (depending on height)
			boxMaterial = new Material(ColorAttribute.createDiffuse(this.theme.grassColor(height)));
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
