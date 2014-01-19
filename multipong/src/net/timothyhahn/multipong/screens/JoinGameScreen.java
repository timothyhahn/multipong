package net.timothyhahn.multipong.screens;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;
import net.timothyhahn.multipong.Timer;
import net.timothyhahn.multipong.networking.ReceiveUpdates;

/** 
 * JoinGameScreen is a Screen when a player wants to join a game
 */
public class JoinGameScreen extends Screen{

	private Stage stage;
	private DataOutputStream rawOutputStream;
	private BufferedReader in;
	private Boolean isLeft = true;
	private Timer timer;
	private ReceiveUpdates ru;
	private Boolean gotSync = false;
	private Thread receiveThread;
	private Skin uiSkin;
	
    public JoinGameScreen(MultiPongGame game, DataOutputStream rawOutputStream, BufferedReader in, String pos) {
        super(game);
        
        stage = new Stage();
        this.rawOutputStream = rawOutputStream;
        this.in = in;
        if(pos.equals("r"))
        	isLeft = false;
        
        String sPosition = "left";

        if(!isLeft)
        	sPosition = "right";
        
        // Load default UI Skin
        uiSkin = new Skin(Gdx.files.internal("data/Holo-light-hdpi.json"));
        
        Label waitingLabel = new Label("Waiting for Other Player", uiSkin);
        Label positionLabel = new Label("You will be on the " + sPosition, uiSkin);
        
        // Create Table layout
        Table table = new Table();
        table.add(waitingLabel);
        table.row();
        table.add(positionLabel);
        table.setFillParent(true);
        stage.addActor(table);
        Calendar start = Calendar.getInstance();
        timer = new Timer(start);	
        
        ru = new ReceiveUpdates(in);
        receiveThread = new Thread(ru);
        receiveThread.start();
    }

    @Override
    public void update() {
    	 GLCommon gl = Gdx.gl;
         Gdx.gl.glClearColor(1, 1, 1, 1);
         Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
         gl.glEnable(GL10.GL_TEXTURE_2D);
         
         stage.act(Gdx.graphics.getDeltaTime());
         stage.draw();
         
         if(!gotSync) {
        	if(ru.isUpdateAvailable()){
	            receiveThread.interrupt();
	        	Thread timerThread = new Thread(timer);
	     		timerThread.start();
	     		gotSync = true;
        	}
         }
         
         if(timer.isFinished()){
	         game.setScreen(new OnlineMultiplayerScreen(game, rawOutputStream, in, isLeft));
         }
    }

    @Override
    public void present() {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
    	uiSkin.dispose();
    	stage.dispose();
    	timer.stop();
        System.gc();
    }

}
