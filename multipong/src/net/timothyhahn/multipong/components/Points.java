package net.timothyhahn.multipong.components;

import com.artemis.Component;

public class Points extends Component {

	private int points;
	
	public Points(){
		this.points = 0;
	}
	
	public int getPoints(){
		return this.points;
	}
	
	public void score(){
		this.points++;
	}
}
