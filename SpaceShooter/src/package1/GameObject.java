package package1;

import java.awt.Graphics;

import javax.swing.JProgressBar;

public class GameObject {
	
	//Basic object
	//Made out of Corners 
	//able to rotate, move and has some additional fucntions
	private Corner[] corners;
	private Corner rotationPoint;
	private double rotationAngle;
	private double velX;
	private double velY;
	private boolean collision;
	private int HP = 3;
    //can be activated after reflection for other beahaviour
	private double invulnerabilityLength = 1*Game.tickMultiply;
	private double invulnerabilityTimer = 0;
	private boolean invulnurable = false;
	
	double angleRotated = 0;
	private Square collisionSquare;
	
	

	
	
	public GameObject(Corner[] corners, double[] rotationPoint2, double rotationAngle) {
		this.corners = corners;
		if(rotationPoint2.length != 2) { 
			System.out.println("Rotation point wrong coords in constructor");
		}
		
		this.setRotationPoint(rotationPoint2);
		initialRAngleSet(rotationAngle);
		makeSquare(getFurthestDistance());
		
	}
	

	public GameObject(Corner[] corners, Corner rp, double rotationAngle) {
		this.corners = corners;
		rotationPoint = rp;
		this.rotationAngle = rotationAngle;
		makeSquare(getFurthestDistance());
		initialRAngleSet(rotationAngle);


	}
	//updates object --> move and rotate
	public void updateOb() {
		fixRotatedAngle();
		moveOb();
		rotateOb();
	}
	
	
	public static GameObject generatePeriodicObject(double distance,int cornerAmount, Corner rp) {
		double angleDifference = 360/cornerAmount;
		Corner[] corners = new Corner[cornerAmount];
		for(int i = 0; i < corners.length; i++) {
			corners[i] = Corner.makeCornerUsinAngle(distance, i*angleDifference, rp);
		}
		return new GameObject(corners,rp,0);
	}
	
	//Check collision  between this object and go
	
		public boolean checkCollision(GameObject go) {
			if(this.getCollisionSquare().squareCollision(go.getCollisionSquare())) {
				if(go instanceof LivingObject) {
					if(((LivingObject) go).getAttachments() != null) {
						if(((LivingObject) go).getAttachments().length > 0) {
							for(ObjectAttachment att : ((LivingObject) go).getAttachments()) {
								if(checkCollisionPlain(att)) {
									return true;
								}
							}
						}
					}
				}
				if(checkCollisionInside(go) || getCrossedLineCorners(go).length == 2 ) {
					return true;
				}
				else {
					return false;
				}
			}
			return false;
		}
		
		public boolean checkCollisionPlain(GameObject go) {
			if(this.getCollisionSquare().squareCollision(go.getCollisionSquare())) {
				if(checkCollisionInside(go) || getCrossedLineCorners(go).length == 2 ) {
					return true;
				}
				else {
					return false;
				}
			}
			return false;
		}
		
		
		//sides bot - y,right - x,top - y,left - x 
		public boolean checkIfOutsideMap(int[] sides, int distance) {
			double rpy = this.getRotationPoint().getY();
			double rpx = this.getRotationPoint().getX();
			if(rpy > sides[0] + distance || rpy < sides[2] - distance) {
				return true;
			}
			else if(rpx > sides[1] + distance || rpx < sides[3] - distance) {
				return true;
			}else {
				return false;
			}
			

		}
	//generates corner is range from random side;	
	//inRange = minimum distance of rp - maximum distance of rp (from side)
	//sides = bot - y,right - x,top - y,left - x 
	public static Corner generateCornerOutsideMapInRange(Corner c,int width, int height, int[] range) {
		int side = pickRandomSide();

		switch(side) {
		case 0:
			return generateCornerInRect(c.getX() - range[1],c.getY() + height + range[0], 2*range[1] + width, range[1] - range[0]);			
		case 1:
			return generateCornerInRect(c.getX() + width + range[0], c.getY() - range[1],range[1] - range[0],height + 2*range[1]);	
		case 2:	
			return generateCornerInRect(c.getX() - range[1], c.getY() - range[1], 2*range[1] + width, range[1] - range[0]);			
		case 3:
			return generateCornerInRect(c.getX() - range[1], c.getY() - range[1],range[1] - range[0],height + 2*range[1]);			
			
		}
		System.out.println("Side : " + side);
		System.out.println("returning null");
		return c;
		
	}
	
	//generates random Corner in a rectangle
	
	public static Corner generateCornerInRect(double x,double y,double width,double height) {
		double xCoord;
		double yCoord;
		xCoord = Math.random()*width + x;
		yCoord = Math.random()*height + y;
		return new Corner(new double[] {xCoord,yCoord});
	}
	
	
	private static int pickRandomSide() {
		return (int) Math.floor(Math.random()*4);
	}
	
	//returns true if rotation point is outside specified rectangle
	//returns false if is Inside rectangle
	
	public boolean checkIfOutsideRect(int x, int y, int width, int height) {
		if(this.getRotationPoint().getX() > x + width || this.getRotationPoint().getX() < x) {
			return true;
		}
		if(this.getRotationPoint().getY() > y + height || this.getRotationPoint().getY() < y) {

			return true;
		}
		return false;
	}
	
	public void startInvulnurability() {
		invulnerabilityTimer = invulnerabilityLength;
		invulnurable = true;
	}
	
	public void updateInvulnurability() {
		if(invulnurable) {
			invulnerabilityTimer -= Game.tickOne;
			if(invulnerabilityTimer <= 0) {
				invulnurable = false;
			}
		}
	}
	
	

	public void handleCollision(GameObject ob) {
		if(ob instanceof Missile) {
			if(!((Missile) ob).isImune(this)) {
				setHP(getHP() - ((Missile) ob).getDmg());
				startInvulnurability();
			}
		}else {
			setHP(getHP() - 1);
			startInvulnurability();
		}
	}
	
	public GameObject getClosestEnemy(GameObject[] enemys) {
		GameObject closestEnemy = null;
		double closest = 100000;
		double newDistance = 0;
		
		for(GameObject gob : enemys) {
			newDistance = this.getRotationPoint().getPointDistance(gob.getRotationPoint());
			if(newDistance < closest) {
				closestEnemy = gob;
				closest = newDistance;
			}
		}
		return closestEnemy;
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
		GameObject c = new GameObject(new Corner[] {co,co}, new double[] {0,0}, 0);
		return c.checkCollisionInside(this);
	}
	
	private Corner[] getCornerReflectedCorners(Corner collided, Corner one, Corner two) {
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
					return new double[] {distance, 0.0};
				} else if(angle == 270){
					return new double[] {-distance, 0.0};
				} else if(angle == 180){
					return new double[] {0.0, distance};
				} else if(angle == 0 || angle == 360){
					return new double[] {0.0, -distance};
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
	
	//returns Corners to reflect from (if bumped into line returns line corners || if bumped into corner return corners that are distanced same length on both lines going from corner and placed on roght side of collision) 
	
	public Corner[] getCrossedLineCorners(GameObject go) {
		Corner[] crossedCorner = checkIfAnyCornerCollision(go);
		if(crossedCorner != null) {
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

		if(corners.length == 0 && go instanceof LivingObject) {
			if(((LivingObject) go).getAttachments() != null) {
				for(ObjectAttachment att : ((LivingObject) go).getAttachments()) {
					corners = att.getCrossedLineCorners(this);
					if(corners.length != 0) {
						return corners;
					}
					
				}
			}
			
		}
		return corners;
	}
	
	
	
	//checks if those o1 - o2 && l1-l2 cross
	
	private boolean checkIfCrosses(Corner o1, Corner o2, Corner l1, Corner l2) {		
		//functions
		double abo[] = getAB(o1,o2);
		double abl[] = getAB(l1,l2);
		//crossing point x;
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
		angleRotated += rotationAngle;
	}
	
	public void rotateOb(double angle) {
		for(Corner corner : corners) {
			corner.rotateCorner(getRotationPoint(), angle);
		}
		angleRotated += angle;
	}
	
	public void moveOb() {
		
		for(Corner corner : corners) {
			corner.moveCorner(getVelX(),getVelY());
		}
		getRotationPoint().moveCorner(getVelX(),getVelY());
		collisionSquare.moveSquare(velX, velY);
	}
	
	public void moveObWithoutRP() {
		for(Corner corner : corners) {
			corner.moveCorner(getVelX(),getVelY());
		}
		collisionSquare.moveSquare(velX, velY);
	}
	
	public void moveOb(int velX, int velY) {
		for(Corner corner : corners) {
			corner.moveCorner(velX,velY);
		}
		getRotationPoint().moveCorner(velX,velY);
		collisionSquare.moveSquare(velX, velY);
	}
	
	public void setVels(double velX, double velY) {
		this.setVelX(velX);
		this.setVelY(velY);
	}
	
	
	//Returns farthest distance from rp to this objects corner
	public double getFurthestDistance() {
		double furthest = 0;
		for(Corner c : getCorners()) {
			if(c.getPointDistance(this.getRotationPoint()) > furthest) {
				furthest = c.getPointDistance(this.getRotationPoint());
			}
		}
		
		return furthest;
	}
	
	public void makeSquare(double longestDirection) {
		longestDirection++;
		collisionSquare = new Square(new Corner(new double[] {this.getRotationPoint().getX()-longestDirection,this.getRotationPoint().getY()-longestDirection}),longestDirection*2);
	}
	
	public void rotateObAroundDifferentCorner(Corner attachmentRP, double angle, Corner getRotationPoint) {
		for(Corner c : getCorners()) {
			c.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
		}
		angleRotated += angle;
	}
	
	public static double generateNumInRange(double[] range) {
		return Math.random()*(range[1] - range[0])+range[0];
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

	public Corner getRotationPoint() {
		return rotationPoint;
	}

	public void setRotationPoint(double[] rotationPoint2) {
		this.rotationPoint = new Corner(new double[] {rotationPoint2[0] ,rotationPoint2[1]}, new double[] {0,0}) ;
	}
	public void setRotationPoint(Corner rotationPoint2) {
		this.rotationPoint = rotationPoint2 ;
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
	
	public void initialRAngleSet(double d) {
		this.rotationAngle = d*Game.tickMultiply;

	}
	
	public Corner[] getCorners() {
		return corners;
		
	}
	
	public void makePositiveRotation() {
		setRotationAngle(Math.abs(getRotationAngle()));
	}
	
	public void makeNegativeRotation() {
		setRotationAngle(-Math.abs(getRotationAngle()));
	}


	public void render(Graphics g) {
//		getCollisionSquare().render(g);
		for(int i = 0;i<corners.length;i++) {
			if(i<corners.length-1) {
				g.drawLine((int) Math.round(corners[i].getX()*Game.camera.toMultiply() + Game.camera.toAddX()),(int) Math.round(corners[i].getY()*Game.camera.toMultiply() + Game.camera.toAddY()),(int) Math.round(corners[i+1].getX()*Game.camera.toMultiply() + Game.camera.toAddX()),(int) Math.round(corners[i+1].getY()*Game.camera.toMultiply() + Game.camera.toAddY()));
			}
			else {
				g.drawLine((int) Math.round(corners[i].getX()*Game.camera.toMultiply() + Game.camera.toAddX()),(int) Math.round(corners[i].getY()*Game.camera.toMultiply() + Game.camera.toAddY()),(int) Math.round(corners[0].getX()*Game.camera.toMultiply() + Game.camera.toAddX()),(int) Math.round(corners[0].getY()*Game.camera.toMultiply() + Game.camera.toAddY()));
			}
		}
	}
	
	public double getReloadLenght() {
		return 0;
	}
	public double getReloadTimer() {
		return 0;
	}
	
	
	
	public Square getCollisionSquare() {
		return collisionSquare;
	}


	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public double getRotatedAngle() {
		return angleRotated;
	}
	
	public void fixRotatedAngle() {
		if(angleRotated > 360) {
			angleRotated -= 360;
		}else if(angleRotated < -360) {
			angleRotated -= -360;
		}
	}
	

}
