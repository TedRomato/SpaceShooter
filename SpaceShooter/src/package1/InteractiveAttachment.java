package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;


//specialni render obrazku pro interactive attachmenty, ktery rotujou s parentem attRangle + parentRangle
public class InteractiveAttachment extends ObjectAttachment{

	Corner wayPoint;
	Corner shootDirection, shootPoint;
	double attRotationAngle;
	boolean shoot;
	double[] rotationSegment = new double[] {};
	int dmg = 1;
	GameObject shotTrajectory;
	Corner aimCorner;
	double inaccuracy = 0;
	double inaccuracyX = 0;
	double inaccuracyY = 0;
	double missileSpeed = 12;
	
//	double maxShotAngleDifference = 20;

	
	//Always make inter. attachments facing down, or count custom shootDir 

	public InteractiveAttachment(Corner[] corners, Corner rp, double[] attachmentRP, double rotationAngle, Corner wayPoint, double lenght, double width) {
		super(corners, rp, attachmentRP, rotationAngle);
		// TODO Auto-generated constructor stub
		this.wayPoint = wayPoint;
		attRotationAngle = 1;
		//Posible error while moving and rotating (shooting transition update)
		shootPoint = new Corner(new double[] {wayPoint.getX(),wayPoint.getY()+20}, getRotationPoint());
		shootDirection = new Corner(new double[] {wayPoint.getX(),wayPoint.getY()+40}, getRotationPoint());
		aimCorner = new Corner(rp,getRotationPoint());
		this.reloadLenght = 10;
		this.reloadTimer = 0;
		makeShotTrajectory(lenght, width);		
	}

	public void rotateToCorner(Corner c) {
		Corner newC = new Corner(c , getAttachmentRP());
		Corner newC2 = new Corner(getWayPoint(),getAttachmentRP());
		double cAngle = newC.getAngle(getAttachmentRP());
		double wAngle = newC2.getAngle(getAttachmentRP());

		//From wangle
		double rightDifference;
		double leftDifference;
		double[] differences = Corner.getAngleDifferencRL(wAngle,cAngle);
		rightDifference = differences[0];
		leftDifference = differences[1];

		if(getRotationSegment().length==2) {

		}
		if(rightDifference > getAttachmentRotationAngle() && leftDifference > getAttachmentRotationAngle()) {
			if(rightDifference < leftDifference) {
				rotateAttachmentAroundItsCorner(getAttachmentRotationAngle());
			}
			else{
				rotateAttachmentAroundItsCorner(-getAttachmentRotationAngle());
			}
		}
		
		else {
			if(rightDifference < leftDifference){
				rotateAttachmentAroundItsCorner(rightDifference);
			}else {
				rotateAttachmentAroundItsCorner(-leftDifference);
			}
		}
		
	}

	public void rotateAttachment(double angle) {
			super.rotateAttachment(angle);
			wayPoint.rotateCorner(getRotationPoint(), angle);
			shootDirection.rotateCorner(getRotationPoint(), angle);
			shootPoint.rotateCorner(getRotationPoint(), angle);		
			shotTrajectory.rotateOb(angle);
		
	}
	
	public void rotateAttachmentAroundItsCorner(double angle) {
		if(checkIfInRotationSegment(getAttachmentAngle()+angle)) {
			super.rotateAttachmentAroundItsCorner(angle);
			wayPoint.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shootDirection.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shootPoint.rotateAroundDifferentRP(attachmentRP, angle, getRotationPoint());
			shotTrajectory.rotateObAroundDifferentCorner(attachmentRP, angle, getRotationPoint());
			}
		}	
	
	
	public void moveAttachment(double velX, double velY) {
		super.moveAttachment(velX, velY);
		wayPoint.moveCorner(velX, velY);
		shootDirection.moveCorner(velX, velY);
		shootPoint.moveCorner(velX, velY);
		shotTrajectory.setVels(velX, velY);
		shotTrajectory.moveObWithoutRP();

		}
	

	
	public boolean shouldShoot(Corner goalCorner) {
		if(getShoot() && goalCorner != null) {
			if(decideIfFire(goalCorner)) {
				return true;
			}
		}
		return false;
	}
	

	
	public boolean shouldShoot() {
		if(getShoot()) {
			return true;
		}
		else return false;
	}
	
	public void updateAimPoint(GameObject go) {
		Corner c = getNewAimCorner(go);
		if(!Double.isNaN(c.getX()) && !Double.isNaN(c.getY())) {
			setAimCorner(c);
		}
	}
	
	protected Missile shoot(GameObject[] imunes) {
		Missile m;
		m = Missile.makeNewMissile(getSP(), getDmg(), getSD(), imunes);
		m.setDmg(dmg);
		getNewInaccuracy();
		return m;
	}
	

	public Corner getNewAimCorner(GameObject go) {
		if(go instanceof MovingObject) {
			if(((MovingObject) go).getCurrentSpeed() > 0) {
				return addInaccuracy(this.getAimCornerForMovingOb((MovingObject) go));
			}
		}		
		return addInaccuracy(new Corner(go.getRotationPoint(), go.getRotationPoint()));
	}
	//TODO Improve missle speed (take it as argument)
	public Corner getAimCornerForMovingOb(MovingObject moo) {

		return getNewAimCorner(moo);
		
	}
	
	private Corner getNewAimCorner(MovingObject moo) {

		Corner temp = new Corner(this.getSP(), moo.getRotationPoint());
		double sdAngle = temp.getAngle(moo.getRotationPoint());
		double mdAngle = moo.getMoveDirection().getAngle(moo.getRotationPoint());
		double distance = this.getSP().getPointDistance(moo.getRotationPoint());
		double angleDifference = decideSmaller(Corner.getAngleDifferencRL(mdAngle, sdAngle));			//gets angle between moo move direction and moo / this.getSP distance

		double a = Math.pow(moo.getCurrentSpeed()/missileSpeed, 2) - 1;
		double b = -2*moo.getCurrentSpeed() * distance / Math.pow(missileSpeed, 2) * Math.cos(Math.toRadians(angleDifference));
		double c = Math.pow(distance / missileSpeed, 2);
		
		double[] roots = countQuadraticFunction(a,b,c);
		Corner corner;
		try {
			corner = Corner.makeCornerUsinAngle(roots[0]*moo.getCurrentSpeed(), moo.getMoveDirection().getAngle(moo.getRotationPoint()), moo.getRotationPoint());
		}catch(NullPointerException ex){
			corner = new Corner(moo.getRotationPoint(), moo.getRotationPoint());
		}
		return corner;
	}
	
	private Corner getAimCornerNew(MovingObject moo) {
		
		Corner temp = new Corner(this.getSP(), moo.getRotationPoint());
		double sdAngle = temp.getAngle(moo.getRotationPoint());
		double mdAngle =  moo.getMoveDirection().getAngle(moo.getRotationPoint());
		double distance = this.getSP().getPointDistance(moo.getRotationPoint());
		double angleDifference = Math.toRadians(decideSmaller(Corner.getAngleDifferencRL(mdAngle, sdAngle)));		
	
		double r2 = moo.getCurrentSpeed()/missileSpeed;
		double a2 = (1-Math.pow(r2, 2))/Math.pow(Math.tan(angleDifference), 2)-Math.pow(r2, 2)+1;
		double b2 = r2*r2*distance*2/Math.tan(angleDifference);
		double c2 = -r2*r2*Math.pow(distance, 2);
		
		double[] roots2 = countQuadraticFunction(a2,b2,c2);
		
		double mooTrack2 = roots2[1]/Math.sin(angleDifference);
		
		Corner corner = Corner.makeCornerUsinAngle(mooTrack2, mdAngle, moo.getRotationPoint());
		return corner;
	}
	
	public long getDiscriminant(double a, double b, double c) {
		return (long) (b*b - 4*a*c);
	}
	
	public double[] countQuadraticFunction(double a, double b, double c) {
		long discriminant = getDiscriminant(a,b,c);
		if(discriminant >= 0) {
			double num1 = countQuadraticFormula(a, b, c, discriminant, '-');
			double num2 = countQuadraticFormula(a, b, c, discriminant, '+');
			return new double[] {num1, num2};
		}
		System.out.println("discriminant < 0");
		return null;
		
	}
	
	public double countQuadraticFormula(double a, double b, double c, double discriminant, char character) {
		if(character == '-') {
			return (-b - Math.sqrt(discriminant))/(2*a);
			
		}else {
			return (-b + Math.sqrt(discriminant))/(2*a);
		}
	}
	
	private double decideSmaller(double[] ab) {
		if(ab[0] <= ab[1]) {
			return ab[0];
		}else {
			return ab[1];
		}
	}
	
	public Corner addInaccuracy(Corner c) {
		if(getInaccuracy() > 0) {	
			c.setX(c.getX() + inaccuracyX);
			c.setY(c.getY() + inaccuracyY);
			return c;
		}else {
			return c;
		}
	}
	
	private void getNewInaccuracy() {
		inaccuracyX = generateNumInRange(new double[] {-getInaccuracy(), getInaccuracy()});
		inaccuracyY = generateNumInRange(new double[] {-getInaccuracy(), getInaccuracy()});

	}

	public boolean decideIfFire(Corner goalCorner) {
		
		if(shotTrajectory.checkCollision(new GameObject(new Corner[] {goalCorner, goalCorner}, goalCorner, 0))) {

			return true;
		}else {

			return false;
		}
	}
	
	
	public void handleFriendlyFire(AI[] ais) {
		for(AI ai : ais) {
			if(ai instanceof HuntingMine == false) {
				if(ai.checkCollision(shotTrajectory)) {
					setShoot(false);
				}
			}
		}
	}
	
	public boolean checkIfInRotationSegment(double d) {
		if(rotationSegment.length != 2) {
			return true;
		}
		if(d > rotationSegment[0] && d < rotationSegment[1]) {
			return true;
		} else return false;
	}
	
	public void upgradeDMG() {
		setDmg(getDmg() + 1);
	}
	
	public int getReloadLenght() {
		return reloadLenght;
	}

	public void setReloadLenght(int reloadLenght) {
		this.reloadLenght = reloadLenght;
	}

	public int getReloadTimer() {
		return reloadTimer;
	}

	public void setReloadTimer(int reloadTimer) {
		this.reloadTimer = reloadTimer;
	}
	
	protected Corner getSP() {
		return shootPoint;
	}
	
	protected Corner getSD() {
		return shootDirection;
	}
		
	public Corner getWayPoint() {
		return wayPoint;
	}


	public void setWayPoint(Corner wayPoint) {
		this.wayPoint = wayPoint;
	}
	
	private double getAttachmentRotationAngle() {
		return attRotationAngle;
	}
	
	public void setAttRangle(double d) {
		attRotationAngle = d;
	}
	
	protected void setShoot(boolean b) {
		shoot = b;
	}
	protected boolean getShoot() {
		return shoot;
	}
	
	public void setRotationSegment(double[] s) {
		rotationSegment = s;
	}
	
	private void makeShotTrajectory(double lenght, double width) {
		Corner rp = new Corner(getRotationPoint(), getRotationPoint());
		Corner c1 = new Corner(new double[] {getSD().getX()-width/2, getSD().getY()}, getRotationPoint());
		Corner c2 = new Corner(new double[] {getSD().getX()-width, getSD().getY()+lenght}, getRotationPoint());
		Corner c3 = new Corner(new double[] {getSD().getX()+width, getSD().getY()+lenght}, getRotationPoint());
		Corner c4 = new Corner(new double[] {getSD().getX()+width/2, getSD().getY()}, getRotationPoint());
		shotTrajectory =new GameObject(new Corner[] {c1,c2,c3,c4}, getRotationPoint(),getRotationAngle());

	}
	
	public void render(Graphics g) {
//		shootDirection.renderCorner(g, 4);
//		shootPoint.renderCorner(g, 4);
//		shotTrajectory.render(g);
//		aimCorner.renderCorner(g, 10);
//		wayPoint.renderCorner(g, 10);
		super.render(g);
		
	}

	public void setAimCorner(Corner newAimCorner) {
		aimCorner = newAimCorner;
	}
	
	public void setAimCorner(double x, double y) {
		aimCorner.setX(x);
		aimCorner.setY(y);
	}
	
	public Corner getAimCorner() {
		return aimCorner;
	}

	public void setDmg(int dmg) {
		// TODO Auto-generated method stub
		this.dmg = dmg;
	}
	
	public int getDmg() {
		return dmg;
	}

	public void setInaccuracy(double ina) {
		inaccuracy = ina;
	}
	
	public double getInaccuracy() {
		return inaccuracy;
	}
	
	public double[] getRotationSegment() {
		return rotationSegment;
	}
	
	
	
	
	

}