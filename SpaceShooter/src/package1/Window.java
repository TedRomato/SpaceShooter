package package1;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements KeyListener{
	private JButton exit, startTower, startClassic, startTest;
	public static JButton MainMenu;
	private JPanel menu;
	private boolean running = true;
	private Game game; 

	public Window() {
		super("EPIC FUCK UP");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setSize(1540,865); 
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		addKeyListener(this);
		
		
		game = new Game(screenWidth,screenHeight,false);
		game.setLayout(null);
		
		
		menu = new JPanel();
		menu.setSize(1920, 1080);
		menu.setLayout(null);
		
		MainMenu = new JButton("Main Menu");
		MainMenu.setBounds(screenWidth/2-100,screenHeight-300 , 200, 100);
		MainMenu.setFocusable(false);
		MainMenu.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getContentPane().removeAll();
				RenewMenu();
				getContentPane().add(menu);
				menu.repaint();
			}
		}); 
		
		startClassic = new JButton("Classic");
		startClassic.setBounds(screenWidth/2-100, screenHeight/2-275, 200, 100);
		startClassic.setFocusable(false);
		startClassic.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				game = new GameModeClassic(screenWidth,screenHeight);
				getContentPane().removeAll();
				getContentPane().invalidate();
				getContentPane().add(game);
				getContentPane().revalidate();
				game.running = true;
			}
		});
		
		
		
		startTower = new JButton("Tower");
		startTower.setBounds(screenWidth/2-100, screenHeight/2-175, 200, 100);
		startTower.setFocusable(false);
		startTower.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				game = new GameModeTower(screenWidth,screenHeight);
				getContentPane().removeAll();
				getContentPane().invalidate();
				getContentPane().add(game); 
				getContentPane().revalidate();
				game.running = true;
				
			}
		});
		
		
		
		startTest = new JButton("Test");
		startTest.setBounds(screenWidth-200, screenHeight-100, 200, 100);
		startTest.setFocusable(false);
		startTest.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				game = new GameModeRun(screenWidth,screenHeight, true);
				getContentPane().removeAll();
				getContentPane().invalidate();
				getContentPane().add(game);
				getContentPane().revalidate();
				game.running = true;
			}
		});
		
		
		
		exit = new JButton("Exit");
		exit.setBounds(screenWidth/2-100, screenHeight/2-75, 200, 100);
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
		
		
		RenewMenu();
		add(menu);
		menu.repaint();
		
        while(running) {
		game.start();
		System.out.print("");
        }	
	}
	public void RenewMenu() {
		menu.add(startClassic);
		menu.add(startTower);
		menu.add(startTest);
		menu.add(exit);
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
		if(e.getKeyCode() == 10	) {
			getContentPane().removeAll();
			getContentPane().invalidate();
			getContentPane().add(game);
			getContentPane().revalidate();
			game.running = true;
		}
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
