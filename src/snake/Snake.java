package snake;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Snake {
	
	public enum Direction{
		UP, LEFT, DOWN, RIGHT, NONE
	}
	public static final Color[] CORAL = {Color.BLACK, Color.BLACK, Color.BLACK, Color.YELLOW, Color.RED, Color.RED, Color.RED, Color.YELLOW};
	public static final Color[] RAINBOW = {Color.GREEN, Color.BLUE, new Color(75,0,130), new Color(128,0,128), Color.RED, Color.ORANGE, Color.YELLOW};
	public static final Color[] RATTLESNAKE = {new Color(210,180,140), new Color(255,222,173), new Color(244,164,96), new Color(140,70,20)};
	
	private Color[] pattern;
	private boolean ai = false;

	public SnakeHead head;
	public ArrayList<SnakeBody> body = new ArrayList<SnakeBody>();
	
	private int partsToAdd = 10;
	private int fastMovements = 0;
	private boolean speed = false;
	
	public Snake(Point location, Direction direction, Color[] pattern, boolean ai){
		head = new SnakeHead(location, null, pattern[0], direction);
		this.pattern = pattern;
		this.ai = ai;
	}
	
	public void add(int addition){
		partsToAdd += addition;
	}
	public void addSpeed(int addition){
		fastMovements += addition;
		if (fastMovements > 0){
			speed = true;
		}
	}
	
	public void setSpeed(boolean b){
		speed = b;
	}
	public boolean getSpeed(){
		return speed;
	}
	public boolean isAI(){
		return ai;
	}
	
	public void moveFast(){
		move();
		fastMovements --;
		if (fastMovements == 0){
			speed = false;
		}
	}
	
	public void move(){
		if (partsToAdd > 0){
			Color patternColor = pattern[(body.size()+1) % pattern.length];
			if (body.size() == 0){
				body.add(new SnakeBody(head.getLocation(), head, patternColor));
			}
			else{
				SnakeBody tail = body.get(body.size() -1);
				body.add(new SnakeBody(tail.location, tail, patternColor));
			}
			partsToAdd --;
		}
		for (int i = body.size() -1; i > -1; i--){
			body.get(i).move();
		}
		head.move();
		
	}
	
	
	
}
