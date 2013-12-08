package net.timothyhahn.multipong.screens;

import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.WorldRenderer;
import net.timothyhahn.multipong.actions.MoveAction;
import net.timothyhahn.multipong.components.Bounds;
import net.timothyhahn.multipong.components.Points;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;
import net.timothyhahn.multipong.systems.CollisionSystem;
import net.timothyhahn.multipong.systems.MovementSystem;
import net.timothyhahn.multipong.systems.PointsSystem;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class SinglePlayerScreen extends Screen implements InputProcessor {
	OrthographicCamera camera;
	Vector3 touchPoint;
	SpriteBatch batch;
	Entity leftPaddle;
	Entity rightPaddle;
	Entity ball;
	World world;
	WorldRenderer renderer;
	final int worldWidth = MultiPongGame.WORLD_WIDTH;
	final int worldHeight = MultiPongGame.WORLD_HEIGHT;
	
	public SinglePlayerScreen(MultiPongGame game){
		super(game);
		
		// LibGDX
		
		camera = new OrthographicCamera(worldWidth, worldHeight);
		camera.position.set(worldWidth / 2, worldHeight / 2, 0);
		touchPoint = new Vector3();
		batch = new SpriteBatch();
		
		// Artemis
		world = new World();
		world.setSystem(new MovementSystem());
		world.setSystem(new CollisionSystem());
		world.setSystem(new PointsSystem());
		world.setManager(new GroupManager());
		world.setManager(new TagManager());
		world.initialize();
		world.setDelta(1);

		renderer = new WorldRenderer(batch, world);
		
		leftPaddle = world.createEntity();
		leftPaddle.addComponent(new Position(0, 100));
		leftPaddle.addComponent(new Velocity(0,0));
		leftPaddle.addComponent(new Bounds(MultiPongGame.PADDLE_WIDTH, MultiPongGame.PADDLE_HEIGHT));
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
		
		ball = world.createEntity();
		ball.addComponent(new Position(worldWidth / 2 - MultiPongGame.BALL_SIZE / 2, worldHeight / 2 - MultiPongGame.BALL_SIZE / 2));
		ball.addComponent(new Velocity(-2,0));
		ball.addComponent(new Bounds(MultiPongGame.BALL_SIZE));
		
		world.getManager(GroupManager.class).add(ball, "BALLS");
	}

	@Override
	public void update() {
		world.process();
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
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
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
		if(screenX < game.gameWidth / 2 - 40){
			MoveAction ma = new MoveAction(screenY, leftPaddle, game.gameHeight);
			ma.process();
		} else if(screenX > game.gameWidth / 2 + 100){
			MoveAction ma = new MoveAction(screenY, rightPaddle, game.gameHeight);
			ma.process();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//MoveAction ma = new MoveAction(screenY, leftPaddle, game.gameHeight);
		//ma.process();
		return false;
	//	return true;
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
