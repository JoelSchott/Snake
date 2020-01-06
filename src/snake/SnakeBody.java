package snake;

import java.awt.Color;
import java.awt.Point;

public class SnakeBody {
	

	protected Point location;
	protected SnakeBody leader;
	protected Color color;
	
	public SnakeBody(Point loc, SnakeBody lead, Color c){
		location = loc;
		leader = lead;
		color = c;
	}

	
	public Point getLocation(){
		return location;
	}
	public Color getColor(){
		return color;
	}
	
	public void move(){
		location = leader.getLocation();
	}
	
	public static Color getRandomColor(){
		int red = (int)(Math.random() * 255);
		int green = (int)(Math.random() * 255);
		int blue=  (int)(Math.random() * 255);
		return new Color(red, green, blue);
	}
	
}
