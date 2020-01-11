package package1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends GameObject{
	private Corner moveDirection;
	private double xyRatio;
	private int maxSpeed;
	private int currentSpeed = 0;
	private int acceleration;
	public Player(Corner[] corners, double[] rotationPoint, double d) {
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
		if(Keys.forward && currentSpeed < maxSpeed) {
			currentSpeed += acceleration;
		}
		if(currentSpeed > maxSpeed) {
			currentSpeed = maxSpeed;
		}
	}
	
	private void makePositiveRotation() {
		setRotationAngle(Math.abs(getRotationAngle()));
	}
	private void makeNegativeRotation() {
		setRotationAngle(-Math.abs(getRotationAngle()));
	}
		
	
}
