package net.timothyhahn.multipong.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import net.timothyhahn.multipong.MultiPongGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MultiplayerLobbiesScreen extends Screen {
	
	private Stage stage;
	private int targetPort = 8000;
	private String targetHost = "localhost";
	TextButton createLobby;
	Table table;
	Skin uiSkin;
	private int counter = 0;

    HashMap<String, String> lobbyList;
    
	public MultiplayerLobbiesScreen(final MultiPongGame game) {
		super(game);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		 lobbyList = getLobbyList();
		 
		 
		createLobby = new TextButton("Create Lobby", uiSkin);
		
        createLobby.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				createLobby();
				lobbyList = getLobbyList();
			}
		});

	}

	private void createLobby(){
		String command = "CRE \r\n";
		byte[] commandBytes = command.getBytes();
		try (Socket socket = new Socket(targetHost, targetPort);
			OutputStream rawOutputStream = socket.getOutputStream()) {
			rawOutputStream.write(commandBytes);
			rawOutputStream.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private HashMap<String, String> getLobbyList() {
		String command = "LOB \r\n";
		byte[] commandBytes = command.getBytes();
		String result = "[]";
		try (Socket socket = new Socket(targetHost, targetPort);
			OutputStream rawOutputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();) {
			socket.setSoTimeout(3000);
			rawOutputStream.write(commandBytes);
			rawOutputStream.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			result = in.readLine().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("RESULT " + result);
		if(result.length() > 5) {
			HashMap<String, String> lobbiesMap = new HashMap<String, String>();
			String jsonString =  result.substring(4);
			JsonParser jsonParser = new JsonParser();
            JsonElement jsonRoot = null;
            jsonRoot = jsonParser.parse(jsonString);
            JsonArray jsonLobbies = jsonRoot.getAsJsonArray();
            for(int i = 0; i < jsonLobbies.size(); i++) {
            	JsonObject jsonLobby = jsonLobbies.get(i).getAsJsonObject();
            	System.out.println("Lobby ID: " + jsonLobby.get("id").getAsString() + " and Name: " + jsonLobby.get("name").getAsString() );
            	lobbiesMap.put(jsonLobby.get("id").getAsString(),  jsonLobby.get("name").getAsString());
            }
            return lobbiesMap;
		} else {
			return new HashMap<String, String>();
		}
	}

	@Override
	public void update() {
        table = new Table();
        table.add(createLobby).width(game.gameWidth / 2).height(game.gameHeight / 4);
        table.row();
        
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        if(counter > 50) {
        	lobbyList = getLobbyList();
        	counter = 0;
        }
        
        counter++;
        
        for (Map.Entry<String, String> lobby : lobbyList.entrySet()){
        	final TextButton lobbyButton = new TextButton(lobby.getValue(), uiSkin);
        	lobbyButton.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new JoinGameScreen(game, (String) lobbyButton.getText()));
        	}
        	});
        	table.add(lobbyButton);
        	table.row();
        }

        table.setFillParent(true);
		stage.addActor(table);
	}

	@Override
	public void present() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
       
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
