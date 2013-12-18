package net.timothyhahn.multipong.components;

/** Artemis Imports **/
import com.artemis.Component;

/**
 * A Position is a Component used to hold the x and y coordinate of an entity.
 */

public class Position extends Component {

    /** Private Variables **/
    private int x;
    private int y;
    
    // Used to reset an entity to its original position
    private int startX;
    private int startY;


    /** Constructors **/

    /**
     * Creates a Position with a specific x and y.
     * @param   x   x coordinate
     * @param   y   y coordinate
     */
    public Position(int x, int y){
        this.startX = x;
        this.startY = y;
        this.x = x;
        this.y = y;
    }


    /** Accessor and Mutator Methods **/

    /**
     * Returns X as int.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets X.
     * @param   x   x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns Y as int.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets Y.
     * @param   y   y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Increments X by a certain amount.
     * @param   x   amount to increment x by
     */
    public void addX(int x) {
        this.x += x;
    }

    /** 
     * Increments Y by a certain amount.
     * @param   y   amount to increment y by
     */
    public void addY(int y){
        this.y += y;
    }

    /**
     * Resets the component to its original position
     */
    public void reset(){
        this.x = startX;
        this.y = startY;
    }
}
