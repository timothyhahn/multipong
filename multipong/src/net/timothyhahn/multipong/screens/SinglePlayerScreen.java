package net.timothyhahn.multipong.screens;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.Constants;
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
    /** Private Variables **/
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Entity ball;
    private WorldRenderer renderer;
    private final int worldWidth = Constants.WORLD_WIDTH;
    private final int worldHeight = Constants.WORLD_HEIGHT;
    protected PointsSystem ps;

    /** Protected Variables **/
    protected Entity leftPaddle;
    protected Entity rightPaddle;
    protected World world;
    protected AISystem as;
    
    /** 
     * Creates a SinglePlayerScreen
     * @param   game    Game being played
     */
    public SinglePlayerScreen(MultiPongGame game){
        super(game);
        
        // Camera and libGDX 
        camera = new OrthographicCamera(worldWidth, worldHeight);
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        batch = new SpriteBatch();

        // Artemis
        world = new World();

        // Create Systems
        world.setSystem(new CollisionSystem());
        world.setSystem(new MovementSystem());

        // These two systems need to be named
        ps = new PointsSystem();
        as = new AISystem();
        world.setSystem(ps);
        world.setSystem(as);

        // Create Managers
        world.setManager(new GroupManager());
        world.setManager(new TagManager());

        // Starts the world
        world.initialize();
        world.setDelta(1);

        // Creates WorldRenderer
        renderer = new WorldRenderer(batch, world);
        renderer.setScale(Constants.WORLD_HEIGHT / game.screenHeight);
        
        // Left Paddle
        leftPaddle = world.createEntity();
        leftPaddle.addComponent(new Position(0, 100));
        leftPaddle.addComponent(new Velocity(0,0));
        leftPaddle.addComponent(new Bounds(Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT + 4));
        leftPaddle.addComponent(new Points());
        world.getManager(GroupManager.class).add(leftPaddle, "PADDLES");
        world.getManager(TagManager.class).register("LEFT", leftPaddle);
        
        // Right Paddle
        rightPaddle = world.createEntity();
        rightPaddle.addComponent(new Position(worldWidth - Constants.PADDLE_WIDTH, 100));
        rightPaddle.addComponent(new Velocity(0,0));
        rightPaddle.addComponent(new Bounds(Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT));
        rightPaddle.addComponent(new Points());
        world.getManager(GroupManager.class).add(rightPaddle, "PADDLES");       
        world.getManager(TagManager.class).register("RIGHT", rightPaddle);
        world.getManager(TagManager.class).register("AI", rightPaddle);
        
        // Ball
        ball = world.createEntity();
        ball.addComponent(new Position(worldWidth / 2 - Constants.BALL_SIZE / 2, worldHeight / 2 - Constants.BALL_SIZE / 2));
        ball.addComponent(new Velocity(-2,0));
        ball.addComponent(new Bounds(Constants.BALL_SIZE));
        world.getManager(GroupManager.class).add(ball, "BALLS");
        world.getManager(TagManager.class).register("BALL", ball);
    }

    /**
     * Tells the Artemis World to take a step
     */
    @Override
    public void update() {
        world.process();
        if (ps.isGameOver()){
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
            game.setScreen(new GameOverScreen(game));
        }
    }

    /**
     * Tells the WorldRenderer to render
     */
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
    	batch.dispose();
    	renderer.dispose();
        System.gc();
    }

    @Override
    public boolean keyDown(int keycode) {
        MoveAction ma = null;

        if(keycode == Keys.W || keycode == Keys.UP)
            ma = new MoveAction(Constants.WORLD_HEIGHT, leftPaddle);
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

    /**
     * SinglePlayer version of touch/mouse controls
     * @param   screenX     x position touched
     * @param   screenY     y position touched
     * @param   pointer     which pointer is touching
     * @param   button      which button was used to touch
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MoveAction ma = null;

        if(screenY > (game.screenHeight / 2))
            ma = new MoveAction(0, leftPaddle);
        else
            ma = new MoveAction(Constants.WORLD_HEIGHT, leftPaddle);

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
