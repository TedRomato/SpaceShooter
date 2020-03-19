package package1;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Game extends JPanel{
	protected int screenWidth;
	protected int screenHeight;
	private RandomMeteorGenerator randomMeteorGenerator = new RandomMeteorGenerator();
	private GameObject[] borders;
	private GameObject[] objects;
	private MovingObject[] reflectableObs;
	private MovingObject[] reflectingObs;
	private LivingObject[] livingObsReflectUpdate;
	private MovingObject[] borderSensitive;
	private AI[] ais;
	private GameObject[][] arrayList;
	private GameObject[] aiVisible;
	private Meteor[] meteors;
	
	private boolean wasCalled = false;
	//public static JPanel gp = new GamePanel();
	private boolean running = true;
	
	public Game(int sw,int sh) {
		this.screenHeight = sh;
		this.screenWidth = sw;
		objects = new GameObject[] {};
	    reflectableObs = new MovingObject[] {};
	    reflectingObs = new MovingObject[] {};
	    livingObsReflectUpdate = new LivingObject[] {};
	    borderSensitive = new MovingObject[] {};
	    aiVisible = new GameObject[] {};
	    ais = new AI[] {};
	    meteors = new Meteor[] {};
		arrayList = new GameObject[][] {objects, reflectableObs, reflectingObs, livingObsReflectUpdate, borderSensitive, aiVisible, ais, meteors};

	    
	    Corner rightBotC = new Corner(new double[] {screenWidth,screenHeight}, new double[] {500,400});
	    Corner leftBotC = new Corner(new double[] {0,screenHeight}, new double[] {500,400});
	    Corner rightTopC = new Corner(new double[] {screenWidth,0}, new double[] {500,400});
	    Corner leftTopC = new Corner(new double[] {0,0}, new double[] {500,400});
	    
	    GameObject rightBorder = new GameObject(new Corner[] {rightTopC, rightBotC}, new double[] {500,400}, 0);
	    GameObject leftBorder = new GameObject(new Corner[] {leftTopC, leftBotC}, new double[] {500,400}, 0);
	    GameObject topBorder = new GameObject(new Corner[] {rightTopC, leftTopC}, new double[] {500,400}, 0);
	    GameObject botBorder = new GameObject(new Corner[] {leftBotC, rightBotC}, new double[] {500,400}, 0);
	    
	    borders = new GameObject[] {botBorder,leftBorder,topBorder,rightBorder};
	    
	    	    

	   
	    
	    
	    
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
                  //  System.out.println("FPS: "+ frames);
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
		checkAndHandleCollision();
    	updateLivingObsReflect();
    	checkAndHandleAllRefs();
        deleteNoHpObs();
    	updateAllInvs();
        reflectFromSides();
        removeObsOut();
		updateAllObs();

	}
	
	protected void removeObsOut() {
		for(GameObject ob : objects) {
			if(ob.checkIfOutsideRect(-300, -300,screenWidth + 800, screenHeight + 800)) {
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
	
	private void checkAndHandleCollision() {
		GameObject[] compareArray = objects; 
		for(int i = 0; i < objects.length; i++) {
			for(int x = 0; x < compareArray.length; x++) {
				
				if(objects[i] != compareArray[x]) {
					
					if(objects[i].checkCollision(compareArray[x])) {
						if(objects[i].getInvulnurability() == false) {
							objects[i].setHP(objects[i].getHP()-1);
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
	
	protected void respawnMeteorsToAmount(int amount) {
		if(meteors.length < amount) {
			addObToGame(randomMeteorGenerator.generateRandomMeteorOutside(screenWidth, screenHeight), new int[] {3,6,4});
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