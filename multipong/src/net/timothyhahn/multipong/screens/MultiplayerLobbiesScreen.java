package net.timothyhahn.multipong.screens;

/** MultiPong Imports **/
import net.timothyhahn.multipong.MultiPongGame;



import net.timothyhahn.multipong.Timer;




/** Java Imports **/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;







/** LibGDX Imports **/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** Jackson Imports **/
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MultiplayerLobbiesScreen extends Screen {
    
    private Stage stage;
    private int targetPort = 8888;
    private String targetHost = "162.209.96.88";
    private TextButton createLobby;
    private Table table;
    private Skin uiSkin;
    private Socket socket;
    private HashMap<String, String> lobbyList;
    private DataOutputStream rawOutputStream;
    private InputStream inputStream;
    private BufferedReader in;
    private ObjectMapper mapper;
    private Timer timer;
    
    public MultiplayerLobbiesScreen(final MultiPongGame game) {
        super(game);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        
        uiSkin = new Skin(Gdx.files.internal("data/Holo-light-hdpi.json"));
        mapper = new ObjectMapper();
        table = new Table();
        table.setFillParent(true);
        Label waitLabel = new Label("Waiting for server", uiSkin);
        waitLabel.setFontScale(2);
        table.add(waitLabel);
        table.pack();
        
        try {
			socket = new Socket(targetHost, targetPort);

            socket.setSoTimeout(10000);
			rawOutputStream = new DataOutputStream(socket.getOutputStream());
            
			inputStream = socket.getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
		    
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
         

        lobbyList = getLobbyList();
        createLobby = new TextButton("Create Lobby", uiSkin);
        
        createLobby.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLobby();
                try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                lobbyList = getLobbyList();
            }
        });
        
        Calendar threeSecondTimer = Calendar.getInstance();
        threeSecondTimer.add(Calendar.SECOND, 1);
        timer = new Timer(threeSecondTimer);
        Thread timerThread = new Thread(timer);
        timerThread.start();
        
    }

    private void createLobby(){
        String command = "{\"type\": \"create\"}\n";
        try {
            rawOutputStream.writeBytes(command);
            rawOutputStream.flush();
            in.readLine();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    private HashMap<String, String> getLobbyList() {
        String command = "{\"type\": \"list\"}\n";
        String result = "[]";
        try {
            rawOutputStream.writeBytes(command);
            rawOutputStream.flush();
            result = in.readLine().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result.length() > 10) {
            HashMap<String, String> lobbiesMap = new HashMap<String, String>();
            String jsonString =  result;

            JsonNode root;
			try {
				root = mapper.readTree(jsonString);
				JsonNode lobbies = root.get("lobbies");
	            Iterator<JsonNode> it = lobbies.iterator();
	            while(it.hasNext()){
	            	JsonNode lobby = it.next();
	            	lobbiesMap.put(lobby.get("name").asText(), lobby.get("count").asText());
	            }
	            return lobbiesMap;
			} catch (IOException e) {
				e.printStackTrace();
				return new HashMap<String, String>();
			}
        } else {
            return new HashMap<String, String>();
        }
    }

    @Override
    public void update() {
        
        if(timer.isFinished()) {
            table.reset();
            table.setFillParent(true);
            table.add(createLobby).width(game.screenWidth / 2).height(game.screenHeight / 4);
            lobbyList = getLobbyList();
            Calendar threeSecondTimer = Calendar.getInstance();
            threeSecondTimer.add(Calendar.SECOND, 3);
            timer.reset(threeSecondTimer);

            for (Map.Entry<String, String> lobby : lobbyList.entrySet()){
                final TextButton lobbyButton = new TextButton(lobby.getKey() , uiSkin);
                lobbyButton.addListener(new ClickListener(){
                	@Override
                	public void clicked(InputEvent event, float x, float y) {
    	            	String command = "{\"type\": \"join\", \"name\": \"" + lobbyButton.getText().toString() + "\"}\n";
    	            	String pos = "l";
    	                try {
    	                    rawOutputStream.writeBytes(command);
    	                    rawOutputStream.flush();
    	                    try {
    							Thread.sleep(250);
    						} catch (InterruptedException e) {
    							e.printStackTrace();
    						}
    	                    String position = in.readLine().toString();
    	                    JsonNode root = mapper.readTree(position);
    	                    
    	                    if(root.get("data").asText().equals("r"))
    	                    	pos = "r";
    	
    	                } catch (UnknownHostException e) {
    	                    e.printStackTrace();
    	                } catch (IOException e) {
    	                    e.printStackTrace();
    	                }
    	                timer.stop();
    	                game.setScreen(new JoinGameScreen(game, rawOutputStream, in, pos));
    	                
    	            }
                });
                table.row();
                table.add(lobbyButton).width(game.screenWidth / 2).height(game.screenHeight / 4);


                table.pack();
            }
        }
        
        stage.addActor(table);
    }

    @Override
    public void present() {
    	Gdx.gl.glClearColor(1, 1, 1, 1);
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
    	stage.dispose();
    	uiSkin.dispose();
    	timer.stop();
        System.gc();
    }

}
