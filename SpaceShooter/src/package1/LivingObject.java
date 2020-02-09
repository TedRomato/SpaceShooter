package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.RoundingMode;


//PRESUNOUT METODY LIVING OBJECTU Z HRACE 
public class LivingObject extends MovingObject{
	private boolean forward = false, turnRight = false, turnLeft = false;
	private Corner movePoint;
	private double maxSpeed = 2.5;
	private double acceleration = maxSpeed/200;
	
	public LivingObject(Corner[] corners, double[] rotationPoint2, double rotationAngle, Corner md) {
		super(corners, rotationPoint2, rotationAngle, md);
		movePoint = new Corner(md, rotationPoint2);
		setReflectedSpeed(maxSpeed*2);
		// TODO Auto-generated constructor stub
	}
	
	
public void updateOb() {
		
		//NaN error typek mizii for no reason :( asi nekde setuju infinity jako speed

				
		updateMovePoint();

		updateSpeed();

		
		moveOb();

	

		if(turnRight || turnLeft) {
			rotateOb();


		}
		updateForward();
		if(forward) {
			getNewRatios();


			setNewVels();


		}
		
	
	}

	private void updateForward() {
		if(getReflected()  == true) {
			forward = false;
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
	
	private void updateMovePoint() {
		// CHYBA kdyz zkousim get x a get y prepisovat do setru Direction tak to nefunguje :(
		if(forward) {
			moveDirection = new Corner(movePoint, getRotationPoint());
		}
	}
	
	
	public void updateSpeed() {
		if(forward && getCurrentSpeed() < maxSpeed) {
			setCurrentSpeed(getCurrentSpeed() + acceleration);
		}
		if(getCurrentSpeed() > maxSpeed) {
			setCurrentSpeed(maxSpeed);
		}if(forward != true && getCurrentSpeed() > 0 - acceleration) {
			setCurrentSpeed(getCurrentSpeed() - acceleration);
			if(getCurrentSpeed() < 0) {
				setCurrentSpeed(0);
			}
			getNewRatios();
			setNewVels();
		}
	}
	
	

	public void updateReflection() {
		if(isReflected()) {
			setReflectedTimer(getReflectedTimer() + 1);
			if(getReflectedTimer() >= getReflectedLenght()) {
				setReflected(false);
				setReflectedTimer(0);
			}
		}
	}
	
	
	
	protected void setForward(boolean b) {
		forward = b;
	}
	protected void setRight(boolean b) {
		turnRight = b;	
	}
	protected void setLeft(boolean b) {
		turnLeft = b;
	}
	protected boolean getForward() {
		return forward;
	}
	protected boolean getTurnLeft() {
		return turnLeft;
	}protected boolean getTurnRight() {
		return turnRight;
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
		g.setColor(Color.BLACK);
	}
} 
