package package1;

import java.awt.event.KeyEvent;

public class GameModeClassic extends Game{

	private Player p = super.p;
	private GameObject coin = GameObject.generatePeriodicObject(15, 20, GameObject.generateCornerInRect(100, 100, screenWidth-100, screenHeight-100));
	private int score = 0;
	


	public GameModeClassic(int sw, int sh) {
		super(sw, sh); 
		coin.setHP(1);
		addObToGame(coin, new int[] {1,2,3,4,5,6,7,8});
		// TODO Auto-generated constructor stub
	}
	
	public void tick() { 
		handleCoin();
		super.tick();
		respawnMeteorsToAmount(8);

		
	}
	
	protected void handleCoin() {
		if(coin.getHP() <=0) {
			 coin = GameObject.generatePeriodicObject(15, 20, GameObject.generateCornerInRect(200, 200, screenWidth-300, screenHeight-300));
			 coin.setHP(1);
			 addObToGame(coin, new int[] {1,2,3,4,5,6,7,8});

		}
		
		if(p.checkCollision(coin)) {
			score++;
			coin.setHP(0);
			removeObFromGame(coin);
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
