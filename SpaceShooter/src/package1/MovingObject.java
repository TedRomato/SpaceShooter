package package1;

import java.awt.Color;
import java.awt.Graphics;

public class MovingObject extends GameObject{
	protected Corner moveDirection;
	private double xyRatio;
	private double currentSpeed = 0.1;
	private boolean reflected = false;
	private int reflectedTimer = 0;
	private int reflectedLenght = 80;
	private double reflectedSpeed = currentSpeed * 2;
	public MovingObject(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d);
		moveDirection = new Corner(md, rotationPoint);
		getNewRatios();
		setNewVels();

	}
	

	
	
	public void updateOb() {
		
		//NaN error typek mizii for no reason :( asi nekde setuju infinity jako speed
		moveOb();
		rotateOb();
	
	}
	
	private double getTempRpX(Corner c1, Corner c2) {
		double difference = Math.abs(c1.getX() - c2.getX());
		if(c1.getX() > c2.getX()) {
			return c2.getX() + difference/2;
		} else if(c1.getX() < c2.getX()) {
			return c1.getX() + difference/2;
		}
		
		System.out.println("71 moving ob returns null");
		return (Double) null;
	}
	
	public void reflect(Corner c1, Corner c2) {
		reflected = true;
		if(getReflected()) {
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
				
				rpx = getTempRpX(c1, c2);
				rpy = rpx*getAB(c1, c2)[0] + getAB(c1, c2)[1];
				/*
				rpx = getCrossedLineX(getAB(c1, c2), getAB(moveDirection, new Corner(new double[] {getRotationPoint()[0],getRotationPoint()[1]}, new double[] {getRotationPoint()[0],getRotationPoint()[1]})));
				rpy = rpx*getAB(c1, c2)[0] + getAB(c1, c2)[1]; */
				
			}

			double[] temprp = new double[] {rpx, rpy};
			Corner co = new Corner(new double[] {c1.getX(),c1.getY()} , temprp );
			Corner ct = new Corner(new double[] {c2.getX(),c2.getY()} , temprp );
			//New Move Direction Angle
			double nmdAngle = getNMDAngle(co, ct ,getRotationPoint()[0] , getRotationPoint()[1], temprp);
			if(nmdAngle - moveDirection.getAngle(getRotationPoint()) < 0) {
				moveDirection.rotateCorner(getRotationPoint(), -(moveDirection.getAngle(getRotationPoint())-nmdAngle));
			} else {
				moveDirection.rotateCorner(getRotationPoint(), nmdAngle - moveDirection.getAngle(getRotationPoint()));
			}
		
		}
		updateAfterReflect();		
	}
	
	
	
	
	
	
	
	private double getNMDAngle(Corner co, Corner ct ,double x , double y, double[] trp) {
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
	
	
	public void rotateOb() {
		for(Corner corner : getCorners()) {
			corner.rotateCorner(getRotationPoint(), getRotationAngle());
			}
		}
	
	public void moveOb() {
		for(Corner corner : getCorners()) {
			corner.moveCorner(getVelX(),getVelY());
		}
		getRotationPoint()[0] += getVelX();
		getRotationPoint()[1] += getVelY();
		moveDirection.moveCorner(getVelX(),getVelY());

	
	}
	
	
	
	
	protected void getNewRatios() {
		xyRatio = moveDirection.getXYRatio(getRotationPoint());
		
	} 
	
	protected void setNewVels() {
		
		if(xyRatio == 0) {
			setVelY(getCurrentSpeed());
			setVelX(0);
			setStraightVectorY();
		}
		else if(xyRatio == Double.POSITIVE_INFINITY) {
			setVelY(0);
			setVelX(getCurrentSpeed());
			setStraightVectorX();
		}
		else {
			if(moveDirection.getQadrant() == 2 || moveDirection.getQadrant() == 3) {
				setVelY( Math.abs(getCurrentSpeed()/Math.sqrt((xyRatio*xyRatio + 1))));

			}else {
				setVelY( -Math.abs(getCurrentSpeed()/Math.sqrt((xyRatio*xyRatio + 1))));

			}
			if(moveDirection.getQadrant() == 1 || moveDirection.getQadrant() == 2) {
				setVelX( Math.abs(getVelY()*xyRatio));
			} else {
				setVelX( -Math.abs(getVelY()*xyRatio));
			}
			
		}
			
	}
	
	private void updateAfterReflect() {
		getNewRatios();
		setCurrentSpeed(reflectedSpeed);
		setNewVels();
		setReflected(true);
	}
	
	
	private void setStraightVectorX(){
		if(moveDirection.getX() < getRotationPoint()[0]) {
			setVelX(-getCurrentSpeed());
		}
		
	}
	private void setStraightVectorY(){
		if(moveDirection.getY() < getRotationPoint()[1]) {
			setVelY(-getCurrentSpeed());
		}
	}
	
	protected void setReflectedSpeed(double d) {
		reflectedSpeed = d;
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
		g.setColor(Color.BLACK);
	}
	
	
	protected void setCurrentSpeed(double speed) {
		currentSpeed = speed;
		
	}
	protected boolean getReflected() {
		return isReflected();
	}




	public int getReflectedTimer() {
		return reflectedTimer;
	}




	public void setReflectedTimer(int reflectedTimer) {
		this.reflectedTimer = reflectedTimer;
	}




	public boolean isReflected() {
		return reflected;
	}




	public void setReflected(boolean reflected) {
		this.reflected = reflected;
	}




	public int getReflectedLenght() {
		return reflectedLenght;
	}




	public void setReflectedLenght(int reflectedLenght) {
		this.reflectedLenght = reflectedLenght;
	}




	public double getCurrentSpeed() {
		return currentSpeed;
	}
}
