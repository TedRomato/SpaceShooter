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
	private Game game;
	private JPanel menu;
	
	public Window() {
		super("EPIC TITLE");
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setSize(1520,800);
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
				dispose();
				game.stop();
			}
		});
		menu.add(exit);
		
		
		add(menu);

		game = new Game(getWidth(), getHeight());
		add(game);

		setVisible(true);
		addKeyListener(this);
        while(true) {
		game.start();
		System.out.println("true");
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
