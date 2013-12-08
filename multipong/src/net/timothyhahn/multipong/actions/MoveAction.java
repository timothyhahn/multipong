package net.timothyhahn.multipong.actions;

import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;
import net.timothyhahn.multipong.MultiPongGame;

import com.artemis.Entity;

public class MoveAction {
	private int moveTo;
	private Entity entity;
	private int gameHeight;
	
	public MoveAction(int moveTo, Entity entity, int gameHeight){
		this.moveTo = moveTo;
		this.entity = entity;
		this.gameHeight = gameHeight;
	}
	public void process() {
		int height = MultiPongGame.PADDLE_HEIGHT;
		Position position = entity.getComponent(Position.class);
		Velocity velocity = entity.getComponent(Velocity.class);
		float scale = gameHeight / MultiPongGame.WORLD_HEIGHT;
		moveTo = gameHeight - moveTo;
		System.out.println(moveTo);
		System.out.println(position.getY());
		System.out.println(position.getY() + (height / 2));
		if((position.getY() * scale + scale * (height / 2)) < moveTo){
			System.out.println("GOUP");
			velocity.setY(3);
		} else {
			System.out.println("GODN");
			velocity.setY(-3);
		}
	}
}
