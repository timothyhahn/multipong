package net.timothyhahn.multipong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static Texture paddle;
	public static Texture ball;
	public static void load(){
		paddle = new Texture(Gdx.files.internal("data/paddle.png"));
		ball = new Texture(Gdx.files.internal("data/ball.png"));
	}

}
