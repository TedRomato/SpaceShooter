package package1;

public class SpecialCharge extends Explosives{
	
	
	public SpecialCharge(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	public void updateExplosive() {
		super.updateExplosive();
	}
	
	public double getDistanceTraveled() {
		return distanceTraveled;
	}
	
	
		
}
