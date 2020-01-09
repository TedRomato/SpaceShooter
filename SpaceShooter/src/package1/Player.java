package package1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends GameObject implements KeyListener{
	private boolean forward, turnRight, turnLeft;
	char moveChar = 'w', turnLeftChar = 'a', turnRightChar = 'd';
	private Corner moveDirection;
	private double xyRatio;
	private int maxSpeed;
	private int currentSpeed = 0;
	private int acceleration;
	public Player(Corner[] corners, int[] rotationPoint, double d) {
		super(corners, rotationPoint, d);
	}
	
	
	
	public void getNewRatios() {
		xyRatio = moveDirection.getXYRatio(getRotationPoint());
		
	} 
	
	private void setNewVels() {
		setVelY((int) Math.round(currentSpeed/(xyRatio + 1)));
		setVelX((int) Math.round(getVelY()*xyRatio));
	}
	
	public void updateSpeed() {
		if(forward && currentSpeed < maxSpeed) {
			currentSpeed += acceleration;
		}
		if(currentSpeed > maxSpeed) {
			currentSpeed = maxSpeed;
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() == moveChar) {
			System.out.println("pressed");
			forward = true;
			
		}
		if(e.getKeyChar() == turnLeftChar) {
			turnLeft = true;
			makeNegativeRotation();
		}
		if(e.getKeyChar() == turnRightChar) {
			turnRight = true;
			makePositiveRotation();
			
		}
	
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() == moveChar) {
			forward = false;
			
		}
		if(e.getKeyChar() == turnLeftChar) {
			turnLeft = false;
		}
		if(e.getKeyChar() == turnRightChar) {
			turnRight = false;
			
		}
	
	}
	
	private void makePositiveRotation() {
		setRotationAngle(Math.abs(getRotationAngle()));
	}
	private void makeNegativeRotation() {
		setRotationAngle(-Math.abs(getRotationAngle()));
	}
		
	
}
