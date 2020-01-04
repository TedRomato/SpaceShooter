package package1;

public class GameObject {
	private int[][] corners;
	private int[] rotationPoint;
	private int rotationAngle;
	
	public GameObject(int[][] corners, int[] rotationPoint, int rotationAngle) {
		for(int[] i : corners) {
			if(i.length != 2) {
				System.out.println("Object wrong coords");
			}	
		}
		if(rotationPoint.length != 2) {
			System.out.println("Rotation point wrong coords");
		}
		this.corners = corners;
		this.rotationPoint = rotationPoint;
		this.rotationAngle = rotationAngle;
	}
	

}
