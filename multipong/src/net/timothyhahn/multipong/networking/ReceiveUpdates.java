package net.timothyhahn.multipong.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.timothyhahn.multipong.State;

public class ReceiveUpdates implements Runnable {
	private volatile boolean updateAvailable;
	private State currentState;
	private BufferedReader in;
	private ObjectMapper mapper;
	private boolean running;
	
	public ReceiveUpdates(BufferedReader in){
		updateAvailable = false;
		running = true;
		currentState = new State();
		this.in = in;
		mapper = new ObjectMapper();
	}
	public boolean isUpdateAvailable(){
		return updateAvailable;
	}
	public State getUpdatedState(){
		updateAvailable = false;
		return currentState;
	}

	@Override
	public void run() {
		while(running){
			try {
				String worldJSON = in.readLine();
				if(worldJSON.length() > 0){
					State state = new State();
					JsonNode root = mapper.readTree(worldJSON);
					JsonNode entities;
					Iterator<JsonNode> it;
					switch(root.get("type").asText()) {
						case "fullsync":
							state.type = State.FULL;
							state.lPoints = root.get("lpoints").asInt();
							state.rPoints = root.get("rpoints").asInt();
							entities = root.get("entities");
							it = entities.iterator();
							while(it.hasNext()){
								JsonNode entity = it.next();
				                String entityID = entity.get("id").asText();
				                if(entityID.equals("LEFT")) {
				                	state.lY = entity.get("y").asInt();
				                	state.lVY = entity.get("yv").asInt();
				                } else if(entityID.equals("RIGHT")) {
				                	state.rY = entity.get("y").asInt();
				                    state.rVY = entity.get("yv").asInt();
				                } else {
				                	state.bX = entity.get("x").asInt();
				                	state.bY = entity.get("y").asInt();
				                	state.bVX = entity.get("xv").asInt();
				                	state.bVY = entity.get("yv").asInt();
				                }
							}
							break;
						case "move":
							state.type = State.MOVE;
							entities = root.get("entities");
							it = entities.iterator();
							while(it.hasNext()){
								JsonNode entity = it.next();
				                String entityID = entity.get("id").asText();
				                if(entityID.equals("LEFT")) {
				                	state.lY = entity.get("y").asInt();
				                	state.lVY = entity.get("yv").asInt();
				                } else if(entityID.equals("RIGHT")) {
				                	state.rY = entity.get("y").asInt();
				                    state.rVY = entity.get("yv").asInt();
				                }
							}
							break;
						default:
							System.out.println("Bad Update");
					}
						
					currentState = state;
					updateAvailable = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
				Thread.currentThread().interrupt();
			}
		}
	}
}
