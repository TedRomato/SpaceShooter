package package1;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Window extends JFrame implements KeyListener{
	private JButton exit, startTower, startClassic, startTest;
	public static JButton MainMenu;
	private Menu menu;
	private boolean running = true;
	private Game game;
	private ChangeListener ButtonsTextHandler;

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
		
		
		menu = new Menu();
		menu.setSize(1920, 1080);
		menu.setLayout(null);
		
		
		MainMenu = new JButton("Main Menu");
		MainMenu.setBounds(screenWidth/2-152,screenHeight-300 , 304, 50);
		MainMenu.setFont(new Font(Font.MONOSPACED,Font.BOLD , 50));
		MainMenu.setForeground(Color.YELLOW);
		Game.MakeTransparentButton(MainMenu);
		setButtonChangeListener(MainMenu);
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
		
		startClassic = new JButton("Run Mode");
		startClassic.setFont(new Font(Font.MONOSPACED, Font.BOLD,55));
		startClassic.setForeground(Color.YELLOW);
		startClassic.setBounds(100, screenHeight/2-55, 300, 45);
		startClassic.setFocusable(false);
		setButtonChangeListener(startClassic);
		Game.MakeTransparentButton(startClassic);
		startClassic.addActionListener(new ActionListener() {		
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
		
		
		
		startTower = new JButton("Tower Mode");
		startTower.setFont(new Font(Font.MONOSPACED, Font.BOLD,55));
		startTower.setForeground(Color.YELLOW);
		startTower.setBounds(100, screenHeight/2-100, 365, 45);
		startTower.setFocusable(false);
		setButtonChangeListener(startTower);
		Game.MakeTransparentButton(startTower);
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
		exit.setFont(new Font(Font.MONOSPACED, Font.BOLD,55));
		exit.setForeground(Color.YELLOW);
		exit.setBounds(100, screenHeight/2-10, 166, 45);
		exit.setFocusable(false);
		setButtonChangeListener(exit);
		Game.MakeTransparentButton(exit);
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
		//menu.add(startTest);
		menu.add(exit);
	}
	public static void setButtonChangeListener(JButton b) {
		b.getModel().addChangeListener(new ChangeListener() {			
			@Override
		public void stateChanged(ChangeEvent e) {
		    if (b.getModel().isRollover()) {
		    	b.setForeground(Color.ORANGE);
		    }
		    else {
		    	b.setForeground(Color.YELLOW);
		    }
		    if (b.getModel().isPressed()) {
		    	b.setForeground(Color.RED);
		    	}	 
			}
		});
		
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
class Menu extends JPanel{
	private BufferedImage BG;
	private JLabel SpaceShooterSign;
	private Shape Outline;
	public Menu() {
		try {
			BG =  ImageIO.read(new File("src/BG/SpaceShooterBG.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SpaceShooterSign = new JLabel("Space Shooter", SwingConstants.CENTER);
		SpaceShooterSign.setForeground(Color.YELLOW);
		SpaceShooterSign.setFont(new Font(Font.MONOSPACED,Font.BOLD, (int)(250*Game.screenRatio)));
		SpaceShooterSign.setBounds(0, 50, Game.currentScreenWidth, 160);
		//add(SpaceShooterSign);
		
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(BG, 0, 0, Game.currentScreenWidth, Game.currentScreenHeight, null);
		FontRenderContext fontRendContext = g2.getFontRenderContext();
		TextLayout tl = new TextLayout(SpaceShooterSign.getText(), SpaceShooterSign.getFont(), fontRendContext);
		AffineTransform af = g2.getTransform();
		af.translate(735*Game.screenRatio, 320*Game.screenRatio);	
		Outline = tl.getOutline(af);
		g2.setColor(Color.YELLOW);
		g2.setClip(Outline);
		g2.fill(Outline.getBounds());
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(5));
		g2.draw(Outline);
		g2.setClip(null);
	}
}