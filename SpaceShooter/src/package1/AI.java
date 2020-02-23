package package1;

public class AI extends LivingObject{
	Corner goalDestination;
	

	public AI(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,Corner goalDestination) {
		super(corners, rotationPoint, rotationAngle, md);
		this.goalDestination = goalDestination;
		this.goalDestination.setToNewRP(rotationPoint);
		// TODO Auto-generated constructor stub
	}
	//AHOJ
	
	
	public void updateRotation() {
		this.goalDestination.setToNewRP(getRotationPoint());
		double movePointAngle = getMP().getAngle(getRotationPoint());
		double goalDestinationAngle = goalDestination.getAngle(getRotationPoint());
		double[] angleDifference = getMP().getAngleDifferencRL(movePointAngle, goalDestinationAngle);
		
	}

}
