package net.timothyhahn.multipong.components;

/** Artemis Imports **/
import com.artemis.Component;

/**
 * A Bounds is a Component used to handle the width and height of an entity
 */
public class Bounds extends Component {

    /** Private Variables **/

    private int width;
    private int height;

    
    /** Constructors **/

    /**
     * Creates a Bounds with the width and height the same.
     * @param   size    size to set the width and height to
     */
    public Bounds(int size) {
        this.width = size;
        this.height = size;
    }

    /**
     * Creates a Bounds with a specific width and height.
     * @param   width   width of the bounds
     * @param   height  height of the bounds
     */
    public Bounds(int width, int height) {
        this.width =  width;
        this.height = height;
    }


    /** Accessor and Mutator Methods **/

    /** 
     * Returns width as int.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns height as int.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
