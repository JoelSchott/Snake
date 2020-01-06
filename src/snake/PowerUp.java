package snake;

import java.awt.Point;

public abstract class PowerUp {

	protected Point location;
	
	public PowerUp(Point location){
		this.location = location;
	}
	
	public Point getLocation(){
		return location;
	}

	public abstract int getAddition();
	
	public abstract int getSpeedBoost();
	
}
