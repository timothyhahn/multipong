package net.timothyhahn.multipong.components;

import com.artemis.Component;

public class Bounds extends Component {
	private int width;
	private int height;
	
	public Bounds(int size) {
		this.width = size;
		this.height = size;
	}
	public Bounds(int width, int height) {
		this.width =  width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}