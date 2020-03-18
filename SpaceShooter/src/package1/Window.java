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

	private GameModeClassic game;
	public Window() {
		super("EPIC TITLE");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setSize(1540,865);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		game = new GameModeClassic(getWidth(), getHeight());
		add(game);
		setVisible(true);
		setLayout(null);
		addKeyListener(this);
        game.start();	
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
