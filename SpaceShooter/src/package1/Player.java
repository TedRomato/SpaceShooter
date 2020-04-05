package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player extends LivingObject implements KeyListener{
	
	char moveChar = 'w', turnLeftChar = 'a', turnRightChar = 'd', shootChar = ' ';
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
		if(e.getKeyChar() == shootChar) {
			setShootForInteractiveAtts(true);
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
		if(e.getKeyChar() == shootChar) {
			setShootForInteractiveAtts(false);
		}

	}

	
	public static Player makeNewPlayer(double[] rp) {
		Player p;
		ObjectAttachment attachment;
		MagazineAttachment canon;
//		ObjectAttachment straightLine;
		
		Corner peakA = new Corner(new double[] {rp[0],rp[1]-75}, rp);
	    Corner rightCornerA = new Corner(new double[] {rp[0] - 10, rp[1]-25}, rp);
	    Corner leftCornerA = new Corner(new double[] {rp[0] + 10,  rp[1]-25}, rp);
	    
	    attachment = new ObjectAttachment(new Corner[] {peakA, rightCornerA, leftCornerA}, rp,new double[] {rp[0],rp[1]},-5);

//	    straightLine = new ObjectAttachment(new Corner[] {new Corner(new double[] {rp[0] ,rp[1] + 25}, rp), new Corner(new double[] {rp[0] ,rp[1] + 350}, rp),}, rp,new double[] {rp[0],rp[1]-25},-5);
	    Corner tl = new Corner(new double[] {rp[0] - 3 ,rp[1] + 23}, rp);
	    Corner tr =	new Corner(new double[] {rp[0] + 3,rp[1] + 23}, rp);
	    Corner br = new Corner(new double[] {rp[0] + 3,rp[1] + 40}, rp);
	    Corner bl = new Corner(new double[] {rp[0] - 3,rp[1] + 40}, rp);
		Corner wp = new Corner(new double[] {rp[0] ,rp[1] + 40}, rp);
	    
	    canon = new MagazineAttachment(new Corner[] {tl,tr,br,bl}, new Corner(rp) , new double[] {rp[0], rp[1]}, 0, wp, 0,0);
	    canon.setMagazineParameters(5, 60);
	    
	    Corner peak = new Corner(new double[] {rp[0] ,rp[1] + 25}, rp);
	    Corner rightCorner = new Corner(new double[] {rp[0] - 25, rp[1] - 25}, rp);
	    Corner leftCorner = new Corner(new double[] {rp[0] + 25, rp[1] - 25}, rp);
	    
	   
	    p = new Player(new Corner[] {peak, rightCorner, leftCorner},rp, 1, new Corner(new double[] {rp[0],rp[1]+25}, rp));
	    p.addAttachment(attachment);	    
	    p.setHP(50);
	    p.setReflectedSpeed(6);
	    p.addAttachment(canon);
//	    p.addAttachment(straightLine);

	    
	    return p;
	}
		
	
}
