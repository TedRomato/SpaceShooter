package package1;

public class ObjectAttachment extends GameObject{
	
	Corner attachmentRP;
	double atachmentCurrentAngle = 0;
	
	public ObjectAttachment(Corner[] corners, double[] rotationPoint,double[] attachmentRP, double rotationAngle) {
		super(corners, rotationPoint, rotationAngle) ;
		this.attachmentRP = new Corner(attachmentRP,rotationPoint);
	}
	
	public void rotateAttachment(double angle) {
		for(Corner c : getCorners()) {
			c.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
		}
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
	
	
	//counts new angle based on rotation of mother objects rotation angle so the attachment rotates same angle at all times
	public double getAngleToRotateConstantly(double obRotationAngle) {
		if(obRotationAngle >= 0 && getRotationAngle() >= 0 ) {
			return  getRotationAngle() - obRotationAngle;
		}
		else if(obRotationAngle < 0 && getRotationAngle() < 0) {
			return getRotationAngle() - obRotationAngle;
		}
		else {
			return getRotationAngle() - obRotationAngle;
		}
	}
	
	
	
	
	
}
