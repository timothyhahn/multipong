package net.timothyhahn.multipong;

/** MultiPong Imports **/
import net.timothyhahn.multipong.components.Points;
import net.timothyhahn.multipong.components.Position;

/** Artemis Imports **/
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

/** LibGDX Imports **/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class WorldRenderer {
	@Mapper 
	private ComponentMapper<Position> pm;
	@Mapper 
	private ComponentMapper<Points> pointsM;
    private World world;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Entity lPaddle;
    private Entity rPaddle;
    private int scale = 1;
    
    public WorldRenderer (SpriteBatch batch, World world){
    	this.world = world;
    	this.batch = batch;
    	this.camera = new OrthographicCamera(MultiPongGame.WORLD_WIDTH, MultiPongGame.WORLD_HEIGHT);
        this.camera.position.set(MultiPongGame.WORLD_WIDTH / 2, MultiPongGame.WORLD_HEIGHT / 2, 0);
        font = new BitmapFont();
        
        FileHandle fontFile = Gdx.files.internal("data/Roboto-Regular.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont(MultiPongGame.WORLD_HEIGHT * scale/ 24);
        font.setScale((float) (1.0 / scale));

        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        lPaddle = world.getManager(TagManager.class).getEntity("LEFT");
        rPaddle = world.getManager(TagManager.class).getEntity("RIGHT");
    }

    public void setScale(int scale){
    	this.scale = scale;
    }
    
    public void render(){
    	camera.update();
    	batch.setProjectionMatrix(camera.combined);
    	
		ImmutableBag<Entity> paddles = world.getManager(GroupManager.class).getEntities("PADDLES");
		ImmutableBag<Entity> balls = world.getManager(GroupManager.class).getEntities("BALLS");
		pm = world.getMapper(Position.class);
		pointsM = world.getMapper(Points.class);
		
    	batch.enableBlending();
        batch.begin();

        for(int i = 0; i < paddles.size(); i++){
			Position paddle = pm.get(paddles.get(i));
			batch.draw(Assets.paddle, paddle.getX(), paddle.getY());
		}

        for(int i = 0; i < balls.size(); i++){
			Position ball = pm.get(balls.get(i));
			batch.draw(Assets.ball, ball.getX(), ball.getY());
		}

    	lPaddle = world.getManager(TagManager.class).getEntity("LEFT");
    	rPaddle = world.getManager(TagManager.class).getEntity("RIGHT");
        Points lPoints = pointsM.get(lPaddle);
        Points rPoints = pointsM.get(rPaddle);
        Integer lScore = lPoints.getPoints();
        Integer rScore = rPoints.getPoints();
        
		font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        font.draw(batch, lScore.toString(), MultiPongGame.WORLD_WIDTH / 2 - 30, MultiPongGame.WORLD_HEIGHT / 2 - 5);

        font.setColor(0.0f, 0.0f, 1.0f, 1.0f);
        font.draw(batch, rScore.toString(), MultiPongGame.WORLD_WIDTH / 2 + 20, MultiPongGame.WORLD_HEIGHT / 2 - 5);
    
        batch.end();
    }
}
