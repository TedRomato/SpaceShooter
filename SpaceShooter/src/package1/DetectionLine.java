package package1;

import java.awt.Color;
import java.awt.Graphics;

//c1 is base ---> connected to ai, and c2 is infront of the ai
//gets triggered if touched 

public class DetectionLine extends GameObject{
	boolean isTriggered;
	
		public DetectionLine(Corner c1, Corner c2, Corner rp, double rotationAngle) {
			super(new Corner[] {c1,c2}, rp, rotationAngle);
		}
		public DetectionLine(Corner c1, Corner c2, double[] rp, double rotationAngle) {
			super(new Corner[] {c1,c2}, rp, rotationAngle);
		}
				
	
	
	
	public void renderDL(Graphics g) {
		
		if(getTriggered()) {
			g.setColor(Color.red);
		}
		g.drawLine((int) Math.round(getCorners()[0].getX()*Game.camera.toMultiply() + Game.camera.toAddX()), (int) Math.round(getCorners()[0].getY()*Game.camera.toMultiply() + Game.camera.toAddY()), (int) Math.round(getCorners()[1].getX()*Game.camera.toMultiply() + Game.camera.toAddX()), (int) Math.round(getCorners()[1].getY()*Game.camera.toMultiply() + Game.camera.toAddY()));
		g.setColor(Color.black);
	}
	
	public Corner getBase() {
		return getCorners()[0];
	}
	
	public Corner getForwardCorner() {
		return getCorners()[1];
	}
	public boolean getTriggered() {
		return isTriggered;
	}
	
	public void setTriggered(boolean triggered) {
		isTriggered = triggered;
	}

}