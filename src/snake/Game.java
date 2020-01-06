package snake;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class Game {
	
	
	private JFrame frame = new JFrame("Snake");
	private GamePanel mainPanel = new GamePanel();
	
	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;
	private static final int GRID_SIZE = 15;
	private static final int BOX_WIDTH = SCREEN_WIDTH/GRID_SIZE;
	private static final int BOX_HEIGHT = SCREEN_HEIGHT/GRID_SIZE;
	
	private boolean running = true;
	
	private Snake snake;
	private Snake snake2;
	private Snake aiSnake;
	private Snake aiSnake2;
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private ArrayList<Snake> snakes = new ArrayList<>();
	
	private Snake.Direction newDirection = Snake.Direction.NONE;
	private Snake.Direction newDirection2 = Snake.Direction.NONE;
	
	public static void main(String[] args){
		Game game = new Game();
		game.start();
	}
	
	public void start(){
		
		frame.add(mainPanel);
		frame.pack();
		
		frame.setSize(SCREEN_WIDTH + 16,SCREEN_HEIGHT + 40);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "moveUp");
		mainPanel.getActionMap().put("moveUp", new MoveUp());
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
		mainPanel.getActionMap().put("moveLeft", new MoveLeft());
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
		mainPanel.getActionMap().put("moveDown", new MoveDown());
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
		mainPanel.getActionMap().put("moveRight", new MoveRight());
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("W"), "moveUpSecond");
		mainPanel.getActionMap().put("moveUpSecond", new MoveUpSecond());
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("A"), "moveLeftSecond");
		mainPanel.getActionMap().put("moveLeftSecond", new MoveLeftSecond());
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("S"), "moveDownSecond");
		mainPanel.getActionMap().put("moveDownSecond", new MoveDownSecond());
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("D"), "moveRightSecond");
		mainPanel.getActionMap().put("moveRightSecond", new MoveRightSecond());
		
		frame.setVisible(true);
		
		
		Point center = new Point((SCREEN_WIDTH/GRID_SIZE)/2, (SCREEN_HEIGHT/GRID_SIZE)/2);
		Point secondCenter = new Point(center.x -1, center.y -1);
		Point leftMid = new Point(0, SCREEN_HEIGHT/GRID_SIZE/2);
		snake = new Snake(center, Snake.Direction.RIGHT, Snake.CORAL, false);
		snake2 = new Snake(secondCenter, Snake.Direction.UP, Snake.RATTLESNAKE, false);
		aiSnake = new Snake(leftMid, Snake.Direction.RIGHT, Snake.RAINBOW, true);
		aiSnake2 = new Snake(new Point(SCREEN_WIDTH/GRID_SIZE -1, SCREEN_HEIGHT/GRID_SIZE -1), Snake.Direction.LEFT, Snake.RATTLESNAKE, true);
		snakes.add(snake);
		snakes.add(snake2);
		//snakes.add(aiSnake);
		//snakes.add(aiSnake2);
		
		loop();
	}
	
	private void loop(){
		while (running){
			mainPanel.requestFocusInWindow();
			frame.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			handleHeadLocation();
			placePowerUps();
			setDirections();
			moveSnakes();			
			
			
			mainPanel.requestFocusInWindow();
			frame.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			handleHeadLocation();
			placePowerUps();
			setSpeedyDirections();
			moveSpeedySnakes();			
		}
		
	}
	
	private void moveSnakes(){
		for (Snake snake : snakes){
			snake.move();
		}
	}
	private void moveSpeedySnakes(){
		for (Snake snake : snakes){
			if (snake.getSpeed()){
				snake.moveFast();
			}
		}
	}
	private void setSpeedyDirections(){
		for (Snake snake : snakes){
			if (snake.getSpeed()){
				setDirection(snake);
			}
		}
	}
	private void setDirections(){
		for (Snake snake : snakes){
			setDirection(snake);
		}
	}
	
	private void setDirection(Snake s){
		
		if (s.equals(snake)){
			if (validNewDirection(snake.head.getDirection(), newDirection)){
				snake.head.setDirection(newDirection);
			}
			newDirection = Snake.Direction.NONE;
		}
		else if (s.equals(snake2)){
			if (validNewDirection(snake2.head.getDirection(), newDirection2)){
				snake2.head.setDirection(newDirection2);
			}
			newDirection2 = Snake.Direction.NONE;
		}
		
		else if (s.isAI()){
			PowerUp target = powerUps.get(0);
			Point currentLocation = s.head.getLocation();
			Snake.Direction currentDirection = s.head.getDirection();
			
			ArrayList<Point> dangerousLocations = new ArrayList<>();
			for (Snake snake : snakes){
				if (!snake.equals(s)){
					dangerousLocations.add(snake.head.getLocation());
					if (snake.head.getDirection().equals(Snake.Direction.UP)){
						dangerousLocations.add(new Point(snake.head.getLocation().x, snake.head.getLocation().y -1));
					}
					else if (snake.head.getDirection().equals(Snake.Direction.LEFT)){
						dangerousLocations.add(new Point(snake.head.getLocation().x-1, snake.head.getLocation().y));
					}
					else if (snake.head.getDirection().equals(Snake.Direction.DOWN)){
						dangerousLocations.add(new Point(snake.head.getLocation().x, snake.head.getLocation().y+1));
					}
					else{
						dangerousLocations.add(new Point(snake.head.getLocation().x + 1, snake.head.getLocation().y));
					}
					for (SnakeBody block : snake.body){
						dangerousLocations.add(block.getLocation());
					}
				}
			}
			
			boolean upDangerous = dangerousLocations.contains(new Point(currentLocation.x, currentLocation.y - 1)) || s.head.getLocation().y == 0;
			boolean leftDangerous = dangerousLocations.contains(new Point(currentLocation.x - 1, currentLocation.y)) || s.head.getLocation().x == 0;
			boolean downDangerous = dangerousLocations.contains(new Point(currentLocation.x, currentLocation.y + 1)) || currentLocation.y == SCREEN_HEIGHT/GRID_SIZE -1;
			boolean rightDangerous = dangerousLocations.contains(new Point(currentLocation.x + 1, currentLocation.y)) || currentLocation.x == SCREEN_WIDTH/GRID_SIZE -1;

			
			if (currentLocation.x < target.getLocation().x){
				if ((currentDirection.equals(Snake.Direction.UP) || currentDirection.equals(Snake.Direction.DOWN)) && !rightDangerous){
					s.head.setDirection(Snake.Direction.RIGHT);
				}
				else if (currentDirection.equals(Snake.Direction.LEFT)){
					if (!upDangerous){
						s.head.setDirection(Snake.Direction.UP);
					}
					else if (!downDangerous){
						s.head.setDirection(Snake.Direction.DOWN);
					}
					
				}
			}
			else if (currentLocation.x > target.getLocation().x){
				if ((currentDirection.equals(Snake.Direction.UP) || currentDirection.equals(Snake.Direction.DOWN)) && !leftDangerous){
					s.head.setDirection(Snake.Direction.LEFT);
				}
				else if (currentDirection.equals(Snake.Direction.RIGHT)){
					if (!upDangerous){
						s.head.setDirection(Snake.Direction.UP);
					}
					else if (!downDangerous){
						s.head.setDirection(Snake.Direction.DOWN);
					}
				}
			}
			else{
				if (currentDirection.equals(Snake.Direction.LEFT) || currentDirection.equals(Snake.Direction.RIGHT)){
					if (target.getLocation().y < currentLocation.y && !upDangerous){
						s.head.setDirection(Snake.Direction.UP);
					}
					else if (!downDangerous){
						s.head.setDirection(Snake.Direction.DOWN);
					}
				}
				else{
					if ((currentDirection.equals(Snake.Direction.UP) && target.getLocation().y > currentLocation.y) || 
							(currentDirection.equals(Snake.Direction.DOWN) && target.getLocation().y < currentLocation.y)){
						if (!leftDangerous){
							s.head.setDirection(Snake.Direction.LEFT);
						}
						else if (!rightDangerous){
							s.head.setDirection(Snake.Direction.RIGHT);
						}
					}
				}
			}
			currentDirection = s.head.getDirection();
			
			if (rightDangerous && currentDirection.equals(Snake.Direction.RIGHT)){
				if (!downDangerous){
					s.head.setDirection(Snake.Direction.DOWN);
				}
				else{
					s.head.setDirection(Snake.Direction.UP);
				}	
			}
			else if (downDangerous && currentDirection.equals(Snake.Direction.DOWN)){
				if (!leftDangerous){
					s.head.setDirection(Snake.Direction.LEFT);
				}
				else{
					s.head.setDirection(Snake.Direction.RIGHT);
				}	
			}
			else if (upDangerous && currentDirection.equals(Snake.Direction.UP)){
				if (!rightDangerous){
					s.head.setDirection(Snake.Direction.RIGHT);
				}
				else{
					s.head.setDirection(Snake.Direction.LEFT);
				}
			}
			else if (leftDangerous && currentDirection.equals(Snake.Direction.LEFT)){
				if (!upDangerous){
					s.head.setDirection(Snake.Direction.UP);
				}
				else{
					s.head.setDirection(Snake.Direction.DOWN);
				}
			}
		}
		
	}
	private boolean validNewDirection(Snake.Direction currentDirection, Snake.Direction newDirection){
		if (newDirection.equals(Snake.Direction.NONE)){
			return false;
		}
		if (((currentDirection == Snake.Direction.UP || currentDirection == Snake.Direction.DOWN) && (newDirection == Snake.Direction.LEFT || newDirection == Snake.Direction.RIGHT)) ||
				((currentDirection == Snake.Direction.LEFT || currentDirection == Snake.Direction.RIGHT) && (newDirection == Snake.Direction.UP || newDirection == Snake.Direction.DOWN))){
			return true;
		}
		return false;
	}
	
	private void placePowerUps(){
		if (powerUps.size() == 0){
			Point newLocation = null;
			boolean taken = true;
			while (taken){
				int randomX = (int)(Math.random() * BOX_WIDTH);
				int randomY = (int)(Math.random() * BOX_HEIGHT);
				newLocation = new Point(randomX, randomY);
				taken = false;
				for (Snake snake : snakes){
					if (snake.head.getLocation().equals(newLocation)){
						taken = true;
					}
					for (SnakeBody block : snake.body){
						if (block.getLocation().equals(newLocation)){
							taken = true;
						}
					}
				}
				
			}
			Apple apple = new Apple(newLocation);
			powerUps.add(apple);
		}
	}
	
	private void handleHeadLocation(){
		ArrayList<Integer> snakeIndexesToRemove = new ArrayList<>();
		for (int s = 0; s < snakes.size(); s++){
			Snake snake = snakes.get(s);
			boolean delete = false;
			for (Snake otherSnake : snakes){
				if (!otherSnake.equals(snake)){
					if (snake.head.getLocation().equals(otherSnake.head.getLocation())){
						delete = true;
					}
					for (SnakeBody block : otherSnake.body){
						if (snake.head.getLocation().equals(block.getLocation())){
							delete = true;
						}
					}
					
				}
			}
			if (snake.head.getLocation().x < 0 || snake.head.getLocation().x >= BOX_WIDTH || 
					snake.head.getLocation().y < 0 || snake.head.getLocation().y >= BOX_HEIGHT){
				delete = true;
			}
			int indexToRemove = -1;
			for (int i = 0; i < powerUps.size(); i++){
				if (snake.head.getLocation().equals(powerUps.get(i).getLocation())){
					snake.add(powerUps.get(i).getAddition());
					snake.addSpeed(powerUps.get(i).getSpeedBoost());
					indexToRemove = i;
				}	
			}
			if (indexToRemove != -1){
				powerUps.remove(indexToRemove);
			}
			if (delete){
				for (SnakeBody block : snake.body){
					Apple apple = new Apple(block.getLocation());
					powerUps.add(apple);
				}
				snakeIndexesToRemove.add(s);
			}
		}
		
		Collections.reverse(snakeIndexesToRemove);
		for (int r : snakeIndexesToRemove){
			snakes.remove(r);
		}
		
	}
	
	private class GamePanel extends JPanel{
		
		public GamePanel(){
			this.setBackground(new Color(0,150,25));
			this.setSize(WIDTH,HEIGHT);
			this.setVisible(true);
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			//drawApples
			g.setColor(Color.RED);
			for (PowerUp powerUp : powerUps){
				if (powerUp.getClass() == Apple.class){
					g.fillOval(powerUp.getLocation().x * GRID_SIZE, powerUp.getLocation().y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
				}
			}
			
			for (Snake snake : snakes){
				//draw snake body
				for (SnakeBody block : snake.body){
					g.setColor(block.getColor());
					g.fillRect(block.getLocation().x * GRID_SIZE, block.getLocation().y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
				}
			}
			for (Snake snake : snakes){
				//draw head
				g.setColor(snake.head.getColor());
				int x = snake.head.getLocation().x * GRID_SIZE;
				int y = snake.head.getLocation().y * GRID_SIZE;
				g.fillRect(x, y, GRID_SIZE, GRID_SIZE);
				int left = x + GRID_SIZE/8;
				int right = x + 5*GRID_SIZE/8;
				int top = y + GRID_SIZE/8;
				int bottom = y + 5*GRID_SIZE/8;
				int[] eyeX = new int[2];
				int[] eyeY = new int[2];
				if (snake.head.getDirection() == Snake.Direction.RIGHT){
					eyeX[0] = right;
					eyeX[1] = right;
					eyeY[0] = top;
					eyeY[1] = bottom;
				}
				else if (snake.head.getDirection() == Snake.Direction.UP){
					eyeX[0] = left;
					eyeX[1] = right;
					eyeY[0] = top;
					eyeY[1] = top;
				}
				else if (snake.head.getDirection() == Snake.Direction.LEFT){
					eyeX[0] = left;
					eyeX[1] = left;
					eyeY[0] = top;
					eyeY[1] = bottom;
				}
				else if (snake.head.getDirection() == Snake.Direction.DOWN){
					eyeX[0] = left;
					eyeX[1] = right;
					eyeY[0] = bottom;
					eyeY[1] = bottom;
				}
				g.setColor(Color.RED);
				int eyeSize = GRID_SIZE/3;
				g.fillOval(eyeX[0], eyeY[0], eyeSize, eyeSize);
				g.fillOval(eyeX[1], eyeY[1], eyeSize, eyeSize);
			}
			
			
			//draw lines
			g.setColor(Color.BLACK);
			for (int i = GRID_SIZE; i < SCREEN_WIDTH; i += GRID_SIZE){
				g.drawLine(i, 0, i, SCREEN_HEIGHT);
			}
			for (int i = GRID_SIZE; i < SCREEN_HEIGHT; i += GRID_SIZE){
				g.drawLine(0, i, SCREEN_WIDTH, i);
			}
		}
	}
	
	private class MoveUp extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection = Snake.Direction.UP;
		}	
	}
	private class MoveLeft extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection = Snake.Direction.LEFT;
		}	
	}
	private class MoveDown extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection = Snake.Direction.DOWN;
		}	
	}
	private class MoveRight extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection = Snake.Direction.RIGHT;
		}	
	}
	
	private class MoveUpSecond extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection2 = Snake.Direction.UP;
		}	
	}
	private class MoveLeftSecond extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection2 = Snake.Direction.LEFT;
		}	
	}
	private class MoveDownSecond extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection2 = Snake.Direction.DOWN;
		}	
	}
	private class MoveRightSecond extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newDirection2 = Snake.Direction.RIGHT;
		}	
	}

}
