package package1;

public class GameObject {
	private Corner[] corners;
	private int[] rotationPoint;
	private int rotationAngle;
	private int velX, velY;
	
	
	public GameObject(Corner[] corners, int[] rotationPoint, int rotationAngle) {
	
		if(rotationPoint.length != 2) {
			System.out.println("Rotation point wrong coords in constructor");
		}
		
		this.rotationPoint = rotationPoint;
		this.rotationAngle = rotationAngle;

	}
	
	public void rotateOb() {
		for(Corner corner : corners) {
			corner.rotateCorner(rotationPoint, rotationAngle);
		}
	}
	
	public void moveOb() {
		for(Corner corner : corners) {
			corner.moveCorner(velX,velY);
		}
		rotationPoint[0] += velX;
		rotationPoint[1] += velY;
	}
	
	public void setVels(int velX, int velY) {
		this.velX = velX;
		this.velY = velY;
	}
	
	
	
	

}
