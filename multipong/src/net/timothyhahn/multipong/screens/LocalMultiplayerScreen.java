package net.timothyhahn.multipong.screens;

/** MultiPong Imports **/
import net.timothyhahn.multipong.Constants;
import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.actions.MoveAction;

/** LibGDX Imports **/
import com.badlogic.gdx.Input.Keys;

public class LocalMultiplayerScreen extends SinglePlayerScreen {

    public LocalMultiplayerScreen(MultiPongGame game) {
        super(game);
        world.deleteSystem(as);
    }

    /**
     * Overridden Methods 
     */

    /**
     * Local Multiplayer version of key controls
     * @param   keycode     the key being pressed
     */
    @Override
    public boolean keyDown(int keycode) {
        MoveAction ma = null;
        switch(keycode) {
            case Keys.W:
                ma = new MoveAction(Constants.WORLD_HEIGHT, leftPaddle);
                break;
            case Keys.S:
                ma = new MoveAction(0, leftPaddle);
                break;
            case Keys.UP:
                ma = new MoveAction(Constants.WORLD_HEIGHT, rightPaddle);
                break;
            case Keys.DOWN:
                ma = new MoveAction(0, rightPaddle);
                break;
        }

        if(ma != null)
            ma.process();
        return true;
    }

    /**
     * Local Multiplayer version of touch/mouse controls
     * @param   screenX     x position touched
     * @param   screenY     y position touched
     * @param   pointer     which pointer is touching
     * @param   button      which button was used to touch
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MoveAction ma = null;

        // If Left
        if(screenX < game.screenWidth / 2 - 40){
            if(screenY > (game.screenHeight / 2))
                ma = new MoveAction(0, leftPaddle);
            else
                ma = new MoveAction(Constants.WORLD_HEIGHT, leftPaddle);
            ma.process();
        } else if(screenX > game.screenWidth / 2 + 100){
            if(screenY > (game.screenHeight / 2))
                ma = new MoveAction(0, rightPaddle);
            else
                ma = new MoveAction(Constants.WORLD_HEIGHT, rightPaddle);

            ma.process();
        }
        return true;
    }
}
