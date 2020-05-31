package package1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.GridLayout;

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class GameModeTower extends Game{

	private Tower tower;
	private JLabel waveDisplay,MoneyDisplay,CostDisplay,ShieldHPDisplay, PowerUpDisplay, MachineGunAmmoDisplay, RocketAmmoDisplay;
	private JProgressBar TowerHPDisplay, MachineGunReload, FaceCannonReload, ShieldStatus, BerserkReloadTime;
	private JButton Power1, Power2, Power3, Power4, Power5, Power6, Power7, Power8, Shop, Resume, TurretUpgrade1, TurretUpgrade2,TurretUpgrade3;
	private BufferedImage Shield, BerserkMode, Pulse,ShieldIcon, Plus1Mag, Plus1Health, DashIcon, MachineGunIcon, RocketIcon, RocketLauncher, MachineGun, BerserkModeIcon;
	private int AIcount = 90;
	private int wave = 6;
	private int waveCount = 0;
	private int AIStrength = 0;
	private int TowerBaseHP=1000;
	private int NumberOfPowerUps = 6;
	private int[] StrenghtAr = new int[] {1,2,4,6,3,4,3};
	private int AIrnd, PUrnd1, PUrnd2, AIPowerLevel;
	private boolean AIneeded = true, waveEnd = false, PUpicked = false, ULTpicked = false, setupdone = false;
	private Font font = super.font;
	public GameModeTower(int sw, int sh) {
		super(sw, sh, true);
		Corner[] corners  = GameObject.generatePeriodicObject(100,8,new Corner(new double[] {mainWidth/2,mainHeight/2-50})).getCorners();
		tower = Tower.makeNewTower(currentScreenWidth/2/screenRatio, currentScreenHeight/2/screenRatio);
		tower.setHP(TowerBaseHP);
		tower.addShotImune(p);
		addObToGame(tower, new int[] {1,3,4,6,7,9,11});
		p.addShotImune(tower);

		setLayout(null); 
		setName("TowerMode");
		super.collectMoney = true;

		try {
			Plus1Mag =  ImageIO.read(new File("src/Icons/Magazine.png"));
			Plus1Health  =  ImageIO.read(new File("src/Icons/MedKit.png"));
			DashIcon = ImageIO.read(new File("src/Icons/Dash.png"));
			MachineGunIcon = ImageIO.read(new File("src/Icons/MachineGunAmmo.png"));
			RocketIcon = ImageIO.read(new File("src/Icons/Rocket.png"));
			RocketLauncher = ImageIO.read(new File("src/Icons/RocketLauncher.png"));
			MachineGun = ImageIO.read(new File("src/Icons/MachineGun.png"));
			ShieldIcon = ImageIO.read(new File("src/Icons/ShieldIcon.png"));
			BerserkModeIcon = ImageIO.read(new File("src/Icons/BerserkModeIcon.png"));
			Shield = ImageIO.read(new File("src/Icons/Shield.png"));
			BerserkMode = ImageIO.read(new File("src/Icons/BerserkMode.png"));
			Pulse = ImageIO.read(new File("src/Icons/Pulse.png"));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BerserkModeIcon = resize(BerserkModeIcon, 30, 30);
		
		Shop = new JButton("Shop");
		Shop.setName("Shop");
		Shop.setFocusable(false);
		Shop.setBounds(currentScreenWidth-100, 0, 100, 50);
		Shop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
				remove(Shop);
				add(Resume);
				invalidate();
				revalidate();
				DisplayShop();
			}
		});
		add(Shop);
		
		TurretUpgrade1 = new JButton("");
		TurretUpgrade1.setFocusable(false);
		TurretUpgrade1.addMouseListener(this);
		
		TurretUpgrade2 = new JButton("");
		TurretUpgrade2.setFocusable(false);
		TurretUpgrade2.addMouseListener(this);
		
		TurretUpgrade3 = new JButton("");
		TurretUpgrade3.setFocusable(false);
		TurretUpgrade3.addMouseListener(this);
		
		Resume = new JButton("Resume");
		Resume.setName("Resume");
		Resume.setFocusable(false);
		Resume.setBounds(currentScreenWidth-100, 0, 100, 50);
		Resume.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				running = true;
				remove(Resume);
				RemoveShop();
				add(Shop);
				invalidate();
				revalidate();
			}
		});
		
		Power1 = new JButton("");
		Power1.addMouseListener(this);
		Power1.setName("Power1");
		Power1.setIcon(new ImageIcon(resize(Plus1Health,(int) ((currentScreenWidth/2-275)*Game.screenRatio),(int) ((currentScreenHeight-600)*Game.screenRatio))));
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
				Shop.setEnabled(true);
				 
			}
		});
		Power2 = new JButton("");
		Power2.addMouseListener(this);
		Power2.setName("Power2");
		Power2.setIcon(new ImageIcon(resize(Plus1Mag, (int) ((currentScreenWidth/2-400)*Game.screenRatio), (int) ((currentScreenHeight-600)*Game.screenRatio))));
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
				Shop.setEnabled(true);
			}
		});
		Power3 = new JButton("");
		Power3.addMouseListener(this);
		Power3.setName("Power3");
		Power3.setIcon(new ImageIcon(resize(RocketLauncher,(int) ((currentScreenWidth/2-550)*Game.screenRatio),(int) ( (currentScreenHeight-700)*Game.screenRatio))));
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
					FaceCannonReload.setMaximum((int) ((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());
					FaceCannonReload.setValue((int) ((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());
					MakeButtonText(Power3,"<html>Rocket Launcher upgrade<html>");
					add(FaceCannonReload);
				}else {
					p.upgradeFaceCanon();
					FaceCannonReload.setMaximum((int) ((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());
				}
				running = true;
				PUpicked = true;
				Shop.setEnabled(true);
			}
		});
		Power4 = new JButton("");
		Power4.addMouseListener(this);
		Power4.setName("Power4");
		Power4.setIcon(new ImageIcon(resize(MachineGun,(int) ((currentScreenWidth/2-550)*Game.screenRatio), (int) ((currentScreenHeight-700)*Game.screenRatio))));
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
					MachineGunReload.setMaximum((int) ((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
					MachineGunReload.setValue((int) ((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
					MakeButtonText(Power4,"<html>Machine Gun upgrade<html>");
					add(MachineGunReload);
				}else {
					p.upgradeMG();
					MachineGunReload.setMaximum((int) ((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
				}
				running = true;
				PUpicked = true;
				Shop.setEnabled(true);
			}
		});
		
		Power5 = new JButton("");
		Power5.addMouseListener(this);
		Power5.setName("Power5");
		Power5.setFocusable(false);
		Power5.setIcon(new ImageIcon(resize(DashIcon,(int) ((currentScreenWidth/2-550)*Game.screenRatio),(int) ((currentScreenHeight-800)*Game.screenRatio))));
		Power5.setBackground(Color.WHITE);
		Power5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { 
				removeButtons();
				invalidate();
				revalidate();
				if(!p.isDashUnlocked()) {
					MakeButtonText(Power5,"<html>Dash upgrade<html>");
					p.setDashUnlocked(true);	 
				} 
				else {
					p.upgradeDash(1);
				}
				MakeDashDisplay(0,230);
				running = true;
				PUpicked = true;
				Shop.setEnabled(true);
			}
		});
		Power6 = new JButton("");
		Power6.addMouseListener(this);
		Power6.setName("Power6");
		Power6.setFocusable(false);
		Power6.setIcon(new ImageIcon(resize(Shield,(int) ((currentScreenWidth/2-600)*Game.screenRatio),(int) ((currentScreenHeight-800)*Game.screenRatio))));
		Power6.setBackground(Color.WHITE);
		Power6.addActionListener(new ActionListener() {
			 
		
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(!p.isShieldIsUnlocked()) {	
					p.setShieldIsUnlocked(true);
					ShieldStatus.setMaximum((int) p.getShieldCooldown());
					ShieldStatus.setValue((int) p.getShieldCooldown());
					MakeButtonText(Power6, "<html>Shield upgrade<html>");
					add(ShieldStatus);
				} 

				else {
					ShieldStatus.setMaximum((int) p.getShieldCooldown());
					p.upgradeShield();
				}
				running = true;
				PUpicked = true;
				Shop.setEnabled(true);
			}
		});
		Power7 = new JButton("");
		Power7.addMouseListener(this);
		Power7.setName("Power7");
		Power7.setFocusable(false);
		Power7.setIcon(new ImageIcon(resize(Pulse,(int) ((currentScreenWidth/2-500)*Game.screenRatio),(int) ((currentScreenHeight-700)*Game.screenRatio))));
		Power7.setBackground(Color.WHITE);
		Power7.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(!p.pulseIsUnlocked) {
					MakeButtonText(Power7, "<html>Pulse upgrade<html>");
					p.setPulseUnlocked(true);
				}
				else {
					p.upgradePulse();
				}
				MakePulseDisplay(0,311);
				running = true;
				ULTpicked = true;
				PUpicked = true;
				Shop.setEnabled(true);
			}
		});
		Power8 = new JButton("");
		Power8.addMouseListener(this);
		Power8.setName("Power8");
		Power8.setFocusable(false);
		Power8.setIcon(new ImageIcon(resize(BerserkMode,(int) ((currentScreenWidth/2-500)*Game.screenRatio),(int) ((currentScreenHeight-700)*Game.screenRatio))));
		Power8.setBackground(Color.WHITE);
		Power8.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButtons();
				invalidate();
				revalidate();
				if(!p.isBerserkModeUnlocked()) {	
					p.setBerserkModeUnlocked(true);
					BerserkReloadTime.setMaximum((int) p.getBerserkModeCooldown());
					BerserkReloadTime.setValue((int) p.getBerserkModeCooldown());
					MakeButtonText(Power8, "<html>BerserkMode upgrade<html>");
					add(BerserkReloadTime);
				} 
				else {
					BerserkReloadTime.setMaximum((int) p.getBerserkModeCooldown());
					p.upgradeBerserkMode();
				}
				running = true;
				ULTpicked = true;
				PUpicked = true;
				Shop.setEnabled(true);
			}
		});
		
		setUpTextOnButtons();
		
		MoneyDisplay = new JLabel("Money: ");
		MoneyDisplay.setFont(new Font("jesus", font.BOLD, 14));
		MoneyDisplay.setBounds(currentScreenWidth-100, 30, 150, 50);
		add(MoneyDisplay);
		
		CostDisplay = new JLabel();
		CostDisplay.setFont(new Font("jesus", font.BOLD, 14));
		CostDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		
		PowerUpDisplay = new JLabel("");
		PowerUpDisplay.setFont(font);
		PowerUpDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		
		MakeAmmoDisplay(30,40);
		
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
		
		MakeHPDisplay(40,0);
		
		waveDisplay = new JLabel("Wave: " + wave);
		waveDisplay.setBounds(currentScreenWidth/2-50, 0, 150, 50);
		waveDisplay.setFont(font);
		add(waveDisplay);
		
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
		if(tower.getAttachments() != null) {
			tower.updateAllTurrets(getAIS());
		}
		handleWaves();
		nextWave();
		updateDisplay();
		DisplayPowerUps();
		endGame();
	}

	public void handleWaves() {
		if(AIneeded && AIcount == 90) {	
			AIrnd = (int) (Math.random() * ((6+1)));
			AIPowerLevel = determinePowerLvl();
			if(StrenghtAr[AIrnd]*AIPowerLevel + AIStrength> wave) {	
				return;
			}
			if(AIStrength*AIPowerLevel + StrenghtAr[AIrnd] < wave) {
				if(AIrnd == 6 && ais.length<3) {
					return;
				}
				spawnAI(AIrnd,AIPowerLevel,false);
				AIStrength += StrenghtAr[AIrnd]*AIPowerLevel;
			}
			if(AIStrength*AIPowerLevel + StrenghtAr[AIrnd] == wave && AIneeded|| AIStrength == wave && AIneeded) {
				if(AIrnd == 6 && ais.length<5) {
					return;
				}
				spawnAI(AIrnd,AIPowerLevel,false);
				AIStrength = wave;
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
	public void setUpTextOnButtons() {
		
		if(!setupdone) {
			MakeButtonText(Power1, "<html>MedKit - Fully restores your Health<html>");
			MakeButtonText(Power2,"<html>Magazine extender - Increases your ammo capacity by 1<html>");
			MakeButtonText(Power3,"<html>Rocket Launcher - RIGHT CLICK + LEFT CLICK to shoot rockets<html>");
			MakeButtonText(Power4,"<html>Machine Gun - Press SPACEBAR + LEFT CLICK to activate<html>");
			MakeButtonText(Power5,"<html>Dash - Press SHIFT to leap forward<html>");
			MakeButtonText(Power6, "<html>Shield - press F to activate<html>");
			MakeButtonText(Power7, "<html>Pulse - press Q to activate<html>");
			MakeButtonText(Power8, "<html>BerserkMode - press C to activate<html>");
			setupdone = true;
		}
	}
	public int determinePowerLvl() {
		double rand = Math.random();
		int difficulty = (int) Math.floor(wave/3)+1;
		double chunkSize = 1.0/difficulty;
		for(int powerLvl = 1; powerLvl <= difficulty; powerLvl++) {
			if(rand > (powerLvl-1)*chunkSize && rand < (powerLvl)*chunkSize) {
				return powerLvl;
			}
		}
		
		System.out.println("determinePowerLvl fucked up");
		return 1;
	}
	
	public GameObject[] getAIEnemys() {
		return new GameObject[] {p,tower};
	}
	
	public void updateDisplay() { 
		super.updateDisplay();
		MoneyDisplay.setText("Money: "+ super.money);
		waveDisplay.setText("Wave: " + wave);
		TowerHPDisplay.setValue(tower.getHP());
		TowerHPDisplay.setString(tower.getHP() + "/" + TowerBaseHP);

		if(p.machinegun!=-1) {
			if(((MagazineAttachment)p.getAttachments()[p.machinegun]).getReloadingMag()) {
				MachineGunReload.setValue((int) ((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadTimer());
			}else {
				MachineGunReload.setValue((int) ((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineReloadLenght());
			}
			MachineGunAmmoDisplay.setText(""+((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.machinegun]).getMagazineMaxSize());
		}
		if(p.faceCanon!=-1) {
			if(((MagazineAttachment)p.getAttachments()[p.faceCanon]).getReloadingMag()) {
				FaceCannonReload.setValue((int) ((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadTimer());
			}else {
				FaceCannonReload.setValue((int) ((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineReloadLenght());

			}
			RocketAmmoDisplay.setText(""+((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.faceCanon]).getMagazineMaxSize());
		}
		if(p.isShieldIsUnlocked()) {
			if(p.shieldIsUp) {
				ShieldHPDisplay.setText(""+p.getCurrentShieldHP());
				add(ShieldHPDisplay);
				ShieldStatus.setMaximum((int) p.getShield().getDuration());
				ShieldStatus.setValue((int) (p.getShield().getDuration()-p.getShield().getDurationTimer()));
			}
			else {
				remove(ShieldHPDisplay);
				ShieldStatus.setMaximum((int) p.getShieldCooldown());
				ShieldStatus.setValue((int) p.getShieldTimer());
			}
		}
		if(p.isBerserkModeUnlocked()) {
			if(p.berserkMode) {
				BerserkReloadTime.setMaximum(p.getExploWave());
				BerserkReloadTime.setValue(p.getExploWave()-p.getExploWaveCounter());
			}
			else {
				BerserkReloadTime.setMaximum((int) p.getBerserkModeCooldown());
				BerserkReloadTime.setValue((int) p.getBerserkModeTimer());
			}
		}
	}
	public void nextWave() {
		if(ais.length == 0 && wave != waveCount+1 && waveEnd) {
			wave++;
			AIneeded = true;
			AIStrength=0;
			AIcount=90;
			waveEnd = false;
		}
	}
	public void endGame() {
		if(tower.getHP()<=0 || p.getHP() <=0) {
			super.endGame();
			remove(Shop);
		}
	}
	public void deleteNoHpObs() {
		for(GameObject ob : objects) {
			if(ob.getHP() <= 0) {
				if(ob instanceof LivingObject) {
					if(((LivingObject) ob).getShield() != null) {
						removeObFromGame(((LivingObject) ob).getShield());
					}
				}
				if(ob instanceof AI && collectMoney) {
					money += ((AI)ob).getMoneyDropped();
				}
				removeObFromGame(ob);
			}
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
			Shop.setEnabled(false);
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
	public void DisplayShop(){
		ActionListener NewTurret = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.addTurret();
				money -= tower.getTurretCost();
				tower.setTurretOn(true);
				DisplayShop();
			}
		};
		ActionListener GrenadeLauncherTurret = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.upgradeToGrenadeLauncher();
				money -= tower.getNextTurretCost();
				tower.setGrenadeLauncherOn(true);
				DisplayShop();
			}
		};
		ActionListener SniperTurret = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.upgradeToSniper();
				money -= tower.getNextTurretCost();
				tower.setSniperOn(true);
				DisplayShop();
			}
		};
		ActionListener MachineGunTurret = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.upgradeToMachineGun();
				money -= tower.getNextTurretCost();
				tower.setMachineGunOn(true);
				DisplayShop();
			}
		};
		ActionListener UpgradeDMG = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.UpgradeDMG();
				money -= tower.getUpgradeCost();
				tower.setUpgradeCost(tower.getUpgradeCost()*2);
				DisplayShop();
			}
		};
		ActionListener UpgradeAccuracy = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.UpgradeAccuracy();
				money -= tower.getUpgradeCost();
				tower.setUpgradeCost(tower.getUpgradeCost()*2);
				DisplayShop();
			}
		};
		ActionListener UpgradeChunks = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.UpgradeGrenadeChunks();
				money -= tower.getUpgradeCost();
				tower.setUpgradeCost(tower.getUpgradeCost()*2);
				DisplayShop();
			}
		};
		ActionListener UpgradeMag = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.UpgradeMagazineSize();
				money -= tower.getUpgradeCost();
				tower.setUpgradeCost(tower.getUpgradeCost()*2);
				DisplayShop();
			}
		};
		ActionListener UpgradeReload = new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tower.UpgradeReloadTime();
				money -= tower.getUpgradeCost();
				tower.setUpgradeCost(tower.getUpgradeCost()*2);
				DisplayShop();
			}
		};
		MoneyDisplay.setText("Money: "+ super.money);
		if(!tower.isTurretOn()) {
			removeActionListeners(TurretUpgrade1);
			TurretUpgrade1.setName("Turret");
			TurretUpgrade1.setText("Buy Turret");
			TurretUpgrade1.setBounds(currentScreenWidth/2-75, currentScreenHeight/2-75, 150, 150);
			TurretUpgrade1.addActionListener(NewTurret);
			checkUpgrades(TurretUpgrade1, tower.getTurretCost());
			add(TurretUpgrade1);
		
		}
		if(tower.isTurretOn()&&!tower.isGrenadeLauncherOn()&&!tower.isSniperOn()&&!tower.isMachineGunOn()) {
			removeActionListeners(TurretUpgrade1);
			TurretUpgrade1.setName("NextTurret");
			TurretUpgrade1.setText("Buy GrenadeLauncher");
			TurretUpgrade1.setBounds(currentScreenWidth/2-225, currentScreenHeight/2-75, 150, 150);
			TurretUpgrade1.addActionListener(GrenadeLauncherTurret);
			checkUpgrades(TurretUpgrade1, tower.getNextTurretCost());
			add(TurretUpgrade1);
			
			removeActionListeners(TurretUpgrade2);
			TurretUpgrade2.setName("NextTurret");
			TurretUpgrade2.setText("Buy MachineGun");
			TurretUpgrade2.setBounds(currentScreenWidth/2-75, currentScreenHeight/2-75, 150, 150);
			TurretUpgrade2.addActionListener(MachineGunTurret);
			checkUpgrades(TurretUpgrade2, tower.getNextTurretCost());
			add(TurretUpgrade2);
			
			removeActionListeners(TurretUpgrade3);
			TurretUpgrade3.setName("NextTurret");
			TurretUpgrade3.setText("Buy Sniper");
			TurretUpgrade3.setBounds(currentScreenWidth/2+75, currentScreenHeight/2-75, 150, 150);
			TurretUpgrade3.addActionListener(SniperTurret);
			checkUpgrades(TurretUpgrade3, tower.getNextTurretCost());
			add(TurretUpgrade3);
			
		}
		if(tower.isGrenadeLauncherOn()) {
			removeActionListeners(TurretUpgrade1);
			TurretUpgrade1.setText("Upgrade GrenadeChunks");
			TurretUpgrade1.addActionListener(UpgradeChunks);
			add(TurretUpgrade1);
			
			removeActionListeners(TurretUpgrade2);
			TurretUpgrade2.setText("Upgrade ReloadTime");
			TurretUpgrade2.addActionListener(UpgradeReload);
			add(TurretUpgrade2);
			
			removeActionListeners(TurretUpgrade3);
			TurretUpgrade3.setText("Upgrade DMG");
			TurretUpgrade3.addActionListener(UpgradeDMG);
			add(TurretUpgrade3);
			
			
		}
		if(tower.isSniperOn()) {
			removeActionListeners(TurretUpgrade1);
			TurretUpgrade1.setText("Upgrade Accuracy");
			TurretUpgrade1.addActionListener(UpgradeAccuracy);
			add(TurretUpgrade1);
			
			removeActionListeners(TurretUpgrade2);
			TurretUpgrade2.setText("Upgrade ReloadTime");
			TurretUpgrade2.addActionListener(UpgradeReload);
			add(TurretUpgrade2);
			
			removeActionListeners(TurretUpgrade3);
			TurretUpgrade3.setText("Upgrade DMG");
			TurretUpgrade3.addActionListener(UpgradeDMG);
			add(TurretUpgrade3);
			
		}
		if(tower.isMachineGunOn()) {
			removeActionListeners(TurretUpgrade1);
			TurretUpgrade1.setText("Upgrade Magazine size");
			TurretUpgrade1.addActionListener(UpgradeMag);
			add(TurretUpgrade1);
			
			removeActionListeners(TurretUpgrade2);
			TurretUpgrade2.setText("Upgrade ReloadTime");
			TurretUpgrade2.addActionListener(UpgradeReload);
			add(TurretUpgrade2);
			
			removeActionListeners(TurretUpgrade3);
			TurretUpgrade3.setText("Upgrade DMG");
			TurretUpgrade3.addActionListener(UpgradeDMG);
			add(TurretUpgrade3);
		
		}
		if(tower.isTurretOn()&&tower.isSniperOn()||tower.isTurretOn()&&tower.isGrenadeLauncherOn()||tower.isTurretOn()&&tower.isMachineGunOn()) {
			TurretUpgrade1.setName("Upgrade");
			TurretUpgrade2.setName("Upgrade");
			TurretUpgrade3.setName("Upgrade");
			checkUpgrades(TurretUpgrade1, tower.getUpgradeCost());
			checkUpgrades(TurretUpgrade2, tower.getUpgradeCost());
			checkUpgrades(TurretUpgrade3, tower.getUpgradeCost());
		}
		repaint();
	}
	public void checkUpgrades(JButton b, int cost) {
		if(money >= cost) {
			b.setEnabled(true);
		}
		else {
			b.setEnabled(false);
		}
	}
	public void removeActionListeners(JButton b) {
		    for( ActionListener al : b.getActionListeners() ) {
		        b.removeActionListener( al );
		    }
	}
	public void RemoveShop() {
		remove(TurretUpgrade1);
		remove(TurretUpgrade2);
		remove(TurretUpgrade3);
	}
	public void MakeButtonText(JButton b, String text) {
		for( ChangeListener cl : b.getChangeListeners() ) {
	        b.getModel().removeChangeListener( cl );
	    }
		String textDisplay = text;
		b.getModel().addChangeListener(new ChangeListener() {		
			@Override
		public void stateChanged(ChangeEvent e) {
				System.out.println(b.getModel().isRollover());
		    if (b.getModel().isRollover()) {
		    	PowerUpDisplay.setBounds(b.getX(), b.getY()+b.getHeight(), b.getWidth(), 70);
				PowerUpDisplay.setText(textDisplay);
				System.out.println(textDisplay);
				System.out.println(b.getChangeListeners().length);
				add(PowerUpDisplay);
				repaint();
		    	}
		    else {
		    	remove(PowerUpDisplay);
		    	repaint();
		    	}
			}
		});	
	}
	 @Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(p.machinegun!=-1) {	
			g2.drawImage(MachineGunIcon, 0, 80, 30, 30,null);
		}
		if(p.faceCanon!=-1) {
			g2.drawImage(RocketIcon, 0, 121, 30, 30,null);
		}
		
		if(p.isShieldIsUnlocked()) {
			g2.drawImage(ShieldIcon, 0,161,30,30,null);
		}
		if(p.isBerserkModeUnlocked()) {
			g2.drawImage(BerserkModeIcon,25,241,30,30,null);
		}
		
	}
	 
	 @Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseEntered(e);
		switch(e.getComponent().getName()) {
			case "TowerMode" : 
				remove(PowerUpDisplay);
				remove(CostDisplay);
				repaint();
				break;
			
			case "Turret" :
				CostDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 50);
				CostDisplay.setText("" + tower.getTurretCost());
				add(CostDisplay);
				repaint();
				break;
			case "NextTurret" :
				CostDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 50);
				CostDisplay.setText("" + tower.getNextTurretCost());
				add(CostDisplay);
				repaint();
				break;
			case "Upgrade" :
				CostDisplay.setBounds(e.getComponent().getX(), e.getComponent().getY()+e.getComponent().getHeight(), e.getComponent().getWidth(), 50);
				CostDisplay.setText("" + tower.getUpgradeCost());
				add(CostDisplay);
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
