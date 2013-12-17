package net.timothyhahn.multipong.systems;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.components.Points;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

/** Artemis Imports **/
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

public class PointsSystem extends EntitySystem {

	@Mapper 
	private ComponentMapper<Position> posM;
	@Mapper 
	private ComponentMapper<Points> pointsM;
	@Mapper 
	private ComponentMapper<Velocity> vm;
	
	private boolean gameOver = false;
	
	public PointsSystem(){
		super(Aspect.getAspectForAll(Position.class, Points.class, Velocity.class));
	}
	

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> arg0) {
		Entity lPaddle = world.getManager(TagManager.class).getEntity("LEFT");
		Entity rPaddle = world.getManager(TagManager.class).getEntity("RIGHT");
		
		ImmutableBag<Entity> paddles = world.getManager(GroupManager.class).getEntities("PADDLES");
		ImmutableBag<Entity> balls = world.getManager(GroupManager.class).getEntities("BALLS");
		Points lPoints = pointsM.get(lPaddle);
		Points rPoints = pointsM.get(rPaddle);
		
		for(int i = 0; i < balls.size(); i++){
			Position position = posM.get(balls.get(i));
			Velocity velocity = vm.get(balls.get(i));
			

			if(!(position.getX() > (0 - MultiPongGame.BALL_SIZE - 5)) ) {
				rPoints.score();
				if(rPoints.getPoints() > 9){
					gameOver = true;
				}
				position.reset();
				for(int j = 0; j < paddles.size(); j++){
					Position pPosition = posM.get(paddles.get(j));
					pPosition.reset();
				}
				velocity.setX(-2);
				velocity.setY(0);
			} else if(!(position.getX() < (MultiPongGame.WORLD_WIDTH + MultiPongGame.BALL_SIZE + 5))) {
				lPoints.score();
				if(lPoints.getPoints() > 9){
					gameOver = true;
				}
				position.reset();
				for(int j = 0; j < paddles.size(); j++){
					Position pPosition = posM.get(paddles.get(j));
					pPosition.reset();
				}
				velocity.setX(2);
				velocity.setY(0);
			}
			
		}
	}

	public boolean isGameOver() {
		return gameOver;
	}


}
