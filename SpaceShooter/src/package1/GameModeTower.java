package package1;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class GameModeTower extends Game{

	private Tower tower;
	private Hunter ht;
	private Grenader gr;
	private HuntingMine hm;
	private Mothership mp;
	private SpaceCanon sca;
	private SpaceCruiser scr;
	private JLabel waveDisplay,ShieldHPDisplay, PlayerHPDisplay, PlayerAmmoDisplay, GameOver, PowerUpDisplay, MachineGunAmmoDisplay, RocketAmmoDisplay;
	private Corner spawnCorner;
	private JProgressBar TowerHPDisplay, PlayerReloadTime, MachineGunReload, FaceCannonReload, DashRefill, ShieldStatus, BerserkReloadTime, PulseReloadTime;
	private JButton Power1, Power2, Power3, Power4, Power5, Power6, Power7, Power8;
	private BufferedImage Shield, BerserkMode, Pulse,ShieldIcon, HealthIcon, AmmoIcon , Plus1Mag, Plus1Health, DashIcon, MachineGunIcon, RocketIcon, RocketLauncher, MachineGun, DashRefillIcon,BerserkModeIcon,PulseIcon;
	private Font font = new Font("josef", Font.PLAIN, 25);
	private int AIcount = 90;
	private int wave = 1;
	private int waveCount = 0;
	private int PowerLevel = 0;
	private int TowerBaseHP=1000;
	private int NumberOfPowerUps = 6;
	private int[] PowerLevelAr = new int[] {1,2,4,6,3,4};
	private int AIrnd, PUrnd1, PUrnd2;
	private boolean AIneeded = true, waveEnd = false, PUpicked = false, ULTpicked = false;
	
	public GameModeTower(int sw, int sh) {
		super(sw, sh, true);
		Corner[] corners  = GameObject.generatePeriodicObject(100,8,new Corner(new double[] {mainWidth/2,mainHeight/2-50})).getCorners();
		tower = Tower.makeNewTower(currentScreenWidth/2/screenRatio, currentScreenHeight/2/screenRatio);
		tower.setHP(TowerBaseHP);

		addObToGame(tower, new int[] {1,3,4,6,7,9,11});
		p.addShotImune(tower);

		setLayout(null); 
		setName("TowerMode");

		try {
			HealthIcon = ImageIO.read(new File("src/Icons/HealthIcon.png"));
			AmmoIcon =  ImageIO.read(new File("src/Icons/AmmoIcon.png"));
			Plus1Mag =  ImageIO.read(new File("src/Icons/Magazine.png"));
			Plus1Health  =  ImageIO.read(new File("src/Icons/MedKit.png"));
			DashIcon = ImageIO.read(new File("src/Icons/Dash.png"));
			MachineGunIcon = ImageIO.read(new File("src/Icons/MachineGunAmmo.png"));
			RocketIcon = ImageIO.read(new File("src/Icons/Rocket.png"));
			RocketLauncher = ImageIO.read(new File("src/Icons/RocketLauncher.png"));
			MachineGun = ImageIO.read(new File("src/Icons/MachineGun.png"));
			DashRefillIcon = ImageIO.read(new File("src/Icons/DashRefillIcon.png"));
			ShieldIcon = ImageIO.read(new File("src/Icons/ShieldIcon.png"));
			BerserkModeIcon = ImageIO.read(new File("src/Icons/BerserkModeIcon.png"));
			PulseIcon = ImageIO.read(new File("src/Icons/PulseIcon.png"));
			Shield = ImageIO.read(new File("src/Icons/Shield.png"));
			BerserkMode = ImageIO.read(new File("src/Icons/BerserkMode.png"));
			Pulse = ImageIO.read(new File("src/Icons/Pulse.png"));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Power1 = new JButton("");
		Power1.addMouseListener(this);
		Power1.setName("Power1");
		Power1.setIcon(new ImageIcon(Plus1Health));
		Power1.setFocusable(false);
		Power1.setBackground(Color.WHITE);
		Power1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				p.setHP(50);
				running = true;
				PUpicked = true;
				
			}
		});
		Power2 = new JButton("");
		Power2.addMouseListener(this);
		Power2.setName("Power2");
		Power2.setIcon(new ImageIcon(Plus1Mag));
		Power2.setFocusable(false);
		Power2.setBackground(Color.WHITE);
		Power2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				((MagazineAttachment)p.getAttachments()[p.baseCanon]).upgradeMag(1);
				running = true;
				PUpicked = true;
				
			}
		});
		Power3 = new JButton("");
		Power3.addMouseListener(this);
		Power3.setName("Power3");
		Power3.setIcon(new ImageIcon(RocketLauncher));
		Power3.setFocusable(false);
		Power3.setBackground(Color.WHITE);
		Power3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(p.faceCanon==-1) {
					p.addFrontCanon();
					FaceCannonReload.setMaximum(((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());
					FaceCannonReload.setValue(((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());
					add(FaceCannonReload);
				}else {
					p.upgradeFaceCanon();
					FaceCannonReload.setMaximum(((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());
				}
				running = true;
				PUpicked = true;
				
			}
		});
		Power4 = new JButton("");
		Power4.addMouseListener(this);
		Power4.setName("Power4");
		Power4.setIcon(new ImageIcon(MachineGun));
		Power4.setFocusable(false);
		Power4.setBackground(Color.WHITE);
		Power4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(p.machinegun==-1) {
					p.addFrontMachineGun();
					MachineGunReload.setMaximum(((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
					MachineGunReload.setValue(((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
					add(MachineGunReload);
				}else {
					p.upgradeMG();
					MachineGunReload.setMaximum(((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
				}
				running = true;
				PUpicked = true;
				
			}
		});
		
		Power5 = new JButton("");
		Power5.addMouseListener(this);
		Power5.setName("Power5");
		Power5.setFocusable(false);
		Power5.setIcon(new ImageIcon(DashIcon));
		Power5.setBackground(Color.WHITE);
		Power5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(!p.isDashUnlocked()) {	
					p.setDashUnlocked(true);
					DashRefill.setMaximum(p.getDashCooldown());
					DashRefill.setValue(p.getDashCooldown());
					add(DashRefill); 
				} 
				else {
					p.upgradeDash(1);
					DashRefill.setMaximum(p.getDashCooldown());
				}
				running = true;
				PUpicked = true;
				
			}
		});
		Power6 = new JButton("");
		Power6.addMouseListener(this);
		Power6.setName("Power6");
		Power6.setFocusable(false);
		Power6.setIcon(new ImageIcon(Shield));
		Power6.setBackground(Color.WHITE);
		Power6.addActionListener(new ActionListener() {
			 
		
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(!p.isShieldIsUnlocked()) {	
					p.setShieldIsUnlocked(true);
					ShieldStatus.setMaximum(p.getShieldCooldown());
					ShieldStatus.setValue(p.getShieldCooldown());
					add(ShieldStatus);
				} 
				else {
					ShieldStatus.setMaximum(p.getShieldCooldown());
					p.upgradeShield();
				}
				running = true;
				PUpicked = true;
				
			}
		});
		Power7 = new JButton("");
		Power7.addMouseListener(this);
		Power7.setName("Power7");
		Power7.setFocusable(false);
		Power7.setIcon(new ImageIcon(Pulse));
		Power7.setBackground(Color.WHITE);
		Power7.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(!p.pulseIsUnlocked) {	
					p.setPulseUnlocked(true);
					PulseReloadTime.setMaximum(p.getPulseCooldown());
					PulseReloadTime.setValue(p.getPulseCooldown());
					add(PulseReloadTime);
				} 
				else {
					PulseReloadTime.setMaximum(p.getPulseCooldown());
					p.upgradePulse();
				}
				running = true;
				ULTpicked = true;
				PUpicked = true;
				
			}
		});
		Power8 = new JButton("");
		Power8.addMouseListener(this);
		Power8.setName("Power8");
		Power8.setFocusable(false);
		Power8.setIcon(new ImageIcon(BerserkMode));
		Power8.setBackground(Color.WHITE);
		Power8.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(!p.isBerserkModeUnlocked()) {	
					p.setBerserkModeUnlocked(true);
					BerserkReloadTime.setMaximum(p.getBerserkModeCooldown());
					BerserkReloadTime.setValue(p.getBerserkModeCooldown());
					add(BerserkReloadTime);
				} 
				else {
					BerserkReloadTime.setMaximum(p.getBerserkModeCooldown());
					p.upgradeBerserkMode();
				}
				running = true;
				ULTpicked = true;
				PUpicked = true;
				
			}
		});
		PowerUpDisplay = new JLabel("");
		PowerUpDisplay.setFont(font);
		PowerUpDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		
		PlayerAmmoDisplay = new JLabel(""+ ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineMaxSize());
		PlayerAmmoDisplay.setBounds(30,40,50,30);
		PlayerAmmoDisplay.setFont(font);
		add(PlayerAmmoDisplay);
		
		ShieldHPDisplay = new JLabel("");
		ShieldHPDisplay.setFont(font);
		ShieldHPDisplay.setBounds(30,161,50,30);
		ShieldHPDisplay.setForeground(new Color(0,191,255));
		
		MachineGunAmmoDisplay = new JLabel("");
		MachineGunAmmoDisplay.setBounds(30,80,50,30);
		MachineGunAmmoDisplay.setFont(font);
		add(MachineGunAmmoDisplay);
		
		RocketAmmoDisplay = new JLabel("");
		RocketAmmoDisplay.setBounds(30,121,50,30);
		RocketAmmoDisplay.setFont(font);
		add(RocketAmmoDisplay);
		
		GameOver = new JLabel("GAME OVER");
		GameOver.setForeground(Color.RED);
		GameOver.setFont(new Font("Karel",Font.BOLD,150));
		GameOver.setBounds(currentScreenHeight/2-50,currentScreenHeight/2-300,1000,300);
		
		PlayerHPDisplay = new JLabel(""+ p.getHP());
		PlayerHPDisplay.setBounds(40,0,30,30);
		PlayerHPDisplay.setFont(font);
		PlayerHPDisplay.setForeground(new Color(141,198,63));
		add(PlayerHPDisplay);
		
		waveDisplay = new JLabel("Wave: " + wave);
		waveDisplay.setBounds(currentScreenWidth/2-50, 0, 150, 50);
		waveDisplay.setFont(font);
		add(waveDisplay);
		
		PlayerReloadTime = new JProgressBar(0,((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadLenght());
		PlayerReloadTime.setBounds(0, 70, 80, 10);
		PlayerReloadTime.setValue(((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadLenght());
		PlayerReloadTime.setForeground(Color.BLACK);
		add(PlayerReloadTime);
		
		DashRefill = new JProgressBar(0,0);
		DashRefill.setBounds(0, 231, 80, 10);
		DashRefill.setForeground(new Color(225,174,19));
		
		MachineGunReload = new JProgressBar(0,0);
		MachineGunReload.setBounds(0, 110, 80, 10);
		MachineGunReload.setForeground(Color.BLACK);
		
		FaceCannonReload = new JProgressBar(0,0);
		FaceCannonReload.setBounds(0, 151, 80, 10);
		FaceCannonReload.setForeground(Color.BLACK);
		
		ShieldStatus = new JProgressBar(0,0);
		ShieldStatus.setBounds(0,191,80,10);
		ShieldStatus.setForeground(new Color(0,191,255));
		
		BerserkReloadTime = new JProgressBar(0,0);
		BerserkReloadTime.setBounds(0, 271, 80, 10);
		BerserkReloadTime.setForeground(Color.RED);
		
		PulseReloadTime = new JProgressBar(0,0);
		PulseReloadTime.setBounds(0,311,80,10);
		PulseReloadTime.setForeground(Color.MAGENTA);
		
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
		tower.updateAllTurrets(getAIS());
		handleWaves();
		nextWave();
		updateDisplay();
		DisplayPowerUps();
		endGame();
	}
	public void handleWaves() {
		if(AIneeded && AIcount == 90) {	
			AIrnd = (int) (Math.random() * ((5-0)+1)) + 0;
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
		super.updateDisplay();
		waveDisplay.setText("Wave: " + wave);
		TowerHPDisplay.setValue(tower.getHP());
		TowerHPDisplay.setString(tower.getHP() + "/" + TowerBaseHP);
		PlayerHPDisplay.setText(""+p.getHP());
		ShieldHPDisplay.setText(""+p.getShieldHP());
		PlayerAmmoDisplay.setText("" + ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineMaxSize());
		if(((MagazineAttachment)p.getAttachments()[p.baseCanon]).getReloadingMag()) {
			PlayerReloadTime.setValue(((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadTimer());
		}else  {
			PlayerReloadTime.setValue(((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadLenght());
		}
		if(p.machinegun!=-1) {
			if(((MagazineAttachment)p.getAttachments()[p.machinegun]).getReloadingMag()) {
				MachineGunReload.setValue(((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadTimer());
			}else {
				MachineGunReload.setValue(((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
			}
			MachineGunAmmoDisplay.setText(""+((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineMaxSize());
		}
		if(p.faceCanon!=-1) {
			if(((MagazineAttachment)p.getAttachments()[p.faceCanon]).getReloadingMag()) {
				FaceCannonReload.setValue(((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadTimer());
			}else {
				FaceCannonReload.setValue(((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());

			}
			RocketAmmoDisplay.setText(""+((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineMaxSize());
		}
		if(p.isDashUnlocked()) {
			DashRefill.setValue(p.getDashCooldownTimer());
		}
		if(p.isShieldIsUnlocked()) {
			if(p.shieldIsUp) {
				add(ShieldHPDisplay);
				ShieldStatus.setMaximum(p.getShieldDuration());
			}
			else {
				remove(ShieldHPDisplay);
				ShieldStatus.setMaximum(p.getShieldCooldown());
				ShieldStatus.setValue(p.getShieldTimer());
			}
		}
		if(p.isBerserkModeUnlocked()) {
			if(p.berserkMode) {
				BerserkReloadTime.setMaximum(p.getExploWave());
				BerserkReloadTime.setValue(p.getExploWave()-p.getExploWaveCounter());
			}
			else {
				BerserkReloadTime.setMaximum(p.getBerserkModeCooldown());
				BerserkReloadTime.setValue(p.getBerserkModeTimer());
			}
		}
		if(p.isPulseUnlocked()) {
			PulseReloadTime.setValue(p.getPulseCooldownTimer());
		}
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
	public void endGame() {
		if(tower.getHP()<=0 || p.getHP() <=0) {
			stop();
			remove(Warning);
			add(GameOver);
			add(Window.MainMenu);
		}
	}
	public void DisplayPowerUps() {
		if(wave%2==0) {
			PUpicked = false;
		}
		
		if(wave%5==0) {
			ULTpicked = false;
		}
		if((wave-1)%2==0 && !PUpicked&&wave!=1||(wave-1)%5==0 && !ULTpicked&&wave!=1) {

			
			PUrnd1 = (int) (Math.random() * ((NumberOfPowerUps-1)+1)) + 1;	
			PUrnd2 = (int) (Math.random() * ((NumberOfPowerUps-1)+1)) + 1;
			while(PUrnd1 == PUrnd2) {	
				PUrnd2 = (int) (Math.random() * ((NumberOfPowerUps-1)+1)) + 1;
			}
			if((wave-1)%5==0) {
				PUrnd1 = 7;
				PUrnd2 = 8;
			}
			choosePowerUps(PUrnd1,125,250); 
			choosePowerUps(PUrnd2, currentScreenWidth/2+125, 250);
			}
			
		}
	public void choosePowerUps(int rnd,int x, int y) {
		switch(rnd) {
		case 1:
			stop();
			Power1.setBounds(x, y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power1);
			repaint();
			break;
		case 2:
			stop();
			Power2.setBounds(x, y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power2);
			repaint();
			break;
		case 3:
			stop();
			Power3.setBounds(x, y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power3);
			repaint();
			break;
		case 4:
			stop();
			Power4.setBounds(x, y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power4);
			repaint();
			break;
		case 5:
			stop();
			Power5.setBounds(x,y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power5);
			repaint();
			break;
		case 6:
			stop();
			Power6.setBounds(x,y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power6);
			repaint();
			break;
		case 7:
			stop();
			Power7.setBounds(x,y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power7);
			repaint();
			break;
		case 8:
			stop();
			Power8.setBounds(x,y, currentScreenWidth/2-250, currentScreenHeight-500);
			add(Power8);
			repaint();
			break;
		default: 
		}
	}
	public void spawnAI(int PL) {
		spawnCorner = GameObject.generateCornerOutsideMapInRange(mainWidth, mainHeight, new int[] {600,1000});
		switch(PL){

			case 0 : hm = HuntingMine.makeNewHuntingMine(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(hm, new int[] {4,7,9,10,11}); 
			break;

			case 1 : sca = SpaceCanon.makeNewSpaceCanon(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(sca, new int[] {4,7,9,10,11}); 
			break;

			case 2 : mp = Mothership.makeNewMothership(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(mp, new int[] {4,7,10,11}); 
			break;

			case 3 : scr = SpaceCruiser.makeNewSpaceCruiser(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys()); addObToGame(scr, new int[] {4,7,9,10,11}); 
			break;
			
			case 4 : ht = Hunter.makeNewHunter(spawnCorner.getX(), spawnCorner.getY(), getAiEnemys()); addObToGame(ht, new int[] {4,7,9,10,11}); 
			break;
			
			case 5 : gr = Grenader.makeNewGrenader(spawnCorner.getX(), spawnCorner.getY(), getAiEnemys()); addObToGame(gr, new int[] {4,7,9,10,11}); 
			break;
			default : 
		}
	}
	public void removeButtons() {
		remove(Power1);
		remove(Power2);
		remove(Power3);
		remove(Power4);
		remove(Power5);
		remove(Power6); 
		remove(Power7);
		remove(Power8);
	}
	 @Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(HealthIcon, 0, 0, 30, 30,null);
		g2.drawImage(AmmoIcon,0,40,30,30,null);
		if(p.machinegun!=-1) {	
			g2.drawImage(MachineGunIcon, 0, 80, 30, 30,null);
		}
		if(p.faceCanon!=-1) {
			g2.drawImage(RocketIcon, 0, 121, 30, 30,null);
		}
		if(p.isDashUnlocked()) {
			g2.drawImage(DashRefillIcon, 20, 201, 40, 30,null);
		}
		if(p.isShieldIsUnlocked()) {
			g2.drawImage(ShieldIcon, 0,161,30,30,null);
		}
		if(p.isBerserkModeUnlocked()) {
			Image tmp = BerserkModeIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	        BufferedImage resized = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = resized.createGraphics();
	        g2d.drawImage(tmp, 0, 0, null);
	        g2d.dispose();
			g2.drawImage(resized,25,241,30,30,null);
		}
		if(p.isPulseUnlocked()) {
			
			g2.drawImage(PulseIcon, 25,281,30,30,null);
		}
	}

	 @Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseEntered(e);
		switch(e.getComponent().getName()) {
			case "TowerMode" : 
				remove(PowerUpDisplay);
				repaint();
				break;
			case "Power1" :
				PowerUpDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 50);
				PowerUpDisplay.setText("<html>MedKit - Fully restores your Health<html>");
				add(PowerUpDisplay);
				repaint();
				break;
			case "Power2" :
				PowerUpDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 50);
				PowerUpDisplay.setText("<html>Magazine extender - Increases your ammo capacity by 1<html>");
				add(PowerUpDisplay);
				repaint();
				break;
			case "Power3" :
				PowerUpDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 65);
				if(p.faceCanon == -1) {
					PowerUpDisplay.setText("<html>Rocket Launcher - RIGHT CLICK + LEFT CLICK to shoot rockets<html>");
				}
				else {
					PowerUpDisplay.setText("<html>Rocket Launcher upgrade - Slightly reduceses reload time and encreases damage by 1<html>");
				}
				add(PowerUpDisplay);
				repaint();
				break;
			case "Power4" :
				PowerUpDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 65);
				if(p.machinegun == -1) {
					PowerUpDisplay.setText("<html>Machine Gun - Press SPACEBAR + LEFT CLICK to activate Machine guns<html>");
				}
				else {
					PowerUpDisplay.setText("<html>Machine Gun upgrade - Slightly reduceses reload time and<br>encreases ammo capacity by 1<html>");
				}
				add(PowerUpDisplay);
				repaint();
				break;
			case "Power5" :
				PowerUpDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 50);
				if(!p.isDashUnlocked()) {
					PowerUpDisplay.setText("<html>Dash - Press SHIFT to leap forward<html>");
				}
				else {
					PowerUpDisplay.setText("<html>Dash upgrade - Slightly reduceses dash charge time<html>");
				}
				add(PowerUpDisplay);
				repaint();
				break;
			default: 
		}
		
	}
	 @Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseExited(e);
	}
}
