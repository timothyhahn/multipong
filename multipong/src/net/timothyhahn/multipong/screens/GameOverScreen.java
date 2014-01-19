package net.timothyhahn.multipong.screens;

/** MultiPong Imports **/
import java.util.Calendar;

import net.timothyhahn.multipong.MultiPongGame;


import net.timothyhahn.multipong.Timer;

/** LibGDX imports **/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * GameOverScreen is a Screen that shows a "Game Over" message
 */
public class GameOverScreen extends Screen {

    /** Private Variables **/
    private Stage stage;
    private Timer timer;
    private Skin uiSkin;

    /** Constructors **/

    /**
     * Creates a GameOverScreen
     * @param   game    the Game being played
     */
    public GameOverScreen(MultiPongGame game) {
        super(game);

        // Create new Stage to draw UI on
        stage = new Stage();

        // Load default UI Skin
        uiSkin = new Skin(Gdx.files.internal("data/Holo-light-hdpi.json"));
        
        // Label that says "Game Over"
        Label gameOverLabel = new Label("Game Over", uiSkin);
        gameOverLabel.setFontScale(2);

        // Create Table layout
        Table table = new Table();
        table.add(gameOverLabel);
        table.setFillParent(true);
        stage.addActor(table);
        Calendar fiveSecondsLater = Calendar.getInstance();
        fiveSecondsLater.add(Calendar.SECOND, 5);
        timer = new Timer(fiveSecondsLater);
        Thread timerThread = new Thread(timer);
        timerThread.start();
    }


    /** Overridden Methods **/
    /**
     * Updates the screen (used solely for waiting)
     */
    @Override
    public void update() {
        if(timer.isFinished()){
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void present() {
        GLCommon gl = Gdx.gl;
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    	uiSkin.dispose();
    	stage.dispose();
    	timer.stop();
        System.gc();
    }

}
