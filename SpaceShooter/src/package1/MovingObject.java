package package1;

import java.awt.Graphics;

public class MovingObject extends GameObject{
	//game object that follows move direction Corner
	//able to set speed and reflect properly
	protected Corner moveDirection;
	private double xyRatio;
	private double currentSpeed = 0;
	private boolean reflected = false;
	private int reflectedTimer = 0;
	private int reflectedLenght = 80;
	private double reflectedSpeed;
	public MovingObject(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle);
		moveDirection = new Corner(md, rotationPoint);
		getNewRatios();
		setNewVels();
	}
	
	public MovingObject(Corner[] corners, Corner rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d);
		moveDirection = new Corner(md, rotationPoint);
		getNewRatios();
		setNewVels();
	}

	
	
	public void updateOb() {
		fixRotatedAngle();
		moveOb();
		rotateOb();
	
	}
	//reflects this from otherOb if collision
	public void checkAndHandleReflect(GameObject otherOb) {
		if(otherOb != this) {
			Corner[] corners = getCrossedLineCorners(otherOb);
			if(corners != null && corners.length == 2) {
				reflect(corners[0], corners[1]);
			}
		}
	}
	
	//updates object after reflect
	
	public void updateAfterReflect() {
		setCurrentSpeed(reflectedSpeed);
		getNewRatios();
		setNewVels();
		setReflected(true);

	}
	
	public void pushFromObject(GameObject go, double speed) {
		Corner c = new Corner(getRotationPoint(), go.getRotationPoint());
		double goalAngle = c.getAngle(go.getRotationPoint());
		Corner newMD = Corner.makeCornerUsinAngle(getMoveDirection().getPointDistance(getRotationPoint()), goalAngle, getRotationPoint());
		setMoveDirection(newMD);
		setCurrentSpeed(speed);
		getNewRatios();
		setNewVels();
		
	}
	
	
	//relfects from  line 
	//NOTE : it doesn't matter if objects touch with line 
	protected void reflect(Corner c1, Corner c2) {
		reflected = true;
		if(getReflected()) {
			double rpx;
			double rpy;
			
			if(getAB(moveDirection, getRotationPoint())[0] == Double.NEGATIVE_INFINITY  || getAB(moveDirection, getRotationPoint())[0] == Double.POSITIVE_INFINITY) {
				rpx = moveDirection.getX();
				rpy = rpx*getAB(c1, c2)[0] + getAB(c1, c2)[1];
			}else if(getAB(c1 ,c2)[0] == Double.NEGATIVE_INFINITY  || getAB(c1 ,c2)[0] == Double.POSITIVE_INFINITY){
				rpx = c1.getX();
				rpy = rpx*getAB(moveDirection, getRotationPoint())[0] + getAB(moveDirection, getRotationPoint())[1];
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
			double nmdAngle = getNMDAngle(co, ct ,getRotationPoint().getX() , getRotationPoint().getY(), temprp);
			if(nmdAngle - moveDirection.getAngle(getRotationPoint()) < 0) {
				moveDirection.rotateCorner(getRotationPoint(), -(moveDirection.getAngle(getRotationPoint())-nmdAngle));
			} else {
				moveDirection.rotateCorner(getRotationPoint(), nmdAngle - moveDirection.getAngle(getRotationPoint()));
			}
		
		}
		updateAfterReflect();	
	}
	
	private double getTempRpX(Corner c1, Corner c2) {
		double difference = Math.abs(c1.getX() - c2.getX());
		if(c1.getX() > c2.getX()) {
			return c2.getX() + difference/2;
		} else if(c1.getX() < c2.getX()) {
			return c1.getX() + difference/2;
		}
		
		System.out.println("returns null");
		return (Double) null;
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
		System.out.println("DANGER 155 LivingOb");
		return (Double) null;
	}
	
	
	
	private boolean checkIfUnder(double[] ab, double x, double y) {
		if(y > x*ab[0] + ab[1]) {
			return true;
		}
		return false;
	}
	
	/*
	public void rotateOb() {
		for(Corner corner : getCorners()) {
			corner.rotateCorner(getRotationPoint(), getRotationAngle());
			}
		}
		*/
	
	public void moveOb() {
		super.moveOb();

		moveDirection.moveCorner(getVelX(),getVelY());
	}
	
	public void moveOb(int velX, int velY) {
		super.moveOb(velX, velY);

		moveDirection.moveCorner(velX,velY);

	}
	
	
	
	//
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
	
	
	
	
	private void setStraightVectorX(){
		if(moveDirection.getX() < getRotationPoint().getX()) {
			setVelX(-getCurrentSpeed());
		}
		
	}
	private void setStraightVectorY(){
		if(moveDirection.getY() < getRotationPoint().getY()) {
			setVelY(-getCurrentSpeed());
		}
	}
	
	protected void setReflectedSpeed(double d) {
		reflectedSpeed = d;
	}
	
	

	
	
	public void render(Graphics g) {
		super.render(g);
	/*	g.setColor(Color.red);
		g.fillRect((int) Math.round(moveDirection.getX()*Game.screenRatio*Game.screenRatio),(int) Math.round(moveDirection.getY()*Game.screenRatio*Game.screenRatio*Game.screenRatio), 10, 10);
		g.setColor(Color.darkGray);
		g.fillRect((int) Math.round(getRotationPoint().getX()*Game.screenRatio),(int) Math.round(getRotationPoint().getY()*Game.screenRatio*Game.screenRatio), 9, 9);
		g.setColor(Color.BLUE);
		g.setColor(Color.BLACK); 
		*/
	}
	
	
	//sets speed
	
	protected void setCurrentSpeed(double speed) {
		currentSpeed = speed;
		//reflectedSpeed = speed;
		setNewVels();
		
		
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
	public double getReflectedSpeed() {
		return reflectedLenght;
	}
	public void setMoveDirection(Corner c) {
		this.moveDirection = c;
	}

	public Corner getMoveDirection() {
		// TODO Auto-generated method stub
		return moveDirection;
	}
	
	public void printXYRatio() {
		System.out.println(xyRatio);
	}
	
	
	
}
