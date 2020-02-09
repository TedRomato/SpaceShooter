package package1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keys implements KeyListener{
	public static boolean left;
	public static boolean right;
	public static boolean forward;
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==e.VK_A) {
			left = true;
			System.out.println("left");
		}
		if(e.getKeyCode()==e.VK_D) {
			right = true;
			System.out.println("right");
		}
		if(e.getKeyCode()==e.VK_W) {
			forward = true;
			System.out.println("forward");
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==e.VK_A) {
			left = false;
			
		}
		if(e.getKeyCode()==e.VK_D) {
			right = false;
			
		}
		if(e.getKeyCode()==e.VK_W) {
			forward = false;
			
		}
	}

}
