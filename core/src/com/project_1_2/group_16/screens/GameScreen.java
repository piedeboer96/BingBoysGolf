package com.project_1_2.group_16.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.project_1_2.group_16.App;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.bot.ai.BRO;
import com.project_1_2.group_16.simpleBot.RuleBasedBot;
import com.project_1_2.group_16.bot.ai.PSO;
import com.project_1_2.group_16.simpleBot.RandomBot;
import com.project_1_2.group_16.bot.ai.SA;
import com.project_1_2.group_16.camera.BallCamera;
import com.project_1_2.group_16.camera.FreeCamera;
import com.project_1_2.group_16.gamelogic.Game;
import com.project_1_2.group_16.gamelogic.Sandpit;
import com.project_1_2.group_16.gamelogic.Terrain;
import com.project_1_2.group_16.math.NumericalSolver;
import com.project_1_2.group_16.misc.ANSI;
import com.project_1_2.group_16.misc.LevelEncoder;
import com.project_1_2.group_16.misc.PowerStatus;
import com.project_1_2.group_16.models.Flagpole;
import com.project_1_2.group_16.models.Golfball;
import com.project_1_2.group_16.models.TerrainBuilder;
import com.project_1_2.group_16.models.Tree;

/**
 * The screen that is used for the actual gameplay.
 */
public class GameScreen extends ScreenAdapter {
    
    // app reference
    private App app;

    // managers
    private AssetManager assets;
	private Array<ModelInstance> instances;
    private Game game;

    // models
    private Vector2 ch1, ch2, ch3, ch4;
    private Golfball golfball;
	private Flagpole flagpole;
	private ModelInstance flagInstance;
	private BitmapFont font;

    // cameras
	private PerspectiveCamera freeCam;
	private PerspectiveCamera ballCam;
	private FreeCamera freeMovement;
	private BallCamera ballMovement;
	private boolean useFreeCam;
	private float xDir, zDir;
	private float power = App.MIN_POWER;

    // logic
    private boolean allowHit;
    private int hitsCounter;
    private Vector3 v = new Vector3();
    private float colorutil;
	private float v0x, v0y;

	// bots
	private RuleBasedBot dumbBot;
	private RandomBot ruleBasedBot;
	private SA sa;
	private BRO bro;
	private PSO pso;

    public GameScreen(App app) {
        this.app = app;
    }

    public void create() {
        // init managers
        this.assets = new AssetManager();
        this.instances = new Array<ModelInstance>();
        this.game = new Game();
        game.setNumericalSolver(NumericalSolver.RK4);

        // init skins
		this.font = new BitmapFont();
		this.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // create crosshair
        this.ch1 = new Vector2(App.SCREEN_WIDTH / 2 + App.SCREEN_WIDTH / 150, App.SCREEN_HEIGHT / 2);
		this.ch2 = new Vector2(App.SCREEN_WIDTH / 2 - App.SCREEN_WIDTH / 150, App.SCREEN_HEIGHT / 2);
		this.ch3 = new Vector2(App.SCREEN_WIDTH / 2, App.SCREEN_HEIGHT / 2 + App.SCREEN_HEIGHT / 100);
		this.ch4 = new Vector2(App.SCREEN_WIDTH / 2, App.SCREEN_HEIGHT / 2 - App.SCREEN_HEIGHT / 100);

        // terrain generation
		if (Input.RANDOM_OBSTACLES) {
			Input.SAND = new ArrayList<Sandpit>();
			for (int i = 0; i < Input.NUMBER_OF_SANDPITS; i++) {
				Terrain.createRandomSandpit();
			}
		}
		this.app.terrainBuilder = new TerrainBuilder();
		this.app.terrainBuilder.begin();
		this.instances.add(new ModelInstance(this.app.terrainBuilder.end()));

        // create golf ball
		this.golfball = new Golfball();
		this.golfball.setPosition(Input.V0.x, Terrain.getHeight(Input.V0.x, Input.V0.y), Input.V0.y);
		this.instances.add(this.golfball.getInstance());
		this.golfball.STATE.x = Input.V0.x;
		this.golfball.STATE.y = Input.V0.y;

		// create flag
		this.createFlag(true);

        // create trees
		if (Input.RANDOM_OBSTACLES) {
			Input.TREES = new ArrayList<Tree>();
			for (int i = 0; i < Input.NUMBER_OF_TREES; i++) {
				Terrain.createRandomTree();
			}
		}
		for (Tree t : Input.TREES) {
			t.setModel(Input.THEME.treeModel(assets));
			t.setBumper(Input.THEME.treeBumper(t.getRadius()));
			this.instances.add(t.getInstance());
			this.instances.add(t.getBumper());
		}

        // create ball camera
		this.ballCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.ballCam.position.set(Input.V0.x, Terrain.getHeight(Input.V0.x, Input.V0.y) + 0.25f, Input.V0.y - 0.5f);
		this.ballCam.near = 0.1f;
		this.ballCam.far = App.RENDER_DISTANCE;
		this.ballCam.lookAt(Input.V0.x, Terrain.getHeight(Input.V0.x, Input.V0.y), Input.V0.y);
		this.ballCam.update();
		this.golfball.setCamera(this.ballCam);
		this.ballMovement = new BallCamera(this.ballCam, this.golfball);

		// create free camera
		this.freeCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.freeCam.position.set(Input.V0.x, Terrain.getHeight(Input.V0.x, Input.V0.y) + 0.25f, Input.V0.y - 0.5f);
		this.freeCam.near = 0.1f;
		this.freeCam.far = App.RENDER_DISTANCE;
		this.freeCam.update();
		this.freeMovement = new FreeCamera(this.freeCam);
    }

    @Override
    public void show() {
        // hide the mouse
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(this.ballMovement);
        this.allowHit = true;
    }

    @Override
    public void render(float delta) {
        // clear screen
		if (App.OS_IS_WIN) Gdx.gl.glViewport(0, 0, App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		ScreenUtils.clear(Input.THEME.skyColor());

        // update models
		this.app.modelBatch.begin(this.useFreeCam ? this.freeCam : this.ballCam);
		this.app.modelBatch.render(this.instances, this.app.environment);
		this.app.modelBatch.render(this.flagInstance, this.app.environment);
		this.app.modelBatch.end();

		// update camera directions
        this.xDir = this.ballCam.direction.x;
		this.zDir = this.ballCam.direction.z;

        // draw labels
		this.v.set(this.golfball.getPosition());
		this.app.spriteBatch.begin();
		this.font.setColor(Color.PINK);
		this.font.draw(this.app.spriteBatch, "Current Position:", App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 5f);
		this.font.draw(this.app.spriteBatch, "X = "+this.v.x, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 20f);
		this.font.draw(this.app.spriteBatch, "Y = "+this.v.z, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 35f);
		this.font.draw(this.app.spriteBatch, "Z = "+this.v.y, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 50f);
		this.font.draw(this.app.spriteBatch, "Velocity: ", App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 75f);
		this.font.draw(this.app.spriteBatch, "Vx = "+this.golfball.STATE.vx, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 90f);
		this.font.draw(this.app.spriteBatch, "Vy = "+this.golfball.STATE.vy, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 105f);
		this.font.draw(this.app.spriteBatch, "Shots: "+this.hitsCounter, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 130f);
		this.font.draw(this.app.spriteBatch, "xDir: "+this.xDir, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 155f);
		this.font.draw(this.app.spriteBatch, "yDir: "+this.zDir, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 170f);
		this.font.draw(this.app.spriteBatch, "Power: "+(this.power - 1) / 4, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 185f);
		this.font.draw(this.app.spriteBatch, "Simulatons: "+Game.simulCounter, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 210f);
		this.font.draw(this.app.spriteBatch, "Shot velocity:", App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 235f);
		this.font.draw(this.app.spriteBatch, "V0x = "+this.v0x, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 250f);
		this.font.draw(this.app.spriteBatch, "V0y = "+this.v0y, App.SCREEN_WIDTH - 115f, App.SCREEN_HEIGHT - 265f);
		this.font.draw(this.app.spriteBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 5f, App.SCREEN_HEIGHT - 5f);
		this.app.spriteBatch.end();

        // draw power gauge
		if (this.power > App.MIN_POWER && this.allowHit) {
			this.app.shapeBatch.begin(ShapeType.Filled);
			this.colorutil = 510 * (this.power - 1) / 4;
			if (this.colorutil > 255)
				this.app.shapeBatch.setColor(1f, (510f - this.colorutil) / 255f, 0f, 1f);
			else
				this.app.shapeBatch.setColor(this.colorutil / 255f, 1f, 0f, 1f);
			this.app.shapeBatch.circle(App.SCREEN_WIDTH / 2, App.SCREEN_HEIGHT / 2, this.power * 10f);
			this.app.shapeBatch.end();
		}

		// draw crosshair
		this.app.shapeBatch.begin(ShapeType.Line);
		this.app.shapeBatch.setColor(Color.LIGHT_GRAY);
		this.app.shapeBatch.line(this.ch1, this.ch2);
		this.app.shapeBatch.line(this.ch3, this.ch4);
		this.app.shapeBatch.end();

		// update golfball
		if (this.allowHit == false) { // ball is moving
			this.game.run(this.golfball.STATE, this.app);
		}
		if(this.golfball.STATE.stop) { // ball has come to a rest
			this.allowHit = true;
		}
		this.golfball.updateState();

        // controls
		if (this.useFreeCam) this.freeMovement.move(Gdx.input, Gdx.graphics.getDeltaTime());
		this.controls();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        Gdx.input.setCursorCatched(false);
        this.allowHit = false;
    }

    /**
	 * The controls for the app.
	 */
	private void controls() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) { // close game
			long init = System.nanoTime();
			Gdx.app.exit();
			System.out.println("closed app in "+ANSI.RED+(System.nanoTime() - init)+ANSI.RESET+" nanoseconds.");
			System.exit(0);
		}
		if (Gdx.input.isKeyJustPressed(Keys.C)) { // switch camera
			Gdx.input.setInputProcessor(this.useFreeCam ? this.ballMovement : this.freeMovement);
			this.useFreeCam = !this.useFreeCam;
		}
		if (Gdx.input.isKeyJustPressed(Keys.R)) { // reset ball to the start
			this.golfball.STATE.x = Input.V0.x; 
			this.golfball.STATE.y = Input.V0.y;
			this.golfball.STATE.vx = 0;
			this.golfball.STATE.vy = 0;
			this.golfball.STATE.stop = true;
			this.allowHit = true;
			this.increaseHitCounter(-this.increaseHitCounter(0));
			this.createFlag(true);
			Game.simulCounter = 0;
			this.v0x = 0; this.v0y = 0;
		}
		if (Gdx.input.isKeyJustPressed(Keys.S)) { // save level
			if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) {
				if (!LevelEncoder.isEncoded()) LevelEncoder.encode();
			}
		}

		// bots
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) { // sim. annealing
			this.sa = new SA(1000, 0.2f, this.golfball.STATE.x, this.golfball.STATE.y, this.game);
			Float[] sol = this.sa.runBot().toArray(new Float[2]);
			this.shoot(sol[0], sol[1]);
		}
		if(Gdx.input.isKeyJustPressed(Keys.NUM_2)){ // battle royale optimization
			this.bro = new BRO(20, 100, 2, this.golfball.STATE.x, this.golfball.STATE.y, this.game);
			Float[] sol = this.bro.runBot().toArray(new Float[2]);
			this.shoot(sol[0], sol[1]);
		}
		if(Gdx.input.isKeyJustPressed(Keys.NUM_3)){ // particle swarm optimization
			this.pso = new PSO(300, 20, this.golfball.STATE.x, this.golfball.STATE.y, this.game);
			Float[] sol = this.pso.runBot().toArray(new Float[2]);
			this.shoot(sol[0], sol[1]);
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_4)) { // rule based bot
			this.dumbBot = new RuleBasedBot(this.golfball.STATE);
			float[] sol = this.dumbBot.play();
			this.shoot(sol[0], sol[1]);
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_5)) { // random bot
			this.ruleBasedBot = new RandomBot(this.golfball.STATE);
			float[] sol = this.ruleBasedBot.play();
			this.shoot(sol[0], sol[1]);
		}
		
		// shooting the ball
		if (this.ballMovement.getPowerStatus() == PowerStatus.POWER_UP) {
			this.power += App.POWER_DELTA;
			if (this.power >= App.MAX_POWER) {
				this.ballMovement.setPowerStatus(PowerStatus.POWER_DOWN);
			}
		}
		else if (this.ballMovement.getPowerStatus() == PowerStatus.POWER_DOWN) {
			this.power -= App.POWER_DELTA;
			if (this.power < App.MIN_POWER) {
				this.ballMovement.setPowerStatus(PowerStatus.POWER_UP);
			}
		}
		else if (this.ballMovement.getPowerStatus() == PowerStatus.SHOOT) {
			this.shoot(this.xDir * this.power, this.zDir * this.power);
			this.power = App.MIN_POWER;
			this.ballMovement.setPowerStatus(PowerStatus.REST);
		}
	}

    /**
	 * Shoot the ball
	 * @param vX velocity in the x direction
	 * @param vY velocity in the y direction
	 * @return if the shot was successful
	 */
	public boolean shoot(float vX, float vY) {
		if (this.allowHit) {
			this.v0x = vX; this.v0y = vY;
			this.v.set(this.golfball.getPosition());
			this.golfball.STATE.vx = vX;
			this.golfball.STATE.vy = vY;

			// sound effect and shot counter
			this.app.hitSound.play();
			this.increaseHitCounter(1);

			// hit the ball
			this.golfball.STATE.prev = new Vector2(this.v.x, this.v.z);
			this.allowHit = false;
			this.golfball.STATE.stop = false;
			return true;
		}
		return false;
	}

    /**
	 * Increase the hit-counter by n.
	 * @param n increase the counter by this number
	 * @return the new value
	 */
	public int increaseHitCounter(int n) {
		this.hitsCounter += n;
		return this.hitsCounter;
	}

	/**
	 * Create the flag model.
	 * @param off true if the hole hasn't been hit
	 */
	public void createFlag(boolean off) {
		this.flagpole = new Flagpole(new Vector3(Input.VT.x, Terrain.getHeight(Input.VT.x, Input.VT.y), 
									 Input.VT.y), Input.R, off);
		this.flagpole.rotateTowardsGolfball(new Vector3(Input.V0.x, Terrain.getHeight(Input.V0.x, Input.V0.y),
											Input.V0.y), Vector3.Z);
		this.flagInstance = this.flagpole.getInstance();
	}
}
