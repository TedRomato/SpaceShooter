package package1;

public class Meteor extends MovingObject {
	int size;
	double speed;
	public Meteor(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md, double speed, int size) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
		this.speed = speed;
		this.size = size;
	
	}
	
	
	public void handleMeteorCollision(GameObject go) {
	
	}
	
}
