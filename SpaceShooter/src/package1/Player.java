package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends GameObject implements KeyListener{
	private boolean forward = false, turnRight = false, turnLeft = false;
	char moveChar = 'w', turnLeftChar = 'a', turnRightChar = 'd';
	private Corner moveDirection;
	private Corner movePoint;
	private double xyRatio;
	private double maxSpeed = 4 ;
	private double currentSpeed = 0;
	private double acceleration = maxSpeed/100;
	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d);
		// CHYBA JE V MD PREPISUJE SE ZAKLAD VSECH CORNERU KTERE JSIU ODVOZENE Z MD 
		moveDirection = new Corner(md, rotationPoint);
		movePoint = new Corner(md, rotationPoint);
		getNewRatios();	

	}
	
	
	public void updatePlayer() {
		
		updateMovePoint();
		
		updateSpeed();

		moveOb();

		if(turnRight || turnLeft) {
			rotateOb();

		}
		if(forward) {
			getNewRatios();

			setNewVels();

		}
		
	
	}
	
	private void updateRotation() {
		if(turnRight) {
			makePositiveRotation();
		} else if(turnLeft) {
			makeNegativeRotation();
		}
	}
	
	public void rotateOb() {
		for(Corner corner : getCorners()) {
			corner.rotateCorner(getRotationPoint(), getRotationAngle());
		}
		movePoint.rotateCorner(getRotationPoint(), getRotationAngle());	
		}
	
	public void moveOb() {
		for(Corner corner : getCorners()) {
			corner.moveCorner(getVelX(),getVelY());
		}
		getRotationPoint()[0] += getVelX();
		getRotationPoint()[1] += getVelY();


		movePoint.moveCorner(getVelX(),getVelY());
		moveDirection.moveCorner(getVelX(),getVelY());

	
	}
	
	
	
	private void getNewRatios() {
		xyRatio = moveDirection.getXYRatio(getRotationPoint());
		
	} 
	
	private void setNewVels() {
		
		if(xyRatio == 0) {
			setVelY(currentSpeed);
			setVelX(0);
			setStraightVectorY();
		}
		else if(xyRatio == Double.POSITIVE_INFINITY) {
			setVelY(0);
			setVelX(currentSpeed);
			setStraightVectorX();
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
		
	}private void setStraightVectorX(){
		if(moveDirection.getX() < getRotationPoint()[0]) {
			setVelX(-currentSpeed);
		}
		
	}
	private void setStraightVectorY(){
		if(moveDirection.getY() < getRotationPoint()[0]) {
			setVelY(-currentSpeed);
		}
	}
	
	private void updateMovePoint() {
		if(forward) {
			moveDirection = new Corner(movePoint, getRotationPoint());
		}
	}
	
	public void updateSpeed() {
		if(forward && currentSpeed < maxSpeed) {
			currentSpeed += acceleration;
		}
		if(currentSpeed > maxSpeed) {
			currentSpeed = maxSpeed;
		}if(forward != true && currentSpeed > 0 - acceleration) {
			currentSpeed -= acceleration;
			if(currentSpeed < 0) {
				currentSpeed = 0;
			}
			getNewRatios();
			setNewVels();
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
	public void render(Graphics g) {
		for(int i = 0;i<getCorners().length;i++) {
			if(i<getCorners().length-1) {
				g.drawLine((int) Math.round(getCorners()[i].getX()),(int) Math.round(getCorners()[i].getY()),(int) Math.round(getCorners()[i+1].getX()),(int) Math.round(getCorners()[i+1].getY()));
			}
			else {
				g.drawLine((int) Math.round(getCorners()[i].getX()),(int) Math.round(getCorners()[i].getY()),(int) Math.round(getCorners()[0].getX()),(int) Math.round(getCorners()[0].getY()));
			}
		}
		g.setColor(Color.red);
		g.fillRect((int) Math.round(moveDirection.getX()),(int) Math.round(moveDirection.getY()), 10, 10);
		g.setColor(Color.darkGray);
		g.fillRect((int) Math.round(getRotationPoint()[0]),(int) Math.round(getRotationPoint()[1]), 9, 9);
		g.setColor(Color.BLUE);
		g.fillRect((int) Math.round(movePoint.getX()),(int) Math.round(movePoint.getY()), 8, 8);
	}
		
	
}
