package net.timothyhahn.multipong;

/** LibGDX Imports **/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Assets are all the textures/sounds used in the game
 */
public class Assets {

    /** Textures **/
    public static Texture paddle;
    public static Texture ball;

    /**
     * Loads all the files
     */
    public static void load(){
        paddle = new Texture(Gdx.files.internal("data/paddle.png"));
        ball = new Texture(Gdx.files.internal("data/ball.png"));
    }

}
