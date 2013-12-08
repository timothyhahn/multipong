package net.timothyhahn.multipong.screens;

import net.timothyhahn.multipong.MultiPongGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen extends Screen {
	private Stage stage;

	public MainMenuScreen(final MultiPongGame game) {
		super(game);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Skin uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		TextButton startGameButton = new TextButton( "Start game", uiSkin );
		startGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SinglePlayerScreen(game));
			}
		});
        stage.addActor( startGameButton );
        
		
	}

	@Override
	public void update() {

	}

	@Override
	public void present() {
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

	}

}
