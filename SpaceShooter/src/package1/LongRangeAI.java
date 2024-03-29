package package1;

import java.awt.Graphics;

public class LongRangeAI extends AI{
	Corner wayPoint;
	boolean wasInStoppingDistance = false;
	double goingDistance = 0;
	boolean goingLeft = false;


	
	public LongRangeAI(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, Corner goalDestination, Corner wP, int powerLvl) {
		super(corners, rotationPoint, rotationAngle, md, goalDestination, powerLvl);
		this.wayPoint = wP;
	}
	
	public void updateAI(GameObject[] enemys, GameObject[] gos, AI[] ais) {
		if(isInStoppingDistance()) {
			if(wasInStoppingDistance == false) {
				setShootForInteractiveAtts(true);
				wasInStoppingDistance = true;
				movePointAndDlsTransition(90);
				goingLeft = false;
				setRotationAngle(0.6);
			}
			turnIfCollisionDanger();
			updateInSD(enemys, gos, ais);
			findAndSetToClosestEnemy(enemys);

		
		}else {
			if(wasInStoppingDistance == true) {
				wasInStoppingDistance = false;
				turnBack();				
				setRotationAngle(1);
				setShootForInteractiveAtts(false);
			}
			super.updateAI(enemys, gos, ais);
			stopIfCollisionDanger();

		}
	}
	
	public void turnIfCollisionDanger() {
		if(collisionDanger == true) {
			movePointAndDlsTransition(180);
			if(goingLeft) {
				setRotationAngle(-0.6);
				goingLeft = false;
			}else {
				setRotationAngle(0.6);
				goingLeft = true;
				}		
			}
		
	}
	public void turnBack() {
		setRotationAngle(0.6);
		if(goingLeft) {
			movePointAndDlsTransition(90);
		}else {
			movePointAndDlsTransition(-90);
		}
	}
	
	private double addToStoppingDistance() {
		if(wasInStoppingDistance == true) {
			return goingDistance;
		}else {
			return 0;
		}
	}
	
	public void updateIsInStoppingDistance(GameObject enemy){
		double toAdd = addToStoppingDistance();
		stoppingDistance += toAdd;
		super.updateIsInStoppingDistance(enemy);
		stoppingDistance-= toAdd;
		
	}
	
	public void updateInSD(GameObject[] enemys, GameObject[] gos, AI[] ais) {
		findAndSetToClosestEnemy(enemys);
		updateAllAimCorners(getTargetedEnemy());
		checkAndHandleTrack(gos);
		if(collisionDanger == false) {
			setGoalToGameObject(getTargetedEnemy());
		}
		updateIsInStoppingDistance(getTargetedEnemy());
		updateForward();
		rotateToCorner(getTargetedEnemy().getRotationPoint());
		stopIfCollisionDanger();
		handleAllFriendlyFire(ais);

	}
	
	public void movePointAndDlsTransition(double angle) {
		getMP().rotateCorner(getRotationPoint(), angle);
		rotateDls(angle);
	}
		
	
	public void circleAround(Corner c) {
		double mpAngle = getMP().getAngle(getRotationPoint());
		double targetAngle = c.getAngle(getRotationPoint());
		double[] differences = Corner.getAngleDifferencRL(targetAngle, mpAngle);
		double rightDifference = differences[0];
		double leftDifference = differences[1];
		
		if(rightDifference <= leftDifference) {
			if(rightDifference > 90) {
				rotateMPAndDLS(getRotationPoint(), -getRotationAngle());
			}else {
				rotateMPAndDLS(getRotationPoint(), getRotationAngle());
			}
		}else {
			if(leftDifference > 90) {
				rotateMPAndDLS(getRotationPoint(), getRotationAngle());
			}else {
				rotateMPAndDLS(getRotationPoint(), -getRotationAngle());
			}
		}
	}
	
	public void rotateToCorner(Corner c) {
		Corner newC = new Corner(c , this.getRotationPoint());
		Corner newC2 = new Corner(getWayPoint(),this.getRotationPoint());
		double cAngle = newC.getAngle(this.getRotationPoint());
		double wAngle = newC2.getAngle(this.getRotationPoint());

		//From wangle
		double rightDifference;
		double leftDifference;
		double[] differences = Corner.getAngleDifferencRL(wAngle,cAngle);
		rightDifference = differences[0];
		leftDifference = differences[1];
		if(rightDifference > getRotationAngle() + 1 && leftDifference > getRotationAngle() + 1) {
			if(rightDifference < leftDifference) {
				setRight(true);
				setLeft(false);
				makePositiveRotation();
			}
			if(leftDifference < rightDifference) {
				setLeft(true);
				setRight(false);
				makeNegativeRotation();
				
			}
		}else {
			setRight(false);
			setLeft(false);
		}
		
	}
	
	
	
	public Corner getWayPoint() {
		return wayPoint;
	}

	public void setWayPoint(Corner wayPoint) {
		this.wayPoint = wayPoint;
	}

	public void rotateWithoutMPAndDLS() {
		super.rotateOb();
		if(this.getAttachments() != null) {
			for(ObjectAttachment att : this.getAttachments()) {
				if(att.getRotateWithParentOb()) {
					att.rotateAttachment(this.getRotationAngle());
				}
			}
		}
	}
	
	public void rotateOb() {
		super.rotateOb();
		getWayPoint().rotateCorner(getRotationPoint(), getRotationAngle());
		
	}
	
	public void moveOb() {
		super.moveOb();
		getWayPoint().moveCorner(getVelX(), getVelY());

	}
	
	public void rotateMPAndDLS(Corner rp, double rAngle) {
		this.getMP().rotateCorner(rp, rAngle);
		rotateDls(rAngle);
	}
	
	public double getGoingDistance() {
		return goingDistance;
	}

	public void setGoingDistance(double goingDistance) {
		this.goingDistance = goingDistance;
	}

	public void render(Graphics g) {
		super.render(g);
	//	g.setColor(Color.ORANGE);
	//	getWayPoint().renderCorner(g, 12);
	}
}
