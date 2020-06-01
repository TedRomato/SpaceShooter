
package package1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class Game extends JPanel implements MouseListener{

	static int mainHeight = 1908, mainWidth = 3392;
	static int baseTicks = 60;
	static int currentTicks = 300;
	static double tickMultiply = (double)baseTicks/(double)currentTicks;
	static double tickOne = 1*tickMultiply;
	protected Player p;
	protected Font font = new Font("josef", Font.PLAIN, 25);
	private Hunter ht;
	private Grenader gr;
	private HuntingMine hm;
	private Mothership mp;
	private SpaceCanon sca;
	private SpaceCruiser scr;
	private Shielder sh;
	public static JLabel scoreDisplay, Warning;
	 BufferedImage WarningSign;
	 BufferedImage bg;
	protected int score = 0;
	int[] aiSpawningRange = new int[] {600,1000};
	private JLabel PlayerHPDisplay, PlayerAmmoDisplay,GameOverDisplay;
	private JProgressBar PlayerReloadTime, PulseReloadTime, DashRefill;
	private BufferedImage HealthIcon, AmmoIcon, PulseIcon, DashRefillIcon;
	private boolean ShowScore;
	private Corner spawnCorner;
	public static int currentScreenWidth;
	public static int currentScreenHeight;
	public static double screenRatio;
	 RandomMeteorGenerator randomMeteorGenerator = new RandomMeteorGenerator();
	public static KeyChecker keyChecker = new KeyChecker();
	public static Camera camera;
	 GameObject[] borders;
	protected GameObject[] objects;
	 MovingObject[] reflectableObs;
	 GameObject[] reflectingObs;
	 LivingObject[] livingObsReflectUpdate;
	 LivingObject[] shootingObs; 
	 MovingObject[] borderSensitive;
	protected AI[] ais;
	protected GameObject[][] arrayList;
	 GameObject[] aiVisible;
	 GameObject[] aiEnemys;
	 boolean WasCalled = false;
	 Meteor[] meteors;
	 Summoner[] summoners;
	 Explosives[] explosives;
	boolean softBorders = false;
	Corner safeZoneCorner = new Corner(new double[] {0,0});
	int safeZoneWidth = mainWidth;
	int safeZoneHeight = mainHeight;
	int removeSquareBlock = 1500;
	int removeSquareCornerX = 0-removeSquareBlock, removeSquareCornerY = 0-removeSquareBlock, removeSquareWidth = safeZoneWidth + (removeSquareBlock * 2), removeSquareHeight = safeZoneHeight + (removeSquareBlock * 2);
	int  spawnBlockHeight = safeZoneHeight, spawnBlockWidth = safeZoneWidth;
	Corner spawnBlockCorner = new Corner(new double[] {0,0});;
	int[] spawnBlockRange = new int[] {600,800};
	protected int money = 10000;
	protected boolean collectMoney = false;
	boolean GameOver = false;
	private boolean wasCalled = false;
	//public static JPanel gp = new GamePanel();
	public static boolean running = false;
	private int Count = 0;
	
	public Game(int sw,int sh,boolean softBorder) {
		this.setBackground(Color.black);
		this.currentScreenHeight = sh;
		this.currentScreenWidth = sw;
		this.softBorders = softBorder;
		try {
			DashRefillIcon = ImageIO.read(new File("src/Icons/DashRefillIcon.png"));
			AmmoIcon =  ImageIO.read(new File("src/Icons/AmmoIcon.png"));
			HealthIcon = ImageIO.read(new File("src/Icons/HealthIcon.png"));
			PulseIcon = ImageIO.read(new File("src/Icons/PulseIcon.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		objects = new GameObject[] {};
	    reflectableObs = new MovingObject[] {};
	    reflectingObs = new MovingObject[] {};
	    livingObsReflectUpdate = new LivingObject[] {};
	    borderSensitive = new MovingObject[] {};
	    aiVisible = new GameObject[] {};
	    shootingObs = new LivingObject[] {};
	    ais = new AI[] {};
	    meteors = new Meteor[] {};
	    summoners = new Summoner[] {};
		aiEnemys = new GameObject[] {};
		explosives = new Explosives[] {};
	    arrayList = new GameObject[][] {objects, reflectableObs, reflectingObs, livingObsReflectUpdate, borderSensitive, aiVisible, ais, meteors, shootingObs,summoners, aiEnemys,explosives};
	    
	    Warning = new JLabel("");
	    Warning.setForeground(Color.RED);
	    Warning.setFont(new Font("Karel",Font.BOLD,60));
	    Warning.setBounds(currentScreenWidth/2-160, currentScreenHeight/2-200,600,120);
	    
	    PulseReloadTime = new JProgressBar(0,0);
	    DashRefill = new JProgressBar(0,0);
	    
	    GameOverDisplay = new JLabel("GAME OVER");
		GameOverDisplay.setForeground(Color.RED);
		GameOverDisplay.setFont(new Font("Karel",Font.BOLD,150));
		GameOverDisplay.setBounds(currentScreenHeight/2-50,currentScreenHeight/2-300,1000,300);
		
	    try {
			WarningSign = ImageIO.read(new File("src/Icons/Warning.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	    try {
			bg = ImageIO.read(new File("src/BG/SpaceShooterBG.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	    
	    addMouseListener(this);
	    
	    if(softBorder == false) {
	    	Corner rightBotC = new Corner(new double[] {mainWidth,mainHeight}, new double[] {500,400});
		    Corner leftBotC = new Corner(new double[] {0,mainHeight}, new double[] {500,400});
		    Corner rightTopC = new Corner(new double[] {mainWidth,0}, new double[] {500,400});
		    Corner leftTopC = new Corner(new double[] {0,0}, new double[] {500,400});
		    
		    GameObject rightBorder = new GameObject(new Corner[] {rightTopC, rightBotC}, new double[] {500,400}, 0);
		    GameObject leftBorder = new GameObject(new Corner[] {leftTopC, leftBotC}, new double[] {500,400}, 0);
		    GameObject topBorder = new GameObject(new Corner[] {rightTopC, leftTopC}, new double[] {500,400}, 0);
		    GameObject botBorder = new GameObject(new Corner[] {leftBotC, rightBotC}, new double[] {500,400}, 0);
		    
		    borders = new GameObject[] {botBorder,leftBorder,topBorder,rightBorder};
	    }	    	    
	    screenRatio = (double)currentScreenWidth/(double)mainWidth;
		camera = new Camera(currentScreenWidth,currentScreenHeight,1);
		camera.setCameraToCorner(new Corner(new double[] {mainWidth/2,mainHeight/2}));
		
	    p = Player.makeNewPlayer(new double[] {100,100});
		addObToGame(p, new int[] {5,6,7,9,11}); 
		
		camera.setCameraToCorner(p.getRotationPoint());

		
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void keyTyped(KeyEvent e) {
		
	}
	public void keyPressed(KeyEvent e) {
		if(e != null) {
			keyChecker.keyPressed(e.getKeyCode());
		}
		
	}
	public void keyReleased(KeyEvent e) {
		if(e != null) {
			keyChecker.keyReleased(e.getKeyCode());
		}
	}
	
	
	public void start() {
		long lastTime = System.nanoTime();
        double amountOfTicks = currentTicks;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
        	long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
      
            while(delta >=1)
            	{
                tick();
                delta--;
                }
            	this.repaint();
                frames++;
                if(System.currentTimeMillis() - timer > 1000)
                { 
                	timer += 1000;
           //         System.out.println("FPS: "+ frames);

                    frames = 0;
                    }
        }
	}
	public void stop() {
		if(running) {
			running = false;	
		}
	}
	
	public void tick() {
		updatePlayer();
		handleShooting();	
		checkAndHandleCollision();
    	updateLivingObsReflect();
    	checkAndHandleAllRefs();
    	updateAllInvs();
        reflectFromSides();
        removeObsOut();
		updateAllObs();
		handleAis();
		handleSummoners();
		handleExplosives();
        deleteNoHpObs();
        updateDisplay();
	}
	
	public void updatePlayer() {
		handleShields();
		handlePulse();
		handleBerserkModes();
		handlePlayerOutsideSafeZone();
		p.handlePlayerKeys();
		updatePlayerAimPoint();
		p.updatePlayer();

	}
	
	public void handlePulse() {
		if(p.isPulse()) {
			p.usePulse(objects);
			p.setPulse(false);
		}
	}
	/*
	public void activateShieldFor(GameObject go, int radius, int HP) {
		Shield s = Shield.makeShield(go.getRotationPoint(), radius);
		s.setHP(HP);
		s.setUpShield(true, new GameObject[] {}, p);
		addObToGame(s, new int[] {1,2,3,4,5,6,7,8,9,10,11});
	}
	*/
	
	//Overload  tower modu
	public void handleShields() {
		for(AI ai : ais) {
			if(ai.activateShield == true && ai.getShield() == null) {
				addObToGame(ai.useShield(getAIEnemys()), new int[] {1,3,4,5,6,7,8,9,10,11});
				ai.activateShield = false;
			}
			
		}
		if(p.activateShield && p.getShield() == null) {
			addObToGame(p.useShield(), new int[] {1,3,4,5,6,7,8,9,10,11});
			p.activateShield = false;
		}
	}
	
	
	
	public GameObject[] getAIEnemys() {
		return new GameObject[] {p};
	}
	
	public void handleBerserkModes() {
		Missile[] missiles;
		missiles = p.handleBereserkMode();
		if(missiles != null) {
			for(Missile m : missiles) {
				addObToGame(m, new int[] {1,2,3,4,6,7,8,9,10,11});
			}
		}
	}
	
	public void handleExplosives() {
		for(Explosives explo : explosives) {

			explo.updateExplosive();
			if(explo.getShouldExplode()) {
				Missile[] m = explo.explode();
				if(m!=null) {
					for(Missile mis : m) {
						addObToGame(mis, new int[] {1,2,3,4,6,7,8,9,10,11});
					}
				}
			}
		
		}
	}
	public void endGame() {
		stop();
		GameOver = true;
		remove(Warning);
		add(GameOverDisplay);
		add(Window.MainMenu);
	}
	public void MakePulseDisplay(int x, int y) {
		if(p.pulseIsUnlocked&&!PulseReloadTime.isShowing()) {
			PulseReloadTime.setBounds(x,y,80,10);
			PulseReloadTime.setForeground(Color.MAGENTA);
		
			PulseReloadTime.setMaximum((int) p.getPulseCooldown());
			PulseReloadTime.setValue((int) p.getPulseCooldown());
			add(PulseReloadTime);
		} 
		else{
			PulseReloadTime.setMaximum((int) p.getPulseCooldown());
		}
	}
	public void MakeDashDisplay(int x, int y) {
		if(p.dashUnlocked&&!DashRefill.isShowing()) {
			
			DashRefill.setBounds(x, y, 80, 10);
			DashRefill.setForeground(new Color(225,174,19));
			
			DashRefill.setMaximum((int) p.getDashCooldown());
			DashRefill.setValue((int) p.getDashCooldown());
			add(DashRefill);
		}
		else {
			DashRefill.setMaximum((int) p.getDashCooldown());
		}
	}
	public void MakeHPDisplay(int x, int y) {
		PlayerHPDisplay = new JLabel(""+ p.getHP());
		PlayerHPDisplay.setBounds(x,y,30,30);
		PlayerHPDisplay.setFont(font);
		PlayerHPDisplay.setForeground(new Color(141,198,63));
		add(PlayerHPDisplay);
	}
	public void MakeAmmoDisplay(int x, int y) {
		PlayerAmmoDisplay = new JLabel(""+ ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineMaxSize());
		PlayerAmmoDisplay.setBounds(x , y,50,30);
		PlayerAmmoDisplay.setFont(font);
		add(PlayerAmmoDisplay);
		
		PlayerReloadTime = new JProgressBar(0,(int) ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadLenght());
		PlayerReloadTime.setBounds(x-30, y+30, 80, 10);
		PlayerReloadTime.setValue((int) ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadLenght());
		PlayerReloadTime.setForeground(Color.BLACK);
		add(PlayerReloadTime);
	}
	public void updateDisplay() {
		if(p.checkIfOutsideRect((int)safeZoneCorner.getX(), (int)safeZoneCorner.getY(), safeZoneWidth, safeZoneHeight)) {
			Warning.setText("WARNING!");
			add(Warning);
		}
		else {
			remove(Warning);
		}
		if(p.isDashUnlocked()) {
			DashRefill.setValue((int) p.getDashCooldownTimer());
		}
		if(p.isPulseUnlocked()) {
			PulseReloadTime.setValue((int) p.getPulseCooldownTimer());
		}
		PlayerAmmoDisplay.setText("" + ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineSize()+"/"+((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineMaxSize());
		if(((MagazineAttachment)p.getAttachments()[p.baseCanon]).getReloadingMag()) {
			PlayerReloadTime.setValue((int) ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadTimer());
		}else  {
			PlayerReloadTime.setValue((int) ((MagazineAttachment)p.getAttachments()[p.baseCanon]).getMagazineReloadLenght());
		}
		PlayerHPDisplay.setText(""+p.getHP());
	}
	protected void handlePlayerOutsideSafeZone() {
		if(p.wasDamagedByZone == false) {
			if(p.getRotationPoint().getY() < safeZoneCorner.getY()) {
				p.setHP(p.getHP() - getHPToSubtract(getDifference(p.getRotationPoint().getY(), safeZoneCorner.getY())));
				p.wasDamagedByZone = true;
			}else if(p.getRotationPoint().getY() > safeZoneCorner.getY() + safeZoneHeight) {
				p.setHP(p.getHP() - getHPToSubtract(getDifference(p.getRotationPoint().getY(),safeZoneCorner.getY() + safeZoneHeight)));
				p.wasDamagedByZone = true;
			}
			if(p.getRotationPoint().getX() <  safeZoneCorner.getX()) {
				p.setHP(p.getHP() - getHPToSubtract(getDifference(p.getRotationPoint().getX(), safeZoneCorner.getX())));
				p.wasDamagedByZone = true;

			}else if(p.getRotationPoint().getX() > safeZoneCorner.getX() + safeZoneWidth) {
				p.setHP(p.getHP() - getHPToSubtract(getDifference(p.getRotationPoint().getX(),safeZoneCorner.getX() + safeZoneWidth)));
				p.wasDamagedByZone = true;

			}
		}
	}
	
	protected double getDifference(double xy, double widthHeight) {
		return Math.abs(xy-widthHeight);
	}
	
	private int getHPToSubtract(double i) {
		return (int) (i/50);
	}
	public void updatePlayerAimPoint() {
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		int x = (int) b.getX();
		int y = (int) b.getY();
		p.setPlayerAimCorner((x+camera.getX()*camera.getZoom())*(double)mainWidth/(double)currentScreenWidth/camera.getZoom(), (y+camera.getY()*camera.getZoom())*(double)mainWidth/(double)currentScreenWidth/camera.getZoom());
	}
	
	private void handleAis() {
		for(AI ai : ais) {
			ai.updateAI(aiEnemys, aiVisible, ais);
		}
	}
	
	private void handleShooting(){
		for(LivingObject sob : shootingObs ) {	
			if(sob.getAttachments() != null && sob.getAttachments().length > 0 && sob.getIsStunned() == false) {
				for(ObjectAttachment att : sob.getAttachments()) {
					if(att instanceof MagazineAttachment) {
						((MagazineAttachment) att).handleMagazine();
					}
					if(att instanceof InteractiveAttachment) {
						if(att instanceof ExplosiveShootingAtt) {
							if(sob instanceof Player) {
								if(att.getReloadTimer() >= att.getReloadLenght() &&  att.shouldShoot()) {
									
									addObToGame(((ExplosiveShootingAtt) att).Fire(sob.getShotImunes()),new int[] {5,6,7,9,10});
									att.setReloadTimer(0);
								}
							}
							else if(att.getReloadTimer() >= att.getReloadLenght() && att.shouldShoot(att.getAimCorner())) {
								addObToGame(((ExplosiveShootingAtt) att).Fire(sob.getShotImunes()),new int[] {5,6,7,9,10});
								att.setReloadTimer(0);
							}
						}
						else if(sob instanceof AI || sob instanceof Tower) {
							if(att.getReloadTimer() >= att.getReloadLenght() && att.shouldShoot(att.getAimCorner())) {
								addObToGame(att.shoot(sob.getShotImunes()), new int[] {1,2,3,4,5,6,7,8,9,10,11});
								att.setReloadTimer(0);
							}
						}
						else if(sob instanceof SpecialCharge) {
							if(att.getReloadTimer() >= att.getReloadLenght() && att.shouldShoot()) {
								addObToGame(att.shoot(sob.getShotImunes()), new int[] {1,2,3,4,5,6,7,8,9,10,11});
								att.setReloadTimer(0);
							}
						}
						else if(att.getReloadTimer() >= att.getReloadLenght() && att.shouldShoot()) {
							addObToGame(att.shoot(sob.getShotImunes()), new int[] {1,2,3,4,5,6,7,8,9,10,11});
							att.setReloadTimer(0);
						}
						if(att.getReloadTimer() != att.getReloadLenght()) { 
							att.setReloadTimer(att.getReloadTimer()+Game.tickOne);
						}
					}
				}
			}
		}
	}

	protected void removeObsOut() {
		for(GameObject ob : objects) {
			if(ob.checkIfOutsideRect(removeSquareCornerX, removeSquareCornerY,removeSquareWidth, removeSquareHeight)) {
				removeObFromGame(ob);
			}
		}
	}
	
	public void spawnAI(int AI, int PL, boolean playerFocus) {
		spawnCorner = GameObject.generateCornerOutsideMapInRange(spawnBlockCorner,mainWidth, mainHeight, aiSpawningRange);
		switch(AI){

			case 0 : hm = HuntingMine.makeNewHuntingMine(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys(),PL); 
				addObToGame(hm, new int[] {4,7,9,10,11}); 
			break;

			case 1 : sca = SpaceCanon.makeNewSpaceCanon(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys(),PL); 
				sca.setPlayerFocus(playerFocus);
				addObToGame(sca, new int[] {4,7,9,10,11}); 
			break;

			case 2 : mp = Mothership.makeNewMothership(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys(),PL);
				addObToGame(mp, new int[] {4,7,10,11}); 
			break;

			case 3 : scr = SpaceCruiser.makeNewSpaceCruiser(spawnCorner.getX(), spawnCorner.getY(),getAiEnemys(),PL); 
				scr.setPlayerFocus(playerFocus);
				addObToGame(scr, new int[] {4,7,9,10,11}); 
			break;
			
			case 4 : ht = Hunter.makeNewHunter(spawnCorner.getX(), spawnCorner.getY(), getAiEnemys(),PL); addObToGame(ht, new int[] {4,7,9,10,11}); 
			break;
			
			case 5 : gr = Grenader.makeNewGrenader(spawnCorner.getX(), spawnCorner.getY(), getAiEnemys(),PL); 
				gr.setPlayerFocus(playerFocus);
				addObToGame(gr, new int[] {4,7,9,10,11}); 
			break;
			case 6 : sh = Shielder.makeShielder((int)spawnCorner.getX(), (int)spawnCorner.getY(), getAiEnemys(),PL);
					 addObToGame(sh, new int[] {4,7,9,10,11});
			default : 
		}
	}
	
	private void updateLivingObsReflect() {
		if(livingObsReflectUpdate != null) {	 
			for(LivingObject livingOb : livingObsReflectUpdate) {
				livingOb.updateReflection();
			}
		}
	}
	
    	
	//handle reflections from objects with attachments
	private void checkAndHandleAllRefs() {
		for(MovingObject mob : reflectableObs) {
			for(GameObject ob : reflectingObs) {
				if(mob != ob) {
					mob.checkAndHandleReflect(ob);
					
				}
			}
		}
	}
	
	private void checkAndHandleCollision() {
		GameObject[] compareArray = objects; 
		for(int i = 0; i < objects.length; i++) {
			for(int x = 0; x < compareArray.length; x++) {
				
				if(objects[i] != compareArray[x]) {
					
					if(objects[i].checkCollision(compareArray[x])) {
					//	if(objects[i].getInvulnurability() == false) {
							objects[i].handleCollision(compareArray[x]);
					//		}
								
						}
					}
				}
	
			}
		}
		
	
	
	private void updateAllInvs() {
		for(GameObject ob : objects) {
			ob.updateInvulnurability();
		}
	}
	
	//Deletes all GameObjects with <= 0 HP
	
	public void deleteNoHpObs() {
		for(GameObject ob : objects) {
			if(ob.getHP() <= 0) {
				if(ob instanceof LivingObject) {
					if(((LivingObject) ob).getShield() != null) {
						removeObFromGame(((LivingObject) ob).getShield());
					}
				}
				removeObFromGame(ob);
			}
		}
		
	}
	
	private void reflectFromSides() {
		if(softBorders == false) {
			for(int i = 0; i < borders.length; i++) {
				for(MovingObject go : borderSensitive) {
					if(go instanceof Meteor) {
						if(go.checkCollision(borders[i])) {
							((Meteor) go).reflectMeteorFromSide(i,go.getRotationPoint());
						}
					}else {
						go.checkAndHandleReflect(borders[i]);
		
					}
				}
			}
		}
	}
	
	protected void handleSummoners() {
		for(Summoner s : summoners) {
			AI ai = s.handleSummoner(getAiEnemys());
			if(ai != null) {
				addObToGame(ai, new int[] {7,4,9,10,11});
			}
		}
	}
	
	protected void respawnMeteorsToAmount(int amount) {
		if(meteors.length < amount) {
			boolean done = false;
			Meteor m = null;
			while(!done) {
				m = randomMeteorGenerator.generateRandomMeteorOutside(spawnBlockWidth, spawnBlockHeight, spawnBlockCorner, spawnBlockRange);
				done = checkIfSpawnCollision(m);
			}
			addObToGame(m, new int[] {3,6,4,8,9,11});
		}
	}
	
	public boolean checkIfSpawnCollision(GameObject t) {
		for(GameObject g : objects) {
			if(t.getCollisionSquare().squareCollision(g.getCollisionSquare())) {
				return false;
			}
		}
		return true;
	}
	
	
	protected void addObToGame(GameObject ob) {
		for(int i = 0; i < arrayList.length; i++) {
			arrayList[i] =  makeNewArrayWith(arrayList[i], ob);
		}
		fixGameArrays();
	}
	
	protected void addObToGame(GameObject ob,int[] exclude) {
		for(int i = 0; i < arrayList.length; i++) {
			boolean excludedArr = false;
			for(int x = 0; x < exclude.length; x++){
				if(exclude[x] == i) {
					excludedArr = true;
				}
			}
			if(excludedArr == false) {
				arrayList[i] =  makeNewArrayWith(arrayList[i], ob);
			}
		}
		fixGameArrays();
	}
	
	
	protected void removeObFromGame(GameObject ob){
		for(int i = 0; i < arrayList.length; i++) {
			for(int index = 0; index < arrayList[i].length; index++) {
				if(arrayList[i][index] == ob) {
					arrayList[i] =  makeNewArrayWithout(arrayList[i], index);
				}
			}
		}
		fixGameArrays();
		
	}
	
	private void fixGameArrays() {
		objects = arrayList[0];
	    reflectableObs = makeGameObArMovingArr(arrayList[1]);
	    reflectingObs = arrayList[2];
	    livingObsReflectUpdate = makeGameObArLivingArr(arrayList[3]);
	    borderSensitive = makeGameObArMovingArr(arrayList[4]);
	    aiVisible = arrayList[5];
	    ais = makeGameObArAIArr(arrayList[6]);
	    meteors = makeMeteorArr(arrayList[7]);
	    shootingObs = makeGameObArLivingArr(arrayList[8]);
	    summoners = makeSummonersObAr(arrayList[9]);
	    aiEnemys = arrayList[10];
	    explosives = makeExplosivesObAr(arrayList[11]);
	}
	
	private Explosives[] makeExplosivesObAr(GameObject[] arr) {
		Explosives[] newArr = new Explosives[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (Explosives) arr[i];
		}
		return newArr;
	}
	
	private Summoner[] makeSummonersObAr(GameObject[] arr) {
		Summoner[] newArr = new Summoner[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (Summoner) arr[i];
		}
		return newArr;
	}
	
	private Meteor[] makeMeteorArr(GameObject[] arr) {
		Meteor[] newArr = new Meteor[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (Meteor) arr[i];
		}
		return newArr;
	}
	private AI[] makeGameObArAIArr(GameObject[] arr) {
		AI[] newArr = new AI[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (AI) arr[i];
		}
		return newArr;
	}
	
	protected MovingObject[] makeGameObArMovingArr(GameObject[] arr) {
		MovingObject[] newArr = new MovingObject[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (MovingObject) arr[i];
		}
		return newArr;
	}
	private LivingObject[] makeGameObArLivingArr(GameObject[] arr) {
		LivingObject[] newArr = new LivingObject[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (LivingObject) arr[i];
		}
		return newArr;
	}
	
	protected Grenade[] makeGamObArGrenadeAr(GameObject[] arr) {
		Grenade[] newArr = new Grenade[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (Grenade) arr[i];
		}
		return newArr;
	}
	
	protected Tower[] makeGamObArTowerAr(GameObject[] arr) {
		Tower[] newArr = new Tower[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (Tower) arr[i];
		}
		return newArr;
	}
	
	protected GameObject[] makeNewArrayWith(GameObject[] arr, GameObject ob) {
		if(ob != null) {
			GameObject[] newArray = new GameObject[arr.length+1];
			for(int i = 0; i < arr.length;i++) {
				newArray[i] = arr[i];
			}
			newArray[newArray.length-1] = ob;
			return newArray;
		}
		return arr;
		
	}
		
	protected GameObject[] makeNewArrayWithout(GameObject[] array ,int index){
		GameObject[] newArray = new GameObject[array.length - 1];
		
		int add = 0;
		for(int i = 0; i < newArray.length; i++) {
			if(i == index) {
				add = 1;
			}
			newArray[i] = array[i + add];
		}
		return newArray;
	}
	
	
	private void updateAllObs() {
		for(GameObject go : objects) {
			go.updateOb();
		}
	}
	
	public static void MakeTransparentButton(JButton b) {
		b.setOpaque(false);
		b.setContentAreaFilled(false);
		b.setBorderPainted(false);
	}
	
	private void renderAll(Graphics g) {
		if(softBorders) {
			renderDangerZone(g);
		}
		if(objects != null) {
			if(objects.length > 0) {
				for(GameObject ob : objects) {
					ob.render(g);
				}
			}
		}
		if(softBorders == false) {
			for(GameObject ob : borders) {
				ob.render(g);
			}
		}	
	}
	
	private void renderDangerZone(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect((int) Math.round(safeZoneCorner.getX()*Game.camera.toMultiply() + Game.camera.toAddX()), (int)Math.round(safeZoneCorner.getY()*Game.camera.toMultiply() + Game.camera.toAddY()),(int)Math.round(safeZoneWidth*Game.camera.toMultiply()),(int) Math.round(safeZoneHeight*Game.camera.toMultiply()));
		g.drawRect((int) Math.round(safeZoneCorner.getX()*Game.camera.toMultiply() + Game.camera.toAddX()-1), (int)Math.round(safeZoneCorner.getY()*Game.camera.toMultiply() + Game.camera.toAddY()-1),(int)Math.round(safeZoneWidth*Game.camera.toMultiply()+2),(int) Math.round(safeZoneHeight*Game.camera.toMultiply())+2);
		g.drawRect((int) Math.round(safeZoneCorner.getX()*Game.camera.toMultiply() + Game.camera.toAddX()-2), (int)Math.round(safeZoneCorner.getY()*Game.camera.toMultiply() + Game.camera.toAddY()-2),(int)Math.round(safeZoneWidth*Game.camera.toMultiply()+4),(int) Math.round(safeZoneHeight*Game.camera.toMultiply())+4);
		g.drawRect((int) Math.round(safeZoneCorner.getX()*Game.camera.toMultiply() + Game.camera.toAddX()-3), (int)Math.round(safeZoneCorner.getY()*Game.camera.toMultiply() + Game.camera.toAddY()-3),(int)Math.round(safeZoneWidth*Game.camera.toMultiply()+6),(int) Math.round(safeZoneHeight*Game.camera.toMultiply())+6);

		g.setColor(Color.BLACK);
		g.fillRect((int) Math.round(safeZoneCorner.getX()*Game.camera.toMultiply() + Game.camera.toAddX()), (int)Math.round(safeZoneCorner.getY()*Game.camera.toMultiply() + Game.camera.toAddY()),(int)Math.round(safeZoneWidth*Game.camera.toMultiply()),(int) Math.round(safeZoneHeight*Game.camera.toMultiply()));
		g.setColor(Color.black);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(HealthIcon, 0, 0, 30, 30,null);
		g2.drawImage(AmmoIcon,0,40,30,30,null);
	}
	
	
	public GameObject[] getAiEnemys() {
		return aiEnemys;
	}
	
	
	public Dimension getPreferredSize() {
		return new Dimension(1920,1080);
	}
	public static void rotateImage(Graphics2D g,BufferedImage img,double ra, Corner rp, int width, int height, int rpX, int rpY) {
		AffineTransform trans = new AffineTransform();
		trans.rotate(Math.toRadians(ra),(rp.getX()*Game.camera.toMultiply() + Game.camera.toAddX()),(int)(rp.getY()*Game.camera.toMultiply() + Game.camera.toAddY()));
		AffineTransform old = g.getTransform();
		g.transform(trans);
		g.drawImage(resize(img,(int)(width*Game.screenRatio),(int)(height*Game.screenRatio)),(int)((rp.getX()-rpX)*Game.camera.toMultiply() + Game.camera.toAddX()),(int)((rp.getY()-rpY)*Game.camera.toMultiply() + Game.camera.toAddY()),null);
		g.setTransform(old);
	}
	public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
	@Override
	protected void paintComponent(Graphics g) {
		renderDangerZone(g);
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.drawImage(bg,(int)((-1920*Game.camera.toMultiply()) + Game.camera.toAddX()), (int)((-3000*Game.camera.toMultiply()) + Game.camera.toAddY()),5760,3240,null);
		renderAll(g2);

		if(p.checkIfOutsideRect((int)safeZoneCorner.getX(), (int)safeZoneCorner.getY(), safeZoneWidth, safeZoneHeight)&&!GameOver) {
			g2.drawImage(WarningSign,currentScreenWidth/2-260, currentScreenHeight/2-200,100,100, null);
			g2.drawImage(WarningSign,currentScreenWidth/2+150, currentScreenHeight/2-200,100,100, null);
		}
		if(p.isPulseUnlocked()) {	
			g2.drawImage(PulseIcon, PulseReloadTime.getX()+25,PulseReloadTime.getY()-30,30,30,null);
		}
		if(p.isDashUnlocked()) {
			g2.drawImage(DashRefillIcon, DashRefill.getX()+20, DashRefill.getY()-30, 40, 30,null);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			keyChecker.setLeftMousePressed(true);
		}
		if(e.getButton() == 3) {
			keyChecker.setRightMousePressed(true);
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == 1) {
			keyChecker.setLeftMousePressed(false);
		}
		if(e.getButton() == 3) {
			keyChecker.setRightMousePressed(false);
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	public AI[] getAIS() {
		return ais;
	}
	
	public int getRemoveSquareBlock() {
		return removeSquareBlock;
	}
	public void setRemoveSquareBlock(int removeSquareBlock) {
		this.removeSquareBlock = removeSquareBlock;
	}
	public int getRemoveSquareCornerX() {
		return removeSquareCornerX;
	}
	public void setRemoveSquareCornerX(int removeSquareCornerX) {
		this.removeSquareCornerX = removeSquareCornerX;
	}
	public int getRemoveSquareCornerY() {
		return removeSquareCornerY;
	}
	public void setRemoveSquareCornerY(int removeSquareCornerY) {
		this.removeSquareCornerY = removeSquareCornerY;
	}
	public int getRemoveSquareWidth() {
		return removeSquareWidth;
	}
	public void setRemoveSquareWidth(int removeSquareWidth) {
		this.removeSquareWidth = removeSquareWidth;
	}
	public int getRemoveSquareHeight() {
		return removeSquareHeight;
	}
	public void setRemoveSquareHeight(int removeSquareHeight) {
		this.removeSquareHeight = removeSquareHeight;
	}
	
	public int getSpawnBlockHeight() {
		return spawnBlockHeight;
	}
	public void setSpawnBlockHeight(int spawnBlockHeight) {
		this.spawnBlockHeight = spawnBlockHeight;
	}
	public int getSpawnBlockWidth() {
		return spawnBlockWidth;
	}
	public void setSpawnBlockWidth(int spawnBlockWidth) {
		this.spawnBlockWidth = spawnBlockWidth;
	}
	public Corner getSpawnBlockCorner() {
		return spawnBlockCorner;
	}
	public void setSpawnBlockCorner(Corner spawnBlockCorner) {
		this.spawnBlockCorner = spawnBlockCorner;
	}
	public int[] getSpawnBlockRange() {
		return spawnBlockRange;
	}
	public void setSpawnBlockRange(int[] spawnBlockRange) {
		this.spawnBlockRange = spawnBlockRange;
	}


	
}