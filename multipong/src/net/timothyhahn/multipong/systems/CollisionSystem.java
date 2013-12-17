package net.timothyhahn.multipong.systems;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.components.Bounds;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

/** Artemis Imports **/
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;

/**
 * CollisionSystem is an EntitySystem that handles collisions between paddles and the ball
 * CollisionSystem requires a Position, a Velocity, and a Bounds
 */
public class CollisionSystem extends EntitySystem {

    /** Private Variables **/
	@Mapper 
	private ComponentMapper<Position> pm;
	@Mapper 
	private ComponentMapper<Velocity> vm;
	@Mapper 
	private ComponentMapper<Bounds> bm;


    /** Constructors **/

    /**
     * Creates a CollisionSystem
     */
	public CollisionSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class, Bounds.class));
	}

	
    /** Private Methods **/

    /** 
     * Returns whether a value is within a range
     * @param   value   the value being examined
     * @param   min     the minimum bound
     * @param   max     the maximum bound
     */
	private boolean valueInRange(int value, int min, int max){
		return (value >= min) && (value <= max);
	}

    /**
     * Handles a specific collision between two entites
     * @param   entityA     The entity that will exert force on the other
     * @param   entityB     The entity that will move after the collision
     */
	private void handleCollision(Entity entityA, Entity entityB) {
		Position positionA = pm.get(entityA);
		Bounds boundsA = bm.get(entityA);
		Position positionB = pm.get(entityB);
		Bounds boundsB = bm.get(entityB);
		
		boolean xOverlap = valueInRange(positionA.getX(), positionB.getX(), positionB.getX() + boundsB.getWidth()) || valueInRange(positionB.getX(), positionA.getX(), positionA.getX() + boundsA.getWidth());
		boolean yOverlap = valueInRange(positionA.getY(), positionB.getY(), positionB.getY() + boundsB.getHeight()) ||
				   		   valueInRange(positionB.getY(), positionA.getY(), positionA.getY() + boundsA.getHeight());
		
        // If a collision occurs
		if(xOverlap && yOverlap){

			Velocity velocityA = vm.get(entityA);
			Velocity velocityB = vm.get(entityB);
            
            // If the absolute difference between the Y values is greater than the distance between the X values
            // 5 is a magic number and will likely be removed
			if(Math.abs(positionA.getY() - positionB.getY()) + 5 >= Math.abs(positionA.getX() - positionB.getX())) {
				if(positionA.getX() <= positionB.getX()) {
					velocityB.goRight();
				} else {
					velocityB.goLeft();
				}
			} else {
				if(positionA.getY() <= positionB.getY()) {
					velocityB.goDown();
				} else {
					velocityB.goUp();
				}
			}
			velocityB.setY(-velocityA.getReflection());
			velocityB.speedUp();
		}

	}
	

    /** Overridden Methods **/

    /** 
     * Returns true 
     */
	@Override
	protected boolean checkProcessing() {
		return true;
	}

    /**
     * Processes paddles and balls for collision
     * @param  entities    not used
     */
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
