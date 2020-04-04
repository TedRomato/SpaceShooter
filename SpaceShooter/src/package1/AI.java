package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class AI extends LivingObject{
	Random random = new Random();
	Corner goalDestination;
	//main detectionLine  --> in direction of move point
	DetectionLine mainDetectionLine;
	//side lines --> from closest to middle to farthest from middle
	DetectionLine[] leftDetectionLines;
	DetectionLine[] rightDetectionLines;
	boolean collisionDanger = false;
	double stoppingDistance = 0;
	boolean isInStoppingDistance = false;

	public AI(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,Corner goalDestination) {
		super(corners, rotationPoint, rotationAngle, md);
		this.goalDestination = goalDestination;
		this.goalDestination.setToNewRP(rotationPoint);
		setForward(true);
		setHP(100);
		setReflectedLenght(80);
		setAcceleration(getMaxSpeed() / 50);
	}
	//inRange = minimum distance of rp - maximum distance of rp (from sides); sides = bot - y,right - x,top - y,left - x 
	
	
	public static AI makeNewAI(double x, double y) {
		//ai
		Corner peakAI = new Corner(new double[] {x,y + 25}, new double[] {x ,y});
	    Corner rightCornerAI = new Corner(new double[] {x-25,y-25}, new double[] {x ,y});
	    Corner leftCornerAI = new Corner(new double[] {x+25,y-25}, new double[] {x ,y});
	    Corner goalCorner = new Corner(new double[] {1000,800}, new double[] {x ,y});
	    //Hmatove vousky
	    Corner base1 = new Corner(new double[] {x,y + 25}, new double[] {x ,y});
	    Corner base2 = new Corner(new double[] {x-55,y-25}, new double[] {x ,y});
	    Corner base3 = new Corner(new double[] {x+55,y-25}, new double[] {x ,y});
	    Corner base4 = new Corner(new double[] {x-100,y-15}, new double[] {x ,y});
	    Corner base5 = new Corner(new double[] {x+100,y-15}, new double[] {x ,y});
	    Corner basePeak = new Corner(new double[] {x,y+205}, new double[] {x ,y}); 
	    Corner rightP = new Corner(new double[] {x+45,y+205}, new double[] {x ,y});
	    Corner leftP = new Corner(new double[] {x-45,y+205}, new double[] {x ,y});
	    Corner rightP2 = new Corner(new double[] {x+60,y+175}, new double[] {x ,y});
	    Corner leftP2 = new Corner(new double[] {x-60,y+175}, new double[] {x ,y});
	    //dl
	    DetectionLine mdl = new DetectionLine(base1, basePeak, new double[] {x ,y}, 0.5);
	    DetectionLine ldl = new DetectionLine(base2, leftP, new double[] {x ,y}, 0.5);
	    DetectionLine rdl = new DetectionLine(base3, rightP, new double[] {x ,y}, 0.5);
	    DetectionLine ldl2 = new DetectionLine(base4, leftP2, new double[] {x ,y}, 0.5);
	    DetectionLine rdl2 = new DetectionLine(base5, rightP2, new double[] {x ,y}, 0.5);
	    AI ai = new AI(new Corner[] {peakAI, rightCornerAI, leftCornerAI}, new double[] {x,y}, 0.5, new Corner(new double[] {x,y+25}, new double[] {x,y}), goalCorner);
	    ai.makeDetection(mdl, new DetectionLine[] {rdl2,rdl}, new DetectionLine[] {ldl2,ldl});

	    ai.setMaxSpeed(0);
	    ai.setRotationAngle(10);
	    ai.setHP(5);
	    return ai;
	}
	
	//Guides AI to goal destination, if about to crash gives priority to avoiding collision 
	
	public void updateAI(Player p, GameObject[] gos, AI[] ais) {
		checkAndHandleTrack(gos);
		updateIsInStoppingDistance(p);
		updateRotationToGoal();
		updateForward();
		if(collisionDanger == false) {
			setGoalToGameObject(p);
		}
		handleAllFriendlyFire(ais);
	}
	
	public void handleAllFriendlyFire(AI[] ais) {
			for(ObjectAttachment att : getAttachments()) {
			if(att instanceof InteractiveAttachment) {
				((InteractiveAttachment) att).handleFriendlyFire(ais);
			}
		}
	}
	
	protected void checkAndHandleTrack(GameObject[] gos) {
		setAllIsTriggered(false);
		setAllDLTriggeresToCurrentObs(gos);
		handleTrack();
	}
	
	
	//if collision on right -- > go left (priority raises closer to main dl)
	//if collision in the middle find the closest not triggered dl and turn its ditection
	private void handleTrack() {
		boolean rightCollision = false;
		boolean leftCollision = false;
		for(int i = 0; i < leftDetectionLines.length && i < rightDetectionLines.length; i++) {
			if(leftDetectionLines[i].getTriggered()) {
				leftCollision = true;
			}
			if(rightDetectionLines[i].getTriggered()) {
				rightCollision = true;
			}
			if(leftCollision && rightCollision) {
				rightCollision = false;
				leftCollision = false;
			}
		}

		if(leftCollision == true) {
			setGoalToCorner(rightDetectionLines[0].getForwardCorner());	
			collisionDanger = true;
		}
		else if(rightCollision == true) {
			setGoalToCorner(leftDetectionLines[0].getForwardCorner());	
			collisionDanger = true;
		}
		
		else if(mainDetectionLine.getTriggered()) {
			setGoalToCorner(rightDetectionLines[0].getForwardCorner());	
			collisionDanger = true;
		}
		else {
			collisionDanger = false;
		}
	}
	
	
	//Loops through all gos and set triggered lines to isTriggered 
	private void setAllDLTriggeresToCurrentObs(GameObject[] gos) {
		for(GameObject go : gos) {
			if(go != this && go instanceof Missile == false) {
				for(int i = 0; i < leftDetectionLines.length && i < rightDetectionLines.length; i++) {
					if(leftDetectionLines[i].getTriggered() == false) {
						leftDetectionLines[i].setTriggered(go.checkCollision(leftDetectionLines[i]));
					}
					if(rightDetectionLines[i].getTriggered() == false) {
						rightDetectionLines[i].setTriggered(go.checkCollision(rightDetectionLines[i]));
					}
				}
				if(mainDetectionLine.getTriggered() == false) {
					mainDetectionLine.setTriggered(go.checkCollision(mainDetectionLine));
				}
			}
		}
	}
	
	
	//Sets all trigger booleans of dlines to false
	private void setAllIsTriggered(boolean bTo) {
		for(int i = 0; i < leftDetectionLines.length && i < rightDetectionLines.length; i++) {
			leftDetectionLines[i].setTriggered(bTo);
			rightDetectionLines[i].setTriggered(bTo);
		}
		mainDetectionLine.setTriggered(bTo);
		
	}
	
	public void stopIfCollisionDanger() {
		if(collisionDanger == true) {
			setForward(false);
		}else {
			setForward(true);
		}
	}

	
	private void setRotationRight() {
		setRight(true);
		setLeft(false);
		makePositiveRotation();
	}
	
	private void setRotationLeft() {
		setLeft(true);
		setRight(false);
		makeNegativeRotation();
	}
	
	protected void setGoalToGameObject(GameObject p) {
		goalDestination.setX(p.getRotationPoint().getX());
		goalDestination.setY(p.getRotationPoint().getY());
	}
	
	private void setGoalToCorner(Corner c) {
		goalDestination.setX(c.getX());
		goalDestination.setY(c.getY());
	}
	
	
	protected void updateForward() {
		if(getReflected() == false) {
			setForward(true);
		}
	}
	
	protected void updateRotationToGoal() {
		this.goalDestination.setToNewRP(getRotationPoint());
		double movePointAngle = getMP().getAngle(getRotationPoint());
		double goalDestinationAngle = goalDestination.getAngle(getRotationPoint());
		double[] angleDifference = Corner.getAngleDifferencRL(movePointAngle, goalDestinationAngle);
		if(angleDifference[0] < 2 || angleDifference[1] < 2) {
			setRight(false);
			setLeft(false);
		}
		else if(angleDifference[0] < angleDifference[1]) {
			setRight(true);
			setLeft(false);
			makePositiveRotation();
			
		} else {
			setLeft(true);
			setRight(false);
			makeNegativeRotation();
			
		}
		
	}
	
	public void makeDetection(DetectionLine main, DetectionLine[] right, DetectionLine[] left) {
		mainDetectionLine = main;
		leftDetectionLines = left;
		rightDetectionLines = right;
		
	}
	
	public void rotateOb() {
		super.rotateOb();
		rotateDls();
		
	}
	
	public void rotateWithoutObject() {
		super.rotateWithoutObject();
		rotateDls();
	}
		
	public void moveOb() {
		super.moveOb();
		moveDls();
		
		
	}
	
	public boolean detectionLineExist() {
		if(mainDetectionLine == null || leftDetectionLines == null || rightDetectionLines == null) {
			return false;
		}else {
			return true;
		}
	}
	
	private void moveDls() {
		for(DetectionLine dl : rightDetectionLines) {
			dl.setVels(getVelX(), getVelY());
			dl.moveOb();
		}
		for(DetectionLine dl : leftDetectionLines) {
			dl.setVels(getVelX(), getVelY());
			dl.moveOb();
		}
		mainDetectionLine.setVels(getVelX(), getVelY());
		mainDetectionLine.moveOb();
	}
	
	protected void rotateDls() {
		for(DetectionLine dl : rightDetectionLines) {
			if(getTurnRight()) {
				dl.makePositiveRotation();
			}else if(getTurnLeft()) {
				dl.makeNegativeRotation();
			}
			dl.rotateOb();
		}
		for(DetectionLine dl : leftDetectionLines) {
			if(getTurnRight()) {
				dl.makePositiveRotation();
			}else if(getTurnLeft()) {
				dl.makeNegativeRotation();
			}
			dl.rotateOb();
		}
		if(getTurnRight()) {
			mainDetectionLine.makePositiveRotation();
		}else if(getTurnLeft()) {
			mainDetectionLine.makeNegativeRotation();
		}
		mainDetectionLine.rotateOb();
	}
	
	protected void rotateDls(double angle) {
		for(DetectionLine dl : rightDetectionLines) {
			dl.rotateOb(angle);
		}
		for(DetectionLine dl : leftDetectionLines) {
			dl.rotateOb(angle);
		}
		mainDetectionLine.rotateOb(angle);
	}
	
	
	//Behavioral methods
	
	public GameObject getClosestEnemy(GameObject[] enemys) {
		if(enemys.length <= 0 ) {
			return null;
		}
		int closestEnemy = 0;
		double closest = this.getRotationPoint().getPointDistance(enemys[0].getRotationPoint());
		for(int i = 1; i < enemys.length; i++) {
			double newDistance = this.getRotationPoint().getPointDistance(enemys[i].getRotationPoint());
			if(newDistance < closest) {
				closestEnemy = i;
				closest = newDistance;
			}
		}
		return enemys[closestEnemy];	
	}
	
	
	public void setGoalToClosestEnemy(GameObject[] enemys) {
		setGoalToCorner((getClosestEnemy(enemys)).getRotationPoint());
	}
	
	public void stopIfTooClose(GameObject enemy) {
		if(this.getRotationPoint().getPointDistance(enemy.getRotationPoint()) < stoppingDistance) {
			setForward(false);
		}else {
			setForward(true);
		}
	}
	
	public void updateIsInStoppingDistance(GameObject enemy) {
		if(this.getRotationPoint().getPointDistance(enemy.getRotationPoint()) < stoppingDistance) {
			setInStoppingDistance(true);
		}else {
			setInStoppingDistance(false);
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		
	//	g.setColor(Color.red);
	//	g.fillRect((int) Math.round(moveDirection.getX()*Game.screenRatio),(int) Math.round(moveDirection.getY()*Game.screenRatio), 10, 10);
	//	g.setColor(Color.darkGray);
	//	g.fillRect((int) Math.round(getRotationPoint().getX()*Game.screenRatio),(int) Math.round(getRotationPoint().getY()*Game.screenRatio), 9, 9);
	//	g.setColor(Color.BLUE);
	//	g.fillRect((int) Math.round(getMP().getX()*Game.screenRatio),(int) Math.round(getMP().getY()*Game.screenRatio), 8, 8);
	//	g.setColor(Color.GREEN);
	//	g.fillRect((int) Math.round(goalDestination.getX()*Game.screenRatio),(int) Math.round(goalDestination.getY()*Game.screenRatio), 10, 10);
	//	g.setColor(Color.BLACK);

		//	g.setColor(Color.black);
	// 	
	//	for(DetectionLine dl : rightDetectionLines) {
	//		dl.renderDL(g);
	//	}
	//	for(DetectionLine dl : leftDetectionLines) {
	//		dl.renderDL(g);
	//	}
	//	mainDetectionLine.renderDL(g); 
	//	 
	}
	
	public void setRotationAngle(double d) {
		for(DetectionLine dl : rightDetectionLines) {
			dl.setRotationAngle(d);
		}
		for(DetectionLine dl : leftDetectionLines) {
			dl.setRotationAngle(d);
		}
		mainDetectionLine.setRotationAngle(d);
		super.setRotationAngle(d);
	}
	
	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public Corner getGoalDestination() {
		return goalDestination;
	}

	public void setGoalDestination(Corner goalDestination) {
		this.goalDestination = goalDestination;
	}

	public DetectionLine getMainDetectionLine() {
		return mainDetectionLine;
	}

	public void setMainDetectionLine(DetectionLine mainDetectionLine) {
		this.mainDetectionLine = mainDetectionLine;
	}

	public DetectionLine[] getLeftDetectionLines() {
		return leftDetectionLines;
	}

	public void setLeftDetectionLines(DetectionLine[] leftDetectionLines) {
		this.leftDetectionLines = leftDetectionLines;
	}

	public DetectionLine[] getRightDetectionLines() {
		return rightDetectionLines;
	}

	public void setRightDetectionLines(DetectionLine[] rightDetectionLines) {
		this.rightDetectionLines = rightDetectionLines;
	}

	public boolean isCollisionDanger() {
		return collisionDanger;
	}

	public void setCollisionDanger(boolean collisionDanger) {
		this.collisionDanger = collisionDanger;
	}

	public boolean isInStoppingDistance() {
		return isInStoppingDistance;
	}

	public void setInStoppingDistance(boolean isInStoppingDistance) {
		this.isInStoppingDistance = isInStoppingDistance;
	}

	public void setStoppingDistance(double d) {
		stoppingDistance = d;
	}
	
	public double getStoppingDistance() {
		return stoppingDistance;
	}
	
	
	
	
	

}
