package net.timothyhahn.multipong.systems;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;
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
 * MovementSystem is an EntitySystem that calculates where an entity should be based position and velocity
 * MovementSystem requires a Position and a Velocity
 */
public class MovementSystem extends EntitySystem {
	@Mapper 
	private ComponentMapper<Position> pm;
	@Mapper 
	private ComponentMapper<Velocity> vm;

	
    /** Constructors **/

    /** 
     * Creates a MovementSystem
     */
	public MovementSystem(){
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
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
     * Calculates the movements of all paddles and balls
     * @param   entities    not used
     */
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		ImmutableBag<Entity> paddles = world.getManager(GroupManager.class).getEntities("PADDLES");
		ImmutableBag<Entity> balls = world.getManager(GroupManager.class).getEntities("BALLS");
		
		for(int i = 0; i < paddles.size(); i++){
			Position position = pm.get(paddles.get(i));
			Velocity velocity = vm.get(paddles.get(i));
			
			if(!(position.getY() > 0) && velocity.getY() < 0) {
				velocity.setY(0);
			}
			if(!(position.getY() < (320 - MultiPongGame.PADDLE_HEIGHT)) && velocity.getY() > 0) {
				velocity.setY(0);
			}

            // Update the position.
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
