package net.timothyhahn.multipong.screens;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;

/** LibGDX Imports **/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen extends Screen {
    private Stage stage;
    private Skin uiSkin;

    public MainMenuScreen(final MultiPongGame game) {
        super(game);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        
        uiSkin = new Skin(Gdx.files.internal("data/Holo-light-hdpi.json"));
        
        // Single Player Game Button
        TextButton startSinglePlayerGameButton = new TextButton( "Single Player", uiSkin );
        startSinglePlayerGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayerScreen(game));
            }
        });
        
        // Local Multiplayer Game Button
        TextButton startLocalMultiplayerGameButton = new TextButton( "Local Multiplayer", uiSkin );
        startLocalMultiplayerGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LocalMultiplayerScreen(game));
            }
        });

        // Online Multiplayer Lobby Button
        TextButton startNetworkMultiplayerGameButton = new TextButton("Online Multiplayer", uiSkin);
        startNetworkMultiplayerGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiplayerLobbiesScreen(game));
            }
        });
        
        // Add things to table
        Table table = new Table();
        table.add(startSinglePlayerGameButton).width(game.screenWidth / 2).height(game.screenHeight / 4);
        table.row();
        table.add(startLocalMultiplayerGameButton).width(game.screenWidth / 2).height(game.screenHeight / 4);
        table.row();
        table.add(startNetworkMultiplayerGameButton).width(game.screenWidth / 2).height(game.screenHeight / 4);
        table.setFillParent(true);
        table.pack();
        stage.addActor(table);
    }

    @Override
    public void update() {

    }

    @Override
    public void present() {
    	Gdx.gl.glClearColor(1, 1, 1, 1);
    	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
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
    	stage.dispose();
    	uiSkin.dispose();
        System.gc();
    }

}
