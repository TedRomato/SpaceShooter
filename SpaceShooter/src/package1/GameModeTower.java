package package1;

import java.awt.event.KeyEvent;

public class GameModeTower extends Game {
	private LivingObject Tower;
	public GameModeTower(int sw, int sh) {
		super(sw, sh);
		Corner LeftTop = new Corner(new double[] {screenWidth/2-50,screenHeight/2-50}, new double[] {screenWidth/2,screenHeight/2});
		Corner LeftBot = new Corner(new double[] {screenWidth/2-50,screenHeight/2+50}, new double[] {screenWidth/2,screenHeight/2});
		Corner RightBot = new Corner(new double[] {screenWidth/2+50,screenHeight/2+50}, new double[] {screenWidth/2,screenHeight/2});
		Corner RightTop = new Corner(new double[] {screenWidth/2+50,screenHeight/2-50}, new double[] {screenWidth/2,screenHeight/2});
		Tower = new LivingObject(new Corner[] {LeftTop,LeftBot,RightBot,RightTop},new double[] {screenWidth/2,screenHeight/2},0,new Corner(new double[] {screenWidth/2,screenHeight/2}, new double[] {screenWidth/2,screenHeight/2}));
		Tower.setAcceleration(20);
		Tower.setHP(50);
		addObToGame(Tower, new int[] {5,6,7,8});
	}
	
public void keyTyped(KeyEvent e) {
		
	}
	public void keyPressed(KeyEvent e) {
		p.keyPressed(e);
	}
	public void keyReleased(KeyEvent e) {
		p.keyReleased(e);
	}

	
}
