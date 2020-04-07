package package1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class GameModeTower extends Game implements KeyListener {
	private LivingObject Tower;
	private HuntingMine hm;
	private Mothership mp;
	private SpaceCanon sca;
	private SpaceCruiser scr;
	private JLabel waveDisplay, PlayerHPDisplay, PlayerAmmoDisplay;
	private Corner spawnCorner;
	private JProgressBar TowerHPDisplay, PlayerReloadTime;
	private JButton Power1, Power2, Power3, Power4;
	private BufferedImage HealthIcon, AmmoIcon;
	private Font font = new Font("josef", Font.PLAIN, 25);
	private int AIcount = 90;
	private int wave = 50;
	private int waveCount = 0;
	private int PowerLevel = 0;
	private int TowerBaseHP=1000;
	private int[] PowerLevelAr = new int[] {1,2,4,6};
	private int AIrnd, PUrnd1, PUrnd2;
	private boolean AIneeded = true, waveEnd = false, PUpicked = false;
	
	public GameModeTower(int sw, int sh) {
		super(sw, sh);
		Corner[] corners  = GameObject.generatePeriodicObject(100,8,new Corner(new double[] {mainWidth/2,mainHeight/2-50})).getCorners();
		Tower = new LivingObject(corners,new double[] {mainWidth/2,mainHeight/2},0,new Corner(new double[] {mainWidth/2,mainHeight/2}, new double[] {mainWidth/2,mainHeight/2}));
		Tower.setHP(TowerBaseHP);
		addObToGame(Tower, new int[] {1,3,4,5,6,7,8,9});
		
		setLayout(null);
		
		try {
			HealthIcon = ImageIO.read(new File("HealthIcon.png"));
			AmmoIcon =  ImageIO.read(new File("AmmoIcon.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Power1 = new JButton("Option 1");
		Power1.setFocusable(false);
		Power1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(Power1);
				remove(Power2);
				remove(Power3);
				remove(Power4);
				invalidate();
				revalidate();
				running = true;
				PUpicked = true;
				
			}
		});
		Power2 = new JButton("Option 2");
		Power2.setFocusable(false);
		Power2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(Power1);
				remove(Power2);
				remove(Power3);
				remove(Power4);;
				invalidate();
				revalidate();
				running = true;
				PUpicked = true;
				
			}
		});
		Power3 = new JButton("Option 3");
		Power3.setFocusable(false);
		Power3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(Power1);
				remove(Power2);
				remove(Power3);
				remove(Power4);
				invalidate();
				revalidate();
				running = true;
				PUpicked = true;
				
			}
		});
		Power4 = new JButton("Option 4");
		Power4.setFocusable(false);
		Power4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(Power1);
				remove(Power2);
				remove(Power3);
				remove(Power4);
				invalidate();
				revalidate();
				running = true;
				PUpicked = true;
				
			}
		});
		
		PlayerAmmoDisplay = new JLabel(""+ ((MagazineAttachment)p.getAttachments()[3]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[3]).getMagazineMaxSize());
		PlayerAmmoDisplay.setBounds(30,40,50,30);
		PlayerAmmoDisplay.setFont(font);
		add(PlayerAmmoDisplay);
		
		PlayerHPDisplay = new JLabel(""+ p.getHP());
		PlayerHPDisplay.setBounds(40,0,30,30);
		PlayerHPDisplay.setFont(font);
		PlayerHPDisplay.setForeground(new Color(141,192,63));
		add(PlayerHPDisplay);
		
		waveDisplay = new JLabel("Wave: " + wave);
		waveDisplay.setBounds(currentScreenWidth/2-50, 0, 150, 50);
		add(waveDisplay);
		
		PlayerReloadTime = new JProgressBar(0,((MagazineAttachment)p.getAttachments()[3]).getMagazineReloadLenght());
		PlayerReloadTime.setBounds(0, 70, 80, 10);
		PlayerReloadTime.setValue(((MagazineAttachment)p.getAttachments()[3]).getMagazineReloadLenght());
		PlayerReloadTime.setForeground(Color.BLACK);
		add(PlayerReloadTime);
		
		TowerHPDisplay = new JProgressBar(0, TowerBaseHP);
		TowerHPDisplay.setBounds(0, currentScreenHeight-50, currentScreenWidth, 50);
		TowerHPDisplay.setUI(new BasicProgressBarUI() {
			  protected Color getSelectionBackground() { return Color.black; }
		      protected Color getSelectionForeground() { return Color.black; }
		});
		TowerHPDisplay.setBackground(Color.RED);
		TowerHPDisplay.setForeground(Color.GREEN);
		TowerHPDisplay.setString(TowerBaseHP + "/" + TowerBaseHP);
		TowerHPDisplay.setStringPainted(true);
		TowerHPDisplay.setValue(TowerBaseHP);
		add(TowerHPDisplay);
	}
	public void tick() {
		super.tick();
		handleWaves();
		nextWave();
		updateDisplay();
		DisplayPowerUps();
		System.out.println(((MagazineAttachment)p.getAttachments()[3]).getMagazineReloadTimer());
	}
	public void handleWaves() {
		if(AIneeded && AIcount == 90) {	
			AIrnd = (int) (Math.random() * ((3-0)+1)) + 0;
			if(PowerLevelAr[AIrnd] + PowerLevel> wave) {	
				return;
			}
			if(PowerLevel + PowerLevelAr[AIrnd] < wave) {
				spawnAI(AIrnd);
				PowerLevel += PowerLevelAr[AIrnd];
			}
			if(PowerLevel + PowerLevelAr[AIrnd] == wave && AIneeded|| PowerLevel == wave && AIneeded) {
				spawnAI(AIrnd);
				PowerLevel = wave;
				AIneeded = false;
				waveEnd = true;
			}
			waveCount = wave;
			AIcount= 0;
		}
		else {
			AIcount++;
		}
	}
	public void updateDisplay() { 
		waveDisplay.setText("Wave: " + wave);
		TowerHPDisplay.setValue(Tower.getHP());
		TowerHPDisplay.setString(Tower.getHP() + "/" + TowerBaseHP);
		PlayerHPDisplay.setText(""+p.getHP());
		PlayerAmmoDisplay.setText(""+ ((MagazineAttachment)p.getAttachments()[3]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[3]).getMagazineMaxSize());
		PlayerReloadTime.setValue(((MagazineAttachment)p.getAttachments()[3]).getMagazineReloadTimer());
	}
	public void nextWave() {
		if(ais.length == 0 && wave != waveCount+1 && waveEnd) {
			wave++;
			AIneeded = true;
			PowerLevel=0;
			AIcount=90;
			waveEnd = false;
		}
	}
	public void DisplayPowerUps() {
		if(wave%5==0) {
			PUpicked = false;
		}
		if((wave-1)%5==0 && !PUpicked) {
			PUrnd1 = (int) (Math.random() * ((4-1)+1)) + 1;
			
			switch(PUrnd1) {
			case 1:
				stop();
				Power1.setBounds(125, 250, currentScreenWidth/2-250, currentScreenHeight-500);
				add(Power1);
				repaint();
				break;
			case 2:
				stop();
				Power2.setBounds(125, 250, currentScreenWidth/2-250, currentScreenHeight-500);
				add(Power2);
				repaint();
				break;
			case 3:
				stop();
				Power3.setBounds(125, 250, currentScreenWidth/2-250, currentScreenHeight-500);
				add(Power3);
				repaint();
				break;
			case 4:
				stop();
				Power4.setBounds(125, 250, currentScreenWidth/2-250, currentScreenHeight-500);
				add(Power4);
				repaint();
				break;
			default:
			}
			PUrnd2 = (int) (Math.random() * ((4-1)+1)) + 1;
			while(PUrnd1 == PUrnd2) {	
				PUrnd2 = (int) (Math.random() * ((4-1)+1)) + 1;
			}

				switch(PUrnd2) {
				case 1:
						stop();
						Power1.setBounds(currentScreenWidth-currentScreenWidth/2+125,250, currentScreenWidth/2-250, currentScreenHeight-500);
						add(Power1);
						repaint();
					break;
					case 2:
						stop();
						Power2.setBounds(currentScreenWidth-currentScreenWidth/2+125,250, currentScreenWidth/2-250, currentScreenHeight-500);
						add(Power2);
						repaint();
						break;
					case 3:
						stop();
						Power3.setBounds(currentScreenWidth-currentScreenWidth/2+125,250, currentScreenWidth/2-250, currentScreenHeight-500);
						add(Power3);
						repaint();
						break;
					case 4:
						stop();
						Power4.setBounds(currentScreenWidth-currentScreenWidth/2+125,250, currentScreenWidth/2-250, currentScreenHeight-500);
						add(Power4);
						repaint();
						break;
					default:
				}
			}
			
		}

	public void spawnAI(int PL) {
		spawnCorner = GameObject.generateCornerOutsideMapInRange(mainWidth, mainHeight, new int[] {70,100});
		switch(PL){

			case 0 : hm = HuntingMine.makeNewHuntingMine(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(hm, new int[] {4,7,9,10}); 
			break;

			case 1 : sca = SpaceCanon.makeNewSpaceCanon(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(sca, new int[] {4,7,9,10}); 
			break;

			case 2 : mp = Mothership.makeNewMothership(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(mp, new int[] {4,7,10}); 
			break;

			case 3 : scr = SpaceCruiser.makeNewSpaceCruiser(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(scr, new int[] {4,7,9,10}); 
			break;
			default : 
		}
	}
	
	 @Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(HealthIcon, 0, 0, 30, 30,null);
		g2.drawImage(AmmoIcon,0,40,30,30,null);
	}

	
}
