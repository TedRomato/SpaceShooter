package package1;

public class SpecialCharge extends LivingObject{
	
	GameObject whoShot;
	double distanceTraveled = 0;
	
	public SpecialCharge(Corner[] corners, Corner rotationPoint, double rotationAngle, Corner md) {
		super(corners, rotationPoint, rotationAngle, md);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public double getDistanceTraveled() {
		return distanceTraveled;
	}



	public void setDistanceTraveled(double distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}



	public void setWhoShot(GameObject whoShot) {
		this.whoShot = whoShot;
	}



	public void moveOb() {
		super.moveOb();
		distanceTraveled += getCurrentSpeed();
	}
	
	public GameObject getWhoShot() {
		return whoShot;
	}
		
}
