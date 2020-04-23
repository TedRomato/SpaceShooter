
package package1;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Game extends JPanel implements MouseListener{

	int mainHeight = 1908,mainWidth = 3392;
	protected Player p;

	public static JLabel scoreDisplay, Warning;
	private BufferedImage WarningSign;
	protected int score = 0;
	private boolean ShowScore;
	public static int currentScreenWidth;
	public static int currentScreenHeight;
	public static double screenRatio;
	private RandomMeteorGenerator randomMeteorGenerator = new RandomMeteorGenerator();
	public static KeyChecker keyChecker = new KeyChecker();
	public static Camera camera;
	private GameObject[] borders;
	private GameObject[] objects;
	private MovingObject[] reflectableObs;
	private MovingObject[] reflectingObs;
	private LivingObject[] livingObsReflectUpdate;
	private LivingObject[] shootingObs; 
	private MovingObject[] borderSensitive;
	protected AI[] ais;
	private GameObject[][] arrayList;
	private GameObject[] aiVisible;
	private GameObject[] aiEnemys;
	private boolean WasCalled = false;
	private Meteor[] meteors;
	private Summoner[] summoners;
	private Explosives[] explosives;
	boolean softBorders = false;
	
	private boolean wasCalled = false;
	//public static JPanel gp = new GamePanel();
	public static boolean running = false;
	private int Count = 0;
	
	public Game(int sw,int sh,boolean softBorder) {
		this.currentScreenHeight = sh;
		this.currentScreenWidth = sw;
		this.softBorders = softBorder;
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
	    
	    try {
			WarningSign = ImageIO.read(new File("src/Icons/Warning.png"));
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
	    
	    p = Player.makeNewPlayer(new double[] {100,100});
		addObToGame(p, new int[] {5,6,7,9,11}); 

		screenRatio = (double)currentScreenWidth/(double)mainWidth;
		camera = new Camera(currentScreenWidth,currentScreenHeight,1);
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
        double amountOfTicks = 60;
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
              //      System.out.println("FPS: "+ frames);

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
	//	respawnMeteorsToAmount(5);
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

	}
	
	public void updatePlayer() {
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
	
	public void activateShieldFor(GameObject go, int radius, int HP) {
		Shield s = Shield.makeShield(go.getRotationPoint(), radius);
		s.setHP(HP);
		addObToGame(s, new int[] {1,2,3,4,5,6,7,8,9,10,11});
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
	 
	public void updateDisplay() {
		if(p.checkIfOutsideRect(0, 0, mainWidth, mainHeight)) {
			Warning.setText("WARNING!");
			add(Warning);
		}
		else {
			remove(Warning);
		}
	}
	protected void handlePlayerOutsideSafeZone() {
		if(p.wasDamagedByZone == false) {
			if(p.getRotationPoint().getY() < 0) {
				p.setHP(p.getHP() - getHPToSubtract(getDifferenceFromZero(p.getRotationPoint().getY())));
				p.wasDamagedByZone = true;
			}else if(p.getRotationPoint().getY() > mainHeight) {
				p.setHP(p.getHP() - getHPToSubtract(getDownOrRightDifference(p.getRotationPoint().getY(),mainHeight)));
				p.wasDamagedByZone = true;
			}
			if(p.getRotationPoint().getX() < 0) {
				p.setHP(p.getHP() - getHPToSubtract(getDifferenceFromZero(p.getRotationPoint().getX())));
				p.wasDamagedByZone = true;
			}else if(p.getRotationPoint().getX() > mainWidth) {
				p.setHP(p.getHP() - getHPToSubtract(getDownOrRightDifference(p.getRotationPoint().getX(),mainWidth)));
				p.wasDamagedByZone = true;
			}
		}
	}
	
	private double getDownOrRightDifference(double xy, double widthHeight) {
		return xy-widthHeight;
	}
	private double getDifferenceFromZero(double xy) {
		return Math.abs(xy);
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
						else if(sob instanceof AI) {
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
							att.setReloadTimer(att.getReloadTimer()+1);
						}
					}
				}
			}
		}
	}

	protected void removeObsOut() {
		for(GameObject ob : objects) {
			if(ob.checkIfOutsideRect(-3000, -3000,mainWidth + 6000, mainHeight + 6000)) {
				removeObFromGame(ob);
			}
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
			for(MovingObject ob : reflectingObs) {
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
						if(objects[i].getInvulnurability() == false) {
							objects[i].handleCollision(compareArray[x]);
							}
								
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
	
	private void deleteNoHpObs() {
		for(GameObject ob : objects) {
			if(ob.getHP() <= 0) {
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
			addObToGame(randomMeteorGenerator.generateRandomMeteorOutside(mainWidth, mainHeight), new int[] {3,6,4,8,9,11});
		}
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
	    reflectingObs = makeGameObArMovingArr(arrayList[2]);
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
	
	private MovingObject[] makeGameObArMovingArr(GameObject[] arr) {
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
	
	private GameObject[] makeNewArrayWith(GameObject[] arr, GameObject ob) {
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
		
	private GameObject[] makeNewArrayWithout(GameObject[] array ,int index){
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
		int width = 2000;
		g.setColor(Color.pink);
		fillRect(g,0-width, 1 - width, currentScreenWidth + 2*width, width );
		fillRect(g,0 - width, -10, width, currentScreenHeight+20);
		fillRect(g,0-width, currentScreenHeight-1, currentScreenWidth + 2*width, width );
		fillRect(g,currentScreenWidth,-10,width,currentScreenHeight+20);
		g.setColor(Color.black);

	}
	
	private void fillRect(Graphics g,int x, int y, int width, int height) {
		g.fillRect((int)Math.round(x*camera.getZoom()+camera.toAddX()), (int)Math.round(y*camera.getZoom()+camera.toAddY()), (int)Math.round(width*camera.getZoom()),(int)Math.round( height*camera.getZoom()));
	}
	
	public GameObject[] getAiEnemys() {
		return aiEnemys;
	}
	
	
	public Dimension getPreferredSize() {
		return new Dimension(1920,1080);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderAll(g2);
		if(p.checkIfOutsideRect(0, 0, mainWidth, mainHeight)) {
			g2.drawImage(WarningSign,currentScreenWidth/2-260, currentScreenHeight/2-200,100,100, null);
			g2.drawImage(WarningSign,currentScreenWidth/2+150, currentScreenHeight/2-200,100,100, null);
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
	
}