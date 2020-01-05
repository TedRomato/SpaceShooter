package package1;

public class GameObject {
	private Corner[] corners;
	private int[] rotationPoint;
	private int rotationAngle;
	
	
	public GameObject(Corner[] corners, int[] rotationPoint, int rotationAngle) {
	
		if(rotationPoint.length != 2) {
			System.out.println("Rotation point wrong coords in constructor");
		}
		
		this.rotationPoint = rotationPoint;
		this.rotationAngle = rotationAngle;

	}
	
	
	
	

}
