package package1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player extends LivingObject implements KeyListener{
	
	Corner TopLeft, TopRight, BotLeft, BotRight, md;
	char moveChar = 'w', turnLeftChar = 'a', turnRightChar = 'd', shootChar = ' ';
	public Player(Corner[] corners, double[] rotationPoint, double d, Corner md) {
		super(corners, rotationPoint, d, md);
		
	}
	public Missile shoot() {
		TopLeft = new Corner(new double[] {getSP().getX(),getSP().getY()}, new double[] {getSP().getX()+5,getSP().getY()+5});
		BotLeft = new Corner(new double[] {getSP().getX(),getSP().getY()+10}, new double[] {getSP().getX()+5,getSP().getY()+5});
		BotRight = new Corner(new double[] {getSP().getX()+10,getSP().getY()+10}, new double[] {getSP().getX()+5,getSP().getY()+5});
		TopRight = new Corner(new double[] {getSP().getX()+10,getSP().getY()}, new double[] {getSP().getX()+5,getSP().getY()+5});
		md = new Corner(new double[] {getSD().getX(), getSD().getY()}, new double[] {getSP().getX()+5,getSP().getY()+5});
		if(getShoot()) {
		Missile m = new Missile(new Corner[] {TopLeft, BotLeft, BotRight, TopRight}, new double[] {getSP().getX()+5,getSP().getY()+5}, 0,md,1.6);
		m.getNewRatios();
		m.setNewVels();
		return m;
		
		}
		else return null;
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
			setShoot(true);
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
			setShoot(false);
		}

	}
		
	
}
