package net.timothyhahn.multipong.components;

import com.artemis.Component;

public class Velocity extends Component {
	private int x;
	private int y;
	public Velocity(int x, int y) {
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
	public void goUp() {
		this.y = -Math.abs(this.y);
	}
	public void goDown() {
		this.y = Math.abs(this.y);
	}
	public void goLeft() {
		this.x = -Math.abs(this.x);
	}
	public void goRight() {
		this.x = Math.abs(this.x);
	}
	public int getReflection() {
		if(this.y > 0)
			return this.y + 1;
		else 
			return this.y - 1;
	}
	public void speedUp() {
		if(Math.abs(this.x) < 8) {
			if(this.x > 0)
				this.x++;
			else
				this.x--;
		}
	}
}
