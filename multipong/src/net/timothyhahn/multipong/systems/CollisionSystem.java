package net.timothyhahn.multipong.systems;

import net.timothyhahn.multipong.components.Bounds;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;

public class CollisionSystem extends EntitySystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Bounds> bm;

	public CollisionSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class, Bounds.class));
	}
	
	private boolean valueInRange(int value, int min, int max){
		return (value >= min) && (value <= max);
	}
	private void handleCollision(Entity entityA, Entity entityB) {
		Position positionA = pm.get(entityA);
		Bounds boundsA = bm.get(entityA);
		Position positionB = pm.get(entityB);
		Bounds boundsB = bm.get(entityB);
		
		boolean xOverlap = valueInRange(positionA.getX(), positionB.getX(), positionB.getX() + boundsB.getWidth()) ||
						   valueInRange(positionB.getX(), positionA.getX(), positionA.getX() + boundsA.getWidth());
		boolean yOverlap = valueInRange(positionA.getY(), positionB.getY(), positionB.getY() + boundsB.getHeight()) ||
				   		   valueInRange(positionB.getY(), positionA.getY(), positionA.getY() + boundsA.getHeight());
		
		if(xOverlap && yOverlap){
			Velocity velocityA = vm.get(entityA);
			Velocity velocityB = vm.get(entityB);
			if(Math.abs(positionA.getY() - positionB.getY()) > Math.abs(positionA.getX() - positionB.getX())) {
				if(positionA.getX() < positionB.getX()) {
					velocityB.goRight();
				} else {
					velocityB.goLeft();
				}
			} else {
				if(positionA.getY() < positionB.getY()) {
					velocityB.goDown();
				} else {
					velocityB.goUp();
				}
			}
			velocityB.setY(-velocityA.getReflection());
			velocityB.speedUp();
		}

	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		ImmutableBag<Entity> paddles = world.getManager(GroupManager.class).getEntities("PADDLES");
		ImmutableBag<Entity> balls = world.getManager(GroupManager.class).getEntities("BALLS");
		for(int i = 0; i < paddles.size(); i++) {
			for(int j = 0; j < balls.size(); j++) {
				handleCollision(paddles.get(i), balls.get(j));
			}
		}
	}
	
}
