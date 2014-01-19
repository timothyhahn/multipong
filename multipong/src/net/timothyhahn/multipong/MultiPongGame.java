package net.timothyhahn.multipong;

/** MultiPong Imports **/
import net.timothyhahn.multipong.screens.MainMenuScreen;
import net.timothyhahn.multipong.screens.Screen;

/** LibGDX Imports **/
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;

/**
 * MultiPongGame is the main LibGDX Game that is initially launched
 */
public class MultiPongGame extends Game {
    
    /** Global vars **/
    public int screenWidth = 480;
    public int screenHeight = 320;
    private Screen screen;
    
    /**
     * Changes the screen
     * @param   screen  The screen to change to
     */
    public void setScreen(Screen screen){
    	this.screen.dispose();
        this.screen = screen;
    }

    /** Overridden methods **/

    /**
     * Loads the assets and starts us at the Main Menu
     */
    @Override
    public void create() {      
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        Assets.load();
        screen = new MainMenuScreen(this);
    }

    /**
     * Disposes of the current screen
     */
    @Override
    public void dispose() {
        screen.dispose();
    }

    /**
     * Calls the current screen's update and present
     */
    @Override
    public void render() {
        screen.update();
        screen.present();
        FPSLogger fps = new FPSLogger();
        fps.log();
    }

    /**
     * Handles resizing
     */
    @Override
    public void resize(int width, int height) {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
    }

    @Override
    public void pause() {
        screen.pause();
    }

    @Override
    public void resume() {
        screen.resume();
    }
}
