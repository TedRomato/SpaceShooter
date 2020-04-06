package package1;

import java.awt.event.KeyEvent;

public class GameModeTesting extends Game{
	private Player p = super.p;


	public GameModeTesting(int sw, int sh) {
		super(sw, sh);
	//	SpaceCruiser ai = SpaceCruiser.makeNewSpaceCruiser(500,200,getAiEnemys());
	//	SpaceCanon ai1 = SpaceCanon.makeNewSpaceCanon(1000,600,getAiEnemys());
	//	SpaceCanon ai1 = SpaceCanon.makeNewSpaceCanon(1000,600);
	/*	SpaceCanon ai11 = SpaceCanon.makeNewSpaceCanon(1000,1000);
		SpaceCanon ai12 = SpaceCanon.makeNewSpaceCanon(1000,800);
		SpaceCanon ai13 = SpaceCanon.makeNewSpaceCanon(1200,860);
		SpaceCanon ai14 = SpaceCanon.makeNewSpaceCanon(900,750);
		SpaceCanon ai15 = SpaceCanon.makeNewSpaceCanon(950,700);
		SpaceCanon ai16 = SpaceCanon.makeNewSpaceCanon(1200,800);*/
	//	HuntingMine ai2 = HuntingMine.makeNewHuntingMine(1000,200);
	//	Mothership ai3 = Mothership.makeNewMothership(200,600,getAiEnemys());
	//	addObToGame(ai, new int[] {4,7,9,10}); 
	//	addObToGame(ai1, new int[] {4,7,9,10}); 
	//	addObToGame(ai3, new int[] {4,7,10}); 

	/*	addObToGame(ai12, new int[] {4,7,9}); 
		addObToGame(ai13, new int[] {4,7,9});
		addObToGame(ai14, new int[] {4,7,9});
		addObToGame(ai15, new int[] {4,7,9});
		addObToGame(ai16, new int[] {4,7,9});*/



		


			
	}
	
	
	public void tick() { 
		super.tick();
		respawnMeteorsToAmount(0);
		handleSummoners();
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
