package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player extends LivingObject implements KeyListener{
	
	char moveChar = 'w', turnLeftChar = 'a', turnRightChar = 'd';
	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		setReflectedLenght(35);
		setRotationAngle(3.9);
		setAcceleration(getMaxSpeed() / 45);
		
	}
	
	public Player(Corner[] corners, Corner rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		setReflectedLenght(35);
		setRotationAngle(3.9);
		setAcceleration(getMaxSpeed() / 45);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyChar() == moveChar) {
			if(getReflected() == false) {
				setForward(true);
			}
			
			
		}
		if(e.getKeyChar() == turnLeftChar) {
			setLeft(true);
			makeNegativeRotation();
		}
		if(e.getKeyChar() == turnRightChar) {
			setRight(true);
			makePositiveRotation();
			
		}
	
	}



	@Override
	public void keyTyped(KeyEvent e) {

		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyChar() == moveChar) {
			setForward(false);
			
		}
		if(e.getKeyChar() == turnLeftChar) {
			setLeft(false);
		}
		if(e.getKeyChar() == turnRightChar) {
			setRight(false);
			
		}

	}
	
	public static Player makeNewPlayer(Corner rp) {
		Player p;
		ObjectAttachment attachment;
		
		Corner peakA = new Corner(new double[] {rp.getX(),rp.getY()-75}, rp);
	    Corner rightCornerA = new Corner(new double[] { - 10, rp.getY()-25}, rp);
	    Corner leftCornerA = new Corner(new double[] {rp.getX() + 10,  rp.getY()-25}, rp);
	    
	    attachment = new ObjectAttachment(new Corner[] {peakA, rightCornerA, leftCornerA}, rp,new double[] {rp.getX(),rp.getY()-25},-5);
		
	    Corner peak = new Corner(new double[] {rp.getX() ,rp.getY() + 25}, rp);
	    Corner rightCorner = new Corner(new double[] {rp.getX() - 25, rp.getY() - 25}, rp);
	    Corner leftCorner = new Corner(new double[] {rp.getX() + 25, rp.getY() - 25}, rp);
	    
	    p = new Player(new Corner[] {peak, rightCorner, leftCorner},rp,6, new Corner(new double[] {rp.getX(),rp.getY()+25}, rp));
	    p.addAttachment(attachment);
	    p.setHP(5);
	    
	    return p;
	}
	
	public static Player makeNewPlayer(double[] rp) {
		Player p;
		ObjectAttachment attachment;
		
		Corner peakA = new Corner(new double[] {rp[0],rp[1]-75}, rp);
	    Corner rightCornerA = new Corner(new double[] {rp[0] - 10, rp[1]-25}, rp);
	    Corner leftCornerA = new Corner(new double[] {rp[0] + 10,  rp[1]-25}, rp);
	    
	    attachment = new ObjectAttachment(new Corner[] {peakA, rightCornerA, leftCornerA}, rp,new double[] {rp[0],rp[1]-25},-5);
		
	    Corner peak = new Corner(new double[] {rp[0] ,rp[1] + 25}, rp);
	    Corner rightCorner = new Corner(new double[] {rp[0] - 25, rp[1] - 25}, rp);
	    Corner leftCorner = new Corner(new double[] {rp[0] + 25, rp[1] - 25}, rp);
	    
	    p = new Player(new Corner[] {peak, rightCorner, leftCorner},rp, 1, new Corner(new double[] {rp[0],rp[1]+25}, rp));
	    p.addAttachment(attachment);	    
	    p.setHP(5);

	    
	    return p;
	}
		
	
}
