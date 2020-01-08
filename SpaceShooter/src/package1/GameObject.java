package package1;

import java.awt.Graphics;

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
	
	public int getRotationAngle() {
		return rotationAngle;
	}
	public void setRotationAngle(int rotationAngle) {
		this.rotationAngle = rotationAngle;
	}
	public void render(Graphics g) {
		for(int i = 0;i<corners.length;i++) {
			if(i<corners.length-1) {
				g.drawLine(corners[i].getX(), corners[i].getY(), corners[i+1].getX(), corners[i+1].getY());
			}
			else {
				g.drawLine(corners[i].getX(), corners[i].getY(), corners[0].getX(), corners[0].getY());
			}
		}
	
	}
	

}
