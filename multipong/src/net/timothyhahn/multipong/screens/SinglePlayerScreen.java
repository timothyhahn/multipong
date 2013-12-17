package net.timothyhahn.multipong.screens;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.WorldRenderer;
import net.timothyhahn.multipong.actions.MoveAction;
import net.timothyhahn.multipong.components.Bounds;
import net.timothyhahn.multipong.components.Points;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;
import net.timothyhahn.multipong.systems.AISystem;
import net.timothyhahn.multipong.systems.CollisionSystem;
import net.timothyhahn.multipong.systems.MovementSystem;
import net.timothyhahn.multipong.systems.PointsSystem;

/** Artemis Imports **/
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;

/** LibGDX Imports **/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SinglePlayerScreen extends Screen implements InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	protected Entity leftPaddle;
	protected Entity rightPaddle;
	private Entity ball;
	protected World world;
	private WorldRenderer renderer;
	private final int worldWidth = MultiPongGame.WORLD_WIDTH;
	private final int worldHeight = MultiPongGame.WORLD_HEIGHT;
	private PointsSystem ps;
	protected AISystem as;
	
	public SinglePlayerScreen(MultiPongGame game){
		super(game);
		
		// LibGDX
		
		camera = new OrthographicCamera(worldWidth, worldHeight);
		camera.position.set(worldWidth / 2, worldHeight / 2, 0);
		batch = new SpriteBatch();
		ps = new PointsSystem();
		as = new AISystem();
		
		// Artemis
		world = new World();

		world.setSystem(new CollisionSystem());
		world.setSystem(new MovementSystem());
		world.setSystem(ps);
		world.setSystem(as);
		world.setManager(new GroupManager());
		world.setManager(new TagManager());
		world.initialize();
		world.setDelta(1);

		renderer = new WorldRenderer(batch, world);
		renderer.setScale(MultiPongGame.WORLD_HEIGHT / game.screenHeight);
		
		leftPaddle = world.createEntity();
		leftPaddle.addComponent(new Position(0, 100));
		leftPaddle.addComponent(new Velocity(0,0));
		leftPaddle.addComponent(new Bounds(MultiPongGame.PADDLE_WIDTH, MultiPongGame.PADDLE_HEIGHT + 4));
		leftPaddle.addComponent(new Points());
		world.getManager(GroupManager.class).add(leftPaddle, "PADDLES");
		world.getManager(TagManager.class).register("LEFT", leftPaddle);
		
		rightPaddle = world.createEntity();
		rightPaddle.addComponent(new Position(worldWidth - MultiPongGame.PADDLE_WIDTH, 100));
		rightPaddle.addComponent(new Velocity(0,0));
		rightPaddle.addComponent(new Bounds(MultiPongGame.PADDLE_WIDTH, MultiPongGame.PADDLE_HEIGHT));
		rightPaddle.addComponent(new Points());
		world.getManager(GroupManager.class).add(rightPaddle, "PADDLES");		
		world.getManager(TagManager.class).register("RIGHT", rightPaddle);
		world.getManager(TagManager.class).register("AI", rightPaddle);
		
		ball = world.createEntity();
		ball.addComponent(new Position(worldWidth / 2 - MultiPongGame.BALL_SIZE / 2, worldHeight / 2 - MultiPongGame.BALL_SIZE / 2));
		ball.addComponent(new Velocity(-2,0));
		ball.addComponent(new Bounds(MultiPongGame.BALL_SIZE));
		
		world.getManager(GroupManager.class).add(ball, "BALLS");
	}

	@Override
	public void update() {
		world.process();
		if (ps.isGameOver()){
			Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
			game.setScreen(new GameOverScreen(game));
			this.dispose();
		}
	}

	@Override
	public void present() {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        gl.glEnable(GL10.GL_TEXTURE_2D);


        Gdx.input.setInputProcessor(this);
		renderer.render();

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		System.gc();
	}

	@Override
	public boolean keyDown(int keycode) {
		MoveAction ma = null;

		if(keycode == Keys.W || keycode == Keys.UP)
			ma = new MoveAction(MultiPongGame.WORLD_HEIGHT, leftPaddle);
		else if(keycode == Keys.S || keycode == Keys.DOWN)
			ma = new MoveAction(0, leftPaddle);

		if(ma != null)
			ma.process();

	    return true;
	}


	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		MoveAction ma = null;

		if(screenY > (game.screenHeight / 2))
			ma = new MoveAction(0, leftPaddle);
		else
			ma = new MoveAction(MultiPongGame.WORLD_HEIGHT, leftPaddle);

		ma.process();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
