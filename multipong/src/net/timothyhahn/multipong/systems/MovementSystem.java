package net.timothyhahn.multipong.systems;

import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;

public class MovementSystem extends EntitySystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;

	
	public MovementSystem(){
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
	}
	

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> dontUse) {
		ImmutableBag<Entity> paddles = world.getManager(GroupManager.class).getEntities("PADDLES");
		ImmutableBag<Entity> balls = world.getManager(GroupManager.class).getEntities("BALLS");

		
		for(int i = 0; i < paddles.size(); i++){
			Position position = pm.get(paddles.get(i));
			Velocity velocity = vm.get(paddles.get(i));
			
			// Update the position.

			if(!(position.getY() > 0) && velocity.getY() < 0) {
				velocity.setY(0);
			}
			if(!(position.getY() < (320 - MultiPongGame.PADDLE_HEIGHT)) && velocity.getY() > 0) {
				velocity.setY(0);
			}
			position.addX(velocity.getX());
			position.addY(velocity.getY());
		}
		for(int i = 0; i < balls.size(); i++){
			Position position = pm.get(balls.get(i));
			Velocity velocity = vm.get(balls.get(i));
			

			if(!(position.getY() > 0) && velocity.getY() < 0) {
				velocity.setY(-velocity.getY());
			}
			
			if(!(position.getY() < (MultiPongGame.WORLD_HEIGHT - MultiPongGame.BALL_SIZE)) && velocity.getY() > 0) {
				velocity.setY(-velocity.getY());
			}
			// Update the position.
			position.addX(velocity.getX());
			position.addY(velocity.getY());
		}
	}

}
