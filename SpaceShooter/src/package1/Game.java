package package1;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Game extends JPanel{
	private  Player p;
	private  Meteor pes, les;
	private GameObject[] objects;
	private MovingObject[] reflectableObs;
	private GameObject[] reflectedLivingObs;
	//public static JPanel gp = new GamePanel();
	private boolean running = true;
	
	public Game() {
		//TEST
		Corner peak = new Corner(new double[] {400,200}, new double[] {400,175});
	    Corner rightCorner = new Corner(new double[] {375,150}, new double[] {400,175});
	    Corner leftCorner = new Corner(new double[] {425,150}, new double[] {400,175});
	    p = new Player(new Corner[] {peak, rightCorner, leftCorner},new double[] {400,175}, 1, new Corner(new double[] {400,200}, new double[] {400,175}));
	    //trojuhelnik s vnitrnim rohem
	   /*
	    Corner top = new Corner(new double[] {200,200}, new double[] {200,250});
	    Corner left = new Corner(new double[] {150,250}, new double[] {200,250});
	    Corner right = new Corner(new double[] {250,250}, new double[] {200,250});
	    Corner bot = new Corner(new double[] {200,300}, new double[] {300,275});*/
	    //ctverec
	    
	    Corner leftTop = new Corner(new double[] {350,350}, new double[] {400,400});
	    Corner leftBot = new Corner(new double[] {350,450}, new double[] {400,400});
	    Corner rightBot = new Corner(new double[] {450,450}, new double[] {400,400});
	    Corner rightTop = new Corner(new double[] {450,350}, new double[] {400,400});
	    les = new Meteor(new Corner[] {leftTop, leftBot, rightBot, rightTop},new double[] {400,400}, -0.5, new Corner(new double[] {350,400}, new double[] {400,400}), -0.2, 1);
	    
	    //kosoctverec
	    Corner top = new Corner(new double[] {200,200}, new double[] {200,250});
	    Corner left = new Corner(new double[] {150,250}, new double[] {200,250});
	    Corner right = new Corner(new double[] {250,250}, new double[] {200,250});
	    Corner bot = new Corner(new double[] {200,300}, new double[] {200,250});
	    
	    
	    pes = new Meteor(new Corner[] {top, left, bot, right},new double[] {200,250}, 0.5, new Corner(new double[] {250,300}, new double[] {200,250}), -0.2, 1);
	    objects = new GameObject[] {pes,les,p};
	    reflectableObs = new MovingObject[] {pes,les,p};
	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	
		p.keyPressed(e);
	}
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		p.keyReleased(e);
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
            this.repaint();
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
    	updateAllObs();
    //	System.out.println(p.checkCollision(pes) + "  collision");
    	p.updateReflection();
    	checkAndHandleAllRefs();
	}
    	/*
    	if(p.getCrossedLineCorners(pes) != null && p.getCrossedLineCorners(pes).length >= 2) {
    		//System.out.println("CORNERS : "+p.getCrossedLineCorners(pes)[0].getX() +" : "+  p.getCrossedLineCorners(pes)[0].getY()  +"    "+  p.getCrossedLineCorners(pes)[1].getX()  +" : "+  p.getCrossedLineCorners(pes)[1].getY());
    		p.reflect(p.getCrossedLineCorners(pes)[0], p.getCrossedLineCorners(pes)[1]);		
    	}
    	if(p.getCrossedLineCorners(les) != null && p.getCrossedLineCorners(les).length >= 2) {
    		//System.out.println("CORNERS : "+p.getCrossedLineCorners(pes)[0].getX() +" : "+  p.getCrossedLineCorners(pes)[0].getY()  +"    "+  p.getCrossedLineCorners(pes)[1].getX()  +" : "+  p.getCrossedLineCorners(pes)[1].getY());
    		p.reflect(p.getCrossedLineCorners(les)[0], p.getCrossedLineCorners(les)[1]);		
    	}
    	if(pes.getCrossedLineCorners(les) != null && pes.getCrossedLineCorners(les).length >= 2) {
    		pes.reflect(pes.getCrossedLineCorners(les)[0], pes.getCrossedLineCorners(les)[1]);		
    	}
    	if(les.getCrossedLineCorners(pes) != null && les.getCrossedLineCorners(pes).length >= 2) {
    		les.reflect(les.getCrossedLineCorners(pes)[0], les.getCrossedLineCorners(pes)[1]);		
    	}
	}*/
	
	private void checkAndHandleAllRefs() {
		int i = 0;
		while(i < reflectableObs.length) {
			int q = 0;
			while(q < reflectableObs.length) {
				if(i != q) {
					reflectableObs[i].checkAndHandleReflect(reflectableObs[q]);;
				}
				q++;
			}
			i++;
		}
	}
	
	private void updateAllObs() {
		for(GameObject go : objects) {
			go.updateOb();
		}
	}
	
	private void renderAll(Graphics g) {
		if(objects != null) {
			if(objects.length > 0) {
				for(GameObject ob : objects) {
					ob.render(g);
				}
			}
		}
	}
	
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
		renderAll(g2);
/*		p.render(g2);
        pes.render(g2);
        les.render(g2);*/
}
	
	/*
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
		p.render(g2);
        pes.render(g2);
        les.render(g2);
		}
	}*/
}