package snake;

import java.awt.Color;
import java.awt.Point;

public class SnakeHead extends SnakeBody {
	
	private Snake.Direction direction;
	
	public SnakeHead(Point loc, SnakeBody lead, Color c, Snake.Direction direction){
		super(loc, lead, c);
		this.direction = direction;
	}
	
	public Snake.Direction getDirection(){
		return direction;
	}
	public void setDirection(Snake.Direction direction){
		this.direction = direction;
	}
	
	@Override
	public void move(){
		if (direction == Snake.Direction.UP){
			location = new Point(location.x, location.y -1);
		}
		else if (direction == Snake.Direction.LEFT){
			location = new Point(location.x - 1, location.y);
		}
		else if (direction == Snake.Direction.DOWN){
			location = new Point(location.x, location.y + 1);
		}
		else{
			location = new Point(location.x + 1, location.y);
		}
	}
	
	
}
