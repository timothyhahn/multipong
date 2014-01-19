package net.timothyhahn.multipong.systems;

/** MultiPong Imports **/
import net.timothyhahn.multipong.Constants;
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

/** 
 * A PointsSystem calculates when a player gets a point
 * A PointsSystem requires a Position, Velocity, and Points
 */
public class PointsSystem extends EntitySystem {

    /** Private Variables **/
    @Mapper 
    private ComponentMapper<Position> posM;
    @Mapper 
    private ComponentMapper<Points> pointsM;
    @Mapper 
    private ComponentMapper<Velocity> vm;
    
    private boolean gameOver = false;
    
    /** Constructors **/
    
    /**
     * Creates a PointsSystem
     */
    public PointsSystem(){
        super(Aspect.getAspectForAll(Position.class, Points.class, Velocity.class));
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
     * Calculates if left side or right side should have an extra point
     * @param   entities    not used
     */
    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        Entity lPaddle = world.getManager(TagManager.class).getEntity("LEFT");
        Entity rPaddle = world.getManager(TagManager.class).getEntity("RIGHT");
        
        ImmutableBag<Entity> paddles = world.getManager(GroupManager.class).getEntities("PADDLES");
        ImmutableBag<Entity> balls = world.getManager(GroupManager.class).getEntities("BALLS");

        Points lPoints = pointsM.get(lPaddle);
        Points rPoints = pointsM.get(rPaddle);
        
        for(int i = 0; i < balls.size(); i++){
            Position position = posM.get(balls.get(i));
            Velocity velocity = vm.get(balls.get(i));
            
            // If ball goes less than the left side
            if(!(position.getX() > (0 - Constants.BALL_SIZE - 5)) ) {
                rPoints.score();
                // Reset the positions for all 
                position.reset();
                for(int j = 0; j < paddles.size(); j++){
                    Position pPosition = posM.get(paddles.get(j));
                    pPosition.reset();
                }
                
                // Send the ball to the left
                velocity.setX(-2);
                velocity.setY(0);
            } else if(!(position.getX() < (Constants.WORLD_WIDTH + Constants.BALL_SIZE + 5))) {

                //Mirrors the above function, but for the right side
                lPoints.score();
               
                position.reset();
                for(int j = 0; j < paddles.size(); j++){
                    Position pPosition = posM.get(paddles.get(j));
                    pPosition.reset();
                }
                velocity.setX(2);
                velocity.setY(0);
            }
            
        }
        
        // Game ends after 9 points
        if(rPoints.getPoints() > 9){
            gameOver = true;
        }
        if(lPoints.getPoints() > 9){
            gameOver = true;
        }

    }


    /** Accessor and Mutator Methods **/

    /**
     * Returns whether or not the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }
}
