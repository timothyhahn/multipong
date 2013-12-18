package net.timothyhahn.multipong.systems;

/** MultiPong Imports **/
import net.timothyhahn.multipong.actions.MoveAction;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

/** Java Imports **/
import java.util.Random;

/** Artemis Imports **/
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

/** 
 * AISystem is an EntitySystem that handles an entity being an AI
 * AISystem requires a Position and a Velocity
 */
public class AISystem extends EntitySystem {

    /** Private Variables **/
    @Mapper 
    private ComponentMapper<Position> pm;
    @Mapper 
    private ComponentMapper<Velocity> vm;
    private int counter = 50;
    private Random generator;


    /** Constructors **/
    
    /**
     * Creates an AISystem and an RNG
     */
    public AISystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class));
        generator = new Random(); 
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
     * Calculates the AI movements of the AI Entity
     * @param   entities   not used
     */
    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        // Assumes only one entity is an AI
        Entity ai = world.getManager(TagManager.class).getEntity("AI");
        Entity ball = world.getManager(GroupManager.class).getEntities("BALLS").get(0);
        Position ballPos = pm.get(ball);
        Velocity ballVel = vm.get(ball);

        // Artificially slow the AI down by only allowing it to calculate a move every 10 ticks
        if(counter > 10){
            // Only really move if the ball is moving towards the AI
            // or randomly with a 5% chance
            if(ballVel.getX() >= 0 || generator.nextInt(100) > 95){
                MoveAction ma = new MoveAction(ballPos.getY(), ai);
                ma.process();
                counter = 0;
            }
        } else {
            counter++;
        }
    }
}
