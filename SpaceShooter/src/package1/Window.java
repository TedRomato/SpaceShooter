package package1;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window extends JFrame {
	private JButton exit, start;
	private boolean running = true;
	private Graphics g;
	private BufferStrategy bs;
	private Corner c1,c2,c3;
	private Corner[] corners = new Corner[3];
	private GameObject GO;
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
		add(exit);
		
		c1 = new Corner(50,50);
		corners[0] = c1;
		c2 = new Corner(75,50);
		corners[1] = c2;
		c3 = new Corner(50,75);
		corners[2] = c3;
		
	}
	public void start() {
		long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
        	long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >=1)
            	{
                tick();
                delta--;
                }
                if(running) 
                {
                	render();
                }
                frames++;
                            
                if(System.currentTimeMillis() - timer > 1000)
                {
                	timer += 1000;
                    System.out.println("FPS: "+ frames);
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
		GO = new GameObject(corners);
		bs = getBufferStrategy();
        if(bs == null) {
         createBufferStrategy(3);
         return;
        }   
        g = bs.getDrawGraphics();
        GO.render(g);
        g.clearRect(0,0,WIDTH,HEIGHT);
		g.setColor(Color.BLUE);
		bs.show();
		g.dispose();
		
			
	}
	public void tick() {
		
	}
	public static void main(String[] args) {
		new Window();
	}
	
}
