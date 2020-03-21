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
	private JPanel menu;
	private boolean running = true;
	private Graphics g;
	private BufferStrategy bs;

	private GameModeTesting game;

	public Window() {
		super("EPIC TITLE");

		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setSize(1540,865);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);


		
		
		menu = new JPanel();
		menu.setSize(1920, 1080);
		
		
		start = new JButton("New Game");
		start.setBounds(1920-50, 1080-25, 100, 50);
		start.setFocusable(false);
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getContentPane().removeAll();
				getContentPane().invalidate();
				getContentPane().add(game);
				getContentPane().revalidate();
				game.running = true;
				
			}
		});
		menu.add(start);
		
		exit = new JButton("Exit");
		exit.setBounds(1920-50, 1080-75, 100, 50);
		exit.setFocusable(false);
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				game.stop();
				running = false;
				dispose();
				
			}
		});
		menu.add(exit);
		
		
		add(menu);

		//game = new Game(getWidth(), getHeight());

		game = new GameModeTesting(getWidth(), getHeight());

		add(game);

		setVisible(true);
		addKeyListener(this);
        while(running) {
		game.start();
		System.out.print("");
        }	
	}
	
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
			game.stop();
			running = false;
			dispose();
			
		}
		game.keyPressed(e);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		game.keyReleased(e);
	}
	
	
}
