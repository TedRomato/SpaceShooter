package package1;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener{
	GameObject[] gameObjects;
	Player[] players;
	private Player p;
	
	public Game(){
		Corner peak = new Corner(new double[] {100,100}, new double[] {100,60});
        Corner rightCorner = new Corner(new double[] {50,50}, new double[] {100,60});
        Corner leftCorner = new Corner(new double[] {150,50}, new double[] {100,60});
        p = new Player(new Corner[] {peak, rightCorner, leftCorner},new double[] {100,60}, -4.333, new Corner(new double[] {100,100}, new double[] {100,60}));
        p.setVels(1, 1);
        gameObjects = new GameObject[] {p};
        players = new Player[] {p};
		
	}
	public void renderGameObs(Graphics g) {
		if(gameObjects.length > 0) {
			for(GameObject go : gameObjects) {
				go.render(g);
			}
		}
	}
	public void updateGame() {
		for(GameObject go : gameObjects) {
			go.moveOb();
			go.rotateOb();
		}
		for(Player player: players) {
			player.update();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		for(Player player : players) {
			player.keyPressed(e);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		for(Player player : players) {
			player.keyReleased(e);
		}
		
	}
	

}
