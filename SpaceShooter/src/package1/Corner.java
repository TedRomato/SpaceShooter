package package1;

public class Corner {
	private double x,y;
	private double currentAngle;
	private int qadrant;
	private double distance;
	
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
	
	public boolean checkIfUnder(double a, double b) {
		if (a*getX()+b < getY()) {
			return true;
		} else {
			return false;
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
	
	public double getXYRatio(double[] ds) {
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
		

		if(x == 0 && y == 0) {
			if(this.y != ds[1]) {
				return 0;
			}else {
				return Double.POSITIVE_INFINITY;
			}
		}
		else {
			if(this.y == 0) {
				return Double.POSITIVE_INFINITY;
			} if(this.x == 0) {
				return 0;
			}
		}
		
		return x/y;
	
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
	
	private void updateQadrants(double rotationPoint[]) {

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
	
	private void getNewCoords(double[] ds) {

		if (qadrant == 1) {
			y = ds[1] - countSinusPoint();
			x = ds[0] + (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 2) {
			x = ds[0] + countSinusPoint();
			y = ds[1] + (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 3) {
			y = ds[1] + countSinusPoint();
			x = ds[0] -  (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 4) {
			x = ds[0] - countSinusPoint();
			y = ds[1] -  (Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}
			
		
	}
	
	
	
	
	public void rotateCorner(double[] ds, double rotationAngle) {
		currentAngle += rotationAngle;
		updateQadrants(ds);
		getNewCoords(ds);
	
	}
	
	private boolean checkIfYIsSmaller(double a, double b) {
		if(a*getX() + b > y) {
			return true;
		}else {
			return false;
		}
	}
	
	public void moveCorner(double velX, double velY) {
		x += velX;
		y += velY;
		
	}
	private double getPointDistance(double[] rotationPoint) {
		double x = rotationPoint[0]-this.x;
		double y = rotationPoint[1]-this.y;
		return Math.sqrt(x*x + y*y);
		
	}
	
	public void rotateAroundDifferentRP(double[] newRP, double angle, double[] objectRP) {
		//Dodelat
		Corner temp = new Corner(new double[] {getX(),getY()}, newRP);
		temp.rotateCorner(newRP, angle);
		setX(temp.getX());
		setY(temp.getY());
		Corner temp2 = new Corner(new double[] {temp.getX(),temp.getY()}, objectRP);
		currentAngle = temp2.getAngle(objectRP);
		distance = getPointDistance(objectRP);
		
		
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
	
	
	
	
	//IMPORTANT

	public Corner(Corner c, double[] rp) {
		this.x = c.getX();
		this.y = c.getY();
		this.currentAngle = c.getAngle(rp);
		this.qadrant = c.getQadrant();
		this.distance= c.getPointDistance(rp);
	}
	
	
	public void updateNoRotation(double[] rp) {
		qadrant = getQadrant(rp);
		currentAngle = getAngle(rp);
		
	}
	
	public void turnAround(char c, double[] rp) {
		switch(c){
		case 'y':
			this.setY(moveToOtherSide(this.getY(), rp[1]));
			break;
		
		case 'x':
			this.setX(moveToOtherSide(this.getX(), rp[0]));
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
