package net.timothyhahn.multipong.screens;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import net.timothyhahn.multipong.Constants;
import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.State;
import net.timothyhahn.multipong.actions.MoveAction;
import net.timothyhahn.multipong.components.Points;
import net.timothyhahn.multipong.components.Position;
import net.timothyhahn.multipong.components.Velocity;
import net.timothyhahn.multipong.networking.ReceiveUpdates;

import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class OnlineMultiplayerScreen extends SinglePlayerScreen {
	private boolean isLeft;

	private DataOutputStream rawOutputStream;
	private TagManager tagManager;
	private ComponentMapper<Position> pm;
	private ComponentMapper<Points> pointsM;
	private ComponentMapper<Velocity> vm;

	private Points lp;
	private Points rp;

	private Position lm;
	private Position rm;
	private Position bm;
	
	private Velocity lvm;
	private Velocity rvm;
	private Velocity bvm;
	
	private ReceiveUpdates ru;
	
	public OnlineMultiplayerScreen(MultiPongGame game, DataOutputStream rawOutputStream, BufferedReader in, Boolean isLeft) {
		super(game);
		world.deleteSystem(as);
		tagManager = world.getManager(TagManager.class);
		this.rawOutputStream = rawOutputStream;
		pm = world.getMapper(Position.class);
        pointsM = world.getMapper(Points.class);
        vm = world.getMapper(Velocity.class);

        lp = pointsM.get(leftPaddle);
        rp = pointsM.get(rightPaddle);
        lm = pm.get(leftPaddle);
        rm = pm.get(rightPaddle);
        bm = pm.get(tagManager.getEntity("BALL"));
        lvm = vm.get(leftPaddle);
        rvm = vm.get(rightPaddle);
        bvm = vm.get(tagManager.getEntity("BALL"));

		this.isLeft = isLeft;
		ru = new ReceiveUpdates(in);
		Thread receiveThread = new Thread(ru);
		receiveThread.start();
	}

    @Override
    public void update() {
	    if(ru.isUpdateAvailable()){
	    	State update = ru.getUpdatedState();
	    	switch(update.type) {
		    	case State.FULL:
		    		lp.setPoints(update.lPoints);
			    	rp.setPoints(update.rPoints);
			    	lm.setY(update.lY);
			    	rm.setY(update.rY);
			    	bm.setX(update.bX);
			    	bm.setY(update.bY);
			    	lvm.setY(update.lVY);
			    	rvm.setY(update.rVY);
			    	bvm.setX(update.bVX);
			    	bvm.setY(update.bVY);
		    		break;
		    	case State.MOVE:
		    		lm.setY(update.lY);
			    	rm.setY(update.rY);
			    	lvm.setY(update.lVY);
			    	rvm.setY(update.rVY);
		    		break;
	    	}
	    	
	    } else {
	    	world.process();
	    }
	    world.process();
        if (ps.isGameOver()){
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
            game.setScreen(new GameOverScreen(game));
            this.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Keys.W || keycode == Keys.UP) {
			move(Constants.WORLD_HEIGHT);
		} else if (keycode == Keys.S || keycode == Keys.DOWN) {
			move(0);
		}

        MoveAction ma = null;

        if(isLeft){
	        if(keycode == Keys.W || keycode == Keys.UP)
	            ma = new MoveAction(Constants.WORLD_HEIGHT, leftPaddle);
	        else if(keycode == Keys.S || keycode == Keys.DOWN)
	            ma = new MoveAction(0, leftPaddle);
        } else {
        	if(keycode == Keys.W || keycode == Keys.UP)
	            ma = new MoveAction(Constants.WORLD_HEIGHT, rightPaddle);
	        else if(keycode == Keys.S || keycode == Keys.DOWN)
	            ma = new MoveAction(0, rightPaddle);
        }

        if(ma != null)
            ma.process();

        return true;
    }

    
	@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenY > (game.screenHeight / 2)){
			move(0);
		} else {
			move(Constants.WORLD_HEIGHT);
		}

		MoveAction ma = null;

        // If Left
        if(isLeft){
            if(screenY > (game.screenHeight / 2))
                ma = new MoveAction(0, leftPaddle);
            else
                ma = new MoveAction(Constants.WORLD_HEIGHT, leftPaddle);
            ma.process();
        } else if(screenX > game.screenWidth / 2 + 100){
            if(screenY > (game.screenHeight / 2))
                ma = new MoveAction(0, rightPaddle);
            else
                ma = new MoveAction(Constants.WORLD_HEIGHT, rightPaddle);
            ma.process();
        }
        return true;
    }

	private void move(int i) {
		String command = "{\"type\": \"move\", \"pos\": " + i + "}\n";
        try {
            rawOutputStream.writeBytes(command);
            rawOutputStream.flush();
        } catch(Exception e){
        	e.printStackTrace();
        }	
	}
	 @Override
    public void dispose() {
		try {
			rawOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.gc();
    }
}
