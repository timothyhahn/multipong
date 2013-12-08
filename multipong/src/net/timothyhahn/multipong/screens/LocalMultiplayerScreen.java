package net.timothyhahn.multipong.screens;

import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.actions.MoveAction;

public class LocalMultiplayerScreen extends SinglePlayerScreen {

	public LocalMultiplayerScreen(MultiPongGame game) {
		super(game);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenX < game.gameWidth / 2 - 40){
			MoveAction ma = new MoveAction(screenY, leftPaddle, game.gameHeight);
			ma.process();
		} else if(screenX > game.gameWidth / 2 + 100){
			MoveAction ma = new MoveAction(screenY, rightPaddle, game.gameHeight);
			ma.process();
		}
		return true;
	}
}
