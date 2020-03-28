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
	
	
	//Always make inter. attachments facing down, or count custom shootDir 

	public InteractiveAttachment(Corner[] corners, Corner rp, double[] attachmentRP, int rotationAngle, Corner wayPoint) {
		super(corners, rp, attachmentRP, rotationAngle);
		// TODO Auto-generated constructor stub
		this.wayPoint = wayPoint;
		attRotationAngle = 1;
		//Posible error while moving and rotating (shooting transition update)
		shootPoint = new Corner(new double[] {wayPoint.getX(),wayPoint.getY()+20}, getRotationPoint());
		shootDirection = new Corner(new double[] {wayPoint.getX(),wayPoint.getY()+40}, getRotationPoint());
		this.reloadTimer = 10;
		
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
			
		
	}
	
	public void rotateAttachmentAroundItsCorner(double angle) {
		if(checkIfInRotationSegment(getAttachmentAngle()+angle)) {
			super.rotateAttchmentAroundItsCorner(angle);
			wayPoint.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shootDirection.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shootPoint.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
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

		}
	
	public Missile shoot() {
		Corner rp = new Corner(new double[] {getSP().getX(),getSP().getY()});
		Corner TopLeft = new Corner(new double[] {getSP().getX()-5,getSP().getY()-5}, rp);
		Corner BotLeft = new Corner(new double[] {getSP().getX()-5,getSP().getY()+5}, rp);
		Corner BotRight = new Corner(new double[] {getSP().getX()+5,getSP().getY()+5}, rp);
		Corner TopRight = new Corner(new double[] {getSP().getX()+5,getSP().getY()-5}, rp);
		Corner md = new Corner(new double[] {getSD().getX(), getSD().getY()}, rp);
		if(getShoot()) {
			Missile m = new Missile(new Corner[] {TopLeft, BotLeft, BotRight, TopRight}, rp, 0,md,12);
			m.getNewRatios();
			m.setNewVels();
			return m;
		}
		else return null;
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
	
	public void render(Graphics g) {
		shootDirection.renderCorner(g, 4);
		shootPoint.renderCorner(g, 4);
		super.render(g);
		
	}
}