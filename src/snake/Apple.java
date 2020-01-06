package snake;

import java.awt.Point;

public class Apple extends PowerUp{

	private final static int ADDITION = 2;
	private final static int SPEED_BOOST = 30;
	
	public Apple (Point location){
		super(location);
	}
	
	public int getAddition(){
		return ADDITION;
	}
	
	public int getSpeedBoost(){
		return SPEED_BOOST;
	}
}
