package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.RoundingMode;


//PRESUNOUT METODY LIVING OBJECTU Z HRACE 
public class LivingObject extends GameObject{
	private boolean forward = false, turnRight = false, turnLeft = false;
	private Corner moveDirection;
	private Corner movePoint;
	private double xyRatio;
	private double maxSpeed = 5;
	private double currentSpeed = 0;
	private double acceleration = maxSpeed/100;
	private boolean reflected = false;
	private int reflectedTimer = 0;
	private int reflectedLenght = 40;
	public LivingObject(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d);
		moveDirection = new Corner(md, rotationPoint);
		movePoint = new Corner(md, rotationPoint);
		getNewRatios();	

	}
	

	
	
	public void updateLivingOb() {
		
		//NaN error typek mizii for no reason :( asi nekde setuju infinity jako speed

				
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
	
	public void reflect(Corner c1, Corner c2) {
		if(reflectedTimer >= 10 || reflectedTimer == 0) {
			double rpx;
			double rpy;
			if(getAB(moveDirection, new Corner(new double[] {getRotationPoint()[0],getRotationPoint()[1]}, new double[] {getRotationPoint()[0],getRotationPoint()[1]}))[0] == Double.NEGATIVE_INFINITY  || getAB(moveDirection, new Corner(new double[] {getRotationPoint()[0],getRotationPoint()[1]}, new double[] {getRotationPoint()[0],getRotationPoint()[1]}))[0] == Double.POSITIVE_INFINITY) {
				rpx = moveDirection.getX();
				rpy = rpx*getAB(c1, c2)[0] + getAB(c1, c2)[1];
			}else if(getAB(c1 ,c2)[0] == Double.NEGATIVE_INFINITY  || getAB(c1 ,c2)[0] == Double.POSITIVE_INFINITY){
				rpx = c1.getX();
				rpy = rpx*getAB(moveDirection, new Corner(new double[] {getRotationPoint()[0],getRotationPoint()[1]}, new double[] {getRotationPoint()[0],getRotationPoint()[1]}))[0] + getAB(moveDirection, new Corner(new double[] {getRotationPoint()[0],getRotationPoint()[1]}, new double[] {getRotationPoint()[0],getRotationPoint()[1]}))[1];
			}
			
			else {
				rpx = getCrossedLineX(getAB(c1, c2), getAB(moveDirection, new Corner(new double[] {getRotationPoint()[0],getRotationPoint()[1]}, new double[] {getRotationPoint()[0],getRotationPoint()[1]})));
				rpy = rpx*getAB(c1, c2)[0] + getAB(c1, c2)[1];
			}

			double[] temprp = new double[] {rpx, rpy};
			Corner co = new Corner(new double[] {c1.getX(),c1.getY()} , temprp );
			Corner ct = new Corner(new double[] {c2.getX(),c2.getY()} , temprp );
			double nmdAngle = getDifference(co, ct ,getRotationPoint()[0] , getRotationPoint()[1], temprp);
		
			if(nmdAngle - moveDirection.getAngle(getRotationPoint()) < 0) {
				moveDirection.rotateCorner(getRotationPoint(), -(moveDirection.getAngle(getRotationPoint())-nmdAngle));
			} else {
				moveDirection.rotateCorner(getRotationPoint(), nmdAngle - moveDirection.getAngle(getRotationPoint()));
			}
			
			getNewRatios();
			setCurrentSpeed(maxSpeed);
			setNewVels();
			forward = false;
			reflected = true;
		}
		
		
	}
	
	
	public void updateReflection() {
		if(reflected) {
			reflectedTimer ++;
			if(reflectedTimer >= reflectedLenght) {
				reflected = false;
				reflectedTimer = 0;
			}
		}
	}
	
	
	private double getDifference(Corner co, Corner ct ,double x , double y, double[] trp) {
		double angleToRotate = 90;
		switch(co.getQadrant()) {
			case 1: 
				if(checkIfUnder(getAB(co, ct), x, y)) {
					return co.getAngle(trp) + angleToRotate;
				}else {
					
					return ct.getAngle(trp) + angleToRotate;
				}
			case 2: 
				if(checkIfUnder(getAB(co, ct), x, y)) {
					return co.getAngle(trp) + angleToRotate;
				}else {
					return co.getAngle(trp) - angleToRotate;
				}
			case 3: 
				if(checkIfUnder(getAB(co, ct), x, y)) {
					return co.getAngle(trp) - angleToRotate;
				}else {
					return co.getAngle(trp) + angleToRotate;
				}
			case 4: 
				if(checkIfUnder(getAB(co, ct), x, y)) {
					return co.getAngle(trp) - angleToRotate;
				}else {

					return ct.getAngle(trp) - angleToRotate;
				}
			case 0:
				if(co.getAngle(trp) == 270) {
					if(checkIfUnder(getAB(co, ct), x, y)) {
						return co.getAngle(trp) - angleToRotate;
					}else {
						return ct.getAngle(trp) - angleToRotate;
					}
				} else if(co.getAngle(trp) == 90) {
					if(checkIfUnder(getAB(co, ct), x, y)) {
						return co.getAngle(trp) + angleToRotate;
					}else {
						return co.getAngle(trp) - angleToRotate;
					}
				} else if(co.getAngle(trp) == 180) {
					if(x > co.getX()) {
						return co.getAngle(trp) - angleToRotate;
					} else {
						return co.getAngle(trp) + angleToRotate;
					}
				} else if(co.getAngle(trp) == 0) {
					if(x > co.getX()) {
						return co.getAngle(trp) + angleToRotate;
					} else {
						return co.getAngle(trp) - angleToRotate;
					}
				}
		}
		System.out.println("DANGER 68 LivingOb");
		return (Double) null;
	}
	
	
	
	private boolean checkIfUnder(double[] ab, double x, double y) {
		if(y > x*ab[0] + ab[1]) {
			return true;
		}
		return false;
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
	
	
	
	
	protected void getNewRatios() {
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
		if(moveDirection.getY() < getRotationPoint()[1]) {
			setVelY(-currentSpeed);
		}
	}
	
	private void updateMovePoint() {
		// CHYBA kdyz zkousim get x a get y prepisovat do setru Direction tak to nefunguje :(
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

	
	protected void makePositiveRotation() {
		setRotationAngle(Math.abs(getRotationAngle()));
	}
	protected void makeNegativeRotation() {
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
		g.setColor(Color.BLACK);
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
	protected void setCurrentSpeed(double speed) {
		currentSpeed = speed;
		
	}
	protected boolean getReflected() {
		return reflected;
	}
}
