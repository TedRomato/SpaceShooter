package package1;

public class ObjectAttachment extends GameObject{
	
	double[] attachmentRP;
	double currentAngle = 0;
	
	public ObjectAttachment(Corner[] corners, double[] rotationPoint,double[] attachmentRP, double rotationAngle) {
		super(corners, rotationPoint, rotationAngle) ;
		this.attachmentRP = attachmentRP;
	}
	
	public void rotateAttachment() {
		for(Corner c : getCorners()) {
			c.rotateCorner(attachmentRP, getRotationAngle());
		}
		currentAngle += getRotationAngle();
	}
	
	
	
}
