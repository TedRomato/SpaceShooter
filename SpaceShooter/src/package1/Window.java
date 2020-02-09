package package1;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements KeyListener{
	private JButton exit, start;
	private boolean running = true;
	private Graphics g;
	private BufferStrategy bs;

	private Game game;
	public Window() {
		super("EPIC TITLE");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setSize(1920,1080);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		game = new Game();
		add(game);
		setVisible(true);
		setLayout(null);
		addKeyListener(this);
		
		
        game.start();
		

		
	}
	/*public void start() {
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
            panel.repaint();
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
	public void render() {

		bs = getBufferStrategy();
        if(bs == null) {
         createBufferStrategy(3);
         return;
        }   
        g = bs.getDrawGraphics();

        p.render(g);
        pes.render(g);
        les.render(g);
        bs.show();
        g.clearRect(0,0,getWidth(),getHeight());
		g.setColor(Color.BLUE);
		
		g.dispose();
		

		

		
			

	}
	public void tick() {		
    	p.updateOb(); 
    //	System.out.println(p.checkCollision(pes) + "  collision");
    	p.updateReflection();
    	pes.updateOb();
    	les.updateOb();
    	if(p.getCrossedLineCorners(pes) != null && p.getCrossedLineCorners(pes).length >= 2) {
    		//System.out.println("CORNERS : "+p.getCrossedLineCorners(pes)[0].getX() +" : "+  p.getCrossedLineCorners(pes)[0].getY()  +"    "+  p.getCrossedLineCorners(pes)[1].getX()  +" : "+  p.getCrossedLineCorners(pes)[1].getY());
    		p.reflect(p.getCrossedLineCorners(pes)[0], p.getCrossedLineCorners(pes)[1]);		
    	}
    	if(p.getCrossedLineCorners(les) != null && p.getCrossedLineCorners(les).length >= 2) {
    		//System.out.println("CORNERS : "+p.getCrossedLineCorners(pes)[0].getX() +" : "+  p.getCrossedLineCorners(pes)[0].getY()  +"    "+  p.getCrossedLineCorners(pes)[1].getX()  +" : "+  p.getCrossedLineCorners(pes)[1].getY());
    		p.reflect(p.getCrossedLineCorners(les)[0], p.getCrossedLineCorners(les)[1]);		
    	}
    	if(pes.getCrossedLineCorners(les) != null && pes.getCrossedLineCorners(les).length >= 2) {
    		System.out.println("CORNERS pes : "+pes.getCrossedLineCorners(les)[0].getX() +" : "+  pes.getCrossedLineCorners(les)[0].getY()  +"    "+  pes.getCrossedLineCorners(les)[1].getX()  +" : "+  pes.getCrossedLineCorners(les)[1].getY());
    		pes.reflect(pes.getCrossedLineCorners(les)[0], pes.getCrossedLineCorners(les)[1]);		
    	}
    	if(les.getCrossedLineCorners(pes) != null && les.getCrossedLineCorners(pes).length >= 2) {
    		System.out.println("CORNERS les : "+les.getCrossedLineCorners(pes)[0].getX() +" : "+  les.getCrossedLineCorners(pes)[0].getY()  +"    "+  les.getCrossedLineCorners(pes)[1].getX()  +" : "+  les.getCrossedLineCorners(pes)[1].getY());
    		les.reflect(les.getCrossedLineCorners(pes)[0], les.getCrossedLineCorners(pes)[1]);		
    	}
    	
    	
    	

	}*/
	public static void main(String[] args) {
		new Window();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == e.VK_ESCAPE) {
			dispose();
			game.stop();
		}
		game.keyPressed(e);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		game.keyReleased(e);
	}
	
	
}
