package package1;

import java.awt.Color;
import java.awt.Graphics;

public class InteractiveAttachment extends ObjectAttachment{
	protected int reloadLenght;
	protected int reloadTimer;
	Corner wayPoint;
	Corner shootDirection, shootPoint;
	double attRotationAngle;
	boolean shoot;
	double[] rotationSegment = new double[] {};
	int dmg = 1;
	GameObject shotTrajectory;
	Corner aimCorner;
	double inaccuracy = 0;
	double inaccuracyX = 0;
	double inaccuracyY = 0;
	
//	double maxShotAngleDifference = 20;

	
	//Always make inter. attachments facing down, or count custom shootDir 

	public InteractiveAttachment(Corner[] corners, Corner rp, double[] attachmentRP, double rotationAngle, Corner wayPoint, double lenght, double width) {
		super(corners, rp, attachmentRP, rotationAngle);
		// TODO Auto-generated constructor stub
		this.wayPoint = wayPoint;
		attRotationAngle = 1;
		//Posible error while moving and rotating (shooting transition update)
		shootPoint = new Corner(new double[] {wayPoint.getX(),wayPoint.getY()+20}, getRotationPoint());
		shootDirection = new Corner(new double[] {wayPoint.getX(),wayPoint.getY()+40}, getRotationPoint());
		aimCorner = new Corner(rp,getRotationPoint());
		this.reloadTimer = 10;
		makeShotTrajectory(lenght, width);
		
	}
	

	public void rotateToCorner(Corner c) {
		Corner newC = new Corner(c , getAttachmentRP());
		Corner newC2 = new Corner(getWayPoint(),getAttachmentRP());
		double cAngle = newC.getAngle(getAttachmentRP());
		double wAngle = newC2.getAngle(getAttachmentRP());

		//From wangle
		double rightDifference;
		double leftDifference;
		double[] differences = Corner.getAngleDifferencRL(wAngle,cAngle);
		rightDifference = differences[0];
		leftDifference = differences[1];
		if(rightDifference > getAttachmentRotationAngle() + 1 && leftDifference > getAttachmentRotationAngle() + 1) {
			if(rightDifference < leftDifference) {
				rotateAttachmentAroundItsCorner(getAttachmentRotationAngle());
			}
			if(leftDifference < rightDifference) {
				rotateAttachmentAroundItsCorner(-getAttachmentRotationAngle());
		}
		}
		
	}
	
	public void rotateAttachment(double angle) {
			super.rotateAttachment(angle);
			wayPoint.rotateCorner(getRotationPoint(), angle);
			shootDirection.rotateCorner(getRotationPoint(), angle);
			shootPoint.rotateCorner(getRotationPoint(), angle);		
			shotTrajectory.rotateOb(angle);
		
	}
	
	public void rotateAttachmentAroundItsCorner(double angle) {
		if(checkIfInRotationSegment(getAttachmentAngle()+angle)) {
			super.rotateAttchmentAroundItsCorner(angle);
			wayPoint.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shootDirection.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shootPoint.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shotTrajectory.rotateObAroundDifferentCorner(attachmentRP, angle, getRotationPoint());
			}
		}	
	
	
	public void moveAttachment(double velX, double velY) {
		for(Corner c : getCorners()) {
			c.moveCorner(velX, velY);
			}
		getRotationPoint().moveCorner(velX, velY);
		attachmentRP.moveCorner(velX, velY);	
		wayPoint.moveCorner(velX, velY);
		shootDirection.moveCorner(velX, velY);
		shootPoint.moveCorner(velX, velY);
		shotTrajectory.setVels(velX, velY);
		shotTrajectory.moveObWithoutRP();

		}
	
	public Missile shoot(Corner goalCorner) {
		if(getShoot() && goalCorner != null) {
			if(decideIfFire(goalCorner)){
				Corner rp = new Corner(new double[] {getSP().getX(),getSP().getY()});
				Corner TopLeft = new Corner(new double[] {getSP().getX()-4*dmg,getSP().getY()-4*dmg}, rp);
				Corner BotLeft = new Corner(new double[] {getSP().getX()-4*dmg,getSP().getY()+4*dmg}, rp);
				Corner BotRight = new Corner(new double[] {getSP().getX()+4*dmg,getSP().getY()+4*dmg}, rp);
				Corner TopRight = new Corner(new double[] {getSP().getX()+4*dmg,getSP().getY()-4*dmg}, rp);
				Corner md = new Corner(new double[] {getSD().getX(), getSD().getY()}, rp);
				Missile m = new Missile(new Corner[] {TopLeft, BotLeft, BotRight, TopRight}, rp, 0,md,12);
				m.getNewRatios();
				m.setNewVels();
				m.setDmg(dmg);
				getNewInaccuracy();
				return m;
			}else {
				return null;
			}
		}
		else return null;
	}
	
	public Missile shoot() {
		if(getShoot()) {
				Corner rp = new Corner(new double[] {getSP().getX(),getSP().getY()});
				Corner TopLeft = new Corner(new double[] {getSP().getX()-4*dmg,getSP().getY()-4*dmg}, rp);
				Corner BotLeft = new Corner(new double[] {getSP().getX()-4*dmg,getSP().getY()+4*dmg}, rp);
				Corner BotRight = new Corner(new double[] {getSP().getX()+4*dmg,getSP().getY()+4*dmg}, rp);
				Corner TopRight = new Corner(new double[] {getSP().getX()+4*dmg,getSP().getY()-4*dmg}, rp);
				Corner md = new Corner(new double[] {getSD().getX(), getSD().getY()}, rp);
				Missile m = new Missile(new Corner[] {TopLeft, BotLeft, BotRight, TopRight}, rp, 0,md,12);
				m.getNewRatios();
				m.setNewVels();
				m.setDmg(dmg);
				return m;
			}
		else return null;
		}
	
	public void updateAimPoint(GameObject go) {
		setAimCorner(getNewAimCorner(go));
	}
	

	public Corner getNewAimCorner(GameObject go) {
		if(go instanceof MovingObject) {
			if(((MovingObject) go).getCurrentSpeed() > 0) {
				return addInaccuracy(this.getAimCornerForMovingOb((MovingObject) go));
			}
		}		
		return addInaccuracy(new Corner(go.getRotationPoint(), go.getRotationPoint()));
	}
	//TODO Improve missle speed (take it as argument)
	public Corner getAimCornerForMovingOb(MovingObject moo) {
		double mooSpeed = moo.getCurrentSpeed();
		double missileSpeed = 12;
		double ratio = missileSpeed/mooSpeed;
		double mdAngle = moo.getMoveDirection().getAngle(moo.getRotationPoint());
		Corner aiCorner = new Corner(this.getSP(), moo.getRotationPoint());
		double aiAngle = aiCorner.getAngle(moo.getRotationPoint());
		double aiMooMdangle = decideSmaller(Corner.getAngleDifferencRL(mdAngle, aiAngle));
		double distance = aiCorner.getPointDistance(moo.getRotationPoint());
		double prefirePointDistance = distance/Math.sqrt(((1+ratio*ratio)-2*(Math.sin(Math.toRadians(aiMooMdangle))*Math.sin(Math.toRadians(aiMooMdangle)))));
		return Corner.makeCornerUsinAngle(prefirePointDistance, mdAngle, moo.getRotationPoint());
	}
	
	private double decideSmaller(double[] ab) {
		if(ab[0] <= ab[1]) {
			return ab[0];
		}else {
			return ab[1];
		}
	}
	
	public Corner addInaccuracy(Corner c) {
		if(getInaccuracy() > 0) {	
			c.setX(c.getX() + inaccuracyX);
			c.setY(c.getY() + inaccuracyY);
			return c;
		}else {
			return c;
		}
	}
	
	private void getNewInaccuracy() {
		inaccuracyX = generateNumInRange(new double[] {-getInaccuracy(), getInaccuracy()});
		inaccuracyY = generateNumInRange(new double[] {-getInaccuracy(), getInaccuracy()});

	}
	
/*	public boolean decideIfFire(Corner goalCorner) {
		Corner sd = new Corner(getSD(),getAttachmentRP());
		Corner gd = new Corner(goalCorner, getAttachmentRP());
		double goalAngle = gd.getAngle(getAttachmentRP());
		double sAngle = sd.getAngle(getAttachmentRP());
		System.out.println("-----------");
		System.out.println("goal angle : " + goalAngle);
		System.out.println("shoot angle : " + sAngle);
		if(sAngle + maxShotAngleDifference > goalAngle && sAngle - maxShotAngleDifference < goalAngle) {
			System.out.println("shoot");

			return true;
		}else {
			System.out.println("dont shoot");

			return false;
		}
	}*/
	public boolean decideIfFire(Corner goalCorner) {
		
		if(shotTrajectory.checkCollision(new GameObject(new Corner[] {goalCorner, goalCorner}, goalCorner, 0))) {

			return true;
		}else {

			return false;
		}
	}
	
	
	public void handleFriendlyFire(AI[] ais) {
		for(AI ai : ais) {
			if(ai.checkCollision(shotTrajectory)) {
				setShoot(false);
			}
		}
	}
	
	public boolean checkIfInRotationSegment(double d) {
		if(rotationSegment.length != 2) {
			return true;
		}
		if(d > rotationSegment[0] && d < rotationSegment[1]) {
			return true;
		} else return false;
	}
	
	public int getReloadLenght() {
		return reloadLenght;
	}

	public void setReloadLenght(int reloadLenght) {
		this.reloadLenght = reloadLenght;
	}

	public int getReloadTimer() {
		return reloadTimer;
	}

	public void setReloadTimer(int reloadTimer) {
		this.reloadTimer = reloadTimer;
	}
	
	protected Corner getSP() {
		return shootPoint;
	}
	
	protected Corner getSD() {
		return shootDirection;
	}
		
	public Corner getWayPoint() {
		return wayPoint;
	}


	public void setWayPoint(Corner wayPoint) {
		this.wayPoint = wayPoint;
	}
	
	private double getAttachmentRotationAngle() {
		return attRotationAngle;
	}
	
	public void setAttRangle(double d) {
		attRotationAngle = d;
	}
	
	protected void setShoot(boolean b) {
		shoot = b;
	}
	protected boolean getShoot() {
		return shoot;
	}
	
	public void setRotationSegment(double[] s) {
		rotationSegment = s;
	}
	
	private void makeShotTrajectory(double lenght, double width) {
		Corner rp = new Corner(getRotationPoint(), getRotationPoint());
		Corner c1 = new Corner(getSD(), getRotationPoint());
		Corner c2 = new Corner(new double[] {getSD().getX()-width, getSD().getY()+lenght}, getRotationPoint());
		Corner c3 = new Corner(new double[] {getSD().getX()+width, getSD().getY()+lenght}, getRotationPoint());
		shotTrajectory =new GameObject(new Corner[] {c1,c2,c3}, getRotationPoint(),getRotationAngle());

	}
	
	public void render(Graphics g) {
//		shootDirection.renderCorner(g, 4);
//		shootPoint.renderCorner(g, 4);
//		shotTrajectory.render(g);
//		aimCorner.renderCorner(g, 10);
		super.render(g);
		
	}

	public void setAimCorner(Corner newAimCorner) {
		aimCorner = newAimCorner;
	}
	public Corner getAimCorner() {
		return aimCorner;
	}

	public void setDmg(int dmg) {
		// TODO Auto-generated method stub
		this.dmg = dmg;
	}
	
	public void setInaccuracy(double ina) {
		inaccuracy = ina;
	}
	
	public double getInaccuracy() {
		return inaccuracy;
	}
	

}