package package1;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window extends JFrame {
	private JButton exit, start;
	private boolean running = false;
	public Window() {
		super("EPIC TITLE");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		
		JButton start = new JButton("START");
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				running = true;
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
		
		
	}
	public void start() {
		long lastTime = System.nanoTime();
        double amountOfTicks = 120.0;
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
		
			
	}
	public void tick() {
		
	}
	public static void main(String[] args) {
		new Window();
	}
	
}
