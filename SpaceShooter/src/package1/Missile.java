package package1;

public class Missile extends MovingObject{

	public Missile(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner movingDirection, double speed) {
		super(corners, rotationPoint, rotationAngle, movingDirection);
		setHP(1);
		setCurrentSpeed(speed);
		// TODO Auto-generated constructor stub
	}

	
	
}
