package package1;

import java.awt.event.KeyEvent;

public class GameModeTesting extends Game{
	private Player p;


	public GameModeTesting(int sw, int sh) {
		super(sw, sh, true);


	//	SpaceCanon ai12 = SpaceCanon.makeNewSpaceCanon(1000,800, getAiEnemys());
	//	SpaceCanon ai13 = SpaceCanon.makeNewSpaceCanon(1200,860,getAiEnemys());
	//	SpaceCanon ai14 = SpaceCanon.makeNewSpaceCanon(900,750,getAiEnemys());
	//	SpaceCanon ai15 = SpaceCanon.makeNewSpaceCanon(950,700,getAiEnemys());
	//	SpaceCanon ai16 = SpaceCanon.makeNewSpaceCanon(1200,800,getAiEnemys());
	//	HuntingMine ai2 = HuntingMine.makeNewHuntingMine(1000,200);
	//	Mothership ai3 = Mothership.makeNewMothership(200,600,getAiEnemys());
	//	addObToGame(hu, new int[] {4,7,9,10}); 

	//	addObToGame(ai, new int[] {4,7,9,10}); 
		//addObToGame(ai1, new int[] {4,7,9,10}); 

	//	addObToGame(ai, new int[] {4,7,9,10,11}); 
	//	addObToGame(ai1, new int[] {4,7,9,10,11}); 
		//addObToGame(ai1, new int[] {4,7,9,10}); 

		//addObToGame(ai3, new int[] {4,7,10}); 
		
//		addObToGame(ai12, new int[] {4,7,9,10}); 
//		addObToGame(ai13, new int[] {4,7,9,10});
//		addObToGame(ai14, new int[] {4,7,9,10});
//		addObToGame(ai15, new int[] {4,7,9,10});
//		addObToGame(ai16, new int[] {4,7,9,10});
	//	addObToGame(nade,new int[] {5,6,7,9});
	//	addObToGame(nade2,new int[] {5,6,7,9});
	//	addObToGame(nade3,new int[] {5,6,7,9});
	//	addObToGame(gr,new int[] {4,7,9,10,11});




		p = super.p; 

		//spawnBunchOfMines(12);


		//Shield s = Shield.makeShield(new Corner(new double[] {500,500}), 150);
		//s.setUpShield(true, new GameObject[] {}, p);
	//	addObToGame(s, new int[] {1,2,3,4,5,6,7,8,9,10,11});
		//spawnBunchOfMines(12);

		


			
	}
	
	public void spawnBunchOfMines(int amount) {
        for(int i = 0; i < amount; i++) {
            if(i % 2 == 0) {
                addObToGame(HuntingMine.makeNewHuntingMine(i*50+500, i*80+600, getAiEnemys()),new int[] {4,7,9,10,11});
            }else {
                addObToGame(HuntingMine.makeNewHuntingMine(i*80+200, i*50+300, getAiEnemys()),new int[] {4,7,9,10,11});
            }
        }
    }
	
	
	public void tick() { 
		super.tick();
		respawnMeteorsToAmount(0);
		handleSummoners();
		
	}
	
	@Override
	public void updateDisplay() {
		super.updateDisplay();
	}
	
	
	

}
