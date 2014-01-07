package net.timothyhahn.multipong.screens;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;

/** LibGDX imports **/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * GameOverScreen is a Screen that shows a "Game Over" message
 */
public class GameOverScreen extends Screen {

    /** Private Variables **/
    private BitmapFont font;
    private Stage stage;
    private int counter = 0;


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
        Skin uiSkin = new Skin(Gdx.files.internal("data/Holo-light-hdpi.json"));
        
        // Label that says "Game Over"
        Label gameOverLabel = new Label("Game Over", uiSkin);
        gameOverLabel.setFontScale(2);

        // Create Table layout
        Table table = new Table();
        table.add(gameOverLabel);
        table.setFillParent(true);
        stage.addActor(table);
    }


    /** Overridden Methods **/
    /**
     * Updates the screen (used solely for waiting)
     */
    @Override
    public void update() {
        //  In order to not freeze the thread, this will wait 6 times
        //  and then go back to the main menu.
    	//  Should use another thread to count, but tired.
        if(counter > 6){
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
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

        // Lazy way to wait
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        counter++;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        System.gc();
    }

}
