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
		// TODO Auto-generated constructor stub
	}
	
	//Guides AI to goal destination, if about to crash gives priority to avoiding collision
	
	public void updateAI(Player p) {
		if(collisionDanger == false) {
			setGoalToPlayer(p);
			updateForward();
			updateRotation();
		}
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
			setRotationRight();
			collisionDanger = true;
		}
		else if(rightCollision == true) {
			setRotationLeft();
			collisionDanger = true;
		}
		
		else if(mainDetectionLine.checkCollision(go)) {
			setRotationRight();
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
	
	
	private void updateForward() {
		if(getReflected() == false) {
			setForward(true);
		}
	}
	
	private void updateRotation() {
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
	
	public void makeDetectionLines() {
		
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
		g.setColor(Color.BLACK);
		g.fillRect((int) Math.round(goalDestination.getX()),(int) Math.round(goalDestination.getY()), 9, 9);

	}

}
