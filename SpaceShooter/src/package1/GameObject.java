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
		
		this.setRotationPoint(rotationPoint);
		this.rotationAngle = rotationAngle;

	}
	
	public void rotateOb() {
		for(Corner corner : corners) {
			corner.rotateCorner(getRotationPoint(), rotationAngle);
		}
	}
	
	public void moveOb() {
		for(Corner corner : corners) {
			corner.moveCorner(velX,getVelY());
		}
		getRotationPoint()[0] += velX;
		getRotationPoint()[1] += getVelY();
	}
	
	public void setVels(int velX, int velY) {
		this.velX = velX;
		this.setVelY(velY);
	}

	public int[] getRotationPoint() {
		return rotationPoint;
	}

	public void setRotationPoint(int[] rotationPoint) {
		this.rotationPoint = rotationPoint;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}
	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}
	
	
	
	

}
