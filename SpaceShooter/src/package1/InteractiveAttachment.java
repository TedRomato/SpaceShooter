package package1;

import java.awt.Color;
import java.awt.Graphics;

public class InteractiveAttachment extends ObjectAttachment{
	
	Corner wayPoint;
	double attRotationAngle;

	public InteractiveAttachment(Corner[] corners, Corner rp, double[] attachmentRP, int rotationAngle, Corner wayPoint) {
		super(corners, rp, attachmentRP, rotationAngle);
		// TODO Auto-generated constructor stub
		this.wayPoint = wayPoint;
		attRotationAngle = 1;
		
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
		if(rightDifference > getAttachmentRotationAngle() + 1 && leftDifference > getAttachmentRotationAngle() + 1) {
			if(rightDifference < leftDifference) {
				rotateAttachment(getAttachmentRotationAngle());
			}
			if(leftDifference < rightDifference) {
				rotateAttachment(-getAttachmentRotationAngle());
		}
		}
		
	}
	
	public void rotateAttachment(double angle) {
		for(Corner c : getCorners()) {
			c.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
		}
		wayPoint.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
	}
	
	
	public void moveAttachment(double velX, double velY) {
		for(Corner c : getCorners()) {
			c.moveCorner(velX, velY);
			}
		getRotationPoint().moveCorner(velX, velY);
		attachmentRP.moveCorner(velX, velY);	
		wayPoint.moveCorner(velX, velY);

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
	
	public void render(Graphics g) {
		g.fillRect(((int)wayPoint.getX()),((int)wayPoint.getY()), 10, 10);
		super.render(g);
		
	}
}