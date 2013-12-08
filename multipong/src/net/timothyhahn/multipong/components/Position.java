package net.timothyhahn.multipong.components;

import com.artemis.Component;

public class Position extends Component {
	private int x;
	private int y;
	
	private int startX;
	private int startY;
	
	public Position(int x, int y){
		this.startX = x;
		this.startY = y;
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void addX(int x) {
		this.x += x;
	}
	public void addY(int y){
		this.y += y;
	}
	public void reset(){
		this.x = startX;
		this.y = startY;
	}
}
