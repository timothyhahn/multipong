package net.timothyhahn.multipong;

/** MultiPong Imports **/
import net.timothyhahn.multipong.screens.MainMenuScreen;
import net.timothyhahn.multipong.screens.Screen;

/** LibGDX Imports **/
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;

public class MultiPongGame extends Game {
	
	/** Static global vars **/
	public static final int PADDLE_HEIGHT = 128;
	public static final int PADDLE_WIDTH = 16;
	public static final int BALL_SIZE = 8;
	public static final int WORLD_WIDTH = 480;
	public static final int WORLD_HEIGHT = 320;
	
	/** Global vars **/
	public int screenWidth = 480;
	public int screenHeight = 320;

	private Screen screen;
	
	public void setScreen(Screen screen){
		this.screen = screen;
	}

	@Override
	public void create() {		
		Assets.load();
		screen = new MainMenuScreen(this);
	}

	@Override
	public void dispose() {
		screen.dispose();
	}

	@Override
	public void render() {
		screen.update();
		screen.present();
		FPSLogger fps = new FPSLogger();
		fps.log();
	}

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
