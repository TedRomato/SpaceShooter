package package1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends GameObject implements KeyListener{
	private boolean forward = true, turnRight = false, turnLeft = false;
	char moveChar = 'w', turnLeftChar = 'a', turnRightChar = 'd';
	private Corner moveDirection;
	private double xyRatio;
	private double maxSpeed = 2;
	private double currentSpeed = 0;
	private double acceleration = maxSpeed/100;
	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d);
		moveDirection = md;
	}
	
	
	public void updatePlayer() {
		updateSpeed();
		getNewRatios();
		setNewVels();
		System.out.println(getVelX() + " " + getVelY());
	}
	
	public void rotateOb() {
		for(Corner corner : getCorners()) {
			corner.rotateCorner(getRotationPoint(), getRotationAngle());
		}
		moveDirection.rotateCorner(getRotationPoint(), getRotationAngle());	
		}
	
	public void moveOb() {
		for(Corner corner : getCorners()) {
			corner.moveCorner(getVelX(),getVelY());
		}
		getRotationPoint()[0] += getVelX();
		getRotationPoint()[1] += getVelY();
		moveDirection.moveCorner(getVelX(),getVelY());
		
	}
	
	
	
	private void getNewRatios() {
		xyRatio = moveDirection.getXYRatio(getRotationPoint());
		
	} 
	
	private void setNewVels() {
		if(xyRatio == Double.POSITIVE_INFINITY) {
			setVelY(currentSpeed);
			setVelX(0);
		}
		else if(xyRatio == 0) {
			setVelY(0);
			setVelX(currentSpeed);
		}
		else {
			if(moveDirection.getQadrant() == 2 || moveDirection.getQadrant() == 3) {
				setVelY( Math.abs(currentSpeed/Math.sqrt((xyRatio*xyRatio + 1))));

			}else {
				setVelY( -Math.abs(currentSpeed/Math.sqrt((xyRatio*xyRatio + 1))));

			}
			if(moveDirection.getQadrant() == 1 || moveDirection.getQadrant() == 2) {
				setVelX( Math.abs(getVelY()*xyRatio));
			} else {
				setVelX( -Math.abs(getVelY()*xyRatio));
			}
			
		}
		
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
