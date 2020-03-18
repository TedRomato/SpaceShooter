package package1;

import java.awt.Graphics;

public class Square {
	Corner tlCorner;
	double side;
	
	public Square(Corner tl,double side) {
		this.tlCorner = tl;
		this.side = side;
	}
	
	
	public boolean squareCollision(Square otherSquare) {
		if(isInOrAround(this.getTl().getY(), this.getTl().getY()+this.getSide(),otherSquare.getTl().getY(), otherSquare.getTl().getY()+otherSquare.getSide())) {
			if(isInOrAround(this.getTl().getX(), this.getTl().getX()+this.getSide(),otherSquare.getTl().getX(), otherSquare.getTl().getX()+otherSquare.getSide())) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public boolean isInOrAround(double pS,double pB, double dS, double dB) {
		if(dS > pB || dB < pS) {
			return false;
		}else {
			return true;
		}
	}
	
	public void moveSquare(double velX, double velY) {
		tlCorner.moveCorner(velX, velY);
	}
	
	public Corner getTl(){
		return tlCorner;
	}
	
	public double getSide() {
		return side;
	}
	
	public void render(Graphics g) {
		g.drawRect((int) tlCorner.getX(),(int) tlCorner.getY(), (int)side,(int) side);
	}
	
	public void printSquareComps() {
		System.out.println("square X " + tlCorner.getX() + " square Y " + tlCorner.getY() + " side " + side);
	}
}
