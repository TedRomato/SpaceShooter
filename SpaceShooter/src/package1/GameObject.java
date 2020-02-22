package package1;

import java.awt.Graphics;

public class GameObject {
	private Corner[] corners;
	private double[] rotationPoint;
	private double rotationAngle;
	private double velX;
	private double velY;
	private boolean collision;
	private int HP = 3;
	private int invulnerabilityLength = 10;
	private int invulnerabilityTimer = 0;
	private boolean invulnurable = false;

	
	
	public GameObject(Corner[] corners, double[] rotationPoint2, double rotationAngle) {
		this.corners = corners;
		if(rotationPoint2.length != 2) {
			System.out.println("Rotation point wrong coords in constructor");
		}
		
		this.setRotationPoint(rotationPoint2);
		this.rotationAngle = rotationAngle;

	}
	
	
	public void startInvulnurability() {
		invulnerabilityTimer = invulnerabilityLength;
		invulnurable = true;
	}
	
	public void updateInvulnurability() {
		if(invulnurable) {
			invulnerabilityTimer --;
			if(invulnerabilityTimer <= 0) {
				invulnurable = false;
			}
		}
	}
	

	
	//p = peak, rc, lc pes = leftTop, mid, rightBot
	public boolean checkCollision(GameObject go) {
		if(checkCollisionInside(go) || getCrossedLineCorners(go).length == 2) {
			return true;
		}else {
			return false;
		}
	}
	
	protected boolean checkCollisionInside(GameObject go) {
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
	
	private Corner[] checkIfAnyCornerCollision(GameObject go) {
		for(int i = 1; i < go.getCorners().length-1; i++) {
			if(checkIfCornerCollision(go.getCorners()[i])) {
				return new Corner[] {go.getCorners()[i], go.getCorners()[i-1],go.getCorners()[i+1]};
			}
		}
		
		if(checkIfCornerCollision(go.getCorners()[0])) {
			return new Corner[] {go.getCorners()[0], go.getCorners()[go.getCorners().length-1],go.getCorners()[1]};
		}else if(checkIfCornerCollision(go.getCorners()[go.getCorners().length-1])) {
			return new Corner[] {go.getCorners()[go.getCorners().length-1], go.getCorners()[0],go.getCorners()[go.getCorners().length-2]};
		}
		return null;
	}
	
	private boolean checkIfCornerCollision(Corner co) {
	//	System.out.println(checkCollision(new GameObject(new Corner[] {co,co}, new double[] {0,0}, 0)));
		GameObject c = new GameObject(new Corner[] {co,co}, new double[] {0,0}, 0);
		return c.checkCollisionInside(this);
	}
	
	private Corner[] getCornerReflectedCorners(Corner collided, Corner one, Corner two) {
		// TODO vyresit pro nekonecno a nulu
		double[] collidedRP = new double[] {collided.getX(), collided.getY()};
		Corner one1 = new Corner(new double[] {one.getX(),one.getY()},collidedRP);
		Corner two1 = new Corner(new double[] {two.getX(),two.getY()},collidedRP);
		double angleOne = one1.getAngle(collidedRP);
		double angleTwo = two1.getAngle(collidedRP);
		Corner newOne = getNewCorner(angleOne, collidedRP, one1.getQadrant(collidedRP));
		Corner newTwo = getNewCorner(angleTwo, collidedRP, two1.getQadrant(collidedRP));
		return new Corner[] {newOne, newTwo};
	}
	
	
	private Corner getNewCorner(double angle, double[] RP, int quadrant) {
		double[] xy = gNCSwitch(angle, quadrant);
		double cX = RP[0] + xy[0];
		double cY = RP[1] + xy[1];
		Corner c = new Corner(new double[] {cX, cY}, RP);
		return c;
	}
	
	
	private double[] gNCSwitch(double angle,int quadrant) {
		double distance = 300;
		double y;
		double x;
		switch(quadrant) {
			case 1: 
				y = Math.cos(Math.toRadians(angle))*distance;
				x = Math.sqrt(distance*distance - y*y);
				return new double[] {x, -y};
			case 2:
				x = Math.cos(Math.toRadians(angle-90))*distance;
				y = Math.sqrt(distance*distance - x*x);
				return new double[] {x, y};
			case 3: 
				y = Math.cos(Math.toRadians(angle-180))*distance;
				x = Math.sqrt(distance*distance - y*y);
				return new double[] {-x, y};
			case 4: 
				x = Math.cos(Math.toRadians(angle-270))*distance;
				y = Math.sqrt(distance*distance - x*x);
				return new double[] {-x, -y};
			case 0:
				if(angle == 90) {
					return new double[] {distance, 0};
				} else if(angle == 270){
					return new double[] {-distance, 0};
				} else if(angle == 180){
					return new double[] {0, distance};
				} else if(angle == 0 || angle == 360){
					return new double[] {0, -distance};
				}
		}
		System.out.println("Chyba get new cords switch");
		return null;
	}
	
	private Corner[] gettingNewCornerHandle(Corner g1,Corner g2,Corner lo1,Corner lo2, Corner[] corners) {
		if(getNewCLCorners(g1,g2,lo1,lo2) != null ) {
			return getNewCLCorners(g1,g2,lo1,lo2);
		} else {
			return corners;
		}
	}
	
	private Corner[] getNewCLCorners(Corner g1,Corner g2,Corner lo1,Corner lo2) {
		if(checkIfCrosses(g1,g2,lo1,lo2)) {
			//if crosses and is closer to RP then
			Corner[] corners = new Corner[] {g1,g2};
			return corners;
		}else {
			return null;
		}
		
			
	}
	
	public Corner[] getCrossedLineCorners(GameObject go) {
		Corner[] crossedCorner = checkIfAnyCornerCollision(go);
		if(crossedCorner != null) {
			//bacha jestli najizdis na vnitrni cornery
			return getCornerReflectedCorners(crossedCorner[0], crossedCorner[1], crossedCorner[2]);
		}
		
		Corner[] corners = new Corner[] {};
		double smallestYDifference = Double.POSITIVE_INFINITY;
		//for each ob loop all lines ---> for all lines loop all lines of scnd ob
		for(int i = 0; i < go.getCorners().length - 1; i++) {
			for(int q = 0; q < getCorners().length - 1; q++) {
				//check if lines cross 
				corners = gettingNewCornerHandle(go.getCorners()[i],go.getCorners()[i+1], getCorners()[q],getCorners()[q+1], corners);
			
			}
			corners = gettingNewCornerHandle(go.getCorners()[i],go.getCorners()[i+1],getCorners()[0],getCorners()[getCorners().length-1], corners);
			
		}
		for(int q = 0; q < getCorners().length - 1; q++) {
			//check if lines cross 
			corners = gettingNewCornerHandle(go.getCorners()[0],go.getCorners()[go.getCorners().length-1],getCorners()[q],getCorners()[q+1], corners);
			
		}
		corners = gettingNewCornerHandle(go.getCorners()[0],go.getCorners()[go.getCorners().length-1],getCorners()[0],getCorners()[getCorners().length-1], corners);
	/*	if(corners.length == 2) {
			System.out.println("corne1 x: " + corners[0].getX() + "corner1 y: " + corners[0].getY());
			System.out.println("corner2 x: " + corners[1].getX() +"corner2 y: " + corners[1].getY());
		} */
		
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
		double cpy = abo[0]*cpx + abo[1];
		if(cpy <= o1.getY() && cpy >= o2.getY() || cpy < o2.getY() && cpy > o1.getY() && cpy < l1.getY() && cpy > l2.getY() || cpy < l2.getY() && cpy > l1.getY()) {
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
	
	
	public void updateOb() {
		moveOb();
		rotateOb();
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
	
	public void setVels(double velX, double velY) {
		this.setVelX(velX);
		this.setVelY(velY);
	}
	
	
	public boolean getInvulnurability() {
		return invulnurable;
	}
	
	public void setHP(int HP) {
		this.HP = HP;
	}
	
	public int getHP() {
		return HP;
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
	
	public void addToRP(double velX, double velY) {
		this.rotationPoint[0] += velX;
		this.rotationPoint[1] += velY;
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
