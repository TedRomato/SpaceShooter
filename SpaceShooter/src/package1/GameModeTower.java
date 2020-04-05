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
	private Corner spawnCorner;
	private int AIcount = 90;
	private int wave = 1;
	private int waveCount = 0;
	private int PowerLevel = 0;
	private int[] PowerLevelAr = new int[] {1,2,4,6};
	private int rnd;
	private boolean AIneeded = true, waveEnd = false;
	
	public GameModeTower(int sw, int sh) {
		super(sw, sh);
		Corner LeftTop = new Corner(new double[] {mainWidth/2-50,mainHeight/2-50}, new double[] {mainWidth/2,mainHeight/2});
		Corner LeftBot = new Corner(new double[] {mainWidth/2-50,mainHeight/2+50}, new double[] {mainWidth/2,mainHeight/2});
		Corner RightBot = new Corner(new double[] {mainWidth/2+50,mainHeight/2+50}, new double[] {mainWidth/2,mainHeight/2});
		Corner RightTop = new Corner(new double[] {mainWidth/2+50,mainHeight/2-50}, new double[] {mainWidth/2,mainHeight/2});
		Tower = new LivingObject(new Corner[] {LeftTop,LeftBot,RightBot,RightTop},new double[] {mainWidth/2,mainHeight/2},0,new Corner(new double[] {mainWidth/2,mainHeight/2}, new double[] {mainWidth/2,mainHeight/2}));
		Tower.setHP(50);
		addObToGame(Tower, new int[] {1,3,4,5,6,7,8,9});
		waveDisplay = new JLabel("Wave: " + wave);
		waveDisplay.setBounds(0+50, mainWidth/2-150, 150, 50);
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
			PowerLevel=0;
			AIcount=90;
			waveEnd = false;
		}
	}
	public void spawnAI(int PL) {
		spawnCorner = GameObject.generateCornerOutsideMapInRange(mainWidth, mainHeight, new int[] {70,100});
		switch(PL){

			case 0 : hm = HuntingMine.makeNewHuntingMine(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(hm, new int[] {4,7,9,10}); 
			break;

			case 1 : sca = SpaceCanon.makeNewSpaceCanon(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(sca, new int[] {4,7,9,10}); 
			break;

			case 2 : mp = Mothership.makeNewMothership(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(mp, new int[] {4,7,10}); 
			break;

			case 3 : scr = SpaceCruiser.makeNewSpaceCruiser(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(scr, new int[] {4,7,9,10}); 
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
