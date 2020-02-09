package package1;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Game{
	public static JPanel gp = new GamePanel();
	private boolean running = true;
	public Game() {
		
	}
	public void start() {
		long lastTime = System.nanoTime();
        double amountOfTicks = 250;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
        	long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            gp.repaint();
            while(delta >=1)
            	{
                tick();
                delta--;
                }
                
                frames++;
                            
                if(System.currentTimeMillis() - timer > 1000)
                {
                	timer += 1000;
            //      System.out.println("FPS: "+ frames);
                    frames = 0;
                    }
        }
	}
	public void stop() {
		if(running) {
			running = false;
		}
	}
	public void tick() {		
    	Window.p.updateOb(); 
    //	System.out.println(p.checkCollision(pes) + "  collision");
    	Window.p.updateReflection();
    	Window.pes.updateOb();
    	Window.les.updateOb();
    	if(Window.p.getCrossedLineCorners(Window.pes) != null && Window.p.getCrossedLineCorners(Window.pes).length >= 2) {
    		//System.out.println("CORNERS : "+p.getCrossedLineCorners(pes)[0].getX() +" : "+  p.getCrossedLineCorners(pes)[0].getY()  +"    "+  p.getCrossedLineCorners(pes)[1].getX()  +" : "+  p.getCrossedLineCorners(pes)[1].getY());
    		Window.p.reflect(Window.p.getCrossedLineCorners(Window.pes)[0], Window.p.getCrossedLineCorners(Window.pes)[1]);		
    	}
    	if(Window.p.getCrossedLineCorners(Window.les) != null && Window.p.getCrossedLineCorners(Window.les).length >= 2) {
    		//System.out.println("CORNERS : "+p.getCrossedLineCorners(pes)[0].getX() +" : "+  p.getCrossedLineCorners(pes)[0].getY()  +"    "+  p.getCrossedLineCorners(pes)[1].getX()  +" : "+  p.getCrossedLineCorners(pes)[1].getY());
    		Window.p.reflect(Window.p.getCrossedLineCorners(Window.les)[0], Window.p.getCrossedLineCorners(Window.les)[1]);		
    	}
    	if(Window.pes.getCrossedLineCorners(Window.les) != null && Window.pes.getCrossedLineCorners(Window.les).length >= 2) {
    		System.out.println("CORNERS pes : "+Window.pes.getCrossedLineCorners(Window.les)[0].getX() +" : "+  Window.pes.getCrossedLineCorners(Window.les)[0].getY()  +"    "+  Window.pes.getCrossedLineCorners(Window.les)[1].getX()  +" : "+  Window.pes.getCrossedLineCorners(Window.les)[1].getY());
    		Window.pes.reflect(Window.pes.getCrossedLineCorners(Window.les)[0], Window.pes.getCrossedLineCorners(Window.les)[1]);		
    	}
    	if(Window.les.getCrossedLineCorners(Window.pes) != null && Window.les.getCrossedLineCorners(Window.pes).length >= 2) {
    		System.out.println("CORNERS les : "+Window.les.getCrossedLineCorners(Window.pes)[0].getX() +" : "+  Window.les.getCrossedLineCorners(Window.pes)[0].getY()  +"    "+  Window.les.getCrossedLineCorners(Window.pes)[1].getX()  +" : "+  Window.les.getCrossedLineCorners(Window.pes)[1].getY());
    		Window.les.reflect(Window.les.getCrossedLineCorners(Window.pes)[0], Window.les.getCrossedLineCorners(Window.pes)[1]);		
    	}
}
}
class GamePanel extends JPanel{ 
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(1920,1080);
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Window.p.render(g2);
        Window.pes.render(g2);
        Window.les.render(g2);
	}
}