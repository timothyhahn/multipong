package net.timothyhahn.multipong.screens;

import net.timothyhahn.multipong.MultiPongGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen extends Screen {

	BitmapFont font;

	SpriteBatch batch;
	Stage stage;
	
	int counter = 0;
	private OrthographicCamera camera;
    public GameOverScreen(MultiPongGame game) {
		super(game);

        FileHandle fontFile = Gdx.files.internal("data/Roboto-Regular.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        int scale = MultiPongGame.WORLD_HEIGHT / game.gameHeight;
        font = generator.generateFont(game.gameHeight / 12);
        
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		stage = new Stage();

		Skin uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		Label gameOverLabel = new Label("Game Over", uiSkin);

		gameOverLabel.setFontScale(6);
        Table table = new Table();
		table.add( gameOverLabel ).width(game.gameWidth / 2).height(game.gameHeight / 4);
        table.setFillParent(true);
        stage.addActor(table);
	}


	@Override
	public void update() {
		if(counter > 6){
			game.setScreen(new MainMenuScreen(game));
			this.dispose();
			}

	}

	@Override
	public void present() {
    	
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        counter ++;
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
