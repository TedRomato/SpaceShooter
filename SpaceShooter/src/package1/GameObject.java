package package1;

import java.awt.Graphics;

public class GameObject {
	private Corner[] corners;
	private double[] rotationPoint;
	private double rotationAngle;
	private double velX;
	private double velY;
	
	
	public GameObject(Corner[] corners, double[] rotationPoint2, double rotationAngle) {
		this.corners = corners;
		if(rotationPoint2.length != 2) {
			System.out.println("Rotation point wrong coords in constructor");
		}
		
		this.setRotationPoint(rotationPoint2);
		this.rotationAngle = rotationAngle;

	}
	
	//p = peak, rc, lc pes = leftTop, mid, rightBot
	public boolean checkCollision(GameObject go) {
		boolean isCollision = false;
		double[] ab;
		System.out.println("Checking new collision");
		for(Corner checkedCorner : corners) {
			System.out.println("Checking new corner");
			for(int i = 0; i < go.getCorners().length; i++) {
				System.out.println("corner num " + i);
				if(i == go.getCorners().length-1) {
					if(checkedCorner.getX() > go.getCorners()[i].getX() && checkedCorner.getX() < go.getCorners()[0].getX() || checkedCorner.getX() < go.getCorners()[i].getX() && checkedCorner.getX() > go.getCorners()[0].getX() ) {
						ab = getAB(go.getCorners()[i],go.getCorners()[0]);
						isCollision = changeBooleanCollision(isCollision,checkedCorner.checkIfUnder(ab[0], ab[1]));
						
					} 
				}else {
					if(checkedCorner.getX() > go.getCorners()[i+1].getX() && checkedCorner.getX() < go.getCorners()[i].getX() || checkedCorner.getX() < go.getCorners()[i+1].getX() && checkedCorner.getX() > go.getCorners()[i].getX() ) {
						ab = getAB(go.getCorners()[i],go.getCorners()[i+1]);
						isCollision = changeBooleanCollision(isCollision,checkedCorner.checkIfUnder(ab[0], ab[1]));
					}
				}
				System.out.println(isCollision);
			}
			if(isCollision) {
				return true;
			}
		}
		return false;
	}
	
	private boolean changeBooleanCollision(boolean b, boolean newB) {
		if(newB == false) {
			return b;
		}
		if(b == true && newB == true) {
			return false;
		}
		//if(b == false && newB == true)
		else{
			return true;
		}
	}
	
	
	
	private double[] getAB(Corner one, Corner two) {
		double a,b;
		a = (one.getY()-two.getY())/(one.getX()-two.getX()); 
		b = one.getY() - a*one.getX();
		return new double[] {a,b};
	}
	


	public void rotateOb() {
		for(Corner corner : corners) {
			corner.rotateCorner(getRotationPoint(), rotationAngle);
		}
	}
	
	public void moveOb() {
		
		for(Corner corner : corners) {
			corner.moveCorner(getVelX(),getVelY());
		}
		getRotationPoint()[0] += getVelX();
		getRotationPoint()[1] += getVelY();
	}
	
	public void setVels(int velX, int velY) {
		this.setVelX(velX);
		this.setVelY(velY);
	}

	public double[] getRotationPoint() {
		return rotationPoint;
	}

	public void setRotationPoint(double[] rotationPoint2) {
		this.rotationPoint = rotationPoint2;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}
	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}
	
	public double getRotationAngle() {
		return rotationAngle;
	}
	public void setRotationAngle(double d) {
		this.rotationAngle = d;
	}
	
	public Corner[] getCorners() {
		return corners;
		
	}
	public void render(Graphics g) {
		for(int i = 0;i<corners.length;i++) {
			if(i<corners.length-1) {
				g.drawLine((int) Math.round(corners[i].getX()),(int) Math.round(corners[i].getY()),(int) Math.round(corners[i+1].getX()),(int) Math.round(corners[i+1].getY()));
			}
			else {
				g.drawLine((int) Math.round(corners[i].getX()),(int) Math.round(corners[i].getY()),(int) Math.round(corners[0].getX()),(int) Math.round(corners[0].getY()));
			}
		}
	
	}
	

}
