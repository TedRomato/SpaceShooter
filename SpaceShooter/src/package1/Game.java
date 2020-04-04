
package package1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Game extends JPanel{
	int mainHeight = 1080,mainWidth = 1920;
	protected Player p;
	public static JLabel scoreDisplay;
	protected int score = 0;
	private boolean ShowScore;
	protected int currentScreenWidth;
	protected int currentScreenHeight;
	public static double screenRatio;
	private RandomMeteorGenerator randomMeteorGenerator = new RandomMeteorGenerator();
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
	private boolean WasCalled = false;
	private Meteor[] meteors;
	private Summoner[] summoners;
	
	private boolean wasCalled = false;
	//public static JPanel gp = new GamePanel();
	public static boolean running = false;
	private int Count = 0;
	
	public Game(int sw,int sh) {
		this.currentScreenHeight = sh;
		this.currentScreenWidth = sw;
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
		arrayList = new GameObject[][] {objects, reflectableObs, reflectingObs, livingObsReflectUpdate, borderSensitive, aiVisible, ais, meteors, shootingObs,summoners};

	    Corner rightBotC = new Corner(new double[] {mainWidth,mainHeight}, new double[] {500,400});
	    Corner leftBotC = new Corner(new double[] {0,mainHeight}, new double[] {500,400});
	    Corner rightTopC = new Corner(new double[] {mainWidth,0}, new double[] {500,400});
	    Corner leftTopC = new Corner(new double[] {0,0}, new double[] {500,400});
	    
	    GameObject rightBorder = new GameObject(new Corner[] {rightTopC, rightBotC}, new double[] {500,400}, 0);
	    GameObject leftBorder = new GameObject(new Corner[] {leftTopC, leftBotC}, new double[] {500,400}, 0);
	    GameObject topBorder = new GameObject(new Corner[] {rightTopC, leftTopC}, new double[] {500,400}, 0);
	    GameObject botBorder = new GameObject(new Corner[] {leftBotC, rightBotC}, new double[] {500,400}, 0);
	    
	    borders = new GameObject[] {botBorder,leftBorder,topBorder,rightBorder};
	    
	    
	    p = Player.makeNewPlayer(new double[] {100,100});
		addObToGame(p, new int[] {5,6,7,9}); 

		screenRatio = (double)currentScreenWidth/(double)mainWidth;
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
		
	}
	public void keyReleased(KeyEvent e) {
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
            this.repaint();
            while(delta >=1)
            	{
                tick();
                delta--;
                }
                
                frames++;

                if(System.currentTimeMillis() - timer > 1000)
                { 
                	timer += 1000;
                    //System.out.println("FPS: "+ frames);

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
		handleShooting();	
		checkAndHandleCollision();
    	updateLivingObsReflect();
    	checkAndHandleAllRefs();
        deleteNoHpObs();
    	updateAllInvs();
        reflectFromSides();
        removeObsOut();
		updateAllObs();
		handleAis();
		handleSummoners();
	}
	
	

	
	private void handleAis() {
		for(AI ai : ais) {
			ai.updateAI(p, aiVisible, ais);
		}
	}

	private void handleShooting(){
		for(LivingObject sob : shootingObs ) {	
			if(sob.getAttachments() != null && sob.getAttachments().length > 0) {
				for(ObjectAttachment att : sob.getAttachments()) {
					if(att instanceof InteractiveAttachment) {
						if(sob instanceof AI) {
							if(((InteractiveAttachment)att).reloadLenght == ((InteractiveAttachment)att).reloadTimer && ((InteractiveAttachment)att).shoot(((AI)sob).getGoalDestination()) !=  null) {
								addObToGame(((InteractiveAttachment)att).shoot(((AI)sob).getGoalDestination()), new int[] {1,2,3,4,6,7,8,9});
								((InteractiveAttachment)att).setReloadLenght(0);
							}
						}
						else if(((InteractiveAttachment)att).reloadLenght == ((InteractiveAttachment)att).reloadTimer && ((InteractiveAttachment)att).shoot() !=  null) {
							addObToGame(((InteractiveAttachment)att).shoot(), new int[] {1,2,3,4,6,7,8,9});
							((InteractiveAttachment)att).setReloadLenght(0);
						}
						if(((InteractiveAttachment)att).reloadLenght != ((InteractiveAttachment)att).reloadTimer) { 
							((InteractiveAttachment)att).reloadLenght++;
						}
					}
				}
			}
		}
	}

	protected void removeObsOut() {
		for(GameObject ob : objects) {
			if(ob.checkIfOutsideRect(-600, -600,mainWidth + 800, mainHeight + 800)) {
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
					if(ob.getClass().getSimpleName().equals("LivingObject") || ob.getClass().getSimpleName().equals("Player")) {
						if(((LivingObject) ob).getAttachments()!= null) {
							if(((LivingObject) ob).getAttachments().length > 0) {
								for(ObjectAttachment att : ((LivingObject) ob).getAttachments()) {
									mob.checkAndHandleReflect(att);
								}
							}
						}
					}
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
							if(compareArray[x] instanceof Missile) {
								if(objects[i] instanceof Missile) {
									((Missile)objects[i]).handleMissileCollision((Missile)compareArray[x]);
								}else {
									objects[i].setHP(objects[i].getHP()-((Missile) compareArray[x]).getDmg());

								}
							}else {
								objects[i].setHP(objects[i].getHP()-1);
							}
							objects[i].startInvulnurability();
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
	
	private void deleteNoHpObs() {
		for(GameObject ob : objects) {
			if(ob.getHP() <= 0) {
				removeObFromGame(ob);
			}
		}
		
	}
	
	private void reflectFromSides() {
		for(int i = 0; i < borders.length; i++) {
			for(MovingObject go : borderSensitive) {
				if(go.getClass().getSimpleName().equals("Meteor") ) {
					if(go.checkCollision(borders[i])) {
						((Meteor) go).reflectMeteorFromSide(i,go.getRotationPoint());
					}
				}else {
					go.checkAndHandleReflect(borders[i]);

				}
			}
		}
	}
	
	protected void handleSummoners() {
		for(Summoner s : summoners) {
			AI ai = s.handleSummoner();
			if(ai != null) {
				addObToGame(ai, new int[] {7,4,9});
			}
		}
	}
	
	
	protected void respawnMeteorsToAmount(int amount) {
		if(meteors.length < amount) {
			addObToGame(randomMeteorGenerator.generateRandomMeteorOutside(mainWidth, mainHeight), new int[] {3,6,4,8,9});
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
		if(objects != null) {
			if(objects.length > 0) {
				for(GameObject ob : objects) {
					ob.render(g);
				}
			}
		}
		for(GameObject ob : borders) {
			ob.render(g);
		}
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

	}
	
}