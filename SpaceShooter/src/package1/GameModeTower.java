package package1;

import java.awt.event.KeyEvent;

import javax.swing.JLabel;

public class GameModeTower extends Game {
	private LivingObject Tower;
	private HuntingMine hm;
	private Mothership mp;
	private SpaceCanon sca;
	private SpaceCruiser scr;
	private JLabel waveDisplay;
	private int AIcount = 90;
	private int wave = 1;
	private int waveCount = 0;
	private int PowerLevel = 0;
	private int[] PowerLevelAr = new int[] {1,2,4,6};
	private int rnd;
	private boolean AIneeded = true, waveEnd = false;
	
	public GameModeTower(int sw, int sh) {
		super(sw, sh);
		Corner LeftTop = new Corner(new double[] {currentScreenWidth/2-50,currentScreenHeight/2-50}, new double[] {currentScreenWidth/2,currentScreenHeight/2});
		Corner LeftBot = new Corner(new double[] {currentScreenWidth/2-50,currentScreenHeight/2+50}, new double[] {currentScreenWidth/2,currentScreenHeight/2});
		Corner RightBot = new Corner(new double[] {currentScreenWidth/2+50,currentScreenHeight/2+50}, new double[] {currentScreenWidth/2,currentScreenHeight/2});
		Corner RightTop = new Corner(new double[] {currentScreenWidth/2+50,currentScreenHeight/2-50}, new double[] {currentScreenWidth/2,currentScreenHeight/2});
		Tower = new LivingObject(new Corner[] {LeftTop,LeftBot,RightBot,RightTop},new double[] {currentScreenWidth/2,currentScreenHeight/2},0,new Corner(new double[] {currentScreenWidth/2,currentScreenHeight/2}, new double[] {currentScreenWidth/2,currentScreenHeight/2}));
		Tower.setHP(50);
		addObToGame(Tower, new int[] {1,3,4,5,6,7,8,9});
		waveDisplay = new JLabel("Wave: " + wave);
		waveDisplay.setBounds(0+50, currentScreenWidth/2-150, 150, 50);
		add(waveDisplay);
	}
	public void tick() {
		super.tick();
		handleWaves();
		nextWave();
		updateDisplay();
	}
	public void handleWaves() {
		if(AIneeded && AIcount == 90) {	
			rnd = (int) (Math.random() * ((3-0)+1)) + 0;
			if(PowerLevelAr[rnd] + PowerLevel> wave) {	
				return;
			}
			if(PowerLevel + PowerLevelAr[rnd] < wave) {
				spawnAI(rnd);
				PowerLevel += PowerLevelAr[rnd];
			}
			if(PowerLevel + PowerLevelAr[rnd] == wave && AIneeded|| PowerLevel == wave && AIneeded) {
				spawnAI(rnd);
				PowerLevel = wave;
				AIneeded = false;
				waveEnd = true;
			}
			waveCount = wave;
			AIcount= 0;
		}
		else {
			AIcount++;
		}
	}
	public void updateDisplay() { 
		waveDisplay.setText("Wave: " + wave);
	}
	public void nextWave() {
		if(ais.length == 0 && wave != waveCount+1 && waveEnd) {
			wave++;
			AIneeded = true;
			System.out.println("W: " + wave);
			PowerLevel=0;
			AIcount=90;
			waveEnd = false;
		}
	}
	public void spawnAI(int PL) {
		switch(PL){
			case 0 : hm = hm.makeNewHuntingMine((int) (Math.random() * ((1000-1)+1)) + 1,(int) (Math.random() * ((1000-1)+1)) + 1); addObToGame(hm, new int[] {7,9}); 
			break;
			case 1 : sca = sca.makeNewSpaceCanon((int) (Math.random() * ((1000-1)+1)) + 1,(int) (Math.random() * ((1000-1)+1)) + 1); addObToGame(sca, new int[] {7,9}); 
			break;
			case 2 : mp = mp.makeNewMothership((int) (Math.random() * ((1000-1)+1)) + 1,(int) (Math.random() * ((1000-1)+1)) + 1); addObToGame(mp, new int[] {7}); 
			break;
			case 3 : scr = scr.makeNewSpaceCruiser((int) (Math.random() * ((1000-1)+1)) + 1,(int) (Math.random() * ((1000-1)+1)) + 1); addObToGame(scr, new int[] {7,9}); 
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
