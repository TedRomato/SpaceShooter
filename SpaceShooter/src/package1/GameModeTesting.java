package package1;

import java.awt.event.KeyEvent;

public class GameModeTesting extends Game{
	private Player p = super.p;


	public GameModeTesting(int sw, int sh) {
		super(sw, sh);
		SpaceCruiser ai = SpaceCruiser.makeNewSpaceCruiser(500,200);
		SpaceCanon ai1 = SpaceCanon.makeNewSpaceCanon(1000,600);
		HuntingMine ai2 = HuntingMine.makeNewHuntingMine(1000,200);
		Mothership ai3 = Mothership.makeNewMothership(200,600);
		addObToGame(ai, new int[] {4,7,9}); 
		addObToGame(ai1, new int[] {4,7,9}); 
		addObToGame(ai2, new int[] {4,7,9}); 
		addObToGame(ai3, new int[] {4,7});

		


			
	}
	
	
	public void tick() { 
		super.tick();
		respawnMeteorsToAmount(3);
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
