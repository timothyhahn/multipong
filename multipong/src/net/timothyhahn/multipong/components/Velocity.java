package net.timothyhahn.multipong.components;

/** Artemis Imports **/
import com.artemis.Component;

/**
 * A Velocity is a Component used to hold the x and y direction magnitudes of an entity.
 */
public class Velocity extends Component {

    /** Private Variables **/

    private int x;
    private int y;


    /** Constructors **/

    /**
     * Creates a Velocity with a specific x and y.
     * @param   x   x magnitude
     * @param   y   y magnitude
     */
    public Velocity(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /** Accessor and Mutator Methods **/

    /**
     * Returns x as int
     */
    public int getX() {
        return x;
    }

    /**
     * Sets x
     * @param   x   x velocity to set to
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns y as int
     */
    public int getY() {
        return y;
    }

    /**
     * Sets y
     * @param   y   y velocity to set to
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Go up
     */
    public void goUp() {
        this.y = -Math.abs(this.y);
    }

    /**
     * Go down
     */
    public void goDown() {
        this.y = Math.abs(this.y);
    }


    /**
     * Go left
     */
    public void goLeft() {
        this.x = -Math.abs(this.x);
    }

    /**
     * Go right
     */
    public void goRight() {
        this.x = Math.abs(this.x);
    }

    /**
     * Returns the reflection of the current y velocity as an int
     */
    public int getReflection() {
        if(this.y > 0)
            return this.y + 1;
        else 
            return this.y - 1;
    }

    /** 
     * Speeds up the velocity up to 8 times
     */
    public void speedUp() {
        if(Math.abs(this.x) < 8) {
            if(this.x > 0)
                this.x++;
            else
                this.x--;
        }
    }
}
