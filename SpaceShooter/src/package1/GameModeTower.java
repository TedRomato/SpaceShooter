package package1;

import java.awt.event.KeyEvent;

public class GameModeTower extends Game {
	private LivingObject Tower;
	private AI ai;
	private SpaceCanon sc;
	private int wave = 20;
	private int PowerLevel = 0;
	private int rnd;
	private boolean AIneeded = true;
	public GameModeTower(int sw, int sh) {
		super(sw, sh);
		Corner LeftTop = new Corner(new double[] {screenWidth/2-50,screenHeight/2-50}, new double[] {screenWidth/2,screenHeight/2});
		Corner LeftBot = new Corner(new double[] {screenWidth/2-50,screenHeight/2+50}, new double[] {screenWidth/2,screenHeight/2});
		Corner RightBot = new Corner(new double[] {screenWidth/2+50,screenHeight/2+50}, new double[] {screenWidth/2,screenHeight/2});
		Corner RightTop = new Corner(new double[] {screenWidth/2+50,screenHeight/2-50}, new double[] {screenWidth/2,screenHeight/2});
		Tower = new LivingObject(new Corner[] {LeftTop,LeftBot,RightBot,RightTop},new double[] {screenWidth/2,screenHeight/2},0,new Corner(new double[] {screenWidth/2,screenHeight/2}, new double[] {screenWidth/2,screenHeight/2}));
		Tower.setHP(50);
		addObToGame(Tower, new int[] {1,3,4,5,6,7,8});
	}
	public void tick() {
		super.tick();
		handleWaves();
	}
	public void handleWaves() {
		if(AIneeded) {	
			rnd = (int) (Math.random() * ((3-1)+1)) + 1;
			if(PowerLevel + rnd > wave) {
				spawnAI(wave - PowerLevel);
				PowerLevel = wave;
			}
			if(PowerLevel + rnd < wave) {
				spawnAI(rnd);
				PowerLevel += rnd;
			}
			if(PowerLevel + rnd == wave || PowerLevel == wave) {
				spawnAI(rnd);
				PowerLevel = wave;
				AIneeded = false;
			}
		}
	}
	public void spawnAI(int PL) {
		switch(PL){
			case 1 : ai = ai.makeNewAI((int) (Math.random() * ((1000-1)+1)) + 1,(int) (Math.random() * ((1000-1)+1)) + 1); addObToGame(ai, new int[] {7});
			break; 
			case 2 : sc = sc.makeNewSpaceCanon((int) (Math.random() * ((1000-1)+1)) + 1,(int) (Math.random() * ((1000-1)+1)) + 1); addObToGame(sc, new int[] {7});
			break;
			case 3 : System.out.println(3);
			break;
			default : 
		}
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
