package net.timothyhahn.multipong.actions;

/** MultiPong Imports **/
import net.timothyhahn.multipong.Constants;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

/** Artemis Imports **/
import com.artemis.Entity;

/**
 * An action that requires a place to move to and an Entity to move.
 * This means you do not need to pass a game, just the entity being processed.
 * This entity should only be a paddle.
 */
public class MoveAction {

    /** Private variables **/
    
    private int moveTo;
    private Entity entity;

    
    /** Constructors **/

    /** 
     * Creates a MoveAction
     * @param   moveTo  a height to a paddle to.
     * @param   entity  the entity to move.
     */
    public MoveAction(int moveTo, Entity entity){
        this.moveTo = moveTo;
        this.entity = entity;
    }


    /** Methods **/

    /**
     * Processes a move.
     * This calculates if you need to move upwards or downwards and
     * sets the velocity.
     */
    public void process() {
        int height = Constants.PADDLE_HEIGHT;
        Position position = entity.getComponent(Position.class);
        Velocity velocity = entity.getComponent(Velocity.class);
        if((position.getY() + (height / 2)) < moveTo){
            velocity.setY(3);
        } else {
            velocity.setY(-3);
        }
    }
}
