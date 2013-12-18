package net.timothyhahn.multipong.components;

/** Artemis Imports **/
import com.artemis.Component;

/**
 * A Points is a Component used to hold the amount of points an entity has scored
 */
public class Points extends Component {

    /** Private Variables **/
    private int points;

    
    /** Constructors **/

    /**
     * Creates a points with a default point count
     */
    public Points() {
        this.points = 0;
    }
    
    /**
     * Accessors and Mutators
     */

    /**
     * Returns points earned as an int.
     */
    public int getPoints() {
        return this.points;
    }
    
    /**
     * Increments the score
     */
    public void score() {
        this.points++;
    }
}
