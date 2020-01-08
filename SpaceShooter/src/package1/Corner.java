package package1;

public class Corner {
	private int x,y;
	private double currentAngle;
	private int qadrant;
	private int distance;
	
	public Corner(int[] coordinates, int[] rotationPoint) {
		x = coordinates[0];
		y = coordinates[1];
		if(coordinates.length!=2) {
			System.out.println("Corner wrong coords in constructor");
		}
		distance = getPointDistance(rotationPoint);
		qadrant = getQadrant(rotationPoint);
		currentAngle = getAngle(rotationPoint);
		
	}
	
	private int getQadrant(int[] rotationPoint) {
		if(x > rotationPoint[0]) {
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
	
	public int getXYRatio(int[] rotationPoint) {
		int x = 0;
		int y = 0;
		if (qadrant == 1) {
			x = countSinusPoint();
			y =  - (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 3) {
			x =  - countSinusPoint();
			y = (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 2) {
			y =  countSinusPoint();
			x =  (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 4) {
			y = - countSinusPoint();
			x = - (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}
		if(Math.abs(y) < 1) {
			return 1;
		}
		return x/y;
	
	}
	
	private double getAngle(int[] rotationPoint) {
		if(qadrant == 1 || qadrant == 3) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint[0]-x)/distance)) + (qadrant-1)*90;
		}else if(qadrant == 2 || qadrant == 4) {
			return Math.toDegrees(Math.asin(Math.abs(rotationPoint[1]-y)/distance)) + (qadrant-1)*90;
		} else {
			System.out.println("getAngle chyba");
			return 0;
		}
		
	}
	
	private void updateQadrants() {
		if(currentAngle - (qadrant-1) * 90 >= 0) {
			qadrant++;
		}
		if(qadrant > 4) {
			qadrant = 1;
			currentAngle -= 360;
		}
	}
	
	private int countSinusPoint() {
		return   (int) Math.round((distance * Math.sin(currentAngle - (qadrant-1)*90)));
	}
	
	private void getNewCoords(int[] rotationPoint) {
	
		if (qadrant == 1) {
			x = rotationPoint[0] + countSinusPoint();
			y = rotationPoint[0] - (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 3) {
			x = rotationPoint[0] - countSinusPoint();
			y = rotationPoint[0] + (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 2) {
			y = rotationPoint[0] + countSinusPoint();
			x = rotationPoint[0] + (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}else if (qadrant == 4) {
			y = rotationPoint[0] - countSinusPoint();
			x = rotationPoint[0] - (int) Math.round(Math.sqrt(distance*distance - countSinusPoint()*countSinusPoint()));
		}
			
		
	}
	
	
	public void rotateCorner(int[] rotationPoint, int rotationAngle) {
		currentAngle += rotationAngle;
		updateQadrants();
		getNewCoords(rotationPoint);
		
	
	}
	
	public void moveCorner(int velX, int velY) {
		x += velX;
		y += velY;
		
	}
	private int getPointDistance(int[] rotationPoint) {
		int x = rotationPoint[0]-this.x;
		int y = rotationPoint[1]-this.y;
		return (int) Math.round(Math.sqrt(x*x + y*y));
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
