package net.timothyhahn.multipong;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "multipong";
		cfg.useGL20 = false;
		cfg.width = 720;
		cfg.height = 480;
		
		new LwjglApplication(new MultiPongGame(), cfg);
	}
}
