package package1;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window extends JFrame implements KeyListener{
	private JButton exit, start;
	private boolean running = true;
	private Graphics g;
	private BufferStrategy bs;
	private Player p;
	private Corner[] corners = new Corner[3];
	private GameObject pes;
	public Window() {
		super("EPIC TITLE");
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setSize(1280,720);
		//setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		addKeyListener(this);
		/*
		JButton start = new JButton("START");
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		start.setBounds(getWidth()/2-150,getHeight()/2-75, 150, 75);
		add(start);
		
		JButton exit = new JButton("EXIT");
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
		exit.setBounds(getWidth()/2-150,getHeight()/2,150,75);
		add(exit);*/
		
		//TEST
		Corner peak = new Corner(new double[] {300,300}, new double[] {300,275});
        Corner rightCorner = new Corner(new double[] {275,250}, new double[] {300,275});
        Corner leftCorner = new Corner(new double[] {325,250}, new double[] {300,275});
        p = new Player(new Corner[] {peak, rightCorner, leftCorner},new double[] {300,275}, 1, new Corner(new double[] {300,300}, new double[] {300,275}));
        p.setVels(0, 0);
        //trojuhelnik s vnitrnim rohem
        Corner top = new Corner(new double[] {200,200}, new double[] {200,250});
        Corner left = new Corner(new double[] {150,250}, new double[] {200,250});
        Corner right = new Corner(new double[] {250,250}, new double[] {200,250});
        Corner bot = new Corner(new double[] {200,300}, new double[] {300,275});
        //ctverec
        /*
        Corner leftTop = new Corner(new double[] {350,350}, new double[] {300,275});
        Corner leftBot = new Corner(new double[] {350,450}, new double[] {300,275});
        Corner rightBot = new Corner(new double[] {450,450}, new double[] {300,275});
        Corner rightTop = new Corner(new double[] {450,350}, new double[] {300,275});
        pes = new GameObject(new Corner[] {leftTop, rightTop,rightBot,leftBot},new double[] {300,275}, -2);
        */
        //kosoctverec
     /*   Corner top = new Corner(new double[] {200,200}, new double[] {300,275});
        Corner left = new Corner(new double[] {150,250}, new double[] {300,275});
        Corner right = new Corner(new double[] {250,250}, new double[] {300,275});
        Corner bot = new Corner(new double[] {200,300}, new double[] {300,275});*/
        pes = new GameObject(new Corner[] {top, left, bot, right},new double[] {300,275}, -0.1);
        pes.setVels(0.2, 0);
        start();
		
		
	}
	public void start() {
		long lastTime = System.nanoTime();
        double amountOfTicks = 240;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
        	long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            render();
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
        if(p.getCrossedLineCorners(pes).length == 2) {
        	g.drawLine((int)Math.round(p.getCrossedLineCorners(pes)[0].getX()), (int)Math.round(p.getCrossedLineCorners(pes)[0].getY()), (int)Math.round(p.getCrossedLineCorners(pes)[1].getX()),(int)Math.round(p.getCrossedLineCorners(pes)[1].getY()));
        }
        bs.show();
        g.clearRect(0,0,getWidth(),getHeight());
		g.setColor(Color.BLUE);
		
		g.dispose();
		

		

		
			

	}
	public void tick() {		
    	p.updateLivingOb(); 
    //	System.out.println(p.checkCollision(pes) + "  collision");
    	p.updateReflection();
    	pes.rotateOb();
    	pes.moveOb();
    	if(p.getCrossedLineCorners(pes) != null && p.getCrossedLineCorners(pes).length >= 2) {
    		//System.out.println("CORNERS : "+p.getCrossedLineCorners(pes)[0].getX() +" : "+  p.getCrossedLineCorners(pes)[0].getY()  +"    "+  p.getCrossedLineCorners(pes)[1].getX()  +" : "+  p.getCrossedLineCorners(pes)[1].getY());
    		p.reflect(p.getCrossedLineCorners(pes)[0], p.getCrossedLineCorners(pes)[1]);
    		
    		
    	}
    	
    	
    	

	}
	public static void main(String[] args) {
		new Window();
	}
	@
	Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		p.keyPressed(e);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		p.keyReleased(e);
	}
	
}
