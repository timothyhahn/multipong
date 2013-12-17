package net.timothyhahn.multipong.systems;

import java.util.Random;

import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.actions.MoveAction;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

public class AISystem extends EntitySystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;
	private int counter = 50;
	private Random generator;
	public AISystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
		generator = new Random(); 
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> arg0) {
		Entity ai = world.getManager(TagManager.class).getEntity("AI");
		Entity ball = world.getManager(GroupManager.class).getEntities("BALLS").get(0);
		Position ballPos = pm.get(ball);
		Velocity ballVel = vm.get(ball);
		if(counter > 10){
			if(ballVel.getX() >= 0 || generator.nextInt(100) > 95){
				MoveAction ma = new MoveAction(ballPos.getY(), ai);
				ma.process();
				counter = 0;
			}
		} else {
			counter++;
		}
	}

}
