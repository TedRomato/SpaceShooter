package package1;

import java.awt.Graphics;

public class GameObject {
	private Corner[] corners;
	private double[] rotationPoint;
	private double rotationAngle;
	private double velX;
	private double velY;
	private boolean collision;

	
	
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
		for(Corner checkedCorner : corners) {
			for(int i = 0; i < go.getCorners().length; i++) {
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
			}
			if(isCollision) {
				setCollision(true);
				return true;
			}
		}
		setCollision(false);
		return false;
	}
	
	public Corner[] getCrossedLineCorners(GameObject go) {
		Corner[] corners = new Corner[] {};
		double smallestYDifference = Double.POSITIVE_INFINITY;
		//for each ob loop all lines ---> for all lines loop all lines of scnd ob
		for(int i = 0; i < go.getCorners().length - 1; i++) {
			for(int q = 0; q < getCorners().length - 1; q++) {
				//check if lines cross 
				if(checkIfCrosses(go.getCorners()[i],go.getCorners()[i+1],getCorners()[q],getCorners()[q+1])) {
					//if crosses and is closer to RP then
					if(getLineCornerYDifference(go.getCorners()[i],go.getCorners()[i+1], getRotationPoint()[0], getRotationPoint()[1]) < smallestYDifference) {
						smallestYDifference = getLineCornerYDifference(go.getCorners()[i],go.getCorners()[i+1], getRotationPoint()[0], getRotationPoint()[1]);
						corners = new Corner[] {go.getCorners()[i],go.getCorners()[i+1]};
					}
				}
			}
			if(checkIfCrosses(go.getCorners()[i],go.getCorners()[i+1],getCorners()[0],getCorners()[getCorners().length-1])) {
				//if crosses and is closer to RP then 
				if(getLineCornerYDifference(go.getCorners()[i],go.getCorners()[i+1], getRotationPoint()[0], getRotationPoint()[1]) < smallestYDifference) {
					smallestYDifference = getLineCornerYDifference(go.getCorners()[i],go.getCorners()[i+1], getRotationPoint()[0], getRotationPoint()[1]);
					corners = new Corner[] {go.getCorners()[i],go.getCorners()[i+1]};
				}
			}
		}
		for(int q = 0; q < getCorners().length - 1; q++) {
			//check if lines cross 
			if(checkIfCrosses(go.getCorners()[0],go.getCorners()[go.getCorners().length-1],getCorners()[q],getCorners()[q+1])) {
				//if crosses and is closer to RP then 
				if(getLineCornerYDifference(go.getCorners()[0],go.getCorners()[go.getCorners().length-1], getRotationPoint()[0], getRotationPoint()[1]) < smallestYDifference) {
					smallestYDifference = getLineCornerYDifference(go.getCorners()[0],go.getCorners()[go.getCorners().length-1], getRotationPoint()[0], getRotationPoint()[1]);
					corners = new Corner[] {go.getCorners()[0],go.getCorners()[go.getCorners().length-1]};
				}
			}
		}
		if(checkIfCrosses(go.getCorners()[0],go.getCorners()[go.getCorners().length-1],getCorners()[0],getCorners()[getCorners().length-1])) {
			//if crosses and is closer to RP then 
			if(getLineCornerYDifference(go.getCorners()[0],go.getCorners()[go.getCorners().length-1], getRotationPoint()[0], getRotationPoint()[1]) < smallestYDifference) {
				smallestYDifference = getLineCornerYDifference(go.getCorners()[0],go.getCorners()[go.getCorners().length-1], getRotationPoint()[0], getRotationPoint()[1]);
				corners = new Corner[] {go.getCorners()[0],go.getCorners()[go.getCorners().length-1]};
			}
		}
	/*	if(corners.length == 2) {
			System.out.println("corne1 x: " + corners[0].getX() + "corner1 y: " + corners[0].getY());
			System.out.println("corner2 x: " + corners[1].getX() +"corner2 y: " + corners[1].getY());
		} */
		
		System.out.println(" ");
		System.out.println(" ");
		return corners;
	}
	
	
	private double getLineCornerYDifference(Corner o1, Corner o2, double x, double y) {
		double difference;
		double ab[];
		ab = getAB(o1, o2);
		difference = x*ab[0] + ab[1] - y;
		if(ab[0] == Double.POSITIVE_INFINITY || ab[0] == Double.NEGATIVE_INFINITY) {
			return Math.abs(0);
		}
		return Math.abs(difference); 
	}
	
	
	private boolean checkIfCrosses(Corner o1, Corner o2, Corner l1, Corner l2) {
		// TODO udelat case i pro abl ininity :)
		
		//functions
		double abo[] = getAB(o1,o2);
		double abl[] = getAB(l1,l2);
		//crossing point x;
	//	System.out.println("ABO : " + abo[0] + "  " + abo[1] + "   ABl : " + abl[0] + "  " + abl[1]);
		if(abo[0] == Double.POSITIVE_INFINITY || abo[0] == Double.NEGATIVE_INFINITY) {
			if(abl[0] * o1.getX() + abl[1] <= o1.getY() && abl[0] * o1.getX() + abl[1] >= o2.getY() || abl[0] * o1.getX() + abl[1] >= o1.getY() && abl[0] * o1.getX() + abl[1] <= o2.getY()) {
				if(o1.getX() <= l1.getX() && o1.getX() >= l2.getX() || o1.getX() <= l2.getX() && o1.getX() >= l1.getX()) {
					return true;
				}
			}
		}
		double cpx = getCrossedLineX(abo, abl);
		if(cpx >= o1.getX() && cpx <= o2.getX()) { 	
			if(cpx >= l1.getX() && cpx < l2.getX()) {
				return true;
			} else if(cpx < l1.getX() && cpx >= l2.getX()){
				return true;
			}
		}
		else if(cpx < o1.getX() && cpx >= o2.getX()){
			if(cpx >= l1.getX() && cpx < l2.getX()) {
				return true;
			} else if(cpx < l1.getX() && cpx >= l2.getX()){
				return true;
			}
		} 
		
		return false;
			
	}
		
	
	protected double getCrossedLineX(double ab1[], double ab2[]) {
		return (ab1[1]-ab2[1])/(ab2[0] - ab1[0]);
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
	
	
	
	protected double[] getAB(Corner one, Corner two) {
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

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	

}
