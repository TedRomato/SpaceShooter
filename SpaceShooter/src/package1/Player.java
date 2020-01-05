package package1;

public class Player extends GameObject {
	private boolean forward;
	private Corner moveDirection;
	private double xyRatio;
	private int maxSpeed;
	private int currentSpeed = 0;
	private int acceleration;
	public Player(Corner[] corners, int[] rotationPoint, int rotationAngle) {
		super(corners, rotationPoint, rotationAngle);
	}
	
	
	
	public void getNewRatios() {
		xyRatio = moveDirection.getXYRatio(getRotationPoint());
		
	} 
	
	private void setNewVels() {
		setVelY((int) Math.round(currentSpeed/(xyRatio + 1)));
		setVelX((int) Math.round(getVelY()*xyRatio));
	}
	
	public void updateSpeed() {
		if(forward && currentSpeed < maxSpeed) {
			currentSpeed += acceleration;
		}
		if(currentSpeed > maxSpeed) {
			currentSpeed = maxSpeed;
		}
	}
	
	
}
