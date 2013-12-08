package net.timothyhahn.multipong.systems;

import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.components.Points;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

public class PointsSystem extends EntitySystem {

	@Mapper ComponentMapper<Position> posM;
	@Mapper ComponentMapper<Points> pointsM;
	@Mapper ComponentMapper<Velocity> vm;
	
	
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
				System.out.println("Left has " + lPoints.getPoints() + " points and Right has " + rPoints.getPoints());
				position.reset();
				for(int j = 0; j < paddles.size(); j++){
					Position pPosition = posM.get(paddles.get(j));
					pPosition.reset();
				}
				velocity.setX(-2);
				velocity.setY(0);
			} else if(!(position.getX() < (MultiPongGame.WORLD_WIDTH + MultiPongGame.BALL_SIZE + 5))) {
				lPoints.score();
				position.reset();
				for(int j = 0; j < paddles.size(); j++){
					Position pPosition = posM.get(paddles.get(j));
					pPosition.reset();
				}
				velocity.setX(2);
				velocity.setY(0);
				System.out.println("Left has " + lPoints.getPoints() + " points and Right has " + rPoints.getPoints());
			}
			
		}
	}



}
