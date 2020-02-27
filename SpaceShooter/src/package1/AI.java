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

	public AI(Corner[] corners, double[] rotationPoint, double rotationAngle, Corner md,Corner goalDestination) {
		super(corners, rotationPoint, rotationAngle, md);
		this.goalDestination = goalDestination;
		this.goalDestination.setToNewRP(rotationPoint);
		setForward(true);
		setHP(100);
		
	}
	
	public static AI makeNewAI(double x, double y) {
		//ai
		Corner peakAI = new Corner(new double[] {x,y + 25}, new double[] {x ,y});
	    Corner rightCornerAI = new Corner(new double[] {x-25,y-25}, new double[] {x ,y});
	    Corner leftCornerAI = new Corner(new double[] {x+25,y-25}, new double[] {x ,y});
	    Corner goalCorner = new Corner(new double[] {1000,800}, new double[] {x ,y});
	    //Hmatove vousky
	    Corner base1 = new Corner(new double[] {x,y + 25}, new double[] {x ,y});
	    Corner base2 = new Corner(new double[] {x-55,y-35}, new double[] {x ,y});
	    Corner base3 = new Corner(new double[] {x+55,y-35}, new double[] {x ,y});
	    Corner basePeak = new Corner(new double[] {x,y+125}, new double[] {x ,y}); 
	    Corner rightP = new Corner(new double[] {x+40,y+125}, new double[] {x ,y});
	    Corner leftP = new Corner(new double[] {x-40,y+125}, new double[] {x ,y});
	    //dl
	    DetectionLine mdl = new DetectionLine(base1, basePeak, new double[] {x ,y}, 0.5);
	    DetectionLine ldl = new DetectionLine(base2, leftP, new double[] {x ,y}, 0.5);
	    DetectionLine rdl = new DetectionLine(base3, rightP, new double[] {x ,y}, 0.5);
	    AI ai = new AI(new Corner[] {peakAI, rightCornerAI, leftCornerAI}, new double[] {x,y}, 0.5, new Corner(new double[] {x,y+25}, new double[] {x,y}), goalCorner);
	    ai.makeDetection(mdl, new DetectionLine[] {rdl}, new DetectionLine[] {ldl});
	    ai.setMaxSpeed(0.8);
	    return ai;
	}
	
	//Guides AI to goal destination, if about to crash gives priority to avoiding collision 
	
	public void updateAI(Player p, GameObject[] gos) {
		
		for(GameObject go : gos) {
			if(go != this) {
			checkAndHandleTrack(go);
			}
			if(collisionDanger) {
				break;
			}
		}
		if(collisionDanger == false) {
			setGoalToPlayer(p);
		}
		
		updateRotationToGoal();
		updateForward();
		
		
		
	}
	
	private void checkAndHandleTrack(GameObject go) {
		boolean rightCollision = false;
		boolean leftCollision = false;
		for(int i = 0; i < leftDetectionLines.length && i < rightDetectionLines.length; i++) {
			if(leftDetectionLines[i].checkCollision(go)) {
				leftCollision = true;
			}
			if(rightDetectionLines[i].checkCollision(go)) {
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
		
		else if(mainDetectionLine.checkCollision(go)) {
			setGoalToCorner(rightDetectionLines[0].getForwardCorner());	
			collisionDanger = true;
		}
		else {
			collisionDanger = false;
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
	
	private void setGoalToPlayer(Player p) {
		goalDestination.setX(p.getRotationPoint().getX());
		goalDestination.setY(p.getRotationPoint().getY());
	}
	
	private void setGoalToCorner(Corner c) {
		goalDestination.setX(c.getX());
		goalDestination.setY(c.getY());
	}
	
	
	private void updateForward() {
		if(getReflected() == false) {
			setForward(true);
		}
	}
	
	private void updateRotationToGoal() {
		this.goalDestination.setToNewRP(getRotationPoint());
		double movePointAngle = getMP().getAngle(getRotationPoint());
		double goalDestinationAngle = goalDestination.getAngle(getRotationPoint());
		double[] angleDifference = getMP().getAngleDifferencRL(movePointAngle, goalDestinationAngle);
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
	
	private void rotateDls() {
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
	
	public void render(Graphics g) {
		for(int i = 0;i<getCorners().length;i++) {
			if(i<getCorners().length-1) {
				g.drawLine((int) Math.round(getCorners()[i].getX()),(int) Math.round(getCorners()[i].getY()),(int) Math.round(getCorners()[i+1].getX()),(int) Math.round(getCorners()[i+1].getY()));
			}
			else {
				g.drawLine((int) Math.round(getCorners()[i].getX()),(int) Math.round(getCorners()[i].getY()),(int) Math.round(getCorners()[0].getX()),(int) Math.round(getCorners()[0].getY()));
			}
		}
		
		g.setColor(Color.red);
		g.fillRect((int) Math.round(moveDirection.getX()),(int) Math.round(moveDirection.getY()), 10, 10);
		g.setColor(Color.darkGray);
		g.fillRect((int) Math.round(getRotationPoint().getX()),(int) Math.round(getRotationPoint().getY()), 9, 9);
		g.setColor(Color.BLUE);
		g.fillRect((int) Math.round(getMP().getX()),(int) Math.round(getMP().getY()), 8, 8);
		g.setColor(Color.YELLOW);
		g.fillRect((int) Math.round(goalDestination.getX()),(int) Math.round(goalDestination.getY()), 9, 9);
		g.setColor(Color.black);

		for(DetectionLine dl : rightDetectionLines) {
			dl.render(g);
		}
		for(DetectionLine dl : leftDetectionLines) {
			dl.render(g);
		}
		mainDetectionLine.render(g);

	}

}
