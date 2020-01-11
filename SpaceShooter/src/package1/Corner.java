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
		if(y == 0) {
			return Double.POSITIVE_INFINITY;
		} if(x == 0) {
			return 0;
		}
		
		return x/y;
	
	}
	
	private double getAngle(double[] rotationPoint) {

		if(qadrant == 1 || qadrant == 3) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint[0]-x)/distance)) + (qadrant-1)*90;
		}else if(qadrant == 2 || qadrant == 4) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint[1]-y)/distance)) + (qadrant-1)*90;
		} else {
			if(x == rotationPoint[0]) {
				if(y > rotationPoint[0]) {
					return 0;
				} else {
					return 180;
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
		if(currentAngle > 360) {
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
	
	public void moveCorner(double velX, double velY) {
		x += velX;
		y += velY;
		
	}
	private double getPointDistance(double[] rotationPoint) {
		double x = rotationPoint[0]-this.x;
		double y = rotationPoint[1]-this.y;
		return Math.sqrt(x*x + y*y);
		
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
	
}
