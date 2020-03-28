package package1;

import java.awt.event.KeyEvent;

public class GameModeTesting extends Game{
	private Player p = super.p;


	public GameModeTesting(int sw, int sh) {
		super(sw, sh);
		SpaceCruiser ai = SpaceCruiser.makeNewSpaceCruiser(1000,600);
		addObToGame(ai, new int[] {4,7}); 



			
	}
	
	
	public void tick() { 
		super.tick();
		respawnMeteorsToAmount(0);
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
