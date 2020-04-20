package package1;

import java.awt.Graphics;

public class ObjectAttachment extends GameObject{
	
	Corner attachmentRP;
	double atachmentCurrentAngle = 0;
	boolean rotateWithParentOb = true;
	protected int reloadTimer;
	protected int reloadLenght;

	
	public ObjectAttachment(Corner[] corners, double[] rotationPoint,double[] attachmentRP, double rotationAngle) {
		super(corners, rotationPoint, rotationAngle) ;
		this.attachmentRP = new Corner(attachmentRP,rotationPoint);
	}
	
	//rotates attachment based on its rp 
	
	public ObjectAttachment(Corner[] corners, Corner rp, double[] attachmentRP2, double rotationAngle) {
		super(corners, rp, rotationAngle) ;
		this.attachmentRP = new Corner(attachmentRP2,rp);
		}

	public void rotateAttachment(double angle) {
		for(Corner c : getCorners()) {
			c.rotateCorner(getRotationPoint(), angle);
		}
		attachmentRP.rotateCorner(getRotationPoint(), angle);
	}
	
	public void rotateAttachmentAroundItsCorner(double angle) {
		for(Corner c : getCorners()) {
			c.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
		}
		attachmentRP.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
		atachmentCurrentAngle += angle;
	}
	
	
	public void moveAttachment(double velX, double velY) {
		for(Corner c : getCorners()) {
			c.moveCorner(velX, velY);
		}
		getRotationPoint().moveCorner(velX, velY);
		attachmentRP.moveCorner(velX, velY);	
		}
	
	public Corner getAttachmentRP() {
		return attachmentRP;
	}
	
	
	
	
	//counts new angle based on rotation of parent objects rotation angle so the attachment rotates same angle at all times
	public void rotateConstantly(double obRotationAngle) {
		if(obRotationAngle >= 0 && getRotationAngle() >= 0 ) {
			rotateAttachment( getRotationAngle() - obRotationAngle);
		}
		else if(obRotationAngle < 0 && getRotationAngle() < 0) {
			rotateAttachment( getRotationAngle() - obRotationAngle);
		}
		else {
			rotateAttachment(getRotationAngle() - obRotationAngle);
		}
	}
	
	public boolean getRotateWithParentOb() {
		return rotateWithParentOb;
	}
	
	public void setRotateWithParentOb(boolean b) {
		rotateWithParentOb = b;
	}
	
	protected Missile shoot(GameObject[] imunes) {
		return null;
	}
	
	public boolean shouldShoot(Corner c){
		return false;
	}
	
	public boolean shouldShoot(){
		return false;
	}
	
	public Corner getAimCorner() {
		return null;
	}
	
	public void render(Graphics g) {
		super.render(g);
	}
	
	public double  getAttachmentAngle() {
		return atachmentCurrentAngle;
	}
	
	public Corner getAttRP() {
		return attachmentRP;
	}
	

	
}
