package package1;

import java.awt.Graphics;

//c1 is base ---> connected to ai, and c2 is infront of the ai

public class DetectionLine extends GameObject{
		public DetectionLine(Corner c1, Corner c2, Corner rp, double rotationAngle) {
			super(new Corner[] {c1,c2}, rp, rotationAngle);
		}
		public DetectionLine(Corner c1, Corner c2, double[] rp, double rotationAngle) {
			super(new Corner[] {c1,c2}, rp, rotationAngle);
		}
				
	
	
	
	public void renderDL(Graphics g) {
		g.drawLine((int) Math.round(getCorners()[0].getX()), (int) Math.round(getCorners()[0].getY()), (int) Math.round(getCorners()[1].getX()), (int) Math.round(getCorners()[1].getY()));
	}
	
	public Corner getBase() {
		return getCorners()[0];
	}
	
	public Corner getForwardCorner() {
		return getCorners()[1];
	}

}