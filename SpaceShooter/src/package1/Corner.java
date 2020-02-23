package package1;

public class Corner {
	
	private double x,y; //X and Y coordinate
	private double currentAngle; //currentAngle from constructor RP
	private int qadrant;  //Corners quadrant based on constructor RP
	private double distance; //Distance from its constructor RP
	
	/*Corners are a foundation object from which visible objects are made of. 
	They are used to determine tilt and borders of object sides*/
	
	//WORKING NOTE
	//Anytime working with Corners dont't forget to change distance, quadrant and currentAngle corresponding to new RP you are trying to use
	
	public Corner(double[] coordinates, double[] rotationPoint) {

		x = coordinates[0];
		y = coordinates[1];
		if(coordinates.length!=2) {
			System.out.println("Corner wrong coords in constructor");
		}
		distance = getPointDistance(rotationPoint);
		qadrant = getQadrant(rotationPoint);
		currentAngle = getAngle(rotationPoint);
	}
	
	
	//checks if this corner is under function line --> intakes a and b from : a*x+b = y
	public boolean checkIfUnder(double a, double b) {
		if (a*getX()+b < getY()) {
			return true;
		} else {
			return false;
		}
	}
	
	/*gets quadrant of corner based on rotationPonit --> quadrants go from 1 - 4 counting clockwise from top right segments
	 If the points are in between segments (XC == XRp ||  YC == YRp) -- then this method return 0 */
	 
	
	public int getQadrant(Corner rp) {
		if(x == rp.getX() || y == rp.getY()) {
			return 0;
		}
		else if(x > rp.getX()) {
			if(y > rp.getY()) {
				return  2;
			}else {
				return 1;
			}
		} else {
			if(y > rp.getY()) {
				return 3;
			}else {
				return 4;
			}	
		}
	}
	
	public int getQadrant(double[] rotationPoint) {
		if(x == rotationPoint[0] || y == rotationPoint[1]) {
			return 0;
		}
		else if(x > rotationPoint[0]) {
			if(y > rotationPoint[1]) {
				return  2;
			}else {
				return 1;
			}
		} else {
			if(y > rotationPoint[1]) {
				return 3;
			}else {
				return 4;
			}	
		}
	}
	
	
	
	/*gets xy ratio of (corner x - rp x) / (corner y - rp y) 
	If the points are in quadrant 0 in vertical line returns : 0 || in horizontal line returns : Double.POSITIVE_INFINITY*/
	
	public double getXYRatio(Corner rp) {
		double x = 0;
		double y = 0;
		if (qadrant == 1) {
			y = Math.abs(countSinusPoint());
			x = Math.abs((Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint())));
		}else if (qadrant == 2) {
			x = Math.abs(countSinusPoint());
			y = Math.abs((Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint())));
		}else if (qadrant == 3) {
			y = Math.abs(countSinusPoint());
			x = Math.abs((Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint())));
		}else if (qadrant == 4) {
			x = Math.abs(countSinusPoint());
			y = Math.abs((Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint())));
		}
		
//TADYY
		if(x == 0 && y == 0) {
			if(this.y != rp.getY()) {
				return 0;
				
			}else {
				return Double.POSITIVE_INFINITY;
			}
		}
	
		
		return x/y;
	
	}
	
	
	//gets Angle from a rotation Point
	
	public double getAngle(Corner rotationPoint) {

		if(qadrant == 1 || qadrant == 3) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint.getX()-x)/distance)) + (qadrant-1)*90;
		}else if(qadrant == 2 || qadrant == 4) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint.getY()-y)/distance)) + (qadrant-1)*90;
		} else {
			if(x == rotationPoint.getX()) {
				if(y > rotationPoint.getY()) {
					return 180;
				} else {
					return 0;
				}
			} else {
				if(x > rotationPoint.getX()) {
					return 90;
				} else {
					return 270;
				}
			
			}
			
		}
		
	}
	
	public double getAngle(double[] rotationPoint) {

		if(qadrant == 1 || qadrant == 3) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint[0]-x)/distance)) + (qadrant-1)*90;
		}else if(qadrant == 2 || qadrant == 4) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint[1]-y)/distance)) + (qadrant-1)*90;
		} else {
			if(x == rotationPoint[0]) {
				if(y > rotationPoint[1]) {
					return 180;
				} else {
					return 0;
				}
			} else {
				if(x > rotationPoint[0]) {
					return 90;
				} else {
					return 270;
				}
			
			}
			
		}
		
	}
	//Updates quadrant based on currentAngle 
	
	private void updateQadrants() {

		if (currentAngle < 360) {
			qadrant = 4;
		}
		if (currentAngle < 270) {
			qadrant = 3;
		}
		if (currentAngle < 180) {
			qadrant = 2;
		}
		if (currentAngle < 90) {
			qadrant = 1;
		}
		if(currentAngle >= 360) {
			qadrant = 1;
			currentAngle = currentAngle -360;
		}
		if(currentAngle < 0) {
			qadrant = 4;
			currentAngle = 360 + currentAngle;
		}


		
	}
	
	
	
	private double countSinusPoint() {

		return   (distance * Math.cos(Math.toRadians((currentAngle - ((qadrant-1)*90)))));
	}
	
	//Gets new coords based on currentAngle and Position of RP
	
	private void getNewCoords(Corner rp) {

		if (qadrant == 1) {
			y = rp.getY() - countSinusPoint();
			x = rp.getX() + (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 2) {
			x = rp.getX() + countSinusPoint();
			y = rp.getY() + (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 3) {
			y = rp.getY() + countSinusPoint();
			x = rp.getX() -  (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 4) {
			x = rp.getX() - countSinusPoint();
			y = rp.getY() -  (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}
			
		
	}
	
	
	//rotates Corner around set RP
	
	public void rotateCorner(Corner rp, double rotationAngle) {
		currentAngle += rotationAngle;
		updateQadrants();
		getNewCoords(rp);
	
	}
	
	
	
	public void moveCorner(double velX, double velY) {
		x += velX;
		y += velY;
		
	}
	
	//gets distance between this and any other point
	
	private double getPointDistance(Corner rp) {
		double x = rp.getX()-this.x;
		double y = rp.getY()-this.y;
		return Math.sqrt(x*x + y*y);
		
	}
	
	private double getPointDistance(double[] rotationPoint) {
		double x = rotationPoint[0]-this.x;
		double y = rotationPoint[1]-this.y;
		return Math.sqrt(x*x + y*y);
		
	}
	
	
	//returns two values --> difference of mainAngle and Secondary angle Clockwise and main and secondary angle counterClockwise 
	public double[] getAngleDifferencRL(double mainAngle, double secondaryAngle) {
		double right;
		if(mainAngle <= secondaryAngle) {
			right = secondaryAngle - mainAngle;
		} else {
			right = 360 - mainAngle + secondaryAngle;
		}
		
		double left;
		if(mainAngle >= secondaryAngle) {
			left = mainAngle - secondaryAngle;
		} else {
			left = 360 - secondaryAngle + mainAngle;	
		}
		
		return new double[] {right, left};
	}
	
	
	//rotates object around different RP and keeps the old one working
	
	public void rotateAroundDifferentRP(Corner newRP, double angle, Corner rp) {
		//Dodelat
		Corner temp = new Corner(new double[] {getX(),getY()}, new double[] {newRP.getX(), newRP.getY()});
		temp.rotateCorner(newRP, angle);
		setX(temp.getX());
		setY(temp.getY());
		Corner temp2 = new Corner(new double[] {temp.getX(),temp.getY()}, rp);
		currentAngle = temp2.getAngle(rp);
		distance = getPointDistance(rp);
		
		
	}
	

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public int getQadrant() {
		return qadrant;
		
	}
	
	
	
	
	public void setToNewRP(double[] rp) {
		Corner c = new Corner(new double[] {getX(), getY()},rp);
		this.qadrant = c.getQadrant();
		this.currentAngle = c.getAngle(rp);
		this.distance = getPointDistance(rp);
	}
	
	public void setToNewRP(Corner rp) {
		Corner c = new Corner(new double[] {getX(), getY()},rp);
		this.qadrant = c.getQadrant();
		this.currentAngle = c.getAngle(rp);
		this.distance = getPointDistance(rp);
	}

	public Corner(Corner c, double[] rp) {
		this.x = c.getX();
		this.y = c.getY();
		this.currentAngle = c.getAngle(rp);
		this.qadrant = c.getQadrant();
		this.distance= c.getPointDistance(rp);
	}
	
	
	public Corner(Corner c, Corner rotationPoint) {
		this.x = c.getX();
		this.y = c.getY();
		distance = getPointDistance(rotationPoint);
		qadrant = getQadrant(rotationPoint);
		currentAngle = getAngle(rotationPoint);
	}

	public Corner(double[] c, Corner rp) {
		this.x = c[0];
		this.y = c[1];
		distance = getPointDistance(rp);
		qadrant = getQadrant(rp);
		currentAngle = getAngle(rp);
	}

	public void updateNoRotation(Corner corner) {
		qadrant = getQadrant(corner);
		currentAngle = getAngle(corner);
		
	}
	
	//changes position of corner to other side of the RP -- x || y based on char input
	
	public void turnAround(char c, Corner corner) {
		switch(c){
		case 'y':
			this.setY(moveToOtherSide(this.getY(), corner.getY()));
			break;
		
		case 'x':
			this.setX(moveToOtherSide(this.getX(), corner.getX()));
			break;
			
			}
		}
	
	
	private double moveToOtherSide(double corner, double rp) {
		double difference = Math.abs(rp - corner);
		if(corner < rp) {
			return rp + difference;
		} else {
			return rp - difference;
		}
	}
	
}
